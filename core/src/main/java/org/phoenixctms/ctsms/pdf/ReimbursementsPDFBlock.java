package org.phoenixctms.ctsms.pdf;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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
import org.phoenixctms.ctsms.vo.BankAccountOutVO;
import org.phoenixctms.ctsms.vo.MoneyTransferByBankAccountSummaryDetailVO;
import org.phoenixctms.ctsms.vo.MoneyTransferByCostTypeSummaryDetailVO;
import org.phoenixctms.ctsms.vo.MoneyTransferByPaymentMethodSummaryDetailVO;
import org.phoenixctms.ctsms.vo.MoneyTransferSummaryVO;
import org.phoenixctms.ctsms.vo.PaymentMethodVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialTagVO;
import org.phoenixctms.ctsms.vo.TrialTagValueOutVO;

public class ReimbursementsPDFBlock {

	public enum BlockType {
		NEW_REIMBURSEMENT,
		NEW_PAGE,
		RECIPIENT_ADDRESS,
		SENDER_ADDRESS,
		FIRST_PAGE_DATE,
		FIRST_PAGE_SUBJECT,
		SALUTATION,
		TRIAL_TITLE,
		RETURN_ADDRESS,
		SECOND_PAGE_DATE,
		SECOND_PAGE_SUBJECT,
		TRIAL_TAGS,
		MONEY_TRANSFER_TABLE_HEAD,
		BANK_ACCOUNT_TABLE_ROW,
		PAYMENT_METHOD_TABLE_ROW,
		// COMPLETE_INSTRUCTION,
		SIGNATURE_TOTAL,
		SPACER,
	}

	private static String getAmount(Float amount) {
		if (amount != null) {
			return L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.AMOUNT_LABEL, PDFUtil.DEFAULT_LABEL, amount,
					Settings.getString(SettingCodes.CURRENCY_SYMBOL, Bundle.SETTINGS, DefaultSettings.CURRENCY_SYMBOL));
		}
		return "";
	}

	private static String getTrialTagName(TrialTagVO tag) {
		return L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.TRIAL_TAG_NAME_LABEL, PDFUtil.DEFAULT_LABEL,
				L10nUtil.getTrialTagName(Locales.REIMBURSEMENTS_PDF, tag.getNameL10nKey()));
	}

	private MoneyTransferByBankAccountSummaryDetailVO byBankAccountSummaryDetail;
	private MoneyTransferByPaymentMethodSummaryDetailVO byPaymentMethodSummaryDetail;
	private MoneyTransferSummaryVO summary;
	private ProbandAddressOutVO address;
	private ProbandOutVO proband;
	private TrialOutVO trial;
	private Collection<TrialTagValueOutVO> trialTagValues;
	private Date now;
	private BlockType type;
	private boolean firstTableRow;
	private boolean lastTableRow;
	private boolean newPage = false;

	public ReimbursementsPDFBlock() {
		type = BlockType.SPACER;
	}

	public ReimbursementsPDFBlock(BlockType type) {
		this.type = type;
	}

	public ReimbursementsPDFBlock(Collection<TrialTagValueOutVO> trialTagValues) {
		this.trialTagValues = trialTagValues;
		this.type = BlockType.TRIAL_TAGS;
	}

	public ReimbursementsPDFBlock(Date now, BlockType type) {
		this.now = now;
		this.type = type;
	}

	public ReimbursementsPDFBlock(MoneyTransferByBankAccountSummaryDetailVO byBankAccountSummaryDetail, boolean first, boolean last) {
		this.byBankAccountSummaryDetail = byBankAccountSummaryDetail;
		this.type = BlockType.BANK_ACCOUNT_TABLE_ROW;
		this.firstTableRow = first;
		this.lastTableRow = last;
	}

	public ReimbursementsPDFBlock(MoneyTransferByPaymentMethodSummaryDetailVO byPaymentMethodSummaryDetail, boolean first, boolean last) {
		this.byPaymentMethodSummaryDetail = byPaymentMethodSummaryDetail;
		this.type = BlockType.PAYMENT_METHOD_TABLE_ROW;
		this.firstTableRow = first;
		this.lastTableRow = last;
	}

	public ReimbursementsPDFBlock(ProbandAddressOutVO address, ProbandOutVO proband, BlockType type) {
		this.address = address;
		this.proband = proband;
		this.type = type;
	}

	public ReimbursementsPDFBlock(ProbandOutVO proband, BlockType type) {
		this.proband = proband;
		this.type = type;
	}

	public ReimbursementsPDFBlock(ProbandOutVO proband, MoneyTransferSummaryVO summary) {
		this.proband = proband;
		this.summary = summary;
		this.type = BlockType.SIGNATURE_TOTAL;
	}

	public ReimbursementsPDFBlock(TrialOutVO trial, BlockType type) {
		this.trial = trial;
		this.type = type;
	}

	public ReimbursementsPDFBlock(TrialOutVO trial, ProbandOutVO proband, BlockType type) {
		this.proband = proband;
		this.trial = trial;
		this.type = type;
	}

	// public ReimbursementsPDFBlock(TrialOutVO trial, ProbandOutVO proband) {
	// this.trial = trial;
	// this.proband = proband;
	// this.type = BlockType.SECOND_PAGE_SUBJECT;
	// }
	private String getBankAccount() {
		StringBuilder sb = new StringBuilder();
		if (byBankAccountSummaryDetail != null) {
			BankAccountOutVO bankAccount = byBankAccountSummaryDetail.getBankAccount();
			if (bankAccount.isDecrypted()) {
				if (bankAccount.getNa()) {
					sb.append(L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.ACCOUNT_HOLDER_NAME_NA_LABEL, PDFUtil.DEFAULT_LABEL));
					sb.append(PDFUtil.PDF_LINE_BREAK);
					sb.append(L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.BANK_NAME_NA_LABEL, PDFUtil.DEFAULT_LABEL));
					// if (!CommonUtil.isEmptyString(bankAccount.getBic())) {
					sb.append(PDFUtil.PDF_LINE_BREAK);
					sb.append(L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.BIC_NA_LABEL, PDFUtil.DEFAULT_LABEL));
					// }
					// if (!CommonUtil.isEmptyString(bankAccount.getIban())) {
					sb.append(PDFUtil.PDF_LINE_BREAK);
					sb.append(L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.IBAN_NA_LABEL, PDFUtil.DEFAULT_LABEL));
					// }
				} else {
					sb.append(L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.ACCOUNT_HOLDER_NAME_LABEL, PDFUtil.DEFAULT_LABEL,
							bankAccount.getAccountHolderName()));
					sb.append(PDFUtil.PDF_LINE_BREAK);
					sb.append(L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.BANK_NAME_LABEL, PDFUtil.DEFAULT_LABEL,
							bankAccount.getBankName()));
					if (!CommonUtil.isEmptyString(bankAccount.getBic())) {
						sb.append(PDFUtil.PDF_LINE_BREAK);
						sb.append(
								L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.BIC_LABEL, PDFUtil.DEFAULT_LABEL, bankAccount.getBic()));
					}
					if (!CommonUtil.isEmptyString(bankAccount.getIban())) {
						sb.append(PDFUtil.PDF_LINE_BREAK);
						sb.append(L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.IBAN_LABEL, PDFUtil.DEFAULT_LABEL,
								bankAccount.getIban()));
					}
					if (!CommonUtil.isEmptyString(bankAccount.getBankCodeNumber())) {
						sb.append(PDFUtil.PDF_LINE_BREAK);
						sb.append(L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.BANK_CODE_NUMBER_LABEL, PDFUtil.DEFAULT_LABEL,
								bankAccount.getBankCodeNumber()));
					}
					if (!CommonUtil.isEmptyString(bankAccount.getAccountNumber())) {
						sb.append(PDFUtil.PDF_LINE_BREAK);
						sb.append(L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.ACCOUNT_NUMBER_LABEL, PDFUtil.DEFAULT_LABEL,
								bankAccount.getAccountNumber()));
					}
				}
			} else {
				sb.append(L10nUtil.getString(MessageCodes.ENCRYPTED_BANK_ACCOUNT_NAME, DefaultMessages.ENCRYPTED_PROBAND_ADDRESS));
			}
		}
		return sb.toString();
	}

	private String getBodyGenderSpecificSalutation() {
		// if (proband != null && proband.getGender() != null) {
		// switch (proband.getGender().getSex()) {
		// case MALE:
		// case TRANSGENDER_MALE:
		// return L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.BODY_MALE_SALUTATION, PDFUtil.DEFAULT_LABEL);
		// case FEMALE:
		// case TRANSGENDER_FEMALE:
		// return L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.BODY_FEMALE_SALUTATION, PDFUtil.DEFAULT_LABEL);
		// default:
		// break;
		// }
		// }
		// return "";
		return CommonUtil.getGenderSpecificSalutation(proband,
				L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.BODY_MALE_SALUTATION, PDFUtil.DEFAULT_LABEL),
				L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.BODY_FEMALE_SALUTATION, PDFUtil.DEFAULT_LABEL));
	}

	private String getCareOf() {
		if (address != null) {
			if (address.isDecrypted()) {
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
		return CommonUtil.getGenderSpecificSalutation(proband,
				L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.MALE_SALUTATION, PDFUtil.DEFAULT_LABEL),
				L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.FEMALE_SALUTATION, PDFUtil.DEFAULT_LABEL));
	}

	public float getHeight(ReimbursementsPDFBlockCursor cursor) throws Exception {
		return renderBlock(null, cursor);
	}

	private String getPaymentMethod() {
		if (byPaymentMethodSummaryDetail != null) {
			PaymentMethodVO methodVO = byPaymentMethodSummaryDetail.getMethod();
			return L10nUtil.getPaymentMethodName(Locales.REIMBURSEMENTS_PDF, methodVO.getNameL10nKey());
		}
		return "";
	}

	private String getPaymentMethods() {
		StringBuilder result = new StringBuilder();
		if (summary != null) {
			Iterator<MoneyTransferByPaymentMethodSummaryDetailVO> it = summary.getTotalsByPaymentMethods().iterator();
			while (it.hasNext()) {
				MoneyTransferByPaymentMethodSummaryDetailVO detail = it.next();
				if (detail.getTotal() > 0.0f) {
					if (result.length() > 0) {
						result.append(", ");
					}
					result.append(L10nUtil.getPaymentMethodName(Locales.REIMBURSEMENTS_PDF, detail.getMethod().getNameL10nKey()));
				}
			}
		}
		return result.toString();
	}

	public ProbandOutVO getProband() {
		return proband;
	}

	private String getProbandName(boolean withTitles, boolean withFirstName) {
		StringBuilder sb = new StringBuilder();
		// if (proband != null) {
		return CommonUtil.getProbandName(proband, withTitles, withFirstName,
				L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_NAME, DefaultMessages.ENCRYPTED_PROBAND_NAME),
				L10nUtil.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME, DefaultMessages.NEW_BLINDED_PROBAND_NAME),
				L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME));
		// if (proband.isDecrypted()) {
		// if (!proband.isBlinded()) {
		// if (withTitles) {
		// CommonUtil.appendString(sb, proband.getPrefixedTitle1(), null);
		// CommonUtil.appendString(sb, proband.getPrefixedTitle2(), " ");
		// CommonUtil.appendString(sb, proband.getPrefixedTitle3(), " ");
		// if (withFirstName) {
		// CommonUtil.appendString(sb, proband.getFirstName(), " ");
		// }
		// CommonUtil.appendString(sb, proband.getLastName(), " ", "?");
		// CommonUtil.appendString(sb, proband.getPostpositionedTitle1(), ", ");
		// CommonUtil.appendString(sb, proband.getPostpositionedTitle2(), ", ");
		// CommonUtil.appendString(sb, proband.getPostpositionedTitle3(), ", ");
		// } else {
		// if (withFirstName) {
		// CommonUtil.appendString(sb, proband.getFirstName(), null);
		// CommonUtil.appendString(sb, proband.getLastName(), " ", "?");
		// } else {
		// CommonUtil.appendString(sb, proband.getLastName(), null, "?");
		// }
		// }
		// } else {
		// if (proband.getId() > 0) {
		// sb.append(MessageFormat.format(L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME), Long.toString(proband.getId())));
		// } else {
		// sb.append(L10nUtil.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME, DefaultMessages.NEW_BLINDED_PROBAND_NAME));
		// }
		// }
		// } else {
		// sb.append(L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_NAME, DefaultMessages.ENCRYPTED_PROBAND_NAME));
		// }
		// }
		// return sb.toString();
	}

	private String getSignatureLabel() {
		if (proband != null) {
			return L10nUtil.getReimbursementsPDFLabel(
					Locales.REIMBURSEMENTS_PDF,
					ReimbursementsPDFLabelCodes.SIGNATURE_ANNOTATION,
					PDFUtil.DEFAULT_LABEL,
					getProbandName(true, true),
					now == null ? null
							: Settings.getSimpleDateFormat(ReimbursementsPDFSettingCodes.SIGNATURE_DATE_PATTERN, Bundle.REIMBURSEMENTS_PDF,
									ReimbursementsPDFDefaultSettings.SIGNATURE_DATE_PATTERN, Locales.REIMBURSEMENTS_PDF).format(now));
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

	public TrialOutVO getTrial() {
		return trial;
	}

	private String getTrialDepartmentPDFLabel(String l10nKey) {
		String key = L10nUtil.getDepartmentL10nKey(l10nKey, trial);
		if (L10nUtil.containsReimbursementsPdfLabel(Locales.REIMBURSEMENTS_PDF, key)) {
			return L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, key, PDFUtil.DEFAULT_LABEL);
		}
		return L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, l10nKey, PDFUtil.DEFAULT_LABEL);
		// if (trial != null && trial.getDepartment() != null) {
		// String l10nKey = baseL10nKey + '_' + trial.getDepartment().getNameL10nKey();
		// if (L10nUtil.containsReimbursementsPdfLabel(Locales.REIMBURSEMENTS_PDF, l10nKey)) {
		// return L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, l10nKey, PDFUtil.DEFAULT_LABEL);
		// }
		// // result = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, baseL10nKey + '_' + trial.getDepartment().getNameL10nKey(), "");
		// }
		// return L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, baseL10nKey, PDFUtil.DEFAULT_LABEL);
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

	public boolean isLastTableRow() {
		return lastTableRow;
	}

	public float renderBlock(PDPageContentStream contentStream, ReimbursementsPDFBlockCursor cursor) throws Exception {
		float x;
		float x1;
		float x2;
		float y;
		float y1;
		float y2;
		float y3;
		String line;
		String line1;
		String line2;
		String line3;
		String line4;
		String line5;
		String line6;
		float height;
		Iterator it = null;
		float total;
		switch (type) {
			case RECIPIENT_ADDRESS:
				line1 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.RECIPIENT_ADDRESS_LINE1, PDFUtil.DEFAULT_LABEL);
				line2 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.RECIPIENT_ADDRESS_LINE2, PDFUtil.DEFAULT_LABEL,
						getGenderSpecificSalutation(), getProbandName(true, true));
				line3 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.RECIPIENT_ADDRESS_LINE6, PDFUtil.DEFAULT_LABEL, getCareOf());
				line4 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.RECIPIENT_ADDRESS_LINE3, PDFUtil.DEFAULT_LABEL,
						getStreetNameHouseNumberEntranceDoornumber());
				line5 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.RECIPIENT_ADDRESS_LINE4, PDFUtil.DEFAULT_LABEL,
						getZipCodeCityName(), getCountryName());
				line6 = "";
				if (!getCountryName().equals(Settings.getString(SettingCodes.INLAND, Bundle.SETTINGS, DefaultSettings.INLAND))) {
					line6 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.RECIPIENT_ADDRESS_LINE5, PDFUtil.DEFAULT_LABEL,
							getCountryName().toUpperCase());
				}
				x = cursor.getBlockX();
				y = Settings.getFloat(ReimbursementsPDFSettingCodes.ADDRESS_Y, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.ADDRESS_Y);
				if (!CommonUtil.isEmptyString(line1)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line1, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				if (!CommonUtil.isEmptyString(line2)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line2, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				if (!CommonUtil.isEmptyString(line3)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line3, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				if (!CommonUtil.isEmptyString(line4)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line4, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				if (!CommonUtil.isEmptyString(line5)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line5, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				if (!CommonUtil.isEmptyString(line6)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line6, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				height = cursor.getBlockY() - y;
				break;
			case SENDER_ADDRESS:
				line1 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.SENDER_ADDRESS_LINE1, PDFUtil.DEFAULT_LABEL);
				line2 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.SENDER_ADDRESS_LINE2, PDFUtil.DEFAULT_LABEL,
						getProbandName(true, true));
				line3 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.SENDER_ADDRESS_LINE6, PDFUtil.DEFAULT_LABEL, getCareOf());
				line4 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.SENDER_ADDRESS_LINE3, PDFUtil.DEFAULT_LABEL,
						getStreetNameHouseNumberEntranceDoornumber());
				line5 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.SENDER_ADDRESS_LINE4, PDFUtil.DEFAULT_LABEL,
						getZipCodeCityName(), getCountryName());
				line6 = "";
				if (!getCountryName().equals(Settings.getString(SettingCodes.INLAND, Bundle.SETTINGS, DefaultSettings.INLAND))) {
					line6 = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.SENDER_ADDRESS_LINE5, PDFUtil.DEFAULT_LABEL,
							getCountryName().toUpperCase());
				}
				x = cursor.getBlockX() + cursor.getBlockWidth();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line1)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line1, x, y,
							PDFUtil.Alignment.TOP_RIGHT);
				}
				if (!CommonUtil.isEmptyString(line2)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontE(), PDFUtil.FontSize.MEDIUM,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line2, x, y,
							PDFUtil.Alignment.TOP_RIGHT);
				}
				if (!CommonUtil.isEmptyString(line3)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line3, x, y,
							PDFUtil.Alignment.TOP_RIGHT);
				}
				if (!CommonUtil.isEmptyString(line4)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line4, x, y,
							PDFUtil.Alignment.TOP_RIGHT);
				}
				if (!CommonUtil.isEmptyString(line5)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line5, x, y,
							PDFUtil.Alignment.TOP_RIGHT);
				}
				if (!CommonUtil.isEmptyString(line6)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line6, x, y,
							PDFUtil.Alignment.TOP_RIGHT);
				}
				height = cursor.getBlockY() - y;
				break;
			case FIRST_PAGE_DATE:
				line = L10nUtil.getReimbursementsPDFLabel(
						Locales.REIMBURSEMENTS_PDF,
						ReimbursementsPDFLabelCodes.DATE,
						PDFUtil.DEFAULT_LABEL,
						now == null ? null
								: Settings.getSimpleDateFormat(ReimbursementsPDFSettingCodes.DATE_PATTERN, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.DATE_PATTERN, Locales.REIMBURSEMENTS_PDF).format(now));
				x = cursor.getBlockX() + cursor.getBlockWidth();
				y = Settings.getFloat(ReimbursementsPDFSettingCodes.FIRST_PAGE_DATE_Y, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.FIRST_PAGE_DATE_Y);
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE11,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line, x, y,
							PDFUtil.Alignment.TOP_RIGHT);
				}
				height = cursor.getBlockY() - y;
				break;
			case FIRST_PAGE_SUBJECT:
				if (trial != null) {
					line = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.FIRST_PAGE_SUBJECT, PDFUtil.DEFAULT_LABEL, trial.getName());
				} else {
					line = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.FIRST_PAGE_SUBJECT_NO_TRIAL, PDFUtil.DEFAULT_LABEL);
				}
				x = cursor.getBlockX();
				y = Settings.getFloat(ReimbursementsPDFSettingCodes.FIRST_PAGE_SUBJECT_Y, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.FIRST_PAGE_SUBJECT_Y);
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				height = cursor.getBlockY() - y;
				break;
			case SALUTATION:
				line = MessageFormat.format(getBodyGenderSpecificSalutation(), getGenderSpecificSalutation(), getProbandName(true, false));
				x = cursor.getBlockX();
				y = Settings.getFloat(ReimbursementsPDFSettingCodes.BODY_SALUTATION_Y, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BODY_SALUTATION_Y);
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE11,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				height = cursor.getBlockY() - y;
				break;
			case TRIAL_TITLE:
				line = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.TRIAL_TITLE, PDFUtil.DEFAULT_LABEL,
						trial != null ? trial.getTitle() : "");
				x = cursor.getBlockX() + cursor.getBlockWidth() / 2.0f;
				y = Settings.getFloat(ReimbursementsPDFSettingCodes.TRIAL_TITLE_Y, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TRIAL_TITLE_Y);
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderMultilineText(contentStream, cursor.getFontC(), PDFUtil.FontSize.SIZE11,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line, x, y,
							PDFUtil.Alignment.TOP_CENTER, cursor.getBlockWidth());
				}
				height = cursor.getBlockY() - y;
				break;
			case RETURN_ADDRESS:
				line = getTrialDepartmentPDFLabel(ReimbursementsPDFLabelCodes.RETURN_ADDRESS);
				x = cursor.getBlockX();
				y = Settings.getFloat(ReimbursementsPDFSettingCodes.ADDRESS_Y, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.ADDRESS_Y);
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line, x, y,
							PDFUtil.Alignment.TOP_LEFT, cursor.getBlockWidth());
				}
				height = cursor.getBlockY() - y;
				break;
			case SECOND_PAGE_DATE:
				line = L10nUtil.getReimbursementsPDFLabel(
						Locales.REIMBURSEMENTS_PDF,
						ReimbursementsPDFLabelCodes.DATE,
						PDFUtil.DEFAULT_LABEL,
						now == null ? null
								: Settings.getSimpleDateFormat(ReimbursementsPDFSettingCodes.DATE_PATTERN, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.DATE_PATTERN, Locales.REIMBURSEMENTS_PDF).format(now));
				x = cursor.getBlockX() + cursor.getBlockWidth();
				y = Settings.getFloat(ReimbursementsPDFSettingCodes.SECOND_PAGE_DATE_Y, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.SECOND_PAGE_DATE_Y);
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE11,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line, x, y,
							PDFUtil.Alignment.TOP_RIGHT);
				}
				height = cursor.getBlockY() - y;
				break;
			case SECOND_PAGE_SUBJECT:
				if (trial != null) {
					line = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.SECOND_PAGE_SUBJECT, PDFUtil.DEFAULT_LABEL, trial.getName(),
							getProbandName(false, true));
				} else {
					line = L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.SECOND_PAGE_SUBJECT_NO_TRIAL, PDFUtil.DEFAULT_LABEL,
							getProbandName(false, true));
				}
				x = cursor.getBlockX();
				y = Settings.getFloat(ReimbursementsPDFSettingCodes.SECOND_PAGE_SUBJECT_Y, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.SECOND_PAGE_SUBJECT_Y);
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderMultilineText(contentStream, cursor.getFontE(), PDFUtil.FontSize.MEDIUM,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR), line, x, y,
							PDFUtil.Alignment.TOP_LEFT, cursor.getBlockWidth());
				}
				height = cursor.getBlockY() - y;
				break;
			case TRIAL_TAGS:
				y1 = cursor.getBlockY()
						- Settings.getFloat(ReimbursementsPDFSettingCodes.Y_TRIAL_TAGS_INDENT, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.Y_TRIAL_TAGS_INDENT);
				y2 = y1;
				x1 = cursor.getBlockX()
						+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_TRIAL_TAGS_INDENT, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.X_TRIAL_TAGS_INDENT);
				x2 = x1
						+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_TRIAL_TAG_NAME_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_TRIAL_TAG_NAME_WIDTH);
				if (trialTagValues != null) {
					it = trialTagValues.iterator();
					while (it.hasNext()) {
						TrialTagValueOutVO tagValue = (TrialTagValueOutVO) it.next();
						y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM,
								Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR),
								getTrialTagName(tagValue.getTag()), x1, y1, PDFUtil.Alignment.TOP_LEFT);
						y2 -= PDFUtil.renderMultilineText(
								contentStream,
								cursor.getFontE(),
								PDFUtil.FontSize.MEDIUM,
								Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR),
								tagValue.getValue(),
								x2,
								y2,
								PDFUtil.Alignment.TOP_LEFT,
								cursor.getBlockX() + cursor.getBlockWidth()
										- x2
										- Settings.getFloat(ReimbursementsPDFSettingCodes.X_TRIAL_TAGS_INDENT, Bundle.REIMBURSEMENTS_PDF,
												ReimbursementsPDFDefaultSettings.X_TRIAL_TAGS_INDENT));
						y1 = Math.min(y1, y2);
						if (it.hasNext()) {
							y1 -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_TRIAL_TAGS_INDENT, Bundle.REIMBURSEMENTS_PDF,
									ReimbursementsPDFDefaultSettings.Y_TRIAL_TAGS_INDENT);
						}
						y2 = y1;
					}
				}
				y = y1 - Settings.getFloat(ReimbursementsPDFSettingCodes.Y_TRIAL_TAGS_INDENT, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.Y_TRIAL_TAGS_INDENT);
				height = cursor.getBlockY() - y;
				break;
			case MONEY_TRANSFER_TABLE_HEAD:
				y1 = cursor.getBlockY()
						- Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_HEAD_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_HEAD_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT) / 2.0f;
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM, Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR,
						Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
						L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF,
								ReimbursementsPDFLabelCodes.PAYMENT_METHOD_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
						x, y1, PDFUtil.Alignment.TOP_CENTER);
				y2 = cursor.getBlockY()
						- Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_HEAD_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_HEAD_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT)
						+ (cursor.getBlockWidth()
								- Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT)
								- Settings.getFloat(
										ReimbursementsPDFSettingCodes.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH))
								/ 2.0f;
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM, Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR,
						Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
						L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF,
								ReimbursementsPDFLabelCodes.COST_TYPE_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
						x, y2, PDFUtil.Alignment.TOP_CENTER);
				y3 = cursor.getBlockY()
						- Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_HEAD_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_HEAD_FRAME_INDENT);
				x = cursor.getBlockX()
						+ cursor.getBlockWidth()
						- Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH) / 2.0f;
				y3 -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM, Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR,
						Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
						L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF,
								ReimbursementsPDFLabelCodes.AMOUNT_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
						x, y3, PDFUtil.Alignment.TOP_CENTER);
				y = Math.min(y1, Math.min(y2, y3));
				y -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_HEAD_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_HEAD_FRAME_INDENT);
				height = cursor.getBlockY() - y;
				PDFUtil.renderFrame(contentStream, Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR), cursor.getBlockX(), cursor.getBlockY(), cursor.getBlockWidth(), height,
						PDFUtil.Alignment.TOP_LEFT, Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR),
						cursor.getBlockX()
								+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT),
						cursor.getBlockY(),
						cursor.getBlockX()
								+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT),
						cursor.getBlockY() - height, Settings.getFloat(
								ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR),
						cursor.getBlockX()
								+ cursor.getBlockWidth()
								- Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH),
						cursor.getBlockY(),
						cursor.getBlockX()
								+ cursor.getBlockWidth()
								- Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH),
						cursor.getBlockY() - height, Settings.getFloat(
								ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				break;
			case BANK_ACCOUNT_TABLE_ROW:
			case PAYMENT_METHOD_TABLE_ROW:
				y1 = cursor.getBlockY();
				if (firstTableRow || newPage) {
					y1 -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
							ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_FRAME_INDENT);
				}
				x = cursor.getBlockX()
						+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_FRAME_INDENT);
				if (byBankAccountSummaryDetail != null) {
					it = byBankAccountSummaryDetail.getByCostTypes().iterator();
					line = (firstTableRow ? getBankAccount() : "");
					total = byBankAccountSummaryDetail.getTotal();
				} else if (byPaymentMethodSummaryDetail != null) {
					it = byPaymentMethodSummaryDetail.getByCostTypes().iterator();
					line = (firstTableRow ? getPaymentMethod() : "");
					total = byPaymentMethodSummaryDetail.getTotal();
				} else {
					it = null;
					line = "";
					total = 0.0f;
				}
				y1 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontE(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
						line,
						x,
						y1,
						PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
												ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_FRAME_INDENT));
				y2 = cursor.getBlockY();
				if (firstTableRow || newPage) {
					y2 -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
							ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_FRAME_INDENT);
				}
				y3 = y2;
				x1 = cursor.getBlockX()
						+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT)
						+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_FRAME_INDENT);
				x2 = cursor.getBlockX()
						+ cursor.getBlockWidth()
						- Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_FRAME_INDENT); // -
				// Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH,
				// Bundle.REIMBURSEMENTS_PDF,
				// ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH);
				if (it != null) {
					while (it.hasNext()) {
						MoneyTransferByCostTypeSummaryDetailVO costTypeDetail = (MoneyTransferByCostTypeSummaryDetailVO) it.next();
						if (costTypeDetail.getCount() > 0l) {
							y2 -= PDFUtil.renderMultilineText(
									contentStream,
									cursor.getFontF(),
									PDFUtil.FontSize.MEDIUM,
									Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF,
											ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
									costTypeDetail.getCostType(),
									x1,
									y2,
									PDFUtil.Alignment.TOP_LEFT,
									x2
											- Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH, Bundle.REIMBURSEMENTS_PDF,
													ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH)
											- x1);
							if (costTypeDetail.getComments().size() > 0) {
								Iterator<String> commentsIt = costTypeDetail.getComments().iterator();
								while (commentsIt.hasNext()) {
									y2 -= PDFUtil.renderMultilineText(
											contentStream,
											cursor.getFontF(),
											PDFUtil.FontSize.SMALL, // TINY,
											Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF,
													ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
											commentsIt.next(),
											x1,
											y2,
											PDFUtil.Alignment.TOP_LEFT,
											x2
													- Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH, Bundle.REIMBURSEMENTS_PDF,
															ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH)
													- x1);
								}
							}
							// if (costTypeDetail.getTotal() != null) {
							y3 -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
									ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
									getAmount(costTypeDetail.getTotal()), x2, y3, PDFUtil.Alignment.TOP_RIGHT);
							// }
							y2 = Math.min(y2, y3)
									- Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
											ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_FRAME_INDENT);
							y3 = y2;
						}
					}
					y = y2;
				} else {
					y = y2
							- Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
									ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_FRAME_INDENT);
				}
				if (lastTableRow) {
					PDFUtil.renderLine(
							contentStream,
							Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
									ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR),
							cursor.getBlockX()
									+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
											ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT),
							y,
							cursor.getBlockX() + cursor.getBlockWidth(),
							y,
							Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_COLUMN_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
									ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_COLUMN_LINE_WIDTH));
					y2 = y
							- Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
									ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_FRAME_INDENT);
					y3 = y2;
					y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM,
							Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR,
									Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
							L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF,
									ReimbursementsPDFLabelCodes.TOTAL_LABEL, PDFUtil.DEFAULT_LABEL),
							x1, y2, PDFUtil.Alignment.TOP_LEFT);
					y3 -= PDFUtil.renderTextLine(contentStream, cursor.getFontE(), PDFUtil.FontSize.MEDIUM,
							Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR,
									Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
							L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF,
									getAmount(total), PDFUtil.DEFAULT_LABEL),
							x2, y3, PDFUtil.Alignment.TOP_RIGHT);
					y = Math.min(y1, Math.min(y2, y3));
					y -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
							ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_FRAME_INDENT);
				} else {
					y1 -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
							ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_FRAME_INDENT);
					y = Math.min(y, y1);
				}
				// y -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
				// ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_FRAME_INDENT);
				height = cursor.getBlockY() - y;
				if (firstTableRow) {
					PDFUtil.renderLine(contentStream, Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
							ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR), cursor.getBlockX(), cursor.getBlockY(), cursor.getBlockX() + cursor.getBlockWidth(),
							cursor.getBlockY(), // height,
							// PDFUtil.Alignment.TOP_LEFT,
							Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
									ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				}
				PDFUtil.renderLine(contentStream, Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR), cursor.getBlockX(), cursor.getBlockY(), cursor.getBlockX(), cursor.getBlockY() - height, // height,
						// PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(contentStream, Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR), cursor.getBlockX() + cursor.getBlockWidth(), cursor.getBlockY(),
						cursor.getBlockX() + cursor.getBlockWidth(), cursor.getBlockY() - height, // height,
						// PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR),
						cursor.getBlockX()
								+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT),
						cursor.getBlockY(),
						cursor.getBlockX()
								+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT),
						cursor.getBlockY() - height, Settings.getFloat(
								ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR),
						cursor.getBlockX()
								+ cursor.getBlockWidth()
								- Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH),
						cursor.getBlockY(),
						cursor.getBlockX()
								+ cursor.getBlockWidth()
								- Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH),
						cursor.getBlockY() - height, Settings.getFloat(
								ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				if (lastTableRow) {
					PDFUtil.renderLine(contentStream, Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
							ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR), cursor.getBlockX(), cursor.getBlockY() - height,
							cursor.getBlockX() + cursor.getBlockWidth(), cursor.getBlockY() - height, // height,
							// PDFUtil.Alignment.TOP_LEFT,
							Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
									ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				}
				// PDFUtil.renderFrame(contentStream, Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
				// ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR), cursor.getBlockX(), cursor.getBlockY(), cursor.getBlockWidth(), height,
				// PDFUtil.Alignment.TOP_LEFT, Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
				// ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				break;
			case SIGNATURE_TOTAL:
				// case COMPLETE_INSTRUCTION:
				y = cursor.getBlockY()
						- Settings.getFloat(ReimbursementsPDFSettingCodes.Y_TOTALS_INDENT, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.Y_TOTALS_INDENT);
				y -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontD(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.COMPLETE_INSTRUCTION_PARAGRAPH, PDFUtil.DEFAULT_LABEL),
						cursor.getBlockX(),
						y,
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth());
				x = cursor.getBlockX()
						+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_FRAME_INDENT);
				x1 = cursor.getBlockX()
						+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT)
						+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_FRAME_INDENT);
				x2 = cursor.getBlockX()
						+ cursor.getBlockWidth()
						- Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_FRAME_INDENT);
				y -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_TOTALS_INDENT, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.Y_TOTALS_INDENT);
				y1 = y - Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_FRAME_INDENT);
				y2 = y1;
				y3 = y1;
				total = summary.getTotal();
				line = getPaymentMethods();
				y1 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontE(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
						line,
						x,
						y1,
						PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
												ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_FRAME_INDENT));
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR,
								Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
						L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF,
								ReimbursementsPDFLabelCodes.TOTAL_LABEL, PDFUtil.DEFAULT_LABEL),
						x1, y2, PDFUtil.Alignment.TOP_LEFT);
				y3 -= PDFUtil.renderTextLine(contentStream, cursor.getFontE(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_TEXT_COLOR,
								Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_TEXT_COLOR),
						L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF,
								getAmount(total), PDFUtil.DEFAULT_LABEL),
						x2, y3, PDFUtil.Alignment.TOP_RIGHT);
				y1 = Math.min(y1, Math.min(y2, y3));
				y1 -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_PAYMENT_TABLE_FRAME_INDENT, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.Y_PAYMENT_TABLE_FRAME_INDENT);
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR),
						cursor.getBlockX()
								+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT),
						y,
						cursor.getBlockX()
								+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_PAYMENT_TABLE_COLUMN_INDENT, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.X_PAYMENT_TABLE_COLUMN_INDENT),
						y1, Settings.getFloat(
								ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR),
						cursor.getBlockX()
								+ cursor.getBlockWidth()
								- Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH),
						y,
						cursor.getBlockX()
								+ cursor.getBlockWidth()
								- Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH),
						y1, Settings.getFloat(
								ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderFrame(contentStream, Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR), cursor.getBlockX(), y, cursor.getBlockWidth(), y - y1,
						PDFUtil.Alignment.TOP_LEFT, Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				// break;
				// y = cursor.getBlockY()
				y = y1 - Settings.getFloat(ReimbursementsPDFSettingCodes.Y_FRAME_UPPER_INDENT_SIGNATURE, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.Y_FRAME_UPPER_INDENT_SIGNATURE);
				x = cursor.getBlockX()
						+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE);
				x1 = cursor.getBlockX()
						+ cursor.getBlockWidth()
						- Settings.getFloat(ReimbursementsPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE)
						- Settings.getFloat(ReimbursementsPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.SIGNATURE_LINE_LENGTH);
				if (Settings.getFloat(ReimbursementsPDFSettingCodes.DATE_LINE_LENGTH, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.DATE_LINE_LENGTH) > 0.0f) {
					x1 -= Settings.getFloat(ReimbursementsPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.REIMBURSEMENTS_PDF,
							ReimbursementsPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE)
							+ Settings.getFloat(ReimbursementsPDFSettingCodes.DATE_LABEL_WIDTH, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.DATE_LABEL_WIDTH)
							+ Settings.getFloat(ReimbursementsPDFSettingCodes.DATE_LINE_LENGTH, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.DATE_LINE_LENGTH);
				}
				y -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontD(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.SIGNATURE, PDFUtil.DEFAULT_LABEL),
						x,
						y,
						PDFUtil.Alignment.TOP_LEFT,
						x1
								- x
								- Settings.getFloat(ReimbursementsPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE));
				y1 = y;
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(ReimbursementsPDFSettingCodes.FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.FRAME_COLOR),
						x1,
						y,
						x1
								+ Settings.getFloat(ReimbursementsPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.SIGNATURE_LINE_LENGTH),
						y,
						Settings.getFloat(ReimbursementsPDFSettingCodes.SIGNATURE_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.SIGNATURE_LINE_WIDTH));
				y -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_OFFSET_SIGNATURE_ANNOTATION, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.Y_OFFSET_SIGNATURE_ANNOTATION);
				y -= PDFUtil.renderTextLine(
						contentStream,
						cursor.getFontD(),
						PDFUtil.FontSize.TINY,
						Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR),
						getSignatureLabel(),
						x1
								+ Settings.getFloat(ReimbursementsPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.REIMBURSEMENTS_PDF,
										ReimbursementsPDFDefaultSettings.SIGNATURE_LINE_LENGTH) / 2.0f,
						y, PDFUtil.Alignment.TOP_CENTER);
				y -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_FRAME_LOWER_INDENT_SIGNATURE, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.Y_FRAME_LOWER_INDENT_SIGNATURE);
				height = cursor.getBlockY() - y;
				if (Settings.getFloat(ReimbursementsPDFSettingCodes.DATE_LINE_LENGTH, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.DATE_LINE_LENGTH) > 0.0f) {
					y = y1;
					x = x1
							+ Settings.getFloat(ReimbursementsPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.REIMBURSEMENTS_PDF,
									ReimbursementsPDFDefaultSettings.SIGNATURE_LINE_LENGTH)
							+ Settings.getFloat(ReimbursementsPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.REIMBURSEMENTS_PDF,
									ReimbursementsPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE);
					x1 = x + Settings.getFloat(ReimbursementsPDFSettingCodes.DATE_LABEL_WIDTH, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.DATE_LABEL_WIDTH);
					PDFUtil.renderMultilineText(
							contentStream,
							cursor.getFontD(),
							PDFUtil.FontSize.MEDIUM,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR),
							L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.SIGNATURE_DATE, PDFUtil.DEFAULT_LABEL),
							x,
							y,
							PDFUtil.Alignment.BOTTOM_LEFT,
							x1
									- x
									- Settings.getFloat(ReimbursementsPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.REIMBURSEMENTS_PDF,
											ReimbursementsPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE));
					PDFUtil.renderLine(contentStream,
							Settings.getColor(ReimbursementsPDFSettingCodes.FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.FRAME_COLOR), x1, y,
							x1 + Settings.getFloat(ReimbursementsPDFSettingCodes.DATE_LINE_LENGTH, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.DATE_LINE_LENGTH),
							y,
							Settings.getFloat(ReimbursementsPDFSettingCodes.SIGNATURE_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
									ReimbursementsPDFDefaultSettings.SIGNATURE_LINE_WIDTH));
					y -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_OFFSET_SIGNATURE_ANNOTATION, Bundle.REIMBURSEMENTS_PDF,
							ReimbursementsPDFDefaultSettings.Y_OFFSET_SIGNATURE_ANNOTATION);
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontD(), PDFUtil.FontSize.TINY,
							Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR),
							L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.DATE_ANNOTATION, PDFUtil.DEFAULT_LABEL),
							x1 + Settings.getFloat(ReimbursementsPDFSettingCodes.DATE_LINE_LENGTH, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.DATE_LINE_LENGTH)
									/ 2.0f,
							y, PDFUtil.Alignment.TOP_CENTER);
					y -= Settings.getFloat(ReimbursementsPDFSettingCodes.Y_FRAME_LOWER_INDENT_SIGNATURE, Bundle.REIMBURSEMENTS_PDF,
							ReimbursementsPDFDefaultSettings.Y_FRAME_LOWER_INDENT_SIGNATURE);
				}
				break;
			case SPACER:
				height = Settings.getFloat(ReimbursementsPDFSettingCodes.SPACER_HEIGHT, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.SPACER_HEIGHT);
				break;
			default:
				height = 0.0f;
				break;
		}
		return height;
	}

	public void setNewPage(boolean newPage) {
		this.newPage = newPage;
	}
}