package org.phoenixctms.ctsms.web.jersey.wrapper;

import java.io.Serializable;

public class SetProbandCategoryWrapper implements Serializable {

	private String comment;
	private Long categoryId;
	private Long version;

	public Long getCategoryId() {
		return categoryId;
	}

	public String getComment() {
		return comment;
	}

	public Long getVersion() {
		return version;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}

