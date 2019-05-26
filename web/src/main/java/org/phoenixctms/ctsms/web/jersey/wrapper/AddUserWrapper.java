package org.phoenixctms.ctsms.web.jersey.wrapper;

import java.io.Serializable;

import org.phoenixctms.ctsms.vo.UserInVO;

public class AddUserWrapper implements Serializable {

	private String plainDepartmentPassword;
	private UserInVO user;

	public String getPlainDepartmentPassword() {
		return plainDepartmentPassword;
	}

	public UserInVO getUser() {
		return user;
	}

	public void setPlainDepartmentPassword(String plainDepartmentPassword) {
		this.plainDepartmentPassword = plainDepartmentPassword;
	}

	public void setUser(UserInVO user) {
		this.user = user;
	}
}