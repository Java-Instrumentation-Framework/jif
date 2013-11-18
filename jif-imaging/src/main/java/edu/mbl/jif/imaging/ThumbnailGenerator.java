package edu.mbl.jif.imaging;

/**
 * Title:        ThumbnailGenerator<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Esteban<p>
 * Company:      ystop<p>
 * @author Esteban
 * @version 1.0
 * uses code found at
 * http://developer.java.sun.com/developer/TechTips/1999/tt1021.html
 * for creating the thumbnails
 */
import java.io.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;


// import com.sun.image.codec.jpeg.*;

public class ThumbnailGenerator
        extends JFrame
{
    JPanel jPanel1 = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JScrollPane jScrollPane1 = new JScrollPane();
    JList jList1 = new JList();
    JButton jButtonSelectFiles = new JButton();
    JButton jButtonMakeThumbnail = new JButton();
    File[] files;
    private int maxDim = 90;
    JSlider jSlider1 = new JSlider();
    JLabel jLabel1 = new JLabel();
    JLabel jLabelMaxDim = new JLabel();
    JProgressBar jProgressBar1 = new JProgressBar();
    JButton jButtonClear = new JButton();

    public ThumbnailGenerator () {
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        setSize(400, 400);
        setVisible(true);
    }


    public static void main (String[] args) {
        ThumbnailGenerator thumbnail1 = new ThumbnailGenerator();
    }


    private void jbInit () throws Exception {
        jPanel1.setLayout(gridBagLayout1);
        jButtonSelectFiles.setText("Select Files");
        jButtonSelectFiles.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed (ActionEvent e) {
                jButtonSelectFiles_actionPerformed(e);
            }
        });
        this.addWindowListener(new java.awt.event.WindowAdapter()
        {

            public void windowClosing (WindowEvent e) {
                this_windowClosing(e);
            }
        });
        jButtonMakeThumbnail.setText("Make Thumbnails");
        jButtonMakeThumbnail.addActionListener(new java.awt.event.
                                               ActionListener()
        {

            public void actionPerformed (ActionEvent e) {
                jButtonMakeThumbnail_actionPerformed(e);
            }
        });
        this.setTitle("Thumbnail Generator");
        jLabel1.setText("Max Dimension");
        jSlider1.setMajorTickSpacing(10);
        jSlider1.setExtent(10);
        jSlider1.setMinimum(50);
        jSlider1.setValue(maxDim);
        jSlider1.setPaintTicks(true);
        jSlider1.setMaximum(200);
        jSlider1.setSnapToTicks(true);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener()
        {

            public void stateChanged (ChangeEvent e) {
                jSlider1_stateChanged(e);
            }
        });

        jLabelMaxDim.setText("" + maxDim);
        jProgressBar1.setBorder(BorderFactory.createEtchedBorder());
        jButtonClear.setText("Clear");
        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(jScrollPane1, new GridBagConstraints(0, 0, 2, 7, 0.5, 0.5
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 5), 0, 0));
        jPanel1.add(jButtonSelectFiles,
                    new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.SOUTH,
                                           GridBagConstraints.HORIZONTAL,
                                           new Insets(0, 0, 0, 5), 0, 0));
        jPanel1.add(jButtonMakeThumbnail,
                    new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER,
                                           GridBagConstraints.NONE,
                                           new Insets(5, 0, 5, 5), 0, 0));
        jPanel1.add(jSlider1, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 5, 5), 0, 0));
        jPanel1.add(jLabel1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 0, 0, 5), 0, 0));
        jPanel1.add(jLabelMaxDim, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 5), 0, 0));
        jPanel1.add(jProgressBar1, new GridBagConstraints(1, 7, 2, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        jPanel1.add(jButtonClear, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.5
                , GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 5, 5), 0, 0));
        jScrollPane1.getViewport().add(jList1, null);
    }


    void jButtonSelectFiles_actionPerformed (ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new ImageFilter());
        fc.showOpenDialog(this);
        files = fc.getSelectedFiles();
        System.out.println("Array length " + files.length);
        if (files.length == 0) {
            return;
        }
        jList1.setListData(files);

    }


    void this_windowClosing (WindowEvent e) {
        System.exit(0);
    }


    public void createThumbnail (File originalFile) {
        String orig = originalFile.getAbsolutePath();

        int dot = orig.lastIndexOf(".");
        String thumb = orig.substring(0, dot) + "_t" + orig.substring(dot);
        System.out.println("Thumb name: " + thumb);
        try {
            // Get the image from a file.
            Image inImage = new ImageIcon(orig).getImage();

            // Determine the scale.
            double scale = (double) maxDim / (double) inImage.getHeight(null);
            if (inImage.getWidth(null) > inImage.getHeight(null)) {
                scale = (double) maxDim / (double) inImage.getWidth(null);
            }

            // Determine size of new image.
            //One of them
            // should equal maxDim.
            int scaledW = (int) (scale * inImage.getWidth(null));
            int scaledH = (int) (scale * inImage.getHeight(null));

            // Create an image buffer in
            //which to paint on.
            BufferedImage outImage = new BufferedImage(scaledW, scaledH,
                    BufferedImage.TYPE_INT_RGB);

            // Set the scale.
            AffineTransform tx = new AffineTransform();

            // If the image is smaller than
            //the desired image size,
            // don't bother scaling.
            if (scale < 1.0d) {
                tx.scale(scale, scale);
            }

            // Paint image.
            Graphics2D g2d = outImage.createGraphics();
            g2d.drawImage(inImage, tx, null);
            g2d.dispose();

            // JPEG-encode the image
            //and write to file.
            OutputStream os = new FileOutputStream(thumb);
            // TODO JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
            // encoder.encode(outImage);
            os.close();
            OutputStream os2 = new FileOutputStream(orig.substring(0, dot) +
                    ".txt");
            PrintWriter pw = new PrintWriter(os2);
            pw.write(originalFile.getName() + " " + inImage.getWidth(null) +
                     " x " + inImage.getHeight(null));
            pw.close();
            os2.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    void jButtonMakeThumbnail_actionPerformed (ActionEvent e) {
        jProgressBar1.setMinimum(0);
        jProgressBar1.setMaximum(files.length);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Runnable backgroundRun = new Runnable()
        {
            public void run () {
                for (int i = 0; i < files.length; i++) {
                    jProgressBar1.setValue(i + 1);
                    createThumbnail(files[i]);
                    Thread.yield();
                }
                jButtonMakeThumbnail.setEnabled(true);
                setCursor(Cursor.getDefaultCursor());
            }
        };

        Thread newt = new Thread(backgroundRun);
        newt.start();

        jButtonMakeThumbnail.setEnabled(false);
    }


    void jSlider1_stateChanged (ChangeEvent e) {
        jLabelMaxDim.setText("" + jSlider1.getValue() + " pixels");
        maxDim = jSlider1.getValue();
    }
}

class ImageFilter
        extends javax.swing.filechooser.FileFilter
{

    // Accept all directories and all gif, jpg, or tiff files.
    public boolean accept (File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.tiff) ||
                extension.equals(Utils.tif) ||
                extension.equals(Utils.gif) ||
                extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg)) {
                return true;
            }
            else {
                return false;
            }
        }

        return false;
    }


    // The description of this filter
    public String getDescription () {
        return "Images: jpg,jpeg,gif,tiff";
    }
}

class Utils
{

    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";

    /*
     * Get the extension of a file.
     */
    public static String getExtension (File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

}
