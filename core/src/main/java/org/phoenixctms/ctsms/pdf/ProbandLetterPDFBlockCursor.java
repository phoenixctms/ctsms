package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;

public class ProbandLetterPDFBlockCursor extends PDFBlockCursor {

	private ProbandLetterPDFPainter painter;

	private ProbandAddressOutVO address;

	public ProbandLetterPDFBlockCursor(ProbandLetterPDFPainter painter) {
		this.painter = painter;
	}

	public ProbandAddressOutVO getAddress() {
		return address;
	}

	public PDFont getFontA() {
		return painter.getFontA();
	}

	public PDFont getFontB() {
		return painter.getFontB();
	}

	public PDFont getFontC() {
		return painter.getFontC();
	}

	public void setAddress(ProbandAddressOutVO address) {
		this.address = address;
	}
}
