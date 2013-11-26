/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.job;

import foxtrot.Job;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author GBH
 */
public abstract class MonitoredJob extends Job {

   private String description;
   private String title;
   private JobMonitorPanel jobMon;
   private boolean indeterminate;

   public MonitoredJob(String title, String description, boolean indeterminate) {
      this.title = title;
      this.description = description;
      this.indeterminate = indeterminate;
   }

   public JobMonitorPanel getMonitor() {
      return jobMon;
   }
   
   public void start(JFrame owner, boolean modal) {
      jobMon = new JobMonitorPanel();
      jobMon.setIndeterminate(indeterminate);
      jobMon.setTheJobToRun(this, description);
      //jobMon.setPreferredSize(new Dimension(300, 200));
      JDialog dialog = new JDialog(owner, title, modal);
      dialog.add(jobMon, BorderLayout.CENTER);
      if (owner != null) {
         dialog.setLocationRelativeTo(owner);
      }
      dialog.pack();
      dialog.setVisible(true); // pop up dialog
   }
}
