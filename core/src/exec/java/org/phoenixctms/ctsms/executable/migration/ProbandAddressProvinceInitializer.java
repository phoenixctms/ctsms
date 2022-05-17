package org.phoenixctms.ctsms.executable.migration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.phoenixctms.ctsms.domain.ProbandAddress;
import org.phoenixctms.ctsms.domain.ProbandAddressDao;
import org.phoenixctms.ctsms.security.CipherText;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.PageSizes;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.springframework.beans.factory.annotation.Autowired;

public class ProbandAddressProvinceInitializer extends EncryptedFieldInitializer {

	@Autowired
	private ProbandAddressDao probandAddressDao;
	private ChunkedDaoOperationAdapter<ProbandAddressDao, ProbandAddress> probandAddressProcessor;

	public ProbandAddressProvinceInitializer() {
	}

	@Override
	public long update(AuthenticationVO auth) throws Exception {
		authenticate(auth);
		probandAddressProcessor = new ChunkedDaoOperationAdapter<ProbandAddressDao, ProbandAddress>(probandAddressDao) {

			@Override
			protected boolean process(Collection<ProbandAddress> page,
					Object passThrough) throws Exception {
				return false;
			}

			@Override
			protected boolean process(ProbandAddress address, Object passThrough)
					throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				boolean decrypted;
				try {
					String countryName = (String) CryptoUtil.decryptValue(address.getCountryNameIv(), address.getEncryptedCountryName());
					decrypted = true;
				} catch (Exception e) {
					decrypted = false;
					jobOutput.println("row not decrypted");
				}
				if (decrypted) {
					if (address.getEncryptedProvince() == null) {
						CipherText cipherText = CryptoUtil.encryptValue(null);
						address.setProvinceIv(cipherText.getIv());
						address.setEncryptedProvince(cipherText.getCipherText());
						this.dao.update(address);
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
		probandAddressProcessor.processEach(PageSizes.TINY, passThrough);
		long updated = (Long) passThrough.get("updated");
		long skipped = (Long) passThrough.get("skipped");
		jobOutput.println(updated + " rows updated, " + skipped + " skipped");
		return updated;
	}
}
