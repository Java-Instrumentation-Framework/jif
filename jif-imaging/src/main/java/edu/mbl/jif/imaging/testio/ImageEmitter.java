package edu.mbl.jif.imaging.testio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * for testing write to disk performance...
 *
 * @author GBH
 */
public class ImageEmitter {

	// image size
	int s = 100;
	byte[] img = new byte[s * s];
	int fps = 100;
	int period = 1000 / fps;
	int frames = 0;

	public void start() {
		for (int i = 0; i < img.length; i++) {
			img[i] = (byte) 128;
		}
		System.out.println("fps: " + fps + ", " + period + " msec.");
		Timer timer = new Timer(period, new ImageGenerator());
		timer.start();
		JFrame f = new JFrame();
		f.setVisible(true);
	}

	class ImageGenerator implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frames++;
			
			if (frames % 100 == 0) {
				System.out.println(": " + frames);
			}
		}

	}

	public static void main(String[] args) {
		new ImageEmitter().start();
	}

}