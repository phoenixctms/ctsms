package org.phoenixctms.ctsms.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.velocity.util.StringUtils;

public class VelocityStringUtils extends StringUtils {

	public boolean isEmpty(String string) {
		return CommonUtil.isEmptyString(string);
	}

	public String urlEncode(String s, String enc) throws UnsupportedEncodingException {
		return URLEncoder.encode(s, enc);
	}
}
