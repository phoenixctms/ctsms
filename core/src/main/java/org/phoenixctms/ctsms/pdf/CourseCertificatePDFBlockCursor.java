package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;

public class CourseCertificatePDFBlockCursor extends PDFBlockCursor {

	private CourseCertificatePDFPainter painter;



	private CourseParticipationStatusEntryOutVO participation;



	public CourseCertificatePDFBlockCursor(CourseCertificatePDFPainter painter) {
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

	public CourseParticipationStatusEntryOutVO getParticipation() {
		return participation;
	}

	public void setParticipation(CourseParticipationStatusEntryOutVO participation) {
		this.participation = participation;
	}

}
