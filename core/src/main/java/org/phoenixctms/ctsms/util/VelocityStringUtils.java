package org.phoenixctms.ctsms.util;

import org.apache.velocity.util.StringUtils;

public class VelocityStringUtils extends StringUtils {

	public boolean isEmpty(String string) {
		return CommonUtil.isEmptyString(string);
	}

}
