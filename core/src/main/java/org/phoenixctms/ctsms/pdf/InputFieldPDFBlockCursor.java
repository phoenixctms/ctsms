package org.phoenixctms.ctsms.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;

public interface InputFieldPDFBlockCursor {

	public float getBlockIndentedCenterX() throws Exception;

	public float getBlockIndentedWidth() throws Exception;

	public float getBlockIndentedX() throws Exception;

	public float getBlockY();

	public PDFJpeg getCheckboxCheckedImage();

	public PDFJpeg getCheckboxCheckedPresetImage();

	public PDFJpeg getCheckboxUncheckedImage();

	public PDFont getFontA();

	public PDFont getFontB();

	public PDFont getFontC();

	public PDFont getFontD();

	public PDFJpeg getRadioOffImage();

	public PDFJpeg getRadioOnImage();

	public PDFJpeg getRadioOnPresetImage();

	public PDFJpeg getSelectboxCheckedImage();

	public PDFJpeg getSelectboxCheckedPresetImage();

	public PDFJpeg getSelectboxUncheckedImage();
}
