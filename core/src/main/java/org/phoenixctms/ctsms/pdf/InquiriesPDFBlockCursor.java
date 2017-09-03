
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
	// private float previousSectionY;
	private float categoryY;
	// private float previousIndexY;
	// private float indexY;
	private InquiriesPDFBlock categoryBlock;
	// private EcrfPDFBlock indexBlock;
	private InquiriesPDFBlock probandTrialBlock;

	private ProbandOutVO proband;

	private TrialOutVO trial;

	// private EcrfPDFBlock previousBlock;
	// private EcrfPDFBlock block;
	public InquiriesPDFBlockCursor(InquiriesPDFPainter painter) {
		this.painter = painter;
		clearBlocks();
	}

	public void clearBlocks() {
		this.categoryBlock = null;
		// this.indexBlock = null;
		this.probandTrialBlock = null;
		// this.previousBlock = null;
		// this.block = null;
	}

	public float getBlockIndentedCenterX() throws Exception {
		return getBlockIndentedX() + getBlockIndentedWidth() / 2.0f;
	}

	public float getBlockIndentedWidth() throws Exception {
		return getBlockIndentedWidth(true);
	}

	public float getBlockIndentedWidth(boolean max) throws Exception {
		float width = blockWidth;
		if (!max || hasCategory()) {
			// width -= //getSectionWidth() +
			// Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		}
		// if (!max || hasIndex()) {
		// // width -= // getIndexWidth() +
		// // 2.0f * Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		// }
		return width;
	}

	public float getBlockIndentedX() throws Exception {
		float x = blockX;
		if (hasCategory()) {
			// x += getSectionWidth() +
			// Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		}
		// if (hasIndex()) {
		// // x += // getIndexWidth() +
		// // Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		// }
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

	public PDFJpeg getCheckboxCheckedImage() {
		return painter.getCheckboxCheckedImage();
	}

	@Override
	public PDFJpeg getCheckboxCheckedPresetImage() {
		return painter.getCheckboxCheckedPresetImage();
	}

	public PDFJpeg getCheckboxUncheckedImage() {
		return painter.getCheckboxUncheckedImage();
	}

	public PDFont getFontA() {
		return painter.getFontA();
	}

	public PDFont getFontB() {
		return painter.getFontB();
	}

	// public float getIndexY() {
	// return indexY;
	// }

	// public EcrfPDFBlock getIndexBlock() {
	// return indexBlock;
	// }
	// public float getIndexWidth() throws Exception {
	// return 0.0f; //PDFUtil.renderTextLine(null, painter.getFontA(), FontSize.BIG, null, null, 0.0f, 0.0f, null);
	// }
	// public EcrfPDFBlock getPreviousBlock() {
	// return previousBlock;
	// }
	// public String getIndexLabel() {
	// if (indexBlock != null) {
	// Long index = indexBlock.getIndex();
	// if (index != null ) {
	// return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_FIELD_INDEX, PDFUtil.DEFAULT_LABEL, index.toString(), indexBlock.getSection());
	// }
	// }
	// return null;
	// }
	// public float getIndexY() {
	// return indexY;
	// }
	// public PDFont getFontE() {
	// return painter.getFontE();
	// }
	//
	// public PDFont getFontF() {
	// return painter.getFontF();
	// }
	public PDFont getFontC() {
		return painter.getFontC();
	}

	public PDFont getFontD() {
		return painter.getFontD();
	}

	public ProbandOutVO getProband() {
		return proband;
	}

	public InquiriesPDFBlock getProbandTrialBlock() {
		return probandTrialBlock;
	}

	public PDFJpeg getRadioOffImage() {
		return painter.getRadioOffImage();
	}

	// public float getSectionWidth() throws Exception {
	// return PDFUtil.renderTextLine(null, painter.getFontB(), FontSize.BIG, null, null, 0.0f, 0.0f, null);
	// }
	public PDFJpeg getRadioOnImage() {
		return painter.getRadioOnImage();
	}

	// public float getSectionY() {
	// return sectionY;
	// }
	@Override
	public PDFJpeg getRadioOnPresetImage() {
		return painter.getRadioOnPresetImage();
	}

	public PDFJpeg getSelectboxCheckedImage() {
		return painter.getSelectboxCheckedImage();
	}

	@Override
	public PDFJpeg getSelectboxCheckedPresetImage() {
		return painter.getSelectboxCheckedPresetImage();
	}

	// public void setIndexY(float indexY) {
	// // this.previousIndexY = this.indexY;
	// this.indexY = indexY;
	// }

	public PDFJpeg getSelectboxUncheckedImage() {
		return painter.getSelectboxUncheckedImage();
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	// public boolean hasIndex() {
	// return !CommonUtil.isEmptyString(getIndexLabel());
	// }
	public boolean hasCategory() {
		return !CommonUtil.isEmptyString(getCategoryLabel());
	}

	public void setBlocks(InquiriesPDFBlock block) {
		// this.previousBlock = this.block;
		// if (this.previousBlock != null) {
		// if (BlockType.NEW_SECTION.equals(this.previousBlock.getType())) {
		// this.previousSectionBlock = this.previousBlock;
		// } else if (BlockType.NEW_INDEX.equals(this.previousBlock.getType())) {
		// this.previousIndexBlock = this.previousBlock;
		// }
		// }
		// this.block = block;
		// this.previousBlock = this.block;
		if (block != null) {
			if (BlockType.NEW_CATEGORY.equals(block.getType())) {
				this.categoryBlock = block;
				// } else if (BlockType.NEW_INDEX.equals(block.getType())) {
				// this.indexBlock = block;
			} else if (BlockType.NEW_PROBAND_TRIAL.equals(block.getType())) {
				this.probandTrialBlock = block;
			}
		}
		// this.block = block;
	}

	public void setCategoryY(float categoryY) {
		// this.previousSectionY = this.sectionY;
		this.categoryY = categoryY;
	}

	public void setProband(ProbandOutVO proband) {
		this.proband = proband;
	}

	public void setTrial(TrialOutVO trial) {
		this.trial = trial;
	}
}
