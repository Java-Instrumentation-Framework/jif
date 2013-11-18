/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.imaging;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.IndexColorModel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class TestLUT extends JFrame {
  DisplayPanel displayPanel;

  JButton brightenButton, darkenButton, contrastIncButton, contrastDecButton,
      reverseButton, resetButton;

  public TestLUT() {
    super();
    Container container = getContentPane();

    displayPanel = new DisplayPanel();
    container.add(displayPanel);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 2));
    panel
        .setBorder(new TitledBorder(
            "Click a Button to Perform the Associated Operation and Reset..."));

    brightenButton = new JButton("Brightness >>");
    brightenButton.addActionListener(new ButtonListener());
    darkenButton = new JButton("Darkness >>");
    darkenButton.addActionListener(new ButtonListener());
    contrastIncButton = new JButton("Contrast >>");
    contrastIncButton.addActionListener(new ButtonListener());
    contrastDecButton = new JButton("Contrast <<");
    contrastDecButton.addActionListener(new ButtonListener());
    reverseButton = new JButton("Negative");
    reverseButton.addActionListener(new ButtonListener());
    resetButton = new JButton("Reset");
    resetButton.addActionListener(new ButtonListener());

    panel.add(brightenButton);
    panel.add(darkenButton);
    panel.add(contrastIncButton);
    panel.add(contrastDecButton);
    panel.add(reverseButton);
    panel.add(resetButton);

    container.add(BorderLayout.SOUTH, panel);

    addWindowListener(new WindowEventHandler());
    setSize(displayPanel.getWidth(), displayPanel.getHeight() + 25);
    show();
  }

  class WindowEventHandler extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
      System.exit(0);
    }
  }

  public static void main(String arg[]) {
    new TestLUT();
  }

  class ButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JButton button = (JButton) e.getSource();

      if (button.equals(brightenButton)) {
        displayPanel.brightenLUT();
        displayPanel.applyFilter();
        displayPanel.repaint();
      } else if (button.equals(darkenButton)) {
        displayPanel.darkenLUT();
        displayPanel.applyFilter();
        displayPanel.repaint();
      } else if (button.equals(contrastIncButton)) {
        displayPanel.contrastIncLUT();
        displayPanel.applyFilter();
        displayPanel.repaint();
      } else if (button.equals(contrastDecButton)) {
        displayPanel.contrastDecLUT();
        displayPanel.applyFilter();
        displayPanel.repaint();
      } else if (button.equals(reverseButton)) {
        displayPanel.reverseLUT();
        displayPanel.applyFilter();
        displayPanel.repaint();
      } else if (button.equals(resetButton)) {
        displayPanel.reset();
        displayPanel.repaint();
      }
    }
  }
}

class DisplayPanel extends JPanel {
  Image displayImage;

  BufferedImage bi;

  Graphics2D big;

  LookupTable lookupTable;

  DisplayPanel() {
    setBackground(Color.black); // panel background color
    loadImage();
    setSize(displayImage.getWidth(this), displayImage.getWidth(this)); // panel
    createBufferedImage();
  }

  public void loadImage() {
    displayImage = Toolkit.getDefaultToolkit().getImage(
        "Diatoms.jpg");
    MediaTracker mt = new MediaTracker(this);
    mt.addImage(displayImage, 1);
    try {
      mt.waitForAll();
    } catch (Exception e) {
      System.out.println("Exception while loading.");
    }

    if (displayImage.getWidth(this) == -1) {
      System.out.println("No jpg file");
      System.exit(0);
    }
  }

  public void createBufferedImage() {
      
              byte[] rLUT = new byte[256];
        byte[] gLUT = new byte[256];
        byte[] bLUT = new byte[256];
        for(int i=0; i<256; i++) {
            rLUT[i]=(byte)i;
            gLUT[i]=(byte)i;
            bLUT[i]=(byte)i;
        }
        bLUT[0] = (byte)255;  // zero pixels are blue
        rLUT[255] = (byte)255; // saturated pixels are red
      //  IndexColorModel cm = new IndexColorModel(8, 256, rLUT, gLUT, bLUT);
      //  bi = new BufferedImage(displayImage.getWidth(null), displayImage.getHeight(null), BufferedImage.TYPE_BYTE_INDEXED, cm);
        //wr = imageDisplayed.getRaster();
        
    bi = new BufferedImage(displayImage.getWidth(this), displayImage
        .getHeight(this), BufferedImage.TYPE_INT_ARGB);

    big = bi.createGraphics();
    big.drawImage(displayImage, 0, 0, this);
  }

  public void brightenLUT() {
    short brighten[] = new short[256];
    for (int i = 0; i < 256; i++) {
      short pixelValue = (short) (i + 10);
      if (pixelValue > 255)
        pixelValue = 255;
      else if (pixelValue < 0)
        pixelValue = 0;
      brighten[i] = pixelValue;
    }
    lookupTable = new ShortLookupTable(0, brighten);
  }

  public void darkenLUT() {
    short brighten[] = new short[256];
    for (int i = 0; i < 256; i++) {
      short pixelValue = (short) (i - 10);
      if (pixelValue > 255)
        pixelValue = 255;
      else if (pixelValue < 0)
        pixelValue = 0;
      brighten[i] = pixelValue;
    }
    lookupTable = new ShortLookupTable(0, brighten);
  }

  public void contrastIncLUT() {
    short brighten[] = new short[256];
    for (int i = 0; i < 256; i++) {
      short pixelValue = (short) (i * 1.2);
      if (pixelValue > 255)
        pixelValue = 255;
      else if (pixelValue < 0)
        pixelValue = 0;
      brighten[i] = pixelValue;
    }
    lookupTable = new ShortLookupTable(0, brighten);
  }

  public void contrastDecLUT() {
    short brighten[] = new short[256];
    for (int i = 0; i < 256; i++) {
      short pixelValue = (short) (i / 1.2);
      if (pixelValue > 255)
        pixelValue = 255;
      else if (pixelValue < 0)
        pixelValue = 0;
      brighten[i] = pixelValue;
    }
    lookupTable = new ShortLookupTable(0, brighten);
  }

  public void reverseLUT() {
    byte reverse[] = new byte[256];
    for (int i = 0; i < 256; i++) {
      reverse[i] = (byte) (255 - i);
    }
    lookupTable = new ByteLookupTable(0, reverse);
  }

  public void reset() {
    big.setColor(Color.black);
    big.clearRect(0, 0, bi.getWidth(this), bi.getHeight(this));
    big.drawImage(displayImage, 0, 0, this);
  }

    public void applyFilter() {
    LookupOp lop = new LookupOp(lookupTable, null);
    lop.filter(bi, bi);
  }

  public void update(Graphics g) {
    g.clearRect(0, 0, getWidth(), getHeight());
    paintComponent(g);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.drawImage(bi, 0, 0, this);
  }
}