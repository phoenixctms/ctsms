package org.phoenixctms.ctsms.vocycle;

import java.util.HashMap;

import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandGroup;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDaoImpl;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntry;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntryDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;

public class ProbandListEntryGraph extends GraphCycle1Helper<ProbandListEntry, ProbandListEntryOutVO, ProbandListStatusEntry, ProbandListStatusEntryOutVO> {

	private static final boolean LIMIT_A_INSTANCES = true;
	private static final int MAX_A_INSTANCES = 1;
	private ProbandListEntryDaoImpl probandListEntryDaoImpl;
	private ProbandListStatusEntryDao probandListStatusEntryDao;
	private TrialDao trialDao;
	private ProbandDao probandDao;
	private ProbandGroupDao probandGroupDao;
	private UserDao userDao;

	public ProbandListEntryGraph(
			ProbandListEntryDaoImpl probandListEntryDaoImpl,
			ProbandListStatusEntryDao probandListStatusEntryDao, TrialDao trialDao,
			ProbandDao probandDao, ProbandGroupDao probandGroupDao,
			UserDao userDao) {
		this.probandListEntryDaoImpl = probandListEntryDaoImpl;
		this.probandListStatusEntryDao = probandListStatusEntryDao;
		this.trialDao = trialDao;
		this.probandDao = probandDao;
		this.probandGroupDao = probandGroupDao;
		this.userDao = userDao;
	}

	@Override
	protected Long getAId(ProbandListEntry source) {
		return source.getId();
	}

	@Override
	protected Class getAVOClass() {
		return ProbandListEntryOutVO.class;
	}

	@Override
	protected Long getBId(ProbandListStatusEntry source) {
		return source.getId();
	}

	@Override
	protected ProbandListStatusEntry getBOfA(ProbandListEntry source) {
		return source.getLastStatus();
	}

	@Override
	protected Class getBVOClass() {
		return ProbandListStatusEntryOutVO.class;
	}

	@Override
	protected int getMaxAInstances() {
		return MAX_A_INSTANCES;
	}

	@Override
	protected boolean limitAInstances() {
		return LIMIT_A_INSTANCES;
	}

	@Override
	protected ProbandListStatusEntryOutVO newBVO() {
		return new ProbandListStatusEntryOutVO();
	}

	@Override
	protected void setBVOOfAVO(ProbandListEntryOutVO target,
			ProbandListStatusEntryOutVO bVO) {
		target.setLastStatus(bVO);
	}

	@Override
	protected void toBVO(ProbandListStatusEntry source,
			ProbandListStatusEntryOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		probandListStatusEntryDao.toProbandListStatusEntryOutVO(source, target, voMap);
	}

	@Override
	protected void toVORemainingFields(ProbandListEntry source,
			ProbandListEntryOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		probandListEntryDaoImpl.toProbandListEntryOutVOBase(source, target);
		Trial trial = source.getTrial();
		Proband proband = source.getProband();
		ProbandGroup group = source.getGroup();
		User modifiedUser = source.getModifiedUser();
		if (trial != null) {
			target.setTrial(trialDao.toTrialOutVO(trial));
		}
		if (proband != null) {
			target.setProband(probandDao.toProbandOutVO(proband));
		}
		if (group != null) {
			target.setGroup(probandGroupDao.toProbandGroupOutVO(group));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(userDao.toUserOutVO(modifiedUser));
		}
		target.setExportStatus(L10nUtil.createExportStatusVO(Locales.USER, source.getExportStatus()));
	}
}
