package org.phoenixctms.ctsms.web.model.inventory;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.vo.InventoryBookingInVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.shared.CollidingCourseParticipationStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingDutyRosterTurnEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingProbandStatusEntryEagerModel;
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
public class InventoryBookingBean extends InventoryBookingBeanBase {

	public static void initBookingDefaultValues(InventoryBookingInVO in, Long inventoryId, StaffOutVO identity) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setComment(Messages.getString(MessageCodes.BOOKING_COMMENT_PRESET));
			in.setCourseId(null);
			in.setInventoryId(inventoryId);
			in.setOnBehalfOfId(identity != null ? identity.getId() : null);
			in.setProbandId(null);
			in.setStart(null);
			in.setStop(null);
			in.setTrialId(null);
			in.setCalendar(Messages.getString(MessageCodes.INVENTORY_BOOKING_CALENDAR_PRESET));
		}
	}

	private Long inventoryId;
	private InventoryOutVO inventory;
	private InventoryBookingLazyModel bookingModel;
	private HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache;
	private HashMap<Long, CollidingDutyRosterTurnEagerModel> collidingDutyRosterTurnModelCache;
	private HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache;
	private HashMap<Long, CollidingCourseParticipationStatusEntryEagerModel> collidingCourseParticipationStatusEntryModelCache;

	public InventoryBookingBean() {
		super();
		collidingStaffStatusEntryModelCache = new HashMap<Long, CollidingStaffStatusEntryEagerModel>();
		collidingDutyRosterTurnModelCache = new HashMap<Long, CollidingDutyRosterTurnEagerModel>();
		collidingProbandStatusEntryModelCache = new HashMap<Long, CollidingProbandStatusEntryEagerModel>();
		collidingCourseParticipationStatusEntryModelCache = new HashMap<Long, CollidingCourseParticipationStatusEntryEagerModel>();
		bookingModel = new InventoryBookingLazyModel();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_INVENTORY_BOOKING_TAB_TITLE_BASE64,
				JSValues.AJAX_INVENTORY_BOOKING_COUNT, MessageCodes.INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT,
				new Long(bookingModel.getRowCount()));
		if (operationSuccess && in.getInventoryId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_JOURNAL_ENTRY_COUNT,
					MessageCodes.INVENTORY_JOURNAL_TAB_TITLE, MessageCodes.INVENTORY_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.INVENTORY_JOURNAL, in.getInventoryId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("inventorybooking_list");
		out = null;
		this.inventoryId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public InventoryBookingLazyModel getBookingModel() {
		return bookingModel;
	}

	public CollidingCourseParticipationStatusEntryEagerModel getCollidingCourseParticipationStatusEntryModel(InventoryBookingOutVO courseBooking) {
		return CollidingCourseParticipationStatusEntryEagerModel.getCachedCollidingCourseParticipationStatusEntryModel(courseBooking,
				true, collidingCourseParticipationStatusEntryModelCache);
	}

	public CollidingDutyRosterTurnEagerModel getCollidingDutyRosterTurnModel(InventoryBookingOutVO courseBooking) {
		return CollidingDutyRosterTurnEagerModel.getCachedCollidingDutyRosterTurnModel(courseBooking, true, collidingDutyRosterTurnModelCache);
	}

	public CollidingProbandStatusEntryEagerModel getCollidingProbandStatusEntryModel(InventoryBookingOutVO probandBooking) {
		return CollidingProbandStatusEntryEagerModel.getCachedCollidingProbandStatusEntryModel(probandBooking, true, collidingProbandStatusEntryModelCache);
	}

	public CollidingStaffStatusEntryEagerModel getCollidingStaffStatusEntryModel(InventoryBookingOutVO courseBooking) {
		return CollidingStaffStatusEntryEagerModel.getCachedCollidingStaffStatusEntryModel(courseBooking, true, collidingStaffStatusEntryModelCache);
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.INVENTORY_BOOKING_TITLE, Long.toString(out.getId()), DateUtil.getDateTimeStartStopString(out.getStart(), out.getStop()));
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_INVENTORY_BOOKING);
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
			inventoryId = in.getInventoryId();
		} else {
			initBookingDefaultValues(in, inventoryId, WebUtil.getUserIdentity());
		}
	}

	@Override
	protected void initSets() {
		collidingInventoryStatusEntryModelCache.clear();
		collidingStaffStatusEntryModelCache.clear();
		collidingDutyRosterTurnModelCache.clear();
		collidingProbandStatusEntryModelCache.clear();
		collidingCourseParticipationStatusEntryModelCache.clear();
		bookingModel.setInventoryId(in.getInventoryId());
		bookingModel.updateRowCount();
		inventory = WebUtil.getInventory(in.getInventoryId(), null, null);
		filterCalendars = WebUtil.getInventoryBookingFilterCalendars(null, inventoryId, null, null, null);
		if (!WebUtil.isInventoryBookable(inventory)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.INVENTORY_NOT_BOOKABLE);
		}
	}

	@Override
	public boolean isCreateable() {
		return (this.in.getInventoryId() == null ? false : WebUtil.isInventoryBookable(inventory));
	}

	@Override
	public boolean isEditable() {
		return isCreated() && WebUtil.isInventoryBookable(inventory);
	}

	public boolean isInputVisible() {
		return isCreated() || WebUtil.isInventoryBookable(inventory);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && WebUtil.isInventoryBookable(inventory);
	}
}
