package org.phoenixctms.ctsms.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.pdfwriter.COSWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.phoenixctms.ctsms.pdf.PDFContentPainter.PageOrientation;

public class PDFImprinter {

	private PDFContentPainter painter;
	private PDFOutput output;
	private ByteArrayOutputStream pdfStream;
	private PDDocument doc;
	private PDFMergerUtility appender;
	private PDPage page;
	private PDPageContentStream contentStream;
	private PDDocument templateDoc;
	private List templatePages;
	private COSWriter docWriter;

	public PDFImprinter() {
	}

	public PDFImprinter(PDFContentPainter painter, PDFOutput output) {
		this.painter = painter;
		this.output = output;
	}

	public void closeContentStream() throws IOException {
		contentStream.close();
	}

	private void createPage(int pageNum) throws Exception {
		String templateFileName;
		if (painter != null) {
			templateFileName = painter.getTemplateFileName();
		} else {
			templateFileName = null;
		}
		if (templateFileName != null && templateFileName.length() > 0) {
			if (templateDoc == null) {
				templateDoc = PDDocument.load(templateFileName);
				templatePages = templateDoc.getDocumentCatalog().getAllPages();
			}
			if (pageNum > templatePages.size()) {
				page = doc.importPage((PDPage) templatePages.get(templatePages.size() - 1));
			} else if (pageNum > 0) {
				page = doc.importPage((PDPage) templatePages.get(pageNum - 1));
			} else {
				page = new PDPage(PDPage.PAGE_SIZE_A4);
				doc.addPage(page);
			}
		} else {
			if (painter != null) {
				page = new PDPage(painter.getDefaultPageSize());
				if (PageOrientation.LANDSCAPE.equals(painter.getPageOrientation())) {
					page.setRotation(90);
				}
			} else {
				page = new PDPage(PDPage.PAGE_SIZE_A4);
			}
			doc.addPage(page);
		}
	}

	public PDDocument getDocument() {
		return doc;
	}

	public PDFOutput getOutput() {
		return output;
	}

	public PDFContentPainter getPainter() {
		return painter;
	}

	private void openContentStream() throws IOException {
		openContentStream(page, true, true);
	}

	public PDPageContentStream openContentStream(PDPage page) throws IOException {
		return openContentStream(page, true, false);
	}

	private PDPageContentStream openContentStream(PDPage page, boolean setPageSize, boolean applyPageOrientation) throws IOException {
		contentStream = new PDPageContentStream(doc, page, true, false);
		if (painter != null) {
			PDRectangle pageSize = page.findMediaBox();
			if (PageOrientation.LANDSCAPE.equals(painter.getPageOrientation())) {
				if (setPageSize) {
					painter.setPageHeight(pageSize.getWidth());
					painter.setPageWidth(pageSize.getHeight());
				}
				if (applyPageOrientation) {
					contentStream.concatenate2CTM(0, 1, -1, 0, pageSize.getWidth(), 0); // cos(theta) sin(theta) -sin(theta) cos(theta) 0 0 cm
				}
			} else {
				if (setPageSize) {
					painter.setPageHeight(pageSize.getHeight());
					painter.setPageWidth(pageSize.getWidth());
				}
			}
		}
		return contentStream;
	}

	public boolean render() throws Exception {
		templateDoc = null;
		templatePages = null;
		doc = null;
		appender = null;
		page = null;
		contentStream = null;
		try {
			doc = new PDDocument();
			if (output != null) {
				output.setMetadata(doc);
			}
			if (painter != null) {
				painter.reset();
				painter.loadFonts(doc);
				// https://issues.apache.org/jira/browse/PDFBOX-1394
				painter.loadImages(doc);
				painter.populateBlocks();
				if (painter.hasNextBlock()) {
					painter.updateCursor();
				}
				painter.startNewPage();
				createPage(painter.getTemplatePageNum());
				openContentStream();
				while (painter.hasNextBlock()) {
					if (!painter.nextBlockFitsOnFullPage()) {
						painter.splitNextBlock();
					}
					if (!painter.nextBlockFitsOnPage()) {
						painter.drawPageBreakOldPage(contentStream);
						painter.drawPage(contentStream);
						closeContentStream();
						painter.updateCursor();
						painter.startNewPage();
						createPage(painter.getTemplatePageNum());
						openContentStream();
						painter.drawPageBreakNewPage(contentStream);
					}
					painter.drawNextBlock(contentStream);
				}
				painter.drawPage(contentStream);
				closeContentStream();
				appendPages();
			} else {
				createPage(1);
			}
			if (painter != null) {
				painter.drawPageNumbers(this);
			}
			pdfStream = new ByteArrayOutputStream();
			docWriter = new COSWriter(pdfStream);
			docWriter.write(doc);
			if (output != null) {
				return output.save(pdfStream);
			} else {
				return false;
			}
		} finally {
			templatePages = null;
			if (templateDoc != null) {
				templateDoc.close();
				templateDoc = null;
			}
			if (doc != null) {
				doc.close();
				doc = null;
			}
			if (pdfStream != null) {
				pdfStream.close();
				pdfStream = null;
			}
			if (docWriter != null) {
				docWriter.close();
				docWriter = null;
			}
		}
	}

	public void setOutput(PDFOutput output) {
		this.output = output;
	}

	public void setPainter(PDFContentPainter painter) {
		this.painter = painter;
	}

	private PDFMergerUtility getAppender() {
		if (appender == null) {
			appender = new PDFMergerUtility();
			appender.setIgnoreAcroFormErrors(true);
		}
		return appender;
	}

	private void appendPages() throws Exception {
		if (painter != null) {
			Collection<byte[]> documentDatas = painter.getAppendDocuments();
			if (documentDatas != null) {
				Iterator<byte[]> it = documentDatas.iterator();
				while (it.hasNext()) {
					ByteArrayInputStream documentStream = new ByteArrayInputStream(it.next());
					PDDocument document = null;
					try {
						document = PDDocument.load(documentStream);
						getAppender().appendDocument(doc, document);
						painter.startNewPages(document.getNumberOfPages());
					} finally {
						if (document != null) {
							document.close();
						}
					}
					documentStream.close();
				}
			}
		}
	}
}
