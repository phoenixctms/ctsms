package org.phoenixctms.ctsms.web.model.inputfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.InputFieldL10nBundle;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InputFieldL10nMissingKeyVO;
import org.phoenixctms.ctsms.vo.InputFieldL10nTextInVO;
import org.phoenixctms.ctsms.vo.InputFieldL10nTextOutVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

/**
 * The localization tab of an input field: the editable localization texts of all bundles in one locale,
 * and the keys of the input field (and its usages) that have no text in that locale.
 */
@ManagedBean
@ViewScoped
public class InputFieldL10nBean extends ManagedBeanBase {

	/**
	 * The text of one bundle as loaded into the editor, and as edited.
	 */
	public static class L10nTextEditor {

		private final InputFieldL10nBundle bundle;
		private String text;
		private String originalText;
		private Long version;
		private InputFieldL10nTextOutVO out;

		private L10nTextEditor(InputFieldL10nBundle bundle) {
			this.bundle = bundle;
		}

		public InputFieldL10nBundle getBundle() {
			return bundle;
		}

		public String getBundleName() {
			return bundle.name();
		}

		public String getModifiedAnnotation() {
			if (out != null) {
				return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
			}
			return "";
		}

		public InputFieldL10nTextOutVO getOut() {
			return out;
		}

		public String getText() {
			return text;
		}

		private void load(InputFieldL10nTextOutVO out) {
			this.out = out;
			text = out != null ? out.getText() : "";
			originalText = text;
			version = out != null ? out.getVersion() : null;
		}

		public void setText(String text) {
			this.text = text;
		}
	}

	private final static String ROOT_LOCALE = "";
	private final static String ROOT_LANGUAGE = Locale.ENGLISH.getLanguage();
	private Long inputFieldId;
	private String locale;
	private ArrayList<SelectItem> locales;
	private ArrayList<L10nTextEditor> editors;
	private Collection<InputFieldL10nMissingKeyVO> missingKeys;

	public InputFieldL10nBean() {
		super();
		locale = ROOT_LOCALE;
		editors = new ArrayList<L10nTextEditor>();
		for (InputFieldL10nBundle bundle : InputFieldL10nBundle.values()) {
			editors.add(new L10nTextEditor(bundle));
		}
		missingKeys = new ArrayList<InputFieldL10nMissingKeyVO>();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
		}
	}

	@Override
	protected String changeAction(Long id) {
		inputFieldId = id;
		loadTexts();
		return CHANGE_OUTCOME;
	}

	@Override
	public String loadAction() {
		return loadAction(inputFieldId);
	}

	@Override
	public String loadAction(Long id) {
		inputFieldId = id;
		loadTexts();
		return LOAD_OUTCOME;
	}

	/**
	 * Reloads the texts of the selected locale (locale dropdown).
	 */
	public void changeLocale() {
		loadTexts();
		appendRequestContextCallbackArgs(true);
	}

	/**
	 * The key escaped as in a .properties text (whitespace, separators, comment characters and backslashes),
	 * followed by "=", to paste as the start of a line into an editor.
	 */
	public String getKeyLine(String key) {
		StringBuilder line = new StringBuilder();
		if (key != null) {
			for (int i = 0; i < key.length(); i++) {
				char c = key.charAt(i);
				switch (c) {
					case '\t':
						line.append("\\t");
						break;
					case '\n':
						line.append("\\n");
						break;
					case '\r':
						line.append("\\r");
						break;
					case '\f':
						line.append("\\f");
						break;
					case ' ':
					case '=':
					case ':':
					case '#':
					case '!':
					case '\\':
						line.append('\\').append(c);
						break;
					default:
						line.append(c);
						break;
				}
			}
		}
		return line.append('=').toString();
	}

	// a StringBuilder, as the JSF 2.1 annotation scanner may miss a bean with string concatenation (invokedynamic), see JsfAnnotationScanCheck:
	private static String getBundleMessageCode(String prefix, InputFieldL10nBundle bundle) {
		return new StringBuilder(prefix).append(bundle.name().toLowerCase()).toString();
	}

	public String getBundleLabel(InputFieldL10nBundle bundle) {
		return bundle != null ? Messages.getString(getBundleMessageCode(MessageCodes.INPUT_FIELD_L10N_BUNDLE_LABEL_PREFIX, bundle)) : "";
	}

	public int getEditorIndex(InputFieldL10nBundle bundle) {
		for (int i = 0; i < editors.size(); i++) {
			if (editors.get(i).getBundle().equals(bundle)) {
				return i;
			}
		}
		return -1;
	}

	public String getAddKeyButtonTitle(InputFieldL10nBundle bundle) {
		return Messages.getMessage(MessageCodes.INPUT_FIELD_L10N_ADD_KEY_BUTTON_TITLE, getBundleTabTitle(bundle));
	}

	/**
	 * The label of an editor, naming the language of its texts (e.g. "Global list of eCRF section texts in German:").
	 */
	public String getBundleEditorLabel(InputFieldL10nBundle bundle) {
		return bundle != null ? Messages.getMessage(getBundleMessageCode(MessageCodes.INPUT_FIELD_L10N_BUNDLE_EDITOR_LABEL_PREFIX, bundle), getEditorLocaleLabel()) : "";
	}

	private String getEditorLocaleLabel() {
		if (ROOT_LOCALE.equals(locale)) {
			return Messages.getString(MessageCodes.INPUT_FIELD_L10N_EDITOR_LABEL_ROOT_LOCALE);
		}
		String name = locale;
		Iterator<SelectItem> it = getLocales().iterator();
		while (it.hasNext()) {
			SelectItem item = it.next();
			if (locale.equals(item.getValue())) {
				name = item.getLabel();
				break;
			}
		}
		return Messages.getMessage(MessageCodes.INPUT_FIELD_L10N_EDITOR_LABEL_LOCALE, name);
	}

	public String getBundleTabTitle(InputFieldL10nBundle bundle) {
		return bundle != null ? Messages.getString(getBundleMessageCode(MessageCodes.INPUT_FIELD_L10N_BUNDLE_TAB_TITLE_PREFIX, bundle)) : "";
	}

	public ArrayList<L10nTextEditor> getEditors() {
		return editors;
	}

	public String getLocale() {
		return locale;
	}

	public ArrayList<SelectItem> getLocales() {
		if (locales == null) {
			locales = new ArrayList<SelectItem>();
			// the default (root) texts are the English ones and the fallback of every language, so English has no entry of its own:
			locales.add(new SelectItem(ROOT_LOCALE, Messages.getString(MessageCodes.INPUT_FIELD_L10N_ROOT_LOCALE_LABEL)));
			Iterator<SelectItem> it = WebUtil.getLocales().iterator();
			while (it.hasNext()) {
				SelectItem locale = it.next();
				if (!ROOT_LANGUAGE.equals(locale.getValue())) {
					locales.add(locale);
				}
			}
		}
		return locales;
	}

	public Collection<InputFieldL10nMissingKeyVO> getMissingKeys() {
		return missingKeys;
	}

	public boolean isMissingKeysEmpty() {
		return missingKeys == null || missingKeys.isEmpty();
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.INPUT_FIELD_ID);
		if (id != null) {
			this.load(id);
		} else {
			loadTexts();
		}
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return inputFieldId != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated();
	}

	private void loadTexts() {
		Iterator<L10nTextEditor> it = editors.iterator();
		while (it.hasNext()) {
			loadText(it.next());
		}
		loadMissingKeys();
	}

	private void loadText(L10nTextEditor editor) {
		InputFieldL10nTextOutVO out = null;
		try {
			out = WebUtil.getServiceLocator().getInputFieldService().getInputFieldL10nText(WebUtil.getAuthentication(), editor.getBundle(), locale);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		editor.load(out);
	}

	private L10nTextEditor getEditor(String bundleName) {
		Iterator<L10nTextEditor> it = editors.iterator();
		while (it.hasNext()) {
			L10nTextEditor editor = it.next();
			if (editor.getBundleName().equals(bundleName)) {
				return editor;
			}
		}
		return null;
	}

	/**
	 * Reloads the text of one editor (its reload button), so unsaved edits in the other editors are kept.
	 */
	public void reload(String bundleName) {
		L10nTextEditor editor = getEditor(bundleName);
		if (editor != null) {
			loadText(editor);
			loadMissingKeys();
		}
		appendRequestContextCallbackArgs(editor != null);
	}

	private void loadMissingKeys() {
		missingKeys = new ArrayList<InputFieldL10nMissingKeyVO>();
		if (inputFieldId != null) {
			try {
				missingKeys = WebUtil.getServiceLocator().getInputFieldService().getInputFieldL10nMissingKeys(WebUtil.getAuthentication(), inputFieldId, locale);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
	}

	public final void save(String bundleName) {
		actionPostProcess(saveAction(bundleName));
	}

	private String saveAction(String bundleName) {
		L10nTextEditor editor = getEditor(bundleName);
		if (editor == null) {
			return ERROR_OUTCOME;
		}
		InputFieldL10nTextInVO in = new InputFieldL10nTextInVO();
		in.setBundle(editor.getBundle());
		in.setLocale(locale);
		in.setText(editor.getText());
		in.setOriginalText(editor.originalText);
		in.setVersion(editor.version);
		try {
			InputFieldL10nTextOutVO out = WebUtil.getServiceLocator().getInputFieldService().setInputFieldL10nText(WebUtil.getAuthentication(), in);
			Long previousVersion = editor.version;
			editor.load(out);
			if (out != null && (previousVersion == null || out.getVersion() != previousVersion.longValue())) {
				addOperationSuccessMessage(MessageCodes.INPUT_FIELD_L10N_TEXT_SAVED);
			} else {
				addOperationSuccessMessage(MessageCodes.INPUT_FIELD_L10N_TEXT_UNCHANGED);
			}
			loadMissingKeys();
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public void setLocale(String locale) {
		this.locale = locale != null ? locale : ROOT_LOCALE;
	}
}
