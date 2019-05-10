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

public class PDFJpeg extends PDJpeg {

	// private final static String IMAGE_FORMAT = "jpg";
	private static final int DEFAULT_USER_SPACE_UNIT_DPI = 72; // PDPage.java
	private final static Color IMAGE_BG_COLOR = Color.WHITE;

	// public static PDFJpeg createCropped(byte[] data, float width, float height, int dpi, org.phoenixctms.ctsms.enumeration.Color bgColor) throws Exception {
	// return ImageIO.read(new ByteArrayInputStream(CommonUtil.cropConvertImage(data, pointsToPixel(width, dpi), pointsToPixel(height, dpi), IMAGE_FORMAT,
	// bgColor == null ? IMAGE_BG_COLOR : CommonUtil.convertColor(bgColor))));
	// }
	//
	// public static BufferedImage createScaledImage(byte[] data, float width, float height, int dpi, org.phoenixctms.ctsms.enumeration.Color bgColor) throws Exception {
	// return ImageIO.read(new ByteArrayInputStream(CommonUtil.scaleConvertImage(data, pointsToPixel(width, dpi), pointsToPixel(height, dpi), IMAGE_FORMAT,
	// bgColor == null ? IMAGE_BG_COLOR : CommonUtil.convertColor(bgColor))));
	// }
	//
	// public static BufferedImage scaleImage(Image image, float width, float height, int dpi, org.phoenixctms.ctsms.enumeration.Color bgColor) throws Exception {
	// return ImageIO.read(new ByteArrayInputStream(CommonUtil.scaleConvertImage(data, pointsToPixel(width, dpi), pointsToPixel(height, dpi), IMAGE_FORMAT,
	// bgColor == null ? IMAGE_BG_COLOR : CommonUtil.convertColor(bgColor))));
	// }
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
		// return (float) (((double) pixel) * ((double) DEFAULT_USER_SPACE_UNIT_DPI / (double) dpi));
	}

	private static int pointsToPixel(float points, int dpi) {
		return (int) (points * ((float) dpi / (float) DEFAULT_USER_SPACE_UNIT_DPI));
		// return (int) (((double) points) * ((double) dpi / (double) DEFAULT_USER_SPACE_UNIT_DPI));
	}
	// private static float pointsToPixelF(float points, int dpi) {
	// return (points * ((float) dpi / (float) DEFAULT_USER_SPACE_UNIT_DPI));
	// // return (int) (((double) points) * ((double) dpi / (double) DEFAULT_USER_SPACE_UNIT_DPI));
	// }

	public static PDFJpeg prepareImage(PDDocument doc, Image image, int compressionQualityPercent, int dpi) {
		if (doc != null && image != null) {
			try {
				return new PDFJpeg(doc, image, compressionQualityPercent / 100.0f, dpi);
			} catch (Exception e) {
			}
		}
		return null;
	}

	// public static PDFJpeg prepareCroppedImage(PDDocument doc, byte[] data, float width, float height, int compressionQualityPercent, int dpi,
	// org.phoenixctms.ctsms.enumeration.Color bgColor) {
	// Image image = createImage(data,bgColor);
	// scaleImage(image,width,height,dpi);
	// return prepareImage(doc,image,compressionQualityPercent,dpi);
	// }
	public static PDFJpeg prepareScaledImage(PDDocument doc, byte[] data, float width, float height, int compressionQualityPercent, int dpi,
			org.phoenixctms.ctsms.enumeration.Color bgColor) {
		Image image = createImage(data, bgColor);
		scaleImage(image, width, height, dpi);
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
			image = PDFJpeg.createImage(inputFieldImage.getDatas(), bgColor);
		} else {
			image = PDFJpeg.createImage(width, height, bgColor);
		}
		// if (image != null) {
		ArrayList<byte[]> inkValues = new ArrayList<byte[]>();
		if (renderRegions) {
			Iterator<InputFieldSelectionSetValueOutVO> it = field.getSelectionSetValues().iterator();
			while (it.hasNext()) {
				inkValues.add(it.next().getInkRegions());
			}
			// try {
			// Iterator<InputFieldSelectionSetValueOutVO> it = field.getSelectionSetValues().iterator();
			// while (it.hasNext()) {
			// image.drawInk(it.next().getInkRegions());
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}
		if (renderValue && inkValue != null) {
			inkValues.add(inkValue);
			// try {
			// image.drawInk(inkValue);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}
		if (image != null) {
			if (inkValues.size() > 0) { // speedup..
				try {
					image.drawInk(inkValues.toArray(new byte[][] {}));
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			try {
				image.crop(width, height);
			} catch (Exception e) {
				// e.printStackTrace();
			}
			if (maxWidth != null && pixelToPoints(image.getImage().getWidth(), dpi) > maxWidth) {
				scaleImage(image, maxWidth, 0.0f, dpi);
			}
		}
		return prepareImage(doc, image, compressionQualityPercent, dpi);
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
		// TODO Auto-generated constructor stub
	}

	private PDFJpeg(PDDocument doc, BufferedImage bi, float compressionQuality) throws IOException {
		super(doc, bi, compressionQuality);
		// TODO Auto-generated constructor stub
	}

	private PDFJpeg(PDDocument doc, Image image, float compressionQuality, int dpi)
			throws IOException {
		this(doc, image.getImage(), compressionQuality);
		this.dpi = dpi <= 0 ? DEFAULT_USER_SPACE_UNIT_DPI : dpi;
	}

	private PDFJpeg(PDDocument doc, InputStream is) throws IOException {
		super(doc, is);
		// TODO Auto-generated constructor stub
	}

	private PDFJpeg(PDStream jpeg) {
		super(jpeg);
		// TODO Auto-generated constructor stub
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
