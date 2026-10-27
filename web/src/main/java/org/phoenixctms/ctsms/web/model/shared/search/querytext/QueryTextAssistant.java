package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import dev.langchain4j.service.UserMessage;

public interface QueryTextAssistant {

	String generate(@UserMessage String request);
}
