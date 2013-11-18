/*
 * DebuggingTools.java
 *
 * Created on May 15, 2006, 12:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.utils.diag;

import edu.mbl.jif.utils.diag.edt.ThreadCheckingRepaintManager;
import javax.swing.RepaintManager;

/**
 *
 * @author GBH
 */
public class DebuggingTools {
   
   /** Creates a new instance of DebuggingTools */
   public DebuggingTools() {
   }
   
   //  for JNI
   // java command line args:  -verbose:jni -Xcheck:jni 
   
   
   
   public static void doEDTmonitoring() {
   RepaintManager.setCurrentManager(new ThreadCheckingRepaintManager(true));
   }
}
