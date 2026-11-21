package org.phoenixctms.ctsms.web.model.inputfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

	public String getBundleLabel(InputFieldL10nBundle bundle) {
		return bundle != null ? Messages.getString(MessageCodes.INPUT_FIELD_L10N_BUNDLE_LABEL_PREFIX + bundle.name().toLowerCase()) : "";
	}

	public String getBundleTabTitle(InputFieldL10nBundle bundle) {
		return bundle != null ? Messages.getString(MessageCodes.INPUT_FIELD_L10N_BUNDLE_TAB_TITLE_PREFIX + bundle.name().toLowerCase()) : "";
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
			locales.add(new SelectItem(ROOT_LOCALE, Messages.getString(MessageCodes.INPUT_FIELD_L10N_ROOT_LOCALE_LABEL)));
			locales.addAll(WebUtil.getLocales());
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
			L10nTextEditor editor = it.next();
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
		loadMissingKeys();
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
		L10nTextEditor editor = null;
		Iterator<L10nTextEditor> it = editors.iterator();
		while (it.hasNext()) {
			L10nTextEditor candidate = it.next();
			if (candidate.getBundleName().equals(bundleName)) {
				editor = candidate;
				break;
			}
		}
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
