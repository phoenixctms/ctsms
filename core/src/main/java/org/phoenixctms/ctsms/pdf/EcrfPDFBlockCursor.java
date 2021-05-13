package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.pdf.EcrfPDFBlock.BlockType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;

public class EcrfPDFBlockCursor extends PDFBlockCursor implements InputFieldPDFBlockCursor {

	protected EcrfPDFPainter painter;
	protected float sectionY;
	protected float indexY;
	protected EcrfPDFBlock sectionBlock;
	protected EcrfPDFBlock indexBlock;
	protected EcrfPDFBlock ecrfBlock;
	protected ProbandListEntryOutVO listEntry;

	public EcrfPDFBlockCursor(EcrfPDFPainter painter) {
		this.painter = painter;
		clearBlocks();
	}

	public void clearBlocks() {
		this.sectionBlock = null;
		this.indexBlock = null;
		this.ecrfBlock = null;
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
		if (!max || hasSection()) {
		}
		if (!max || hasIndex()) {
			width -= 2.0f * Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		}
		return width;
	}

	@Override
	public float getBlockIndentedX() throws Exception {
		float x = blockX;
		if (hasSection()) {
			// x += getSectionWidth() +
			// Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		}
		if (hasIndex()) {
			x += Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT);
		}
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

	public EcrfPDFBlock getEcrfBlock() {
		return ecrfBlock;
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

	public EcrfPDFBlock getIndexBlock() {
		return indexBlock;
	}

	public String getIndexLabel() {
		if (indexBlock != null) {
			Long index = indexBlock.getIndex();
			if (index != null) {
				return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_FIELD_INDEX, PDFUtil.DEFAULT_LABEL, index.toString(), indexBlock.getSection());
			}
		}
		return null;
	}

	public float getIndexY() {
		return indexY;
	}

	public ProbandListEntryOutVO getListEntry() {
		return listEntry;
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

	public EcrfPDFBlock getSectionBlock() {
		return sectionBlock;
	}

	public String getSectionLabel() {
		if (sectionBlock != null) {
			String section = sectionBlock.getSection();
			if (section != null && section.length() > 0) {
				return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_FIELD_SECTION, PDFUtil.DEFAULT_LABEL, section);
			}
		}
		return null;
	}

	public float getSectionY() {
		return sectionY;
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

	public PDFJpeg getSignatureAvailableImage() {
		return painter.getSignatureAvailableImage();
	}

	public PDFJpeg getSignatureInvalidImage() {
		return painter.getSignatureInvalidImage();
	}

	public PDFJpeg getSignatureValidImage() {
		return painter.getSignatureValidImage();
	}

	public boolean hasIndex() {
		return !CommonUtil.isEmptyString(getIndexLabel());
	}

	public boolean hasSection() {
		return !CommonUtil.isEmptyString(getSectionLabel());
	}

	public void setBlocks(EcrfPDFBlock block) {
		if (block != null) {
			if (BlockType.NEW_SECTION.equals(block.getType())) {
				this.sectionBlock = block;
			} else if (BlockType.NEW_INDEX.equals(block.getType())) {
				this.indexBlock = block;
			} else if (BlockType.NEW_ECRF.equals(block.getType())) {
				this.ecrfBlock = block;
			} else if (BlockType.LIST_ENTRY_TAG_VALUE.equals(block.getType())) {
				this.sectionBlock = null;
				this.indexBlock = null;
			}
		}
	}

	public void setIndexY(float indexY) {
		this.indexY = indexY;
	}

	public void setListEntry(ProbandListEntryOutVO listEntry) {
		this.listEntry = listEntry;
	}

	public void setSectionY(float sectionY) {
		this.sectionY = sectionY;
	}
}
