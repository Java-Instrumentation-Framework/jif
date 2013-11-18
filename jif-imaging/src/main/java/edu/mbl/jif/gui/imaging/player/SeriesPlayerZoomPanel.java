package edu.mbl.jif.gui.imaging.player;

import edu.mbl.jif.imaging.api.SeriesFileListener;
import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.gui.imaging.ImageDisplayPanel;
import java.awt.*;
import javax.swing.*;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SeriesPlayerZoomPanel
    extends JPanel implements SeriesViewer, SeriesFileListener {

    BorderLayout borderLayout1 = new BorderLayout();
    SeriesPlayerController playCtrl;
    MultipageTiffFile tif;
    String path;
    ImageDisplayPanel viewPanel;
    Dimension imageDim;

    public SeriesPlayerZoomPanel(String _path) {
//      addFrameListener(new InternalFrameAdapter()
//      {
//         public void internalFrameClosing (InternalFrameEvent e) {
//            close();
//         }
//      });
        path = _path;
        tif = new MultipageTiffFile(path);

        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        setLayout(borderLayout1);
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
        add(viewPanel, BorderLayout.CENTER);
        add(playCtrl, BorderLayout.SOUTH);
    }

    public Dimension getImageDimension() {
        return imageDim;
    }

    public void deactivateOverhead() {
        // viewPanel.enableObservers()
    }

    public void showImage(int n) {
        viewPanel.showImage(tif.getImage(n));
    }

    public void reactivateOverhead() {
    }

    public void stopOn(int n) {
        viewPanel.changeImage(tif.getImage(n));
    }

    @Override
    public int imageAdded() {
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

    // <editor-fold defaultstate="collapsed" desc=">>>--- Cleanup when parent closed ---<<<" >
    private WindowListener wl = new WindowAdapter() {

        public void windowClosing(WindowEvent evt) {
            cleanup();
        }

    };

    @Override
    public void addNotify() {
        super.addNotify();
        System.out.println("addNotify");
        SwingUtilities.windowForComponent(this).addWindowListener(wl);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        System.out.println("removeNotify");
        SwingUtilities.windowForComponent(this).removeWindowListener(wl);
    }

    protected void cleanup() {
        System.out.println("cleanup called");
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                playCtrl.killPlayer();
                playCtrl = null;
                tif.close();
                tif = null;
                //dispose();
            }

        });
    }
// </editor-fold>

    public static void main(String[] args) {
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
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                String testSeries = // edu.mbl.jif.Constants.testDataPath +
                    "C:\\_dev\\testData\\" +
                    "Series_TZ\\STMPS_Yuki_TZ_04_0428_1427_41_Z-5.tif";
                SeriesPlayerZoomPanel p = new SeriesPlayerZoomPanel(testSeries);
                FrameForTest f = new FrameForTest();
                f.addContents(p);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.pack();
                f.setVisible(true);
            }

        });

    }

}
