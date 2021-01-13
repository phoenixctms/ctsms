package org.phoenixctms.ctsms.pdf;

import java.util.Collection;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public interface PDFContentPainter {

	public enum PageOrientation {
		PORTRAIT, LANDSCAPE
	};

	public void drawNextBlock(PDPageContentStream contentStream) throws Exception;

	public void drawPage(PDPageContentStream contentStream) throws Exception;

	public void drawPageBreakNewPage(PDPageContentStream contentStream) throws Exception;

	public void drawPageBreakOldPage(PDPageContentStream contentStream) throws Exception;

	public void drawPageNumbers(PDFImprinter writer) throws Exception;

	public PDRectangle getDefaultPageSize();

	public PageOrientation getPageOrientation();

	public String getTemplateFileName() throws Exception;

	public int getTemplatePageNum();

	public boolean hasNextBlock();

	public void loadFonts(PDDocument doc) throws Exception;

	public void loadImages(PDDocument doc) throws Exception;

	public boolean nextBlockFitsOnPage() throws Exception;

	public boolean nextBlockFitsOnFullPage() throws Exception;

	public void splitNextBlock() throws Exception;

	public void populateBlocks();

	public void reset();

	public void setPageHeight(float pageHeight);

	public void setPageWidth(float pageWidth);

	public void startNewPage();

	public void startNewPages(int pageCount);

	public void updateCursor();

	public Collection<byte[]> getAppendDocuments();
}
