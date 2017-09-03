package org.phoenixctms.ctsms.web.conversion;

import org.phoenixctms.ctsms.vo.ECRFSectionProgressVO;


public class EcrfSectionProgressItemValue extends ECRFSectionProgressVO {

	public EcrfSectionProgressItemValue( ) {
		super();
	}

	public EcrfSectionProgressItemValue(ECRFSectionProgressVO copy) {
		super(copy);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
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
		return true;
	}

	@Override
	public String toString()
	{
		return this.getSection();
	}
}
