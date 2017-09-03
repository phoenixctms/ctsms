package org.phoenixctms.ctsms.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PDFStream implements PDFOutput {

	private byte[] documentData;
	private ByteArrayInputStream inputStream;

	public PDFStream() {
		documentData = null;
		inputStream = null;
	}

	public ByteArrayInputStream getInputStream() {
		if (documentData != null) {
			if (inputStream == null) {
				inputStream = new ByteArrayInputStream(documentData);
			}
			return inputStream;
		}
		return null;
	}

	public int getSize() {
		if (documentData != null) {
			return documentData.length;
		}
		return 0;
	}

	@Override
	public boolean save(ByteArrayOutputStream pdfStream) throws Exception {
		inputStream = null;
		documentData = pdfStream.toByteArray();
		return true;
	}

	@Override
	public void setMetadata(PDDocument doc) throws Exception {
	}
}
