/*
 * ChannelSelector.java
 *
 * Created on December 7, 2006, 10:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.imaging;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author GBH
 */
public class ChannelSelector extends JPanel {
    
    /** Creates a new instance of ChannelSelector */
    public ChannelSelector() {
       // box vertical..  this.setLayout(new)
        JRadioButton b = new JRadioButton("1");
    }
    // interface ChannelStack
    // implemented by PolStack
    // addChannel("lable", icon, channelType [, bufferedImage]);
    // channelType = sample | derived
}
