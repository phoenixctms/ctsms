package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel.OpenAiChatModelBuilder;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolArgumentsErrorHandler;
import dev.langchain4j.service.tool.ToolExecutionErrorHandler;

/**
 * Runs the query agent against the OpenAI-compatible chat endpoint configured by query_text_llm_url.
 */
public final class QueryTextAgent {

	// local servers ignore the key, but the OpenAI client wants one
	private static final String NO_API_KEY = "none";
	private static final Logger logger = LoggerFactory.getLogger(QueryTextAgent.class);
	private static final int STEP_TEXT_LENGTH = 300;
	private static final String START_ATTRIBUTE = "start";
	private static final String THREAD_NAME = "query-text-agent";

	/**
	 * Reports each model round to the run and the log: its duration, tokens, reasoning size, tool calls and the tool results it was sent with.
	 */
	private static final class RoundListener implements ChatModelListener {

		private static String abbreviate(String text) {
			if (text == null) {
				return "";
			}
			String flat = text.replaceAll("\s+", " ").trim();
			return flat.length() > STEP_TEXT_LENGTH ? flat.substring(0, STEP_TEXT_LENGTH) + "..." : flat;
		}

		private static String elapsed(Map<Object, Object> attributes) {
			Object start = attributes.get(START_ATTRIBUTE);
			return start instanceof Long ? String.format("%.1fs", (System.currentTimeMillis() - (Long) start) / 1000.0) : "?";
		}

		private final QueryTextRun run;
		private int round;

		private RoundListener(QueryTextRun run) {
			this.run = run;
		}

		private void step(String step) {
			run.addStep(step);
			logger.info("query agent {}", step);
		}

		@Override
		public void onRequest(ChatModelRequestContext context) {
			context.attributes().put(START_ATTRIBUTE, System.currentTimeMillis());
			List<ChatMessage> messages = context.chatRequest().messages();
			// the tool results of the previous round are the trailing messages
			int first = messages.size();
			while (first > 0 && messages.get(first - 1) instanceof ToolExecutionResultMessage) {
				first--;
			}
			for (int i = first; i < messages.size(); i++) {
				ToolExecutionResultMessage result = (ToolExecutionResultMessage) messages.get(i);
				step("   " + result.toolName() + " -> " + abbreviate(result.text()));
			}
		}

		@Override
		public void onResponse(ChatModelResponseContext context) {
			ChatResponse response = context.chatResponse();
			AiMessage message = response.aiMessage();
			StringBuilder line = new StringBuilder();
			line.append('#').append(++round).append(' ').append(elapsed(context.attributes()));
			line.append(", thinking ").append(message.thinking() == null ? 0 : message.thinking().length()).append(" chars");
			TokenUsage usage = response.tokenUsage();
			if (usage != null) {
				line.append(", tokens ").append(usage.inputTokenCount()).append('/').append(usage.outputTokenCount());
			}
			if (message.hasToolExecutionRequests()) {
				Iterator<ToolExecutionRequest> it = message.toolExecutionRequests().iterator();
				while (it.hasNext()) {
					ToolExecutionRequest request = it.next();
					line.append("\n   ").append(request.name()).append(' ').append(abbreviate(request.arguments()));
				}
			} else {
				line.append(", finish ").append(response.finishReason());
				if (!CommonUtil.isEmptyString(message.text())) {
					line.append("\n   answer: ").append(abbreviate(message.text()));
				}
			}
			step(line.toString());
		}

		@Override
		public void onError(ChatModelErrorContext context) {
			step("#" + (round + 1) + " " + elapsed(context.attributes()) + ", failed: " + context.error());
		}
	}

	private QueryTextAgent() {
	}

	public static boolean isConfigured() {
		return !CommonUtil.isEmptyString(getUrl());
	}

	private static String getUrl() {
		return Settings.getString(SettingCodes.QUERY_TEXT_LLM_URL, Bundle.SETTINGS, DefaultSettings.QUERY_TEXT_LLM_URL);
	}

	private static ChatModel createChatModel(QueryTextRun run) {
		int timeout = Settings.getInt(SettingCodes.QUERY_TEXT_LLM_TIMEOUT_MS, Bundle.SETTINGS, DefaultSettings.QUERY_TEXT_LLM_TIMEOUT_MS);
		if (timeout <= 0) {
			timeout = DefaultSettings.QUERY_TEXT_LLM_TIMEOUT_MS;
		}
		String apiKey = Settings.getString(SettingCodes.QUERY_TEXT_LLM_API_KEY, Bundle.SETTINGS, DefaultSettings.QUERY_TEXT_LLM_API_KEY);
		String model = Settings.getString(SettingCodes.QUERY_TEXT_LLM_MODEL, Bundle.SETTINGS, DefaultSettings.QUERY_TEXT_LLM_MODEL);
		OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
				.baseUrl(getUrl().trim())
				.apiKey(CommonUtil.isEmptyString(apiKey) ? NO_API_KEY : apiKey.trim())
				.timeout(Duration.ofMillis(timeout))
				.temperature(Settings.getDouble(SettingCodes.QUERY_TEXT_LLM_TEMPERATURE, Bundle.SETTINGS, DefaultSettings.QUERY_TEXT_LLM_TEMPERATURE))
				.maxRetries(0)
				// only read for the step log, it is not sent back to the model
				.returnThinking(true)
				.listeners(Collections.singletonList(new RoundListener(run)));
		if (!CommonUtil.isEmptyString(model)) {
			builder.modelName(model.trim());
		}
		return builder.build();
	}

	/**
	 * Starts the agent in a background thread. The outcome lands in the run, which the view polls.
	 */
	public static void start(String instructions, String request, QueryTextTools tools, QueryTextRun run) {
		int maxToolCalls = Settings.getInt(SettingCodes.QUERY_TEXT_LLM_MAX_TOOL_CALLS, Bundle.SETTINGS, DefaultSettings.QUERY_TEXT_LLM_MAX_TOOL_CALLS);
		if (maxToolCalls <= 0) {
			maxToolCalls = DefaultSettings.QUERY_TEXT_LLM_MAX_TOOL_CALLS;
		}
		QueryTextAssistant assistant = AiServices.builder(QueryTextAssistant.class)
				.chatModel(createChatModel(run))
				.tools(tools)
				.systemMessageProvider(memoryId -> instructions)
				.maxSequentialToolsInvocations(maxToolCalls)
				// bad tool arguments go back to the model, the tools report their own errors as results
				.toolArgumentsErrorHandler(ToolArgumentsErrorHandler.sendExceptionMessageToLlm())
				.toolExecutionErrorHandler(ToolExecutionErrorHandler.failInvocationUnlessVisibleToLlm())
				.build();
		Thread thread = new Thread(() -> {
			try {
				run.done(assistant.generate(request), tools.getLastValidText());
			} catch (Exception e) {
				logger.warn("query agent failed", e);
				run.failed(e.getMessage() != null ? e.getMessage() : e.getClass().getName());
			}
		}, THREAD_NAME);
		thread.setDaemon(true);
		run.setThread(thread);
		thread.start();
	}
}
