package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.vo.CourseOutVO;

public class CourseParticipantListPDFBlockCursor extends PDFBlockCursor {

	private CourseParticipantListPDFPainter painter;
	private CourseOutVO course;

	public CourseParticipantListPDFBlockCursor(CourseParticipantListPDFPainter painter) {
		this.painter = painter;
	}

	public CourseOutVO getCourse() {
		return course;
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

	public void setCourse(CourseOutVO course) {
		this.course = course;
	}
}
