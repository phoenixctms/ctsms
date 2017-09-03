package org.phoenixctms.ctsms.vocycle;

public class DeferredVO {

	private boolean deferred;
	private Object vo;

	public DeferredVO(boolean deferred, Object vo) {
		this.deferred = deferred;
		this.vo = vo;
	}

	public Object getVo() {
		return vo;
	}

	public boolean isDeferred() {
		return deferred;
	}

	public void setDeferred(boolean deferred) {
		this.deferred = deferred;
	}
}
