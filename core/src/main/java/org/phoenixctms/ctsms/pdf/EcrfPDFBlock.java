package org.phoenixctms.ctsms.pdf;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.phoenixctms.ctsms.adapt.InputFieldValueStringAdapterBase;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.pdf.PDFUtil.Alignment;
import org.phoenixctms.ctsms.pdf.PDFUtil.FontSize;
import org.phoenixctms.ctsms.pdf.PDFUtil.LineStyle;
import org.phoenixctms.ctsms.pdf.PDFUtil.TextDecoration;
import org.phoenixctms.ctsms.security.EntitySignature;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.vo.SignatureVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

public class EcrfPDFBlock extends InputFieldPDFBlock {

	public enum BlockType {
		// NEW_PAGE,
		// NEW_LIST_ENTRY,
		PAGE_TITLE,
		NEW_ECRF,
		LIST_ENTRY_TAG_VALUE,
		NEW_SECTION,
		NEW_INDEX,
		INPUT_FIELD,
		AUDIT_TRAIL_VALUE,
		FIELD_STATUS_ENTRY,
		END_OF_INDEX,
		END_OF_SECTION,
		// ECRF_SIGNATURE,
		// SPACER,
	}

	private ECRFFieldValueOutVO auditTrailValue;
	private ECRFFieldStatusEntryOutVO fieldStatusEntry;
	private ProbandListEntryTagValueOutVO listEntryTagValuesVO;

	private ECRFFieldValueOutVO value;
	private ProbandListEntryOutVO listEntry;
	private ECRFOutVO ecrf;
	private ECRFStatusEntryVO statusEntry;
	private SignatureVO signature;
	private String section;
	private Long index;
	private BlockType type;
	private boolean inserted = false;
	private Date now;
	private boolean hasNext = false;

	private final static InputFieldValueStringAdapterBase INPUT_FIELD_VALUE_ADAPTER = new InputFieldValueStringAdapterBase<ECRFFieldValueOutVO>() {

		@Override
		protected boolean getBooleanValue(ECRFFieldValueOutVO value) {
			return value.getBooleanValue();
		}

		@Override
		protected String getCheckboxString(boolean value) {
			return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF,
					value ? EcrfPDFLabelCodes.INPUT_FIELD_VALUE_CHECKBOX_CHECKED : EcrfPDFLabelCodes.INPUT_FIELD_VALUE_CHECKBOX_UNCHECKED, PDFUtil.DEFAULT_LABEL);
		}

		@Override
		protected DateFormat getDateFormat() {
			return Settings.getSimpleDateFormat(EcrfPDFSettingCodes.DATE_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.DATE_VALUE_PATTERN, Locales.ECRF_PDF);
		}

		@Override
		protected DateFormat getDateTimeFormat() {
			return Settings.getSimpleDateFormat(EcrfPDFSettingCodes.TIMESTAMP_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.TIMESTAMP_VALUE_PATTERN, Locales.ECRF_PDF);
		}

		@Override
		protected Date getDateValue(ECRFFieldValueOutVO value) {
			return value.getDateValue();
		}

		@Override
		protected Float getFloatValue(ECRFFieldValueOutVO value) {
			return value.getFloatValue();
		}

		@Override
		protected Long getLongValue(ECRFFieldValueOutVO value) {
			return value.getLongValue();
		}

		@Override
		protected Collection<InputFieldSelectionSetValueOutVO> getSelectionSetValues(ECRFFieldValueOutVO value) {
			return value.getSelectionValues();
		}

		@Override
		protected String getSelectionSetValuesSeparator() {
			return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.INPUT_FIELD_VALUE_SELECTION_SET_VALUES_SEPARATOR, PDFUtil.DEFAULT_LABEL);
		}

		@Override
		protected Integer getTextClipMaxLength() {
			return null;
		}

		@Override
		protected String getTextValue(ECRFFieldValueOutVO value) {
			return value.getTextValue();
		}

		@Override
		protected DateFormat getTimeFormat() {
			return Settings.getSimpleDateFormat(EcrfPDFSettingCodes.TIME_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.TIME_VALUE_PATTERN, Locales.ECRF_PDF);
		}

		@Override
		protected Date getTimestampValue(ECRFFieldValueOutVO value) {
			return value.getTimestampValue();
		}

		@Override
		protected Date getTimeValue(ECRFFieldValueOutVO value) {
			return value.getTimeValue();
		}
	};

	public EcrfPDFBlock(BlockType type, boolean inserted) {
		super();
		this.type = type;
		this.inserted = inserted;
	}

	// public EcrfPDFBlock() {
	// type = BlockType.SPACER;
	// }

	// public EcrfPDFBlock(BlockType type) {
	// this.type = type;
	// }
	public EcrfPDFBlock(ECRFFieldValueOutVO value, PDFJpeg ximage, boolean blank, boolean hasNext) {
		super(value.getEcrfField().getField(), ximage, blank);
		this.value = value;
		this.hasNext = hasNext;
		this.type = BlockType.INPUT_FIELD;
	}

	public EcrfPDFBlock(EcrfPDFBlock block, BlockType type, boolean inserted) {
		super(block);
		value = block.value;
		listEntry = block.listEntry;
		ecrf = block.ecrf;
		statusEntry = block.statusEntry;
		signature = block.signature;
		section = block.section;
		index = block.index;
		auditTrailValue = block.auditTrailValue;
		fieldStatusEntry = block.fieldStatusEntry;
		listEntryTagValuesVO = block.listEntryTagValuesVO;
		hasNext = block.hasNext;
		this.type = type;
		now = block.now;
		this.inserted = inserted;
	}

	public EcrfPDFBlock(EcrfPDFBlock block,boolean inserted) {
		this(block, block.type, inserted);
	}

	public EcrfPDFBlock(Object logItem, boolean hasNext) {
		super();
		this.hasNext = hasNext;
		if (logItem instanceof ECRFFieldValueOutVO) {
			this.auditTrailValue = (ECRFFieldValueOutVO) logItem;
			this.type = BlockType.AUDIT_TRAIL_VALUE;
		} else if (logItem instanceof ECRFFieldStatusEntryOutVO) {
			this.fieldStatusEntry = (ECRFFieldStatusEntryOutVO) logItem;
			this.type = BlockType.FIELD_STATUS_ENTRY;
		}
	}

	// public EcrfPDFBlock(ProbandListEntryOutVO listEntry) {
	// super();
	// this.listEntry = listEntry;
	// this.type = BlockType.NEW_LIST_ENTRY;
	// }

	public EcrfPDFBlock(ProbandListEntryOutVO listEntry, ECRFOutVO ecrf, ECRFStatusEntryVO statusEntry, SignatureVO signature, Date now, boolean blank) {
		super();
		this.listEntry = listEntry;
		this.ecrf = ecrf;
		this.statusEntry = statusEntry;
		this.signature = signature;
		this.now = now;
		this.blank = blank;
		this.type = BlockType.NEW_ECRF;
	}

	public EcrfPDFBlock(ProbandListEntryTagValueOutVO listEntryTagValuesVO, PDFJpeg ximage, boolean blank, boolean hasNext) {
		super();
		this.ximage = ximage;
		this.blank = blank;
		this.hasNext = hasNext;
		this.listEntryTagValuesVO = listEntryTagValuesVO;
		this.type = BlockType.LIST_ENTRY_TAG_VALUE;
	}

	// public EcrfPDFBlock(SignatureVO signature) {
	// super();
	// this.signature = signature;
	// this.type = BlockType.ECRF_SIGNATURE;
	// }

	public EcrfPDFBlock(String section) {
		super();
		this.section = section;
		this.type = BlockType.NEW_SECTION;
	}

	public EcrfPDFBlock(String section, Long index) {
		super();
		this.section = section;
		this.index = index;
		this.type = BlockType.NEW_INDEX;
	}

	@Override
	protected boolean getBooleanValue() {
		return value.getBooleanValue();
	}

	@Override
	protected String getComment() {
		return value.getEcrfField().getComment();
	}

	@Override
	protected Date getDateValue() {
		return value.getDateValue();
	}

	@Override
	protected SimpleDateFormat getDateValueFormat() {
		return Settings.getSimpleDateFormat(EcrfPDFSettingCodes.DATE_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.DATE_VALUE_PATTERN, Locales.ECRF_PDF);
	}

	@Override
	protected String getDateValueFormatPattern() {
		return Settings.getString(EcrfPDFSettingCodes.DATE_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.DATE_VALUE_PATTERN);
	}

	@Override
	protected float getFieldFrameLineWidth() {
		return Settings.getFloat(EcrfPDFSettingCodes.FIELD_FRAME_LINE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.FIELD_FRAME_LINE_WIDTH);
	}

	@Override
	protected Float getFloatValue() {
		return value.getFloatValue();
	}

	@Override
	protected DecimalFormat getFloatValueFormat() {
		return Settings.getDecimalFormat(EcrfPDFSettingCodes.FLOAT_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.FLOAT_VALUE_PATTERN, Locales.ECRF_PDF);
	}

	@Override
	protected String getFloatValueFormatPattern() {
		return Settings.getString(EcrfPDFSettingCodes.FLOAT_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.FLOAT_VALUE_PATTERN);
	}

	@Override
	protected Color getFrameColor() {
		return Settings.getColor(EcrfPDFSettingCodes.FRAME_COLOR, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.FRAME_COLOR);
	}

	public float getHeight(EcrfPDFBlockCursor cursor) throws Exception {
		return renderBlock(null, cursor);
	}

	@Override
	protected Float getHorizontalSelectionItemWidth() {
		return Settings.getFloatNullable(EcrfPDFSettingCodes.HORIZONTAL_SELECTION_ITEM_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.HORIZONTAL_SELECTION_ITEM_WIDTH);
	}

	public Long getIndex() {
		return index;
	}

	@Override
	protected String getInputFieldCommentLabel(String comment) {
		return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.INPUT_FIELD_COMMENT, PDFUtil.DEFAULT_LABEL, comment);
	}

	@Override
	protected String getInputFieldNameLabel(String name, String position) {
		return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.INPUT_FIELD_NAME, PDFUtil.DEFAULT_LABEL, name, position);
	}

	@Override
	protected String getInputFieldTitleLabel(String position,String title) {
		return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.INPUT_FIELD_TITLE, PDFUtil.DEFAULT_LABEL,position, title);
	}

	@Override
	protected String getInputFieldTitleOptionalLabel(String position,String title) {
		return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.INPUT_FIELD_TITLE_OPTIONAL, PDFUtil.DEFAULT_LABEL,position, title);
	}

	@Override
	protected String getInputFieldValidationErrorMessageLabel(String validationErrorMessage) {
		return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.INPUT_FIELD_VALIDATION_ERROR_MESSAGE, PDFUtil.DEFAULT_LABEL, validationErrorMessage);
	}

	@Override
	protected DecimalFormat getIntegerValueFormat() {
		return Settings.getDecimalFormat(EcrfPDFSettingCodes.INTEGER_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.INTEGER_VALUE_PATTERN, Locales.ECRF_PDF);
	}

	@Override
	protected String getIntegerValueFormatPattern() {
		return Settings.getString(EcrfPDFSettingCodes.INTEGER_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.INTEGER_VALUE_PATTERN);
	}

	@Override
	protected Locales getLabelLocale() {
		return Locales.ECRF_PDF;
	}

	public ProbandListEntryOutVO getListEntry() {
		return listEntry;
	}

	private float getLogFrameLineWidth() {
		return Settings.getFloat(EcrfPDFSettingCodes.LOG_FRAME_LINE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.LOG_FRAME_LINE_WIDTH);
	}

	@Override
	protected Long getLongValue() {
		return value.getLongValue();
	}

	@Override
	protected String  getModifiedLabel(String modifiedUser, String modifiedTimestamp) {
		return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.MODIFIED_LABEL, PDFUtil.DEFAULT_LABEL, modifiedUser, modifiedTimestamp);
	}

	@Override
	protected Date getModifiedTimestamp() {
		return value.getModifiedTimestamp();
	}

	@Override
	protected SimpleDateFormat getModifiedTimestampFormat() {
		return Settings.getSimpleDateFormat(EcrfPDFSettingCodes.MODIFIED_TIMESTAMP_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.MODIFIED_TIMESTAMP_PATTERN, Locales.ECRF_PDF);
	}

	@Override
	protected UserOutVO getModifiedUser() {
		return value.getModifiedUser();
	}

	@Override
	protected float getMultiLineTextMinHeight() {
		return Settings.getFloat(EcrfPDFSettingCodes.MULTI_LINE_TEXT_MIN_HEIGHT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.MULTI_LINE_TEXT_MIN_HEIGHT);
	}

	@Override
	protected long getPosition() {
		return value.getEcrfField().getPosition();
	}

	@Override
	protected Color getPresetTextColor() {
		return Settings.getColor(EcrfPDFSettingCodes.PRESET_TEXT_COLOR, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.PRESET_TEXT_COLOR);
	}

	@Override
	protected boolean getRenderSketchImages() {
		return Settings.getBoolean(EcrfPDFSettingCodes.RENDER_SKETCH_IMAGES, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.RENDER_SKETCH_IMAGES);
	}

	public String getSection() {
		return section;
	}

	@Override
	protected String getSelectionSetValueLabel(String name, String value) {
		return L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.SELECTION_SET_VALUE_NAME, PDFUtil.DEFAULT_LABEL, name,value);
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getSelectionValues() {
		return value.getSelectionValues();
	}

	private String getStatusTypeName() {
		if (statusEntry != null && statusEntry.getStatus() != null) {
			return L10nUtil.getEcrfStatusTypeName(Locales.ECRF_PDF, statusEntry.getStatus().getNameL10nKey());
		}
		return "";
	}

	private String getStatusUser() {
		if (statusEntry != null && statusEntry.getModifiedUser() != null) {
			if (statusEntry.getModifiedUser().getIdentity() != null) {
				return CommonUtil.staffOutVOToString(statusEntry.getModifiedUser().getIdentity());
			} else {
				return CommonUtil.userOutVOToString(statusEntry.getModifiedUser());
			}
		}
		return "";
	}


	@Override
	protected Color getTextColor() {
		return Settings.getColor(EcrfPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.TEXT_COLOR);
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
		return Settings.getSimpleDateFormat(EcrfPDFSettingCodes.TIMESTAMP_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.TIMESTAMP_VALUE_PATTERN, Locales.ECRF_PDF);
	}

	@Override
	protected String getTimestampValueFormatPattern() {
		return Settings.getString(EcrfPDFSettingCodes.TIMESTAMP_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.TIMESTAMP_VALUE_PATTERN);
	}

	@Override
	protected Date getTimeValue() {
		return value.getTimeValue();
	}

	// @Override
	// protected boolean isShowValidationErrorMessages() {
	// return Settings.getBoolean(EcrfPDFSettingCodes.SHOW_VALIDATION_ERROR_MESSAGES, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SHOW_VALIDATION_ERROR_MESSAGES);
	// }

	@Override
	protected SimpleDateFormat getTimeValueFormat() {
		return Settings.getSimpleDateFormat(EcrfPDFSettingCodes.TIME_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.TIME_VALUE_PATTERN, Locales.ECRF_PDF);
	}

	@Override
	protected String getTimeValueFormatPattern() {
		return Settings.getString(EcrfPDFSettingCodes.TIME_VALUE_PATTERN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.TIME_VALUE_PATTERN);
	}

	public BlockType getType() {
		return type;
	}

	@Override
	protected Color getValidationErrorMessageTextColor() {
		return Settings.getColor(EcrfPDFSettingCodes.VALIDATION_ERROR_MESSAGE_TEXT_COLOR, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.VALIDATION_ERROR_MESSAGE_TEXT_COLOR);
	}

	@Override
	protected float getValueFrameLineWidth() {
		return Settings.getFloat(EcrfPDFSettingCodes.VALUE_FRAME_LINE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.VALUE_FRAME_LINE_WIDTH);
	}

	@Override
	protected float getXFieldColumnIndent() {
		return Settings.getFloat(EcrfPDFSettingCodes.X_FIELD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_FIELD_COLUMN_INDENT);
	}

	@Override
	protected float getXFrameIndent() {
		return Settings.getFloat(EcrfPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_FRAME_INDENT);
	}

	@Override
	protected float getXSelectionItemImageLabelOffset() {
		return Settings.getFloat(EcrfPDFSettingCodes.X_SELECTION_ITEM_IMAGE_LABEL_OFFSET, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_SELECTION_ITEM_IMAGE_LABEL_OFFSET);
	}

	@Override
	protected float getXSelectionItemIndent() {
		return Settings.getFloat(EcrfPDFSettingCodes.X_SELECTION_ITEM_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_SELECTION_ITEM_INDENT);
	}

	@Override
	protected float getXValueFrameIndent() {
		return Settings.getFloat(EcrfPDFSettingCodes.X_VALUE_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_VALUE_FRAME_INDENT);
	}

	@Override
	protected float getYFrameIndent() {
		return Settings.getFloat(EcrfPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_FRAME_INDENT);
	}

	@Override
	protected float getYSelectionItemImageLabelOffset() {
		return Settings.getFloat(EcrfPDFSettingCodes.Y_SELECTION_ITEM_IMAGE_LABEL_OFFSET, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_SELECTION_ITEM_IMAGE_LABEL_OFFSET);
	}

	@Override
	protected float getYSelectionItemIndent() {
		return Settings.getFloat(EcrfPDFSettingCodes.Y_SELECTION_ITEM_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_SELECTION_ITEM_INDENT);
	}

	@Override
	protected float getYValueFrameIndent() {
		return Settings.getFloat(EcrfPDFSettingCodes.Y_VALUE_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_VALUE_FRAME_INDENT);
	}

	@Override
	protected boolean isDisabled() {
		return value.getEcrfField().getDisabled();
	}

	protected boolean isInputFieldLongTitle() {
		String title = getInputFieldTitle();
		if (title != null && title.length() > Settings.getInt(EcrfPDFSettingCodes.LONG_TITLE_LENGTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.LONG_TITLE_LENGTH)) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean isOptional() {
		return value.getEcrfField().getOptional();
	}

	@Override
	protected boolean isPreset() {
		return value.getId() == null;
	}

	@Override
	protected boolean isShowModifiedLabel() {
		return Settings.getBoolean(EcrfPDFSettingCodes.SHOW_MODIFIED_LABEL, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SHOW_MODIFIED_LABEL);
	}

	@Override
	protected boolean isShowPresetValues() {
		return Settings.getBoolean(EcrfPDFSettingCodes.SHOW_PRESET_VALUES, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SHOW_PRESET_VALUES);
	}

	public float renderBlock(PDPageContentStream contentStream, EcrfPDFBlockCursor cursor) throws Exception {
		// if (contentStream != null) {
		// System.out.println("rendering block " + type);
		// }
		float x;
		float y;
		float y1;
		float height1;
		float height2;
		float height3;
		float height;
		float width;
		PDFJpeg ximage;
		switch (type) {
			case PAGE_TITLE:
				height = PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.BIG, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.PAGE_TITLE, PDFUtil.DEFAULT_LABEL, listEntry.getTrial().getName()
								, ecrf.getName(), // + "xxxxxxxxxxxxxxxxxxxxxxxxxXXXXXXXXXXXXX"
								Long.toString(listEntry.getProband().getId()), listEntry
								.getProband()
								.getInitials(), listEntry.getProband().getName(),
								listEntry.getProband().getDateOfBirth() != null ? Settings.getSimpleDateFormat(
										EcrfPDFSettingCodes.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
										Bundle.ECRF_PDF,
										EcrfPDFDefaultSettings.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
										Locales.ECRF_PDF).format(listEntry.getProband().getDateOfBirth()) : "",
										listEntry.getProband().getYearOfBirth() != null ? Integer.toString(listEntry.getProband().getYearOfBirth()) : "",
												listEntry.getProband().getAge() != null ? Integer.toString(listEntry.getProband().getAge()) : ""
								),
								cursor.getBlockX(),
								Settings.getFloat(EcrfPDFSettingCodes.PAGE_TITLE_Y, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.PAGE_TITLE_Y),
								Alignment.TOP_LEFT,
								cursor.getBlockWidth());
				break;
			case NEW_ECRF:
				width = (cursor.getBlockWidth() - 3.0f * Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF,
						EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT)) / 3.0f;
				x = cursor.getBlockX();
				y = cursor.getBlockY();
				if (!inserted) {
					y -= Settings.getFloat(EcrfPDFSettingCodes.Y_HEADLINE_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_HEADLINE_INDENT);
					y -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.LARGE, getTextColor(),
							ecrf.getUniqueName(),
							cursor.getBlockCenterX(),
							y,
							Alignment.TOP_CENTER,
							cursor.getBlockWidth());
					y -= Settings.getFloat(EcrfPDFSettingCodes.Y_HEADLINE_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_HEADLINE_INDENT);
				}
				y1 = y;
				y -= getYFrameIndent();
				height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.TRIAL_NAME_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT) - getXFrameIndent());
				x += Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.TRIAL_NAME, PDFUtil.DEFAULT_LABEL, listEntry.getTrial().getName(), listEntry.getTrial()
								.getTitle()),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								width - getXFrameIndent()), height1);
				x += width;
				height2 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.PROBAND_NAME_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT) - getXFrameIndent());
				x += Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height2 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.PROBAND_NAME, PDFUtil.DEFAULT_LABEL, Long.toString(listEntry.getProband().getId()), listEntry
								.getProband()
								.getInitials(), listEntry.getProband().getName()),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								width - getXFrameIndent()), height2);
				x += width;
				height3 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.PROBAND_DATE_OF_BIRTH_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT) - getXFrameIndent());
				x += Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height3 = Math.max(
						PDFUtil.renderMultilineText(
								contentStream,
								cursor.getFontA(),
								FontSize.MEDIUM,
								getTextColor(),
								L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.PROBAND_DATE_OF_BIRTH, PDFUtil.DEFAULT_LABEL,
										listEntry.getProband().getDateOfBirth() != null ? Settings.getSimpleDateFormat(
												EcrfPDFSettingCodes.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
												Bundle.ECRF_PDF,
												EcrfPDFDefaultSettings.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
												Locales.ECRF_PDF).format(listEntry.getProband().getDateOfBirth()) : "",
												listEntry.getProband().getYearOfBirth() != null ? Integer.toString(listEntry.getProband().getYearOfBirth()) : "",
														listEntry.getProband().getAge() != null ? Integer.toString(listEntry.getProband().getAge()) : ""),
														x + getXFrameIndent(),
														y,
														Alignment.TOP_LEFT,
														width - getXFrameIndent()), height3);
				//
				x = cursor.getBlockX();
				y -= Math.max(Math.max(height1, height2), height3) + getYFrameIndent();
				height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_NAME_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT) - getXFrameIndent());
				x += Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_NAME, PDFUtil.DEFAULT_LABEL, ecrf.getName()),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						width - getXFrameIndent()), height1);
				x += width;
				height2 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.GROUP_TITLE_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT) - getXFrameIndent());
				x += Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height2 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.GROUP_TITLE, PDFUtil.DEFAULT_LABEL, ecrf.getGroup() != null ? ecrf.getGroup().getTitle() : "",
								ecrf.getGroup() != null ? ecrf.getGroup().getToken() : ""),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								width - getXFrameIndent()), height2);
				x += width;
				height3 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.VISIT_TITLE_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT) - getXFrameIndent());
				x += Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height3 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.VISIT_TITLE, PDFUtil.DEFAULT_LABEL, ecrf.getVisit() != null ? ecrf.getVisit().getTitle() : "",
								ecrf.getVisit() != null ? ecrf.getVisit().getToken() : ""),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								width - getXFrameIndent()), height3);
				//
				x = cursor.getBlockX();
				y -= Math.max(Math.max(height1, height2), height3) + getYFrameIndent();
				height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_STATUS_TYPE_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT) - getXFrameIndent());
				x += Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						blank ? L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_STATUS_TYPE_BLANK, PDFUtil.DEFAULT_LABEL, getStatusTypeName()) :
							L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_STATUS_TYPE, PDFUtil.DEFAULT_LABEL, getStatusTypeName()),
							x + getXFrameIndent(),
							y,
							Alignment.TOP_LEFT,
							width - getXFrameIndent()), height1);
				x += width;
				height2 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_STATUS_USER_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT) - getXFrameIndent());
				x += Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height2 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_STATUS_USER, PDFUtil.DEFAULT_LABEL, getStatusUser()),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						width - getXFrameIndent()), height2);
				x += width;
				height3 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.CONTENT_TIMESTAMP_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT) - getXFrameIndent());
				x += Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height3 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.CONTENT_TIMESTAMP, PDFUtil.DEFAULT_LABEL, Settings.getSimpleDateFormat(
								EcrfPDFSettingCodes.CONTENT_TIMESTAMP_DATETIME_PATTERN,
								Bundle.ECRF_PDF,
								EcrfPDFDefaultSettings.CONTENT_TIMESTAMP_DATETIME_PATTERN,
								Locales.ECRF_PDF).format(now)),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								width - getXFrameIndent()), height3);
				x = cursor.getBlockX();
				y -= Math.max(Math.max(height1, height2), height3) + getYFrameIndent();
				if (!inserted) {
					height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
							L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_TITLE_LABEL, PDFUtil.DEFAULT_LABEL),
							x + getXFrameIndent(),
							y,
							Alignment.TOP_LEFT,
							Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN1_INDENT) - getXFrameIndent());
					x += Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN1_INDENT);
					height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontC(), FontSize.MEDIUM, getTextColor(),
							ecrf.getTitle(),
							x + getXFrameIndent(),
							y,
							Alignment.TOP_LEFT,
							cursor.getBlockWidth() - x - 2.0f * getXFrameIndent()), height1);
					if (!CommonUtil.isEmptyString(ecrf.getDescription())) {
						x = cursor.getBlockX();
						y -= height1 + getYFrameIndent();
						height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
								L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.ECRF_DESCRIPTION_LABEL, PDFUtil.DEFAULT_LABEL),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN1_INDENT) - getXFrameIndent());
						x += Settings.getFloat(EcrfPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.X_HEAD_COLUMN1_INDENT);
						height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontC(), FontSize.MEDIUM, getTextColor(),
								ecrf.getDescription(),
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
						// + (cursor.hasSection() ? Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF,
						// EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT) : 0.0f),
						y1,
						cursor.getBlockWidth(), // .getIndexWidth(),
						y1 - y,
						// - Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT),
						Alignment.TOP_LEFT,
						Settings.getFloat(EcrfPDFSettingCodes.HEAD_FRAME_LINE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.HEAD_FRAME_LINE_WIDTH));
				y -= Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				height = cursor.getBlockY() - y;
				if (!inserted) {
					if (signature != null) {
						y1 = y;
						x = cursor.getBlockX() + getXFrameIndent();
						y -= getYFrameIndent();
						if (signature.getVerified()) {
							if (signature.getValid()) {
								ximage = cursor.getSignatureValidImage();
							} else {
								ximage = cursor.getSignatureInvalidImage();
							}
						} else {
							ximage = cursor.getSignatureAvailableImage();
						}
						PDFUtil.renderImage(contentStream, ximage, x, y, Alignment.TOP_LEFT);
						x += ximage.getWidthPoints();
						y -= Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.SMALL, getTextColor(),
								EntitySignature.getDescription(signature, Locales.ECRF_PDF,
												Settings.getIntNullable(EcrfPDFSettingCodes.SIGNATURE_VALUE_LENGTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SIGNATURE_VALUE_LENGTH),
												false),
										x + getXFrameIndent(),
										y,
										Alignment.TOP_LEFT,
										cursor.getBlockWidth() - x - 2.0f * getXFrameIndent()), ximage.getHeightPoints());
						y -= getYFrameIndent();
						height += y1 - y;
						PDFUtil.renderFrame(contentStream,
								getFrameColor(),
								cursor.getBlockX(),
								y1,
								cursor.getBlockWidth(),
								y1 - y,
								Alignment.TOP_LEFT,
								Settings.getFloat(EcrfPDFSettingCodes.HEAD_FRAME_LINE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.HEAD_FRAME_LINE_WIDTH));
						// Settings.getFloat(EcrfPDFSettingCodes.SIGNATURE_FRAME_LINE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SIGNATURE_FRAME_LINE_WIDTH));
					}
					height += Settings.getFloat(EcrfPDFSettingCodes.Y_HEADLINE_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_HEADLINE_INDENT);
				}
				break;
			case NEW_SECTION:
				height = 0.0f;
				if (cursor.hasSection() || contentStream == null) {
					if (!inserted) {
						height += Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT);
						height += PDFUtil.renderTextLine(contentStream, cursor.getFontB(), FontSize.BIG, getTextColor(), cursor.getSectionLabel(), cursor.getBlockX(),
								cursor.getBlockY() - Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT),
								Alignment.TOP_LEFT);
					}
					height += Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				}
				break;
			case NEW_INDEX:
				height = 0.0f;
				if (cursor.hasIndex() || contentStream == null) { // always allow getHeight
					if (!inserted) {
						height += Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT);
						height += PDFUtil.renderTextLine(contentStream, cursor.getFontA(), FontSize.BIG, getTextColor(), cursor.getIndexLabel(), cursor.getBlockIndentedX(),
								cursor.getBlockY() - Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT),
								Alignment.TOP_LEFT);
					}
					height += Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				}
				break;
			case END_OF_INDEX:
				height = 0.0f;
				if (cursor.hasIndex()) {
					// PDFUtil.renderFrame(
					// contentStream,
					// getFrameColor(),
					// cursor.getBlockX()
					// + (cursor.hasSection() ? (cursor.getSectionWidth() + Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF,
					// EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT)) : 0.0f),
					// cursor.getIndexY(),
					// cursor.getIndexWidth(),
					// cursor.getIndexY() - cursor.getBlockY(),
					// // - Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT),
					// Alignment.TOP_LEFT,
					// Settings.getFloat(EcrfPDFSettingCodes.INDEX_FRAME_LINE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.INDEX_FRAME_LINE_WIDTH));
					PDFUtil.renderFrame(
							contentStream,
							getFrameColor(),
							cursor.getBlockX(),
							// + (cursor.hasSection() ? Settings.getFloat(EcrfPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.ECRF_PDF,
							// EcrfPDFDefaultSettings.X_BOX_FRAME_INDENT) : 0.0f),
							cursor.getIndexY(),
							cursor.getBlockWidth(), // .getIndexWidth(),
							cursor.getIndexY() - cursor.getBlockY(),
							// - Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT),
							Alignment.TOP_LEFT,
							Settings.getFloat(EcrfPDFSettingCodes.INDEX_FRAME_LINE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.INDEX_FRAME_LINE_WIDTH),
							LineStyle.DASHED);
					height += Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				}
				break;
			case END_OF_SECTION:
				height = 0.0f;
				if (cursor.hasSection()) {
					// PDFUtil.renderFrame(contentStream,
					// getFrameColor(),
					// cursor.getBlockX(),
					// cursor.getSectionY(),
					// cursor.getSectionWidth(),
					// cursor.getSectionY() - cursor.getBlockY(),
					// // + Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT),
					// Alignment.TOP_LEFT,
					// Settings.getFloat(EcrfPDFSettingCodes.SECTION_FRAME_LINE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SECTION_FRAME_LINE_WIDTH));
					height += Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				}
				break;
			case INPUT_FIELD:
				height = renderInputFieldBlock(contentStream, cursor) +
				(hasNext ? Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_LOG_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_LOG_FRAME_INDENT) : Settings
						.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT));
				break;
			case LIST_ENTRY_TAG_VALUE:
				height = (new ProbandListEntryTagsPDFBlock(listEntryTagValuesVO, this.ximage, blank)).renderInputFieldBlock(contentStream, cursor) +
				(hasNext ? Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT) : Settings
						.getFloat(EcrfPDFSettingCodes.Y_HEADLINE_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_HEADLINE_INDENT));
				break;
			case AUDIT_TRAIL_VALUE:
				x = cursor.getBlockIndentedX();
				y = cursor.getBlockY() - getYFrameIndent();
				y -= Math.max(PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.SMALL,
						getTextColor(),
						L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.VALUE_SAVED_LABEL, PDFUtil.DEFAULT_LABEL, auditTrailValue.getVersion()),
						x + getXFrameIndent(),
						y,
						PDFUtil.Alignment.TOP_LEFT,
						getXFieldColumnIndent() - getXFrameIndent()), // , TextDecoration.UNDERLINE),
						PDFUtil.renderMultilineText(
								contentStream,
								cursor.getFontA(),
								PDFUtil.FontSize.SMALL,
								getTextColor(),
								INPUT_FIELD_VALUE_ADAPTER.toString(auditTrailValue.getEcrfField().getField(), auditTrailValue),
								x + getXFieldColumnIndent() + getXFrameIndent(),
								y,
								PDFUtil.Alignment.TOP_LEFT,
								cursor.getBlockIndentedX() + cursor.getBlockIndentedWidth() - (x + getXFieldColumnIndent() + getXFrameIndent()) - getXFrameIndent(),
								TextDecoration.STRIKE_THROUGH))
								+ getYFrameIndent();
				y -= Math.max(PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.TINY,
						getTextColor(),
						getModifiedLabel(auditTrailValue.getModifiedUser(), auditTrailValue.getModifiedTimestamp()),
						x + getXFrameIndent(),
						y,
						PDFUtil.Alignment.TOP_LEFT,
						getXFieldColumnIndent() - getXFrameIndent()),
						PDFUtil.renderMultilineText(
								contentStream,
								cursor.getFontC(),
								PDFUtil.FontSize.SMALL,
								getTextColor(),
								auditTrailValue.getReasonForChange(),
								x + getXFieldColumnIndent() + getXFrameIndent(),
								y,
								PDFUtil.Alignment.TOP_LEFT,
								cursor.getBlockIndentedX() + cursor.getBlockIndentedWidth() - (x + getXFieldColumnIndent() + getXFrameIndent()) - getXFrameIndent()))
								+ getYFrameIndent();
				height = cursor.getBlockY() - y;
				PDFUtil.renderFrame(contentStream, getFrameColor(),
						cursor.getBlockIndentedX(), cursor.getBlockY(), cursor.getBlockIndentedWidth(), height,
						PDFUtil.Alignment.TOP_LEFT, getLogFrameLineWidth());
				height += (hasNext ? Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_LOG_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_LOG_FRAME_INDENT) : Settings
						.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT));
				// height += Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				// if (isInputFieldLongTitle()) {
				// y -= renderTitle(x, y, contentStream, cursor) + getYFrameIndent();
				// y -= renderTextValue(x + getXFieldColumnIndent() + getXFrameIndent(), y, fieldType, contentStream, cursor) + getYFrameIndent();
				// } else {
				// y1 = y;
				// y2 = y;
				// y1 -= renderTitle(x, y1, contentStream, cursor);
				// y2 -= renderTextValue(x + getXFieldColumnIndent() + getXFrameIndent(), y2, fieldType, contentStream, cursor);
				// y = Math.min(y1, y2) - getYFrameIndent();
				// }
				// y -= renderComments(x, y, contentStream, cursor);
				// height = cursor.getBlockY() - y;
				// renderFrame(height, contentStream, cursor);
				break;
			case FIELD_STATUS_ENTRY:
				x = cursor.getBlockIndentedX();
				y = cursor.getBlockY() - getYFrameIndent();
				y -= Math.max(
						PDFUtil.renderMultilineText(
								contentStream,
								cursor.getFontA(),
								PDFUtil.FontSize.SMALL,
								fieldStatusEntry.getStatus().getColor(),
								// getTextColor(),
								L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.FIELD_STATUS_LABEL, PDFUtil.DEFAULT_LABEL,
										L10nUtil.getEcrfFieldStatusTypeName(Locales.ECRF_PDF, fieldStatusEntry.getStatus().getNameL10nKey())),
										x + getXFrameIndent(),
										y,
										PDFUtil.Alignment.TOP_LEFT,
										getXFieldColumnIndent() - getXFrameIndent()),
										PDFUtil.renderMultilineText(
												contentStream,
												cursor.getFontC(),
												PDFUtil.FontSize.SMALL,
												getTextColor(),
												fieldStatusEntry.getComment(),
												x + getXFieldColumnIndent() + getXFrameIndent(),
												y,
												PDFUtil.Alignment.TOP_LEFT,
												cursor.getBlockIndentedX() + cursor.getBlockIndentedWidth() - (x + getXFieldColumnIndent() + getXFrameIndent()) - getXFrameIndent()
												))
												+ getYFrameIndent();
				y -= PDFUtil.renderTextLine(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.TINY,
						getTextColor(),
						getModifiedLabel(fieldStatusEntry.getModifiedUser(), fieldStatusEntry.getModifiedTimestamp()),
						x + getXFrameIndent(),
						y,
						PDFUtil.Alignment.TOP_LEFT) + getYFrameIndent();
				height = cursor.getBlockY() - y;
				PDFUtil.renderFrame(contentStream, getFrameColor(),
						cursor.getBlockIndentedX(), cursor.getBlockY(), cursor.getBlockIndentedWidth(), height,
						PDFUtil.Alignment.TOP_LEFT, getLogFrameLineWidth());
				height += (hasNext ? Settings.getFloat(EcrfPDFSettingCodes.Y_BOX_LOG_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_LOG_FRAME_INDENT) : Settings
						.getFloat(EcrfPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.Y_BOX_FRAME_INDENT));
				break;
				// case ECRF_SIGNATURE:
				// height = 0.0f;
				// // if (signature != null) {
				// // x = cursor.getBlockX() + getXFrameIndent();
				// // y = cursor.getBlockY() - getYFrameIndent();
				// // if (signature.getVerified()) {
				// // if (signature.getValid()) {
				// // ximage = cursor.getSignatureValidImage();
				// // } else {
				// // ximage = cursor.getSignatureInvalidImage();
				// // }
				// // } else {
				// // ximage = cursor.getSignatureAvailableImage();
				// // }
				// // PDFUtil.renderImage(contentStream, ximage, x, y, Alignment.TOP_LEFT);
				// // x += ximage.getWidthPoints();
				// // y -= Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.TINY, getTextColor(),
				// // EntitySignature.getDescription(signature, Locales.ECRF_PDF),
				// // x + getXFrameIndent(),
				// // y,
				// // Alignment.TOP_LEFT,
				// // cursor.getBlockWidth() - x - 2.0f * getXFrameIndent()), ximage.getHeightPoints());
				// // y -= getYFrameIndent();
				// // height = cursor.getBlockY() - y;
				// // PDFUtil.renderFrame(contentStream,
				// // getFrameColor(),
				// // cursor.getBlockX(),
				// // cursor.getBlockY(),
				// // cursor.getBlockWidth(),
				// // height,
				// // Alignment.TOP_LEFT,
				// // Settings.getFloat(EcrfPDFSettingCodes.SIGNATURE_FRAME_LINE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SIGNATURE_FRAME_LINE_WIDTH));
				// // }
				//
				// break;
				// case NEW_SECTION:
				// PDFUtil.renderFrame(contentStream,
				// getFrameColor(),
				// cursor.getBlockX(),
				// cursor.getPreviousSectionY(),
				// 50.0f,
				// cursor.getPreviousSectionY() - cursor.getBlockY(),
				// Alignment.TOP_LEFT,
				// 2.0f);
				// height = 0.0f;
				// break;
				// case NEW_INDEX:
				// PDFUtil.renderFrame(contentStream,
				// getFrameColor(),
				// cursor.getBlockX() + 20.0f,
				// cursor.getPreviousIndexY(),
				// 30.0f,
				// cursor.getPreviousIndexY() - cursor.getBlockY(),
				// Alignment.TOP_LEFT,
				// 2.0f);
				// height = 0.0f;
				// break;
			default:
				height = 0.0f;
				break;
		}
		return height;
	}
}
