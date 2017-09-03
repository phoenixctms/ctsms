package org.phoenixctms.ctsms.email;

import javax.mail.internet.MimeMessage;

import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;

public class NewsletterEmailSender extends EmailSender<Newsletter, Proband> {

	private ProbandDao probandDao;

	@Override
	protected int prepare(MimeMessage mimeMessage, Newsletter entity,
			Proband recipient) throws Exception {

        //http://www.javabeat.net/how-to-send-email-using-spring-framework/

		return 0;
	}

	public void setProbandDao(ProbandDao probandDao) {
		this.probandDao = probandDao;
	}
}