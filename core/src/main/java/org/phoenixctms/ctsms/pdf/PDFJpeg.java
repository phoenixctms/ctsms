package org.phoenixctms.ctsms.pdf;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.Image;
import org.phoenixctms.ctsms.vo.InputFieldImageVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class PDFJpeg extends PDJpeg {

	private static final int DEFAULT_USER_SPACE_UNIT_DPI = 72;
	private final static Color IMAGE_BG_COLOR = Color.WHITE;
	private final static ErrorCorrectionLevel QRCODE_ERROR_CORRECTION_LEVEL = ErrorCorrectionLevel.L;
	private final static int QRCODE_DEFAULT_WIDTH = 100;
	private final static int QRCODE_DEFAULT_HEIGHT = 100;
	private final static int QRCODE_DEFAULT_MARGIN = 4;
	private final static long FILE_IMAGE_MAX_PIXELS = 4096L * 4096L;

	public static Image createImage(byte[] data, org.phoenixctms.ctsms.enumeration.Color bgColor) {
		if (data != null && data.length > 0) {
			try {
				return new Image(data, bgColor == null ? IMAGE_BG_COLOR : bgColor);
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static Image createImage(int width, int height, org.phoenixctms.ctsms.enumeration.Color bgColor) {
		if (width > 0 && height > 0) {
			try {
				return new Image(width, height, bgColor == null ? IMAGE_BG_COLOR : bgColor);
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static Image cropImage(Image image, float width, float height, int dpi) {
		if (image != null) {
			try {
				image.crop(pointsToPixel(width, dpi), pointsToPixel(height, dpi));
			} catch (Exception e) {
			}
		}
		return image;
	}

	private static float pixelToPoints(int pixel, int dpi) {
		return (pixel) * ((float) DEFAULT_USER_SPACE_UNIT_DPI / (float) dpi);
	}

	private static int pointsToPixel(float points, int dpi) {
		return (int) (points * ((float) dpi / (float) DEFAULT_USER_SPACE_UNIT_DPI));
	}

	public static PDFJpeg prepareImage(PDDocument doc, Image image, int compressionQualityPercent, int dpi) {
		if (doc != null && image != null) {
			try {
				return new PDFJpeg(doc, image, compressionQualityPercent / 100.0f, dpi);
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static PDFJpeg prepareScaledImage(PDDocument doc, byte[] data, float width, float height, int compressionQualityPercent, int dpi,
			org.phoenixctms.ctsms.enumeration.Color bgColor) {
		return prepareScaledImage(doc, data, width, height, 0.0f, compressionQualityPercent, dpi, bgColor);
	}

	public static PDFJpeg prepareScaledImage(PDDocument doc, byte[] data, float width, float height, float maxWidth, int compressionQualityPercent, int dpi,
			org.phoenixctms.ctsms.enumeration.Color bgColor) {
		Image image = createImage(data, bgColor);
		if (image != null && image.getImage() != null && maxWidth > 0.0f) {
			// the width the image would get, and if too wide, scale it down to the max width, keeping the aspect ratio:
			float scaledWidth = width > 0.0f ? width
					: (height > 0.0f ? height * image.getImage().getWidth() / image.getImage().getHeight() : pixelToPoints(image.getImage().getWidth(), dpi));
			if (scaledWidth > maxWidth) {
				height = width > 0.0f && height > 0.0f ? height * maxWidth / width : 0.0f;
				width = maxWidth;
			}
		}
		scaleImage(image, width, height, dpi);
		return prepareImage(doc, image, compressionQualityPercent, dpi);
	}

	public static PDFJpeg prepareQRCodeImage(PDDocument doc, String text, int compressionQualityPercent, int dpi,
			org.phoenixctms.ctsms.enumeration.Color bgColor, org.phoenixctms.ctsms.enumeration.Color fgColor) {
		return prepareQRCodeImage(doc, text, QRCODE_DEFAULT_WIDTH, QRCODE_DEFAULT_HEIGHT, QRCODE_DEFAULT_MARGIN, compressionQualityPercent, dpi, bgColor, fgColor);
	}

	public static PDFJpeg prepareQRCodeImage(PDDocument doc, String text, int width, int height, int margin, int compressionQualityPercent, int dpi,
			org.phoenixctms.ctsms.enumeration.Color bgColor, org.phoenixctms.ctsms.enumeration.Color fgColor) {
		Image image = createImage(
				CommonUtil.generateQRCodeImage(text, width, height, margin, bgColor != null ? CommonUtil.convertColor(bgColor) : null, CommonUtil.convertColor(fgColor),
						QRCODE_ERROR_CORRECTION_LEVEL),
				bgColor);
		return prepareImage(doc, image, compressionQualityPercent, dpi);
	}

	public static PDFJpeg prepareSketchImage(PDDocument doc, InputFieldOutVO field,
			InputFieldImageVO inputFieldImage,
			byte[] inkValue,
			boolean renderRegions,
			boolean renderValue,
			Float maxWidth, int compressionQualityPercent, int dpi, org.phoenixctms.ctsms.enumeration.Color bgColor) {
		Image image;
		int width = CommonUtil.safeLongToInt(inputFieldImage.getWidth());
		int height = CommonUtil.safeLongToInt(inputFieldImage.getHeight());
		if (inputFieldImage.getHasImage()) {
			image = createImage(inputFieldImage.getDatas(), bgColor);
		} else {
			image = createImage(width, height, bgColor);
		}
		ArrayList<byte[]> inkValues = new ArrayList<byte[]>();
		if (renderRegions) {
			Iterator<InputFieldSelectionSetValueOutVO> it = field.getSelectionSetValues().iterator();
			while (it.hasNext()) {
				inkValues.add(it.next().getInkRegions());
			}
		}
		if (renderValue && inkValue != null) {
			inkValues.add(inkValue);
		}
		if (image != null) {
			if (inkValues.size() > 0) { // speedup..
				try {
					image.drawInk(inkValues.toArray(new byte[][] {}));
				} catch (Exception e) {
				}
			}
			try {
				image.crop(width, height);
			} catch (Exception e) {
			}
			if (maxWidth != null && pixelToPoints(image.getImage().getWidth(), dpi) > maxWidth) {
				scaleImage(image, maxWidth, 0.0f, dpi);
			}
		}
		return prepareImage(doc, image, compressionQualityPercent, dpi);
	}

	public static PDFJpeg prepareFileImage(PDDocument doc, byte[] data, float maxWidth, float maxHeight, int compressionQualityPercent, int dpi,
			org.phoenixctms.ctsms.enumeration.Color bgColor) {
		Image image = null;
		if (data != null && data.length > 0) {
			try {
				// uploaded images can have any dimensions, so never decode them fully:
				image = new Image(data, maxWidth > 0.0f ? pointsToPixel(maxWidth, dpi) : 0, maxHeight > 0.0f ? pointsToPixel(maxHeight, dpi) : 0,
						FILE_IMAGE_MAX_PIXELS, bgColor == null ? IMAGE_BG_COLOR : bgColor);
			} catch (Exception e) {
			}
		}
		if (image != null && image.getImage() != null) {
			float width = pixelToPoints(image.getImage().getWidth(), dpi);
			float height = pixelToPoints(image.getImage().getHeight(), dpi);
			if (width > 0.0f && height > 0.0f) {
				float factor = Math.min(maxWidth > 0.0f ? maxWidth / width : 1.0f, maxHeight > 0.0f ? maxHeight / height : 1.0f);
				if (factor < 1.0f) {
					scaleImage(image, width * factor, height * factor, dpi);
				}
			}
			return prepareImage(doc, image, compressionQualityPercent, dpi);
		}
		return null;
	}

	public static Image scaleImage(Image image, float width, float height, int dpi) {
		if (image != null) {
			try {
				image.scale(pointsToPixel(width, dpi), pointsToPixel(height, dpi));
			} catch (Exception e) {
			}
		}
		return image;
	}

	private int dpi;

	private PDFJpeg(PDDocument doc, BufferedImage bi) throws IOException {
		super(doc, bi);
	}

	private PDFJpeg(PDDocument doc, BufferedImage bi, float compressionQuality) throws IOException {
		super(doc, bi, compressionQuality);
	}

	private PDFJpeg(PDDocument doc, Image image, float compressionQuality, int dpi)
			throws IOException {
		this(doc, image.getImage(), compressionQuality);
		this.dpi = dpi <= 0 ? DEFAULT_USER_SPACE_UNIT_DPI : dpi;
	}

	private PDFJpeg(PDDocument doc, InputStream is) throws IOException {
		super(doc, is);
	}

	private PDFJpeg(PDStream jpeg) {
		super(jpeg);
	}

	public float getHeightPoints() {
		return pixelToPoints(getHeight(), dpi);
	}

	public float getWidthPoints() {
		return pixelToPoints(getWidth(), dpi);
	}

	public void setHeightPoints(float height) {
		setHeight(pointsToPixel(height, dpi));
	}

	public void setWidthPoints(float width) {
		setWidth(pointsToPixel(width, dpi));
	}
}
