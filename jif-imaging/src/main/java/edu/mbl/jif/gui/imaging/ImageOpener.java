/*
 * ImageOpener.java
 *
 * Created on September 25, 2006, 3:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.imaging;


import edu.mbl.jif.gui.dialog.DialogBox;
import edu.mbl.jif.gui.file.DirectoryChooserPanel;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;

import edu.mbl.jif.utils.FileUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author GBH
 */
public class ImageOpener {
    
    /** Creates a new instance of ImageOpener */
    public ImageOpener() {
    }
    
    public static void open(File file) {
        open(file.getAbsolutePath());
    }
    
    
    public static void open(String path) {
        String ext = FileUtil.getExtension(path);
        if(ext.equalsIgnoreCase("tif") || ext.equalsIgnoreCase("tiff") ) {
            MultipageTiffFile tifFile = new MultipageTiffFile(path);
            if(tifFile!=null) {
                if(tifFile.getNumImages()>1) {
                    openSeries(tifFile);
                } else {
                    
                }
            } else {
                // failed
            }
            
        }
        
        
    }
    
    public static void openSeries(MultipageTiffFile tifFile) {
        
    }
    
    public static BufferedImage load(String file) {
        Image image = null;
        try {
            image = ImageIO.read(new File(file));
        } catch (Exception e) {
            System.out.println("Exception loading: " + file);
        }
        return (BufferedImage)image;
    }
    //-----------------------------------------------------------
    // fileOpenerTree
    //
    public static void fileOpenerTree(String msg, String path) {
        JPanel panelFileChooser = new JPanel();
        panelFileChooser.setLayout(new BorderLayout());
        JPanel choices = new JPanel(new FlowLayout());
        final DirectoryChooserPanel dlg = new DirectoryChooserPanel(path);
        panelFileChooser.add(dlg, BorderLayout.CENTER);
        JButton openButton = new JButton("Open");
        choices.add(openButton);
        panelFileChooser.add(choices, BorderLayout.SOUTH);
        final JFrame frame =
                new JFrame("Open File");
        frame.add(panelFileChooser);
        //, 20, 50);
        //
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dlg.getSelectedFile() != null) {
                    String ext =
                            FileUtil.getFileExtension(dlg.getSelectedFile(), false);
                    
                    // System.out.println("dlg.getSelectedFile(): " + dlg.getSelectedFile());
                    if (dlg.getSelectedFile().isFile()
                    && (ext.equalsIgnoreCase("tif")
                    || ext.equalsIgnoreCase("tiff"))) {
                        open(dlg.getSelectedFile());
                        frame.dispose();
                    } else {
                        DialogBox.boxError(frame, "Sorry",
                                "Cannot open this file type");
                    }
                }
            }
        });
        frame.setSize(new Dimension(300, 600));
        frame.setVisible(true);
    }
    
    
    //----------------------------------------------------------------
    // openSelectedFile (for TIFFs and PolStacks)
    //
//    public static void openSelectedFile(final java.io.File file) {
//        //System.out.println("::" + file);
//        try {
//            Worker.post(new Task() {
//                public Object run() throws Exception {
//                    int imageType =
//                            psj.Image.ImageType.getImageTypeFor(file.getName());
//                    edu.mbl.jif.utils.PSjUtils.statusProgress("Opening file: " +
//                            file.getName());
//                    if ((imageType == psj.Image.ImageType.POLSTACK)
//                    || (imageType == psj.Image.ImageType.RAWPOLSTACK)
//                    || (imageType == psj.Image.ImageType.BKGDSTACK)) {
//                        psj.Image.ImageManager.loadPolStackFromFile(file.
//                                getAbsolutePath());
//                    } else {
//                        //ArrayList imgs = ImageUtils.loadImageArrayList(file.getAbsolutePath());
//
//                        if (imgs != null) {
//                           // ViewerUtil.openViewerForTIFF(file.getAbsolutePath(), imgs);
//                        } else {
//                            DialogBox.boxError("Error opening TIFF file",
//                                    "Cannot open: " + file.getAbsolutePath());
//                        }
//                    }
//                    return null;
//                }
//            });
//        } catch (Exception x) {
//            DialogBox.boxError("Error Open_InAnalyzer", x.getMessage());
//        } finally {
//            //statusClear();
//        }
//       //statusClear();
//    }
    
}
