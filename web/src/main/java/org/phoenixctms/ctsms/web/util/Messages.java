package org.phoenixctms.ctsms.web.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.phoenixctms.ctsms.util.CommonUtil;

public final class Messages {

	private static final String DEFAULT_FACES_MESSAGE = "<message>";
	private static final ArrayList<String> DEFAULT_FACES_MESSAGE_LIST = new ArrayList<String>();
	public static final String MESSAGE_BUNDLE_DEFAULT = Settings.WEB_ROOT_PACKAGE_NAME + ".messages";
	private final static ArrayList<String> CRITERION_COMPONENT_CLIENT_ID_PREFIXES = new ArrayList<String>();
	static {
		CRITERION_COMPONENT_CLIENT_ID_PREFIXES.add("tie");
	}
	private final static ArrayList<String> INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES = new ArrayList<String>();
	static {
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("singleLineText");
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("multiLineText");
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("selectOne");
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("selectMany");
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("autocomplete");
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("checkBox");
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("integer");
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("float");
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("date");
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("timestamp");
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("time");
		// INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("sketch");
		INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.add("sketchpad");
		DEFAULT_FACES_MESSAGE_LIST.add(DEFAULT_FACES_MESSAGE);
	}
	public final static String DUMMY_INPUT_FIELD_ID = "dummy";

	public static void addCriterionMessages(Object data, FacesMessage.Severity severity, String message) {
		Long index;
		try {
			index = (Long) data;
		} catch (ClassCastException e) {
			index = null;
		}
		if (index != null) {
			Iterator<String> it = CRITERION_COMPONENT_CLIENT_ID_PREFIXES.iterator();
			while (it.hasNext()) {
				Messages.addMessageId(it.next() + Long.toString(index - CommonUtil.LIST_INITIAL_POSITION), severity, message);
			}
		} else {
			Messages.addMessage(severity, message);
		}
	}

	public static void addFieldInputMessages(String index, FacesMessage.Severity severity, String message) {
		if (index != null && index.length() > 0) {
			Iterator<String> it = INPUT_FIELD_COMPONENT_CLIENT_ID_PREFIXES.iterator();
			while (it.hasNext()) {
				Messages.addMessageId(it.next() + index, severity, message);
			}
		} else {
			Messages.addMessage(severity, message);
		}
	}

	public static void addLocalizedMessage(FacesMessage.Severity severity, String l10nKey) {
		FacesContext context = FacesContext.getCurrentInstance();
		addMessage(context, severity, CommonUtil.getString(l10nKey, getMessageResourceBundle(context), DEFAULT_FACES_MESSAGE));
	}

	public static void addLocalizedMessage(FacesMessage.Severity severity, String l10nKey, Object... args) {
		FacesContext context = FacesContext.getCurrentInstance();
		addMessage(context, severity, CommonUtil.getMessage(l10nKey, args, getMessageResourceBundle(context), DEFAULT_FACES_MESSAGE));
	}

	public static void addLocalizedMessageClientId(String clientId, FacesMessage.Severity severity, String l10nKey) {
		FacesContext context = FacesContext.getCurrentInstance();
		addMessageClientId(context, clientId, severity, CommonUtil.getString(l10nKey, getMessageResourceBundle(context), DEFAULT_FACES_MESSAGE));
	}

	public static void addLocalizedMessageClientId(String clientId, FacesMessage.Severity severity, String l10nKey, Object... args) {
		FacesContext context = FacesContext.getCurrentInstance();
		addMessageClientId(context, clientId, severity, CommonUtil.getMessage(l10nKey, args, getMessageResourceBundle(context), DEFAULT_FACES_MESSAGE));
	}

	public static void addLocalizedMessageId(String id, FacesMessage.Severity severity, String l10nKey) {
		FacesContext context = FacesContext.getCurrentInstance();
		addMessageId(context, id, severity, CommonUtil.getString(l10nKey, getMessageResourceBundle(context), DEFAULT_FACES_MESSAGE));
	}

	public static void addLocalizedMessageId(String id, FacesMessage.Severity severity, String l10nKey, Object... args) {
		FacesContext context = FacesContext.getCurrentInstance();
		addMessageId(context, id, severity, CommonUtil.getMessage(l10nKey, args, getMessageResourceBundle(context), DEFAULT_FACES_MESSAGE));
	}

	private static void addMessage(FacesContext context, FacesMessage.Severity severity, String message) {
		if (context != null) {
			FacesMessage facesMessage = new FacesMessage(severity, message, null);
			context.addMessage(null, facesMessage);
		}
	}

	public static void addMessage(FacesMessage.Severity severity, String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		addMessage(context, severity, message);
	}

	private static void addMessageClientId(FacesContext context, String clientId, FacesMessage.Severity severity, String message) {
		if (context != null) {
			FacesMessage facesMessage = new FacesMessage(severity, message, null);
			context.addMessage(clientId, facesMessage);
		}
	}

	public static void addMessageClientId(String clientId, FacesMessage.Severity severity, String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		addMessageClientId(context, clientId, severity, message);
	}

	private static void addMessageId(FacesContext context, String id, FacesMessage.Severity severity, String message) {
		if (context != null) {
			UIComponent component = WebUtil.findComponentById(context.getViewRoot(), id);
			FacesMessage facesMessage = new FacesMessage(severity, message, null);
			if (component == null) {
				context.addMessage(null, facesMessage);
			} else {
				context.addMessage(component.getClientId(context), facesMessage);
			}
		}
	}

	public static void addMessageId(String id, FacesMessage.Severity severity, String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		addMessageId(context, id, severity, message);
	}

	public static String getMessage(String l10nKey, Object... args) {
		FacesContext context = FacesContext.getCurrentInstance();
		return CommonUtil.getMessage(l10nKey, args, getMessageResourceBundle(context), DEFAULT_FACES_MESSAGE);
	}

	public static List<FacesMessage> getMessageList() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			return context.getMessageList();
		}
		return new ArrayList<FacesMessage>();
	}

	private static ResourceBundle getMessageResourceBundle(FacesContext context) {
		if (context == null) {
			return CommonUtil.getBundle(MESSAGE_BUNDLE_DEFAULT, Locale.getDefault());
		} else {
			Locale locale;
			try {
				locale = WebUtil.getLocale();
			} catch (Exception e) {
				locale = Locale.getDefault();
			}
			return CommonUtil.getBundle(context.getApplication().getMessageBundle(), locale);
		}
	}

	public static String getString(String l10nKey) {
		FacesContext context = FacesContext.getCurrentInstance();
		return CommonUtil.getString(l10nKey, getMessageResourceBundle(context), DEFAULT_FACES_MESSAGE);
	}

	public static ArrayList<String> getStringList(String l10nKey) {
		FacesContext context = FacesContext.getCurrentInstance();
		return CommonUtil.getValueStringList(l10nKey, getMessageResourceBundle(context), DEFAULT_FACES_MESSAGE_LIST);
	}

	private Messages() {
	}
}