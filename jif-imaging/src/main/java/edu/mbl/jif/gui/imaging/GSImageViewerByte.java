package edu.mbl.jif.gui.imaging;



import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import javax.swing.*;

public class GSImageViewerByte extends JComponent {
    private BufferedImage image1;
    private BufferedImage image2;
    private int sizeX;
    private int sizeY;

    public GSImageViewerByte() {
        sizeX = 256;
        sizeY = 256;

        // Create first image

        byte[] data1 = new byte[sizeX*sizeY];
        setData(data1, sizeX, sizeY);
        DataBuffer dataBuffer = new DataBufferByte(data1, data1.length);
        int dataType = dataBuffer.getDataType();
        int imageDepth = DataBuffer.getDataTypeSize(dataType);
        WritableRaster wr = Raster.createInterleavedRaster(dataBuffer,
            sizeX, sizeY, sizeX, 1, new int[]{0}, null);
        ComponentColorModel ccm = new ComponentColorModel(
            ColorSpace.getInstance(ColorSpace.CS_GRAY),
            new int[] {imageDepth},
            false, // hasAlpha
            false, // Alpha premultiplied
            Transparency.OPAQUE, dataType);
        image1 = new BufferedImage(ccm, wr, true, null);

        // Create second image

        image2 = new BufferedImage(sizeX, sizeY,
            BufferedImage.TYPE_BYTE_GRAY);

        byte[] data2 = ((DataBufferByte)image2.getRaster().getDataBuffer()).getData();
        setData(data2, sizeX, sizeY);
    }

    private void setData(byte[] data, int sizeX, int sizeY) {
        for (int y = 0; y < sizeY ; y++) {
            for (int x = 0; x < sizeX; x++) {
                data[y*sizeX+x] = (byte)y;
            }
        }
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(image1, 0, 0, null);
        //g.drawImage(image2, 256, 0, null);
    }

    public Dimension getMinimumSize() {
        return new Dimension(sizeX*2, sizeY);
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("GrayScale Image Viewer Byte");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GSImageViewerByte viewer = new GSImageViewerByte();
        frame.getContentPane().add(viewer);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
