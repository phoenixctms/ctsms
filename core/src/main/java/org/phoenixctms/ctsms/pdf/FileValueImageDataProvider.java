package org.phoenixctms.ctsms.pdf;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.FileContentOutVO;
import org.phoenixctms.ctsms.vo.FileOutVO;

// image data of FILE input field values for PDF rendering. an image is loaded when it is painted instead of all up front, without
// attaching the bytes to the File entity of the session, and only the most recent ones are kept for values sharing a file, so an
// export does not hold all images in memory:
public class FileValueImageDataProvider {

	private final static int CACHE_SIZE = 4;
	private final FileDao fileDao;
	private final HashMap<Long, FileOutVO> fileValues;
	private final LinkedHashMap<Long, byte[]> cache;

	public FileValueImageDataProvider(FileDao fileDao) {
		this.fileDao = fileDao;
		fileValues = new HashMap<Long, FileOutVO>();
		cache = new LinkedHashMap<Long, byte[]>(CACHE_SIZE + 1, 0.75f, true) {

			@Override
			protected boolean removeEldestEntry(Map.Entry<Long, byte[]> eldest) {
				return size() > CACHE_SIZE;
			}
		};
	}

	public void add(FileOutVO fileValue) {
		if (fileValue != null && fileValue.getContentType() != null && fileValue.getContentType().getImage()) {
			fileValues.put(fileValue.getId(), fileValue);
		}
	}

	public boolean contains(Long fileId) {
		return fileValues.containsKey(fileId);
	}

	public byte[] get(Long fileId) throws Exception {
		FileOutVO fileValue = fileValues.get(fileId);
		if (fileValue == null) {
			return null;
		}
		if (cache.containsKey(fileId)) {
			return cache.get(fileId);
		}
		byte[] data;
		if (fileValue.isExternalFile() || CommonUtil.getUseFileEncryption(fileValue.getModule())) {
			FileContentOutVO content = fileDao.toFileContentOutVO(fileDao.load(fileId));
			data = content != null ? content.getDatas() : null;
		} else {
			data = fileDao.findData(fileId);
			if (data != null) {
				CryptoUtil.verifyMD5(data, fileValue.getMd5());
			}
		}
		if (data != null && data.length == 0) {
			data = null;
		}
		cache.put(fileId, data);
		return data;
	}
}
