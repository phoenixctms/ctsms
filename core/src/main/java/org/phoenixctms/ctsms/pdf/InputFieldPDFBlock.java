package org.phoenixctms.ctsms.pdf;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.pdf.PDFUtil.Alignment;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

public abstract class InputFieldPDFBlock {

	private InputFieldOutVO inputField;
	protected boolean blank;
	protected PDFJpeg ximage;


	protected InputFieldPDFBlock() {
		this.inputField = null;
		this.ximage = null;
		this.blank = false;
	}

	protected InputFieldPDFBlock(InputFieldOutVO inputField, PDFJpeg ximage, boolean blank) {
		this.inputField = inputField;
		this.ximage = ximage;
		this.blank = blank;
	}

	protected InputFieldPDFBlock(InputFieldPDFBlock block) {
		this.inputField = block.inputField;
		this.ximage = block.ximage;
		this.blank = block.blank;
	}

	protected abstract boolean getBooleanValue();

	private boolean getBooleanValue(boolean preset) {
		if (preset) {
			if (isShowPresetValues()) {
				return inputField.getBooleanPreset();
			} else {
				return false;
			}
		} else {
			return getBooleanValue();
		}
	}

	protected abstract String getComment();

	protected abstract Date getDateValue();

	private Date getDateValue(boolean preset) {
		if (preset) {
			if (isShowPresetValues()) {
				return inputField.getDatePreset();
			} else {
				return null;
			}
		} else {
			return getDateValue();
		}
	}

	protected abstract SimpleDateFormat getDateValueFormat();

	protected abstract String getDateValueFormatPattern();

	protected abstract String getDecimalSeparator();

	protected abstract float getFieldFrameLineWidth();

	protected abstract Float getFloatValue();

	private Float getFloatValue(boolean preset) {
		if (preset) {
			if (isShowPresetValues()) {
				return inputField.getFloatPreset();
			} else {
				return null;
			}
		} else {
			return getFloatValue();
		}
	}

	protected abstract DecimalFormat getFloatValueFormat();

	protected abstract String getFloatValueFormatPattern();

	protected abstract Color getFrameColor();

	protected abstract Float getHorizontalSelectionItemWidth();

	private String getInputFieldComment() {
		String comment;
		if (inputField.getLocalized()) {
			comment = L10nUtil.getInputFieldComment(getLabelLocale(), inputField.getCommentL10nKey());
		} else {
			comment = inputField.getCommentL10nKey();
		}
		if (comment == null) {
			comment = "";
		}
		return getInputFieldCommentLabel(comment); // L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.INPUT_FIELD_COMMENT, PDFUtil.DEFAULT_LABEL, comment);
	}

	protected abstract String getInputFieldCommentLabel(String comment);

	private String getInputFieldName() {
		String name;
		if (inputField.getLocalized()) {
			name = L10nUtil.getInputFieldName(getLabelLocale(), inputField.getNameL10nKey());
		} else {
			name = inputField.getNameL10nKey();
		}
		return getInputFieldNameLabel(name, Long.toString(getPosition())); // L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.INPUT_FIELD_NAME, PDFUtil.DEFAULT_LABEL,
		// name);
	}

	protected abstract String getInputFieldNameLabel(String name, String position);

	protected String getInputFieldTitle() {
		String title;
		if (inputField.getLocalized()) {
			title = L10nUtil.getInputFieldTitle(getLabelLocale(), inputField.getTitleL10nKey());
		} else {
			title = inputField.getTitleL10nKey();
		}
		if (isOptional()) {
			return getInputFieldTitleOptionalLabel(title, Long.toString(getPosition()));
		} else {
			return getInputFieldTitleLabel(title, Long.toString(getPosition())); // L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.INPUT_FIELD_TITLE,
			// PDFUtil.DEFAULT_LABEL, title);
		}
	}

	protected abstract String getInputFieldTitleLabel(String position, String title);

	protected abstract String getInputFieldTitleOptionalLabel(String position,String title);

	private String getInputFieldValidationErrorMessage() {
		String validationErrorMessage;
		if (inputField.getLocalized()) {
			validationErrorMessage = L10nUtil.getInputFieldValidationErrorMsg(getLabelLocale(), inputField.getValidationErrorMsgL10nKey());
		} else {
			validationErrorMessage = inputField.getValidationErrorMsgL10nKey();
		}
		if (validationErrorMessage == null) {
			validationErrorMessage = "";
		}
		return getInputFieldValidationErrorMessageLabel(validationErrorMessage); // L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.INPUT_FIELD_TITLE,
		// PDFUtil.DEFAULT_LABEL, title);
	}

	protected abstract String getInputFieldValidationErrorMessageLabel(String validationErrorMessage);

	protected abstract DecimalFormat getIntegerValueFormat();

	protected abstract String getIntegerValueFormatPattern();

	protected abstract Locales getLabelLocale();

	protected abstract Long getLongValue();

	private Long getLongValue(boolean preset) {
		if (preset) {
			if (isShowPresetValues()) {
				return inputField.getLongPreset();
			} else {
				return null;
			}
		} else {
			return getLongValue();
		}
	}

	protected abstract String getModifiedLabel(String modifiedUser, String modifiedTimestamp);

	protected String getModifiedLabel(UserOutVO modifiedUser, Date modifiedTimestamp) {
		if (modifiedUser != null && modifiedTimestamp != null) {
			StaffOutVO identity = modifiedUser.getIdentity();
			String user;
			if (identity != null) {
				user = identity.getName();
			} else {
				user = modifiedUser.getName();
			}
			return getModifiedLabel(user,getModifiedTimestampFormat().format(modifiedTimestamp));
			//		} else if (modifiedTimestamp != null) {
			//
			//		} else {
		}
		return "";
	}

	protected abstract Date getModifiedTimestamp();

	protected abstract SimpleDateFormat getModifiedTimestampFormat();

	protected abstract UserOutVO getModifiedUser();


	protected abstract float getMultiLineTextMinHeight();
	protected abstract long getPosition();

	protected abstract Color getPresetTextColor();

	protected abstract boolean getRenderSketchImages();

	protected abstract String getSelectionSetValueLabel(String name, String value);

	private Map<Long, String> getSelectionSetValueMap() {
		Collection<InputFieldSelectionSetValueOutVO> selectionSetValues = inputField.getSelectionSetValues();
		LinkedHashMap<Long, String> result = new LinkedHashMap<Long, String>(selectionSetValues.size());
		Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValues.iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValueOutVO selectionValue = it.next();
			String name;
			if (selectionValue.getLocalized()) {
				name = L10nUtil.getInputFieldSelectionSetValueName(getLabelLocale(), selectionValue.getNameL10nKey());
			} else {
				name = selectionValue.getName();
			}
			result.put(selectionValue.getId(), getSelectionSetValueLabel(name,selectionValue.getValue()));
		}
		return result;
	}

	private Set<Long> getSelectionValueIds(boolean preset) {
		Collection<InputFieldSelectionSetValueOutVO> selectionValues = getSelectionValues(preset);
		HashSet<Long> result = new HashSet<Long>(selectionValues.size());
		Iterator<InputFieldSelectionSetValueOutVO> it = selectionValues.iterator();
		while (it.hasNext()) {
			result.add(it.next().getId());
		}
		return result;
	}

	protected abstract Collection<InputFieldSelectionSetValueOutVO> getSelectionValues();

	private Collection<InputFieldSelectionSetValueOutVO> getSelectionValues(boolean preset) {
		if (preset) {
			if (isShowPresetValues()) {
				Collection<InputFieldSelectionSetValueOutVO> selectionSetValues = inputField.getSelectionSetValues();
				ArrayList<InputFieldSelectionSetValueOutVO> result = new ArrayList<InputFieldSelectionSetValueOutVO>(selectionSetValues.size());
				Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValues.iterator();
				while (it.hasNext()) {
					InputFieldSelectionSetValueOutVO selectionSetValue = it.next();
					if (selectionSetValue.getPreset()) {
						result.add(selectionSetValue);
					}
				}
				return result;
			} else {
				return new ArrayList<InputFieldSelectionSetValueOutVO>();
			}
		} else {
			return getSelectionValues();
		}
	}

	protected abstract Color getTextColor();

	protected abstract String getTextValue();

	private String getTextValue(boolean preset) {
		if (preset) {
			if (isShowPresetValues()) {
				if (InputFieldType.AUTOCOMPLETE.equals(inputField.getFieldType().getType())) {
					return inputField.getTextPreset();
				} else {
					if (inputField.isLocalized()) {
						return L10nUtil.getInputFieldTextPreset(getLabelLocale(), inputField.getTextPresetL10nKey());
					} else {
						return inputField.getTextPresetL10nKey();
					}
				}
			} else {
				return null;
			}
		} else {
			return getTextValue();
		}
	}

	protected abstract Date getTimestampValue();

	private Date getTimestampValue(boolean preset) {
		if (preset) {
			if (isShowPresetValues()) {
				return inputField.getTimestampPreset();
			} else {
				return null;
			}
		} else {
			return getTimestampValue();
		}
	}

	protected abstract SimpleDateFormat getTimestampValueFormat();

	protected abstract String getTimestampValueFormatPattern();

	protected abstract Date getTimeValue();

	private Date getTimeValue(boolean preset) {
		if (preset) {
			if (isShowPresetValues()) {
				return inputField.getTimePreset();
			} else {
				return null;
			}
		} else {
			return getTimeValue();
		}
	}

	protected abstract SimpleDateFormat getTimeValueFormat();

	// protected abstract boolean isShowValidationErrorMessages();

	protected abstract String getTimeValueFormatPattern();

	protected abstract Color getValidationErrorMessageTextColor();

	protected abstract float getValueFrameLineWidth();

	protected abstract float getXFieldColumnIndent();

	protected abstract float getXFrameIndent();

	protected abstract float getXSelectionItemImageLabelOffset();

	protected abstract float getXSelectionItemIndent();

	protected abstract float getXValueFrameIndent();

	protected abstract float getYFrameIndent();

	protected abstract float getYSelectionItemImageLabelOffset();

	protected abstract float getYSelectionItemIndent();

	protected abstract float getYValueFrameIndent();

	protected abstract boolean isDisabled();

	protected abstract boolean isInputFieldLongTitle();

	protected abstract boolean isOptional();

	protected abstract boolean isPreset();

	protected abstract boolean isShowModifiedLabel();

	protected abstract boolean isShowPresetValues();

	private float renderBooleanValue(float x, float y, PDPageContentStream contentStream, InputFieldPDFBlockCursor cursor) throws Exception {
		boolean isPresetColor = isPreset() || blank;
		PDFJpeg ximage;
		if (getBooleanValue(blank)) {
			ximage = isPresetColor ? cursor.getCheckboxCheckedPresetImage() : cursor.getCheckboxCheckedImage();
		} else {
			ximage = cursor.getCheckboxUncheckedImage();
		}
		PDFUtil.renderImage(contentStream, ximage, x, y, Alignment.TOP_LEFT);
		return ximage.getHeightPoints();
	}

	private float renderComments(float x, float y, PDPageContentStream contentStream, InputFieldPDFBlockCursor cursor) throws Exception {
		float y1 = y;
		if (blank) {
			String validationErrorMessage = getInputFieldValidationErrorMessage();
			if (!CommonUtil.isEmptyString(validationErrorMessage)) {
				y1 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.SMALL,
						getValidationErrorMessageTextColor(),
						validationErrorMessage,
						x + cursor.getBlockIndentedWidth() - getXFrameIndent(),
						y1,
						PDFUtil.Alignment.TOP_RIGHT,
						cursor.getBlockIndentedWidth() - 2.0f * getXFrameIndent());
				y1 -= getYFrameIndent();
			}
		}
		String inputFieldComment = getInputFieldComment();
		if (!CommonUtil.isEmptyString(inputFieldComment)) {
			y1 -= PDFUtil.renderMultilineText(
					contentStream,
					cursor.getFontC(),
					PDFUtil.FontSize.SMALL,
					getTextColor(),
					inputFieldComment,
					x + getXFrameIndent(),
					y1,
					PDFUtil.Alignment.TOP_LEFT,
					cursor.getBlockIndentedWidth() - 2.0f * getXFrameIndent());
			y1 -= getYFrameIndent();
		}
		String comment = getComment();
		if (!CommonUtil.isEmptyString(comment)) {
			y1 -= PDFUtil.renderMultilineText(
					contentStream,
					cursor.getFontC(),
					PDFUtil.FontSize.SMALL,
					getTextColor(),
					comment,
					x + getXFrameIndent(),
					y1,
					PDFUtil.Alignment.TOP_LEFT,
					cursor.getBlockIndentedWidth() - 2.0f * getXFrameIndent());
			y1 -= getYFrameIndent();
		}
		if (!blank && isShowModifiedLabel()) {
			String modifiedLabel = getModifiedLabel(getModifiedUser(), getModifiedTimestamp());
			if (!CommonUtil.isEmptyString(modifiedLabel)) {
				y1 -= PDFUtil.renderTextLine(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.TINY,
						getTextColor(),
						modifiedLabel,
						x + getXFrameIndent(),
						y1,
						PDFUtil.Alignment.TOP_LEFT); // ,
				// cursor.getBlockIndentedWidth() - 2.0f * getXFrameIndent());
				y1 -= getYFrameIndent();
			}
		}
		return y - y1;
	}

	private float renderFormattedValue(float x, float y, InputFieldType fieldType, PDPageContentStream contentStream, InputFieldPDFBlockCursor cursor) throws Exception {
		Object value;
		String string;
		float width;
		boolean isPresetColor = isPreset() || blank;
		DecimalFormat numberFormat;
		SimpleDateFormat dateFormat;
		switch (fieldType) {
			case INTEGER:
				value = getLongValue(blank);
				numberFormat = getIntegerValueFormat();
				if (value != null) {
					string = numberFormat.format((Long) value);
				} else {
					string = (isShowPresetValues() ? getIntegerValueFormatPattern() : null); // numberFormat.toString(); // .toLocalizedPattern();
					isPresetColor = true;
				}
				width = Math.max(PDFUtil.getStringWidth(string, cursor.getFontD(), PDFUtil.FontSize.MEDIUM),
						PDFUtil.getStringWidth(getIntegerValueFormatPattern(), cursor.getFontD(), PDFUtil.FontSize.MEDIUM));
				break;
			case FLOAT:
				value = getFloatValue(blank);
				numberFormat = getFloatValueFormat();
				if (value != null) {
					string = numberFormat.format((Float) value);
				} else {
					string = (isShowPresetValues() ? getFloatValueFormatPattern() : null); // numberFormat.toString(); // .toLocalizedPattern();
					isPresetColor = true;
				}
				string = CommonUtil.formatDecimal(string, getDecimalSeparator());
				width = Math.max(PDFUtil.getStringWidth(string, cursor.getFontD(), PDFUtil.FontSize.MEDIUM),
						PDFUtil.getStringWidth(getFloatValueFormatPattern(), cursor.getFontD(), PDFUtil.FontSize.MEDIUM));
				break;
			case DATE:
				value = getDateValue(blank);
				dateFormat = getDateValueFormat();
				if (value != null) {
					string = dateFormat.format((Date) value);
				} else {
					string = (isShowPresetValues() ? getDateValueFormatPattern() : null); // dateFormat.toString(); // .toLocalizedPattern();
					isPresetColor = true;
				}
				width = Math.max(PDFUtil.getStringWidth(string, cursor.getFontD(), PDFUtil.FontSize.MEDIUM),
						PDFUtil.getStringWidth(getDateValueFormatPattern(), cursor.getFontD(), PDFUtil.FontSize.MEDIUM));
				break;
			case TIMESTAMP:
				value = getTimestampValue(blank);
				dateFormat = getTimestampValueFormat();
				if (value != null) {
					string = dateFormat.format((Date) value);
				} else {
					string = (isShowPresetValues() ? getTimestampValueFormatPattern() : null); // dateFormat.toString(); // .toLocalizedPattern();
					isPresetColor = true;
				}
				width = Math.max(PDFUtil.getStringWidth(string, cursor.getFontD(), PDFUtil.FontSize.MEDIUM),
						PDFUtil.getStringWidth(getTimestampValueFormatPattern(), cursor.getFontD(), PDFUtil.FontSize.MEDIUM));
				break;
			case TIME:
				value = getTimeValue(blank);
				dateFormat = getTimeValueFormat();
				if (value != null) {
					string = dateFormat.format((Date) value);
				} else {
					string = (isShowPresetValues() ? getTimeValueFormatPattern() : null); // dateFormat.toString(); // .toLocalizedPattern();
					isPresetColor = true;
				}
				width = Math.max(PDFUtil.getStringWidth(string, cursor.getFontD(), PDFUtil.FontSize.MEDIUM),
						PDFUtil.getStringWidth(getTimeValueFormatPattern(), cursor.getFontD(), PDFUtil.FontSize.MEDIUM));
				break;
			default:
				return 0.0f;
		}
		float height = PDFUtil.renderTextLine(
				contentStream,
				cursor.getFontD(),
				PDFUtil.FontSize.MEDIUM,
				isPresetColor ? getPresetTextColor() : getTextColor(),
						string,
						x,
						y,
						PDFUtil.Alignment.TOP_LEFT);
		PDFUtil.renderFrame(contentStream, getFrameColor(),
				x - getXValueFrameIndent(),
				y + getYValueFrameIndent(),
				width + 2.0f * getXValueFrameIndent(),
				height + 2.0f * getYValueFrameIndent(),
				PDFUtil.Alignment.TOP_LEFT, getValueFrameLineWidth());
		return height;
	}

	private void renderFrame(float height, PDPageContentStream contentStream, InputFieldPDFBlockCursor cursor) throws Exception {
		PDFUtil.renderFrame(contentStream, getFrameColor(),
				cursor.getBlockIndentedX(), cursor.getBlockY(), cursor.getBlockIndentedWidth(), height,
				PDFUtil.Alignment.TOP_LEFT, getFieldFrameLineWidth());
	}

	protected float renderInputFieldBlock(PDPageContentStream contentStream, InputFieldPDFBlockCursor cursor) throws Exception {
		float x;
		float y;
		float y1;
		float y2;
		float height;
		InputFieldType fieldType = inputField.getFieldType().getType();
		switch (fieldType) {
			case AUTOCOMPLETE:
			case SINGLE_LINE_TEXT:
			case MULTI_LINE_TEXT:
				x = cursor.getBlockIndentedX();
				y = cursor.getBlockY() - getYFrameIndent();
				if (isInputFieldLongTitle()) {
					y -= renderTitle(x, y, contentStream, cursor) + getYFrameIndent();
					y -= renderTextValue(x + getXFieldColumnIndent() + getXFrameIndent(), y, fieldType, contentStream, cursor) + getYFrameIndent();
				} else {
					y1 = y;
					y2 = y;
					y1 -= renderTitle(x, y1, contentStream, cursor);
					y2 -= renderTextValue(x + getXFieldColumnIndent() + getXFrameIndent(), y2, fieldType, contentStream, cursor);
					y = Math.min(y1, y2) - getYFrameIndent();
				}
				y -= renderComments(x, y, contentStream, cursor);
				height = cursor.getBlockY() - y;
				renderFrame(height, contentStream, cursor);
				break;
			case INTEGER:
			case FLOAT:
			case DATE:
			case TIMESTAMP:
			case TIME:
				x = cursor.getBlockIndentedX();
				y = cursor.getBlockY() - getYFrameIndent();
				if (isInputFieldLongTitle()) {
					y -= renderTitle(x, y, contentStream, cursor) + getYFrameIndent();
					y -= renderFormattedValue(x + getXFieldColumnIndent() + getXFrameIndent(), y, fieldType, contentStream, cursor) + getYFrameIndent();
				} else {
					y1 = y;
					y2 = y;
					y1 -= renderTitle(x, y1, contentStream, cursor);
					y2 -= renderFormattedValue(x + getXFieldColumnIndent() + getXFrameIndent(), y2, fieldType, contentStream, cursor);
					y = Math.min(y1, y2) - getYFrameIndent();
				}
				y -= renderComments(x, y, contentStream, cursor);
				height = cursor.getBlockY() - y;
				renderFrame(height, contentStream, cursor);
				break;
			case SELECT_ONE_DROPDOWN:
			case SELECT_ONE_RADIO_H:
			case SELECT_ONE_RADIO_V:
			case SELECT_MANY_H:
			case SELECT_MANY_V:
				x = cursor.getBlockIndentedX();
				y = cursor.getBlockY() - getYFrameIndent();
				if (isInputFieldLongTitle()) {
					y -= renderTitle(x, y, contentStream, cursor) + getYFrameIndent();
					y -= renderSelectionValue(x + getXFieldColumnIndent() + getXFrameIndent(), y, fieldType, contentStream, cursor) + getYFrameIndent();
				} else {
					y1 = y;
					y2 = y;
					y1 -= renderTitle(x, y1, contentStream, cursor);
					y2 -= renderSelectionValue(x + getXFieldColumnIndent() + getXFrameIndent(), y2, fieldType, contentStream, cursor);
					y = Math.min(y1, y2) - getYFrameIndent();
				}
				y -= renderComments(x, y, contentStream, cursor);
				height = cursor.getBlockY() - y;
				renderFrame(height, contentStream, cursor);
				break;
			case CHECKBOX:
				x = cursor.getBlockIndentedX();
				y = cursor.getBlockY() - getYFrameIndent();
				if (isInputFieldLongTitle()) {
					y -= renderTitle(x, y, contentStream, cursor) + getYFrameIndent();
					y -= renderBooleanValue(x + getXFieldColumnIndent() + getXFrameIndent(), y, contentStream, cursor) + getYFrameIndent();
				} else {
					y1 = y;
					y2 = y;
					y1 -= renderTitle(x, y1, contentStream, cursor);
					y2 -= renderBooleanValue(x + getXFieldColumnIndent() + getXFrameIndent(), y2, contentStream, cursor);
					y = Math.min(y1, y2) - getYFrameIndent();
				}
				y -= renderComments(x, y, contentStream, cursor);
				height = cursor.getBlockY() - y;
				renderFrame(height, contentStream, cursor);
				break;
			case SKETCH:
				x = cursor.getBlockIndentedX();
				y = cursor.getBlockY() - getYFrameIndent();
				if (isInputFieldLongTitle()) {
					y -= renderTitle(x, y, contentStream, cursor) + getYFrameIndent();
					y -= (getRenderSketchImages() ? 0.0f : renderSelectionValue(x + getXFieldColumnIndent() + getXFrameIndent(), y, fieldType, contentStream, cursor))
							+ getYFrameIndent();
				} else {
					y1 = y;
					y2 = y;
					y1 -= renderTitle(x, y1, contentStream, cursor);
					y2 -= (getRenderSketchImages() ? 0.0f : renderSelectionValue(x + getXFieldColumnIndent() + getXFrameIndent(), y2, fieldType, contentStream, cursor));
					y = Math.min(y1, y2) - getYFrameIndent();
				}
				if (getRenderSketchImages()) {
					y -= renderSketchImage(cursor.getBlockIndentedCenterX(), y, contentStream, cursor) + getYFrameIndent();
				}
				y -= renderComments(x, y, contentStream, cursor);
				height = cursor.getBlockY() - y;
				renderFrame(height, contentStream, cursor);
				break;
			default:
				// throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_INPUT_FIELD_TYPE, DefaultMessages.UNSUPPORTED_INPUT_FIELD_TYPE,
				// new Object[] { fieldType.toString() }));
				height = 0.0f;
				break;
		}
		return height;
	}

	private float renderSelectionValue(float x, float y, InputFieldType fieldType, PDPageContentStream contentStream, InputFieldPDFBlockCursor cursor) throws Exception {
		float y1 = y;
		float x1 = x;
		float height;
		float rowHeight = 0.0f;
		Float horizontalSelectionItemWidth = getHorizontalSelectionItemWidth();
		boolean isPresetColor = isPreset() || blank;
		Set<Long> selectionSetValueIds = getSelectionValueIds(blank);
		// Map<Long, String> selectionSetValueMap = getSelectionSetValueMap();
		// if (selectionSetValueMap.size() > 0) {
		Iterator<Entry<Long, String>> it = getSelectionSetValueMap().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, String> selectionSetValue = it.next();
			Long id = selectionSetValue.getKey();
			String string = selectionSetValue.getValue();
			boolean horizontal = false;
			boolean last = !it.hasNext();
			PDFJpeg ximage;
			switch (fieldType) {
				case SELECT_ONE_RADIO_H:
					horizontal = true;
				case SELECT_ONE_DROPDOWN:
				case SELECT_ONE_RADIO_V:
					if (selectionSetValueIds.contains(id)) {
						ximage = isPresetColor ? cursor.getRadioOnPresetImage() : cursor.getRadioOnImage();
					} else {
						ximage = cursor.getRadioOffImage();
					}
					break;
				case SKETCH:
				case SELECT_MANY_H:
					horizontal = true;
				case SELECT_MANY_V:
					if (selectionSetValueIds.contains(id)) {
						ximage = isPresetColor ? cursor.getSelectboxCheckedPresetImage() : cursor.getSelectboxCheckedImage();
					} else {
						ximage = cursor.getSelectboxUncheckedImage();
					}
					break;
				default:
					return 0.0f;
			}
			if (horizontal) {
				float width;
				if (horizontalSelectionItemWidth != null) {
					width = horizontalSelectionItemWidth;
				} else {
					width = PDFUtil.getStringWidth(string, cursor.getFontA(), PDFUtil.FontSize.MEDIUM) + ximage.getWidthPoints() + getXSelectionItemImageLabelOffset();
				}
				if ((x1 + width) > (cursor.getBlockIndentedX() + cursor.getBlockIndentedWidth() - getXFrameIndent())) { // new row
					y1 -= rowHeight + getYSelectionItemIndent();
					x1 = x;
					rowHeight = 0.0f;
				}
				PDFUtil.renderImage(contentStream, ximage, x1, y1, Alignment.TOP_LEFT);
				if (horizontalSelectionItemWidth != null) {
					height = PDFUtil.renderMultilineText(
							contentStream,
							cursor.getFontA(),
							PDFUtil.FontSize.MEDIUM,
							getTextColor(),
							string,
							x1 + ximage.getWidthPoints() + getXSelectionItemImageLabelOffset(),
							y1 - getYSelectionItemImageLabelOffset(),
							PDFUtil.Alignment.TOP_LEFT,
							width - ximage.getWidthPoints() + getXSelectionItemImageLabelOffset());
				} else {
					height = PDFUtil.renderTextLine(
							contentStream,
							cursor.getFontA(),
							PDFUtil.FontSize.MEDIUM,
							getTextColor(),
							string,
							x1 + ximage.getWidthPoints() + getXSelectionItemImageLabelOffset(),
							y1 - getYSelectionItemImageLabelOffset(),
							PDFUtil.Alignment.TOP_LEFT);
				}
				height = Math.max(height + getYSelectionItemImageLabelOffset(), ximage.getHeightPoints());
				x1 += width + getXSelectionItemIndent();
				rowHeight = Math.max(rowHeight, height);
				if (last) {
					y1 -= rowHeight;
				}
			} else {
				PDFUtil.renderImage(contentStream, ximage, x1, y1, Alignment.TOP_LEFT);
				height = Math.max(PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.MEDIUM,
						getTextColor(),
						string,
						x1 + ximage.getWidthPoints() + getXSelectionItemImageLabelOffset(),
						y1 - getYSelectionItemImageLabelOffset(),
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockIndentedX() + cursor.getBlockIndentedWidth() - (x1 + ximage.getWidthPoints() + getXSelectionItemImageLabelOffset()) - getXFrameIndent())
						+ getYSelectionItemImageLabelOffset(), ximage.getHeightPoints());
				y1 -= height;
				if (!last) {
					y1 -= getYSelectionItemIndent();
				}
			}
		}
		// y1 -= getYFrameIndent();
		return y - y1;
		// } else {
		// return 0.0f;
		// }
	}

	private float renderSketchImage(float x, float y, PDPageContentStream contentStream, InputFieldPDFBlockCursor cursor) throws Exception {
		PDFUtil.renderImage(contentStream, ximage, x, y, Alignment.TOP_CENTER);
		// PDFUtil.renderFrame(contentStream, getFrameColor(),
		// x,
		// y,
		// ximage.getWidthPoints(),
		// ximage.getHeightPoints(),
		// PDFUtil.Alignment.TOP_CENTER, getValueFrameLineWidth());
		return ximage.getHeightPoints();
	}

	private float renderTextValue(float x, float y, InputFieldType fieldType, PDPageContentStream contentStream, InputFieldPDFBlockCursor cursor) throws Exception {
		float width = cursor.getBlockIndentedX() + cursor.getBlockIndentedWidth() - x - getXFrameIndent();
		float height = Math.max(PDFUtil.renderMultilineText(
				contentStream,
				cursor.getFontB(),
				PDFUtil.FontSize.MEDIUM,
				isPreset() || blank ? getPresetTextColor() : getTextColor(),
						getTextValue(blank),
						x,
						y,
						PDFUtil.Alignment.TOP_LEFT,
						width), InputFieldType.MULTI_LINE_TEXT.equals(fieldType) ? getMultiLineTextMinHeight() : 0.0f);
		PDFUtil.renderFrame(contentStream, getFrameColor(),
				x - getXValueFrameIndent(),
				y + getYValueFrameIndent(),
				width + 2.0f * getXValueFrameIndent(),
				height + 2.0f * getYValueFrameIndent(),
				PDFUtil.Alignment.TOP_LEFT, getValueFrameLineWidth());
		return height;
	}

	private float renderTitle(float x, float y, PDPageContentStream contentStream, InputFieldPDFBlockCursor cursor) throws Exception {
		return PDFUtil.renderMultilineText(
				contentStream,
				cursor.getFontA(),
				PDFUtil.FontSize.MEDIUM,
				getTextColor(),
				getInputFieldTitle(),
				x + getXFrameIndent(),
				y,
				PDFUtil.Alignment.TOP_LEFT,
				isInputFieldLongTitle() ? (cursor.getBlockIndentedWidth() - 2.0f * getXFrameIndent()) : (getXFieldColumnIndent() - getXFrameIndent())); // - 2.0f *
		// getXFrameIndent());
	}
}