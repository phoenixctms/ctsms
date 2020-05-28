package org.phoenixctms.ctsms.compare;



import java.util.Date;

import org.phoenixctms.ctsms.vo.VisitScheduleAppointmentVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;

public class VisitScheduleAppointmentIntervalScheduleComparator extends IntervalScheduleComparatorBase<VisitScheduleAppointmentVO> {

	public VisitScheduleAppointmentIntervalScheduleComparator(boolean desc) {
		super(desc);
	}

	@Override
	protected Date getStart(VisitScheduleAppointmentVO item) {
		return item.getStart();
	}

	@Override
	protected Date getStop(VisitScheduleAppointmentVO item) {
		return item.getStop();
	}

	@Override
	protected boolean isInterval(VisitScheduleAppointmentVO item) {
		return item != null && item.getStart() != null && item.getStop() != null;
	}
}
