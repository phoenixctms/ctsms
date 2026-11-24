package org.phoenixctms.ctsms.web.conversion;

import org.phoenixctms.ctsms.vo.ECRFSectionProgressVO;

public class EcrfSectionProgressItemValue extends ECRFSectionProgressVO {

	private Long selectionIndex;

	public EcrfSectionProgressItemValue() {
		super();
	}

	public EcrfSectionProgressItemValue(ECRFSectionProgressVO copy) {
		super(copy);
	}

	public Long getSelectionIndex() {
		return selectionIndex;
	}

	public void setSelectionIndex(Long selectionIndex) {
		this.selectionIndex = selectionIndex;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ECRFSectionProgressVO)) {
			return false;
		}
		ECRFSectionProgressVO other = (ECRFSectionProgressVO) obj;
		if (section == null) {
			if (other.getSection() != null) {
				return false;
			}
		} else if (!section.equals(other.getSection())) {
			return false;
		}
		Long otherSelectionIndex = (other instanceof EcrfSectionProgressItemValue) ? ((EcrfSectionProgressItemValue) other).getSelectionIndex() : null;
		if (selectionIndex == null) {
			if (otherSelectionIndex != null) {
				return false;
			}
		} else if (!selectionIndex.equals(otherSelectionIndex)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((section == null) ? 0 : section.hashCode());
		result = 31 * result + ((selectionIndex == null) ? 0 : selectionIndex.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return ECRFSectionProgressConverter.toConverterKey(this);
	}
}
