package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

public class ReimbursementsPDFBlockCursor extends PDFBlockCursor {

	private ReimbursementsPDFPainter painter;

	private ProbandOutVO proband;

	private TrialOutVO trial;

	public ReimbursementsPDFBlockCursor(ReimbursementsPDFPainter painter) {
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

	public PDFont getFontD() {
		return painter.getFontD();
	}

	public PDFont getFontE() {
		return painter.getFontE();
	}

	public PDFont getFontF() {
		return painter.getFontF();
	}

	public ProbandOutVO getProband() {
		return proband;
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	public void setProband(ProbandOutVO proband) {
		this.proband = proband;
	}

	public void setTrial(TrialOutVO trial) {
		this.trial = trial;
	}
}