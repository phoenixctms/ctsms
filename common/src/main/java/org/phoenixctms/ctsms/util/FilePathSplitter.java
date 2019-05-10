package org.phoenixctms.ctsms.util;

import java.text.MessageFormat;

public class FilePathSplitter {

	private String fileName;
	private String name;
	private String directory;
	private String extension;

	public FilePathSplitter() {
	}

	public FilePathSplitter(String filePath) {
		splitFilePath(filePath);
	}

	public String getDirectory() {
		return directory;
	}

	public String getExtension() {
		return extension;
	}

	public String getFileName() {
		return fileName;
	}

	public String getName() {
		return name;
	}

	public String joinFilePath(String fileNameFormat) {
		return joinFilePath("{0}", fileNameFormat);
	}

	public String joinFilePath(String directoryFormat, String fileNameFormat) {
		return (new java.io.File(
				MessageFormat.format(directoryFormat, directory),
				MessageFormat.format(fileNameFormat, name, extension))).toString();
	}

	public void splitFilePath(String filePath) {
		if (filePath != null && filePath.length() > 0) {
			java.io.File file = new java.io.File(filePath);
			fileName = file.getName();
			directory = file.getParent();
		} else {
			fileName = null;
			directory = null;
		}
		name = null;
		extension = null;
		if (fileName != null && fileName.length() > 0) {
			int pos = fileName.lastIndexOf('.');
			if (pos > 0 && pos < fileName.length()) {
				extension = fileName.substring(pos + 1);
				name = fileName.substring(0, pos);
			}
		}
	}
}
