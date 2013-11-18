package edu.mbl.jif.gui.imaging.player;

import edu.mbl.jif.gui.imaging.ImageDisplayPanel;
import edu.mbl.jif.imaging.api.SeriesFileListener;
import java.awt.*;
import javax.swing.*;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;

public class SeriesPlayerZoomFrame
    extends JFrame implements SeriesViewer, SeriesFileListener {

    BorderLayout borderLayout1 = new BorderLayout();
    SeriesPlayerController playCtrl;
    MultipageTiffFile tif;
    String path;
    ImageDisplayPanel viewPanel;
    Dimension imageDim;

    public SeriesPlayerZoomFrame(String _path)
      {
//      addFrameListener(new InternalFrameAdapter()
//      {
//         public void internalFrameClosing (InternalFrameEvent e) {
//            close();
//         }
//      });
        path = _path;
        this.setTitle("TimeSeries: " + path);
        tif = new MultipageTiffFile(path);

        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
      }

    private void jbInit() throws Exception
      {
//        try {
//            com.jgoodies.looks.plastic.Plastic3DLookAndFeel lookFeel = new com.jgoodies.looks.plastic.Plastic3DLookAndFeel();
//            com.jgoodies.looks.plastic.PlasticLookAndFeel.setPlasticTheme(
//                //new com.jgoodies.looks.plastic.theme.DesertBluer());
//                // new com.jgoodies.looks.plastic.theme.Silver());
//                //new com.jgoodies.looks.plastic.theme.SkyBluerTahoma());
//                new com.jgoodies.looks.plastic.theme.DesertBlue());
//            com.jgoodies.looks.plastic.PlasticLookAndFeel.setTabStyle("Metal");
//            UIManager.setLookAndFeel(lookFeel);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //getContentPane().setLayout(borderLayout1);
        // === SeriesPlayerController
        playCtrl = new SeriesPlayerController(tif, this);
//      playCtrl.setViewer(this);
//      playCtrl.setSeriesFile(tif);
        playCtrl.setNumFrames(tif.getNumImages());
        //
        imageDim = new Dimension(tif.getWidth(0), tif.getHeight(0));
        // getWorkSpaceBounds
        //GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        //Dimension dimVscroll = new Dimension(640, 480);

        viewPanel = new ImageDisplayPanel(imageDim);
        viewPanel.changeImage(tif.getImage(0));
        getContentPane().add(viewPanel, BorderLayout.CENTER);
        getContentPane().add(playCtrl, BorderLayout.SOUTH);
      }

    public Dimension getImageDimension()
      {
        return imageDim;
      }

    public void deactivateOverhead()
      {
        // viewPanel.enableObservers()
      }

    public void showImage(int n)
      {
        viewPanel.showImage(tif.getImage(n));
      }

    public void reactivateOverhead()
      {
      }

    public void stopOn(int n)
      {
        viewPanel.changeImage(tif.getImage(n));
      }

    @Override
    public int imageAdded()
      {
        boolean onLastSlice = playCtrl.isOnLastSlice();
        tif.closeRead();
        tif.openRead(path);
        int numImages = tif.getNumImages();
        // System.out.println("ImageAdded NumImages: " +  numImages);
        playCtrl.setNumFrames(numImages);
        if (onLastSlice) {
            playCtrl.gotoSlice(numImages - 1);
        }
        // tif.getImage(i);
        return numImages;
      }

    public static void main(String[] args)
      {
        SwingUtilities.invokeLater(new Runnable() {

            public void run()
              {
                String testSeries = // edu.mbl.jif.Constants.testDataPath +
                         "./test-images/Z50_T50.tif";
												 //"xyzt-200x200x10x15.tif";
                    //"C:\\_DevEnv\\testData\\STMPS_Yuki_TZ_04_0428_1427_41_Z-5.tif";
                SeriesPlayerZoomFrame f = new SeriesPlayerZoomFrame(testSeries);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.pack();
                f.setVisible(true);
              }

        });

      }

}
