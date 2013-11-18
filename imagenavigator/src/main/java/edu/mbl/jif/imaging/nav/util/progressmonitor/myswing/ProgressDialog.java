package edu.mbl.jif.imaging.nav.util.progressmonitor;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

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
public class ProgressDialog extends JDialog implements ChangeListener{
    private JLabel statusLabel = new JLabel();
    private JProgressBar progressBar;
    private ProgressMonitor monitor;

    public ProgressDialog(Frame owner, ProgressMonitor monitor) throws HeadlessException{
        super(owner, "Progress", true);
        init(monitor);
    }

    public ProgressDialog(Dialog owner, ProgressMonitor monitor) throws HeadlessException{
        super(owner);
        init(monitor);
    }

    private void init(ProgressMonitor monitor){
        this.monitor = monitor;

        progressBar = new JProgressBar(0, monitor.getTotal());
        if(monitor.isIndeterminate())
            progressBar.setIndeterminate(true);
        else
            progressBar.setValue(monitor.getCurrent());
        statusLabel.setText(monitor.getStatus());

        JPanel contents = (JPanel)getContentPane();
        contents.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contents.add(statusLabel, BorderLayout.NORTH);
        contents.add(progressBar);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        monitor.addChangeListener(this);
    }

    public void stateChanged(final ChangeEvent ce){
        // to ensure EDT thread
        if(!SwingUtilities.isEventDispatchThread()){
            try{
                SwingUtilities.invokeAndWait(new Runnable(){
                    public void run(){
                        stateChanged(ce);
                    }
                });
            } catch(InterruptedException e){
                e.printStackTrace();
            } catch(InvocationTargetException e){
                // should never happen
                e.printStackTrace();
            }
            return;
        }

        if(!monitor.isFinished()){
            statusLabel.setText(monitor.getStatus());
            if(!monitor.isIndeterminate())
                progressBar.setValue(monitor.getCurrent());
        }else
            dispose();
    }
}
