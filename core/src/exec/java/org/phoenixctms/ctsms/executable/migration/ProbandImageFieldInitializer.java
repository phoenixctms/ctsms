package org.phoenixctms.ctsms.executable.migration;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.phoenixctms.ctsms.domain.ProbandContactParticulars;
import org.phoenixctms.ctsms.domain.ProbandContactParticularsDao;
import org.phoenixctms.ctsms.security.CipherText;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.TableSizes;
import org.phoenixctms.ctsms.vo.AuthenticationVO;

public class ProbandImageFieldInitializer extends EncryptedFieldInitializer {

	@Autowired
	private ProbandContactParticularsDao probandContactParticularsDao;
	private ChunkedDaoOperationAdapter<ProbandContactParticularsDao, ProbandContactParticulars> probandContactParticularsProcessor;

	public ProbandImageFieldInitializer() {
	}

	@Override
	public long update(AuthenticationVO auth) throws Exception {
		authenticate(auth);
		probandContactParticularsProcessor = new ChunkedDaoOperationAdapter<ProbandContactParticularsDao, ProbandContactParticulars>(probandContactParticularsDao) {

			@Override
			protected boolean process(Collection<ProbandContactParticulars> page,
					Object passThrough) throws Exception {
				return false;
			}

			@Override
			protected boolean process(ProbandContactParticulars particulars, Object passThrough)
					throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				boolean decrypted;
				try {
					Date dateOfBirth = (Date) CryptoUtil.decryptValue(particulars.getDateOfBirthIv(), particulars.getEncryptedDateOfBirth());
					decrypted = true;
				} catch (Exception e) {
					decrypted = false;
					jobOutput.println("row not decrypted");
				}
				if (decrypted) {
					if (particulars.getEncryptedData() == null) {
						CipherText cipherText = CryptoUtil.encryptValue(null);
						particulars.setFileNameIv(cipherText.getIv());
						particulars.setEncryptedFileName(cipherText.getCipherText());
						cipherText = CryptoUtil.encryptValue(null);
						particulars.setDataIv(cipherText.getIv());
						particulars.setEncryptedData(cipherText.getCipherText());
						this.dao.update(particulars);
						jobOutput.println("row updated");
						inOut.put("updated", ((Long) inOut.get("updated")) + 1l);
					} else {
						inOut.put("skipped", ((Long) inOut.get("skipped")) + 1l);
					}
				}
				return true;
			}
		};
		Map<String, Object> passThrough = new HashMap<String, Object>();
		passThrough.put("updated", 0l);
		passThrough.put("skipped", 0l);
		probandContactParticularsProcessor.processEach(TableSizes.TINY, passThrough);
		long updated = (Long) passThrough.get("updated");
		long skipped = (Long) passThrough.get("skipped");
		jobOutput.println(updated + " rows updated, " + skipped + " skipped");
		return updated;
	}
}
