package org.phoenixctms.ctsms.web.model.course;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InventoryBookingInVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.shared.CollidingCourseParticipationStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingDutyRosterTurnEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingStaffStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.InventoryBookingBeanBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class CourseInventoryBookingBean extends InventoryBookingBeanBase {

	public static void initBookingDefaultValues(InventoryBookingInVO in, Long courseId, StaffOutVO identity) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setComment(Messages.getString(MessageCodes.BOOKING_COMMENT_PRESET));
			in.setCourseId(courseId);
			in.setInventoryId(null);
			in.setOnBehalfOfId(identity != null ? identity.getId() : null);
			in.setProbandId(null);
			in.setStart(null);
			in.setStop(null);
			in.setTrialId(null);
			in.setCalendar(Messages.getString(MessageCodes.COURSE_INVENTORY_BOOKING_CALENDAR_PRESET));
		}
	}

	private Long courseId;
	private CourseInventoryBookingLazyModel bookingModel;
	private HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache;
	private HashMap<Long, CollidingDutyRosterTurnEagerModel> collidingDutyRosterTurnModelCache;
	private HashMap<Long, CollidingCourseParticipationStatusEntryEagerModel> collidingCourseParticipationStatusEntryModelCache;

	public CourseInventoryBookingBean() {
		super();
		collidingStaffStatusEntryModelCache = new HashMap<Long, CollidingStaffStatusEntryEagerModel>();
		collidingDutyRosterTurnModelCache = new HashMap<Long, CollidingDutyRosterTurnEagerModel>();
		collidingCourseParticipationStatusEntryModelCache = new HashMap<Long, CollidingCourseParticipationStatusEntryEagerModel>();
		bookingModel = new CourseInventoryBookingLazyModel();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_COURSE_INVENTORY_BOOKING_TAB_TITLE_BASE64,
				JSValues.AJAX_COURSE_INVENTORY_BOOKING_COUNT, MessageCodes.COURSE_INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.COURSE_INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT,
				new Long(bookingModel.getRowCount()));
		if (operationSuccess && in.getCourseId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_COURSE_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_JOURNAL_ENTRY_COUNT,
					MessageCodes.COURSE_JOURNAL_TAB_TITLE, MessageCodes.COURSE_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.COURSE_JOURNAL, in.getCourseId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("inventorybooking_list");
		out = null;
		this.courseId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public CourseInventoryBookingLazyModel getBookingModel() {
		return bookingModel;
	}

	public CollidingCourseParticipationStatusEntryEagerModel getCollidingCourseParticipationStatusEntryModel(InventoryBookingOutVO courseBooking) {
		return CollidingCourseParticipationStatusEntryEagerModel.getCachedCollidingCourseParticipationStatusEntryModel(courseBooking,
				true, collidingCourseParticipationStatusEntryModelCache);
	}

	public CollidingDutyRosterTurnEagerModel getCollidingDutyRosterTurnModel(InventoryBookingOutVO courseBooking) {
		return CollidingDutyRosterTurnEagerModel.getCachedCollidingDutyRosterTurnModel(courseBooking, true, collidingDutyRosterTurnModelCache);
	}

	public CollidingStaffStatusEntryEagerModel getCollidingStaffStatusEntryModel(InventoryBookingOutVO courseBooking) {
		return CollidingStaffStatusEntryEagerModel.getCachedCollidingStaffStatusEntryModel(courseBooking, true, collidingStaffStatusEntryModelCache);
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.COURSE_INVENTORY_BOOKING_TITLE, Long.toString(out.getId()), CommonUtil.inventoryOutVOToString(out.getInventory()),
					DateUtil.getDateTimeStartStopString(out.getStart(), out.getStop()));
		} else {
			return Messages.getString(MessageCodes.COURSE_CREATE_NEW_INVENTORY_BOOKING);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.INVENTORY_BOOKING_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	@Override
	protected void initIn() {
		if (in == null) {
			in = new InventoryBookingInVO();
		}
		if (out != null) {
			copyBookingOutToIn(in, out);
			courseId = in.getCourseId();
		} else {
			initBookingDefaultValues(in, courseId, WebUtil.getUserIdentity());
		}
	}

	@Override
	protected void initSets() {
		collidingInventoryStatusEntryModelCache.clear();
		collidingStaffStatusEntryModelCache.clear();
		collidingDutyRosterTurnModelCache.clear();
		collidingCourseParticipationStatusEntryModelCache.clear();
		filterCalendars = WebUtil.getInventoryBookingFilterCalendars(null, null, null, courseId, null);
		bookingModel.setCourseId(in.getCourseId());
		bookingModel.updateRowCount();
	}

	@Override
	public boolean isCreateable() {
		return (in.getCourseId() == null ? false : true);
	}
}
