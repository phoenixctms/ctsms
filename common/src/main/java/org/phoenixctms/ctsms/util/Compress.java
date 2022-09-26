package org.phoenixctms.ctsms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

//https://stackoverflow.com/questions/15968883/how-to-zip-a-folder-itself-using-java
public class Compress {

	public static final String ZIP_MIMETYPE_STRING = "application/zip";
	private List<String> fileList;

	public Compress() {
		fileList = new ArrayList<String>();
	}

	public void zipDirectory(String folder, String zipFile) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(zipFile);
			zipDirectory(folder, fos);
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	public byte[] zipDirectory(String folder) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		zipDirectory(folder, bos);
		return bos.toByteArray();
	}

	public void zipDirectory(String folder, OutputStream fos) throws IOException {
		generateFileList(folder);
		byte[] buffer = new byte[1024];
		String source = new File(folder).getName();
		ZipOutputStream zos = null;
		try {
			//fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);
			//System.out.println("Output to Zip : " + zipFile);
			Iterator<String> it = fileList.iterator();
			while (it.hasNext()) {
				String file = it.next();
				//System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(source + File.separator + file);
				zos.putNextEntry(ze);
				FileInputStream in = null;
				try {
					in = new FileInputStream(folder + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} finally {
					try {
						in.close();
					} catch (Exception e) {
					}
				}
			}
			zos.closeEntry();
			//System.out.println("Folder successfully compressed");
			//        } catch (IOException ex) {
			//            ex.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void generateFileList(String folder) {
		fileList.clear();
		generateFileList(folder, new File(folder));
	}

	private void generateFileList(String folder, File node) {
		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(folder, node.toString()));
		}
		if (node.isDirectory()) {
			String[] files = node.list();
			for (int i = 0; i < files.length; i++) {
				generateFileList(folder, new File(node, files[i]));
			}
		}
	}

	private String generateZipEntry(String folder, String file) {
		return file.substring(folder.length() + 1, file.length());
	}
}