package org.phoenixctms.ctsms.util;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.service.course.CourseService;
import org.phoenixctms.ctsms.service.inventory.InventoryService;
import org.phoenixctms.ctsms.service.massmail.MassMailService;
import org.phoenixctms.ctsms.service.proband.ProbandService;
import org.phoenixctms.ctsms.service.shared.FileService;
import org.phoenixctms.ctsms.service.shared.HyperlinkService;
import org.phoenixctms.ctsms.service.shared.InputFieldService;
import org.phoenixctms.ctsms.service.shared.JobService;
import org.phoenixctms.ctsms.service.shared.JournalService;
import org.phoenixctms.ctsms.service.shared.SearchService;
import org.phoenixctms.ctsms.service.staff.StaffService;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.service.user.UserService;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CriteriaInVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionOutVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.vo.CriterionRestrictionVO;
import org.phoenixctms.ctsms.vo.CriterionTieVO;

public final class ExecUtil {

	public static final String CSV_FILENAME_EXTENSION = "csv";
	public static final String CSV_MIMETYPE_STRING = "text/csv";
	public final static ArrayList<Class> AUTHORIZED_SERVICE_CLASSES = new ArrayList<Class>();
	static {
		AUTHORIZED_SERVICE_CLASSES.add(InventoryService.class);
		AUTHORIZED_SERVICE_CLASSES.add(StaffService.class);
		AUTHORIZED_SERVICE_CLASSES.add(CourseService.class);
		AUTHORIZED_SERVICE_CLASSES.add(TrialService.class);
		AUTHORIZED_SERVICE_CLASSES.add(ProbandService.class);
		AUTHORIZED_SERVICE_CLASSES.add(UserService.class);
		AUTHORIZED_SERVICE_CLASSES.add(InputFieldService.class);
		AUTHORIZED_SERVICE_CLASSES.add(MassMailService.class);
		AUTHORIZED_SERVICE_CLASSES.add(FileService.class);
		AUTHORIZED_SERVICE_CLASSES.add(HyperlinkService.class);
		AUTHORIZED_SERVICE_CLASSES.add(JobService.class);
		AUTHORIZED_SERVICE_CLASSES.add(JournalService.class);
		AUTHORIZED_SERVICE_CLASSES.add(SearchService.class);
	}
	private final static Detector detector = new DefaultDetector(); // All build-in Tika detectors are thread-safe, so it is ok to share the detector globally.

	public static AuthenticationVO authenticationPrompt(Scanner in) throws IOException {
		System.out.print("username:");
		return new AuthenticationVO(in.nextLine(), readPassword(in, "password:"), null, null, "localhost");
	}

	public static boolean confirmationPrompt(Scanner in, String msg) {
		System.out.println(msg);
		System.out.print("type 'yes' to proceed:");
		if ("yes".equals(in.nextLine())) {
			return true;
		} else {
			System.out.println("execution not confirmed");
			return false;
		}
	}

	public static boolean confirmationPrompt(String msg) {
		Scanner in = getScanner();
		boolean result = confirmationPrompt(in, msg);
		in.close();
		return result;
	}

	public static void criteriaOutToIn(CriteriaInVO criteriaIn, Set<CriterionInVO> criterionsIn, CriteriaOutVO out) {
		Collection<CriterionOutVO> criterionVOs = out.getCriterions();
		criteriaIn.setCategory(out.getCategory());
		criteriaIn.setId(out.getId());
		criteriaIn.setVersion(out.getVersion());
		criteriaIn.setLabel(out.getLabel());
		criteriaIn.setComment(out.getComment());
		criteriaIn.setModule(out.getModule());
		criteriaIn.setLoadByDefault(out.getLoadByDefault());
		criterionsIn.clear();
		Iterator<CriterionOutVO> it = criterionVOs.iterator();
		while (it.hasNext()) {
			CriterionInVO criterionIn = new CriterionInVO();
			criterionOutToIn(criterionIn, it.next());
			criterionsIn.add(criterionIn);
		}
	}

	public static void criterionOutToIn(CriterionInVO criterionIn, CriterionOutVO criterionOut) {
		CriterionPropertyVO criterionPropertyVO = criterionOut.getProperty();
		CriterionRestrictionVO criterionRestrictionVO = criterionOut.getRestriction();
		CriterionTieVO criterionTieVO = criterionOut.getTie();
		CriteriaOutVO criteriaVO = criterionOut.getCriteria();
		criterionIn.setBooleanValue(criterionOut.getBooleanValue());
		criterionIn.setDateValue(criterionOut.getDateValue());
		criterionIn.setTimeValue(criterionOut.getTimeValue());
		criterionIn.setFloatValue(criterionOut.getFloatValue());
		criterionIn.setLongValue(criterionOut.getLongValue());
		criterionIn.setPosition(criterionOut.getPosition());
		criterionIn.setPropertyId(criterionPropertyVO == null ? null : criterionPropertyVO.getId());
		criterionIn.setRestrictionId(criterionRestrictionVO == null ? null : criterionRestrictionVO.getId());
		criterionIn.setStringValue(criterionOut.getStringValue());
		criterionIn.setTieId(criterionTieVO == null ? null : criterionTieVO.getId());
		criterionIn.setTimestampValue(criterionOut.getTimestampValue());
	}

	public static Long departmentL10nKeyToId(String departmentL10nKey, DepartmentDao departmentDao, JobOutput jobOutput) throws Exception {
		Department department;
		if (!CommonUtil.isEmptyString(departmentL10nKey)) {
			department = departmentDao.searchUniqueNameL10nKey(departmentL10nKey);
			if (department == null) {
				jobOutput.println("no matching department for l10n name " + departmentL10nKey);
			}
		} else {
			department = null;
		}
		return department == null ? null : department.getId();
	}

	public static String formatFileName(String fileName, String fileNameFormat) {
		FilePathSplitter filePath = new FilePathSplitter(fileName);
		return filePath.joinFilePath(fileNameFormat);
	}

	public static String getMimeType(byte[] data, String fileName) throws Throwable {
		TikaInputStream tikaStream = null;
		Metadata metadata = new Metadata();
		metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, fileName);
		try {
			tikaStream = TikaInputStream.get(data, metadata);
			return detector.detect(tikaStream, metadata).toString();
		} catch (Throwable t) {
			throw t;
		} finally {
			if (tikaStream != null) {
				try {
					tikaStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static String getMimeType(File file) throws Throwable {
		TikaInputStream tikaStream = null;
		Metadata metadata = new Metadata();
		metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());
		try {
			tikaStream = TikaInputStream.get(file, metadata);
			return detector.detect(tikaStream, metadata).toString();
		} catch (Throwable t) {
			throw t;
		} finally {
			if (tikaStream != null) {
				try {
					tikaStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static Scanner getScanner() {
		return new Scanner(System.in);
	}

	public static void lockProcess(String lockFileName) {
		if (!CommonUtil.isEmptyString(lockFileName)) {
			// https://stackoverflow.com/questions/7036108/prevent-launching-multiple-instances-of-a-java-application
			File file = new File(lockFileName);
			try {
				FileChannel fc = FileChannel.open(file.toPath(),
						StandardOpenOption.CREATE,
						StandardOpenOption.WRITE);
				FileLock lock = fc.tryLock();
				if (lock == null) {
					throw new RuntimeException("another instance is running");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static String readPassword(Scanner in, String prompt) throws IOException {
		System.out.print(prompt);
		return in.nextLine();
	}

	public static String removeExtension(String fileName) {
		if (fileName != null) {
			return fileName.replaceFirst("[.][^.]+$", "");
		}
		return null;
	}

	public static String sanitizeEncoding(String encoding, JobOutput jobOutput) {
		if (!CommonUtil.isEmptyString(encoding)) {
			jobOutput.println("using " + encoding + " encoding");
			return encoding;
		} else {
			String defaultEncoding = ExecSettings.getString(ExecSettingCodes.DEFAULT_ENCODING, ExecDefaultSettings.DEFAULT_ENCODING);
			jobOutput.println("no encoding specified, using " + defaultEncoding);
			return defaultEncoding;
		}
	}

	private ExecUtil() {
	}
}
