package org.phoenixctms.ctsms.executable.csv;

import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.MimeType;
import org.phoenixctms.ctsms.domain.MimeTypeDao;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.fileprocessors.csv.LineProcessor;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class MimeTypeLineProcessor extends LineProcessor {

	private final static int MIME_TYPE_COLUMN_INDEX = 0;
	private final static int FILE_EXTENSION_COLUMN_INDEX = 1;
	private final static String DEFAULT_FILE_EXTENSION_SEPARATOR_PATTERN = " ";

	private static void updateMimeType(MimeType m, String mimeType, String fileNameExtensions) {
		m.setFileNameExtensions(fileNameExtensions);
		m.setNodeStyleClass("ctsms-mimetype-" + mimeType.replaceAll("[^a-zA-Z0-9-]", "-"));
		m.setImage(mimeType.startsWith("image/"));
	}

	@Autowired
	protected MimeTypeDao mimeTypeDao;
	private FileModule module;
	private int mimeTypeColumnIndex;
	private int fileExtensionColumnIndex;
	private Pattern fileExtensionSeparatorRegexp;

	public MimeTypeLineProcessor() {
		super();
	}

	private MimeType createUpdateMimeType(FileModule module, String mimeType, String fileNameExtensions) {
		Iterator<MimeType> it = mimeTypeDao.findByMimeTypeModule(mimeType, module).iterator();
		MimeType m;
		if (it.hasNext()) {
			m = it.next();
			updateMimeType(m, mimeType, fileNameExtensions);
			mimeTypeDao.update(m);
		} else {
			m = MimeType.Factory.newInstance();
			m.setModule(module);
			m.setMimeType(mimeType);
			updateMimeType(m, mimeType, fileNameExtensions);
			m = mimeTypeDao.create(m);
		}
		return m;
	}

	private String getFileExtension(String[] values) {
		return values[fileExtensionColumnIndex];
	}

	public int getFileExtensionColumnIndex() {
		return fileExtensionColumnIndex;
	}

	public String getFileExtensionSeparatorRegexpPattern() {
		return fileExtensionSeparatorRegexp.pattern();
	}

	private String getMimeType(String[] values) {
		return values[mimeTypeColumnIndex];
	}

	public int getMimeTypeColumnIndex() {
		return mimeTypeColumnIndex;
	}

	public FileModule getModule() {
		return module;
	}

	@Override
	public void init() {
		super.init();
		mimeTypeColumnIndex = MIME_TYPE_COLUMN_INDEX;
		fileExtensionColumnIndex = FILE_EXTENSION_COLUMN_INDEX;
		this.setFileExtensionSeparatorRegexpPattern(DEFAULT_FILE_EXTENSION_SEPARATOR_PATTERN);
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getMimeType(values))
				.toHashCode();
	}

	@Override
	public void postProcess() {
	}

	@Override
	protected int processLine(String[] values, long lineNumber) {
		String[] extensions;
		if (fileExtensionSeparatorRegexp != null) {
			extensions = fileExtensionSeparatorRegexp.split(getFileExtension(values), -1);
		} else {
			extensions = new String[1];
			extensions[0] = getFileExtension(values);
		}
		StringBuilder fileNameExtensions = new StringBuilder();
		for (int i = 0; i < extensions.length; i++) {
			if (extensions[i].length() > 0) {
				if (CommonUtil.FILE_EXTENSION_REGEXP_MODE) {
					if (fileNameExtensions.length() > 0) {
						fileNameExtensions.append("|");
						fileNameExtensions.append(extensions[i]);
					} else {
						fileNameExtensions.append(extensions[i]);
					}
				} else {
					if (fileNameExtensions.length() > 0) {
						fileNameExtensions.append(";*.");
						fileNameExtensions.append(extensions[i]);
					} else {
						fileNameExtensions.append("*.");
						fileNameExtensions.append(extensions[i]);
					}
				}
			} else {
				jobOutput.println("line " + lineNumber + ": zero-length file extension");
			}
		}
		createUpdateMimeType(module, getMimeType(values), fileNameExtensions.toString());
		return 1;
	}

	public void setFileExtensionColumnIndex(int fileExtensionColumnIndex) {
		this.fileExtensionColumnIndex = fileExtensionColumnIndex;
	}

	public void setFileExtensionSeparatorRegexpPattern(String fileExtensionSeparatorRegexpPattern) {
		this.fileExtensionSeparatorRegexp = Pattern.compile(fileExtensionSeparatorRegexpPattern);
	}

	public void setMimeTypeColumnIndex(int mimeTypeColumnIndex) {
		this.mimeTypeColumnIndex = mimeTypeColumnIndex;
	}

	public void setModule(FileModule module) {
		this.module = module;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long lineNumber) {
		if (CommonUtil.isEmptyString(getMimeType(values))) {
			jobOutput.println("line " + lineNumber + ": empty mime type name");
			return false;
		}
		if (CommonUtil.isEmptyString(getFileExtension(values))) {
			jobOutput.println("line " + lineNumber + ": empty file extensions");
			return false;
		}
		return true;
	}
}
