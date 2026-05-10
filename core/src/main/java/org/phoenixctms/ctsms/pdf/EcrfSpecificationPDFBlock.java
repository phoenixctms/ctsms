package org.phoenixctms.ctsms.pdf;

import java.util.Date;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.phoenixctms.ctsms.pdf.PDFUtil.Alignment;
import org.phoenixctms.ctsms.pdf.PDFUtil.FontSize;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;

public class EcrfSpecificationPDFBlock {

	public enum BlockType {
		PAGE_TITLE,
		SPACER,
		HEAD,
		TRIAL_TITLE,
		TRIAL_DESCRIPTION,
		GENERATED_ON,
		GENERATED_BY,
		ECRF_HEAD,
		ECRF_TITLE,
		ECRF_DESCRIPTION,
		ECRF_REVISION,
		ECRF_VISITS,
		ECRF_GROUPS,
		SPECIFICATION_TABLE_HEAD,
		SPECIFICATION_TABLE_ROW
	}

	protected TrialOutVO trial;
	protected ECRFOutVO ecrf;
	protected Date now;
	protected ECRFFieldOutVO ecrfField;
	protected BlockType type;

	public EcrfSpecificationPDFBlock() {
		type = BlockType.SPACER;
	}

	public EcrfSpecificationPDFBlock(BlockType type) {
		this.type = type;
	}

	public EcrfSpecificationPDFBlock(ECRFFieldOutVO ecrfField) {
		this.ecrfField = ecrfField;
		this.type = BlockType.SPECIFICATION_TABLE_ROW;
	}

	public EcrfSpecificationPDFBlock(Date now) {
		this.now = now;
		this.type = BlockType.GENERATED_ON;
	}

	public EcrfSpecificationPDFBlock(TrialOutVO trial, BlockType type) {
		this.trial = trial;
		this.type = type;
	}

	public EcrfSpecificationPDFBlock(ECRFOutVO ecrf, BlockType type) {
		this.ecrf = ecrf;
		this.type = type;
	}

	public float getHeight(EcrfSpecificationPDFBlockCursor cursor) throws Exception {
		return renderBlock(null, cursor);
	}

	protected String getTrialTitle() {
		if (trial != null) {
			return trial.getTitle();
		}
		return "";
	}

	protected String getTrialName() {
		if (trial != null) {
			return trial.getName();
		}
		return "";
	}

	protected String getEcrfTitle() {
		if (ecrf != null) {
			return ecrf.getTitle();
		}
		return "";
	}

	protected String getEcrfDescription() {
		if (ecrf != null) {
			return ecrf.getDescription();
		}
		return "";
	}

	protected String getEcrfRevision() {
		if (ecrf != null) {
			return ecrf.getRevision();
		}
		return "";
	}

	protected String getEcrfFieldSection() {
		if (ecrfField != null) {
			return ecrfField.getSection();
		}
		return "";
	}

	protected String getEcrfFieldTitle() {
		if (ecrfField != null) {
			if (CommonUtil.isEmptyString(ecrfField.getTitle())) {
				return ecrfField.getField().getTitle();
			}
			return ecrfField.getTitle();
		}
		return "";
	}

	protected String getEcrfFieldType() {
		if (ecrfField != null) {
			return ecrfField.getField().getFieldType().getName();
		}
		return "";
	}

	protected String getSelectionSetValues() {
		if (ecrfField != null) {
			StringBuilder selectionSetValues = new StringBuilder();
			if (ecrfField.getField().getSelectionSetValues().size() > 0) {
				Iterator<InputFieldSelectionSetValueOutVO> it = ecrfField.getField().getSelectionSetValues().iterator();
				while (it.hasNext()) {
					InputFieldSelectionSetValueOutVO selectionSetValue = it.next();
					selectionSetValues.append(L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.SELECTION_SET_VALUE,
							PDFUtil.DEFAULT_LABEL, selectionSetValue.getName(), selectionSetValue.getValue()));
					if (it.hasNext()) {
						selectionSetValues.append(PDFUtil.PDF_LINE_BREAK);
					}
				}
			}
			return selectionSetValues.toString();
		}
		return "";
	}

	protected String getGroupTitle(ProbandGroupOutVO group) {
		if (group != null) {
			return group.getTitle();
		}
		return "";
	}

	protected String getVisitTitle(VisitOutVO visit) {
		if (visit != null) {
			return visit.getTitle();
		}
		return "";
	}

	protected String getTrialDescription() {
		if (trial != null) {
			return trial.getDescription();
		}
		return "";
	}

	protected String getPageTitleName() {
		if (ecrf != null) {
			return ecrf.getUniqueName();
		}
		return getTrialName();
	}

	protected String getGeneratedByName(UserOutVO user) {
		if (user != null) {
			if (user.getIdentity() != null) {
				return user.getIdentity().getNameWithTitles();
			} else {
				return user.getName();
			}
		}
		return "";
	}

	public BlockType getType() {
		return type;
	}

	public float renderBlock(PDPageContentStream contentStream, EcrfSpecificationPDFBlockCursor cursor) throws Exception {
		String line;
		float x;
		float y;
		float y1;
		float y2;
		float y3;
		float y4;
		float height;
		switch (type) {
			case PAGE_TITLE:
				height = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.BIG,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.PAGE_TITLE, PDFUtil.DEFAULT_LABEL, getPageTitleName()),
						cursor.getBlockX()
								+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
										EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT),
						cursor.getPageHeight() - Settings.getFloat(EcrfSpecificationPDFSettingCodes.PAGE_UPPER_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.PAGE_UPPER_MARGIN),
						Alignment.TOP_LEFT,
						cursor.getBlockWidth());
				break;
			case HEAD:
				line = L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.HEADLINE, PDFUtil.DEFAULT_LABEL, getTrialName());
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), FontSize.LARGE,
							Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
							line,
							cursor.getBlockCenterX(),
							y,
							Alignment.TOP_CENTER,
							cursor.getBlockWidth());
				}
				height = cursor.getBlockY() - y;
				break;
			case TRIAL_TITLE:
				y1 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.TRIAL_TITLE, PDFUtil.DEFAULT_LABEL), x,
						y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontB(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						getTrialTitle(),
						x,
						y2,
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth()
								- Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
										EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
												EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT));
				y2 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case TRIAL_DESCRIPTION:
				y1 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.TRIAL_DESCRIPTION, PDFUtil.DEFAULT_LABEL), x,
						y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						getTrialDescription(),
						x,
						y2,
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth()
								- Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
										EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
												EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT));
				y2 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case GENERATED_ON:
				y1 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR,
						Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.GENERATED_ON, PDFUtil.DEFAULT_LABEL), x, y1,
						PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(
						contentStream,
						cursor.getFontB(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						now == null ? ""
								: Settings.getSimpleDateFormat(EcrfSpecificationPDFSettingCodes.NOW_DATE_PATTERN, Bundle.ECRF_SPECIFICATION_PDF,
										EcrfSpecificationPDFDefaultSettings.NOW_DATE_PATTERN, Locales.ECRF_SPECIFICATION_PDF,
										Settings.getBoolean(EcrfSpecificationPDFSettingCodes.DATE_USER_TIME_ZONE, Bundle.SETTINGS,
												EcrfSpecificationPDFDefaultSettings.DATE_USER_TIME_ZONE))
										.format(now),
						x, y2,
						PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case GENERATED_BY:
				y1 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR,
						Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.GENERATED_BY, PDFUtil.DEFAULT_LABEL), x, y1,
						PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(
						contentStream,
						cursor.getFontB(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						getGeneratedByName(cursor.getUser()),
						x, y2,
						PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case ECRF_HEAD:
				line = L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.ECRF_HEADLINE, PDFUtil.DEFAULT_LABEL, ecrf.getName());
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), FontSize.LARGE,
							Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
							line,
							cursor.getBlockX(),
							y,
							Alignment.TOP_LEFT,
							cursor.getBlockWidth());
				}
				height = cursor.getBlockY() - y;
				break;
			case ECRF_TITLE:
				y1 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.ECRF_TITLE, PDFUtil.DEFAULT_LABEL), x,
						y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontB(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						getEcrfTitle(),
						x,
						y2,
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth()
								- Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
										EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
												EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT));
				y2 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case ECRF_DESCRIPTION:
				y1 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.ECRF_DESCRIPTION, PDFUtil.DEFAULT_LABEL), x,
						y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						getEcrfDescription(),
						x,
						y2,
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth()
								- Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
										EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
												EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT));
				y2 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case ECRF_REVISION:
				y1 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.ECRF_REVISION, PDFUtil.DEFAULT_LABEL),
						x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						getEcrfRevision(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case ECRF_VISITS:
				y1 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.ECRF_VISITS, PDFUtil.DEFAULT_LABEL),
						x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				if (ecrf.getVisits().size() > 0) {
					x = cursor.getBlockX()
							+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
									EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
							+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
									EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
					Iterator<VisitOutVO> it = ecrf.getVisits().iterator();
					while (it.hasNext()) {
						y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontC(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
								EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
								getVisitTitle(it.next()), x, y2, PDFUtil.Alignment.TOP_LEFT);
					}
				}
				y2 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case ECRF_GROUPS:
				y1 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.ECRF_GROUPS, PDFUtil.DEFAULT_LABEL),
						x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				if (ecrf.getGroups().size() > 0) {
					x = cursor.getBlockX()
							+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_COLUMN_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
									EcrfSpecificationPDFDefaultSettings.X_COLUMN_INDENT)
							+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
									EcrfSpecificationPDFDefaultSettings.X_FRAME_INDENT);
					Iterator<ProbandGroupOutVO> it = ecrf.getGroups().iterator();
					while (it.hasNext()) {
						y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontC(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
								EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
								getGroupTitle(it.next()), x, y2, PDFUtil.Alignment.TOP_LEFT);
					}
				}
				y2 -= Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case SPECIFICATION_TABLE_HEAD:
				y1 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_SPECIFICATION_TABLE_HEAD_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_SPECIFICATION_TABLE_HEAD_FRAME_INDENT);
				y2 = y1;
				y3 = y1;
				y4 = y1;
				x = cursor.getBlockX() + Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_SPECIFICATION_TABLE_HEAD_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.X_SPECIFICATION_TABLE_HEAD_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
						EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFLabelCodes.SECTION_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
						x, y1, PDFUtil.Alignment.TOP_LEFT);
				x += Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_SECTION_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_SECTION_WIDTH);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
						EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFLabelCodes.FIELD_TITLE_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
						x, y2, PDFUtil.Alignment.TOP_LEFT);
				x += Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_FIELD_TITLE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_FIELD_TITLE_WIDTH);
				y3 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
						EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFLabelCodes.FIELD_TYPE_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
						x, y3, PDFUtil.Alignment.TOP_LEFT);
				x += Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_FIELD_TYPE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_FIELD_TYPE_WIDTH);
				y4 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
						EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_TEXT_COLOR),
						L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFLabelCodes.SELECTION_VALUES_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
						x, y4, PDFUtil.Alignment.TOP_LEFT);
				height = cursor.getBlockY() - Math.min(Math.min(y1, y2), Math.min(y3, y4))
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_SPECIFICATION_TABLE_HEAD_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_SPECIFICATION_TABLE_HEAD_FRAME_INDENT);
				PDFUtil.renderFrame(contentStream, Settings.getColor(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_COLOR), cursor.getBlockX(), cursor.getBlockY(), cursor.getBlockWidth(), height,
						PDFUtil.Alignment.TOP_LEFT, Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_LINE_WIDTH,
								Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_LINE_WIDTH));
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_SECTION_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_SECTION_WIDTH);
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_COLOR),
						x,
						cursor.getBlockY(),
						x,
						cursor.getBlockY() - height, Settings.getFloat(
								EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_LINE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_LINE_WIDTH));
				x += Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_FIELD_TITLE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_FIELD_TITLE_WIDTH);
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_COLOR),
						x,
						cursor.getBlockY(),
						x,
						cursor.getBlockY() - height, Settings.getFloat(
								EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_LINE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_LINE_WIDTH));
				x += Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_FIELD_TYPE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_FIELD_TYPE_WIDTH);
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_COLOR),
						x,
						cursor.getBlockY(),
						x,
						cursor.getBlockY() - height, Settings.getFloat(
								EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_LINE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_LINE_WIDTH));
				break;
			case SPECIFICATION_TABLE_ROW:
				y1 = cursor.getBlockY()
						- Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_SPECIFICATION_TABLE_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_SPECIFICATION_TABLE_FRAME_INDENT);
				y2 = y1;
				y3 = y1;
				y4 = y1;
				x = cursor.getBlockX() + Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_SPECIFICATION_TABLE_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.X_SPECIFICATION_TABLE_FRAME_INDENT);
				y1 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
						EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_TEXT_COLOR), getEcrfFieldSection(), x, y1, PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(
								EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_SECTION_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_SECTION_WIDTH)
								- 2.0f * Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_SPECIFICATION_TABLE_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
										EcrfSpecificationPDFDefaultSettings.X_SPECIFICATION_TABLE_FRAME_INDENT));
				x += Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_SECTION_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_SECTION_WIDTH);
				y2 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
						EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_TEXT_COLOR), getEcrfFieldTitle(), x, y2, PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(
								EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_FIELD_TITLE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_FIELD_TITLE_WIDTH)
								- 2.0f * Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_SPECIFICATION_TABLE_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
										EcrfSpecificationPDFDefaultSettings.X_SPECIFICATION_TABLE_FRAME_INDENT));
				x += Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_FIELD_TITLE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_FIELD_TITLE_WIDTH);
				y3 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
						EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_TEXT_COLOR),
						getEcrfFieldType(),
						x, y3, PDFUtil.Alignment.TOP_LEFT);
				x += Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_FIELD_TYPE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_FIELD_TYPE_WIDTH);
				y4 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontC(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
						EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_TEXT_COLOR), getSelectionSetValues(), x, y4, PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockX() + cursor.getBlockWidth() - x
								- Settings.getFloat(EcrfSpecificationPDFSettingCodes.X_SPECIFICATION_TABLE_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
										EcrfSpecificationPDFDefaultSettings.X_SPECIFICATION_TABLE_FRAME_INDENT));
				height = cursor.getBlockY() - Math.min(Math.min(y1, y2), Math.min(y3, y4))
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.Y_SPECIFICATION_TABLE_FRAME_INDENT, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.Y_SPECIFICATION_TABLE_FRAME_INDENT);
				PDFUtil.renderFrame(contentStream, Settings.getColor(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_COLOR), cursor.getBlockX(), cursor.getBlockY(), cursor.getBlockWidth(), height,
						PDFUtil.Alignment.TOP_LEFT, Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_LINE_WIDTH,
								Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_LINE_WIDTH));
				x = cursor.getBlockX()
						+ Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_SECTION_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_SECTION_WIDTH);
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_COLOR),
						x,
						cursor.getBlockY(),
						x,
						cursor.getBlockY() - height, Settings.getFloat(
								EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_LINE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_LINE_WIDTH));
				x += Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_FIELD_TITLE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_FIELD_TITLE_WIDTH);
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_COLOR),
						x,
						cursor.getBlockY(),
						x,
						cursor.getBlockY() - height, Settings.getFloat(
								EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_LINE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_LINE_WIDTH));
				x += Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_COLUMN_FIELD_TYPE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_COLUMN_FIELD_TYPE_WIDTH);
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_COLOR, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_COLOR),
						x,
						cursor.getBlockY(),
						x,
						cursor.getBlockY() - height, Settings.getFloat(
								EcrfSpecificationPDFSettingCodes.SPECIFICATION_TABLE_FRAME_LINE_WIDTH, Bundle.ECRF_SPECIFICATION_PDF,
								EcrfSpecificationPDFDefaultSettings.SPECIFICATION_TABLE_FRAME_LINE_WIDTH));
				break;
			case SPACER:
				height = Settings.getFloat(EcrfSpecificationPDFSettingCodes.SPACER_HEIGHT, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.SPACER_HEIGHT);
				break;
			default:
				height = 0.0f;
				break;
		}
		return height;
	}

	public ECRFOutVO getEcrf() {
		return ecrf;
	}
}