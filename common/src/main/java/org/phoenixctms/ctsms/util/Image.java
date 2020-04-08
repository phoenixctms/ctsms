package org.phoenixctms.ctsms.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.w3c.dom.Document;

public class Image {

	private ImageTranscoder imageTranscoder = null;
	private boolean drawn;
	private BufferedImage image;
	private java.awt.Color bgColor;
	private final static org.phoenixctms.ctsms.enumeration.Color DEFAULT_BG_COLOR = org.phoenixctms.ctsms.enumeration.Color.BLACK;
	private final static int DEFAULT_SCALE_MODE = java.awt.Image.SCALE_SMOOTH;
	private final static int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;

	public Image(byte[] imageData, org.phoenixctms.ctsms.enumeration.Color bgColor) throws Exception {
		this.image = ImageIO.read(new ByteArrayInputStream(imageData));
		this.bgColor = CommonUtil.convertColor(bgColor == null ? DEFAULT_BG_COLOR : bgColor);
		drawn = false;
	}

	public Image(int width, int height, org.phoenixctms.ctsms.enumeration.Color bgColor) throws Exception {
		this.image = new BufferedImage(width, height, DEFAULT_IMAGE_TYPE);
		this.bgColor = CommonUtil.convertColor(bgColor == null ? DEFAULT_BG_COLOR : bgColor);
		Graphics g = image.getGraphics();
		g.setColor(this.bgColor);
		g.fillRect(0, 0, width, height);
		drawn = true;
	}

	public byte[] convert(String outputFormat) throws Exception {
		drawImage(image, image.getWidth(), image.getHeight());
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ImageIO.write(image, outputFormat, buffer);
		return buffer.toByteArray();
	}

	public Image crop(int width, int height) throws Exception {
		if (width == 0) {
			width = image.getWidth();
		}
		if (height == 0) {
			height = image.getHeight();
		}
		BufferedImage croppedImage = image.getSubimage(0, 0, width, height);
		image = croppedImage;
		drawn = false;
		return this;
	}

	private void drawImage(java.awt.Image image, int width, int height) {
		if (!drawn) {
			BufferedImage imageBuffer = new BufferedImage(width, height, DEFAULT_IMAGE_TYPE);
			imageBuffer.getGraphics().drawImage(image, 0, 0, bgColor, null);
			this.image = imageBuffer;
			drawn = true;
		}
	}

	public void drawInk(byte[]... inkValues) throws Exception {
		Document svg = CommonUtil.inkValueToSvg(image.getWidth(), image.getHeight(), inkValues);
		if (svg != null) {
			getSvgTranscoder().transcode(new TranscoderInput(svg), null);
		}
	}

	public BufferedImage getImage() {
		drawImage(image, image.getWidth(), image.getHeight());
		return image;
	}

	private ImageTranscoder getSvgTranscoder() {
		if (imageTranscoder == null) {
			final Image img = this;
			imageTranscoder = new ImageTranscoder() {

				@Override
				public BufferedImage createImage(int w, int h) {
					return img.getImage();
				}

				public BufferedImage getBufferedImage() {
					return img.getImage();
				}

				@Override
				public void writeImage(BufferedImage imgage, TranscoderOutput output) {
					img.setImage(imgage);
				}
			};
		}
		return imageTranscoder;
	}

	public Image scale(int width, int height) throws Exception {
		if (width == 0) {
			width = (height * image.getWidth()) / image.getHeight();
		}
		if (height == 0) {
			height = (width * image.getHeight()) / image.getWidth();
		}
		java.awt.Image scaledImage = image.getScaledInstance(width, height, DEFAULT_SCALE_MODE);
		drawn = false;
		drawImage(scaledImage, width, height);
		return this;
	}

	private void setImage(BufferedImage image) {
		this.image = image;
		drawn = true;
	}
}
