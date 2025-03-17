package org.phoenixctms.ctsms.executable.migration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.phoenixctms.ctsms.domain.PersonContactParticulars;
import org.phoenixctms.ctsms.domain.PersonContactParticularsDao;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.PageSizes;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonNameNormalizedInitializer {

	protected JobOutput jobOutput;
	@Autowired
	private PersonContactParticularsDao personContactParticularsDao;
	private ChunkedDaoOperationAdapter<PersonContactParticularsDao, PersonContactParticulars> personContactParticularsProcessor;

	public PersonNameNormalizedInitializer() {
	}

	public long update() throws Exception {
		personContactParticularsProcessor = new ChunkedDaoOperationAdapter<PersonContactParticularsDao, PersonContactParticulars>(personContactParticularsDao) {

			@Override
			protected boolean process(Collection<PersonContactParticulars> page,
					Object passThrough) throws Exception {
				return false;
			}

			@Override
			protected boolean process(PersonContactParticulars particulars, Object passThrough)
					throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				particulars.setFirstNameNormalized(CommonUtil.normalizeFirstName(particulars.getFirstName()));
				particulars.setLastNameNormalized(CommonUtil.normalizeLastName(particulars.getLastName()));
				this.dao.update(particulars);
				jobOutput.println("row updated");
				inOut.put("updated", ((Long) inOut.get("updated")) + 1l);
				return true;
			}
		};
		Map<String, Object> passThrough = new HashMap<String, Object>();
		passThrough.put("updated", 0l);
		passThrough.put("skipped", 0l);
		personContactParticularsProcessor.processEach(PageSizes.TINY, passThrough);
		long updated = (Long) passThrough.get("updated");
		long skipped = (Long) passThrough.get("skipped");
		jobOutput.println(updated + " rows updated, " + skipped + " skipped");
		return updated;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
