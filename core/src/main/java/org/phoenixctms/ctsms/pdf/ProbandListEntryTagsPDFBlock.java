
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
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

public class ProbandListEntryTagsPDFBlock extends InputFieldPDFBlock {

	public enum BlockType {
		// NEW_PAGE,
		// NEW_LIST_ENTRY,
		PAGE_TITLE,
		NEW_LIST_ENTRY,
		INPUT_FIELD,

		// ECRF_SIGNATURE,
		// SPACER,
	}

	private ProbandListEntryTagValueOutVO value;
	private ProbandListEntryOutVO listEntry;
	// private ECRFOutVO ecrf;
	// private ECRFStatusEntryVO statusEntry;
	// private SignatureVO signature;
	// private String section;
	// private Long index;
	private BlockType type;
	private boolean inserted = false;
	private Date now;

	// public EcrfPDFBlock() {
	// type = BlockType.SPACER;
	// }
	public ProbandListEntryTagsPDFBlock(BlockType type, boolean inserted) {
		super();
		this.type = type;
		this.inserted = inserted;
	}

	// public EcrfPDFBlock(ProbandListEntryOutVO listEntry) {
	// super();
	// this.listEntry = listEntry;
	// this.type = BlockType.NEW_LIST_ENTRY;
	// }
	public ProbandListEntryTagsPDFBlock(ProbandListEntryOutVO listEntry,  Date now, boolean blank) {
		super();
		this.listEntry = listEntry;
		//		this.ecrf = ecrf;
		//		this.statusEntry = statusEntry;
		//		this.signature = signature;
		this.now = now;
		this.blank = blank;
		this.type = BlockType.NEW_LIST_ENTRY;
	}

	public ProbandListEntryTagsPDFBlock(ProbandListEntryTagsPDFBlock block, BlockType type, boolean inserted) {
		super(block);
		value = block.value;
		listEntry = block.listEntry;
		// ecrf = block.ecrf;
		// statusEntry = block.statusEntry;
		// signature = block.signature;
		// section = block.section;
		// index = block.index;
		this.type = type;
		now = block.now;
		this.inserted = inserted;
	}

	public ProbandListEntryTagsPDFBlock(ProbandListEntryTagsPDFBlock block, boolean inserted) {
		this(block, block.type, inserted);
	}

	// public EcrfPDFBlock(BlockType type) {
	// this.type = type;
	// }
	public ProbandListEntryTagsPDFBlock(ProbandListEntryTagValueOutVO value, PDFJpeg ximage, boolean blank) {
		super(value.getTag().getField(), ximage, blank);
		this.value = value;
		this.type = BlockType.INPUT_FIELD;
	}

	@Override
	protected boolean getBooleanValue() {
		return value.getBooleanValue();
	}

	// public EcrfPDFBlock(SignatureVO signature) {
	// super();
	// this.signature = signature;
	// this.type = BlockType.ECRF_SIGNATURE;
	// }
	// public ProbandListEntryTagsPDFBlock(String section) {
	// super();
	// this.section = section;
	// this.type = BlockType.NEW_SECTION;
	// }

	// public EcrfPDFBlock(String section, Long index) {
	// super();
	// this.section = section;
	// this.index = index;
	// this.type = BlockType.NEW_INDEX;
	// }

	@Override
	protected String getComment() {
		return value.getTag().getComment();
	}

	@Override
	protected Date getDateValue() {
		return value.getDateValue();
	}

	@Override
	protected SimpleDateFormat getDateValueFormat() {
		return Settings.getSimpleDateFormat(ProbandListEntryTagsPDFSettingCodes.DATE_VALUE_PATTERN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.DATE_VALUE_PATTERN, Locales.PROBAND_LIST_ENTRY_TAGS_PDF);
	}

	@Override
	protected String getDateValueFormatPattern() {
		return Settings.getString(ProbandListEntryTagsPDFSettingCodes.DATE_VALUE_PATTERN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.DATE_VALUE_PATTERN);
	}

	@Override
	protected float getFieldFrameLineWidth() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.FIELD_FRAME_LINE_WIDTH, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.FIELD_FRAME_LINE_WIDTH);
	}

	@Override
	protected Float getFloatValue() {
		return value.getFloatValue();
	}

	@Override
	protected DecimalFormat getFloatValueFormat() {
		return Settings.getDecimalFormat(ProbandListEntryTagsPDFSettingCodes.FLOAT_VALUE_PATTERN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.FLOAT_VALUE_PATTERN, Locales.PROBAND_LIST_ENTRY_TAGS_PDF);
	}

	@Override
	protected String getFloatValueFormatPattern() {
		return Settings.getString(ProbandListEntryTagsPDFSettingCodes.FLOAT_VALUE_PATTERN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.FLOAT_VALUE_PATTERN);
	}

	@Override
	protected Color getFrameColor() {
		return Settings.getColor(ProbandListEntryTagsPDFSettingCodes.FRAME_COLOR, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFDefaultSettings.FRAME_COLOR);
	}

	public float getHeight(ProbandListEntryTagsPDFBlockCursor cursor) throws Exception {
		return renderBlock(null, cursor);
	}

	@Override
	protected Float getHorizontalSelectionItemWidth() {
		return Settings.getFloatNullable(ProbandListEntryTagsPDFSettingCodes.HORIZONTAL_SELECTION_ITEM_WIDTH, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.HORIZONTAL_SELECTION_ITEM_WIDTH);
	}

	@Override
	protected String getInputFieldCommentLabel(String comment) {
		return L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.INPUT_FIELD_COMMENT, PDFUtil.DEFAULT_LABEL, comment);
	}

	// public Long getIndex() {
	// return index;
	// }

	@Override
	protected String getInputFieldNameLabel(String name, String position) {
		return L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.INPUT_FIELD_NAME, PDFUtil.DEFAULT_LABEL, name,
				position);
	}

	@Override
	protected String getInputFieldTitleLabel(String position, String title) {
		return L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.INPUT_FIELD_TITLE, PDFUtil.DEFAULT_LABEL, position,
				title);
	}

	@Override
	protected String getInputFieldTitleOptionalLabel(String position, String title) {
		return L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.INPUT_FIELD_TITLE_OPTIONAL, PDFUtil.DEFAULT_LABEL,
				position, title);
	}

	@Override
	protected String getInputFieldValidationErrorMessageLabel(String validationErrorMessage) {
		return L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.INPUT_FIELD_VALIDATION_ERROR_MESSAGE,
				PDFUtil.DEFAULT_LABEL, validationErrorMessage);
	}

	@Override
	protected DecimalFormat getIntegerValueFormat() {
		return Settings.getDecimalFormat(ProbandListEntryTagsPDFSettingCodes.INTEGER_VALUE_PATTERN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.INTEGER_VALUE_PATTERN, Locales.PROBAND_LIST_ENTRY_TAGS_PDF);
	}

	@Override
	protected String getIntegerValueFormatPattern() {
		return Settings.getString(ProbandListEntryTagsPDFSettingCodes.INTEGER_VALUE_PATTERN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.INTEGER_VALUE_PATTERN);
	}

	@Override
	protected Locales getLabelLocale() {
		return Locales.PROBAND_LIST_ENTRY_TAGS_PDF;
	}

	public ProbandListEntryOutVO getListEntry() {
		return listEntry;
	}

	@Override
	protected Long getLongValue() {
		return value.getLongValue();
	}

	@Override
	protected String  getModifiedLabel(String modifiedUser, String modifiedTimestamp) {
		return L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.MODIFIED_LABEL, PDFUtil.DEFAULT_LABEL, modifiedUser, modifiedTimestamp);
	}

	@Override
	protected Date getModifiedTimestamp() {
		return value.getModifiedTimestamp();
	}

	@Override
	protected SimpleDateFormat getModifiedTimestampFormat() {
		return Settings.getSimpleDateFormat(ProbandListEntryTagsPDFSettingCodes.MODIFIED_TIMESTAMP_PATTERN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFDefaultSettings.MODIFIED_TIMESTAMP_PATTERN, Locales.PROBAND_LIST_ENTRY_TAGS_PDF);
	}

	@Override
	protected UserOutVO getModifiedUser() {
		return value.getModifiedUser();
	}

	// public String getSection() {
	// return section;
	// }

	@Override
	protected float getMultiLineTextMinHeight() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.MULTI_LINE_TEXT_MIN_HEIGHT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.MULTI_LINE_TEXT_MIN_HEIGHT);
	}

	@Override
	protected long getPosition() {
		return value.getTag().getPosition();
	}

	@Override
	protected Color getPresetTextColor() {
		return Settings.getColor(ProbandListEntryTagsPDFSettingCodes.PRESET_TEXT_COLOR, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.PRESET_TEXT_COLOR);
	}

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
	protected boolean getRenderSketchImages() {
		return Settings.getBoolean(ProbandListEntryTagsPDFSettingCodes.RENDER_SKETCH_IMAGES, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.RENDER_SKETCH_IMAGES);
	}

	@Override
	protected String getSelectionSetValueLabel(String name, String value) {
		return L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.SELECTION_SET_VALUE_NAME, PDFUtil.DEFAULT_LABEL,
				name, value);
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getSelectionValues() {
		return value.getSelectionValues();
	}

	private String getStatusTypeName() {
		if (listEntry != null && listEntry.getLastStatus() != null) {
			return L10nUtil.getProbandListStatusTypeName(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, listEntry.getLastStatus().getStatus().getNameL10nKey());
		}
		return "";
	}

	@Override
	protected Color getTextColor() {
		return Settings.getColor(ProbandListEntryTagsPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFDefaultSettings.TEXT_COLOR);
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
		return Settings.getSimpleDateFormat(ProbandListEntryTagsPDFSettingCodes.TIMESTAMP_VALUE_PATTERN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.TIMESTAMP_VALUE_PATTERN, Locales.PROBAND_LIST_ENTRY_TAGS_PDF);
	}

	@Override
	protected String getTimestampValueFormatPattern() {
		return Settings.getString(ProbandListEntryTagsPDFSettingCodes.TIMESTAMP_VALUE_PATTERN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.TIMESTAMP_VALUE_PATTERN);
	}

	@Override
	protected Date getTimeValue() {
		return value.getTimeValue();
	}

	@Override
	protected SimpleDateFormat getTimeValueFormat() {
		return Settings.getSimpleDateFormat(ProbandListEntryTagsPDFSettingCodes.TIME_VALUE_PATTERN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.TIME_VALUE_PATTERN, Locales.PROBAND_LIST_ENTRY_TAGS_PDF);
	}

	@Override
	protected String getTimeValueFormatPattern() {
		return Settings.getString(ProbandListEntryTagsPDFSettingCodes.TIME_VALUE_PATTERN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.TIME_VALUE_PATTERN);
	}

	public BlockType getType() {
		return type;
	}

	// @Override
	// protected boolean isShowValidationErrorMessages() {
	// return Settings.getBoolean(ProbandListEntryTagsPDFSettingCodes.SHOW_VALIDATION_ERROR_MESSAGES, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
	// ProbandListEntryTagsPDFDefaultSettings.SHOW_VALIDATION_ERROR_MESSAGES);
	// }
	@Override
	protected Color getValidationErrorMessageTextColor() {
		return Settings.getColor(ProbandListEntryTagsPDFSettingCodes.VALIDATION_ERROR_MESSAGE_TEXT_COLOR, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.VALIDATION_ERROR_MESSAGE_TEXT_COLOR);
	}

	@Override
	protected float getValueFrameLineWidth() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.VALUE_FRAME_LINE_WIDTH, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.VALUE_FRAME_LINE_WIDTH);
	}

	@Override
	protected float getXFieldColumnIndent() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_FIELD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.X_FIELD_COLUMN_INDENT);
	}

	@Override
	protected float getXFrameIndent() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFDefaultSettings.X_FRAME_INDENT);
	}

	@Override
	protected float getXSelectionItemImageLabelOffset() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_SELECTION_ITEM_IMAGE_LABEL_OFFSET, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.X_SELECTION_ITEM_IMAGE_LABEL_OFFSET);
	}

	@Override
	protected float getXSelectionItemIndent() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_SELECTION_ITEM_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.X_SELECTION_ITEM_INDENT);
	}

	@Override
	protected float getXValueFrameIndent() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_VALUE_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.X_VALUE_FRAME_INDENT);
	}

	@Override
	protected float getYFrameIndent() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFDefaultSettings.Y_FRAME_INDENT);
	}

	@Override
	protected float getYSelectionItemImageLabelOffset() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_SELECTION_ITEM_IMAGE_LABEL_OFFSET, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.Y_SELECTION_ITEM_IMAGE_LABEL_OFFSET);
	}

	@Override
	protected float getYSelectionItemIndent() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_SELECTION_ITEM_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.Y_SELECTION_ITEM_INDENT);
	}

	@Override
	protected float getYValueFrameIndent() {
		return Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_VALUE_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.Y_VALUE_FRAME_INDENT);
	}

	@Override
	protected boolean isDisabled() {
		return value.getTag().getDisabled();
	}

	protected boolean isInputFieldLongTitle() {
		String title = getInputFieldTitle();
		if (title != null && title.length() > Settings.getInt(ProbandListEntryTagsPDFSettingCodes.LONG_TITLE_LENGTH, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.LONG_TITLE_LENGTH)) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean isOptional() {
		return value.getTag().getOptional();
	}

	@Override
	protected boolean isPreset() {
		return value.getId() == null;
	}

	@Override
	protected boolean isShowModifiedLabel() {
		return Settings.getBoolean(ProbandListEntryTagsPDFSettingCodes.SHOW_MODIFIED_LABEL, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.SHOW_MODIFIED_LABEL);
	}

	@Override
	protected boolean isShowPresetValues() {
		return Settings.getBoolean(ProbandListEntryTagsPDFSettingCodes.SHOW_PRESET_VALUES, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.SHOW_PRESET_VALUES);
	}

	public float renderBlock(PDPageContentStream contentStream, ProbandListEntryTagsPDFBlockCursor cursor) throws Exception {
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
						L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.PAGE_TITLE, PDFUtil.DEFAULT_LABEL,
								listEntry.getTrial().getName(),
								Long.toString(listEntry.getProband().getId()), listEntry
								.getProband()
								.getInitials(), listEntry.getProband().getName(),
								listEntry.getProband().getDateOfBirth() != null ? Settings.getSimpleDateFormat(
										ProbandListEntryTagsPDFSettingCodes.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
										Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
										ProbandListEntryTagsPDFDefaultSettings.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
										Locales.PROBAND_LIST_ENTRY_TAGS_PDF).format(listEntry.getProband().getDateOfBirth()) : "",
										listEntry.getProband().getYearOfBirth() != null ? Integer.toString(listEntry.getProband().getYearOfBirth()) : "",
												listEntry.getProband().getAge() != null ? Integer.toString(listEntry.getProband().getAge()) : ""
								),
								cursor.getBlockX(),
								Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.PAGE_TITLE_Y, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
										ProbandListEntryTagsPDFDefaultSettings.PAGE_TITLE_Y),
										Alignment.TOP_LEFT,
										cursor.getBlockWidth());
				break;
			case NEW_LIST_ENTRY:
				width = (cursor.getBlockWidth() - 2.0f * Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN_INDENT)) / 2.0f;
				x = cursor.getBlockX();
				y = cursor.getBlockY();
				if (!inserted) {
					y -= Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_HEADLINE_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
							ProbandListEntryTagsPDFDefaultSettings.Y_HEADLINE_INDENT);
					y -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.LARGE, getTextColor(),
							L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.HEADLINE, PDFUtil.DEFAULT_LABEL,
									listEntry.getTrial().getName(),
									Long.toString(listEntry.getProband().getId()), listEntry.getProband().getInitials(), listEntry.getProband().getName(),
									listEntry.getProband().getDateOfBirth() != null ? Settings.getSimpleDateFormat(
											ProbandListEntryTagsPDFSettingCodes.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
											Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
											ProbandListEntryTagsPDFDefaultSettings.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
											Locales.PROBAND_LIST_ENTRY_TAGS_PDF).format(listEntry.getProband().getDateOfBirth()) : "",
											listEntry.getProband().getYearOfBirth() != null ? Integer.toString(listEntry.getProband().getYearOfBirth()) : "",
													listEntry.getProband().getAge() != null ? Integer.toString(listEntry.getProband().getAge()) : ""
									),
									cursor.getBlockCenterX(),
									y,
									Alignment.TOP_CENTER,
									cursor.getBlockWidth());
					y -= Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_HEADLINE_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
							ProbandListEntryTagsPDFDefaultSettings.Y_HEADLINE_INDENT);
				}
				y1 = y;
				y -= getYFrameIndent();
				// height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
				// L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.TRIAL_NAME_LABEL, PDFUtil.DEFAULT_LABEL),
				// x + getXFrameIndent(),
				// y,
				// Alignment.TOP_LEFT,
				// Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				// ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN_INDENT)
				// - getXFrameIndent());
				// x += Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				// ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				// height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.MEDIUM, getTextColor(),
				// L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.TRIAL_NAME, PDFUtil.DEFAULT_LABEL,
				// trial.getName(),
				// trial.getTitle()),
				// x + getXFrameIndent(),
				// y,
				// Alignment.TOP_LEFT,
				// width - getXFrameIndent()), height1);
				// x += width;
				height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.PROBAND_NAME_LABEL, PDFUtil.DEFAULT_LABEL),
						x + getXFrameIndent(),
						y,
						Alignment.TOP_LEFT,
						Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
								ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN_INDENT)
								- getXFrameIndent());
				x += Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height1 = Math.max(
						PDFUtil.renderMultilineText(
								contentStream,
								cursor.getFontB(),
								FontSize.MEDIUM,
								getTextColor(),
								L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.PROBAND_NAME,
										PDFUtil.DEFAULT_LABEL,
										Long.toString(listEntry.getProband().getId()),
										CommonUtil.getProbandAlias(listEntry.getProband(),
												L10nUtil.getString(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, MessageCodes.NEW_BLINDED_PROBAND_NAME,
														DefaultMessages.NEW_BLINDED_PROBAND_NAME),
												L10nUtil.getString(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME)
												),
												listEntry.getProband().getInitials(), listEntry.getProband().getName()),
												x + getXFrameIndent(),
												y,
												Alignment.TOP_LEFT,
												width - getXFrameIndent()), height1);
				x += width;
				height2 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.PROBAND_DATE_OF_BIRTH_LABEL,
								PDFUtil.DEFAULT_LABEL),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
										ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN_INDENT)
										- getXFrameIndent());
				x += Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height2 = Math.max(
						PDFUtil.renderMultilineText(
								contentStream,
								cursor.getFontA(),
								FontSize.MEDIUM,
								getTextColor(),
								L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.PROBAND_DATE_OF_BIRTH,
										PDFUtil.DEFAULT_LABEL,
										listEntry.getProband().getDateOfBirth() != null ? Settings.getSimpleDateFormat(
												ProbandListEntryTagsPDFSettingCodes.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
												Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
												ProbandListEntryTagsPDFDefaultSettings.PROBAND_DATE_OF_BIRTH_DATE_PATTERN,
												Locales.PROBAND_LIST_ENTRY_TAGS_PDF).format(listEntry.getProband().getDateOfBirth()) : "",
												listEntry.getProband().getYearOfBirth() != null ? Integer.toString(listEntry.getProband().getYearOfBirth()) : "",
														listEntry.getProband().getAge() != null ? Integer.toString(listEntry.getProband().getAge()) : ""),
														x + getXFrameIndent(),
														y,
														Alignment.TOP_LEFT,
														width - getXFrameIndent()), height2);
				//

				x = cursor.getBlockX();
				y -= Math.max(height1, height2) + getYFrameIndent();
				height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.PROBAND_LIST_ENTRY_LAST_STATUS_TYPE_LABEL,
								PDFUtil.DEFAULT_LABEL),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
										ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN_INDENT)
										- getXFrameIndent());
				x += Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						blank ? L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF,
								ProbandListEntryTagsPDFLabelCodes.PROBAND_LIST_ENTRY_LAST_STATUS_TYPE_BLANK,
								PDFUtil.DEFAULT_LABEL, getStatusTypeName()) :
									L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF,
											ProbandListEntryTagsPDFLabelCodes.PROBAND_LIST_ENTRY_LAST_STATUS_TYPE,
											PDFUtil.DEFAULT_LABEL,
											getStatusTypeName()),
											x + getXFrameIndent(),
											y,
											Alignment.TOP_LEFT,
											width - getXFrameIndent()), height1);
				x += width;
				height2 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.CONTENT_TIMESTAMP_LABEL,
								PDFUtil.DEFAULT_LABEL),
								x + getXFrameIndent(),
								y,
								Alignment.TOP_LEFT,
								Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
										ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN_INDENT)
										- getXFrameIndent());
				x += Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN_INDENT);
				height2 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
						L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.CONTENT_TIMESTAMP, PDFUtil.DEFAULT_LABEL,
								Settings
								.getSimpleDateFormat(
										ProbandListEntryTagsPDFSettingCodes.CONTENT_TIMESTAMP_DATETIME_PATTERN,
										Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
										ProbandListEntryTagsPDFDefaultSettings.CONTENT_TIMESTAMP_DATETIME_PATTERN,
										Locales.PROBAND_LIST_ENTRY_TAGS_PDF).format(now)),
										x + getXFrameIndent(),
										y,
										Alignment.TOP_LEFT,
										width - getXFrameIndent()), height2);
				x = cursor.getBlockX();
				y -= Math.max(height1, height2) + getYFrameIndent();
				if (!inserted) {
					height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
							L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.TRIAL_TITLE_LABEL,
									PDFUtil.DEFAULT_LABEL),
									x + getXFrameIndent(),
									y,
									Alignment.TOP_LEFT,
									Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
											ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN1_INDENT)
											- getXFrameIndent());
					x += Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
							ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN1_INDENT);
					height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontC(), FontSize.MEDIUM, getTextColor(),
							listEntry.getTrial().getTitle(),
							x + getXFrameIndent(),
							y,
							Alignment.TOP_LEFT,
							cursor.getBlockWidth() - x - 2.0f * getXFrameIndent()), height1);
					if (!CommonUtil.isEmptyString(listEntry.getTrial().getDescription())) {
						x = cursor.getBlockX();
						y -= height1 + getYFrameIndent();
						height1 = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.MEDIUM, getTextColor(),
								L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFLabelCodes.TRIAL_DESCRIPTION_LABEL,
										PDFUtil.DEFAULT_LABEL),
										x + getXFrameIndent(),
										y,
										Alignment.TOP_LEFT,
										Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
												ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN1_INDENT)
												- getXFrameIndent());
						x += Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_HEAD_COLUMN1_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
								ProbandListEntryTagsPDFDefaultSettings.X_HEAD_COLUMN1_INDENT);
						height1 = Math.max(PDFUtil.renderMultilineText(contentStream, cursor.getFontC(), FontSize.MEDIUM, getTextColor(),
								listEntry.getTrial().getDescription(),
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
						// + (cursor.hasSection() ? Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.X_BOX_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						// ProbandListEntryTagsPDFDefaultSettings.X_BOX_FRAME_INDENT) : 0.0f),
						y1,
						cursor.getBlockWidth(), // .getIndexWidth(),
						y1 - y,
						// - Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						// ProbandListEntryTagsPDFDefaultSettings.Y_BOX_FRAME_INDENT),
						Alignment.TOP_LEFT,
						Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.HEAD_FRAME_LINE_WIDTH, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
								ProbandListEntryTagsPDFDefaultSettings.HEAD_FRAME_LINE_WIDTH));
				y -= Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				height = cursor.getBlockY() - y;

				break;
				// case NEW_CATEGORY:
				// height = 0.0f;
				// if (cursor.hasCategory()) {
				// if (!inserted) {
				// height += Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				// ProbandListEntryTagsPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				// height += PDFUtil.renderTextLine(
				// contentStream,
				// cursor.getFontB(),
				// FontSize.BIG,
				// getTextColor(),
				// cursor.getCategoryLabel(),
				// cursor.getBlockX(),
				// cursor.getBlockY()
				// - Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				// ProbandListEntryTagsPDFDefaultSettings.Y_BOX_FRAME_INDENT),
				// Alignment.TOP_LEFT);
				// }
				// height += Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				// ProbandListEntryTagsPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				// }
				// break;
				//
				// case END_OF_CATEGORY:
				// height = 0.0f;
				// if (cursor.hasCategory()) {
				// // PDFUtil.renderFrame(contentStream,
				// // getFrameColor(),
				// // cursor.getBlockX(),
				// // cursor.getSectionY(),
				// // cursor.getSectionWidth(),
				// // cursor.getSectionY() - cursor.getBlockY(),
				// // // + Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				// ProbandListEntryTagsPDFDefaultSettings.Y_BOX_FRAME_INDENT),
				// // Alignment.TOP_LEFT,
				// // Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.SECTION_FRAME_LINE_WIDTH, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				// ProbandListEntryTagsPDFDefaultSettings.SECTION_FRAME_LINE_WIDTH));
				// height += Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				// ProbandListEntryTagsPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				// }
				// break;
			case INPUT_FIELD:
				height = renderInputFieldBlock(contentStream, cursor) +
				Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.Y_BOX_FRAME_INDENT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.Y_BOX_FRAME_INDENT);
				break;
			default:
				height = 0.0f;
				break;
		}
		return height;
	}
}
