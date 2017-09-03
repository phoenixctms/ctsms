package org.phoenixctms.ctsms.web.model.shared;

import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;



public class EcrfFieldSection {

	private String section;
	private int rowIndex;
	private Long seriesIndex;

	public EcrfFieldSection(String section, Long seriesIndex) {
		this.section = section;
		this.seriesIndex = seriesIndex;
		this.rowIndex = 0;
	}

	public EcrfFieldSection(String section, Long seriesIndex, int rowIndex) {
		this.section = section;
		this.seriesIndex = seriesIndex;
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
		EcrfFieldSection other = (EcrfFieldSection) obj;
		if (section == null) {
			if (other.section != null) {
				return false;
			}
		} else if (!section.equals(other.section)) {
			return false;
		}
		if (seriesIndex == null) {
			if (other.seriesIndex != null) {
				return false;
			}
		} else if (!seriesIndex.equals(other.seriesIndex)) {
			return false;
		}
		return true;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public String getSection() {
		return section;
	}

	public Long getSeriesIndex() {
		return seriesIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((section == null) ? 0 : section.hashCode());
		result = prime * result + ((seriesIndex == null) ? 0 : seriesIndex.hashCode());
		return result;
	}

	public boolean isSeries() {
		return seriesIndex != null;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public void setSeriesIndex(Long seriesIndex) {
		this.seriesIndex = seriesIndex;
	}

	public String toString() {
		if (isSeries()) {
			return Messages.getMessage(MessageCodes.ECRF_ENTRY_SECTION_SERIES_INDEX_LABEL, seriesIndex, section);
		} else {
			return Messages.getMessage(MessageCodes.ECRF_ENTRY_SECTION_LABEL, section);
		}
	}

}
