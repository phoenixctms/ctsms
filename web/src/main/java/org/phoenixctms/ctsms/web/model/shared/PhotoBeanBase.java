package org.phoenixctms.ctsms.web.model.shared;

import java.io.ByteArrayInputStream;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.Image;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

public abstract class PhotoBeanBase extends ManagedBeanBase {

	private final static String CAPTURE_MIME_TYPE = "image/png";
	private final static String CAPTURE_FILE_NAME_EXTENSION = "png";
	private final static String PREVIEW_FORMAT = "png";
	private final static String PREVIEW_MIME_TYPE = "image/png";
	private final static org.phoenixctms.ctsms.enumeration.Color PREVIEW_BG_COLOR = org.phoenixctms.ctsms.enumeration.Color.WHITE;
	private final static int CAPTURE_WIDTH = 320;
	private final static int CAPTURE_HEIGHT = 240;

	private static byte[] scale(byte[] imageData) {
		if (imageData == null || imageData.length == 0) {
			return null;
		}
		try {
			Image image = new Image(imageData, PREVIEW_BG_COLOR);
			image.scale(CAPTURE_WIDTH, 0);
			return image.convert(PREVIEW_FORMAT);
			// return CommonUtil.scaleConvertImage(image, CAPTURE_WIDTH, 0, PREVIEW_FORMAT, PREVIEW_BGCOLOR);
		} catch (Exception e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			return null;
		}
	}

	private String uuid;
	private String allowTypes;
	private Integer uploadSizeLimit;

	public PhotoBeanBase() {
		super();
		uuid = CommonUtil.generateUUID();
	}

	public void clearImage(ActionEvent e) {
		setFileName(null);
		setMimeType(null);
		setData(null);
		setTimestamp(null);
		storeImage();
	}

	public String getAllowTypes() {
		return allowTypes;
	}

	protected abstract byte[] getData();

	public String getFileDownloadLinkLabel() {
		if (isHasPhoto()) {
			return getFileName();
		} else {
			return Messages.getString(MessageCodes.NO_FILE_LINK_LABEL);
		}
	}

	protected abstract FileModule getFileModule();

	public abstract String getFileName();

	public StreamedContent getFileStreamedContent() throws Exception {
		if (isHasPhoto()) {
			return new DefaultStreamedContent(new ByteArrayInputStream(getData()), getMimeType(), getFileName());
		}
		return null;
	}

	protected abstract String getMimeType();

	public abstract Date getTimestamp();

	public Integer getUploadSizeLimit() {
		return uploadSizeLimit;
	}

	public String getUuid() {
		return uuid;
	}

	public void handleFileUpload(FileUploadEvent event) {
		UploadedFile uploadedFile = event.getFile();
		setFileName(uploadedFile.getFileName());
		setMimeType(uploadedFile.getContentType());
		setData(uploadedFile.getContents());
		setTimestamp(new Date());
		storeImage();
		Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.UPLOAD_OPERATION_SUCCESSFUL);
	}

	protected void initSets() {
		allowTypes = WebUtil.getAllowedFileExtensionsPattern(getFileModule(), true);
		uploadSizeLimit = loadUploadSizeLimit();
		storeImage();
		initSpecificSets();
	}

	protected abstract void initSpecificSets();

	public boolean isHasPhoto() {
		byte[] data = getData();
		if (data != null && data.length > 0) {
			return true;
		}
		return false;
	}

	public abstract boolean isInputVisible();

	protected abstract Integer loadUploadSizeLimit();

	public void onCapture(CaptureEvent captureEvent) {
		Date now = new Date();
		setFileName(Messages.getMessage(MessageCodes.WEBCAM_FILE_NAME, DateUtil.getDigitsOnlyDateTimeFormat().format(now), CAPTURE_FILE_NAME_EXTENSION));
		setMimeType(CAPTURE_MIME_TYPE);
		setData(captureEvent.getData());
		setTimestamp(now);
		storeImage();
	}

	protected abstract void setData(byte[] data);

	protected abstract void setFileName(String fileName);

	protected abstract void setMimeType(String mimeType);

	protected abstract void setTimestamp(Date now);

	private void storeImage() {
		WebUtil.getSessionScopeBean().putImage(uuid, scale(getData()), PREVIEW_MIME_TYPE);
	}
}
