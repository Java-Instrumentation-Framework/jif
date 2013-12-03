package edu.mbl.jif.gui.imaging.player;

import edu.mbl.jif.gui.slider.SpinInteger;
import edu.mbl.jif.utils.StaticSwingUtils;
import edu.mbl.jif.gui.*;
import edu.mbl.jif.utils.JifUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;

public abstract class PlayerControlHoriz extends JPanel {

    private JPanel panel_Buttons = new JPanel();
    protected JPanel panel_Lower = new JPanel();
    protected JPanel panel_Upper = new JPanel();
    private JButton button_StepBack = new JButton();
    private JButton button_Begin = new JButton();
    private JButton button_PlayRev = new JButton();
    private JButton button_StepFwd = new JButton();
    public JButton button_Pause = new JButton();
    private JButton button_End = new JButton();
    private JButton button_PlayFwd = new JButton();
    public JToggleButton togglePlayStop = new JToggleButton();
    private JSlider slider = new JSlider();
    private ChangeListener sliderListener;
    private JToggleButton button_Repeat = new JToggleButton();
    protected boolean repeat = false;
    private JToggleButton button_Bounce = new JToggleButton();
    protected boolean bounce = false;
    private SpinInteger spin_FPS;
    private boolean isPlaying = false;
    private int currentFrame = 0;
    private int lastFrame = 0;
    private Border border1;
    JLabel valueMax = new JLabel();
    JLabel valueCurrent = new JLabel();
    Component component1;
    Component component2;

    public PlayerControlHoriz() {
        super();
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        String imagePath = "edu/mbl/jif/gui/imaging/player/icons";

        component1 = Box.createHorizontalStrut(7);
        component2 = Box.createHorizontalStrut(8);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLoweredBevelBorder());
        setOpaque(true);
        //
        button_StepBack.setMargin(new Insets(0, 0, 0, 0));
        button_StepBack.setIcon(JifUtils.loadImageIcon("playPrev.gif", imagePath));
        button_StepBack.setMaximumSize(new Dimension(24, 24));
        button_StepBack.setMinimumSize(new Dimension(24, 24));
        button_StepBack.setPreferredSize(new Dimension(24, 24));
        button_StepBack.setToolTipText("Step to Previous Frame");
        button_StepBack.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_StepBack_actionPerformed(e);
            }

        });
        button_Begin.setMargin(new Insets(0, 0, 0, 0));
        button_Begin.setIcon(JifUtils.loadImageIcon("playBegin.gif", imagePath));
        button_Begin.setMaximumSize(new Dimension(24, 24));
        button_Begin.setMinimumSize(new Dimension(24, 24));
        button_Begin.setPreferredSize(new Dimension(24, 24));
        button_Begin.setToolTipText("Go to Beginning");
        button_Begin.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_Begin_actionPerformed(e);
            }

        });
        button_PlayRev.setMargin(new Insets(0, 0, 0, 0));
        button_PlayRev.setIcon(JifUtils.loadImageIcon("playRev.gif", imagePath));
        button_PlayRev.setMaximumSize(new Dimension(24, 24));
        button_PlayRev.setMinimumSize(new Dimension(24, 24));
        button_PlayRev.setPreferredSize(new Dimension(24, 24));
        button_PlayRev.setToolTipText("Play in Reverse");
        button_PlayRev.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_PlayRev_actionPerformed(e);
            }

        });
        button_StepFwd.setMargin(new Insets(0, 0, 0, 0));
        button_StepFwd.setIcon(JifUtils.loadImageIcon("playNext.gif", imagePath));
        button_StepFwd.setMaximumSize(new Dimension(24, 24));
        button_StepFwd.setMinimumSize(new Dimension(24, 24));
        button_StepFwd.setPreferredSize(new Dimension(24, 24));
        button_StepFwd.setToolTipText("Step to Next Frame");
        button_StepFwd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_StepFwd_actionPerformed(e);
            }

        });
        button_Pause.setMargin(new Insets(0, 0, 0, 0));
        button_Pause.setIcon(JifUtils.loadImageIcon("playPause.gif", imagePath));
        button_Pause.setMaximumSize(new Dimension(24, 24));
        button_Pause.setMinimumSize(new Dimension(24, 24));
        button_Pause.setPreferredSize(new Dimension(24, 24));
        button_Pause.setToolTipText("Pause");
        button_Pause.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_Pause_actionPerformed(e);
            }

        });
        button_End.setMargin(new Insets(0, 0, 0, 0));
        button_End.setIcon(JifUtils.loadImageIcon("playEnd.gif", imagePath));
        button_End.setMaximumSize(new Dimension(24, 24));
        button_End.setMinimumSize(new Dimension(24, 24));
        button_End.setPreferredSize(new Dimension(24, 24));
        button_End.setToolTipText("Go to End");
        button_End.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_End_actionPerformed(e);
            }

        });
        button_PlayFwd.setMargin(new Insets(0, 0, 0, 0));
        button_PlayFwd.setIcon(JifUtils.loadImageIcon("playFwd.gif", imagePath));
        button_PlayFwd.setMaximumSize(new Dimension(24, 24));
        button_PlayFwd.setMinimumSize(new Dimension(24, 24));
        button_PlayFwd.setPreferredSize(new Dimension(24, 24));
        button_PlayFwd.setToolTipText("Play Forward");
        button_PlayFwd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_PlayFwd_actionPerformed(e);
            }

        });

        togglePlayStop.setMaximumSize(new Dimension(24, 24));
        togglePlayStop.setMinimumSize(new Dimension(24, 24));
        togglePlayStop.setPreferredSize(new Dimension(24, 24));
        togglePlayStop.setIcon(JifUtils.loadImageIcon("playFwd.gif", imagePath));
        togglePlayStop.setSelectedIcon(JifUtils.loadImageIcon("playPause.gif", imagePath));
        togglePlayStop.setMargin(new Insets(1, 1, 1, 1));
        togglePlayStop.setToolTipText("Play / Stop");

        togglePlayStop.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent ev) {
                //System.out.println("ItemEvent!" + ev.getStateChange());
                if (ev.getStateChange() == ItemEvent.DESELECTED) {
                    //System.out.println("Deselected");
                    button_Pause_actionPerformed(null);
                }
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    //System.out.println("Selected");
                    button_PlayFwd_actionPerformed(null);
                }
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
        panel_Buttons.setLayout(new BoxLayout(panel_Buttons, BoxLayout.X_AXIS));
        //panel_Buttons.setBorder(BorderFactory.createRaisedBevelBorder());
        panel_Buttons.setOpaque(false);

        valueMax.setFont(new java.awt.Font("Dialog", 0, 10));
        valueMax.setMaximumSize(new Dimension(30, 14));
        valueMax.setMinimumSize(new Dimension(30, 14));
        valueMax.setPreferredSize(new Dimension(30, 14));
        valueMax.setText("/ MAX");
        valueCurrent.setFont(new java.awt.Font("Dialog", 0, 10));
        valueCurrent.setMaximumSize(new Dimension(24, 14));
        valueCurrent.setMinimumSize(new Dimension(24, 14));
        valueCurrent.setPreferredSize(new Dimension(24, 14));
        valueCurrent.setHorizontalAlignment(SwingConstants.RIGHT);
        valueCurrent.setHorizontalTextPosition(SwingConstants.RIGHT);
        valueCurrent.setText("0");
        button_Repeat.setMaximumSize(new Dimension(24, 24));
        button_Repeat.setMinimumSize(new Dimension(24, 24));
        button_Repeat.setPreferredSize(new Dimension(24, 24));
        button_Bounce.setMaximumSize(new Dimension(24, 24));
        button_Bounce.setMinimumSize(new Dimension(24, 24));
        button_Bounce.setPreferredSize(new Dimension(24, 24));

        panel_Lower.setOpaque(false);
        panel_Upper.setOpaque(false);
        panel_Buttons.add(component1, null);
        panel_Buttons.add(button_Begin, null);
        // panel_Buttons.add(button_PlayRev, null);
        panel_Buttons.add(button_StepBack, null);
        //panel_Buttons.add(button_Pause, null);
        panel_Buttons.add(button_StepFwd, null);
        //panel_Buttons.add(button_PlayFwd, null);
        panel_Buttons.add(togglePlayStop, null);
        panel_Buttons.add(button_End, null);
        //
        button_Repeat.setToolTipText("Repeat");
        button_Repeat.setIcon(JifUtils.loadImageIcon("repeat.gif", imagePath));
        button_Repeat.setMargin(new Insets(2, 1, 2, 1));
        button_Bounce.setIcon(JifUtils.loadImageIcon("bounce.gif", imagePath));
        button_Bounce.setMargin(new Insets(2, 1, 2, 1));
        button_Bounce.setToolTipText("Bounce (forward/backward)");
        //
        slider.setPreferredSize(new Dimension(100, 27));
        slider.setMaximum(lastFrame - 1);
        slider.setMinimum(0);
        slider.setValue(0);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
        slider.setSnapToTicks(true);
        slider.setOpaque(true);
        slider.setBackground(Color.lightGray);

        //
        final SpinnerNumberModel modelNum = new SpinnerNumberModel(2, 1, 100, 1);
        spin_FPS = new SpinInteger("fps", modelNum, "##", 3, 10);
        modelNum.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                fps_Changed(modelNum.getNumber().intValue());
            }

        });
        spin_FPS.setOpaque(false);
        spin_FPS.setMaximumSize(new Dimension(100, 24));
        spin_FPS.setMinimumSize(new Dimension(70, 24));
        spin_FPS.setPreferredSize(new Dimension(70, 24));

        this.setBackground(Color.lightGray);
        this.setPreferredSize(new Dimension(256, 100));
        this.setMinimumSize(new Dimension(200, 100));
        panel_Lower.setLayout(new BoxLayout(panel_Lower, BoxLayout.X_AXIS));
        panel_Upper.setLayout(new BoxLayout(panel_Upper, BoxLayout.X_AXIS));

        panel_Lower.add(panel_Buttons, null);
        panel_Lower.add(spin_FPS, null);
        panel_Lower.add(button_Repeat, null);
        panel_Lower.add(button_Bounce, null);
        panel_Lower.add(component2, null);
        panel_Lower.add(valueCurrent, null);
        panel_Lower.add(valueMax, null);

        panel_Upper.add(slider, null);
        this.add(panel_Upper);
        this.add(panel_Lower);
    }

    public void setLastFrame(int numFrames) {
        lastFrame = numFrames;
        StaticSwingUtils.dispatchToEDT(new Runnable() {
            public void run() {
                slider.setMaximum(lastFrame - 1);
                valueMax.setText("/ " + String.valueOf(lastFrame));
            }
        });
        sliderListener = new ChangeListener() {
            // This method is called whenever the slider's value is changed
            public void stateChanged(ChangeEvent evt) {
                JSlider slider = (JSlider) evt.getSource();
                int value = slider.getValue();
                sliderMoved(value);
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

    void button_Begin_actionPerformed(ActionEvent e) {
        // updateSliderPosition(currentFrame);
    }

    void button_PlayRev_actionPerformed(ActionEvent e) {
        // updateSliderPosition(currentFrame);
    }

    void button_StepBack_actionPerformed(ActionEvent e) {
        // updateSliderPosition(currentFrame);
    }

    void button_Pause_actionPerformed(ActionEvent e) {
        // updateSliderPosition(currentFrame);
    }

    void button_StepFwd_actionPerformed(ActionEvent e) {
        //  updateSliderPosition(currentFrame);
    }

    void button_PlayFwd_actionPerformed(ActionEvent e) {
        //  updateSliderPosition(currentFrame);
    }

    void button_End_actionPerformed(ActionEvent e) {
        //  updateSliderPosition(currentFrame);
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

    public void setCurrentDisplay(final int n) {
        StaticSwingUtils.dispatchToEDT(new Runnable() {
            public void run() {
                valueCurrent.setText(String.valueOf(n));
            }
        });
    }

    abstract void sliderMoved(int i);
    //{
//        System.out.println("Slider: " + i);
//        valueCurrent.setText(String.valueOf(i));
    //}
    
    
    public void updateSliderPosition(int frame) {
        final int f = frame;
        StaticSwingUtils.dispatchToEDT(new Runnable() {

            public void run() {
                slider.setValue(f);
                slider.repaint();
                valueCurrent.setText(String.valueOf(f + 1));
            }

        });
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

    public static void main(String[] args) {
//        JFrame f = new JFrame();
//        PlayerControlHoriz pc = new PlayerControlHoriz();
//        pc.setLastFrame(33);
//        pc.setBackground(Color.lightGray);
//        f.getContentPane().add(pc, BorderLayout.CENTER);
//        f.pack();
//        //f.validate();
//        f.setVisible(true);
    }

}
