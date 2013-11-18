/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging.nav;

import java.lang.reflect.Method;
import javax.swing.Action;
import javax.swing.JFrame;

/**
 *
 * @author GBH
 */
public class DynamicCaller {
// * To enable another plugin to register with TopFrame without having a dependency, i.e. only if TopFrame.jar is
// * available:

   private void registerFrameWithToolFrame(JFrame controlFrame_) {
// call static TopFrame.addFrameToShow(JFrame frame) 
      try {
         ClassLoader l = Thread.currentThread().getContextClassLoader();
         Class cls = l.loadClass("TopFrame");
         Method mainMethod = cls.getDeclaredMethod("addFrameToShow", new Class[]{JFrame.class});
         mainMethod.invoke(null, new Object[]{controlFrame_});
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   private void registerActionWithToolFrame(Action action) {
// call static TopFrame.addActionButton(Action action) 
      try {
         ClassLoader l = Thread.currentThread().getContextClassLoader();
         Class cls = l.loadClass("TopFrame");
         Method mainMethod = cls.getDeclaredMethod("addActionButton", new Class[]{Action.class});
         mainMethod.invoke(null, new Object[]{action});
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
}
