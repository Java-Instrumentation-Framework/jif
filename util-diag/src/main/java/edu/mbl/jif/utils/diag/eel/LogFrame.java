package edu.mbl.jif.utils.diag.eel;
/*
 * LogFrame.java
 */

/**
 * This class displays a frame for event logging from the EEL system.
 * You can clear messages from the screen or completely dismiss the
 * window.  This class is meant to be attached to an EEL
 * instance, but could be used as a standalone component.
 * <p>
 * To use this component, you should call <code>EEL.showFrame()</code>.
 * You can remove the component from the screen by calling
 * <code>EEL.hideFrame()</code>.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LogFrame extends JFrame {
  JTextArea screen;

  public LogFrame() {
    super("Event Logging Window");
    setSize(400,600);

    screen = new JTextArea();
    screen.setEditable(false);

    JButton clearB, closeB;
    JPanel buttonPane = new JPanel();
    clearB = new JButton("Clear");
    closeB = new JButton("Close");
    buttonPane.add(clearB);
    buttonPane.add(closeB);

    getContentPane().add(new JScrollPane(screen), BorderLayout.CENTER);
    getContentPane().add(buttonPane, BorderLayout.SOUTH);

    clearB.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
	  screen.setText("");
	}
      });

    closeB.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
	  setVisible(false);
	}
      });
  }

  public void log(String msg) {
    screen.append(msg);
    if (!msg.endsWith("\n")) {
      screen.append("\n");
    }
  }
}
