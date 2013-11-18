package edu.mbl.jif.gui.imaging.magnify;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;


public class DetachedMagnifyingGlass extends JComponent implements MouseMotionListener {
   double     zoom;
   JComponent comp;
   Point      point;
   Dimension  mySize;
   Robot      robot;
   PopupMenu  m;

   public DetachedMagnifyingGlass(JComponent comp, Dimension size, double zoom) {
      this.comp = comp;
      // flag to say don't draw until we get a MouseMotionEvent
      point = new Point(-1, -1);
      comp.addMouseMotionListener(this);
      this.mySize = size;
      this.zoom = zoom;
      // if we can't get a robot, then we just never
      // paint anything
      try {
         robot = new Robot();
      } catch (AWTException awte) {
         System.err.println("Can't get a Robot");
         awte.printStackTrace();
      }
      addComponentListener(
         new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
               onResize();
            }
         });

      m = new PopupMenu();
      ActionListener menuListener =
         new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               System.out.println(
                     "Popup menu item [" + event.getActionCommand() + "] was pressed.");
               String cmd         = event.getActionCommand();
               if (cmd.equalsIgnoreCase("2")) {
                  setZoom(2.0);
               }
               if (cmd.equalsIgnoreCase("4")) {
                  setZoom(4.0);
               }
               if (cmd.equalsIgnoreCase("8")) {
                  setZoom(8.0);
               }
               if (cmd.equalsIgnoreCase("16")) {
                  setZoom(16.0);
               }
               if (cmd.equalsIgnoreCase("32")) {
                  setZoom(32.0);
               }
            }
         };
      MenuItem item;
      item = new MenuItem("2");
      item.addActionListener(menuListener);
      m.add(item);
      item = new MenuItem("4");
      item.addActionListener(menuListener);
      m.add(item);
      item = new MenuItem("8");
      item.addActionListener(menuListener);
      m.add(item);
      item = new MenuItem("16");
      item.addActionListener(menuListener);
      m.add(item);
      item = new MenuItem("32");
      item.addActionListener(menuListener);
      m.add(item);
      add(m); // add Popup to Component
      enableEvents(AWTEvent.MOUSE_EVENT_MASK);
   }

   public void processMouseEvent(MouseEvent me) {
      //System.err.println("MouseEvent: " + me);
      if (me.isPopupTrigger()) {
         m.show(this, me.getX(), me.getY());
      } else {
         super.processMouseEvent(me);
      }
   }
   ;
   public void setZoom(double zoom) {
      this.zoom = zoom;
      repaint();
   }


   public void onResize() {
      mySize = getSize();
      repaint();
   }


   public void paint(Graphics g) {
      if ((robot == null) || (point.x == -1)) {
         g.setColor(Color.blue);
         g.fillRect(0, 0, mySize.width, mySize.height);
         return;
      }
      Rectangle     grabRect = computeGrabRect();
      BufferedImage grabImg  = robot.createScreenCapture(grabRect);
      Image         scaleImg =
         grabImg.getScaledInstance(mySize.width, mySize.height, Image.SCALE_FAST);
      g.drawImage(scaleImg, 0, 0, null);
      g.setColor(Color.blue);
      g.drawLine(
         (mySize.width / 2) - 10,
         mySize.height / 2,
         (mySize.width / 2) + 10,
         mySize.height / 2);
      g.drawLine(
         mySize.width / 2,
         (mySize.height / 2) - 10,
         mySize.width / 2,
         (mySize.height / 2) + 10);
   }


   public void setSize(Dimension dim) {
      mySize = dim;
      repaint();
   }


   private Rectangle computeGrabRect() {
      // width, height are size of this comp / zoom
      int grabWidth  = (int)((double)mySize.width / zoom);
      int grabHeight = (int)((double)mySize.height / zoom);

      // upper left corner is current point
      return new Rectangle(point.x, point.y, grabWidth, grabHeight);
   }


   public Dimension getPreferredSize() {
      return mySize;
   }


   public Dimension getMinimumSize() {
      return mySize;
   }


   public Dimension getMaximumSize() {
      return mySize;
   }


   // MouseMotionListener implementations
   public void mouseMoved(MouseEvent e) {
      Point offsetPoint = comp.getLocationOnScreen();
      offsetPoint.translate(
         -(int)(this.getWidth() / zoom / 2),
         -(int)(this.getHeight() / zoom / 2));
      e.translatePoint(offsetPoint.x, offsetPoint.y);
      point = e.getPoint();
      repaint();
   }


   public void mouseDragged(MouseEvent e) {
      mouseMoved(e);
   }
}
/*   Swing Hacks
 *   Tips and Tools for Killer GUIs
 * By Joshua Marinacci, Chris Adamson
 *   First Edition June 2005
 *   Series: Hacks
 *   ISBN: 0-596-00907-0
 *   http://www.oreilly.com/catalog/swinghks
 */
