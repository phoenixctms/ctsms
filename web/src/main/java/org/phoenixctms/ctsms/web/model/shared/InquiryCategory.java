package org.phoenixctms.ctsms.web.model.shared;

public class InquiryCategory {

	private String category;
	private String categoryLocalized;
	private int rowIndex;

	public InquiryCategory(String category, int rowIndex) {
		this.category = category;
		this.rowIndex = rowIndex;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		InquiryCategory other = (InquiryCategory) obj;
		if (category == null) {
			if (other.category != null) {
				return false;
			}
		} else if (!category.equals(other.category)) {
			return false;
		}
		return true;
	}

	public String getCategory() {
		return category;
	}

	/**
	 * The translated category, for display only: equality is by the category as entered.
	 */
	public String getCategoryLocalized() {
		return categoryLocalized != null ? categoryLocalized : category;
	}

	public void setCategoryLocalized(String categoryLocalized) {
		this.categoryLocalized = categoryLocalized;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		return result;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
}
