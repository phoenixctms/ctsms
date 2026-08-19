package org.phoenixctms.ctsms.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.PDFStreamEngine;
import org.apache.pdfbox.util.operator.Concatenate;
import org.apache.pdfbox.util.operator.GRestore;
import org.apache.pdfbox.util.operator.GSave;

/**
 * Prepares an existing PDF page for appended drawing. Microsoft Word PDFs often
 * leave a non-identity CTM and/or a non-zero /Rotate, which makes PDFBox edits
 * render upside down.
 */
public final class PDFPageNormalizer {

	private static final float IDENTITY_EPSILON = 0.001f;

	private PDFPageNormalizer() {
	}

	public static void normalize(PDDocument document, PDPage page) throws IOException {
		if (document == null || page == null) {
			return;
		}
		if (page.getCOSDictionary().getDictionaryObject(COSName.CONTENTS) != null) {
			boolean leftoverCtm;
			try {
				leftoverCtm = hasLeftoverCtm(page);
			} catch (IOException e) {
				leftoverCtm = true;
			}
			if (leftoverCtm) {
				wrapInSaveRestore(document, page);
			}
		}
		flattenRotation(document, page);
	}

	private static boolean hasLeftoverCtm(PDPage page) throws IOException {
		LeftoverCtmDetector detector = new LeftoverCtmDetector();
		PDResources resources = page.findResources();
		if (resources == null) {
			resources = new PDResources();
		}
		COSBase contents = page.getCOSDictionary().getDictionaryObject(COSName.CONTENTS);
		if (contents instanceof COSArray) {
			COSArray array = (COSArray) contents;
			boolean first = true;
			for (int i = 0; i < array.size(); i++) {
				COSBase item = array.getObject(i);
				if (item instanceof COSStream) {
					if (first) {
						detector.processStream(page, resources, (COSStream) item);
						first = false;
					} else {
						detector.processSubStream(page, resources, (COSStream) item);
					}
				}
			}
			if (first) {
				return false;
			}
		} else if (contents instanceof COSStream) {
			detector.processStream(page, resources, (COSStream) contents);
		} else {
			return false;
		}
		return !isIdentity(detector.getGraphicsState().getCurrentTransformationMatrix());
	}

	private static boolean isIdentity(Matrix matrix) {
		if (matrix == null) {
			return true;
		}
		float[][] values = matrix.getValues();
		return nearly(values[0][0], 1.0f) && nearly(values[0][1], 0.0f)
				&& nearly(values[1][0], 0.0f) && nearly(values[1][1], 1.0f)
				&& nearly(values[2][0], 0.0f) && nearly(values[2][1], 0.0f);
	}

	private static boolean nearly(float actual, float expected) {
		return Math.abs(actual - expected) < IDENTITY_EPSILON;
	}

	private static void wrapInSaveRestore(PDDocument document, PDPage page) throws IOException {
		prependRawCommands(document, page, "q\n");
		appendRawCommands(document, page, "Q\n");
	}

	private static void flattenRotation(PDDocument document, PDPage page) throws IOException {
		int rotation = page.findRotation();
		if (rotation == 0) {
			return;
		}
		rotation = ((rotation % 360) + 360) % 360;
		if (rotation == 0) {
			return;
		}
		PDRectangle mediaBox = page.findMediaBox();
		float width = mediaBox.getWidth();
		float height = mediaBox.getHeight();
		String cm;
		boolean swap = false;
		switch (rotation) {
			case 90:
				cm = formatCm(0, 1, -1, 0, width, 0);
				swap = true;
				break;
			case 180:
				cm = formatCm(-1, 0, 0, -1, width, height);
				break;
			case 270:
				cm = formatCm(0, -1, 1, 0, 0, height);
				swap = true;
				break;
			default:
				return;
		}
		prependRawCommands(document, page, cm);
		if (swap) {
			swapBox(page);
		}
		page.setRotation(0);
	}

	private static void swapBox(PDPage page) {
		PDRectangle mediaBox = page.findMediaBox();
		page.setMediaBox(new PDRectangle(mediaBox.getHeight(), mediaBox.getWidth()));
		PDRectangle cropBox = page.getCropBox();
		if (cropBox != null && cropBox != mediaBox) {
			page.setCropBox(new PDRectangle(cropBox.getHeight(), cropBox.getWidth()));
		}
	}

	private static String formatCm(float a, float b, float c, float d, float e, float f) {
		return String.format(Locale.US, "%f %f %f %f %f %f cm\n", a, b, c, d, e, f);
	}

	private static void prependRawCommands(PDDocument document, PDPage page, String commands) throws IOException {
		PDStream stream = createCommandStream(document, commands);
		COSBase contents = page.getCOSDictionary().getDictionaryObject(COSName.CONTENTS);
		if (contents instanceof COSArray) {
			((COSArray) contents).add(0, stream.getStream());
		} else if (contents instanceof COSStream) {
			COSArray array = new COSArray();
			array.add(stream.getStream());
			array.add(contents);
			page.getCOSDictionary().setItem(COSName.CONTENTS, array);
		} else {
			page.setContents(stream);
		}
	}

	private static void appendRawCommands(PDDocument document, PDPage page, String commands) throws IOException {
		PDStream stream = createCommandStream(document, commands);
		COSBase contents = page.getCOSDictionary().getDictionaryObject(COSName.CONTENTS);
		if (contents instanceof COSArray) {
			((COSArray) contents).add(stream.getStream());
		} else if (contents instanceof COSStream) {
			COSArray array = new COSArray();
			array.add(contents);
			array.add(stream.getStream());
			page.getCOSDictionary().setItem(COSName.CONTENTS, array);
		} else {
			page.setContents(stream);
		}
	}

	private static PDStream createCommandStream(PDDocument document, String commands) throws IOException {
		PDStream stream = new PDStream(document);
		OutputStream out = stream.createOutputStream();
		try {
			out.write(commands.getBytes("ISO-8859-1"));
		} finally {
			out.close();
		}
		return stream;
	}

	private static final class LeftoverCtmDetector extends PDFStreamEngine {

		private LeftoverCtmDetector() {
			registerOperatorProcessor("q", new GSave());
			registerOperatorProcessor("Q", new GRestore());
			registerOperatorProcessor("cm", new Concatenate());
		}
	}
}
