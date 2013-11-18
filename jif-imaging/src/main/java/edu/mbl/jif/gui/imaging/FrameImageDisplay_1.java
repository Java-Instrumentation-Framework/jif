package edu.mbl.jif.gui.imaging;

import edu.mbl.jif.imaging.util.ImageFactoryGrayScale;
import edu.mbl.jif.imaging.util.GraphicsUtilities;
import edu.mbl.jif.imaging.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * <p>Title: </p>
 *
 * <p>Description:
 * Test of ImageDisplayPanel
 * This uses ZoomControl16 with MouseSensitiveZSP
 * <p>Copyright: Copyright (c) 2006</p>
 * @author gbh at mbl.edu
 * @version 1.0
 */
public class FrameImageDisplay_1 extends JFrame {

    BorderLayout borderLayout1 = new BorderLayout();
    ImageDisplayPanel viewPanel;
    int w;
    int h;
    BufferedImage img = null;
    String title = "(none)";

    public FrameImageDisplay_1(BufferedImage _img, String title) {
        this(_img);
        this.setTitle(title);
    }

    public FrameImageDisplay_1(BufferedImage __img) {
        this(__img.getWidth(), __img.getHeight(), __img);
    }

    public FrameImageDisplay_1(int w, int h, BufferedImage img) {
        super(GraphicsUtilities.getCONFIGURATION());
        this.w = w;
        this.h = h;
        this.img = img;
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
//        try {
//            com.jgoodies.looks.plastic.Plastic3DLookAndFeel lookFeel =
//                new com.jgoodies.looks.plastic.Plastic3DLookAndFeel();
//            com.jgoodies.looks.plastic.PlasticLookAndFeel.setPlasticTheme(
//                // new com.jgoodies.looks.plastic.theme.Silver());
//                //new com.jgoodies.looks.plastic.theme.SkyBluerTahoma());
//                new com.jgoodies.looks.plastic.theme.DesertBlue());
//            com.jgoodies.looks.plastic.PlasticLookAndFeel.setTabStyle("Metal");
//            UIManager.setLookAndFeel(lookFeel);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        getContentPane().setLayout(borderLayout1);
        Dimension imageDim = new Dimension(w, h);
        // getWorkSpaceBounds
        //GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        viewPanel = new ImageDisplayPanel(imageDim);
        viewPanel.addMagnifierButton();
        this.add(viewPanel, BorderLayout.CENTER);
        if (img != null) {
            viewPanel.addImage(img, 0, 0);
        }
        this.setTitle(title);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }
    // +++ On maximizeFrame and restore, do FitToWindow
    public void addImage(BufferedImage img, double x, double y) {
        viewPanel.addImage(img, x, y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                //            File file = new File(
                //            edu.mbl.jif.Constants.testDataPath + "Aster_azim.tif");
                //            // @todo Opener...
                //            BufferedImage img = null;
                //            try {
                //               img = ImageIO.read(file);
                //               if(img == null){
                //                  System.err.println("Couldn't load pond");
                //                  return;
                //               }
                //               FrameImageDisplayTest f = new FrameImageDisplayTest(img.getWidth(),
                //                     img.getHeight(), img);
                //
                //               f.setVisible(true); }
                //            catch (IOException ex) {
                //            }


//        FrameImageDisplay_1 f =   new FrameImageDisplay_1(ImageFactoryGrayScale.testImageByte(), "byte");
//        new TestAppHarness(f);
//        f.addImage(ImageFactoryGrayScale.testImageByte(), 200, 200);
//        f.addImage(ImageFactoryGrayScale.testImageByte(), 200, 200);
//        f.addImage(ImageFactoryGrayScale.testImageByte(), 500, -200);
//        f.addImage(ImageFactoryGrayScale.testImageByte(), 700, 400);
//        f.addImage(ImageFactoryGrayScale.testImageByte(), 600, -300);

                //new TestAppHarness((new FrameImageDisplay(ImageFactoryGrayScale.testImageFloat(), "floater")));
                //(new FrameImageDisplay(ImageFactoryGrayScale.testImageShort12(), "12")).setVisible(true);
							
                (new FrameImageDisplay(
										GraphicsUtilities.toCompatibleImage(
										ImageFactoryGrayScale.testImageShort16()), "16")).setVisible(true);
                //(new FrameImageDisplay(ImageFactoryGrayScale.testImageFloat(), "floater")).setVisible(true);
                // test with
                // ReallyBigXYImage
//                System.out.println(
//                System.getProperty("user.dir") + System.getProperty("file.separator"));
//                BufferedImage image = load("diatoms.jpg");
                
//                FrameImageDisplay f = new FrameImageDisplay(image, "TestPattern");

                /*
                byte lut[] = new byte[256];
                for (int j = 0; j < 256; j++) {
                    lut[j] = (byte) (256 - j);
                }
                ByteLookupTable blut = new ByteLookupTable(0, lut);
                LookupOp lop = new LookupOp(blut, null);

                //BufferedImageOp op =  new ColorTintFilter(Color.WHITE, 0.5f);
                f.getImageDisplayPanel().setImageOp(lop);
                f.setVisible(true);
               // f.getImageDisplayPanel().changeImage(image);
                GraphicsUtilities.showCofiguration();

                 */
   //             showImageInfo(image);

            }

        });

    }

    public static void showImageInfo(BufferedImage img) {
        edu.mbl.jif.gui.text.TextWindow tw = new edu.mbl.jif.gui.text.TextWindow("ImageInfo");
        tw.set(edu.mbl.jif.imaging.util.ImgInfoDumper.dump(img));
        tw.setVisible(true);
    }

    public static BufferedImage load(String file) {
        Image image = null;
        try {
            image = ImageIO.read(new File(file));
        } catch (Exception e) {
            System.out.println("Exception loading: " + file);
        }
        return GraphicsUtilities.toCompatibleImage((BufferedImage) image);
    }

}
