package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.PageSizes;
import org.phoenixctms.ctsms.vo.AuthenticationVO;

public abstract class EncryptedStringHashReindexInitializer<ENTITY, DAO> extends EncryptedFieldInitializer {

	@FunctionalInterface
	protected interface ByteArrayAccessor<ENTITY> {

		byte[] access(ENTITY entity);
	}

	@FunctionalInterface
	protected interface HashSetter<ENTITY> {

		void set(ENTITY entity, byte[] hash);
	}

	@FunctionalInterface
	protected interface PlainTextTransformer {

		String transform(String plainText) throws Exception;
	}

	protected static abstract class StringHashField<ENTITY> {

		protected abstract String getName();

		protected abstract byte[] getIv(ENTITY entity);

		protected abstract byte[] getEncrypted(ENTITY entity);

		protected abstract byte[] getHash(ENTITY entity);

		protected abstract void setHash(ENTITY entity, byte[] hash);

		protected boolean isPresent(ENTITY entity) {
			return getEncrypted(entity) != null;
		}

		protected String transformPlainText(String plainText) throws Exception {
			return plainText;
		}
	}

	protected static final class SimpleStringHashField<ENTITY> extends StringHashField<ENTITY> {

		private final String name;
		private final ByteArrayAccessor<ENTITY> ivAccessor;
		private final ByteArrayAccessor<ENTITY> encryptedAccessor;
		private final ByteArrayAccessor<ENTITY> hashAccessor;
		private final HashSetter<ENTITY> hashSetter;
		private final PlainTextTransformer plainTextTransformer;

		private SimpleStringHashField(String name, ByteArrayAccessor<ENTITY> ivAccessor, ByteArrayAccessor<ENTITY> encryptedAccessor,
				ByteArrayAccessor<ENTITY> hashAccessor, HashSetter<ENTITY> hashSetter, PlainTextTransformer plainTextTransformer) {
			this.name = name;
			this.ivAccessor = ivAccessor;
			this.encryptedAccessor = encryptedAccessor;
			this.hashAccessor = hashAccessor;
			this.hashSetter = hashSetter;
			this.plainTextTransformer = plainTextTransformer;
		}

		@Override
		protected String getName() {
			return name;
		}

		@Override
		protected byte[] getIv(ENTITY entity) {
			return ivAccessor.access(entity);
		}

		@Override
		protected byte[] getEncrypted(ENTITY entity) {
			return encryptedAccessor.access(entity);
		}

		@Override
		protected byte[] getHash(ENTITY entity) {
			return hashAccessor.access(entity);
		}

		@Override
		protected void setHash(ENTITY entity, byte[] hash) {
			hashSetter.set(entity, hash);
		}

		@Override
		protected String transformPlainText(String plainText) throws Exception {
			if (plainTextTransformer != null) {
				return plainTextTransformer.transform(plainText);
			}
			return plainText;
		}
	}

	protected static <ENTITY> StringHashField<ENTITY> encryptedStringField(String name, ByteArrayAccessor<ENTITY> ivAccessor,
			ByteArrayAccessor<ENTITY> encryptedAccessor, ByteArrayAccessor<ENTITY> hashAccessor, HashSetter<ENTITY> hashSetter) {
		return new SimpleStringHashField<ENTITY>(name, ivAccessor, encryptedAccessor, hashAccessor, hashSetter, null);
	}

	protected static <ENTITY> StringHashField<ENTITY> encryptedStringField(String name, ByteArrayAccessor<ENTITY> ivAccessor,
			ByteArrayAccessor<ENTITY> encryptedAccessor, ByteArrayAccessor<ENTITY> hashAccessor, HashSetter<ENTITY> hashSetter,
			PlainTextTransformer plainTextTransformer) {
		return new SimpleStringHashField<ENTITY>(name, ivAccessor, encryptedAccessor, hashAccessor, hashSetter, plainTextTransformer);
	}

	protected abstract DAO getDao();

	protected abstract List<StringHashField<ENTITY>> getStringHashFields();

	protected abstract void persist(DAO dao, ENTITY entity) throws Exception;

	protected String getEntityLabel() {
		return getClass().getSimpleName();
	}

	@Override
	public long update(AuthenticationVO auth) throws Exception {
		authenticate(auth);
		return reindexLogged();
	}

	protected long reindexLogged() throws Exception {
		jobOutput.println("starting " + getEntityLabel());
		return reindex();
	}

	protected long reindex() throws Exception {
		final List<StringHashField<ENTITY>> stringHashFields = getStringHashFields();
		ChunkedDaoOperationAdapter<DAO, ENTITY> processor = new ChunkedDaoOperationAdapter<DAO, ENTITY>(getDao()) {

			@Override
			protected boolean process(Collection<ENTITY> page, Object passThrough) throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				inOut.put("pageUpdated", 0l);
				Iterator<ENTITY> it = page.iterator();
				while (it.hasNext()) {
					if (!process(it.next(), passThrough)) {
						return false;
					}
				}
				printProgress(passThrough);
				return true;
			}

			@Override
			protected boolean process(ENTITY entity, Object passThrough) throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				boolean updated = false;
				Iterator<StringHashField<ENTITY>> fieldIt = stringHashFields.iterator();
				while (fieldIt.hasNext()) {
					StringHashField<ENTITY> field = fieldIt.next();
					if (!field.isPresent(entity)) {
						continue;
					}
					try {
						String plainText = (String) CryptoUtil.decryptValue(field.getIv(entity), field.getEncrypted(entity));
						byte[] newHash = CryptoUtil.hashForSearch(field.transformPlainText(plainText));
						if (!Arrays.equals(newHash, field.getHash(entity))) {
							field.setHash(entity, newHash);
							updated = true;
						}
					} catch (Exception e) {
						jobOutput.println(field.getName() + ": row not decrypted");
						inOut.put("failed", ((Long) inOut.get("failed")) + 1l);
					}
				}
				if (updated) {
					persist(this.dao, entity);
					inOut.put("pageUpdated", ((Long) inOut.get("pageUpdated")) + 1l);
					inOut.put("updated", ((Long) inOut.get("updated")) + 1l);
				} else {
					inOut.put("skipped", ((Long) inOut.get("skipped")) + 1l);
				}
				return true;
			}
		};
		Map<String, Object> passThrough = new HashMap<String, Object>();
		passThrough.put("updated", 0l);
		passThrough.put("skipped", 0l);
		passThrough.put("failed", 0l);
		processor.processPages(PageSizes.DEFAULT, passThrough);
		long updated = (Long) passThrough.get("updated");
		long skipped = (Long) passThrough.get("skipped");
		long failed = (Long) passThrough.get("failed");
		jobOutput.println("finished " + getEntityLabel() + ": " + formatProgressSummary(updated, skipped, failed));
		return updated;
	}

	private void printProgress(Object passThrough) {
		Map<String, Object> inOut = (Map<String, Object>) passThrough;
		long pageUpdated = (Long) inOut.get("pageUpdated");
		if (pageUpdated > 0) {
			jobOutput.println("row updated");
		}
		jobOutput.println(getEntityLabel() + ": " + formatProgressSummary((Long) inOut.get("updated"), (Long) inOut.get("skipped"), (Long) inOut.get("failed")));
	}

	private String formatProgressSummary(long updated, long skipped, long failed) {
		return updated + " rows updated, " + skipped + " skipped, " + failed + " field decrypt failures";
	}
}
