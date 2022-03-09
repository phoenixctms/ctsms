package org.phoenixctms.ctsms.executable;

import java.util.Iterator;

import org.phoenixctms.ctsms.domain.Job;
import org.phoenixctms.ctsms.domain.JobDao;
import org.phoenixctms.ctsms.domain.PasswordDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.security.Authenticator;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.service.user.UserService;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class JobRunner {

	@Autowired
	private UserDao userDao;
	@Autowired
	private JobDao jobDao;
	@Autowired
	private PasswordDao passwordDao;
	@Autowired
	private UserService userService;
	private JobOutput jobOutput;
	private Authenticator authenticator;

	public JobRunner() {
	}

	public long processJobs(AuthenticationVO auth, Boolean daily, Boolean weekly, Boolean monthly) throws Exception {
		UserOutVO userVO = null;
		try {
			userVO = userService.getUser(auth, userDao.searchUniqueName(auth.getUsername()).getId(), null, null, null);
			jobOutput.println("department: " + userVO.getDepartment().getName());
		} catch (Exception e) {
		}
		String plainDepartmentPassword = CryptoUtil.decryptDepartmentPassword(authenticator.authenticate(auth, false), auth.getPassword());
		long count = 0l;
		Iterator<Job> jobsIt = jobDao.findPending(userVO.getDepartment().getId(), daily, weekly, monthly).iterator();
		while (jobsIt.hasNext()) {
			Job job = jobsIt.next();
			jobOutput.println("running job ID " + Long.toString(job.getId()));
			User jobUser = job.getModifiedUser();
			AuthenticationVO jobUserAuth = new AuthenticationVO();
			jobUserAuth.setUsername(jobUser.getName());
			jobUserAuth.setPassword(CryptoUtil.decryptPassword(passwordDao.findLastPassword(jobUser.getId()), plainDepartmentPassword));
			CoreUtil.launchJob(jobUserAuth, job, true);
			count += 1l;
		}
		jobOutput.println(count + " jobs executed");
		return count;
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
