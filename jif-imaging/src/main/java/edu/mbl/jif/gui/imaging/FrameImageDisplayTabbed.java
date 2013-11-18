package edu.mbl.jif.gui.imaging;

import edu.mbl.jif.imaging.util.ImagingUtils;
import edu.mbl.jif.gui.imaging.array.PanelTabbedImage;
import edu.mbl.jif.gui.dialog.DialogBox;
import edu.mbl.jif.imaging.*;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import javax.swing.JFrame;
import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

// usage: edu.mbl.jif.imaging.FrameImageDisplayTabbed(arrayList);

public class FrameImageDisplayTabbed extends JFrame {
    public FrameImageDisplayTabbed(ArrayList imgs) {
        super();
        PanelTabbedImage tip = new PanelTabbedImage(imgs);
        getContentPane().add(tip, BorderLayout.CENTER);
        setBounds(0, 0, 500, 500);
        setVisible(true);
    }
    
    // loadImageArrayList
    public static ArrayList loadImageArrayList(String tiffPathFile) {
        ArrayList imgs = new ArrayList();
        try {
            imgs = MultipageTiffFile.loadImageArrayList(tiffPathFile);
            if (imgs == null) {
                DialogBox.boxError("Could not open image", "file: " + tiffPathFile);
                System.err.println("Could not loadImageArrayList from: " + tiffPathFile);
            }
        } catch (Exception e) {
            System.err.println("Error loading ImageArray from:\n " + tiffPathFile);
            e.printStackTrace();
            imgs = null;
        }
        return imgs;
    }
    public static void loadFileIntoTabbedViewerFrame(String filename) {
        ArrayList imgs = new ArrayList();
        imgs = ImagingUtils.loadImageArrayList(filename);
        if (imgs != null) {
            ImagingUtils.getImageDataType(imgs);
            int width = ((BufferedImage) imgs.get(0)).getWidth();
            int height = ((BufferedImage) imgs.get(0)).getHeight();
            int size = width * height;
            int numImages = imgs.size();
            new edu.mbl.jif.gui.imaging.FrameImageDisplayTabbed(imgs);
        }
    }
    
    
    public static void main(String[] args) {
        String filename = //edu.mbl.jif.Constants.testDataPath +
                "ps\\_BG_03_0825_1748_01-4Frame.tif";
        loadFileIntoTabbedViewerFrame(filename);
    }
}
