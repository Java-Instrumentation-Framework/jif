package edu.mbl.jif.gui.imaging.array;

import edu.mbl.jif.gui.imaging.FrameImageDisplay;
import edu.mbl.jif.imaging.api.SeriesOfImages;
import edu.mbl.jif.imaging.series.SeriesOfImagesMultipageTiff;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2003</p> <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ImageArrayDisplayPanel extends JPanel {

	BorderLayout bLayout = new BorderLayout();
	GridLayout gridLayout1 = new GridLayout();
	JScrollPane scrollPane = new JScrollPane();
	JPanel imageGrid = new JPanel();
	JPanel status = new JPanel();
	SeriesOfImages series;
	JList list = new JList();
	ElementZoomImage[] pZi;
	int nZ = 1;
	int nT = 1;
	MyMouseListener myMouseListener = new MyMouseListener();
	int sampling = -1;

	public ImageArrayDisplayPanel(SeriesOfImages series) {
		this(series, -1);
	}

	public ImageArrayDisplayPanel(SeriesOfImages series, int sampling) {
		this.series = series;
		this.sampling = sampling;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		// determine sampling if not specified
		if (sampling == -1) {
			// TODO Set sampling based on the number and dimension of images 
			int numImages = series.getNumImages();
			series.getImageDimensions();
		}

		this.setLayout(bLayout);
		this.setOpaque(true);
		imageGrid.setBackground(Color.lightGray);
		nT = series.getTimeIntervals();
		nZ = series.getZSections();
		imageGrid.setLayout(gridLayout1);
		gridLayout1.setColumns(nT);
		gridLayout1.setRows(nZ);
		gridLayout1.setHgap(1);
		gridLayout1.setVgap(1);

		pZi = new ElementZoomImage[nZ * nT];

		Dimension dim = series.getImageDimensions();

		for (int z = 0; z < nZ; z++) {
			for (int t = 0; t < nT; t++) {
				try {
					BufferedImage img = series.getAsThumbnail((t * nZ) + z, sampling);
					if (img != null) {
						pZi[z + t] = new ElementZoomImage(img, 1.0f, false);
						imageGrid.add(pZi[z + t], null);
						pZi[z + t].setT(t);
						pZi[z + t].setZ(z);
						pZi[z + t].addMouseListener(myMouseListener);
						pZi[z + t].repaint();
					} else {
						System.err.println("Couldn't get thumbnail");
					}
				} catch (Exception ex) {
					System.err.println("Exception");
				}
			}
		}
		scrollPane.getViewport().add(imageGrid, null);
		this.add(scrollPane, BorderLayout.CENTER);
		this.validate();
	}

	class MyMouseListener extends MouseInputAdapter {

		public void mouseClicked(MouseEvent e) {
			ElementZoomImage eZi = (ElementZoomImage) e.getSource();
			System.out.println("z / t : " + eZi.getZ() + " / " + eZi.getT());
			if (e.getClickCount() == 2) {
				System.out.println(" double click");
				int r = eZi.getZ();
				int c = eZi.getT();
				System.out.println(r + "," + c);
				// Open the image in viewer...
				openImageViewer(series, r, c);
			}
			// Popup Menu
//                if (SwingUtilities.isRightMouseButton(e)) {
//                    //int index = list.locationToIndex(e.getPoint());
//                    popup.show(e.getComponent(), e.getX(), e.getY());
//                }

		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

	}

	private void openImageViewer(SeriesOfImages series, int r, int c) {
		int n = r + c * series.getZSections();
		String title = "" + series.getFilename() + ": " + r + ", " + c + " (" + n + ")";
		BufferedImage img = series.getImage(n);
		(new FrameImageDisplay(img, title)).setVisible(true);
	}

	public static void openInFrame(SeriesOfImages series) {
		// ToDo Limit number of images to ?
		int numImages = series.getNumImages();



		int sampling = 5;
		ImageArrayDisplayPanel p = new ImageArrayDisplayPanel(series, sampling);

		//p.setImage(image, 0.1f);
		p.setPreferredSize(new Dimension(300, 300));
		JFrame zoomwin = new JFrame();
		zoomwin.getContentPane().setLayout(new BorderLayout());
		zoomwin.getContentPane().add(p, BorderLayout.CENTER);
		zoomwin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		zoomwin.pack();
		zoomwin.setVisible(true);
	}

	public static void main(String[] args) {
//        String FILE = edu.mbl.jif.Constants.testDataPath +
//            "Series_TZ\\STMPS_Yuki_TZ_04_0428_1427_41_Z-5.tif";
		// "lightfield\\PollenLView850.tif";
		//"Series_TZ\\STAPS_04_0621_1451_54.tif";
		//"Series_TZ\\xyzt-200x200x10x15_b.tif";
		//"Series_TZ\\31Aug95.Newt3Lamellap.tif";
		String FILE = "./test-images/Z5_T10.tif";
		int zSections = 5;
		//String FILE = "./test-images/HyperStack.tif";
		SeriesOfImages series = new SeriesOfImagesMultipageTiff(FILE, zSections);
		ImageArrayDisplayPanel.openInFrame(series);
	}

}
