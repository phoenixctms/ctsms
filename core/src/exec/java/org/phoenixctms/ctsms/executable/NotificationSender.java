package org.phoenixctms.ctsms.executable;

import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.service.shared.ToolsService;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationSender {

	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private ToolsService toolsService;
	private JobOutput jobOutput;

	public NotificationSender() {
	}

	public long prepareNotifications(String departmentL10nKey) throws Exception {
		jobOutput.println("department l10n key: " + departmentL10nKey);
		long count = toolsService.prepareNotifications(ExecUtil.departmentL10nKeyToId(departmentL10nKey, departmentDao, jobOutput));
		jobOutput.println(count + " notifications prepared");
		return count;
	}

	public long processNotifications(String departmentL10nKey, Integer limit) throws Exception {
		jobOutput.println("department l10n key: " + departmentL10nKey);
		PSFVO psf = new PSFVO();
		psf.setSortField("id");
		psf.setSortOrder(true);
		if (limit != null && limit >= 0) {
			psf.setFirst(0);
			psf.setPageSize(limit);
			jobOutput.println("limit: " + limit);
		}
		long emailSentCount = toolsService.processNotifications(ExecUtil.departmentL10nKeyToId(departmentL10nKey, departmentDao, jobOutput), psf);
		jobOutput.println("emails sent to " + emailSentCount + " recipients of " + psf.getRowCount() + " pending notifications");
		return emailSentCount;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
