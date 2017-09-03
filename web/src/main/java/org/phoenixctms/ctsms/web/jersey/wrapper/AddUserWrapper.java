package org.phoenixctms.ctsms.web.jersey.wrapper;

import java.io.Serializable;

import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.phoenixctms.ctsms.vo.UserInVO;

public class AddUserWrapper implements Serializable {

	private String plainDepartmentPassword;
	private UserInVO user;
	private PasswordInVO password;

	public PasswordInVO getPassword() {
		return password;
	}

	public String getPlainDepartmentPassword() {
		return plainDepartmentPassword;
	}

	public UserInVO getUser() {
		return user;
	}

	public void setPassword(PasswordInVO password) {
		this.password = password;
	}

	public void setPlainDepartmentPassword(String plainDepartmentPassword) {
		this.plainDepartmentPassword = plainDepartmentPassword;
	}

	public void setUser(UserInVO user) {
		this.user = user;
	}
}
