package edu.mbl.jif.imaging.nav.util.progressmonitor;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * MySwing: Advanced Swing Utilites
 * Copyright (C) 2005  Santhosh Kumar T
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

/**
 * @author Santhosh Kumar T
 * @email  santhosh@in.fiorano.com
 */
public class ProgressUtil{
    static class MonitorListener implements ChangeListener, ActionListener{
        private ProgressMonitor monitor;
        private Timer timer;

        public MonitorListener(ProgressMonitor monitor){
            this.monitor = monitor;
        }

        public void stateChanged(ChangeEvent ce){
            ProgressMonitor monitor = (ProgressMonitor)ce.getSource();
            if(!monitor.isFinished()){
                if(timer==null){
                    timer = new Timer(monitor.getMilliSecondsToWait(), this);
                    timer.setRepeats(false);
                    timer.start();
                }
            }else{
                if(timer!=null && timer.isRunning())
                    timer.stop();
                monitor.removeChangeListener(this);
            }
        }

        public void actionPerformed(ActionEvent e){
            monitor.removeChangeListener(this);
            if(!monitor.isFinished()){ // better to check again
                Window owner = ActiveWindowTracker.findActiveWindow();
                ProgressDialog dlg = owner instanceof Frame
                        ? new ProgressDialog((Frame)owner, monitor)
                        : new ProgressDialog((Dialog)owner, monitor);
                dlg.pack();
                dlg.setLocationRelativeTo(null);
                if(!monitor.isFinished()) // better to check again
                    dlg.setVisible(true);
            }
        }
    }

    public static ProgressMonitor createModalProgressMonitor(int total, boolean indeterminate){
        ProgressMonitor monitor = new ProgressMonitor(total, indeterminate);
        monitor.addChangeListener(new MonitorListener(monitor));
        return monitor;
    }

    public static ProgressMonitor createModalProgressMonitor(int total, boolean indeterminate, int milliSecondsToWait){
        ProgressMonitor monitor = new ProgressMonitor(total, indeterminate, milliSecondsToWait);
        monitor.addChangeListener(new MonitorListener(monitor));
        return monitor;
    }
}