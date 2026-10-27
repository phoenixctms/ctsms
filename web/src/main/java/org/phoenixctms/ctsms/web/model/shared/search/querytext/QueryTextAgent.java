package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import java.time.Duration;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel.OpenAiChatModelBuilder;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolArgumentsErrorHandler;
import dev.langchain4j.service.tool.ToolExecutionErrorHandler;

/**
 * Runs the query agent against the OpenAI-compatible chat endpoint configured by query_text_llm_url.
 */
public final class QueryTextAgent {

	// local servers ignore the key, but the OpenAI client wants one
	private static final String NO_API_KEY = "none";

	private QueryTextAgent() {
	}

	public static boolean isConfigured() {
		return !CommonUtil.isEmptyString(getUrl());
	}

	private static String getUrl() {
		return Settings.getString(SettingCodes.QUERY_TEXT_LLM_URL, Bundle.SETTINGS, DefaultSettings.QUERY_TEXT_LLM_URL);
	}

	private static ChatModel createChatModel() {
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
				.maxRetries(0);
		if (!CommonUtil.isEmptyString(model)) {
			builder.modelName(model.trim());
		}
		return builder.build();
	}

	public static String generate(String instructions, String request, QueryTextTools tools) {
		int maxToolCalls = Settings.getInt(SettingCodes.QUERY_TEXT_LLM_MAX_TOOL_CALLS, Bundle.SETTINGS, DefaultSettings.QUERY_TEXT_LLM_MAX_TOOL_CALLS);
		if (maxToolCalls <= 0) {
			maxToolCalls = DefaultSettings.QUERY_TEXT_LLM_MAX_TOOL_CALLS;
		}
		QueryTextAssistant assistant = AiServices.builder(QueryTextAssistant.class)
				.chatModel(createChatModel())
				.tools(tools)
				.systemMessageProvider(memoryId -> instructions)
				.maxSequentialToolsInvocations(maxToolCalls)
				// bad tool arguments go back to the model, the tools report their own errors as results
				.toolArgumentsErrorHandler(ToolArgumentsErrorHandler.sendExceptionMessageToLlm())
				.toolExecutionErrorHandler(ToolExecutionErrorHandler.failInvocationUnlessVisibleToLlm())
				.build();
		return assistant.generate(request);
	}
}
