package org.phoenixctms.ctsms.web.model;

import java.util.Date;

import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.WebUtil.ColorOpacity;
import org.primefaces.model.ScheduleEvent;

public abstract class ScheduleEventBase<IN> implements ScheduleEvent {

	protected enum InitSource {
		NONE,
		DEFAULT_VALUES,
		OUT,
		EVENT
	}

	protected IN in;
	private ScheduleEvent event;
	private String id;

	protected final static ColorOpacity DEFAULT_COLOR_OPACITY = ColorOpacity.ALPHA50;

	public ScheduleEventBase() {
		initIn(InitSource.DEFAULT_VALUES);
	}

	public ScheduleEventBase(IN in) {
		setIn(in);

	}

	public ScheduleEventBase(ScheduleEvent event) {
		setEvent(event);
	}

	private void copyEventToIn() {
		IN data = null;
		try {
			data = (IN) event.getData();
		} catch (ClassCastException e) {
		}
		if (data != null) {
			copyToIn(data);
		} else {
			initDefaultValues();
		}
	}

	protected abstract boolean copyOutToIn();

	protected abstract void copyToIn(IN source);

	protected abstract void createIn();

	@Override
	public Object getData() {
		return in;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	@Override
	public String getId() {
		return this.id;
	}

	protected final Date getOpenStartDate(Date start) {
		if (start == null) {
			return new Date(DateUtil.getScheduleMinDateValue().getTime());
		} else {
			return DateUtil.sanitizeScheduleDate(true, start);
		}
	}

	protected final Date getOpenStartTimestamp(Date start) {
		if (start == null) {
			return new Date(DateUtil.getScheduleMinDateValue().getTime());
		} else {
			return DateUtil.sanitizeScheduleTimestamp(true, start);
		}
	}

	protected final Date getOpenStopDate(Date stop) {
		if (stop == null) {
			return new Date(DateUtil.getScheduleMaxDateValue().getTime());
		} else {
			return DateUtil.sanitizeScheduleDate(true, stop);
		}
	}

	protected final Date getOpenStopTimestamp(Date stop) {
		if (stop == null) {
			return new Date(DateUtil.getScheduleMaxDateValue().getTime());
		} else {
			return DateUtil.sanitizeScheduleTimestamp(true, stop);
		}
	}

	protected abstract void initDefaultValues();

	protected void initIn(InitSource initBy) {
		if (in == null) {
			createIn();
		}
		switch (initBy) {
			case DEFAULT_VALUES:
				initDefaultValues();
				break;
			case OUT:
				if (!copyOutToIn()) {
					initDefaultValues();
				}
				break;
			case EVENT:
				if (event != null) {
					copyEventToIn();
				} else {
					initDefaultValues();
				}
				break;
			default:
				// no init/change..
		}
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
		this.initIn(InitSource.EVENT);
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public void setIn(IN in) {
		initIn(InitSource.NONE);
		if (in != null) {
			copyToIn(in);
		} else {
			initDefaultValues();
		}
	}
}
