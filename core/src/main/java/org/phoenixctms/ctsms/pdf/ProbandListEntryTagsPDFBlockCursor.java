package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.pdf.ProbandListEntryTagsPDFBlock.BlockType;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;

public class ProbandListEntryTagsPDFBlockCursor extends PDFBlockCursor implements InputFieldPDFBlockCursor {

	protected ProbandListEntryTagsPDFPainter painter;
	protected ProbandListEntryTagsPDFBlock listEntryBlock;
	protected ProbandListEntryOutVO listEntry;

	public ProbandListEntryTagsPDFBlockCursor(ProbandListEntryTagsPDFPainter painter) {
		this.painter = painter;
		clearBlocks();
	}

	public void clearBlocks() {
		this.listEntryBlock = null;
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
		return width;
	}

	@Override
	public float getBlockIndentedX() throws Exception {
		float x = blockX;
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

	@Override
	public PDFont getFontC() {
		return painter.getFontC();
	}

	@Override
	public PDFont getFontD() {
		return painter.getFontD();
	}

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

	public void setBlocks(ProbandListEntryTagsPDFBlock block) {
		if (block != null) {
			if (BlockType.NEW_LIST_ENTRY.equals(block.getType())) {
				this.listEntryBlock = block;
			}
		}
	}

	public void setListEntry(ProbandListEntryOutVO listEntry) {
		this.listEntry = listEntry;
	}
}
