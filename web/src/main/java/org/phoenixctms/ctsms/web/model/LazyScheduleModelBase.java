package org.phoenixctms.ctsms.web.model;

import java.util.Date;

import org.primefaces.model.LazyScheduleModel;

public abstract class LazyScheduleModelBase extends LazyScheduleModel {

	protected Date start;
	protected Date stop;

	public void clearCaches() {
	}

	protected abstract void getLazyResult(Date start, Date stop);

	@Override
	public final void loadEvents(Date start, Date stop) {
		this.start = start;
		this.stop = stop;
		clear();
		clearCaches();
		getLazyResult(start, stop);
	}

	public final void reLoadEvents() {
		this.loadEvents(start, stop);
	}
}
