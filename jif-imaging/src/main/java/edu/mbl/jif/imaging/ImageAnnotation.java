package edu.mbl.jif.imaging;

import edu.mbl.jif.gui.imaging.FrameImageDisplay;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.awt.image.WritableRaster;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


/**
 * <p>Title: </p>
 *
 * <p>Description: Takes JifImageMetadata object and creates an image containing the
 *      info as graphic text.  This can be inserted in a stack as a metadata record.</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ImageAnnotation {
    
    enum Position {
        TOP, TOP_LEFT, TOP_RIGHT,
        BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT
    }
    Position pos;
    static int yPos = 100;
    
    static int w = 800;
    static int h = 600;
    static int frameH;
    static int frameW;
    static int bitDepth;
    static long timeStamp = 0;
    
    static BufferedImage image = null;
    static WritableRaster wr;
    static Graphics2D graphics;
    static RenderingHints hints = new RenderingHints(null);
    
    Color foregroundColor = Color.white;
    Font font;
    static final int SHADOW_OFFSET = 1;
    static Font f1B = new Font("Dialog", Font.BOLD, 24);
    static Font f2B = new Font("Dialog", Font.BOLD, 18);
    static Font f3B = new Font("Dialog", Font.BOLD, 14);
    //int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
    //int fontSize = (int)Math.round(12.0 * screenRes / 72.0);
    static Font f1 = new Font("Dialog", Font.PLAIN, 24);
    static Font f2 = new Font("Dialog", Font.PLAIN, 18);
    static Font f3 = new Font("Dialog", Font.PLAIN, 14);
    
    static String dateStr;
    static SimpleDateFormat formatter =
            new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.getDefault());
    
    static Color fillColor = Color.gray;
    Color backgroundColor = Color.black;
    
    public ImageAnnotation() {
    }
    
    
    // ?? FontMetrics...
    
    public static void writeLines(String[] lines) {
        int numberOfLines = lines.length;
        int perLine = h / numberOfLines;
        int xPos = 10;
        for (int i = 0; i < lines.length; i++) {
            writeShadowed(lines[i], xPos, yPos);
            yPos = yPos + perLine;
        }
    }
    
    public static BufferedImage getRDI(String title,
            String[] lines, int _frameW, int _frameH,
            int _bitDepth) {
        //_prmFileName;
        frameW = _frameW;
        frameH = _frameH;
        w = _frameW;
        h = _frameH;
        bitDepth = _bitDepth;
        if (bitDepth == 8) {
            try {
                image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
                //returnImage = new BufferedImage(frameW, frameH, BufferedImage.TYPE_BYTE_GRAY);
            } catch (Exception e) {
            }
        }
        if (bitDepth == 16) {}
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHints(hints);
        graphics.setColor(fillColor);
        graphics.fillRect(0,0,w,h);
        // Title...
        graphics.setFont(f1B);
        writeShadowed(title, 10, 50);
        // Lines...
        graphics.setFont(f3B);
        //dateStr = formatter.format(new Date());
        //writeShadowed(dateStr, 30, 70);
        writeLines(lines);
//        graphicsR = (Graphics2D) returnImage.getGraphics();
//        graphicsR.setRenderingHints(hints);
//        graphicsR.drawImage(image, 0, 0, frameW, frameH, null);
//        wr = returnImage.getRaster();
        return image;
    }
    
    
    
    public static void writeShadowed(String s, int x, int y) {
        graphics.setColor(Color.black);
        graphics.drawString(s, x + SHADOW_OFFSET, y + SHADOW_OFFSET);
        graphics.setColor(Color.white);
        graphics.drawString(s, x, y);
    }
    
    
    public static void main(String[] args) {
        String titles = "Title 1";
        String[] lines = {"Line 1"};
        // "Line 1", "Line 2","Line 3", "Line 4"};
        
        BufferedImage img = getRDI(titles, lines, 640, 480, 8);
        (new FrameImageDisplay(img, "ImageMetaDisplay")).setVisible(true);
        
//        ZoomWindow zoomwin = new ZoomWindow("ZoomWindow Example", 3);
//        zoomwin.setImage(img);
//        zoomwin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        zoomwin.setSize(680, 460);
//        zoomwin.setVisible(true);
        
    }
}
