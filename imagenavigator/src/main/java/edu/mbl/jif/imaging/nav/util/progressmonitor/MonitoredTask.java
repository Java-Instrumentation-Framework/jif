package edu.mbl.jif.imaging.nav.util.progressmonitor;


import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingWorker;

/**
 * Add indeterminate...
 *
 * @author GBH
 */
public class MonitoredTask implements PropertyChangeListener {

   private ModalProgressMonitor progressMonitor;
   private SwingWorker task;
   private boolean indeterminate = false;

   public MonitoredTask(SwingWorker task, String msg, Component parentComponent) {
      this.task = task;
      // create the progress monitor
      progressMonitor = new ModalProgressMonitor(parentComponent, msg, "", 0, 100);
      progressMonitor.setProgress(0);
      // add ProgressMonitorExample as a listener on CopyFiles;
      // of specific interest is the bound property progress
      task.addPropertyChangeListener(this);
   }

   public void start() {
      //task.execute();
      if (indeterminate) {
         progressMonitor.setIndeterminate(true);
      }
      SwingWorkerExecutor.getInstance().execute(task);
   }

   public void setIndeterminate(boolean indeterminate) {
      this.indeterminate = indeterminate;
   }

   // executes in event dispatch thread
   public void propertyChange(PropertyChangeEvent event) {
      // if the operation is finished or has been canceled by
      // the user, take appropriate action
      if (progressMonitor.isCanceled()) {
         task.cancel(true);
      } else if (event.getPropertyName().equals("progress")) {
         // get the % complete from the progress event
         // and set it on the progress monitor
         int progress = ((Integer) event.getNewValue()).intValue();
         progressMonitor.setProgress(progress);
         if (progress >= 100) {
            progressMonitor.close();
         }
      }
   }
}
