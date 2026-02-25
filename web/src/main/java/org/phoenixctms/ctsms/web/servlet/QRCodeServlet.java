package org.phoenixctms.ctsms.web.servlet;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.GetParamNames;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeServlet extends FileServletBase {

	private final static ErrorCorrectionLevel DEFAULT_ERROR_CORRECTION_LEVEL = ErrorCorrectionLevel.L;
	private final static int DEFAULT_WIDTH = 100;
	private final static int DEFAULT_HEIGHT = 100;
	private final static int DEFAULT_MARGIN = 4;
	private final static Color BG_COLOR = Color.WHITE;
	private final static Color FG_COLOR = Color.BLACK;
	private final static String FILE_NAME = "qrcode." + CommonUtil.PNG_FILENAME_EXTENSION;

	@Override
	protected FileStream createFileStream(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ErrorCorrectionLevel errorCorrectionLevel = DEFAULT_ERROR_CORRECTION_LEVEL;
		int margin = DEFAULT_MARGIN;
		try {
			String labelData[] = request.getParameter(GetParamNames.QR_CODE_CHLD.toString()).trim().split("\\s*\\|\\s*", 2);
			errorCorrectionLevel = ErrorCorrectionLevel.valueOf(labelData[0]);
			margin = Integer.parseInt(labelData[1]);
		} catch (Exception e) {
		}
		int width = DEFAULT_WIDTH;
		int height = DEFAULT_HEIGHT;
		try {
			String size[] = request.getParameter(GetParamNames.QR_CODE_CHS.toString()).trim().split("\\s*x\\s*", 2);
			width = Integer.parseInt(size[0]);
			height = Integer.parseInt(size[1]);
		} catch (Exception e) {
		}
		String text = request.getParameter(GetParamNames.QR_CODE_CHL.toString());
		final byte[] data = CommonUtil.generateQRCodeImage(text, width, height, margin, BG_COLOR, FG_COLOR, errorCorrectionLevel);
		final Long fileSize = (data != null ? (long) data.length : null);
		return new FileStream() {

			@Override
			public String getFileName() {
				return FILE_NAME;
			}

			@Override
			public Long getFileSize() {
				return fileSize;
			}

			@Override
			public String getMimeType() {
				return CommonUtil.PNG_MIMETYPE_STRING;
			}

			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(data);
			}

			@Override
			public boolean isNotFound() {
				return data == null;
			}
		};
	}
}
