/*
 * ViewerScannerTZ.java
 *
 * Created on March 29, 2007, 6:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.gui.imaging;

import edu.mbl.jif.imaging.series.SeriesOfImagesMultipageTiff;
//import ij.gui.GenericDialog;
import javax.swing.JFrame;

/**
 *
 * @author GBH
 */
public class ViewerScannerTZ extends JFrame implements ImageViewer {
    
    /** Creates a new instance of ViewerScannerTZ */
    public ViewerScannerTZ() {
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                String FILE = "./test-images/Z10_T500.tif";
								int zSections = 10;
						//"xyzt-200x200x10x15.tif";
                    //edu.mbl.jif.Constants.testDataPath +
                    //    "Series_TZ\\STMPS_Yuki_TZ_04_0428_1427_41_Z-5.tif";
                new ViewerScannerTZ().openImageSeries(new String[]{FILE}, zSections);
            }
        });
    }
    
    
    public void openImageSeries(final String[] argList, final int zSections) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println(argList[0]);
                
                // Prompt for Z-sections
//                GenericDialog gd = new GenericDialog("TZ Scanner");
//                gd.addNumericField("Z-sections: ", zSections, 1);
//                gd.showDialog();
//                if (gd.wasCanceled()) return;
//                zSections = (int)gd.getNextNumber();
                
                ScannerTZ stz = new ScannerTZ(new SeriesOfImagesMultipageTiff(argList[0], zSections));
                getRootPane().setContentPane(stz);
                setSize(400, 500);
                pack();
                setVisible(true);
            }
        });
        
    }
    
}
