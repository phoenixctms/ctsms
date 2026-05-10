package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

public class EcrfSpecificationPDFBlockCursor extends PDFBlockCursor {

	protected EcrfSpecificationPDFPainter painter;
	protected ECRFOutVO ecrf;

	public EcrfSpecificationPDFBlockCursor(EcrfSpecificationPDFPainter painter) {
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
	//	public float getPageWidth() {
	//		return painter.pageWidth;
	//	}

	public float getPageHeight() {
		return painter.pageHeight;
	}

	public ECRFOutVO getEcrf() {
		return ecrf;
	}

	public void setEcrf(ECRFOutVO ecrf) {
		this.ecrf = ecrf;
	}

	public UserOutVO getUser() {
		return painter.getPdfVO().getRequestingUser();
	}
}
