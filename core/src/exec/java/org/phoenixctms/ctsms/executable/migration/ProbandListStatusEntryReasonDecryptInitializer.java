package org.phoenixctms.ctsms.executable.migration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.phoenixctms.ctsms.domain.ProbandListStatusEntry;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntryDao;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.PageSizes;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.springframework.beans.factory.annotation.Autowired;

public class ProbandListStatusEntryReasonDecryptInitializer extends EncryptedFieldInitializer {

	@Autowired
	private ProbandListStatusEntryDao probandListStatusEntryDao;
	private ChunkedDaoOperationAdapter<ProbandListStatusEntryDao, ProbandListStatusEntry> probandListStatusEntryProcessor;

	public ProbandListStatusEntryReasonDecryptInitializer() {
	}

	@Override
	public long update(AuthenticationVO auth) throws Exception {
		authenticate(auth);
		probandListStatusEntryProcessor = new ChunkedDaoOperationAdapter<ProbandListStatusEntryDao, ProbandListStatusEntry>(probandListStatusEntryDao) {

			@Override
			protected boolean process(Collection<ProbandListStatusEntry> page,
					Object passThrough) throws Exception {
				return false;
			}

			@Override
			protected boolean process(ProbandListStatusEntry probandListStatusEntry, Object passThrough)
					throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				try {
					probandListStatusEntry.setReason((String) CryptoUtil.decryptValue(probandListStatusEntry.getReasonIv(), probandListStatusEntry.getEncryptedReason()));
					probandListStatusEntry.setReasonHash(null);
					probandListStatusEntry.setEncryptedReason(null);
					probandListStatusEntry.setReasonIv(null);
					this.dao.update(probandListStatusEntry);
					jobOutput.println("row updated");
					inOut.put("updated", ((Long) inOut.get("updated")) + 1l);
				} catch (Exception e) {
					jobOutput.println("row skipped: " + e.getMessage());
					inOut.put("skipped", ((Long) inOut.get("skipped")) + 1l);
				}
				return true;
			}
		};
		Map<String, Object> passThrough = new HashMap<String, Object>();
		passThrough.put("updated", 0l);
		passThrough.put("skipped", 0l);
		probandListStatusEntryProcessor.processEach(PageSizes.BIG, passThrough);
		long updated = (Long) passThrough.get("updated");
		long skipped = (Long) passThrough.get("skipped");
		jobOutput.println(updated + " rows updated, " + skipped + " skipped");
		return updated;
	}
}
