package org.phoenixctms.ctsms.web.model;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class IDVO implements Comparable<IDVO> {

	private final static VOTransformation<Object, Object> PASSTHROUGH_VO_TRANSFORMATION = new VOTransformation<Object, Object>() {

		@Override
		public Object transform(Object vo) {
			return vo;
		}
	};

	public static abstract class VOTransformation<In, Out> {

		IDVO transformVo(In in) {
			return new IDVO(transform(in));
		}

		public abstract Out transform(In vo);
	}

	public static IDVO transformVo(Object object) {
		return PASSTHROUGH_VO_TRANSFORMATION.transformVo(object);
	}

	public static IDVO transformVo(Object object, VOTransformation transformation) {
		return transformation.transformVo(object);
	}

	public static void transformVoCollection(Collection<?> vos) {
		if (vos != null) {
			CollectionUtils.transform(vos, new Transformer() {

				@Override
				public Object transform(Object input) {
					return transformVo(input);
				}
			});
		}
	}

	public static void transformVoCollection(Collection<?> vos, VOTransformation transformation) {
		if (vos != null) {
			CollectionUtils.transform(vos, new Transformer() {

				@Override
				public Object transform(Object input) {
					return transformVo(input, transformation);
				}
			});
		}
	}

	private Object vo;
	private int rowIndex;
	private PSFVO psf;

	public IDVO() {
	}

	public IDVO(Object vo) {
		this.vo = vo;
	}

	@Override
	public int compareTo(IDVO object) {
		if (object == null) {
			return -1;
		}
		Long id = getId();
		Long objectId = object.getId();
		if (objectId == null) {
			if (id == null) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (id == null) {
				return 1;
			} else {
				return id.compareTo(objectId);
			}
		}
	}

	@Override
	public boolean equals(final Object object) {
		if (object == null || !(object instanceof IDVO)) {
			return false;
		}
		IDVO rhs = (IDVO) object;
		if (rhs.vo == null) {
			if (vo == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (vo == null) {
				return false;
			} else {
				Long id = getId();
				Long rhsId = rhs.getId();
				if (rhsId == null) {
					if (id == null) {
						return true;
					} else {
						return false;
					}
				} else {
					if (id == null) {
						return false;
					} else {
						return id.equals(rhsId);
					}
				}
			}
		}
	}

	public Long getId() {
		return CommonUtil.getVOId(vo);
	}

	public PSFVO getPsf() {
		return psf;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public int getRowPosition() {
		return rowIndex - WebUtil.FACES_INITIAL_ROW_INDEX + 1;
	}

	public Object getVo() {
		return vo;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1249046965, -82296885).append(this.getId()).toHashCode();
	}

	public void setPsf(PSFVO psf) {
		this.psf = psf;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public void setVo(Object vo) {
		this.vo = vo;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", this.getId()).toString();
	}
}
