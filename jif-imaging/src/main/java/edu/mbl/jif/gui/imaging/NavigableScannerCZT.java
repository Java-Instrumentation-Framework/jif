package edu.mbl.jif.gui.imaging;

import edu.mbl.jif.imaging.util.GraphicsUtilities;
import edu.mbl.jif.imaging.api.SeriesOfImages;
import edu.mbl.jif.imaging.series.SeriesEditor;
import edu.mbl.jif.imaging.series.SeriesOfImagesMultipageTiff;
import edu.mbl.jif.imaging.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NavigableScannerCZT extends JPanel {

	private SeriesOfImages series;
	private NavigableImagePanel idp;
	private JPanel bottomPane = new JPanel();
	private JLabel label;
	private int n;
	private int nC;
	private int nZ;
	private int nT;
	private int c = 0;
	private int z = 0;
	private int t = 0;
	private float scaleFactor = 1.0f;
	private final boolean editing = false;

	public NavigableScannerCZT(SeriesOfImages series) {
		super(true);
		//
		this.series = series;
		n = series.getNumImages();
		nC = series.getChannels();
		nZ = series.getZSections();
		nT = series.getTimeIntervals();
		//
		setLayout(new BorderLayout());
		InputMap im = this.getInputMap();
		ActionMap am = this.getActionMap();

		// Horizontal (T) scrollbar ------------------
		if (nT > 1) {
			final JSlider hbar = new JSlider(SwingConstants.HORIZONTAL, 0, nT - 1, 0);
			ChangeListener adjustmentListenerH = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					t = ((JSlider) e.getSource()).getValue();
					//System.out.println("t=" + t);
					showImage();
				}

			};
			hbar.addChangeListener(adjustmentListenerH);
			hbar.setMajorTickSpacing(1);
			hbar.setPaintTicks(true);
			hbar.setPaintTrack(false);
			hbar.setSnapToTicks(true);
			add(hbar, BorderLayout.NORTH);

			// add right/left arrow key responses
			//InputMap im = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
			AbstractAction right = new AbstractAction("right") {
				public void actionPerformed(ActionEvent evt) {
					if (hbar.getValue() < hbar.getMaximum()) {
						hbar.setValue(hbar.getValue() + 1);
					}
					;
				}

			};
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
			AbstractAction left = new AbstractAction("left") {
				public void actionPerformed(ActionEvent evt) {
					if (hbar.getValue() > hbar.getMinimum()) {
						hbar.setValue(hbar.getValue() - 1);
					}
					;
				}

			};

			am.put("right", right);
			am.put("left", left);
		}
		//
		// Vertical (Z) scrollbar ------------------------------

		if (nZ > 1) {
			final JSlider vbar = new JSlider(JScrollBar.VERTICAL, 0, nZ - 1, 0);
			ChangeListener adjustmentListenerV = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					z = ((JSlider) e.getSource()).getValue();
					//System.out.println("z=" + t);
					showImage();
				}

			};
			vbar.addChangeListener(adjustmentListenerV);
			vbar.setMajorTickSpacing(1);
			vbar.setPaintTicks(true);
			vbar.setPaintTrack(false);
			vbar.setInverted(true);
			vbar.setOrientation(SwingConstants.VERTICAL);
			vbar.setSnapToTicks(true);
			add(vbar, BorderLayout.WEST);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "dn");
			AbstractAction dn = new AbstractAction("dn") {
				public void actionPerformed(ActionEvent evt) {
					if (vbar.getValue() < vbar.getMaximum()) {
						vbar.setValue(vbar.getValue() + 1);
					}
					;
				}

			};
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
			AbstractAction up = new AbstractAction("up") {
				public void actionPerformed(ActionEvent evt) {
					if (vbar.getValue() > vbar.getMinimum()) {
						vbar.setValue(vbar.getValue() - 1);
					}
					;
				}

			};
			am.put("dn", dn);
			am.put("up", up);
		}

		// Channel (C) scrollbar ------------------------------
		if (nC > 1) {
			final JSlider cbar = new JSlider(JScrollBar.VERTICAL, 0, nC - 1, 0);
			ChangeListener adjustmentListenerC = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					c = ((JSlider) e.getSource()).getValue();
					//System.out.println("z=" + t);
					showImage();
				}

			};
			cbar.addChangeListener(adjustmentListenerC);
			cbar.setMajorTickSpacing(1);
			cbar.setPaintTicks(true);
			cbar.setPaintTrack(false);
			cbar.setInverted(false);
			cbar.setOrientation(SwingConstants.VERTICAL);
			cbar.setSnapToTicks(true);
			add(cbar, BorderLayout.EAST);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
					java.awt.event.InputEvent.SHIFT_DOWN_MASK), "chdn");
			AbstractAction chdn = new AbstractAction("chdn") {
				public void actionPerformed(ActionEvent evt) {
					if (cbar.getValue() < cbar.getMaximum()) {
						cbar.setValue(cbar.getValue() + 1);
					}
					;
				}

			};
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
					java.awt.event.InputEvent.SHIFT_DOWN_MASK), "chup");
			AbstractAction chup = new AbstractAction("chup") {
				public void actionPerformed(ActionEvent evt) {
					if (cbar.getValue() > cbar.getMinimum()) {
						cbar.setValue(cbar.getValue() - 1);
					}
					;
				}

			};
			am.put("chdn", chdn);
			am.put("chup", chup);
		}

		bottomPane.setLayout(new BorderLayout());
		label = new JLabel();
		label.setHorizontalAlignment(JLabel.CENTER);
		bottomPane.add(label, BorderLayout.CENTER);
		// If editing enabled {
		if (editing) {
			SeriesEditor seriesEdit = new SeriesEditor(n);
			// Delete Z section
			JButton deleteZsection = new JButton("DelZ");
			// Delete TimePoint
			JButton deleteTimePoint = new JButton("DelT");
			bottomPane.add(deleteZsection, BorderLayout.WEST);
			bottomPane.add(deleteTimePoint, BorderLayout.EAST);
		}
		add(bottomPane, BorderLayout.SOUTH);
		idp = new NavigableImagePanel();
		idp.setNavigationImageEnabled(true);
		this.setBackground(new Color(7,7,7));
		//pZi.setImage(series.getImage(1), scaleFactor, true);
		//add(pZi, BorderLayout.CENTER);
		add(idp, BorderLayout.CENTER);
		showImage();
	}

	void showImage() {
		int imgIndex = c + (z * nC) + (t * nZ*nC);
		System.out.println(t + ":" + z + ":" + c + "  = " + imgIndex);
		idp.setImage(GraphicsUtilities.toCompatibleImage(series.getImage(c, z, t)));
		StringBuffer sb = new StringBuffer();
		if (nC > 0) {
			sb.append(" C: " + (c + 1) + "/" + nC);
		}
		if (nZ > 0) {
			sb.append(" Z: " + (z + 1) + "/" + nZ);
		}
		if (nT > 0) {
			sb.append(" T: " + (t + 1) + "/" + nT);
		}		
		sb.append("   (" + imgIndex + ")");
		label.setText(sb.toString());
	}

	public static void main(String[] s) {
		String FILE = //edu.mbl.jif.Constants.testDataPath +
				//"Series_TZ\\" +
				//"STMPS_Yuki_TZ_04_0428_1427_41_Z-5.tif";
				// "./test-images/xyzt-200x200x10x15.tif";
		"./test-images/C3_Z5_T10.tif";
		int zSections = 5;
		int channels = 3;
		// xyzt-200x200x10x15_b causes TIFFImageReader.getCompression NullPointer
		//"Series_TZ\\xyzt-200x200x10x15_b.tif";
		JFrame frame = new JFrame("ScannerTZ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SeriesOfImagesMultipageTiff series = new SeriesOfImagesMultipageTiff(FILE, channels, zSections);
		frame.setContentPane(new NavigableScannerCZT(series));
		frame.setSize(500, 500);
		frame.setVisible(true);
	}

}
