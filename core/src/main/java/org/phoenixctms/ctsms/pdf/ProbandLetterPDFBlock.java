package org.phoenixctms.ctsms.pdf;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;

public class ProbandLetterPDFBlock {

	public enum BlockType {
		NEW_LETTER, NEW_PAGE, ADDRESS, PROBAND_ID, FIRST_PAGE_DATE, SECOND_PAGE_DATE, SALUTATION
	}

	private ProbandAddressOutVO address;
	private Date now;
	private BlockType type;

	public ProbandLetterPDFBlock(BlockType type) {
		this.type = type;
	}

	public ProbandLetterPDFBlock(Date now, BlockType type) {
		this.now = now;
		this.type = type;
	}

	public ProbandLetterPDFBlock(ProbandAddressOutVO address, BlockType type) {
		this.address = address;
		this.type = type;
	}

	public ProbandAddressOutVO getAddress() {
		return address;
	}

	private String getBodyGenderSpecificSalutation() {
		if (address != null) {
			return CommonUtil.getGenderSpecificSalutation(address.getProband(),
					L10nUtil.getReimbursementsPDFLabel(Locales.PROBAND_LETTER_PDF, ReimbursementsPDFLabelCodes.BODY_MALE_SALUTATION, PDFUtil.DEFAULT_LABEL),
					L10nUtil.getReimbursementsPDFLabel(Locales.PROBAND_LETTER_PDF, ReimbursementsPDFLabelCodes.BODY_FEMALE_SALUTATION, PDFUtil.DEFAULT_LABEL));
		}
		return "";
	}

	private String getCareOf() {
		if (address != null) {
			if (address.isDecrypted() && !CommonUtil.isEmptyString(address.getCareOf())) {
				return address.getCareOf();
			}
		}
		return "";
	}

	private String getCountryName() {
		if (address != null) {
			if (address.isDecrypted()) {
				return address.getCountryName();
			}
		}
		return "";
	}

	private String getGenderSpecificSalutation() {
		if (address != null) {
			return CommonUtil.getGenderSpecificSalutation(address.getProband(),
					L10nUtil.getProbandLetterPDFLabel(Locales.PROBAND_LETTER_PDF, ProbandLetterPDFLabelCodes.MALE_SALUTATION, PDFUtil.DEFAULT_LABEL),
					L10nUtil.getProbandLetterPDFLabel(Locales.PROBAND_LETTER_PDF, ProbandLetterPDFLabelCodes.FEMALE_SALUTATION, PDFUtil.DEFAULT_LABEL));
		}
		return "";
	}

	public float getHeight(ProbandLetterPDFBlockCursor cursor) throws Exception {
		return renderBlock(null, cursor);
	}

	private String getProbandId() {
		if (address != null) {
			return Long.toString(address.getProband().getId());
		}
		return "";
	}

	private String getProbandName(boolean withTitles, boolean withFirstName) {
		if (address != null) {
			return CommonUtil.getProbandName(address.getProband(), withTitles, withFirstName,
					L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_NAME, DefaultMessages.ENCRYPTED_PROBAND_NAME),
					L10nUtil.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME, DefaultMessages.NEW_BLINDED_PROBAND_NAME),
					L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME));
		}
		return "";
	}

	private String getStreetNameHouseNumberEntranceDoornumber() {
		StringBuilder sb = new StringBuilder();
		if (address != null) {
			if (address.isDecrypted()) {
				StringBuilder hed = new StringBuilder();
				CommonUtil.appendString(sb, address.getStreetName(), null, "?");
				CommonUtil.appendString(hed, address.getHouseNumber(), null, "?");
				CommonUtil.appendString(hed, address.getEntrance(), "/");
				CommonUtil.appendString(hed, address.getDoorNumber(), "/");
				CommonUtil.appendString(sb, hed.toString(), " ");
			} else {
				sb.append(L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_ADDRESS, DefaultMessages.ENCRYPTED_PROBAND_ADDRESS));
			}
		}
		return sb.toString();
	}

	public BlockType getType() {
		return type;
	}

	private String getZipCodeCityName() {
		if (address != null) {
			if (address.isDecrypted()) {
				StringBuilder sb = new StringBuilder();
				CommonUtil.appendString(sb, address.getZipCode(), null, "?");
				CommonUtil.appendString(sb, address.getCityName(), " ", "?");
				return sb.toString();
			}
		}
		return "";
	}

	public float renderBlock(PDPageContentStream contentStream, ProbandLetterPDFBlockCursor cursor) throws Exception {
		float x;
		float y;
		String line;
		float height;
		switch (type) {
			case ADDRESS:
				String line1 = L10nUtil.getProbandLetterPDFLabel(Locales.PROBAND_LETTER_PDF, ProbandLetterPDFLabelCodes.ADDRESS_LINE1, PDFUtil.DEFAULT_LABEL);
				String line2 = L10nUtil.getProbandLetterPDFLabel(Locales.PROBAND_LETTER_PDF, ProbandLetterPDFLabelCodes.ADDRESS_LINE2, PDFUtil.DEFAULT_LABEL,
						getGenderSpecificSalutation(), getProbandName(true, true));
				String line3 = L10nUtil.getProbandLetterPDFLabel(Locales.PROBAND_LETTER_PDF, ProbandLetterPDFLabelCodes.ADDRESS_LINE6, PDFUtil.DEFAULT_LABEL, getCareOf());
				String line4 = L10nUtil.getProbandLetterPDFLabel(Locales.PROBAND_LETTER_PDF, ProbandLetterPDFLabelCodes.ADDRESS_LINE3, PDFUtil.DEFAULT_LABEL,
						getStreetNameHouseNumberEntranceDoornumber());
				String line5 = L10nUtil.getProbandLetterPDFLabel(Locales.PROBAND_LETTER_PDF, ProbandLetterPDFLabelCodes.ADDRESS_LINE4, PDFUtil.DEFAULT_LABEL, getZipCodeCityName(),
						getCountryName());
				String line6 = "";
				if (!getCountryName().equals(Settings.getString(SettingCodes.INLAND, Bundle.SETTINGS, DefaultSettings.INLAND))) {
					line6 = L10nUtil.getProbandLetterPDFLabel(Locales.PROBAND_LETTER_PDF, ProbandLetterPDFLabelCodes.ADDRESS_LINE5, PDFUtil.DEFAULT_LABEL, getCountryName()
							.toUpperCase());
				}
				x = cursor.getBlockX();
				y = Settings.getFloat(ProbandLetterPDFSettingCodes.ADDRESS_Y, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.ADDRESS_Y);
				if (!CommonUtil.isEmptyString(line1)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ProbandLetterPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.TEXT_COLOR), line1, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				if (!CommonUtil.isEmptyString(line2)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ProbandLetterPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.TEXT_COLOR), line2, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				if (!CommonUtil.isEmptyString(line3)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ProbandLetterPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.TEXT_COLOR), line3, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				if (!CommonUtil.isEmptyString(line4)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ProbandLetterPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.TEXT_COLOR), line4, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				if (!CommonUtil.isEmptyString(line5)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ProbandLetterPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.TEXT_COLOR), line5, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				if (!CommonUtil.isEmptyString(line6)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ProbandLetterPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.TEXT_COLOR), line6, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				height = cursor.getBlockY() - y;
				break;
			case FIRST_PAGE_DATE:
				line = L10nUtil.getProbandLetterPDFLabel(
						Locales.PROBAND_LETTER_PDF,
						ProbandLetterPDFLabelCodes.DATE,
						PDFUtil.DEFAULT_LABEL,
						now == null ? null
								: Settings.getSimpleDateFormat(ProbandLetterPDFSettingCodes.DATE_PATTERN, Bundle.PROBAND_LETTER_PDF,
										ProbandLetterPDFDefaultSettings.DATE_PATTERN, Locales.PROBAND_LETTER_PDF).format(now));
				x = cursor.getBlockX() + cursor.getBlockWidth();
				y = Settings.getFloat(ProbandLetterPDFSettingCodes.FIRST_PAGE_DATE_Y, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.FIRST_PAGE_DATE_Y);
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE11,
							Settings.getColor(ProbandLetterPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.TEXT_COLOR), line, x, y,
							PDFUtil.Alignment.TOP_RIGHT);
				}
				height = cursor.getBlockY() - y;
				break;
			case PROBAND_ID:
				line = L10nUtil.getProbandLetterPDFLabel(Locales.PROBAND_LETTER_PDF, ProbandLetterPDFLabelCodes.PROBAND_ID, PDFUtil.DEFAULT_LABEL, getProbandId());
				x = cursor.getBlockX() + cursor.getBlockWidth();
				y = Settings.getFloat(ProbandLetterPDFSettingCodes.PROBAND_ID_Y, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.PROBAND_ID_Y);
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.TINY,
							Settings.getColor(ProbandLetterPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.TEXT_COLOR), line, x, y,
							PDFUtil.Alignment.TOP_RIGHT);
				}
				height = cursor.getBlockY() - y;
				break;
			case SECOND_PAGE_DATE:
				line = L10nUtil.getProbandLetterPDFLabel(
						Locales.PROBAND_LETTER_PDF,
						ProbandLetterPDFLabelCodes.DATE,
						PDFUtil.DEFAULT_LABEL,
						now == null ? null
								: Settings.getSimpleDateFormat(ProbandLetterPDFSettingCodes.DATE_PATTERN, Bundle.PROBAND_LETTER_PDF,
										ProbandLetterPDFDefaultSettings.DATE_PATTERN, Locales.PROBAND_LETTER_PDF).format(now));
				x = cursor.getBlockX() + cursor.getBlockWidth();
				y = Settings.getFloat(ProbandLetterPDFSettingCodes.SECOND_PAGE_DATE_Y, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.SECOND_PAGE_DATE_Y);
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE11,
							Settings.getColor(ProbandLetterPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.TEXT_COLOR), line, x, y,
							PDFUtil.Alignment.TOP_RIGHT);
				}
				height = cursor.getBlockY() - y;
				break;
			case SALUTATION:
				line = MessageFormat.format(getBodyGenderSpecificSalutation(), getGenderSpecificSalutation(), getProbandName(true, false));
				x = cursor.getBlockX();
				y = Settings.getFloat(ProbandLetterPDFSettingCodes.BODY_SALUTATION_Y, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BODY_SALUTATION_Y);
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE11,
							Settings.getColor(ProbandLetterPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.TEXT_COLOR), line, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				height = cursor.getBlockY() - y;
				break;
			default:
				height = 0.0f;
				break;
		}
		return height;
	}
}
