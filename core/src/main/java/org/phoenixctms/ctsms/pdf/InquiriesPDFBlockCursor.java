package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.pdf.InquiriesPDFBlock.BlockType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

public class InquiriesPDFBlockCursor extends PDFBlockCursor implements InputFieldPDFBlockCursor {

	private InquiriesPDFPainter painter;
	private float categoryY;
	private InquiriesPDFBlock categoryBlock;
	private InquiriesPDFBlock probandTrialBlock;
	private ProbandOutVO proband;
	private TrialOutVO trial;

	public InquiriesPDFBlockCursor(InquiriesPDFPainter painter) {
		this.painter = painter;
		clearBlocks();
	}

	public void clearBlocks() {
		this.categoryBlock = null;
		this.probandTrialBlock = null;
	}

	@Override
	public float getBlockIndentedCenterX() throws Exception {
		return getBlockIndentedX() + getBlockIndentedWidth() / 2.0f;
	}

	@Override
	public float getBlockIndentedWidth() throws Exception {
		return getBlockIndentedWidth(true);
	}

	public float getBlockIndentedWidth(boolean max) throws Exception {
		float width = blockWidth;
		if (!max || hasCategory()) {
			// width -= //getSectionWidth() +
			// Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		}
		return width;
	}

	@Override
	public float getBlockIndentedX() throws Exception {
		float x = blockX;
		if (hasCategory()) {
			// x += getSectionWidth() +
			// Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		}
		return x;
	}

	public InquiriesPDFBlock getCategoryBlock() {
		return categoryBlock;
	}

	public String getCategoryLabel() {
		if (categoryBlock != null) {
			String category = categoryBlock.getCategory();
			if (category != null && category.length() > 0) {
				return L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.INQUIRY_CATEGORY, PDFUtil.DEFAULT_LABEL, category);
			}
		}
		return null;
	}

	public float getCategoryY() {
		return categoryY;
	}

	@Override
	public PDFJpeg getCheckboxCheckedImage() {
		return painter.getCheckboxCheckedImage();
	}

	@Override
	public PDFJpeg getCheckboxCheckedPresetImage() {
		return painter.getCheckboxCheckedPresetImage();
	}

	@Override
	public PDFJpeg getCheckboxUncheckedImage() {
		return painter.getCheckboxUncheckedImage();
	}

	@Override
	public PDFont getFontA() {
		return painter.getFontA();
	}

	@Override
	public PDFont getFontB() {
		return painter.getFontB();
	}

	@Override
	public PDFont getFontC() {
		return painter.getFontC();
	}

	@Override
	public PDFont getFontD() {
		return painter.getFontD();
	}

	public ProbandOutVO getProband() {
		return proband;
	}

	public InquiriesPDFBlock getProbandTrialBlock() {
		return probandTrialBlock;
	}

	@Override
	public PDFJpeg getRadioOffImage() {
		return painter.getRadioOffImage();
	}

	@Override
	public PDFJpeg getRadioOnImage() {
		return painter.getRadioOnImage();
	}

	@Override
	public PDFJpeg getRadioOnPresetImage() {
		return painter.getRadioOnPresetImage();
	}

	@Override
	public PDFJpeg getSelectboxCheckedImage() {
		return painter.getSelectboxCheckedImage();
	}

	@Override
	public PDFJpeg getSelectboxCheckedPresetImage() {
		return painter.getSelectboxCheckedPresetImage();
	}

	@Override
	public PDFJpeg getSelectboxUncheckedImage() {
		return painter.getSelectboxUncheckedImage();
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	public boolean hasCategory() {
		return !CommonUtil.isEmptyString(getCategoryLabel());
	}

	public void setBlocks(InquiriesPDFBlock block) {
		if (block != null) {
			if (BlockType.NEW_CATEGORY.equals(block.getType())) {
				this.categoryBlock = block;
			} else if (BlockType.NEW_PROBAND_TRIAL.equals(block.getType())) {
				this.probandTrialBlock = block;
			}
		}
	}

	public void setCategoryY(float categoryY) {
		this.categoryY = categoryY;
	}

	public void setProband(ProbandOutVO proband) {
		this.proband = proband;
	}

	public void setTrial(TrialOutVO trial) {
		this.trial = trial;
	}
}
