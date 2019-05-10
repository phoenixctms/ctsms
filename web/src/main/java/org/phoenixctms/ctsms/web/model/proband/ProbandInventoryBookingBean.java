package org.phoenixctms.ctsms.web.model.proband;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InventoryBookingInVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.shared.CollidingProbandStatusEntryEagerModel;
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
public class ProbandInventoryBookingBean extends InventoryBookingBeanBase {

	public static void initBookingDefaultValues(InventoryBookingInVO in, Long probandId, StaffOutVO identity) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setComment(Messages.getString(MessageCodes.BOOKING_COMMENT_PRESET));
			in.setCourseId(null);
			in.setInventoryId(null);
			in.setOnBehalfOfId(identity != null ? identity.getId() : null);
			in.setProbandId(probandId);
			in.setStart(null);
			in.setStop(null);
			in.setTrialId(null);
			in.setCalendar(Messages.getString(MessageCodes.PROBAND_INVENTORY_BOOKING_CALENDAR_PRESET));
		}
	}

	private Long probandId;
	private ProbandOutVO proband;
	private ProbandInventoryBookingLazyModel bookingModel;
	private HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache;
	private AddBookingReimbursementBean addReimbursementBean;

	public ProbandInventoryBookingBean() {
		super();
		collidingProbandStatusEntryModelCache = new HashMap<Long, CollidingProbandStatusEntryEagerModel>();
		bookingModel = new ProbandInventoryBookingLazyModel();
		addReimbursementBean = new AddBookingReimbursementBean();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_INVENTORY_BOOKING_TAB_TITLE_BASE64,
				JSValues.AJAX_PROBAND_INVENTORY_BOOKING_COUNT, MessageCodes.PROBAND_INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.PROBAND_INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT,
				new Long(bookingModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("inventorybooking_list");
		out = null;
		this.probandId = id;
		// addReimbursementBean.change();
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public void changeAddReimbursement() {
		Long bookingId = WebUtil.getLongParamValue(GetParamNames.INVENTORY_BOOKING_ID);
		addReimbursementBean.setProbandId(in.getProbandId());
		addReimbursementBean.changeRootEntity(bookingId);
	}

	public AddBookingReimbursementBean getAddReimbursementBean() {
		return addReimbursementBean;
	}

	public ProbandInventoryBookingLazyModel getBookingModel() {
		return bookingModel;
	}

	public CollidingProbandStatusEntryEagerModel getCollidingProbandStatusEntryModel(InventoryBookingOutVO probandBooking) {
		return CollidingProbandStatusEntryEagerModel.getCachedCollidingProbandStatusEntryModel(probandBooking, true, collidingProbandStatusEntryModelCache);
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROBAND_INVENTORY_BOOKING_TITLE, Long.toString(out.getId()), CommonUtil.inventoryOutVOToString(out.getInventory()),
					DateUtil.getDateTimeStartStopString(out.getStart(), out.getStop()));
		} else {
			return Messages.getString(MessageCodes.PROBAND_CREATE_NEW_INVENTORY_BOOKING);
		}
	}

	@PostConstruct
	private void init() {
		// System.out.println("POSTCONSTRUCT: " + this.toString());
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
			probandId = in.getProbandId();
		} else {
			initBookingDefaultValues(in, probandId, WebUtil.getUserIdentity());
		}
	}

	@Override
	protected void initSets() {
		//addReimbursementBean.setProbandId(in.getProbandId());
		collidingInventoryStatusEntryModelCache.clear();
		collidingProbandStatusEntryModelCache.clear();
		filterCalendars = WebUtil.getInventoryBookingFilterCalendars(null, null, probandId, null, null);
		bookingModel.setProbandId(in.getProbandId());
		bookingModel.updateRowCount();
		proband = WebUtil.getProband(this.in.getProbandId(), null, null, null);
		if (WebUtil.isProbandLocked(proband)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getProbandId() == null ? false : !WebUtil.isProbandLocked(proband));
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isProbandLocked(proband);
	}

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isProbandLocked(proband);
	}

	public boolean isReimbursementCreateable(InventoryBookingOutVO booking) {
		if (proband == null || booking == null) {
			return false;
		} else {
			return !WebUtil.isTrialLocked(booking.getTrial()) && !WebUtil.isProbandLocked(proband) && WebUtil.isProbandPerson(proband);
		}
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isProbandLocked(proband);
	}
}
