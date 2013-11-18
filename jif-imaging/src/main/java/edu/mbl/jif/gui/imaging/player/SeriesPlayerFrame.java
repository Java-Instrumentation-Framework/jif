package edu.mbl.jif.gui.imaging.player;

import edu.mbl.jif.gui.imaging.*;
import edu.mbl.jif.gui.imaging.player.SeriesPlayerController;
import edu.mbl.jif.gui.imaging.player.SeriesViewer;
import java.awt.*;
import javax.swing.*;

import edu.mbl.jif.gui.dialog.DialogBoxI;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;


/**
 * <imagePanel>Title: </imagePanel>
 * <imagePanel>Description: </imagePanel>
 * <imagePanel>Copyright: Copyright (c) 2003</imagePanel>
 * <imagePanel>Company: </imagePanel>
 * @author not attributable
 * @version 1.0
 */

public class SeriesPlayerFrame
      extends JFrame implements SeriesViewer
{
   SeriesPlayerController playCtrl;
   ResizableZoomImagePanel imagePanel;
   MultipageTiffFile tif;
   String path;
   BorderLayout borderLayout1 = new BorderLayout();

   public SeriesPlayerFrame (String _path) {
      this();
//      addFrameListener(new InternalFrameAdapter()
//      {
//         public void internalFrameClosing (InternalFrameEvent e) {
//            close();
//         }
//      });
      path = _path;
      this.setTitle("TimeSeries: " + path);
      tif = new MultipageTiffFile(path);
      Dimension imgDim = new Dimension(tif.getWidth(0), tif.getHeight(0));
      //     Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      //     if ((height > screenSize.height * 0.8) ||
      //       (width > screenSize.width * 0.8)) {
      //       return 0.5f;
      //    }
      // ?? imgDim.setSize(2.0, 2.0);
      // imagePanel.setPreferredSize(imgDim);
      try {
         imagePanel = new ResizableZoomImagePanel(tif.getImage(0), 1.0f, true);
      }
      catch (Exception ex) {
         DialogBoxI.boxError("Series File", "Failed to open file.");
         return;
      }
      this.getContentPane().setLayout(borderLayout1);

      // === SeriesPlayerController
      playCtrl = new SeriesPlayerController(tif, this);
//      playCtrl.setViewer(this);
//      playCtrl.setSeriesFile(tif);
      //playCtrl.setNumFrames(tif.getNumImages());
      //
      getContentPane().add(imagePanel, BorderLayout.CENTER);
      getContentPane().add(playCtrl, BorderLayout.SOUTH);
      setSize(648, 480);
      setLocation(200, 200);
      //pack();
      //replayFrame.setSize(new Dimension(width, height + 50));
      //PSj.deskTopFrame.desktop.add(this);
      setVisible(true);
   }


   public SeriesPlayerFrame () {
      super(); //"Series", true, true, true, true);
      try {
         jbInit();
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
   }


   void jbInit () throws Exception {

   }


   public void showImage (int n) {
      imagePanel.showImage(tif.getImage(n));
   }


   public void close () {
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run () {
            playCtrl.killPlayer();
            playCtrl = null;
            imagePanel = null;
            tif.close();
            tif = null;
            //dispose();
         }
      });

   }


   //---------------------------------------------------------------------------

   public static void main (String[] args) {
      new SeriesPlayerFrame(// edu.mbl.jif.Constants.testDataPath +
            //"C:\\testData\\" +
            //"Series_TZ\\" +
            //"STMPS_Yuki_TZ_04_0428_1427_41_Z-5.tif");
      "./test-images/xyzt-200x200x10x15.tif" );
      //"Series_TZ/31Aug95.Newt3Lamellap.tif");
      //"C:\\PSjData\\project1\\June 21\\image\\STMPS_04_0621_1451_54_Copy.tif");
      /*  String image_file_name = "PSCollagenDark.gif";
        //Image testImage = new ImageIcon(ClassLoader.getSystemResource(image_file_name)).getImage();
        Image image = null;
        try {
            image = ImageIO.read(new File(image_file_name));
        }
        catch (Exception e) {
            System.out.println("Exception loading: " + image_file_name);
        }
        JFrame zoomwin = new JFrame();
        zoomwin.getContentPane().setLayout(new BorderLayout());
        PanelZoomImage imagePanel = new PanelZoomImage();
        imagePanel.setImage(image, 1.0f);
        zoomwin.getContentPane().add(imagePanel, BorderLayout.CENTER);
        zoomwin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SeriesPlayerController playCtrl = new SeriesPlayerController();

        zoomwin.getContentPane().add(playCtrl, BorderLayout.SOUTH);
        zoomwin.setSize(new Dimension(400, 400));
//zoomwin.pack();
        zoomwin.setVisible(true);
       */

   }
}
