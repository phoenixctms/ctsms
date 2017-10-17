package org.phoenixctms.ctsms.web.model;

import java.text.MessageFormat;
import java.util.Date;

import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.MimeTypeVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;

public class FileFolderVO {

	private String folderName;
	private String folderPath;
	private int fileCount;
	private int totalFileCount;
	private int folderCount;
	private Date folderModifiedTimestamp;
	private long size;
	private FileFolderVO parent;
	private final static String FOLDER_STYLECLASS = "ctsms-icon-filefolder";
	private final static String FOLDER_TITLE = "{0} ({1})";

	public FileFolderVO() {
		folderName = "";
		folderPath = CommonUtil.LOGICAL_PATH_SEPARATOR;
		totalFileCount = 0;
		fileCount = 0;
		folderCount = 0;
		size = 0;
		folderModifiedTimestamp = null;
		parent = null;
	}

	public FileFolderVO(String parentLogicalPath, String logicalPath, FileFolderVO parent) {
		folderName = logicalPath.substring(parentLogicalPath.length(), logicalPath.length() - 1);
		folderPath = logicalPath;
		totalFileCount = 0;
		fileCount = 0;
		folderCount = 0;
		size = 0;
		folderModifiedTimestamp = null;
		this.parent = parent;
	}

	public void addSize(long size) {
		this.size += size;
		if (parent != null) {
			parent.addSize(size);
		}
	}

	public Boolean getActive() {
		return null;
	}

	public MimeTypeVO getContentType() {
		MimeTypeVO folderDummyMimeType = new MimeTypeVO();
		folderDummyMimeType.setMimeType(Messages.getString(MessageCodes.FOLDER_LABEL));
		folderDummyMimeType.setNodeStyleClass(FOLDER_STYLECLASS);
		return folderDummyMimeType;
	}

	public int getFileCount() {
		return fileCount;
	}

	public String getFileName() {
		return "";
	}

	public int getFolderCount() {
		return folderCount;
	}

	public String getFolderName() {
		return folderName;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public Long getId() {
		return null;
	}

	public String getLogicalPath() {
		return folderPath;
	}

	public String getLogicalPathBase64() {
		return JsUtil.encodeBase64(folderPath, false);
	}

	public Date getModifiedTimestamp() {
		return folderModifiedTimestamp;
	}

	public UserOutVO getModifiedUser() {
		return null;
	}

	public Boolean getPublicFile() {
		return null;
	}

	public long getSize() {
		return size;
	}

	public String getTitle() {
		return MessageFormat.format(FOLDER_TITLE, folderName, fileCount);
	}

	public int getTotalFileCount() {
		return totalFileCount;
	}

	public void incrementFileCount() {
		this.fileCount++;
		incrementTotalFileCount();
	}

	public void incrementFolderCount() {
		this.folderCount++;
	}

	private void incrementTotalFileCount() {
		this.totalFileCount++;
		if (parent != null) {
			parent.incrementTotalFileCount();
		}
	}

	public boolean isDecrypted() {
		return true;
	}

	public void resetCounts() {
		fileCount = 0;
		folderCount = 0;
		size = 0;
		folderModifiedTimestamp = null;
	}

	public void updateModifiedTimestamp(Date modifiedTimestamp) {
		if (modifiedTimestamp != null) {
			if (folderModifiedTimestamp == null || modifiedTimestamp.compareTo(folderModifiedTimestamp) > 0) {
				folderModifiedTimestamp = modifiedTimestamp;
				if (parent != null) {
					parent.updateModifiedTimestamp(modifiedTimestamp);
				}
			}
		}
	}
}
