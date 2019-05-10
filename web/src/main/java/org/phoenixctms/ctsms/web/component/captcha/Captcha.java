/*
 * Generated, Do Not Modify
 */
/*
 * Copyright 2009-2012 Prime Teknoloji.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.phoenixctms.ctsms.web.component.captcha;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.util.MessageFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@FacesComponent("ctsms.Captcha")
public class Captcha extends UIInput {

	protected enum PropertyKeys {
		theme, language, tabindex, label, secure;

		String toString;

		PropertyKeys() {
		}

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	enum ReCaptchaVersions {
		V1, V2;
	}

	public static final String COMPONENT_TYPE = "ctsms.Captcha";
	private static final String DEFAULT_RENDERER = "ctsms.Captcha";
	public final static String INVALID_MESSAGE_ID = "primefaces.captcha.INVALID";
	public final static ReCaptchaVersions RECAPTCHA_VERSION = ReCaptchaVersions.V2;
	private static final Logger logger = Logger.getLogger(Captcha.class.getName());

	public Captcha() {
		setRendererType(DEFAULT_RENDERER);
	}

	private String createPostParameters(FacesContext facesContext, Verification verification) throws UnsupportedEncodingException {
		String remoteAddress = ((HttpServletRequest) facesContext.getExternalContext().getRequest()).getRemoteAddr();
		String privateKey = Settings.getString(SettingCodes.PRIVATE_CAPTCHA_KEY, Bundle.SETTINGS, DefaultSettings.PRIVATE_CAPTCHA_KEY);
		if (privateKey == null || privateKey.length() == 0) {
			throw new FacesException("Cannot find private key for catpcha, use PRIVATE_CAPTCHA_KEY property to define one");
		}
		StringBuilder postParams = new StringBuilder();
		if (ReCaptchaVersions.V1.equals(RECAPTCHA_VERSION)) {
			postParams.append("privatekey=").append(URLEncoder.encode(privateKey, "UTF-8"));
			postParams.append("&challenge=").append(URLEncoder.encode(verification.getChallenge(), "UTF-8"));
			postParams.append("&response=").append(URLEncoder.encode(verification.getAnswer(), "UTF-8"));
		} else if (ReCaptchaVersions.V2.equals(RECAPTCHA_VERSION)) {
			postParams.append("secret=").append(URLEncoder.encode(privateKey, "UTF-8"));
			postParams.append("&response=").append(URLEncoder.encode(verification.getToken(), "UTF-8"));
		} else {
			throw new FacesException("reCAPTCHA version not supported");
		}
		postParams.append("&remoteip=").append(URLEncoder.encode(remoteAddress, "UTF-8"));
		return postParams.toString();
	}

	@Override
	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public java.lang.String getLabel() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.label, null);
	}

	public java.lang.String getLanguage() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.language, "en");
	}

	public int getTabindex() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.tabindex, 0);
	}

	public java.lang.String getTheme() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.theme, "red");
	}

	public boolean isSecure() {
		return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.secure, false);
	}

	public void setLabel(java.lang.String _label) {
		getStateHelper().put(PropertyKeys.label, _label);
	}

	public void setLanguage(java.lang.String _language) {
		getStateHelper().put(PropertyKeys.language, _language);
	}

	public void setSecure(boolean _secure) {
		getStateHelper().put(PropertyKeys.secure, _secure);
	}

	public void setTabindex(int _tabindex) {
		getStateHelper().put(PropertyKeys.tabindex, _tabindex);
	}

	public void setTheme(java.lang.String _theme) {
		getStateHelper().put(PropertyKeys.theme, _theme);
	}

	@Override
	protected void validateValue(FacesContext context, Object value) {
		super.validateValue(context, value);
		if (isValid()) {
			Verification verification = (Verification) value;
			boolean isValid = false;
			OutputStream out = null;
			BufferedReader rd = null;
			StringBuilder errorCodes = new StringBuilder();
			try {
				URL url;
				if (ReCaptchaVersions.V1.equals(RECAPTCHA_VERSION)) {
					url = new URL("http://api-verify.recaptcha.net/verify");
				} else if (ReCaptchaVersions.V2.equals(RECAPTCHA_VERSION)) {
					url = new URL("https://www.google.com/recaptcha/api/siteverify");
				} else {
					throw new IllegalArgumentException("reCAPTCHA version not supported");
				}
				URLConnection conn = url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				String postBody = createPostParameters(context, verification);
				out = conn.getOutputStream();
				out.write(postBody.getBytes());
				out.flush();
				out.close();
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				if (ReCaptchaVersions.V1.equals(RECAPTCHA_VERSION)) {
					isValid = Boolean.valueOf(rd.readLine());
				} else if (ReCaptchaVersions.V2.equals(RECAPTCHA_VERSION)) {
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = rd.readLine()) != null) {
						sb.append(line);
					}
					// System.out.println(sb.toString());
					JsonObject response = WebUtil.parseJson(sb.toString()).getAsJsonObject();
					isValid = response.get("success").getAsBoolean();
					if (!isValid) {
						Iterator<JsonElement> errorCodesIt = WebUtil.parseJson(sb.toString()).getAsJsonObject().get("error-codes").getAsJsonArray().iterator();
						while (errorCodesIt.hasNext()) {
							errorCodes.append(errorCodesIt.next());
							if (errorCodesIt.hasNext()) {
								errorCodes.append(", ");
							}
						}
					}
				} else {
					throw new IllegalArgumentException("reCAPTCHA version not supported");
				}
				// rd.close();
			} catch (Exception exception) {
				throw new FacesException(exception);
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
				try {
					rd.close();
				} catch (Exception e) {
				}
			}
			if (!isValid) {
				setValid(false);
				String validatorMessage = getValidatorMessage();
				if (validatorMessage != null) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, validatorMessage, validatorMessage);
					context.addMessage(getClientId(context), msg);
				} else if (ReCaptchaVersions.V1.equals(RECAPTCHA_VERSION)) {
					Object[] params = new Object[2];
					params[0] = MessageFactory.getLabel(context, this);
					params[1] = verification.getAnswer();
					FacesMessage msg = MessageFactory.getMessage(Captcha.INVALID_MESSAGE_ID, FacesMessage.SEVERITY_ERROR, params);
					context.addMessage(getClientId(context), msg);
				} else if (ReCaptchaVersions.V2.equals(RECAPTCHA_VERSION)) {
					if (errorCodes.length() > 0) {
						Messages.addLocalizedMessageClientId(getClientId(context), FacesMessage.SEVERITY_ERROR, MessageCodes.CAPTCHA_ERROR_MESSAGE,
								MessageFactory.getLabel(context, this), errorCodes.toString());
					} else {
						Messages.addLocalizedMessageClientId(getClientId(context), FacesMessage.SEVERITY_ERROR, MessageCodes.CAPTCHA_NOT_VERIFIED_ERROR_MESSAGE,
								MessageFactory.getLabel(context, this));
					}
				} else {
					throw new FacesException("reCAPTCHA version not supported");
				}
			}
		}
	}
}