/*
 * SimpleQTJ16Player.java
 *
 * Created on July 17, 2006, 12:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.imaging.qt;

/*
  (c) 2004, Chris Adamson, invalidname@mac.com
  all rights granted - use as you see fit
*/

// no package statement for this version,
// just to keep compile-and-run section simple

import java.awt.*;
import java.awt.event.*;
import java.io.*;
//import quicktime.*;
//import quicktime.io.*;
//import quicktime.std.movies.*;
//import quicktime.app.view.*;

public class SimpleQTJ16Player extends Frame {
//
//  Movie movie;
//
//  public SimpleQTJ16Player (String title) {
//    super (title);
//    try {
//
//      FileDialog fd = new FileDialog (this,
//                                      "Select source movie",
//                                      FileDialog.LOAD);
//      fd.setVisible(true);
//      if (fd.getFile() == null)
//          return;
//      // get movie from file
//      File f = new File (fd.getDirectory(), fd.getFile());
//      
//      
//      QTSession.open();
//      OpenMovieFile omFile =
//        OpenMovieFile.asRead (new QTFile (f));
//      movie = Movie.fromFile (omFile);
//      // get a MovieController, then a Component
//      MovieController controller = new MovieController (movie);
//
//      Component canvas = (Component)
//          QTFactory.makeQTComponent (controller);
//
//      /* controller-less alternative
//      Component canvas = (Component)
//          QTFactory.makeQTComponent (movie);
//      */
//
//      System.out.println ("QTFactory gave us a " + 
//                          canvas.getClass().getName());
//      add (canvas);
//      // windows-like close-to-quit
//      addWindowListener (new WindowAdapter() {
//        public void windowClosing (WindowEvent e) {
//          QTSession.close();
//          System.exit(0);
//        }
//      });
//    } catch (Exception e) {
//      e.printStackTrace();
//    } 
//  }
//
//  public static void main (String[] args) {
//    System.out.println ("java version is " +
//                        System.getProperty ("java.version"));
//    SimpleQTJ16Player frame =
//      new SimpleQTJ16Player ("Simple QTJ Player");
//    frame.pack();
//    frame.setVisible(true);
//    try {
//      frame.movie.start();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
}