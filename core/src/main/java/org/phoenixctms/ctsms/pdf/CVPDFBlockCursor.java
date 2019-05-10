package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.vo.StaffOutVO;

public class CVPDFBlockCursor extends PDFBlockCursor {

	private CVPDFPainter painter;
	private StaffOutVO staff;

	public CVPDFBlockCursor(CVPDFPainter painter) {
		this.painter = painter;
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

	public StaffOutVO getStaff() {
		return staff;
	}

	public void setStaff(StaffOutVO staff) {
		this.staff = staff;
	}
}
