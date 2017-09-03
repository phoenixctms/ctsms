package org.phoenixctms.ctsms.email;

import javax.mail.internet.MimeMessage;

import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;

public class ShortMessageSender extends EmailSender<ShortMessage, Staff> {

	private StaffDao staffDao;

	@Override
	protected int prepare(MimeMessage mimeMessage, ShortMessage entity,
			Staff recipient) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setStaffDao(StaffDao staffDao) {
		this.staffDao = staffDao;
	}
}
