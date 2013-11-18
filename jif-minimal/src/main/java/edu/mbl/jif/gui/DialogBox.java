package edu.mbl.jif.gui;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DialogBox {

   static Component defaultParent = null;

   public static void setDefaultParent(Component _parent) {
      defaultParent = _parent;
   }

   private DialogBox() {
   }

   /*
    showConfirmDialog Asks a confirming question, like yes/no/cancel.
    showInputDialog Prompt for some input.
    void showMessageDialog - something that has happened. (OK button)
    showOptionDialog The Grand Unification of the above three

    returns:
    int result =
    YES_OPTION
    NO_OPTION
    CANCEL_OPTION
    OK_OPTION
    CLOSED_OPTION
    */
//---------------------------------------------------------------------------
// Error dialog
   public static void boxError(String title, Object message) {
      boxError(defaultParent, title, message);
   }

   public static void boxError(Component parent, String title, Object message) {
//      if (Prefs.usr.getBoolean("soundFeedback", true)) {
//         SoundClip clickSoundClip = new SoundClip("ding.wav");
//         if (clickSoundClip != null) {
//            clickSoundClip.play();
//         }
//      }
      JOptionPane.showMessageDialog(
              parent,
              message,
              title,
              JOptionPane.ERROR_MESSAGE);
      //jif.utils.PSjUtils.logError(title + ", " + message);
   }

//--------------------------------------------------------------------------
// Message dialog
   public static void boxNotify(String title, Object message) {
      boxNotify(defaultParent, title, message);
   }

   public static void boxNotify(Component parent, String title, Object message) {
      JOptionPane.showMessageDialog(
              parent,
              message,
              title,
              JOptionPane.INFORMATION_MESSAGE);
   }

//--------------------------------------------------------------------------
// Confirm dialog with the options yes/no
   public static boolean boxConfirm(String title, Object message) {
      return boxConfirm(defaultParent, title, message);
   }

   public static boolean boxConfirm(Component parent, String title,
           Object message) {
      int result = JOptionPane.showConfirmDialog(
              parent,
              message,
              title,
              JOptionPane.OK_CANCEL_OPTION);
      return result == JOptionPane.YES_OPTION;
   }

//--------------------------------------------------------------------------
// Confirm dialog with the options yes/no/cancel
   public static int boxYesNoCancel(String title, Object message) {
      return boxYesNoCancel(defaultParent, title, message);
   }

   public static int boxYesNoCancel(Component parent, String title,
           Object message) {
      int result = JOptionPane.showConfirmDialog(
              parent,
              message,
              title,
              JOptionPane.YES_NO_CANCEL_OPTION,
              JOptionPane.INFORMATION_MESSAGE);
      return result;
   }

//--------------------------------------------------------------------------
// Input dialog (string)
   public static String boxInputString(Component parent,
           String title, Object message) {
      String inputValue =
              JOptionPane.showInputDialog(parent, "Please input a value");
      return inputValue;
   }

//--------------------------------------------------------------------------
// Selection dialog (from a list)
   // Object[] possibleValues = { "First", "Second", "Third" };
   public static String boxSelectFromList(Component parent,
           String title, Object message,
           Object[] possibleValues) {
      Object selectedValue = JOptionPane.showInputDialog(
              parent,
              message,
              title,
              JOptionPane.INFORMATION_MESSAGE,
              null,
              possibleValues,
              possibleValues[0]);
      return (String) selectedValue;
   }

   public static void main(String args[]) {
      java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
            String selection = boxSelectFromList(null, "Select", "Select one of these:",
                    new Object[]{"First", "Second", "Third"});
            System.out.println("selection = " + selection);
            String input = boxInputString(null, "Select", "Select one of these:");
            System.out.println("input = " + input);
            boolean yesOrNo = boxConfirm("Really", "Do you really want to do that?");
            System.out.println("yesOrNo = " + yesOrNo);
            int so = boxYesNoCancel("Would you rather", "Would you rather, or cancel.");
         }
      });
   }
}
