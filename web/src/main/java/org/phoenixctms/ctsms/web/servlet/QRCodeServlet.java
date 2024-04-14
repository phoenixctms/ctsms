package org.phoenixctms.ctsms.web.servlet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.GetParamNames;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeServlet extends FileServletBase {

	private final static ErrorCorrectionLevel DEFAULT_ERROR_CORRECTION_LEVEL = ErrorCorrectionLevel.L;
	private final static int DEFAULT_WIDTH = 100;
	private final static int DEFAULT_HEIGHT = 100;
	private final static int DEFAULT_MARGIN = 4;
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
		final byte[] data;
		final Long fileSize;
		if (!CommonUtil.isEmptyString(text)) {
			Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
			//hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hintMap.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
			hintMap.put(EncodeHintType.MARGIN, margin);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = null;
			try {
				byteMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hintMap);
			} catch (Exception e) {
			}
			if (byteMatrix != null) {
				BufferedImage image = new BufferedImage(byteMatrix.getWidth(), byteMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
				image.createGraphics();
				Graphics2D graphics = (Graphics2D) image.getGraphics();
				graphics.setColor(Color.WHITE);
				graphics.fillRect(0, 0, byteMatrix.getWidth(), byteMatrix.getHeight());
				graphics.setColor(Color.BLACK);
				for (int i = 0; i < byteMatrix.getWidth(); i++) {
					for (int j = 0; j < byteMatrix.getHeight(); j++) {
						if (byteMatrix.get(i, j)) {
							graphics.fillRect(i, j, 1, 1);
						}
					}
				}
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, CommonUtil.PNG_FILENAME_EXTENSION, baos);
				data = baos.toByteArray();
				fileSize = (long) data.length;
			} else {
				data = null;
				fileSize = null;
			}
		} else {
			data = null;
			fileSize = null;
		}
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
