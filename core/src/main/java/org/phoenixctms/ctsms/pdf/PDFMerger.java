package org.phoenixctms.ctsms.pdf;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFMergerUtility;

import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.security.VerifyMD5InputStream;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;

public class PDFMerger implements PDFOutput {

	private PDFMergerUtility merger;
	private FileDao fileDao;
	public final static String AGGREGATED_PDF_FILES_FILENAME_PREFIX = "aggregated_pdfs_";


	public PDFMerger(FileDao fileDao) {
		merger = new PDFMergerUtility();
		merger.setIgnoreAcroFormErrors(true);
		this.fileDao = fileDao;
	}

	@Override
	public boolean save(ByteArrayOutputStream pdfStream) throws Exception {
		merger.setDestinationStream(pdfStream);
		merger.mergeDocuments();
		return true;
	}

	public void setFiles(Collection<File> files) throws Exception {
		long fileCount = 0;
		if (files != null) {
			Iterator<File> filesIt = files.iterator();
			while (filesIt.hasNext()) {
				FileStreamOutVO streamVO = fileDao.toFileStreamOutVO(filesIt.next());
				if (streamVO.isDecrypted()) {
					((VerifyMD5InputStream) streamVO.getStream()).setOn(false);
					merger.addSource(streamVO.getStream());
					fileCount++;
				} else {
					streamVO.getStream().close();
				}
			}
		}
		if (fileCount == 0) {
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.NO_DECRYPTABLE_PDF_FILES, DefaultMessages.NO_DECRYPTABLE_PDF_FILES));
		}
	}

	@Override
	public void setMetadata(PDDocument doc) throws Exception {
	}
}
