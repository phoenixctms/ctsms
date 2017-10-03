package org.phoenixctms.ctsms.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.imageio.ImageIO;
import javax.script.ScriptException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.excel.ExcelCellFormat;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class Settings {

	public enum Bundle {
		SETTINGS,
		TRUSTED_HOSTS,
		CV_PDF,
		REIMBURSEMENTS_PDF,
		COURSE_PARTICIPANT_LIST_PDF,
		PROBAND_LETTER_PDF,
		COURSE_CERTIFICATE_PDF,
		JOURNAL_EXCEL,
		PROBAND_LIST_EXCEL,
		SEARCH_RESULT_EXCEL,
		VISIT_SCHEDULE_EXCEL,
		TEAM_MEMBERS_EXCEL,
		REIMBURSEMENTS_EXCEL,
		ECRF_PDF,
		INQUIRIES_PDF,
		PROBAND_LIST_ENTRY_TAGS_PDF,
		AUDIT_TRAIL_EXCEL,
		INVENTORY_BOOKINGS_EXCEL,
	}

	private static String settingsBundleBasename;
	private static String trustedHostsBundleBasename;
	private static String cvPdfSettingsBundleBasename;
	private static String reimbursementsPdfSettingsBundleBasename;
	private static String courseParticipantListPdfSettingsBundleBasename;
	private static String probandLetterPdfSettingsBundleBasename;
	private static String courseCertificatePdfSettingsBundleBasename;
	private static String ecrfPdfSettingsBundleBasename;
	private static String inquiriesPdfSettingsBundleBasename;
	private static String probandListEntryTagsPdfSettingsBundleBasename;
	private static String journalExcelSettingsBundleBasename;
	private static String probandListExcelSettingsBundleBasename;
	private static String searchResultExcelSettingsBundleBasename;
	private static String visitScheduleExcelSettingsBundleBasename;
	private static String teamMembersExcelSettingsBundleBasename;
	private static String reimbursementsExcelSettingsBundleBasename;
	private static String auditTrailExcelSettingsBundleBasename;
	private static String inventoryBookingsExcelSettingsBundleBasename;

	// private final static ArrayList<String> CLASS_PATH_ELEMENTS = new ArrayList<String>();
	// static {
	// CLASS_PATH_ELEMENTS.add("");
	// CLASS_PATH_ELEMENTS.addAll(Arrays.asList(System.getProperty("java.class.path", ".").split(System.getProperty("path.separator"))));
	// }

	private static String checkDirectory(String externalFileDataDir) throws IOException, IllegalArgumentException {
		String directory;
		File file = new File(externalFileDataDir);
		if (file.exists()) {
			if (file.isDirectory()) {
				if (file.isAbsolute()) {
					if (file.canRead() && file.canWrite()) {
						directory = file.getCanonicalPath();
					} else {
						throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.EXTERNAL_FILE_DATADIR_ACCESS_ERROR, DefaultMessages.EXTERNAL_FILE_DATADIR_ACCESS_ERROR,
								externalFileDataDir));
					}
				} else {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.EXTERNAL_FILE_DATADIR_NOT_ABSOLUTE_ERROR,
							DefaultMessages.EXTERNAL_FILE_DATADIR_NOT_ABSOLUTE_ERROR, externalFileDataDir));
				}
			} else {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.EXTERNAL_FILE_DATADIR_NOTADIR_ERROR, DefaultMessages.EXTERNAL_FILE_DATADIR_NOTADIR_ERROR,
						externalFileDataDir));
			}
		} else if (file.mkdirs()) {
			directory = file.getCanonicalPath();
		} else {
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.EXTERNAL_FILE_DATADIR_CREATION_ERROR, DefaultMessages.EXTERNAL_FILE_DATADIR_CREATION_ERROR,
					externalFileDataDir));
		}
		return directory;
	}

	private static String checkImageFileName(String imageFileName, String path) throws IOException, IllegalArgumentException {
		String image;
		if (imageFileName != null && imageFileName.length() > 0) {
			File file = new File(imageFileName);
			if (file.exists()) {
				if (file.isFile()) {
					if (file.canRead()) {

						if ( ImageIO.read(file) != null) {
							image = file.getCanonicalPath();
						} else {
							throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.IMAGE_FILE_INVALID_IMAGE_ERROR,
									DefaultMessages.IMAGE_FILE_INVALID_IMAGE_ERROR, imageFileName));
						}

					} else {
						throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.IMAGE_FILE_ACCESS_ERROR, DefaultMessages.IMAGE_FILE_ACCESS_ERROR,
								imageFileName));
					}
				} else {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.IMAGE_FILE_NOTAFILE_ERROR, DefaultMessages.IMAGE_FILE_NOTAFILE_ERROR,
							imageFileName));
				}
			} else {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.IMAGE_FILE_DOES_NOT_EXIST_ERROR, DefaultMessages.IMAGE_FILE_DOES_NOT_EXIST_ERROR,
						imageFileName));
			}
		} else {
			image = "";
		}
		return image;
	}

	private static String checkJsFileName(String JsFileName, String path) throws IOException, IllegalArgumentException {
		String js;
		if (JsFileName != null && JsFileName.length() > 0) {
			File file = new File(path, JsFileName);
			if (file.exists()) {
				if (file.isFile()) {
					if (file.canRead()) {
						try {
							CoreUtil.getJsEngine().eval(new FileReader(file));
							js = file.getCanonicalPath();
						} catch (ScriptException e) {
							js = file.getCanonicalPath();
						} catch (Exception e) {
							throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.JS_FILE_SCRIPT_ERROR,
									DefaultMessages.JS_FILE_SCRIPT_ERROR, JsFileName, e.getMessage()), e);
						}
					} else {
						throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.JS_FILE_ACCESS_ERROR, DefaultMessages.JS_FILE_ACCESS_ERROR,
								JsFileName));
					}
				} else {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.JS_FILE_NOTAFILE_ERROR, DefaultMessages.JS_FILE_NOTAFILE_ERROR,
							JsFileName));
				}
			} else {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.JS_FILE_DOES_NOT_EXIST_ERROR, DefaultMessages.JS_FILE_DOES_NOT_EXIST_ERROR,
						JsFileName));
			}
		} else {
			js = "";
		}
		return js;
	}

	private static String checkPDFFontName(String fontName, String label, String path) throws IOException, IllegalArgumentException {
		String font;
		if (fontName != null && fontName.length() > 0) {
			if (PDType1Font.getStandardFont(fontName) != null) {
				font = fontName;
			} else {
				File file = new File(fontName);
				if (file.exists()) {
					if (file.isFile()) {
						if (file.canRead()) {
							PDDocument doc = null;
							try {
								doc = new PDDocument();
								PDFont ttfFont = PDTrueTypeFont.loadTTF(doc, file);
								font = file.getCanonicalPath();
							} catch (Exception e) {
								throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.TTF_INVALID_TTF_ERROR, DefaultMessages.TTF_INVALID_TTF_ERROR, label, fontName),
										e);
							} finally {
								if (doc != null) {
									doc.close();
								}
							}
						} else {
							throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.TTF_FILE_ACCESS_ERROR, DefaultMessages.TTF_FILE_ACCESS_ERROR, label, fontName));
						}
					} else {
						throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.TTF_NOTAFILE_ERROR, DefaultMessages.TTF_NOTAFILE_ERROR, label, fontName));
					}
				} else {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.TTF_FILE_DOES_NOT_EXIST_ERROR, DefaultMessages.TTF_FILE_DOES_NOT_EXIST_ERROR, label,
							fontName));
				}
			}
		} else {
			font = "";
		}
		return font;
	}

	private static String checkPDFTemplateFileName(String PDFTemplateFileName, String path) throws IOException, IllegalArgumentException {
		String template;
		if (PDFTemplateFileName != null && PDFTemplateFileName.length() > 0) {
			File file = new File(PDFTemplateFileName);
			if (file.exists()) {
				if (file.isFile()) {
					if (file.canRead()) {
						PDDocument doc = null;
						try {
							doc = PDDocument.load(file);
							PDPage page = (PDPage) doc.getDocumentCatalog().getAllPages().get(0);
							PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, true);
							contentStream.close();
							template = file.getCanonicalPath();
						} catch (Exception e) {
							throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PDF_TEMPLATE_FILE_INVALID_PDF_ERROR,
									DefaultMessages.PDF_TEMPLATE_FILE_INVALID_PDF_ERROR, PDFTemplateFileName), e);
						} finally {
							if (doc != null) {
								doc.close();
							}
						}
					} else {
						throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PDF_TEMPLATE_FILE_ACCESS_ERROR, DefaultMessages.PDF_TEMPLATE_FILE_ACCESS_ERROR,
								PDFTemplateFileName));
					}
				} else {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PDF_TEMPLATE_FILE_NOTAFILE_ERROR, DefaultMessages.PDF_TEMPLATE_FILE_NOTAFILE_ERROR,
							PDFTemplateFileName));
				}
			} else {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PDF_TEMPLATE_FILE_DOES_NOT_EXIST_ERROR, DefaultMessages.PDF_TEMPLATE_FILE_DOES_NOT_EXIST_ERROR,
						PDFTemplateFileName));
			}
		} else {
			template = "";
		}
		return template;
	}

	public static boolean containsKey(String key, Bundle bundle) {
		return CommonUtil.bundleContainsKey(key,getBundle(bundle));
	}

	public static boolean getBoolean(String key, Bundle bundle, boolean defaultValue) {
		return CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	}

	private static ResourceBundle getBundle(Bundle bundle) {
		switch (bundle) {
			case SETTINGS:
				return getBundle(settingsBundleBasename);
			case TRUSTED_HOSTS:
				return getBundle(trustedHostsBundleBasename);
			case CV_PDF:
				return getBundle(cvPdfSettingsBundleBasename);
			case REIMBURSEMENTS_PDF:
				return getBundle(reimbursementsPdfSettingsBundleBasename);
			case COURSE_PARTICIPANT_LIST_PDF:
				return getBundle(courseParticipantListPdfSettingsBundleBasename);
			case PROBAND_LETTER_PDF:
				return getBundle(probandLetterPdfSettingsBundleBasename);
			case COURSE_CERTIFICATE_PDF:
				return getBundle(courseCertificatePdfSettingsBundleBasename);
			case JOURNAL_EXCEL:
				return getBundle(journalExcelSettingsBundleBasename);
			case PROBAND_LIST_EXCEL:
				return getBundle(probandListExcelSettingsBundleBasename);
			case SEARCH_RESULT_EXCEL:
				return getBundle(searchResultExcelSettingsBundleBasename);
			case VISIT_SCHEDULE_EXCEL:
				return getBundle(visitScheduleExcelSettingsBundleBasename);
			case AUDIT_TRAIL_EXCEL:
				return getBundle(auditTrailExcelSettingsBundleBasename);
			case INVENTORY_BOOKINGS_EXCEL:
				return getBundle(inventoryBookingsExcelSettingsBundleBasename);
			case TEAM_MEMBERS_EXCEL:
				return getBundle(teamMembersExcelSettingsBundleBasename);
			case REIMBURSEMENTS_EXCEL:
				return getBundle(reimbursementsExcelSettingsBundleBasename);
			case ECRF_PDF:
				return getBundle(ecrfPdfSettingsBundleBasename);
			case INQUIRIES_PDF:
				return getBundle(inquiriesPdfSettingsBundleBasename);
			case PROBAND_LIST_ENTRY_TAGS_PDF:
				return getBundle(probandListEntryTagsPdfSettingsBundleBasename);
			default:
				return null;
		}
	}

	private static ResourceBundle getBundle(String baseName) {
		return CommonUtil.getBundle(baseName, Locale.ROOT);
	}

	public static Map<String, String> getBundleMap(Bundle bundle, boolean sorted) {
		return CommonUtil.getBundleSymbolMap(getBundle(bundle), sorted);
	}

	public static Color getColor(String key, Bundle bundle, Color defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return Color.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static DecimalFormat getDecimalFormat(String key, Bundle bundle, String defaultValue, Locales locale) {
		String decimalFormat = CommonUtil.getValue(key, getBundle(bundle), defaultValue);
		try {
			return new DecimalFormat(decimalFormat, new DecimalFormatSymbols(L10nUtil.getLocale(locale)));
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static String getDirectory(String key, Bundle bundle, String defaultValue) throws IllegalArgumentException, IOException {
		return checkDirectory(CommonUtil.getValue(key, getBundle(bundle), defaultValue));
	}

	public static String getDocumentRootReplacement() {
		return MessageFormat.format(Settings.getString(SettingCodes.HTTP_DOCUMENT_ROOT_REPLACEMENT, Bundle.SETTINGS, DefaultSettings.HTTP_DOCUMENT_ROOT_REPLACEMENT),getHttpBaseUrl());
	}

	public static ECRFFieldStatusQueue getEcrfFieldStatusQueue(String key, Bundle bundle, ECRFFieldStatusQueue defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return ECRFFieldStatusQueue.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	public static List<ECRFFieldStatusQueue> getEcrfFieldStatusQueueList(String key, Bundle bundle, ArrayList<String> defaultValue) {
		ArrayList<String> queues = CommonUtil.getValueStringList(key, getBundle(bundle), defaultValue);
		ArrayList<ECRFFieldStatusQueue> result = new ArrayList<ECRFFieldStatusQueue>(queues.size());
		Iterator<String> it = queues.iterator();
		while (it.hasNext()) {
			result.add(ECRFFieldStatusQueue.fromString(it.next()));
		}
		return result;
	}

	public static String getEmailDomainName() {
		return MessageFormat.format(Settings.getString(SettingCodes.EMAIL_DOMAIN_NAME, Bundle.SETTINGS, DefaultSettings.EMAIL_DOMAIN_NAME), getHttpDomainName());
	}

	public static String getEmailExecFromAddress() {
		return MessageFormat.format(Settings.getString(SettingCodes.EMAIL_EXEC_FROM_ADDRESS, Bundle.SETTINGS, DefaultSettings.EMAIL_EXEC_FROM_ADDRESS), getEmailDomainName());
	}

	public static String getEmailExecFromName() {
		return MessageFormat.format(Settings.getString(SettingCodes.EMAIL_EXEC_FROM_NAME, Bundle.SETTINGS, DefaultSettings.EMAIL_EXEC_FROM_NAME),
				Settings.getString(SettingCodes.APPLICATION_ABBREVIATION, Bundle.SETTINGS, null), getInstanceName());
	}

	public static String getEmailNotificationFromAddress() {
		return MessageFormat.format(Settings.getString(SettingCodes.EMAIL_NOTIFICATION_FROM_ADDRESS, Bundle.SETTINGS, DefaultSettings.EMAIL_NOTIFICATION_FROM_ADDRESS),
				getEmailDomainName());
	}

	public static String getEmailNotificationFromName() {
		return MessageFormat
				.format(Settings.getString(SettingCodes.EMAIL_NOTIFICATION_FROM_NAME, Bundle.SETTINGS, DefaultSettings.EMAIL_NOTIFICATION_FROM_NAME),
						Settings.getString(SettingCodes.APPLICATION_ABBREVIATION, Bundle.SETTINGS, null), getInstanceName());
	}

	public static ExcelCellFormat getExcelCellFormat(String key, Bundle bundle, ExcelCellFormat defaultValue) {
		ArrayList<String> value = CommonUtil.getValueStringList(key, getBundle(bundle), null);
		if (value.size() > 0) {
			try {
				return ExcelCellFormat.fromString(value.get(0), value.get(1), value.get(2), value.get(3), value.get(4));
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			return defaultValue;
		}
	}

	public static float getFloat(String key, Bundle bundle, float defaultValue) {
		return CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	}

	public static Float getFloatNullable(String key, Bundle bundle, Float defaultValue) {
		return CommonUtil.getValueNullable(key, getBundle(bundle), defaultValue);
	}

	public static String getHttpBaseUrl() {
		return MessageFormat.format(Settings.getString(SettingCodes.HTTP_BASE_URL, Bundle.SETTINGS, DefaultSettings.HTTP_BASE_URL), getHttpScheme(), getHttpHost());
	}

	public static String getHttpDomainName() {
		return  Settings.getString(SettingCodes.HTTP_DOMAIN_NAME, Bundle.SETTINGS, DefaultSettings.HTTP_DOMAIN_NAME);
	}

	public static String getHttpHost() {
		return MessageFormat.format(Settings.getString(SettingCodes.HTTP_HOST, Bundle.SETTINGS, DefaultSettings.HTTP_HOST), getHttpDomainName());
	}

	public static String getHttpScheme() {
		return Settings.getString(SettingCodes.HTTP_SCHEME, Bundle.SETTINGS, DefaultSettings.HTTP_SCHEME);
	}

	public static String getImageFilename(String key, Bundle bundle, String defaultValue) throws IllegalArgumentException, IOException {
		return checkImageFileName(CommonUtil.getValue(key, getBundle(bundle), defaultValue), null);
	}

	// public static String getImageFilename(String key, Bundle bundle, String defaultValue) throws IllegalArgumentException, IOException {
	// String fileName = CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	// IllegalArgumentException e = null;
	// Iterator<String> it = CLASS_PATH_ELEMENTS.iterator();
	// while (it.hasNext()) {
	// try {
	// return checkImageFileName(fileName, it.next());
	// } catch (IllegalArgumentException exc) {
	// e = exc;
	// if (exc.getCause() != null) {
	// throw exc;
	// }
	// // } catch (IOException e) {
	// // exception = e;
	// }
	// }
	// if (e != null) {
	// throw e;
	// }
	// return "";
	// }

	public static String getInstanceName() {
		return MessageFormat
				.format(Settings.getString(SettingCodes.INSTANCE_NAME, Bundle.SETTINGS, DefaultSettings.INSTANCE_NAME), CommonUtil.LOCAL_HOST_NAME, getHttpDomainName());
	}

	public static int getInt(String key, Bundle bundle, int defaultValue) {
		return CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	}

	public static Integer getIntNullable(String key, Bundle bundle, Integer defaultValue) {
		return CommonUtil.getValueNullable(key, getBundle(bundle), defaultValue);
	}

	public static String getJsFilename(String key, Bundle bundle, String defaultValue) throws IllegalArgumentException, IOException {
		return checkJsFileName(CommonUtil.getValue(key, getBundle(bundle), defaultValue), null);
	}

	// public static InputStreamReader getJsFile(String key, Bundle bundle, String defaultValue) throws IOException {
	// String fileName = CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	// ClassPathResource resource = new ClassPathResource("/" + fileName);
	// return new InputStreamReader(resource.getInputStream());
	// //resource.getInputStream()
	// //byte[] data = CommonUtil.inputStreamToByteArray(resource.getInputStream());
	// }
	// public static String getJsFilename(String key, Bundle bundle, String defaultValue) throws IllegalArgumentException, IOException {
	// String fileName = CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	// IllegalArgumentException e = null;
	// Iterator<String> it = CLASS_PATH_ELEMENTS.iterator();
	// while (it.hasNext()) {
	// try {
	// return checkJsFileName(fileName, it.next());
	// } catch (IllegalArgumentException exc) {
	// e = exc;
	// if (exc.getCause() != null) {
	// throw exc;
	// }
	// // } catch (IOException e) {
	// // exception = e;
	// }
	// }
	// if (e != null) {
	// throw e;
	// }
	// return "";
	// }

	public static long getLong(String key, Bundle bundle, long defaultValue) {
		return CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	}

	public static Long getLongNullable(String key, Bundle bundle, Long defaultValue) {
		return CommonUtil.getValueNullable(key, getBundle(bundle), defaultValue);
	}

	public static String getPDFFontName(String key, Bundle bundle, String defaultValue) throws IllegalArgumentException, IOException {
		return checkPDFFontName(CommonUtil.getValue(key, getBundle(bundle), defaultValue), key, null);
	}

	// public static String getPDFFontName(String key, Bundle bundle, String defaultValue) throws IllegalArgumentException, IOException {
	// String fontName = CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	// IllegalArgumentException e = null;
	// Iterator<String> it = CLASS_PATH_ELEMENTS.iterator();
	// while (it.hasNext()) {
	// try {
	// return checkPDFFontName(fontName, key, it.next());
	// } catch (IllegalArgumentException exc) {
	// e = exc;
	// if (exc.getCause() != null) {
	// throw exc;
	// }
	// // } catch (IOException e) {
	// // exception = e;
	// }
	// }
	// if (e != null) {
	// throw e;
	// }
	// return "";
	// }

	public static String getPDFTemplateFilename(String key, Bundle bundle, String defaultValue) throws IllegalArgumentException, IOException {
		return checkPDFTemplateFileName(CommonUtil.getValue(key, getBundle(bundle), defaultValue), null);
	}

	// public static String getPDFTemplateFilename(String key, Bundle bundle, String defaultValue) throws IllegalArgumentException, IOException {
	// String fontName = CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	// IllegalArgumentException e = null;
	// Iterator<String> it = CLASS_PATH_ELEMENTS.iterator();
	// while (it.hasNext()) {
	// try {
	// return checkPDFTemplateFileName(fontName, it.next());
	// } catch (IllegalArgumentException exc) {
	// e = exc;
	// if (exc.getCause() != null) {
	// throw exc;
	// }
	// // } catch (IOException e) {
	// // exception = e;
	// }
	// }
	// if (e != null) {
	// throw e;
	// }
	// return "";
	// }

	public static Pattern getRegexp(String key, Bundle bundle, String defaultValue) {
		String pattern = CommonUtil.getValue(key, getBundle(bundle), defaultValue);
		if (pattern != null && pattern.length() > 0) {
			try {
				return java.util.regex.Pattern.compile(pattern);
			} catch (PatternSyntaxException e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			return null;
		}
	}



	public static SimpleDateFormat getSimpleDateFormat(String key, Bundle bundle, String defaultValue, Locales locale) {
		String dateFormat = CommonUtil.getValue(key, getBundle(bundle), defaultValue);
		try {
			return new SimpleDateFormat(dateFormat, L10nUtil.getLocale(locale));
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static String getString(String key, Bundle bundle, String defaultValue) {
		return CommonUtil.getValue(key, getBundle(bundle), defaultValue);
	}

	public static ArrayList<String> getStringList(String key, Bundle bundle, ArrayList<String> defaultValue) {
		return CommonUtil.getValueStringList(key, getBundle(bundle), defaultValue);
	}

	public static VariablePeriod getVariablePeriod(String key, Bundle bundle, VariablePeriod defaultValue) {
		String value = CommonUtil.getValue(key, getBundle(bundle), defaultValue == null ? null : defaultValue.name());
		if (value != null && value.length() > 0) {
			return VariablePeriod.fromString(value); // illegal arg exc!
		} else {
			return null;
		}
	}

	private Settings() {
	}

	@Autowired(required = true)
	public void setAuditTrailExcelSettingsBundleBasename(
			String auditTrailExcelSettingsBundleBasename) {
		Settings.auditTrailExcelSettingsBundleBasename = auditTrailExcelSettingsBundleBasename;
		getBundle(auditTrailExcelSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setCourseCertificatePdfSettingsBundleBasename(
			String courseCertificatePdfSettingsBundleBasename) {
		Settings.courseCertificatePdfSettingsBundleBasename = courseCertificatePdfSettingsBundleBasename;
		getBundle(courseCertificatePdfSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setCourseParticipantListPdfSettingsBundleBasename(
			String courseParticipantListPdfSettingsBundleBasename) {
		Settings.courseParticipantListPdfSettingsBundleBasename = courseParticipantListPdfSettingsBundleBasename;
		getBundle(courseParticipantListPdfSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setCvPdfSettingsBundleBasename(
			String cvPdfSettingsBundleBasename) {
		Settings.cvPdfSettingsBundleBasename = cvPdfSettingsBundleBasename;
		getBundle(cvPdfSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setEcrfPdfSettingsBundleBasename(
			String ecrfPdfSettingsBundleBasename) {
		Settings.ecrfPdfSettingsBundleBasename = ecrfPdfSettingsBundleBasename;
		getBundle(ecrfPdfSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setInquiriesPdfSettingsBundleBasename(
			String inquiriesPdfSettingsBundleBasename) {
		Settings.inquiriesPdfSettingsBundleBasename = inquiriesPdfSettingsBundleBasename;
		getBundle(inquiriesPdfSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setInventoryBookingsExcelSettingsBundleBasename(
			String inventoryBookingsExcelSettingsBundleBasename) {
		Settings.inventoryBookingsExcelSettingsBundleBasename = inventoryBookingsExcelSettingsBundleBasename;
		getBundle(inventoryBookingsExcelSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setJournalExcelSettingsBundleBasename(
			String journalExcelSettingsBundleBasename) {
		Settings.journalExcelSettingsBundleBasename = journalExcelSettingsBundleBasename;
		getBundle(journalExcelSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setProbandLetterPdfSettingsBundleBasename(
			String probandLetterPdfSettingsBundleBasename) {
		Settings.probandLetterPdfSettingsBundleBasename = probandLetterPdfSettingsBundleBasename;
		getBundle(probandLetterPdfSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setProbandListEntryTagsPdfSettingsBundleBasename(
			String probandListEntryTagsPdfSettingsBundleBasename) {
		Settings.probandListEntryTagsPdfSettingsBundleBasename = probandListEntryTagsPdfSettingsBundleBasename;
		getBundle(probandListEntryTagsPdfSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setProbandListExcelSettingsBundleBasename(
			String probandListExcelSettingsBundleBasename) {
		Settings.probandListExcelSettingsBundleBasename = probandListExcelSettingsBundleBasename;
		getBundle(probandListExcelSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setReimbursementsExcelSettingsBundleBasename(
			String reimbursementsExcelSettingsBundleBasename) {
		Settings.reimbursementsExcelSettingsBundleBasename = reimbursementsExcelSettingsBundleBasename;
		getBundle(reimbursementsExcelSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setReimbursementsPdfSettingsBundleBasename(
			String reimbursementsPdfSettingsBundleBasename) {
		Settings.reimbursementsPdfSettingsBundleBasename = reimbursementsPdfSettingsBundleBasename;
		getBundle(reimbursementsPdfSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setSearchResultExcelSettingsBundleBasename(
			String searchResultExcelSettingsBundleBasename) {
		Settings.searchResultExcelSettingsBundleBasename = searchResultExcelSettingsBundleBasename;
		getBundle(searchResultExcelSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setSettingsBundleBasename(
			String settingsBundleBasename) {
		Settings.settingsBundleBasename = settingsBundleBasename;
		getBundle(settingsBundleBasename);
	}

	@Autowired(required = true)
	public void setTeamMembersExcelSettingsBundleBasename(
			String teamMembersExcelSettingsBundleBasename) {
		Settings.teamMembersExcelSettingsBundleBasename = teamMembersExcelSettingsBundleBasename;
		getBundle(teamMembersExcelSettingsBundleBasename);
	}

	@Autowired(required = true)
	public void setTrustedHostsBundleBasename(
			String trustedHostsBundleBasename) {
		Settings.trustedHostsBundleBasename = trustedHostsBundleBasename;
		getBundle(trustedHostsBundleBasename);
	}

	@Autowired(required = true)
	public void setVisitScheduleExcelSettingsBundleBasename(
			String visitScheduleExcelSettingsBundleBasename) {
		Settings.visitScheduleExcelSettingsBundleBasename = visitScheduleExcelSettingsBundleBasename;
		getBundle(visitScheduleExcelSettingsBundleBasename);
	}
}
