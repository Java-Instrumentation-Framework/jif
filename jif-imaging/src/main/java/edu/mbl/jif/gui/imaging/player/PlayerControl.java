package edu.mbl.jif.gui.imaging.player;

import edu.mbl.jif.gui.slider.SpinInteger;
import edu.mbl.jif.utils.JifUtils;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;



public class PlayerControl extends JPanel {
  private JPanel panel_Buttons = new JPanel();
  private JButton button_StepBack = new JButton();
  private JButton button_Begin = new JButton();
  private JButton button_PlayRev = new JButton();
  private JButton button_StepFwd = new JButton();
  public JButton button_Pause = new JButton();
  private JButton button_End = new JButton();
  private JButton button_PlayFwd = new JButton();
  private JSlider slider = new JSlider();
  private ChangeListener sliderListener;
  private JToggleButton button_Repeat = new JToggleButton();
  public boolean repeat = false;
  private JToggleButton button_Bounce = new JToggleButton();
  public boolean bounce = false;
  private SpinInteger spin_FPS;
  private boolean isPlaying = false;
  private int currentFrame = 0;
  private int lastFrame = 0;
  private Border border1;
  JLabel valueMax = new JLabel();
  JLabel valueCurrent = new JLabel();
  JLabel labelFPS = new JLabel();

  public PlayerControl() {
    super();
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    //
    setLayout(null);
    setBorder(BorderFactory.createLoweredBevelBorder());
    setOpaque(true);
    //
    //
    button_StepBack.setMargin(new Insets(0, 0, 0, 0));
    button_StepBack.setIcon(JifUtils.loadImageIcon("playPrev.gif", PlayerControl.class));
    button_StepBack.setToolTipText("Step to Previous Frame");
    button_StepBack.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_StepBack_actionPerformed(e);
      }
    });
    button_Begin.setMargin(new Insets(0, 0, 0, 0));
    button_Begin.setIcon(JifUtils.loadImageIcon("playBegin.gif", PlayerControl.class));
    button_Begin.setToolTipText("Go to Beginning");
    button_Begin.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_Begin_actionPerformed(e);
      }
    });
    button_PlayRev.setMargin(new Insets(0, 0, 0, 0));
    button_PlayRev.setIcon(JifUtils.loadImageIcon("playRev.gif", PlayerControl.class));
    button_PlayRev.setToolTipText("Play in Reverse");
    button_PlayRev.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_PlayRev_actionPerformed(e);
      }
    });
    button_StepFwd.setMargin(new Insets(0, 0, 0, 0));
    button_StepFwd.setIcon(JifUtils.loadImageIcon("playNext.gif", PlayerControl.class));
    button_StepFwd.setToolTipText("Step to Next Frame");
    button_StepFwd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_StepFwd_actionPerformed(e);
      }
    });
    button_Pause.setMargin(new Insets(0, 0, 0, 0));
    button_Pause.setIcon(JifUtils.loadImageIcon("playPause.gif", PlayerControl.class));
    button_Pause.setToolTipText("Pause");
    button_Pause.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_Pause_actionPerformed(e);
      }
    });
    button_End.setMargin(new Insets(0, 0, 0, 0));
    button_End.setIcon(JifUtils.loadImageIcon("playEnd.gif", PlayerControl.class));
    button_End.setToolTipText("Go to End");
    button_End.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_End_actionPerformed(e);
      }
    });
    button_PlayFwd.setMargin(new Insets(0, 0, 0, 0));
    button_PlayFwd.setIcon(JifUtils.loadImageIcon("playFwd.gif", PlayerControl.class));
    button_PlayFwd.setToolTipText("Play Forward");
    button_PlayFwd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_PlayFwd_actionPerformed(e);
      }
    });

    //
    button_Repeat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_Repeat_actionPerformed(e);
      }
    });
    button_Bounce.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_Bounce_actionPerformed(e);
      }
    });
    //
    border1 = BorderFactory.createEmptyBorder();
    panel_Buttons.setBorder(border1);
    //panel_Buttons.setBorder(BorderFactory.createRaisedBevelBorder());
    panel_Buttons.setBounds(new Rectangle(2, 2, 249, 41));
    panel_Buttons.setOpaque(false);

    valueMax.setFont(new java.awt.Font("Dialog", 1, 12));
    valueMax.setText("/ MAX");
    valueMax.setBounds(new Rectangle(54, 45, 37, 19));
    valueCurrent.setFont(new java.awt.Font("Dialog", 1, 12));
    valueCurrent.setHorizontalAlignment(SwingConstants.RIGHT);
    valueCurrent.setBounds(new Rectangle(9, 44, 41, 21));
    valueCurrent.setText("0");
    labelFPS.setFont(new java.awt.Font("Dialog", 0, 10));
    labelFPS.setHorizontalAlignment(SwingConstants.RIGHT);
    labelFPS.setText("fps");
    labelFPS.setBounds(new Rectangle(169, 46, 21, 15));
    slider.setFont(new java.awt.Font("Dialog", 0, 10));
    panel_Buttons.add(button_Begin, null);
    panel_Buttons.add(button_PlayRev, null);
    panel_Buttons.add(button_StepBack, null);
    panel_Buttons.add(button_Pause, null);
    panel_Buttons.add(button_StepFwd, null);
    panel_Buttons.add(button_PlayFwd, null);
    panel_Buttons.add(button_End, null);
    //
    button_Repeat.setBounds(new Rectangle(113, 42, 23, 24));
    button_Repeat.setToolTipText("Repeat");
    button_Repeat.setIcon(JifUtils.loadImageIcon("repeat.gif", PlayerControl.class));
    button_Repeat.setMargin(new Insets(2, 1, 2, 1));
    button_Bounce.setIcon(JifUtils.loadImageIcon("bounce.gif", PlayerControl.class));
    button_Bounce.setMargin(new Insets(2, 1, 2, 1));
    button_Bounce.setToolTipText("Bounce (forward/backward)");
    button_Bounce.setBounds(new Rectangle(140, 42, 23, 24));
    //
    slider.setMaximum(lastFrame - 1);
    slider.setMinimum(0);
    slider.setValue(0);
    slider.setMinorTickSpacing(1);
    slider.setMajorTickSpacing(10);
    slider.setPaintTicks(true);
    slider.setSnapToTicks(true);
    slider.setBounds(new Rectangle(7, 69, 248, 27));
    slider.setOpaque(false);
    slider.setBackground(Color.lightGray);
    //
    final SpinnerNumberModel modelNum = new SpinnerNumberModel(2, 1, 30, 1);
    spin_FPS = new SpinInteger("", modelNum, "##", 3, 12);
    modelNum.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        fps_Changed(modelNum.getNumber().intValue());
      }
    });
    spin_FPS.setBounds(new Rectangle(188, 40, 62, 28));
    spin_FPS.setOpaque(false);
    this.setBackground(Color.lightGray);
    this.setPreferredSize(new Dimension(256, 100));
    this.add(panel_Buttons, null);
    this.add(spin_FPS, null);
    this.add(valueCurrent, null);
    this.add(valueMax, null);
    this.add(button_Repeat, null);
    this.add(button_Bounce, null);
    this.add(labelFPS, null);
    this.add(slider, null);
  }

  public void setLastFrame(int numFrames) {
    lastFrame = numFrames;
    slider.setMaximum(lastFrame - 1);
    valueMax.setText("/ " + String.valueOf(lastFrame));
    sliderListener =
        new ChangeListener() {
      // This method is called whenever the slider's value is changed
      public void stateChanged(ChangeEvent evt) {
        JSlider slider = (JSlider) evt.getSource();

        //System.out.println("SliderEvent: " + evt);
        //if (!slider.getValueIsAdjusting()) {
        int value = slider.getValue();
        sliderMoved(value);
        //}
      }
    };
    enableSliderListener(true);

  }

  public void setbackground(Color color) {
    this.setBackground(color);
    slider.setBackground(color);
  }

  void fps_Changed(int value) {
  }

  public void setFPS(int fps) {
    spin_FPS.setValue(fps);
  }

  ////////////////////////////////////////////////////////////////
  void button_Begin_actionPerformed(ActionEvent e) {
    updateSliderPosition(currentFrame);
  }

  void button_PlayRev_actionPerformed(ActionEvent e) {
    updateSliderPosition(currentFrame);
  }

  void button_StepBack_actionPerformed(ActionEvent e) {
    updateSliderPosition(currentFrame);
  }

  void button_Pause_actionPerformed(ActionEvent e) {
    updateSliderPosition(currentFrame);
  }

  void button_StepFwd_actionPerformed(ActionEvent e) {
    updateSliderPosition(currentFrame);
  }

  void button_PlayFwd_actionPerformed(ActionEvent e) {
    updateSliderPosition(currentFrame);
  }

  void button_End_actionPerformed(ActionEvent e) {
    updateSliderPosition(currentFrame);
  }

  void sliderMoved(int i) {
    System.out.println("Slider: " + i);
    valueCurrent.setText(String.valueOf(i));
  }

  void setCurrentDisplay(int n) {
    valueCurrent.setText(String.valueOf(n));
  }

  ////////////////////////////////////////////////////////////////
  void updateSliderPosition(int frame) {
    final int f = frame;
        edu.mbl.jif.gui.swingthread.SwingWorker3 worker = new edu.mbl.jif.gui.swingthread.SwingWorker3(){
      public Object construct() {
        slider.setValue(f);
        slider.repaint();
        valueCurrent.setText(String.valueOf(f+1));
        return null;
      }

      public void finished() {
      }
    };
    worker.start();
  }

  public void enableSliderListener(boolean t) {
    if (t) {
      slider.addChangeListener(sliderListener);
    } else {
      slider.removeChangeListener(sliderListener);
    }
  }

  public void setEnabledAll(boolean t) {
    button_Begin.setEnabled(t);
    button_PlayRev.setEnabled(t);
    button_StepBack.setEnabled(t);
    button_Pause.setEnabled(t);
    button_StepFwd.setEnabled(t);
    button_PlayFwd.setEnabled(t);
    button_End.setEnabled(t);
  }

  void button_Repeat_actionPerformed(ActionEvent e) {
    if (button_Repeat.isSelected()) {
      button_Bounce.setSelected(false);
      bounce = false;
      repeat = true;
    } else {
      repeat = false;
    }
  }

  void button_Bounce_actionPerformed(ActionEvent e) {
    if (button_Bounce.isSelected()) {
      button_Repeat.setSelected(false);
      repeat = false;
      bounce = true;
    } else {
      bounce = false;
    }
  }

  public boolean isRepeat() {
    return repeat;
  }

  public boolean isBounce() {
    return bounce;
  }

  public static void main(String[] args) {
    JFrame f = new JFrame();
    PlayerControl pc = new PlayerControl();
    pc.setLastFrame(33);
    pc.setBackground(Color.lightGray);
    f.getContentPane().add(pc, BorderLayout.CENTER);
    f.pack();
    f.validate();
    f.setVisible(true);
  }
}
