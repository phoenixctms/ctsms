package org.phoenixctms.ctsms.pdf;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.pdf.PDFUtil.Alignment;
import org.phoenixctms.ctsms.pdf.PDFUtil.FontSize;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

public class InquiriesPDFBlock extends InputFieldPDFBlock {

	public enum BlockType {
		// NEW_PAGE,
		// NEW_LIST_ENTRY,
		PAGE_TITLE,
		NEW_PROBAND_TRIAL,
		NEW_CATEGORY,
		// NEW_INDEX,
		INPUT_FIELD,
		// END_OF_INDEX,
		END_OF_CATEGORY,
		// ECRF_SIGNATURE,
		// SPACER,
	}

	private InquiryValueOutVO value;
	private ProbandOutVO proband;
	private TrialOutVO trial;
	private String category;
	// private Long index;
	private BlockType type;
	private boolean inserted = false;
	private Date now;

	// public InquiriesPDFBlock() {
	// type = BlockType.SPACER;
	// }
	public InquiriesPDFBlock(BlockType type, boolean inserted) {
		super();
		this.type = type;
		this.inserted = inserted;
	}

	public InquiriesPDFBlock(InquiriesPDFBlock block, BlockType type, boolean inserted) {
		super(block);
		value = block.value;
		proband = block.proband;
		trial = block.trial;
		category = block.category;
		this.type = type;
		now = block.now;
		this.inserted = inserted;
	}

	public InquiriesPDFBlock(InquiriesPDFBlock block, boolean inserted) {
		this(block, block.type, inserted);
	}

	// public InquiriesPDFBlock(BlockType type) {
	// this.type = type;
	// }
	public InquiriesPDFBlock(InquiryValueOutVO value, PDFJpeg ximage, boolean blank) {
		super(value.getInquiry().getField(), ximage, blank);
		this.value = value;
		this.type = BlockType.INPUT_FIELD;
	}

	// public InquiriesPDFBlock(ProbandListEntryOutVO listEntry) {
	// super();
	// this.listEntry = listEntry;
	// this.type = BlockType.NEW_LIST_ENTRY;
	// }
	public InquiriesPDFBlock(ProbandOutVO proband, TrialOutVO trial, Date now, boolean blank) {
		super();
		this.proband = proband;
		this.trial = trial;
		this.now = now;
		this.blank = blank;
		this.type = BlockType.NEW_PROBAND_TRIAL;
	}

	// public InquiriesPDFBlock(SignatureVO signature) {
	// super();
	// this.signature = signature;
	// this.type = BlockType.ECRF_SIGNATURE;
	// }
	public InquiriesPDFBlock(String category) {
		super();
		this.category = category;
		this.type = BlockType.NEW_CATEGORY;
	}

	// public InquiriesPDFBlock(String section, Long index) {
	// super();
	// this.section = section;
	// this.index = index;
	// this.type = BlockType.NEW_INDEX;
	// }
	@Override
	protected boolean getBooleanValue() {
		return value.getBooleanValue();
	}

	public String getCategory() {
		return category;
	}

	@Override
	protected String getComment() {
		return value.getInquiry().getComment();
	}

	@Override
	protected Date getDateValue() {
		return value.getDateValue();
	}

	@Override
	protected SimpleDateFormat getDateValueFormat() {
		return Settings.getSimpleDateFormat(InquiriesPDFSettingCodes.DATE_VALUE_PATTERN, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.DATE_VALUE_PATTERN,
				Locales.INQUIRIES_PDF);
	}

	@Override
	protected String getDateValueFormatPattern() {
		return Settings.getString(InquiriesPDFSettingCodes.DATE_VALUE_PATTERN, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.DATE_VALUE_PATTERN);
	}

	@Override
	protected float getFieldFrameLineWidth() {
		return Settings.getFloat(InquiriesPDFSettingCodes.FIELD_FRAME_LINE_WIDTH, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.FIELD_FRAME_LINE_WIDTH);
	}

	@Override
	protected Float getFloatValue() {
		return value.getFloatValue();
	}

	@Override
	protected DecimalFormat getFloatValueFormat() {
		return Settings
				.getDecimalFormat(InquiriesPDFSettingCodes.FLOAT_VALUE_PATTERN, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.FLOAT_VALUE_PATTERN, Locales.INQUIRIES_PDF);
	}

	@Override
	protected String getFloatValueFormatPattern() {
		return Settings.getString(InquiriesPDFSettingCodes.FLOAT_VALUE_PATTERN, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.FLOAT_VALUE_PATTERN);
	}

	@Override
	protected Color getFrameColor() {
		return Settings.getColor(InquiriesPDFSettingCodes.FRAME_COLOR, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.FRAME_COLOR);
	}

	public float getHeight(InquiriesPDFBlockCursor cursor) throws Exception {
		return renderBlock(null, cursor);
	}

	// public Long getIndex() {
	// return index;
	// }
	@Override
	protected Float getHorizontalSelectionItemWidth() {
		return Settings.getFloatNullable(InquiriesPDFSettingCodes.HORIZONTAL_SELECTION_ITEM_WIDTH, Bundle.INQUIRIES_PDF,
				InquiriesPDFDefaultSettings.HORIZONTAL_SELECTION_ITEM_WIDTH);
	}

	@Override
	protected String getInputFieldCommentLabel(String comment) {
		return L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.INPUT_FIELD_COMMENT, PDFUtil.DEFAULT_LABEL, comment);
	}

	@Override
	protected String getInputFieldNameLabel(String name, String position) {
		return L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.INPUT_FIELD_NAME, PDFUtil.DEFAULT_LABEL, name, position);
	}

	@Override
	protected String getInputFieldTitleLabel(String position, String title) {
		return L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.INPUT_FIELD_TITLE, PDFUtil.DEFAULT_LABEL, position, title);
	}

	@Override
	protected String getInputFieldTitleOptionalLabel(String position, String title) {
		return L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.INPUT_FIELD_TITLE_OPTIONAL, PDFUtil.DEFAULT_LABEL, position, title);
	}

	@Override
	protected String getInputFieldValidationErrorMessageLabel(String validationErrorMessage) {
		return L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.INPUT_FIELD_VALIDATION_ERROR_MESSAGE, PDFUtil.DEFAULT_LABEL, validationErrorMessage);
	}

	@Override
	protected DecimalFormat getIntegerValueFormat() {
		return Settings.getDecimalFormat(InquiriesPDFSettingCodes.INTEGER_VALUE_PATTERN, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.INTEGER_VALUE_PATTERN,
				Locales.INQUIRIES_PDF);
	}

	@Override
	protected String getIntegerValueFormatPattern() {
		return Settings.getString(InquiriesPDFSettingCodes.INTEGER_VALUE_PATTERN, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.INTEGER_VALUE_PATTERN);
	}

	@Override
	protected Locales getLabelLocale() {
		return Locales.INQUIRIES_PDF;
	}

	@Override
	protected Long getLongValue() {
		return value.getLongValue();
	}

	@Override
	protected String  getModifiedLabel(String modifiedUser, String modifiedTimestamp) {
		return L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.MODIFIED_LABEL, PDFUtil.DEFAULT_LABEL, modifiedUser, modifiedTimestamp);
	}

	@Override
	protected Date getModifiedTimestamp() {
		return value.getModifiedTimestamp();
	}

	@Override
	protected SimpleDateFormat getModifiedTimestampFormat() {
		return Settings.getSimpleDateFormat(InquiriesPDFSettingCodes.MODIFIED_TIMESTAMP_PATTERN, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.MODIFIED_TIMESTAMP_PATTERN, Locales.INQUIRIES_PDF);
	}

	@Override
	protected UserOutVO getModifiedUser() {
		return value.getModifiedUser();
	}

	@Override
	protected float getMultiLineTextMinHeight() {
		return Settings.getFloat(InquiriesPDFSettingCodes.MULTI_LINE_TEXT_MIN_HEIGHT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.MULTI_LINE_TEXT_MIN_HEIGHT);
	}

	@Override
	protected long getPosition() {
		return value.getInquiry().getPosition();
	}

	@Override
	protected Color getPresetTextColor() {
		return Settings.getColor(InquiriesPDFSettingCodes.PRESET_TEXT_COLOR, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.PRESET_TEXT_COLOR);
	}

	public ProbandOutVO getProband() {
		return proband;
	}

	@Override
	protected boolean getRenderSketchImages() {
		return Settings.getBoolean(InquiriesPDFSettingCodes.RENDER_SKETCH_IMAGES, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.RENDER_SKETCH_IMAGES);
	}

	@Override
	protected String getSelectionSetValueLabel(String name, String value) {
		return L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.SELECTION_SET_VALUE_NAME, PDFUtil.DEFAULT_LABEL, name, value);
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getSelectionValues() {
		return value.getSelectionValues();
	}

	private String getStatusTypeName() {
		if (trial != null && trial.getStatus() != null) {
			return L10nUtil.getTrialStatusTypeName(Locales.INQUIRIES_PDF, trial.getStatus().getNameL10nKey());
		}
		return "";
	}

	// private String getStatusTypeName() {
	// if (statusEntry != null && statusEntry.getStatus() != null) {
	// return L10nUtil.getEcrfStatusTypeName(Locales.INQUIRIES_PDF, statusEntry.getStatus().getNameL10nKey());
	// }
	// return "";
	// }
	// private String getStatusUser() {
	// if (statusEntry != null && statusEntry.getModifiedUser() != null) {
	// if (statusEntry.getModifiedUser().getIdentity() != null) {
	// return CommonUtil.staffOutVOToString(statusEntry.getModifiedUser().getIdentity());
	// } else {
	// return CommonUtil.userOutVOToString(statusEntry.getModifiedUser());
	// }
	// }
	// return "";
	// }
	@Override
	protected Color getTextColor() {
		return Settings.getColor(InquiriesPDFSettingCodes.TEXT_COLOR, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.TEXT_COLOR);
	}

	@Override
	protected String getTextValue() {
		return value.getTextValue();
	}

	@Override
	protected Date getTimestampValue() {
		return value.getTimestampValue();
	}

	@Override
	protected SimpleDateFormat getTimestampValueFormat() {
		return Settings.getSimpleDateFormat(InquiriesPDFSettingCodes.TIMESTAMP_VALUE_PATTERN, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.TIMESTAMP_VALUE_PATTERN,
				Locales.INQUIRIES_PDF);
	}

	@Override
	protected String getTimestampValueFormatPattern() {
		return Settings.getString(InquiriesPDFSettingCodes.TIMESTAMP_VALUE_PATTERN, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.TIMESTAMP_VALUE_PATTERN);
	}

	@Override
	protected Date getTimeValue() {
		return value.getTimeValue();
	}

	@Override
	protected SimpleDateFormat getTimeValueFormat() {
		return Settings.getSimpleDateFormat(InquiriesPDFSettingCodes.TIME_VALUE_PATTERN, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.TIME_VALUE_PATTERN,
				Locales.INQUIRIES_PDF);
	}

	@Override
	protected String getTimeValueFormatPattern() {
		return Settings.getString(InquiriesPDFSettingCodes.TIME_VALUE_PATTERN, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.TIME_VALUE_PATTERN);
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	public BlockType getType() {
		return type;
	}

	// @Override
	// protected boolean isShowValidationErrorMessages() {
	// return Settings.getBoolean(InquiriesPDFSettingCodes.SHOW_VALIDATION_ERROR_MESSAGES, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.SHOW_VALIDATION_ERROR_MESSAGES);
	// }
	@Override
	protected Color getValidationErrorMessageTextColor() {
		return Settings.getColor(InquiriesPDFSettingCodes.VALIDATION_ERROR_MESSAGE_TEXT_COLOR, Bundle.INQUIRIES_PDF,
				InquiriesPDFDefaultSettings.VALIDATION_ERROR_MESSAGE_TEXT_COLOR);
	}

	@Override
	protected float getValueFrameLineWidth() {
		return Settings.getFloat(InquiriesPDFSettingCodes.VALUE_FRAME_LINE_WIDTH, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.VALUE_FRAME_LINE_WIDTH);
	}

	@Override
	protected float getXFieldColumnIndent() {
		return Settings.getFloat(InquiriesPDFSettingCodes.X_FIELD_COLUMN_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_FIELD_COLUMN_INDENT);
	}

	@Override
	protected float getXFrameIndent() {
		return Settings.getFloat(InquiriesPDFSettingCodes.X_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_FRAME_INDENT);
	}

	@Override
	protected float getXSelectionItemImageLabelOffset() {
		return Settings.getFloat(InquiriesPDFSettingCodes.X_SELECTION_ITEM_IMAGE_LABEL_OFFSET, Bundle.INQUIRIES_PDF,
				InquiriesPDFDefaultSettings.X_SELECTION_ITEM_IMAGE_LABEL_OFFSET);
	}

	@Override
	protected float getXSelectionItemIndent() {
		return Settings.getFloat(InquiriesPDFSettingCodes.X_SELECTION_ITEM_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_SELECTION_ITEM_INDENT);
	}

	@Override
	protected float getXValueFrameIndent() {
		return Settings.getFloat(InquiriesPDFSettingCodes.X_VALUE_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_VALUE_FRAME_INDENT);
	}

	@Override
	protected float getYFrameIndent() {
		return Settings.getFloat(InquiriesPDFSettingCodes.Y_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_FRAME_INDENT);
	}

	@Override
	protected float getYSelectionItemImageLabelOffset() {
		return Settings.getFloat(InquiriesPDFSettingCodes.Y_SELECTION_ITEM_IMAGE_LABEL_OFFSET, Bundle.INQUIRIES_PDF,
				InquiriesPDFDefaultSettings.Y_SELECTION_ITEM_IMAGE_LABEL_OFFSET);
	}

	@Override
	protected float getYSelectionItemIndent() {
		return Settings.getFloat(InquiriesPDFSettingCodes.Y_SELECTION_ITEM_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_SELECTION_ITEM_INDENT);
	}

	@Override
	protected float getYValueFrameIndent() {
		return Settings.getFloat(InquiriesPDFSettingCodes.Y_VALUE_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_VALUE_FRAME_INDENT);
	}

	@Override
	protected boolean isDisabled() {
		return value.getInquiry().getDisabled();
	}

	protected boolean isInputFieldLongTitle() {
		String title = getInputFieldTitle();
		if (title != null && title.length() > Settings.getInt(InquiriesPDFSettingCodes.LONG_TITLE_LENGTH, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.LONG_TITLE_LENGTH)) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean isOptional() {
		return value.getInquiry().getOptional();
	}

	@Override
	protected boolean isPreset() {
		return value.getId() == null;
	}

	@Override
	protected boolean isShowModifiedLabel() {
		return Settings.getBoolean(InquiriesPDFSettingCodes.SHOW_MODIFIED_LABEL, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.SHOW_MODIFIED_LABEL);
	}

	@Override
	protected boolean isShowPresetValues() {
		return Settings.getBoolean(InquiriesPDFSettingCodes.SHOW_PRESET_VALUES, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.SHOW_PRESET_VALUES);
	}

	public float renderBlock(PDPageContentStream contentStream, InquiriesPDFBlockCursor cursor) throws Exception {
		// if (contentStream != null) {
		// System.out.println("rendering block " + type);
		// }
		float x;
		float y;
		float y1;
		float height1;
		float height2;
		// float height3;
		float height;
		float width;
		PDFJpeg ximage;
		switch (type) {
			case PAGE_TITLE:
				height = PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.BIG, getTextColor(),
						L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.PAGE_TITLE, PDFUtil.DEFAULT_LABEL,
								trial.getName(),
								Long.toString(proband.getId()), proband.getInitials(), proband.getName(),
								proband.getDateOfBirth() != null ? Settings.getSimpleDateFormat(
										InquiriesPDFSettingCodes.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
										Bundle.INQUIRIES_PDF,
										InquiriesPDFDefaultSettings.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
										Locales.INQUIRIES_PDF).format(proband.getDateOfBirth()) : "", proband.getYearOfBirth() != null ? Integer.toString(proband.getYearOfBirth())
												: "",
												proband.getAge() != null ? Integer.toString(proband.getAge()) : ""
								),
								cursor.getBlockX(),
								Settings.getFloat(InquiriesPDFSettingCodes.PAGE_TITLE_Y, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.PAGE_TITLE_Y),
								Alignment.TOP_LEFT,
								cursor.getBlockWidth());
				break;
			case NEW_PROBAND_TRIAL:
				width = (cursor.getBlockWidth() - 2.0f * Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.INQUIRIES_PDF,
						InquiriesPDFDefaultSettings.X_HEAD_COLUMN_INDENT)) / 2.0f;
				x = cursor.getBlockX();
				y = cursor.getBlockY();
				if (!inserted) {
					y -= Settings.getFloat(InquiriesPDFSettingCodes.Y_HEADLINE_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_HEADLINE_INDENT);
					y -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.LARGE, getTextColor(),
							L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.HEADLINE, PDFUtil.DEFAULT_LABEL,
									trial.getName(),
									Long.toString(proband.getId()), proband.getInitials(), proband.getName(),
									proband.getDateOfBirth() != null ? Settings.getSimpleDateFormat(
											InquiriesPDFSettingCodes.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
											Bundle.INQUIRIES_PDF,
											InquiriesPDFDefaultSettings.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
											Locales.INQUIRIES_PDF).format(proband.getDateOfBirth()) : "",
											proband.getYearOfBirth() != null ? Integer.toString(proband.getYearOfBirth()) : "",
													proband.getAge() != null ? Integer.toString(proband.getAge()) : ""
									),
									cursor.getBlockCenterX(),
									y,
									Alignment.TOP_CENTER,
									cursor.getBlockWidth());
					y -= Settings.getFloat(InquiriesPDFSettingCodes.Y_HEADLINE_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_HEADLINE_INDENT);
				}
				y1 = y;
				y -= getYFrameIndent();
				//				height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
				//						L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.TRIAL_NAME_LABEL, PDFUtil.DEFAULT_LABEL),
				//						x + getXFrameIndent(),
				//						y,
				//						Alignment.TOP_LEFT,
				//						Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN_INDENT)
				//						- getXFrameIndent());
				//				x += Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				//				height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.MEDIUM, getTextColor(),
				//						L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.TRIAL_NAME, PDFUtil.DEFAULT_LABEL, trial.getName(), trial.getTitle()),
				//								x + getXFrameIndent(),
				//								y,
				//								Alignment.TOP_LEFT,
				//								width - getXFrameIndent()), height1);
				//				x += width;
				height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.PROBAND_NAME_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN_INDENT)
						- getXFrameIndent());
				x += Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height1 = Math.max(
						PDFUtil.renderMultilineText(
								contentStream,
								cursor.getFontB(),
								FontSize.MEDIUM,
								getTextColor(),
								L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.PROBAND_NAME, PDFUtil.DEFAULT_LABEL,
										Long.toString(proband.getId()),
										CommonUtil.getProbandAlias(proband,
												L10nUtil.getString(Locales.INQUIRIES_PDF, MessageCodes.NEW_BLINDED_PROBAND_NAME, DefaultMessages.NEW_BLINDED_PROBAND_NAME),
												L10nUtil.getString(Locales.INQUIRIES_PDF, MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME)
												),
										proband.getInitials(), proband.getName()),
												x + getXFrameIndent(),
												y,
												Alignment.TOP_LEFT,
												width - getXFrameIndent()), height1);
				x += width;
				height2 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.PROBAND_DATE_OF_BIRTH_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN_INDENT)
						- getXFrameIndent());
				x += Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height2 = Math.max(
						PDFUtil.renderMultilineText(
								contentStream,
								cursor.getFontA(),
								FontSize.MEDIUM,
								getTextColor(),
								L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.PROBAND_DATE_OF_BIRTH, PDFUtil.DEFAULT_LABEL,
										proband.getDateOfBirth() != null ? Settings.getSimpleDateFormat(
												InquiriesPDFSettingCodes.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
												Bundle.INQUIRIES_PDF,
												InquiriesPDFDefaultSettings.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
												Locales.INQUIRIES_PDF).format(proband.getDateOfBirth()) : "",
												proband.getYearOfBirth() != null ? Integer.toString(proband.getYearOfBirth()) : "",
														proband.getAge() != null ? Integer.toString(proband.getAge()) : ""),
														x + getXFrameIndent(),
														y,
														Alignment.TOP_LEFT,
														width - getXFrameIndent()), height2);
				//

				x = cursor.getBlockX();
				y -= Math.max(height1, height2) + getYFrameIndent();
				height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.TRIAL_STATUS_TYPE_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN_INDENT)
						- getXFrameIndent());
				x += Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						blank ? L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.TRIAL_STATUS_TYPE_BLANK, PDFUtil.DEFAULT_LABEL, getStatusTypeName()) :
							L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.TRIAL_STATUS_TYPE, PDFUtil.DEFAULT_LABEL, getStatusTypeName()),
							x + getXFrameIndent(),
							y,
							Alignment.TOP_LEFT,
							width - getXFrameIndent()), height1);

				x += width;
				height2 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.CONTENT_TIMESTAMP_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN_INDENT)
						- getXFrameIndent());
				x += Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height2 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.CONTENT_TIMESTAMP, PDFUtil.DEFAULT_LABEL, Settings.getSimpleDateFormat(
								InquiriesPDFSettingCodes.CONTENT_TIMESTAMP_DATETIME_PATTERN,
								Bundle.INQUIRIES_PDF,
								InquiriesPDFDefaultSettings.CONTENT_TIMESTAMP_DATETIME_PATTERN,
								Locales.INQUIRIES_PDF).format(now)),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								width - getXFrameIndent()), height2);
				x = cursor.getBlockX();
				y -= Math.max(height1, height2) + getYFrameIndent();
				if (!inserted) {
					height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
							L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.TRIAL_TITLE_LABEL, PDFUtil.DEFAULT_LABEL),
							x + getXFrameIndent(),
							y,
							Alignment.TOP_LEFT,
							Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN1_INDENT)
							- getXFrameIndent());
					x += Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN1_INDENT);
					height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontC(), FontSize.MEDIUM, getTextColor(),
							trial.getTitle(),
							x + getXFrameIndent(),
							y,
							Alignment.TOP_LEFT,
							cursor.getBlockWidth() - x - 2.0f * getXFrameIndent()), height1);
					if (!CommonUtil.isEmptyString(trial.getDescription())) {
						x = cursor.getBlockX();
						y -= height1 + getYFrameIndent();
						height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
								L10nUtil.getInquiriesPDFLabel(Locales.INQUIRIES_PDF, InquiriesPDFLabelCodes.TRIAL_DESCRIPTION_LABEL, PDFUtil.DEFAULT_LABEL),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN1_INDENT)
								- getXFrameIndent());
						x += Settings.getFloat(InquiriesPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.X_HEAD_COLUMN1_INDENT);
						height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontC(), FontSize.MEDIUM, getTextColor(),
								trial.getDescription(),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								cursor.getBlockWidth() - x - 2.0f * getXFrameIndent()), height1);
					}
					x = cursor.getBlockX();
					y -= height1 + getYFrameIndent();
				}
				PDFUtil.renderFrame(
						contentStream,
						getFrameColor(),
						cursor.getBlockX(),
						// + (cursor.hasSection() ? Settings.getFloat(InquiriesPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.INQUIRIES_PDF,
						// InquiriesPDFDefaultSettings.X_BOX_FRAME_INDENT) : 0.0f),
						y1,
						cursor.getBlockWidth(), // .getIndexWidth(),
						y1 - y,
						// - Settings.getFloat(InquiriesPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_BOX_FRAME_INDENT),
						Alignment.TOP_LEFT,
						Settings.getFloat(InquiriesPDFSettingCodes.HEAD_FRAME_LINE_WIDTH, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.HEAD_FRAME_LINE_WIDTH));
				y -= Settings.getFloat(InquiriesPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				height = cursor.getBlockY() - y;

				break;
			case NEW_CATEGORY:
				height = 0.0f;
				if (cursor.hasCategory()) {
					if (!inserted) {
						height += Settings.getFloat(InquiriesPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_BOX_FRAME_INDENT);
						height += PDFUtil.renderTextLine(
								contentStream,
								cursor.getFontB(),
								FontSize.BIG,
								getTextColor(),
								cursor.getCategoryLabel(),
								cursor.getBlockX(),
								cursor.getBlockY()
								- Settings.getFloat(InquiriesPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_BOX_FRAME_INDENT),
								Alignment.TOP_LEFT);
					}
					height += Settings.getFloat(InquiriesPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				}
				break;

			case END_OF_CATEGORY:
				height = 0.0f;
				if (cursor.hasCategory()) {
					// PDFUtil.renderFrame(contentStream,
					// getFrameColor(),
					// cursor.getBlockX(),
					// cursor.getSectionY(),
					// cursor.getSectionWidth(),
					// cursor.getSectionY() - cursor.getBlockY(),
					// // + Settings.getFloat(InquiriesPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_BOX_FRAME_INDENT),
					// Alignment.TOP_LEFT,
					// Settings.getFloat(InquiriesPDFSettingCodes.SECTION_FRAME_LINE_WIDTH, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.SECTION_FRAME_LINE_WIDTH));
					height += Settings.getFloat(InquiriesPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				}
				break;
			case INPUT_FIELD:
				height = renderInputFieldBlock(contentStream, cursor) +
				Settings.getFloat(InquiriesPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.INQUIRIES_PDF, InquiriesPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				break;
			default:
				height = 0.0f;
				break;
		}
		return height;
	}
}
