/*
 * TestSimultaneous.java
 *
 * Created on December 8, 2006, 9:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging;

import edu.mbl.jif.gui.imaging.player.SeriesPlayerZoomFrame;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import edu.mbl.jif.imaging.util.ImageFactoryGrayScale;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFrame;

/**
 *
 * @author GBH
 */
public class TestSimultaneous {

	/**
	 * Creates a new instance of TestSimultaneous
	 */
	public TestSimultaneous() {
	}

	static String tiffFilename = "testsimul";

	public static void main(String[] args) {
		test2();
	}

	static void test2() {

		MultipageTiffFile tif = new MultipageTiffFile();

		tif.openWrite(tiffFilename);

		for (int i = 0; i < 10; i++) {

			BufferedImage img = ImageFactoryGrayScale.testImageByte();
			String name = timeStamp();
			img = dataOnImage(img, name, new String[]{String.valueOf(i)});
			//tif.prepareWriteSequence();
			tif.appendImage((BufferedImage) img);
			//tif.endWriteSequence();
			wait(1000);
			if (i == 0) {
				SeriesPlayerZoomFrame f = new SeriesPlayerZoomFrame(tiffFilename);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.pack();
				f.setVisible(true);
				tif.addSeriesFileListener(f);
			}
		}
		//tif.close();
	}

	static void test1() {
		for (int i = 0; i < 10; i++) {
			BufferedImage img = ImageFactoryGrayScale.testImageByte();
			String name = timeStamp();
			img = dataOnImage(img, name, new String[]{String.valueOf(i)});
			MultipageTiffFile.appendImageToTiffFile(img, "testSimul");
			wait(200);
		}
	}

	public static BufferedImage dataOnImage(BufferedImage bImage, String title,
			String[] data) {
		Graphics bG = bImage.getGraphics();
		int w = bImage.getWidth();
		int h = bImage.getHeight();
		int incr = (int) (h / (data.length + 2));

		// draw the Title
		bG.setColor(Color.white);
		bG.drawString(title, (int) (w / 8), incr);

		// draw the strings
		for (int i = 0; i < data.length; i++) {
			bG.setColor(Color.white);
			bG.drawString(data[i], (int) (w / 8), incr * (i + 2));
		}
		return bImage;
	}

	public static String timeStamp() {
		SimpleDateFormat formatter =
				new SimpleDateFormat("yy-MM-dd_HH-mm-ss_SS", Locale.getDefault());
		Date currentDate = new Date();
		String dateStr = formatter.format(currentDate);
		return dateStr;
	}
	
	public static void wait(int msecs) {
      try {Thread.sleep(msecs);} catch (InterruptedException e) { }
   }

}
