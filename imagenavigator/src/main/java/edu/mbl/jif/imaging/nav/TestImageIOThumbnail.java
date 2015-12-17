/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging.nav;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author GBH
 */
public class TestImageIOThumbnail {

   public void test(int width) {
      try {
         File sourceImageFile = new File("bigfile.jpg");
         BufferedImage sourceImage = ImageIO.read(sourceImageFile);
         Image thumbnail = sourceImage.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
         BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null),
                 thumbnail.getHeight(null),
                 BufferedImage.TYPE_INT_RGB);
         bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);
         //ImageIO.write(bufferedThumbnail, "jpeg", outputStream);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

}
