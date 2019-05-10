package org.phoenixctms.ctsms.pdf;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.encoding.Encoding;
import org.apache.pdfbox.encoding.EncodingManager;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.phoenixctms.ctsms.util.CommonUtil;

public final class PDFUtil {

	public enum Alignment {
		TOP_LEFT, TOP_CENTER, TOP_RIGHT, MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
	}

	public enum FontSize {
		TINY, SMALL, MEDIUM, BIG, LARGE, SIZE11, SIZE12
	}

	public enum LineStyle {
		SOLID, DASHED
	}

	public enum Rotation {
		NONE, HORIZONTAL, VERTICAL, CCW90, CW90
	}

	public enum TextDecoration {
		NONE, STRIKE_THROUGH, UNDERLINE
	}

	public final static String DEFAULT_LABEL = "<label>";
	public final static String PDF_LINE_BREAK = "\r\n";
	private final static String LINE_BREAK_REGEXP_PATTERN = "(" + Pattern.quote(PDF_LINE_BREAK) + ")|(\\n)|(\\r)";
	private final static Pattern LINE_BREAK_REGEXP = Pattern.compile(LINE_BREAK_REGEXP_PATTERN);
	private final static float TINY_FONT_SIZE = 6.0f;
	private final static float TINY_FONT_LINE_SPACING = 2.0f; // 1.0f;
	private final static float TINY_FONT_TEXT_DECORATION_LINE_WIDTH = 0.2f;
	private final static float SMALL_FONT_SIZE = 8.0f;
	private final static float SMALL_FONT_LINE_SPACING = 2.6666f; // 1.3333f;
	private final static float SMALL_FONT_TEXT_DECORATION_LINE_WIDTH = SMALL_FONT_SIZE / 10.0f;
	private final static float MEDIUM_FONT_SIZE = 10.0f;
	private final static float MEDIUM_FONT_LINE_SPACING = 3.333f; // 2.0f;
	private final static float MEDIUM_FONT_TEXT_DECORATION_LINE_WIDTH = MEDIUM_FONT_SIZE / 10.0f;
	private final static float BIG_FONT_SIZE = 13.0f;
	private final static float BIG_FONT_LINE_SPACING = 4.3333f; // 3.0f;
	private final static float BIG_FONT_TEXT_DECORATION_LINE_WIDTH = BIG_FONT_SIZE / 10.0f;
	private final static float LARGE_FONT_SIZE = 18.0f;
	private final static float LARGE_FONT_LINE_SPACING = 6.0f; // 4.0f;
	private final static float LARGE_FONT_TEXT_DECORATION_LINE_WIDTH = LARGE_FONT_SIZE / 10.0f;
	private final static float SIZE11_FONT_SIZE = 11.0f;
	private final static float SIZE11_FONT_LINE_SPACING = 3.5f; // 2.0f;
	private final static float SIZE11_FONT_TEXT_DECORATION_LINE_WIDTH = SIZE11_FONT_SIZE / 10.0f;
	private final static float SIZE12_FONT_SIZE = 12.0f;
	private final static float SIZE12_FONT_LINE_SPACING = 4.1f; // 2.0f;
	private final static float SIZE12_FONT_TEXT_DECORATION_LINE_WIDTH = SIZE12_FONT_SIZE / 10.0f;
	private final static float STRIKE_THROUGH_DECORATION_LINE_OFFSET = 0.44f;
	private final static float UNDERLINE_DECORATION_LINE_OFFSET = -0.15f;
	private final static ArrayList<Character> WORD_BOUNDARY_CHARS = new ArrayList<Character>();
	static {
		WORD_BOUNDARY_CHARS.add(' ');
		WORD_BOUNDARY_CHARS.add('\t');
		WORD_BOUNDARY_CHARS.add('/');
		WORD_BOUNDARY_CHARS.add('-');
		WORD_BOUNDARY_CHARS.add(',');
		WORD_BOUNDARY_CHARS.add(';');
		// WORD_BOUNDARY_CHARS.add('.');
	}

	private static void drawString(PDPageContentStream contentStream, String text) throws IOException {
		if (contentStream != null && text != null && text.length() > 0) {
			char[] tc = text.toCharArray();
			StringBuilder sb = new StringBuilder();
			Encoding encoding = EncodingManager.INSTANCE.getEncoding(COSName.WIN_ANSI_ENCODING);
			for (int i = 0; i < tc.length; i++) {
				Character c = tc[i];
				try {
					sb.appendCodePoint(encoding.getCode(encoding.getNameFromCharacter(c)));
				} catch (IOException e) {
					sb.append(c);
				}
			}
			contentStream.drawString(sb.toString());
		}
	}

	private static float getFontSize(FontSize fontSize) {
		if (fontSize != null) {
			switch (fontSize) {
				case TINY:
					return TINY_FONT_SIZE;
				case SMALL:
					return SMALL_FONT_SIZE;
				case MEDIUM:
					return MEDIUM_FONT_SIZE;
				case BIG:
					return BIG_FONT_SIZE;
				case LARGE:
					return LARGE_FONT_SIZE;
				case SIZE11:
					return SIZE11_FONT_SIZE;
				case SIZE12:
					return SIZE12_FONT_SIZE;
				default:
					break;
			}
		}
		return MEDIUM_FONT_SIZE;
	}

	private static float getLineHeight(PDFont font, FontSize fontSize) {
		float size = getFontSize(fontSize);
		return Math.min(glyphUnitsToPoints(font.getFontDescriptor().getCapHeight(), size), glyphUnitsToPoints(font.getFontDescriptor().getFontBoundingBox().getHeight(), size));
	}

	private static float getLineSpacing(FontSize lineSpacing) {
		if (lineSpacing != null) {
			switch (lineSpacing) {
				case TINY:
					return TINY_FONT_LINE_SPACING;
				case SMALL:
					return SMALL_FONT_LINE_SPACING;
				case MEDIUM:
					return MEDIUM_FONT_LINE_SPACING;
				case BIG:
					return BIG_FONT_LINE_SPACING;
				case LARGE:
					return LARGE_FONT_LINE_SPACING;
				case SIZE11:
					return SIZE11_FONT_LINE_SPACING;
				case SIZE12:
					return SIZE12_FONT_LINE_SPACING;
				default:
					break;
			}
		}
		return MEDIUM_FONT_LINE_SPACING;
	}

	public static float getStringWidth(String string, PDFont font, FontSize fontSize) throws IOException {
		return glyphUnitsToPoints(font.getStringWidth(string), getFontSize(fontSize));
	}

	private static float getTextDecorationLineWidth(FontSize lineWidth) {
		if (lineWidth != null) {
			switch (lineWidth) {
				case TINY:
					return TINY_FONT_TEXT_DECORATION_LINE_WIDTH;
				case SMALL:
					return SMALL_FONT_TEXT_DECORATION_LINE_WIDTH;
				case MEDIUM:
					return MEDIUM_FONT_TEXT_DECORATION_LINE_WIDTH;
				case BIG:
					return BIG_FONT_TEXT_DECORATION_LINE_WIDTH;
				case LARGE:
					return LARGE_FONT_TEXT_DECORATION_LINE_WIDTH;
				case SIZE11:
					return SIZE11_FONT_TEXT_DECORATION_LINE_WIDTH;
				case SIZE12:
					return SIZE12_FONT_TEXT_DECORATION_LINE_WIDTH;
				default:
					break;
			}
		}
		return MEDIUM_FONT_TEXT_DECORATION_LINE_WIDTH;
	}

	private static float glyphUnitsToPoints(float glyphUnits, float fontSize) {
		return glyphUnits * fontSize / 1000.0f;
	}

	public static PDFont loadFont(String fontFileName, PDDocument doc, PDFont defaultBaseFont) throws IOException {
		// public static PDFont loadFont(String fontFileName, PDDocument doc, PDFont defaultBaseFont, PDStream pdStream) throws IOException {
		PDFont font = null;
		if (fontFileName != null && fontFileName.length() > 0) {
			font = PDType1Font.getStandardFont(fontFileName);
			if (font == null) {
				font = PDTrueTypeFont.loadTTF(doc, fontFileName);
				// http://stackoverflow.com/questions/5570225/workaround-for-pdfbox-pdtruetypefont-bad-widths-bug
			}
		} else {
			font = defaultBaseFont;
		}
		// if (font != null && pdStream != null) {
		// if (font instanceof PDType1Font) {
		// ((PDType1Font) font).setToUnicode(pdStream.getCOSObject());
		// } else if (font instanceof PDTrueTypeFont) {
		// ((PDTrueTypeFont) font).setToUnicode(pdStream.getCOSObject());
		// }
		// }
		return font;
	}

	public static byte[] loadImage(String fileName) throws Exception {
		if (fileName != null && fileName.length() > 0) {
			return CommonUtil.inputStreamToByteArray(new FileInputStream(fileName));
		}
		return null;
	}

	// public static PDFJpeg prepareCroppedImage(PDDocument doc, byte[] data, float width, float height, int compressionQualityPercent, int dpi,
	// org.phoenixctms.ctsms.enumeration.Color bgColor) {
	// if (doc != null && data != null && data.length > 0) {
	// BufferedImage image;
	// try {
	// image = PDFJpeg.createCroppedImage(data, width, height, dpi, bgColor);
	// return new PDFJpeg(doc, image, compressionQualityPercent / 100.0f, dpi);
	// } catch (Exception e) {
	// }
	// }
	// return null;
	// }
	//
	public static void renderFrame(PDPageContentStream contentStream, org.phoenixctms.ctsms.enumeration.Color color, float x, float y, float width, float height, Alignment align,
			float lineWidth) throws IOException {
		renderFrame(contentStream, color, x, y, width, height, align, lineWidth, LineStyle.SOLID);
	}

	public static void renderFrame(PDPageContentStream contentStream, org.phoenixctms.ctsms.enumeration.Color color, float x, float y, float width, float height, Alignment align,
			float lineWidth, LineStyle lineStyle) throws IOException {
		if (contentStream != null && lineWidth > 0.0f) {
			float left = x;
			float bottom = y - height;
			if (align != null) {
				switch (align) {
					case TOP_CENTER:
						left = x - width / 2.0f;
						bottom = y - height;
						break;
					case TOP_RIGHT:
						left = x - width;
						bottom = y - height;
						break;
					case MIDDLE_LEFT:
						left = x;
						bottom = y - height / 2.0f;
						break;
					case MIDDLE_CENTER:
						left = x - width / 2.0f;
						bottom = y - height / 2.0f;
						break;
					case MIDDLE_RIGHT:
						left = x - width;
						bottom = y - height / 2.0f;
						break;
					case BOTTOM_LEFT:
						left = x;
						bottom = y;
						break;
					case BOTTOM_CENTER:
						left = x - width / 2.0f;
						bottom = y;
						break;
					case BOTTOM_RIGHT:
						left = x - width;
						bottom = y;
						break;
					default: // topleft else...
				}
			}
			contentStream.setStrokingColor(CommonUtil.convertColor(color));
			setLineDashPattern(contentStream, lineStyle);
			contentStream.setLineWidth(lineWidth);
			contentStream.addRect(left, bottom, width, height);
			contentStream.closeAndStroke();
		}
	}

	public static void renderImage(PDPageContentStream contentStream, PDFJpeg ximage, float x, float y, Alignment align) throws IOException {
		if (ximage != null && contentStream != null) {
			float imageHeight = ximage.getHeightPoints();
			float imageWidth = ximage.getWidthPoints();
			float left = x;
			float bottom = y - imageHeight;
			if (align != null) {
				switch (align) {
					case TOP_CENTER:
						left = x - imageWidth / 2.0f;
						bottom = y - imageHeight;
						break;
					case TOP_RIGHT:
						left = x - imageWidth;
						bottom = y - imageHeight;
						break;
					case MIDDLE_LEFT:
						left = x;
						bottom = y - imageHeight / 2.0f;
						break;
					case MIDDLE_CENTER:
						left = x - imageWidth / 2.0f;
						bottom = y - imageHeight / 2.0f;
						break;
					case MIDDLE_RIGHT:
						left = x - imageWidth;
						bottom = y - imageHeight / 2.0f;
						break;
					case BOTTOM_LEFT:
						left = x;
						bottom = y;
						break;
					case BOTTOM_CENTER:
						left = x - imageWidth / 2.0f;
						bottom = y;
						break;
					case BOTTOM_RIGHT:
						left = x - imageWidth;
						bottom = y;
						break;
					default: // topleft else...
				}
			}
			// http://stackoverflow.com/questions/15789954/pdfbox-pdjpeg-gets-drawn-in-wrong-colors-when-resized-adobe-reader-window
			contentStream.drawXObject(ximage, left, bottom, imageWidth, imageHeight);
		}
	}

	public static void renderLine(PDPageContentStream contentStream, org.phoenixctms.ctsms.enumeration.Color color, float xStart, float yStart, float xEnd, float yEnd,
			float lineWidth)
			throws IOException {
		renderLine(contentStream, color, xStart, yStart, xEnd, yEnd, lineWidth, LineStyle.SOLID);
	}

	public static void renderLine(PDPageContentStream contentStream, org.phoenixctms.ctsms.enumeration.Color color, float xStart, float yStart, float xEnd, float yEnd,
			float lineWidth, LineStyle lineStyle)
			throws IOException {
		if (contentStream != null && lineWidth > 0.0f) {
			contentStream.setStrokingColor(CommonUtil.convertColor(color));
			setLineDashPattern(contentStream, lineStyle);
			contentStream.setLineWidth(lineWidth);
			contentStream.addLine(xStart, yStart, xEnd, yEnd);
			contentStream.closeAndStroke();
		}
	}

	public static float renderMultilineText(PDPageContentStream contentStream, PDFont font, FontSize fontSize, org.phoenixctms.ctsms.enumeration.Color color, String text, float x,
			float y, Alignment align, float textBlockWidth) throws IOException {
		return renderMultilineText(contentStream, font, fontSize, color, text, x, y, align, textBlockWidth, TextDecoration.NONE);
	}

	public static float renderMultilineText(PDPageContentStream contentStream, PDFont font, FontSize fontSize, org.phoenixctms.ctsms.enumeration.Color color, String text, float x,
			float y, Alignment align, float textBlockWidth, TextDecoration decoration) throws IOException {
		String[] lines;
		if (text != null) {
			lines = LINE_BREAK_REGEXP.split(text, -1);
		} else {
			lines = new String[1];
			lines[0] = "";
		}
		ArrayList<String> wrappedLines = new ArrayList<String>(lines.length);
		float maxWidth;
		boolean wrapLines;
		if (textBlockWidth > 0.0f) {
			maxWidth = textBlockWidth;
			wrapLines = true;
		} else {
			maxWidth = 0.0f;
			wrapLines = false;
		}
		float size = getFontSize(fontSize);
		for (int i = 0; i < lines.length; i++) {
			float width = glyphUnitsToPoints(font.getStringWidth(lines[i]), size);
			if (wrapLines) {
				if (lines[i].length() > 0) {
					if (width > textBlockWidth) {
						char[] chars = lines[i].toCharArray();
						StringBuffer line = new StringBuffer();
						StringBuffer word = new StringBuffer();
						for (int k = 0; k < chars.length; k++) {
							word.append(chars[k]);
							if (WORD_BOUNDARY_CHARS.contains(chars[k])) {
								if ((glyphUnitsToPoints(font.getStringWidth(line.toString()), size)
										+ glyphUnitsToPoints(font.getStringWidth(word.toString()), size)) > textBlockWidth) {
									wrappedLines.add(line.toString());
									line.delete(0, line.length());
								}
								line.append(word);
								word.delete(0, word.length());
							}
						}
						// handle any extra chars in current word
						if (word.length() > 0) {
							if ((glyphUnitsToPoints(font.getStringWidth(line.toString()), size)
									+ glyphUnitsToPoints(font.getStringWidth(word.toString()), size)) > textBlockWidth) {
								wrappedLines.add(line.toString());
								line.delete(0, line.length());
							}
							line.append(word);
						}
						// handle extra line
						if (line.length() > 0) {
							wrappedLines.add(line.toString());
						}
					} else {
						wrappedLines.add(lines[i]);
					}
				} else {
					wrappedLines.add(lines[i]);
				}
			} else {
				wrappedLines.add(lines[i]);
				maxWidth = Math.max(maxWidth, width);
			}
		}
		float lineSpacing = getLineSpacing(fontSize);
		float lineHeight = getLineHeight(font, fontSize); // glyphUnitsToPoints(font.getFontDescriptor().getFontBoundingBox().getHeight(),size);
		float totalHeight = (lineHeight + lineSpacing) * wrappedLines.size();
		if (contentStream != null) {
			float left = x;
			float bottom = y - lineHeight - lineSpacing / 2.0f;
			if (align != null) {
				switch (align) {
					case TOP_CENTER:
						left = x - maxWidth / 2.0f;
						bottom = y - lineHeight - lineSpacing / 2.0f;
						break;
					case TOP_RIGHT:
						left = x - maxWidth;
						bottom = y - lineHeight - lineSpacing / 2.0f;
						break;
					case MIDDLE_LEFT:
						left = x;
						bottom = y + (totalHeight - lineSpacing) / 2.0f;
						break;
					case MIDDLE_CENTER:
						left = x - maxWidth / 2.0f;
						bottom = y + (totalHeight - lineSpacing) / 2.0f;
						break;
					case MIDDLE_RIGHT:
						left = x - maxWidth;
						bottom = y + (totalHeight - lineSpacing) / 2.0f;
						break;
					case BOTTOM_LEFT:
						left = x;
						bottom = y + totalHeight - lineHeight - lineSpacing / 2.0f;
						break;
					case BOTTOM_CENTER:
						left = x - maxWidth / 2.0f;
						bottom = y + totalHeight - lineHeight - lineSpacing / 2.0f;
						break;
					case BOTTOM_RIGHT:
						left = x - maxWidth;
						bottom = y + totalHeight - lineHeight - lineSpacing / 2.0f;
						break;
					default: // topleft else...
				}
			}
			contentStream.beginText();
			contentStream.setFont(font, size);
			contentStream.setNonStrokingColor(CommonUtil.convertColor(color));
			contentStream.moveTextPositionByAmount(left, bottom);
			float l = left;
			float b = bottom;
			ArrayList<Float[]> decorationLines = new ArrayList<Float[]>(wrappedLines.size());
			Float decorationLineOffset = null;
			if (decoration != null) {
				switch (decoration) {
					case STRIKE_THROUGH:
						decorationLineOffset = STRIKE_THROUGH_DECORATION_LINE_OFFSET;
						break;
					case UNDERLINE:
						decorationLineOffset = UNDERLINE_DECORATION_LINE_OFFSET;
						break;
					default:
						break;
				}
			}
			Iterator<String> it = wrappedLines.iterator();
			while (it.hasNext()) {
				String line = it.next();
				float xOffset;
				float width = glyphUnitsToPoints(font.getStringWidth(line), size);
				if (align != null) {
					switch (align) {
						case TOP_CENTER:
						case MIDDLE_CENTER:
						case BOTTOM_CENTER:
							xOffset = (maxWidth - width) / 2.0f;
							contentStream.moveTextPositionByAmount(xOffset, 0);
							l += xOffset;
							drawString(contentStream, line);
							if (decorationLineOffset != null) {
								decorationLines.add(new Float[] { l, b + lineHeight * decorationLineOffset, l + width, b + lineHeight * decorationLineOffset });
							}
							contentStream.moveTextPositionByAmount(-1.0f * xOffset, 0);
							l -= xOffset;
							break;
						case TOP_RIGHT:
						case MIDDLE_RIGHT:
						case BOTTOM_RIGHT:
							xOffset = (maxWidth - width);
							contentStream.moveTextPositionByAmount(xOffset, 0);
							l += xOffset;
							drawString(contentStream, line);
							if (decorationLineOffset != null) {
								decorationLines.add(new Float[] { l, b + lineHeight * decorationLineOffset, l + width, b + lineHeight * decorationLineOffset });
							}
							contentStream.moveTextPositionByAmount(-1.0f * xOffset, 0);
							l -= xOffset;
							break;
						case TOP_LEFT:
						case MIDDLE_LEFT:
						case BOTTOM_LEFT:
						default: // topleft else...
							drawString(contentStream, line);
							if (decorationLineOffset != null) {
								decorationLines.add(new Float[] { l, b + lineHeight * decorationLineOffset, l + width, b + lineHeight * decorationLineOffset });
							}
							break;
					}
				} else {
					drawString(contentStream, line);
					if (decorationLineOffset != null) {
						decorationLines.add(new Float[] { l, b + lineHeight * decorationLineOffset, l + width, b + lineHeight * decorationLineOffset });
					}
				}
				contentStream.moveTextPositionByAmount(0, -1.0f * (lineHeight + lineSpacing));
				b -= (lineHeight + lineSpacing);
			}
			contentStream.endText();
			if (decorationLines.size() > 0) {
				contentStream.setStrokingColor(CommonUtil.convertColor(color));
				setLineDashPattern(contentStream, LineStyle.SOLID);
				contentStream.setLineWidth(getTextDecorationLineWidth(fontSize));
				Iterator<Float[]> decorationLinesIt = decorationLines.iterator();
				while (decorationLinesIt.hasNext()) {
					Float[] decorationLine = decorationLinesIt.next();
					contentStream.drawLine(decorationLine[0], decorationLine[1], decorationLine[2], decorationLine[3]);
				}
			}
		}
		return totalHeight;
	}

	public static float renderTextLine(PDPageContentStream contentStream, PDFont font, FontSize fontSize, org.phoenixctms.ctsms.enumeration.Color color, String text, float x,
			float y,
			Alignment align) throws IOException {
		return renderTextLine(contentStream, font, fontSize, color, text, x, y, align, TextDecoration.NONE, Rotation.NONE);
	}

	public static float renderTextLine(PDPageContentStream contentStream, PDFont font, FontSize fontSize, org.phoenixctms.ctsms.enumeration.Color color, String text, float x,
			float y,
			Alignment align, TextDecoration decoration) throws IOException {
		return renderTextLine(contentStream, font, fontSize, color, text, x, y, align, decoration, Rotation.NONE);
	}

	public static float renderTextLine(PDPageContentStream contentStream, PDFont font, FontSize fontSize, org.phoenixctms.ctsms.enumeration.Color color, String text, float x,
			float y,
			Alignment align, TextDecoration decoration, Rotation rotate) throws IOException {
		float size = getFontSize(fontSize);
		float lineSpacing = getLineSpacing(fontSize);
		float lineHeight = getLineHeight(font, fontSize);
		if (contentStream != null) {
			String line;
			if (text != null) {
				line = text.replaceAll(LINE_BREAK_REGEXP_PATTERN, " ");
			} else {
				line = "";
			}
			float width = glyphUnitsToPoints(font.getStringWidth(line), size);
			float left = x;
			float bottom = y - lineHeight - lineSpacing / 2.0f;
			if (align != null) {
				switch (align) {
					case TOP_CENTER:
						left = x - width / 2.0f;
						bottom = y - lineHeight - lineSpacing / 2.0f;
						break;
					case TOP_RIGHT:
						left = x - width;
						bottom = y - lineHeight - lineSpacing / 2.0f;
						break;
					case MIDDLE_LEFT:
						left = x;
						bottom = y - lineHeight / 2.0f;
						break;
					case MIDDLE_CENTER:
						left = x - width / 2.0f;
						bottom = y - lineHeight / 2.0f;
						break;
					case MIDDLE_RIGHT:
						left = x - width;
						bottom = y - lineHeight / 2.0f;
						break;
					case BOTTOM_LEFT:
						left = x;
						bottom = y + lineSpacing / 2.0f;
						break;
					case BOTTOM_CENTER:
						left = x - width / 2.0f;
						bottom = y + lineSpacing / 2.0f;
						break;
					case BOTTOM_RIGHT:
						left = x - width;
						bottom = y + lineSpacing / 2.0f;
						break;
					default: // topleft else...
				}
			}
			contentStream.beginText();
			contentStream.setFont(font, size);
			contentStream.setNonStrokingColor(CommonUtil.convertColor(color));
			contentStream.moveTextPositionByAmount(left, bottom);
			float l = left;
			float b = bottom;
			ArrayList<Float[]> decorationLines = new ArrayList<Float[]>(1);
			Float decorationLineOffset = null;
			if (decoration != null) {
				switch (decoration) {
					case STRIKE_THROUGH:
						decorationLineOffset = STRIKE_THROUGH_DECORATION_LINE_OFFSET;
						break;
					case UNDERLINE:
						decorationLineOffset = UNDERLINE_DECORATION_LINE_OFFSET;
						break;
					default:
						break;
				}
			}
			if (rotate != null) {
				switch (rotate) {
					case CW90:
						contentStream.setTextRotation(Math.PI / -2.0, x, y);
						if (decorationLineOffset != null) {
							decorationLines.add(new Float[] { l + lineHeight * decorationLineOffset, b, l + lineHeight * decorationLineOffset, b - width });
						}
						break;
					case VERTICAL:
					case CCW90:
						contentStream.setTextRotation(Math.PI / 2.0, x, y);
						if (decorationLineOffset != null) {
							decorationLines.add(new Float[] { l - lineHeight * decorationLineOffset, b, l - lineHeight * decorationLineOffset, b + width });
						}
						break;
					default: // none else...
						if (decorationLineOffset != null) {
							decorationLines.add(new Float[] { l, b + lineHeight * decorationLineOffset, l + width, b + lineHeight * decorationLineOffset });
						}
						break;
				}
			}
			drawString(contentStream, line);
			contentStream.endText();
			if (decorationLines.size() > 0) {
				contentStream.setStrokingColor(CommonUtil.convertColor(color));
				setLineDashPattern(contentStream, LineStyle.SOLID);
				contentStream.setLineWidth(getTextDecorationLineWidth(fontSize));
				Iterator<Float[]> decorationLinesIt = decorationLines.iterator();
				while (decorationLinesIt.hasNext()) {
					Float[] decorationLine = decorationLinesIt.next();
					contentStream.drawLine(decorationLine[0], decorationLine[1], decorationLine[2], decorationLine[3]);
				}
			}
		}
		return lineHeight + lineSpacing;
	}

	private static void setLineDashPattern(PDPageContentStream contentStream, LineStyle lineStyle) throws IOException {
		if (contentStream != null) { // && lineStyle != null) {
			float[] pattern;
			float phase;
			switch (lineStyle) {
				case DASHED:
					pattern = new float[] { 3.0f };
					phase = 0.0f;
					break;
				default: //solid else...
					pattern = new float[] {};
					phase = 0.0f;
					break;
			}
			// if (pattern != null) {
			contentStream.setLineDashPattern(pattern, phase);
			// }
		}
	}

	private PDFUtil() {
	}
}
