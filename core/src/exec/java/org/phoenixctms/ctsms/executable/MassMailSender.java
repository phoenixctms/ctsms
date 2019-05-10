package org.phoenixctms.ctsms.executable;

import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.service.massmail.MassMailService;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.springframework.beans.factory.annotation.Autowired;

public class MassMailSender {

	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private MassMailService massMailService;
	private JobOutput jobOutput;

	public MassMailSender() {
	}

	public long processMassMails(AuthenticationVO auth, String departmentL10nKey, Integer limit) throws Exception {
		jobOutput.println("department l10n key: " + departmentL10nKey);
		PSFVO psf = new PSFVO();
		// psf.setSortField("id");
		// psf.setSortOrder(true);
		if (limit != null && limit >= 0) {
			psf.setFirst(0);
			psf.setPageSize(limit);
			jobOutput.println("limit: " + limit);
		}
		long emailSentCount = massMailService.processMassMails(auth, ExecUtil.departmentL10nKeyToId(departmentL10nKey, departmentDao, jobOutput), psf);
		jobOutput.println("emails sent to " + emailSentCount + " recipients of " + psf.getRowCount() + " pending mass mails");
		return emailSentCount;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
