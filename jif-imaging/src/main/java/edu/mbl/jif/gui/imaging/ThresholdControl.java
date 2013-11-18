package edu.mbl.jif.gui.imaging;

import javax.media.jai.*;
import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import java.util.*;



public class ThresholdControl extends JPanel implements ChangeListener {
  private PlanarImage  source = null;
  private PlanarImage  target = null;
  private double[]     low;
  private double[]     high;
  private double[]     map;
  private ImageDisplayPanel idp;
  private int highest = 255;


  public ThresholdControl(ImageDisplayPanel _idp) {
    idp = _idp; // contains the image.
    low = new double[1];
    high = new double[1];
    map = new double[1];
    low[0] = 0.0F;
    high[0] = 0.0F;
    map[0] = 0.0F;
    JSlider   slider = new JSlider(JSlider.HORIZONTAL, 0, highest, 0);
    Hashtable labels = new Hashtable();
    labels.put(new Integer(0), new JLabel("0"));
    labels.put(new Integer(255), new JLabel("255"));
    slider.setLabelTable(labels);
    slider.setPaintLabels(true);
    slider.addChangeListener(this);
    slider.setEnabled(true);
    slider.setOpaque(false);
    slider.setFont(new java.awt.Font("Dialog", 0, 10));
    //JPanel borderedPane = new JPanel();
    //setLayout(new BorderLayout());
    //setBorder(BorderFactory.createTitledBorder("Threshold"));
    add(slider, BorderLayout.NORTH);
    //add(borderedPane, BorderLayout.SOUTH);
  }

  // called by the ImageDisplayPanel component to update the current image
  public void setCurrentImage(BufferedImage image) {
     ParameterBlock pb = new ParameterBlock();
     pb.add(image);
     source = (PlanarImage)JAI.create("awtImage", pb);
  }

  public final void stateChanged(ChangeEvent e) {
    JSlider slider = (JSlider) e.getSource();
    high[0] = (double) slider.getValue();
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(source);
    pb.add(low);
    pb.add(high);
    pb.add(map);
    target = JAI.create("threshold", pb, null);
    idp.changeImage(target.getAsBufferedImage());

  }
}

