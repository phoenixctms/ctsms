package org.phoenixctms.ctsms.fileprocessors.xls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.phoenixctms.ctsms.util.CommonUtil;

/**
 * A setup spreadsheet (.xls) bundled with the files its rows reference (e.g. sketch background images) in a zip archive:
 *
 * <pre>
 * setup.zip
 * +-- setup.xls
 * +-- images/&lt;md5 of content&gt;/body.png
 * </pre>
 *
 * Rows reference the files by their path within the archive (e.g. "images/&lt;md5&gt;/body.png"). The last path
 * segment is the original file name.
 */
public final class XlsArchive {

	public static final String ZIP_FILENAME_EXTENSION = "zip";
	public static final String XLS_FILENAME_EXTENSION = "xls";
	public static final String IMAGES_FOLDER = "images";
	private static final String PATH_SEPARATOR = "/";
	private static final String MACOSX_FOLDER = "__MACOSX/";
	private static final int BUFFER_SIZE = 8192;
	private static final long DEFAULT_SIZE_LIMIT = 256l * 1024l * 1024l;

	public static boolean isZip(byte[] data) {
		return data != null && data.length >= 4 && data[0] == 'P' && data[1] == 'K' && data[2] == 3 && data[3] == 4;
	}

	public static String normalizePath(String path) {
		if (path == null) {
			return null;
		}
		String result = CommonUtil.sanitizeFilePath(path.trim());
		while (result.startsWith("." + PATH_SEPARATOR)) {
			result = result.substring(2);
		}
		return result;
	}

	public static XlsArchive read(byte[] data) throws IOException {
		return read(data, DEFAULT_SIZE_LIMIT);
	}

	public static XlsArchive read(byte[] data, long sizeLimit) throws IOException {
		LinkedHashMap<String, byte[]> entries = new LinkedHashMap<String, byte[]>();
		long totalSize = 0l;
		ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(data));
		try {
			ZipEntry entry;
			byte[] block = new byte[BUFFER_SIZE];
			while ((entry = zip.getNextEntry()) != null) {
				String name = normalizePath(entry.getName());
				if (entry.isDirectory() || name.startsWith(MACOSX_FOLDER)) {
					continue;
				}
				if (name.startsWith(PATH_SEPARATOR) || name.matches("^[A-Za-z]:.*") || (PATH_SEPARATOR + name + PATH_SEPARATOR).contains(PATH_SEPARATOR + ".." + PATH_SEPARATOR)) {
					throw new IllegalArgumentException("invalid zip entry path " + entry.getName());
				}
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;
				while ((nRead = zip.read(block, 0, block.length)) != -1) {
					totalSize += nRead;
					if (totalSize > sizeLimit) {
						throw new IllegalArgumentException("zip archive content exceeds " + sizeLimit + " bytes");
					}
					buffer.write(block, 0, nRead);
				}
				if (entries.containsKey(name)) {
					throw new IllegalArgumentException("duplicate zip entry " + name);
				}
				entries.put(name, buffer.toByteArray());
			}
		} finally {
			zip.close();
		}
		return new XlsArchive(stripCommonFolder(entries));
	}

	// archives created by zipping a folder (e.g. Windows "send to compressed folder") put everything below that folder
	private static LinkedHashMap<String, byte[]> stripCommonFolder(LinkedHashMap<String, byte[]> entries) {
		String folder = null;
		Iterator<String> it = entries.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			int pos = name.indexOf(PATH_SEPARATOR);
			if (pos <= 0) {
				return entries;
			}
			String entryFolder = name.substring(0, pos + 1);
			if (folder == null) {
				folder = entryFolder;
			} else if (!folder.equals(entryFolder)) {
				return entries;
			}
		}
		if (folder == null) {
			return entries;
		}
		LinkedHashMap<String, byte[]> result = new LinkedHashMap<String, byte[]>();
		Iterator<Entry<String, byte[]>> entryIt = entries.entrySet().iterator();
		while (entryIt.hasNext()) {
			Entry<String, byte[]> entry = entryIt.next();
			result.put(entry.getKey().substring(folder.length()), entry.getValue());
		}
		return stripCommonFolder(result);
	}

	public static byte[] write(String xlsFileName, byte[] xlsData, Map<String, byte[]> files) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(buffer);
		try {
			putEntry(zip, xlsFileName, xlsData);
			if (files != null) {
				Iterator<Entry<String, byte[]>> it = files.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, byte[]> file = it.next();
					putEntry(zip, file.getKey(), file.getValue());
				}
			}
		} finally {
			zip.close();
		}
		return buffer.toByteArray();
	}

	private static void putEntry(ZipOutputStream zip, String name, byte[] data) throws IOException {
		zip.putNextEntry(new ZipEntry(name));
		if (data != null) {
			zip.write(data);
		}
		zip.closeEntry();
	}

	public static String getXlsFileName(String fileName) {
		return replaceExtension(fileName, XLS_FILENAME_EXTENSION);
	}

	public static String getZipFileName(String fileName) {
		return replaceExtension(fileName, ZIP_FILENAME_EXTENSION);
	}

	private static String replaceExtension(String fileName, String extension) {
		int pos = fileName.lastIndexOf('.');
		int separatorPos = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if (pos > separatorPos + 1) {
			return fileName.substring(0, pos + 1) + extension;
		}
		return fileName + "." + extension;
	}

	private LinkedHashMap<String, byte[]> entries;
	private String xlsFileName;

	private XlsArchive(LinkedHashMap<String, byte[]> entries) {
		this.entries = entries;
		xlsFileName = null;
		Iterator<String> it = entries.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			if (!name.contains(PATH_SEPARATOR) && name.toLowerCase(Locale.ENGLISH).endsWith("." + XLS_FILENAME_EXTENSION)) {
				if (xlsFileName != null) {
					throw new IllegalArgumentException("zip archive contains more than one .xls file (" + xlsFileName + ", " + name + ")");
				}
				xlsFileName = name;
			}
		}
		if (xlsFileName == null) {
			throw new IllegalArgumentException("zip archive contains no .xls file");
		}
	}

	/**
	 * Looks up a file by its path within the archive. If there is no such entry, a plain file name (e.g. "body.png" in a
	 * hand-crafted spreadsheet) is looked up in any folder of the archive (exact case first, then ignoring case).
	 *
	 * @return the file content, or null if not found
	 * @throws IllegalArgumentException if a plain file name matches several entries of different content
	 */
	public byte[] getFile(String path) {
		String name = normalizePath(path);
		if (name == null) {
			return null;
		}
		byte[] data = entries.get(name);
		if (data != null || name.contains(PATH_SEPARATOR)) {
			return data;
		}
		data = findFileByName(name, false);
		return data != null ? data : findFileByName(name, true);
	}

	private byte[] findFileByName(String name, boolean ignoreCase) {
		byte[] result = null;
		StringBuilder matches = new StringBuilder();
		boolean ambiguous = false;
		Iterator<Entry<String, byte[]>> it = entries.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, byte[]> entry = it.next();
			String entryName = entry.getKey().substring(entry.getKey().lastIndexOf(PATH_SEPARATOR) + 1);
			if (ignoreCase ? entryName.equalsIgnoreCase(name) : entryName.equals(name)) {
				if (matches.length() > 0) {
					matches.append(", ");
				}
				matches.append(entry.getKey());
				if (result == null) {
					result = entry.getValue();
				} else if (!Arrays.equals(result, entry.getValue())) {
					ambiguous = true;
				}
			}
		}
		if (ambiguous) {
			throw new IllegalArgumentException("file name " + name + " is ambiguous, use the path within the zip archive (" + matches + ")");
		}
		return result;
	}

	public int getFileCount() {
		return entries.size() - 1;
	}

	public byte[] getXlsData() {
		return entries.get(xlsFileName);
	}

	public String getXlsFileName() {
		return xlsFileName;
	}
}
