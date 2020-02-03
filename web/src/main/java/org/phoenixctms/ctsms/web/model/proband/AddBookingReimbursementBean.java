package org.phoenixctms.ctsms.web.model.proband;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class AddBookingReimbursementBean extends AddReimbursementBeanBase {

	private InventoryBookingOutVO booking;

	public AddBookingReimbursementBean() {
		super();
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		this.trialId = null;
		this.booking = WebUtil.getInventoryBooking(id);
		if (this.booking != null) {
			this.trialId = booking.getTrial() != null ? booking.getTrial().getId() : null;
		}
		initIn();
		initSets(true);
		return CHANGE_OUTCOME;
	}

	public InventoryBookingOutVO getBooking() {
		return booking;
	}

	@Override
	protected String getComment() {
		StringBuilder comment = new StringBuilder();
		if (booking != null) {
			appendTravelExpenseComment(comment, booking.getStart(), getReimbursementTitle());
			appendTicketExpenseComment(comment, booking.getStart(), getReimbursementTitle());
			appendAccommodationExpenseComment(comment, booking.getStart(), booking.getStop());
			appendReimbursementComment(comment, booking.getStart(), getReimbursementTitle());
		}
		return comment.toString();
	}

	@Override
	protected String getReimbursementTitle() {
		if (booking != null) {
			return CommonUtil.clipString(booking.getComment(),
					Settings.getInt(SettingCodes.ADD_REIMBURSEMENT_COMMENT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.ADD_REIMBURSEMENT_COMMENT_CLIP_MAX_LENGTH));
		}
		return null;
	}

	@Override
	protected void initSpecificSets(boolean reset) {
		super.initSpecificSets(reset);
		if (reset) {
			reimbursementAmount = 0.0f;
			addReimbursement = reimbursementAmount > 0.0f;
		}
	}

	@Override
	public boolean isCreateable() {
		if (in.getProbandId() == null || booking == null) {
			return false;
		} else {
			return !WebUtil.isTrialLocked(booking.getTrial()) && !WebUtil.isProbandLocked(proband) && WebUtil.isProbandPerson(proband);
		}
	}
}
