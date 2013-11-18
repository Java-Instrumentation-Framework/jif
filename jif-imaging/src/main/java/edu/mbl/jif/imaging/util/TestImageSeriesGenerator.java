package edu.mbl.jif.imaging.util;

import edu.mbl.jif.imaging.tiff.MultipageTiffFile;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import java.text.SimpleDateFormat;

import java.util.Locale;

/**
 * <p>Title: TestImageSeriesGenerator</p>
 *
 * <p>Description: Test Image Sequence Generator for 3,4 and 5 dimensions </p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: </p>
 *
 * @author GBH
 * @version 1.0
 */
public class TestImageSeriesGenerator {

	static int bitDepth;
	static long timeStamp = 0;
	static BufferedImage image = null;
	static WritableRaster wr;
	static Graphics2D graphics;
	static Font f1B = new Font("Dialog", Font.BOLD, 24);
	static Font f2B = new Font("Dialog", Font.BOLD, 18);
	static Font f3B = new Font("Dialog", Font.BOLD, 14);
	//int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
	//int fontSize = (int)Math.round(12.0 * screenRes / 72.0);
	static Font f1 = new Font("Dialog", Font.PLAIN, 24);
	static Font f2 = new Font("Dialog", Font.PLAIN, 18);
	static Font f3 = new Font("Dialog", Font.PLAIN, 14);
	static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy.MM.dd HH:mm:ss.SSS",
			Locale.getDefault());
	static String dateStr;
	static RenderingHints hints = new RenderingHints(null);
	static int yPos = 100;
	static Color fillColor = Color.gray;
	static final int SHADOW_OFFSET = 1;
	static int width;
	static int height;
	static int sections;
	static int timePoints;
	static int channels;
	static MultipageTiffFile mf;

	public TestImageSeriesGenerator(int bitDepth, int width, int height, int sections,
			int timePoints, String filename) {
		this(bitDepth, width, height, sections, timePoints, 1, filename);
	}

	public TestImageSeriesGenerator(int bitDepth, int width, int height, int sections,
			int timePoints, int channels, String filename) {

		this.width = width;
		this.height = height;
		this.sections = sections;
		this.timePoints = timePoints;
		this.channels = channels;
		this.bitDepth = bitDepth;
		//
		mf = new MultipageTiffFile(filename);
		generateSeries();
		mf.close();
	}

	public static void generateSeries() {
		for (int i = 0; i < timePoints; i++) {
			for (int j = 0; j < sections; j++) {
				for (int c = 0; c < channels; c++) {
					BufferedImage image = generateImage(width, height, bitDepth, 
							i, timePoints, j, sections, c, channels);
					mf.appendImage(image);
				}
			}
		}
	}
	
	public static BufferedImage generateImage(
			int width, int height, int _bitDepth, 
			int i, int timePoints, 
			int j, int sections, 
			int c, int channels)			
	{
		Color channelColor = Color.gray;
		StringBuffer id = new StringBuffer();
		BufferedImage image = null;
		if (_bitDepth == 8) {
			try {
				image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
			} catch (Exception e) {	}
		}
		if (_bitDepth == 16) {
			try {
				image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
			} catch (Exception e) {	}
		}
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics = (Graphics2D) image.getGraphics();
		graphics.setRenderingHints(hints);
		if (channels > 1) {
			channelColor = new Color(Color.HSBtoRGB((float) c / channels, 0.5f, 0.80f));
			graphics.setColor(channelColor);
		} else {
			graphics.setColor(fillColor);
		}
		graphics.fillRect(0, 0, width, height);
		int dx = width / timePoints;
		int dy = height / sections;
		int offY = j * dy;
		int offX = i * dx;
		graphics.setColor(Color.black);
		graphics.fillRect(offX, offY, dx, dy);

		if (channels > 1) {
			id.append("  C: " + String.valueOf(c));
		}
		if (sections > 1) {
			id.append("  Z: " + String.valueOf(j));
		}
		if (timePoints > 1) {
			id.append("  T: " + String.valueOf(i));
		}
		graphics.setFont(f1B);
		writeShadowed(id.toString(), 10, 50);
		return image;
	}
	
	
//    public static void writeLines(String[] lines) {
//        int numberOfLines = lines.length;
//        int perLine = h / numberOfLines;
//        int xPos = 10;
//        for (int i = 0; i < lines.length; i++) {
//            writeShadowed(lines[i], xPos, yPos);
//            yPos = yPos + perLine;
//        }
//    }

	public static void writeShadowed(String s, int x, int y) {
		graphics.setColor(Color.black);
		graphics.drawString(s, x + SHADOW_OFFSET, y + SHADOW_OFFSET);
		graphics.setColor(Color.white);
		graphics.drawString(s, x, y);
	}

	public static void main(String[] args) {
		String titles = "Title 1";
		String[] lines = {"Line 1"};

		// "Line 1", "Line 2","Line 3", "Line 4"};
//		TestImageSeriesGenerator gen = new TestImageSeriesGenerator(8, 640, 480, 10, 500, "Z10_T500");
//        TestImageSeriesGenerator gen = new TestImageSeriesGenerator(8, 640, 480, 5, 10, "Z5_T10");
		TestImageSeriesGenerator gen = new TestImageSeriesGenerator(8, 640, 480, 5, 10, 3, "C3_Z5_T10");
//        gen = new TestImageSeriesGenerator(8, 640, 480, 1, 10, "Z1_T10");        
//        gen = new TestImageSeriesGenerator(8, 640, 480, 10, 1, "Z10_T1");     
		// (new FrameImageDisplay(img, "ImageMetaDisplay")).setVisible(true);
		//        ZoomWindow zoomwin = new ZoomWindow("ZoomWindow Example", 3);
		//        zoomwin.setImage(img);
		//        zoomwin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//        zoomwin.setSize(680, 460);
		//        zoomwin.setVisible(true);
	}

}
