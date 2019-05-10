package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.pdf.ProbandListEntryTagsPDFBlock.BlockType;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;

public class ProbandListEntryTagsPDFBlockCursor extends PDFBlockCursor implements InputFieldPDFBlockCursor {

	private ProbandListEntryTagsPDFPainter painter;
	// private float previousSectionY;
	// private float sectionY;
	// // private float previousIndexY;
	// private float indexY;
	// private ProbandListEntryTagsPDFBlock sectionBlock;
	// private ProbandListEntryTagsPDFBlock indexBlock;
	private ProbandListEntryTagsPDFBlock listEntryBlock;
	private ProbandListEntryOutVO listEntry;

	public ProbandListEntryTagsPDFBlockCursor(ProbandListEntryTagsPDFPainter painter) {
		this.painter = painter;
		clearBlocks();
	}

	// private ProbandListEntryTagsPDFBlock previousBlock;
	// private ProbandListEntryTagsPDFBlock block;
	public void clearBlocks() {
		//		this.sectionBlock = null;
		//		this.indexBlock = null;
		this.listEntryBlock = null;
		// this.previousBlock = null;
		// this.block = null;
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
		// if (!max || hasSection()) {
		// // width -= //getSectionWidth() +
		// // Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		// }
		// if (!max || hasIndex()) {
		// width -= // getIndexWidth() +
		// 2.0f * Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		// }
		return width;
	}

	@Override
	public float getBlockIndentedX() throws Exception {
		float x = blockX;
		// if (hasSection()) {
		// // x += getSectionWidth() +
		// // Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		// }
		// if (hasIndex()) {
		// x += // getIndexWidth() +
		// Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		// }
		return x;
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

	// public PDFont getFontE() {
	// return painter.getFontE();
	// }
	//
	// public PDFont getFontF() {
	// return painter.getFontF();
	// }
	@Override
	public PDFont getFontC() {
		return painter.getFontC();
	}

	@Override
	public PDFont getFontD() {
		return painter.getFontD();
	}
	// public ProbandListEntryTagsPDFBlock getIndexBlock() {
	// return indexBlock;
	// }
	// public float getIndexWidth() throws Exception {
	// return 0.0f; //PDFUtil.renderTextLine(null, painter.getFontA(), FontSize.BIG, null, null, 0.0f, 0.0f, null);
	// }
	// public ProbandListEntryTagsPDFBlock getPreviousBlock() {
	// return previousBlock;
	// }
	// public String getIndexLabel() {
	// if (indexBlock != null) {
	// Long index = indexBlock.getIndex();
	// if (index != null) {
	// return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_FIELD_INDEX, PDFUtil.DEFAULT_LABEL, index.toString(), indexBlock.getSection());
	// }
	// }
	// return null;
	// }

	// public float getIndexY() {
	// return indexY;
	// }
	// public float getIndexY() {
	// return indexY;
	// }
	public ProbandListEntryOutVO getListEntry() {
		return listEntry;
	}

	public ProbandListEntryTagsPDFBlock getListEntryBlock() {
		return listEntryBlock;
	}

	@Override
	public PDFJpeg getRadioOffImage() {
		return painter.getRadioOffImage();
	}
	// public ProbandListEntryTagsPDFBlock getSectionBlock() {
	// return sectionBlock;
	// }
	// public float getSectionWidth() throws Exception {
	// return PDFUtil.renderTextLine(null, painter.getFontB(), FontSize.BIG, null, null, 0.0f, 0.0f, null);
	// }
	// public String getSectionLabel() {
	// if (sectionBlock != null) {
	// String section = sectionBlock.getSection();
	// if (section != null && section.length() > 0) {
	// return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_FIELD_SECTION, PDFUtil.DEFAULT_LABEL, section);
	// }
	// }
	// return null;
	// }

	// public float getSectionY() {
	// return sectionY;
	// }
	// public float getSectionY() {
	// return sectionY;
	// }
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
	// public PDFJpeg getSignatureAvailableImage() {
	// return painter.getSignatureAvailableImage();
	// }
	//
	// public PDFJpeg getSignatureInvalidImage() {
	// return painter.getSignatureInvalidImage();
	// }
	//
	// public PDFJpeg getSignatureValidImage() {
	// return painter.getSignatureValidImage();
	// }

	// public boolean hasIndex() {
	// return !CommonUtil.isEmptyString(getIndexLabel());
	// }
	//
	// public boolean hasSection() {
	// return !CommonUtil.isEmptyString(getSectionLabel());
	// }
	@Override
	public PDFJpeg getSelectboxCheckedPresetImage() {
		return painter.getSelectboxCheckedPresetImage();
	}

	// public void setIndexY(float indexY) {
	// // this.previousIndexY = this.indexY;
	// this.indexY = indexY;
	// }
	//
	// public void setSectionY(float sectionY) {
	// // this.previousSectionY = this.sectionY;
	// this.sectionY = sectionY;
	// }
	@Override
	public PDFJpeg getSelectboxUncheckedImage() {
		return painter.getSelectboxUncheckedImage();
	}

	public void setBlocks(ProbandListEntryTagsPDFBlock block) {
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
			if (BlockType.NEW_LIST_ENTRY.equals(block.getType())) {
				this.listEntryBlock = block;
				// } else if (BlockType.NEW_INDEX.equals(block.getType())) {
				// this.indexBlock = block;
				// } else if (BlockType.NEW_ECRF.equals(block.getType())) {
				// this.ecrfBlock = block;
			}
		}
		// this.block = block;
	}

	public void setListEntry(ProbandListEntryOutVO listEntry) {
		this.listEntry = listEntry;
	}
}
