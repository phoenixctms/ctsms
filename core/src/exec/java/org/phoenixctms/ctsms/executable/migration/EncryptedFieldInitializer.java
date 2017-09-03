package org.phoenixctms.ctsms.executable.migration;

import org.phoenixctms.ctsms.Search;
import org.phoenixctms.ctsms.SearchParameter;
import org.phoenixctms.ctsms.UserContext;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.KeyPair;
import org.phoenixctms.ctsms.domain.KeyPairDao;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.security.Authenticator;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.service.user.UserService;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class EncryptedFieldInitializer {

	protected JobOutput jobOutput;
	@Autowired
	private UserDao userDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private UserService userService;
	@Autowired
	private KeyPairDao keyPairDao;
	private Authenticator authenticator;

	protected void authenticate(AuthenticationVO auth) throws Exception {
		UserOutVO userVO = userService.getUser(auth, userDao.searchUniqueName(auth.getUsername()).getId(), null);
		authenticator.authenticate(auth, false);
		UserContext context = CoreUtil.getUserContext();
		Department department = departmentDao.load(userVO.getDepartment().getId());
		context.setDepartmentKey(CryptoUtil.getDepartmentKey(CryptoUtil.decryptDepartmentKey(department, context.getPlainDepartmentPassword())));
		KeyPair keyPair = keyPairDao.search(new Search(new SearchParameter[] {
				new SearchParameter("user.id", userVO.getId(), SearchParameter.EQUAL_COMPARATOR) })).iterator().next();
		context.setPrivateKey(CryptoUtil.getPrivateKey(CryptoUtil.decryptPrivateKey(keyPair, context.getPlainDepartmentPassword())));
		context.setPublicKey(CryptoUtil.getPublicKey(keyPair.getPublicKey()));
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}

	public abstract long update(AuthenticationVO auth) throws Exception;
}
