package org.phoenixctms.ctsms.web.jersey.wrapper;

import java.io.Serializable;

import org.phoenixctms.ctsms.vo.UserInVO;

public class UpdateUserWrapper implements Serializable {

	private String plainOldDepartmentPassword;
	private String plainNewDepartmentPassword;
	private UserInVO user;

	public UserInVO getUser() {
		return user;
	}

	public void setUser(UserInVO user) {
		this.user = user;
	}

	public String getPlainOldDepartmentPassword() {
		return plainOldDepartmentPassword;
	}

	public void setPlainOldDepartmentPassword(String plainOldDepartmentPassword) {
		this.plainOldDepartmentPassword = plainOldDepartmentPassword;
	}

	public String getPlainNewDepartmentPassword() {
		return plainNewDepartmentPassword;
	}

	public void setPlainNewDepartmentPassword(String plainNewDepartmentPassword) {
		this.plainNewDepartmentPassword = plainNewDepartmentPassword;
	}
}
