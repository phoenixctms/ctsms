package org.phoenixctms.ctsms.web.jersey.wrapper;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class NoShortcutSerializationWrapper<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static <T> void  transformVoCollection(Collection<T> vos) {
		if (vos != null) {
			CollectionUtils.transform(vos, new Transformer() {

				@Override
				public Object transform(Object input) {
					return new NoShortcutSerializationWrapper((Serializable) input);
				}
			});
		}
	}

	private Serializable vo;

	public NoShortcutSerializationWrapper(Serializable vo) {
		this.vo = vo;
	}

	public Serializable getVo() {
		return vo;
	}
}
