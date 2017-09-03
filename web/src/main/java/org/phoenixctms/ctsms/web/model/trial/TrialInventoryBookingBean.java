package org.phoenixctms.ctsms.web.model.trial;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InventoryBookingInVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
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
public class TrialInventoryBookingBean extends InventoryBookingBeanBase {

	public static void initBookingDefaultValues(InventoryBookingInVO in, Long trialId, StaffOutVO identity) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setComment(Messages.getString(MessageCodes.BOOKING_COMMENT_PRESET));
			in.setCourseId(null);
			in.setInventoryId(null);
			in.setOnBehalfOfId(identity != null ? identity.getId() : null);
			in.setProbandId(null);
			in.setStart(null);
			in.setStop(null);
			in.setTrialId(trialId);
			in.setCalendar(Messages.getString(MessageCodes.TRIAL_INVENTORY_BOOKING_CALENDAR_PRESET));
		}
	}

	private Long trialId;
	private TrialOutVO trial;
	private TrialInventoryBookingLazyModel bookingModel;
	private HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache;

	public TrialInventoryBookingBean() {
		super();
		collidingProbandStatusEntryModelCache = new HashMap<Long, CollidingProbandStatusEntryEagerModel>();
		bookingModel = new TrialInventoryBookingLazyModel();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_TRIAL_INVENTORY_BOOKING_TAB_TITLE_BASE64,
				JSValues.AJAX_TRIAL_INVENTORY_BOOKING_COUNT, MessageCodes.TRIAL_INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.TRIAL_INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT, new Long(
						bookingModel.getRowCount()));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("inventorybooking_list");
		out = null;
		this.trialId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public TrialInventoryBookingLazyModel getBookingModel() {
		return bookingModel;
	}

	public CollidingProbandStatusEntryEagerModel getCollidingProbandStatusEntryModel(InventoryBookingOutVO probandBooking) {
		return CollidingProbandStatusEntryEagerModel.getCachedCollidingProbandStatusEntryModel(probandBooking, true, collidingProbandStatusEntryModelCache);
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.TRIAL_INVENTORY_BOOKING_TITLE, Long.toString(out.getId()), CommonUtil.inventoryOutVOToString(out.getInventory()),
					DateUtil.getDateTimeStartStopString(out.getStart(), out.getStop()));
		} else {
			return Messages.getString(MessageCodes.TRIAL_CREATE_NEW_INVENTORY_BOOKING);
		}
	}

	public TrialOutVO getTrial() {
		return trial;
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
			trialId = in.getTrialId();
		} else {
			initBookingDefaultValues(in, trialId, WebUtil.getUserIdentity());
		}
	}

	@Override
	protected void initSets() {
		collidingInventoryStatusEntryModelCache.clear();
		collidingProbandStatusEntryModelCache.clear();
		bookingModel.setTrialId(in.getTrialId());
		bookingModel.updateRowCount();
		trial = WebUtil.getTrial(this.in.getTrialId());
		filterCalendars = WebUtil.getInventoryBookingFilterCalendars(null, null, null, null, trialId);
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getTrialId() == null ? false : !WebUtil.isTrialLocked(trial));
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isTrialLocked(trial);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}
}
