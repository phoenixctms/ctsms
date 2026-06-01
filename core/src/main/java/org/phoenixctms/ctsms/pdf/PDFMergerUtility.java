package org.phoenixctms.ctsms.pdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.io.RandomAccess;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;

/**
 * Custom PDFMergerUtility that supports stamping a filename onto the merged pages.
 */
public class PDFMergerUtility extends org.apache.pdfbox.util.PDFMergerUtility {

	// Parallel list to keep track of filenames for the merged sources
	private final List<String> fileNames = new ArrayList<String>();
	// Tracks our iteration progress during the merge process
	private int currentSourceIndex = 0;

	public PDFMergerUtility() {
		super();
	}

	/**
	 * Custom method: Add an InputStream source and associate it with a specific filename.
	 *
	 * @param stream   The document stream to merge
	 * @param fileName The text/filename to render on this document's pages
	 */
	public void addSource(InputStream stream, String fileName) {
		super.addSource(stream);
		fileNames.add(fileName);
	}
	/* * OVERRIDES: We must override all other addSource methods so that documents
	 * added without a filename inject a 'null' into our parallel tracking list.
	 * This prevents IndexOutOfBounds exceptions.
	 */

	@Override
	public void addSource(File source) {
		super.addSource(source);
		fileNames.add(null);
	}

	@Override
	public void addSource(InputStream source) {
		super.addSource(source);
		fileNames.add(null);
	}

	@Override
	public void addSources(List<InputStream> sourcesList) {
		super.addSources(sourcesList);
		for (int i = 0; i < sourcesList.size(); i++) {
			fileNames.add(null);
		}
	}
	// Note: addSource(String) isn't overridden because the base class implementation 
	// calls this.addSource(new File(String)), which routes into our overridden method above.

	@Override
	public void mergeDocuments() throws IOException, COSVisitorException {
		// Reset our index counter right before the merging process starts
		this.currentSourceIndex = 0;
		super.mergeDocuments();
	}

	@Override
	public void mergeDocumentsNonSeq(RandomAccess scratchFile) throws IOException, COSVisitorException {
		// Reset our index counter right before the merging process starts
		this.currentSourceIndex = 0;
		super.mergeDocumentsNonSeq(scratchFile);
	}

	@Override
	public void appendDocument(PDDocument destination, PDDocument source) throws IOException {
		// Retrieve the filename mapped to the document currently being appended
		String fileName = fileNames.get(currentSourceIndex++);
		if (fileName != null) {
			stampFileNameOnPages(source, fileName);
		}
		// Let the parent class handle the actual page copying and form merging
		super.appendDocument(destination, source);
	}

	/**
	 * Renders the given filename onto every page of the provided document.
	 */
	private void stampFileNameOnPages(PDDocument document, String fileName) throws IOException {
		@SuppressWarnings("unchecked")
		String fileNameLabel = L10nUtil.getMessage(MessageCodes.AGGREGATED_PDF_FILENAMES_FORMAT, DefaultMessages.AGGREGATED_PDF_FILENAMES_FORMAT, fileName);
		if (!CommonUtil.isEmptyString(fileNameLabel)) {
			Iterator pagesIt = document.getDocumentCatalog().getAllPages().iterator();
			while (pagesIt.hasNext()) {
				PDPage page = (PDPage) pagesIt.next();
				// The 5-argument constructor ensures the graphics state is reset.
				// This prevents existing page transformations (like scaling or rotation) 
				// from ruining our text placement.
				PDPageContentStream contentStream = new PDPageContentStream(document, page, true, true, true);
				PDRectangle mediaBox = page.findMediaBox();
				float x = 5.0f;
				float y = mediaBox.getHeight() - 5.0f;
				PDFUtil.renderTextLine(contentStream, PDType1Font.HELVETICA, PDFUtil.FontSize.MEDIUM, Color.BLACK, fileNameLabel, x, y, PDFUtil.Alignment.TOP_LEFT);
				contentStream.close();
			}
		}
	}
}
