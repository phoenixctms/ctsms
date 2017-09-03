package org.phoenixctms.ctsms.pdf;

import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface PDFOutput {

	public boolean save(ByteArrayOutputStream pdfStream) throws Exception;

	public void setMetadata(PDDocument doc) throws Exception;
}
