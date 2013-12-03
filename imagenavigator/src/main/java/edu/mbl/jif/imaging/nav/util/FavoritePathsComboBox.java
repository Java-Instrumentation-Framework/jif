package edu.mbl.jif.imaging.nav.util;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class FavoritePathsComboBox extends JComboBox {

   static String[] longPaths;

   public FavoritePathsComboBox(List<String> fullPaths, int maxLen) {
      super(createShortenedPaths(fullPaths, maxLen));
      setRenderer(new ComboBoxWithToolTipRenderer(longPaths));
   }

//   public static JComboBox create(List<String> fullPaths, int maxLen) {
//      JComboBox cbox = new JComboBox(createShortenedPaths(fullPaths, maxLen));
//      cbox.setRenderer(new ComboBoxWithToolTipRenderer(longPaths));
//      return cbox;
//   }
//
//   public static JComboBox create(String[] fullPaths, int maxLen) {
//      JComboBox cbox = new JComboBox(createShortenedPaths(fullPaths, maxLen));
//      cbox.setRenderer(new ComboBoxWithToolTipRenderer(fullPaths));
//      return cbox;
//   }
   
   
   private static String[] createShortenedPaths(String[] fullPaths, int maxLen) {
      String[] shortPaths = new String[fullPaths.length];
      for (int i = 0; i < shortPaths.length; i++) {
         shortPaths[i] = FilePathUtils.limitPath(fullPaths[i], maxLen);
      }
      return shortPaths;
   }

   private static String[] createShortenedPaths(List<String> fullPaths, int maxLen) {
      String[] shortPaths = new String[fullPaths.size()];
      longPaths = new String[fullPaths.size()];
      for (int i = 0; i < shortPaths.length; i++) {
         shortPaths[i] = FilePathUtils.limitPath(fullPaths.get(i), maxLen);
         longPaths[i] = fullPaths.get(i);
      }
      return shortPaths;
   }

   @Override
   // returns the full string/path
   public Object getSelectedItem() {
      if(longPaths!=null && longPaths.length>0) {
      int index = this.getSelectedIndex();
      return longPaths[index];
      } else {
         return null;
      }
   }
   
   public interface FavoritePathsComboBoxListener {

      void favoritePathSelected(String path);
   }

   public static void main(String args[]) {
      try {
         UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      } catch (Exception evt) {
         evt.printStackTrace();
      }

      JFrame fram = new JFrame();
      fram.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            System.exit(0);
         }
      });

      fram.getContentPane().setLayout(new FlowLayout());
//      JComboBox favs = FavoritePathsComboBox.create(
//              new String[]{
//                 "C:\\Users\\GBH\\Desktop\\ZumaRegistration",
//                 "C:/DarkfieldBiref/2012_09_13_CalciteCrystal40x/SM_2012_0914_0132_1",
//                 "Java bdbdbd bbbbbbbs bsbs b bbbbbbbbbbbbbbss bbb  b b b b bbbbbbbbb",
//                 "J2EE bdbdbd bbbbbbbs bsbs b bbbbbbbbbbbbbbss bbb  b b b b bbbbbbbbb",
//                 "Java Script bdbdbd bbbbbbbs bsbs b bbbbbbbbbbbbbbss bbb  b b b b bbbbbbbbb"},
//              30);
//      fram.getContentPane().add(favs);
      fram.setSize(200, 140);
      fram.setVisible(true);
   }


}
