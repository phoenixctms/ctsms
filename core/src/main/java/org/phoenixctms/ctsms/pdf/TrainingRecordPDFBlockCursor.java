package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.vo.StaffOutVO;

public class TrainingRecordPDFBlockCursor extends PDFBlockCursor {

	protected TrainingRecordPDFPainter painter;
	protected StaffOutVO staff;

	public TrainingRecordPDFBlockCursor(TrainingRecordPDFPainter painter) {
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

	public PDFJpeg getCheckboxCheckedImage() {
		return painter.getCheckboxCheckedImage();
	}

	public PDFJpeg getCheckboxUncheckedImage() {
		return painter.getCheckboxUncheckedImage();
	}
}
