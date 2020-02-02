package org.phoenixctms.ctsms.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.jempbox.xmp.XMPMetadata;
import org.apache.jempbox.xmp.XMPSchemaBasic;
import org.apache.jempbox.xmp.XMPSchemaDublinCore;
import org.apache.jempbox.xmp.XMPSchemaPDF;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public abstract class PDFPainterBase implements PDFContentPainter {

	protected Date now;
	private ArrayList<Integer> totalPageCounts;
	private int pageNum;
	private int templatePageNum;
	private boolean drawPageNumbers;
	protected final static PDFont DEFAULT_BASE_FONT = PDType1Font.HELVETICA;

	protected PDFPainterBase() {
		now = new Date();
		totalPageCounts = new ArrayList<Integer>();
	}

	@Override
	public void drawPageBreakNewPage(PDPageContentStream contentStream) throws Exception {
	}

	@Override
	public void drawPageBreakOldPage(PDPageContentStream contentStream) throws Exception {
	}

	@Override
	public boolean nextBlockFitsOnFullPage() throws Exception {
		return true;
	}

	@Override
	public void splitNextBlock() throws Exception {
	}

	protected abstract void drawPageNumber(PDFImprinter writer, PDPage page, int pageNumber, int totalPages) throws IOException;

	@Override
	public void drawPageNumbers(PDFImprinter writer)
			throws Exception {
		if (drawPageNumbers) {
			int i = 1;
			int j = 0;
			Iterator it = writer.getDocument().getDocumentCatalog().getAllPages().iterator();
			while (it.hasNext()) {
				int totalPages = totalPageCounts.get(j);
				drawPageNumber(writer, (PDPage) it.next(), i, totalPages);
				i++;
				if (i > totalPages) {
					j++;
					i = 1;
				}
			}
		}
	}

	@Override
	public int getTemplatePageNum() {
		return templatePageNum;
	}

	@Override
	public void reset() {
		now.setTime(System.currentTimeMillis());
		pageNum = 0;
		templatePageNum = 0;
		totalPageCounts.clear();
	}

	public void setDrawPageNumbers(boolean drawPageNumbers) {
		this.drawPageNumbers = drawPageNumbers;
	}

	public void setMetadata(PDDocument doc) throws Exception {
		PDDocumentCatalog catalog = doc.getDocumentCatalog();
		PDDocumentInformation info = doc.getDocumentInformation();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(now);
		XMPMetadata metadata = new XMPMetadata();
		XMPSchemaPDF pdfSchema = metadata.addPDFSchema();
		pdfSchema.setKeywords(info.getKeywords());
		pdfSchema.setProducer(info.getProducer());
		XMPSchemaBasic basicSchema = metadata.addBasicSchema();
		basicSchema.setModifyDate(cal);
		basicSchema.setCreateDate(cal);
		basicSchema.setCreatorTool(info.getCreator());
		basicSchema.setMetadataDate(cal);
		XMPSchemaDublinCore dcSchema = metadata.addDublinCoreSchema();
		dcSchema.setTitle(info.getTitle());
		dcSchema.addCreator("PDFBox");
		dcSchema.setDescription(info.getSubject());
		PDMetadata metadataStream = new PDMetadata(doc);
		metadataStream.importXMPMetadata(metadata);
		catalog.setMetadata(metadataStream);
	}

	protected void startNewPage(boolean reset) {
		startNewPage(reset, reset);
	}

	protected void startNewPage(boolean resetTemplate, boolean resetPageCount) {
		pageNum++;
		if (resetTemplate) {
			templatePageNum = 1;
		} else {
			templatePageNum++;
		}
		if (resetPageCount) {
			totalPageCounts.add(1);
		} else {
			totalPageCounts.set(totalPageCounts.size() - 1, totalPageCounts.get(totalPageCounts.size() - 1) + 1);
		}
	}
}
