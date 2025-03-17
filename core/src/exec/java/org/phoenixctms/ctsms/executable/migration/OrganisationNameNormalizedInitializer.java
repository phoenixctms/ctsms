package org.phoenixctms.ctsms.executable.migration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.phoenixctms.ctsms.domain.OrganisationContactParticulars;
import org.phoenixctms.ctsms.domain.OrganisationContactParticularsDao;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.PageSizes;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.springframework.beans.factory.annotation.Autowired;

public class OrganisationNameNormalizedInitializer {

	protected JobOutput jobOutput;
	@Autowired
	private OrganisationContactParticularsDao organisationContactParticularsDao;
	private ChunkedDaoOperationAdapter<OrganisationContactParticularsDao, OrganisationContactParticulars> organisationContactParticularsProcessor;

	public OrganisationNameNormalizedInitializer() {
	}

	public long update() throws Exception {
		organisationContactParticularsProcessor = new ChunkedDaoOperationAdapter<OrganisationContactParticularsDao, OrganisationContactParticulars>(
				organisationContactParticularsDao) {

			@Override
			protected boolean process(Collection<OrganisationContactParticulars> page,
					Object passThrough) throws Exception {
				return false;
			}

			@Override
			protected boolean process(OrganisationContactParticulars particulars, Object passThrough)
					throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				particulars.setOrganisationNameNormalized(CommonUtil.normalizeOrganisationName(particulars.getOrganisationName()));
				this.dao.update(particulars);
				jobOutput.println("row updated");
				inOut.put("updated", ((Long) inOut.get("updated")) + 1l);
				return true;
			}
		};
		Map<String, Object> passThrough = new HashMap<String, Object>();
		passThrough.put("updated", 0l);
		passThrough.put("skipped", 0l);
		organisationContactParticularsProcessor.processEach(PageSizes.TINY, passThrough);
		long updated = (Long) passThrough.get("updated");
		long skipped = (Long) passThrough.get("skipped");
		jobOutput.println(updated + " rows updated, " + skipped + " skipped");
		return updated;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
