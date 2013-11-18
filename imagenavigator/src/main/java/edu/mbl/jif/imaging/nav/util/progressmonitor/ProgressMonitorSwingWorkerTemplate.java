package edu.mbl.jif.imaging.nav.util.progressmonitor;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


public class ProgressMonitorSwingWorkerTemplate extends JPanel implements ActionListener {

   private static final int DEFAULT_WIDTH = 700;
   private static final int DEFAULT_HEIGHT = 350;
   private JButton runTaskButton;
   private JTextArea console;
   //
   private TheTask operation; // a SwingWorker

   public static void main(String[] args) {
      // tell the event dispatch thread to schedule the execution
      // of this Runnable (which will create the example app GUI) for a later time
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            // create example app window
            JFrame frame = new JFrame("Progress Monitor Example");
            // application will exit on close
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

            // create example app content pane
            // ProgressMonitorExample constructor does additional GUI setup
            JComponent contentPane = new ProgressMonitorSwingWorkerTemplate();
            contentPane.setOpaque(true);
            frame.setContentPane(contentPane);

            // display example app window
            frame.setVisible(true);
         }
      });
   }

   public ProgressMonitorSwingWorkerTemplate() {
      // set up the copy files button
      runTaskButton = new JButton("To Task");
      runTaskButton.addActionListener(this);
      JPanel buttonPanel = new JPanel();
      buttonPanel.add(runTaskButton);

      // set up the console for display of operation output
      console = new JTextArea(15, 60);
      console.setMargin(new Insets(5, 5, 5, 5));
      console.setEditable(false);
      add(new JScrollPane(console), BorderLayout.CENTER);
      add(buttonPanel, BorderLayout.SOUTH);
   }

   public void actionPerformed(ActionEvent event) {
      doTask();
   }

   public void doTask() {
      String someArg = "Something";
      // schedule the copy files operation for execution on a background thread
      operation = new TheTask(someArg);
      MonitoredTask monTask = new MonitoredTask(operation, "Doing tasks...", this);
      //monTask.setIndeterminate(true);
      monTask.start();
      // we're running our operation; disable the button
      runTaskButton.setEnabled(false);

   }

   /**
    * SwingWorker<T,V>
    * <T> - the result type returned by this SwingWorker's doInBackground and get methods
    * <V> - the type used for carrying out intermediate results by this SwingWorker's publish and
    * process methods More doc below...
    */
   class TheTask extends SwingWorker<Void, Integer> {

      private static final int PROGRESS_CHECKPOINT = 10000;
      private String someArg;
      int numSubTasks = 400;

      TheTask(String someArg) {
         this.someArg = someArg;
      }

      // perform time-consuming copy task in the worker thread
      @Override
      public Void doInBackground() {

         // initialize bound property progress (inherited from SwingWorker)
         setProgress(0);

         while (!isCancelled()) {
            // do sub-tasks...

            for (int i = 0; i < numSubTasks; i++) {
               int current = i;
               try {
                  Thread.sleep(10);
               } catch (InterruptedException ex) {
               }
               // set new value on bound property
               // progress and fire property change event
               int progress = (int) (100 * (current + 1) / numSubTasks);
               setProgress(progress);
               System.out.println("current = " + current);
               // publish current progress data for copy task
               publish(current);
               if (isCancelled()) {
                  return null;
               }
            }
            break;
         }
         return null;
      }

      // process(List<V> chunks)
      // process copy task progress data in the event dispatch thread
      @Override
      public void process(java.util.List<Integer> progressList) {
         if (isCancelled()) {
            return;
         }
         Integer complete = progressList.get(progressList.size() - 1);
         int progress = (int) (100 * (complete + 1) / numSubTasks);
         //progressBar.setValue(percentComplete.intValue());
         setProgress(progress);
      }

      // perform final updates in the event dispatch thread
      @Override
      public void done() {
         try {
            // call get() to tell us whether the operation completed or 
            // was canceled; we don't do anything with this result
            Void result = get();
            console.append("Copy operation completed.\n");
         } catch (InterruptedException e) {
         } catch (CancellationException e) {
            // get() throws CancellationException if background task was canceled
            console.append("Copy operation canceled.\n");
         } catch (ExecutionException e) {
            console.append("Exception occurred: " + e.getCause());
         }
         // reset the example app
         runTaskButton.setEnabled(true);
         //progressMonitor.setProgress(0);
         setProgress(100);  // This is required to close the ProgressMonitor
      }
   }
   /*
    * protected abstract T doInBackground()
    This method is executed on a background thread. It is expected to return the result of the long running process or throw an exception. This method is abstract and you are forced to implement it.
 
    protected void publish(V... chunks)
    This method should be called from the background thread while the long running task is executing. It is used to pass data to the process() method which executes on the EDT.
 
    protected void process(List<V> chunks)
    This method receives information from the publish() method. As it is executed on the EDT therefore it can be used to update the gui, during the execution of the background task. You should override this method if you need to provide feedback to the gui while the doInBackground() method is running.
 
    protected void done()
    The done() method is called once the doInBackground() method is complete. If you wish to perform some feedback once the background task is complete this is the method you should override. It is executed on the EDT and therefore can be used to update the gui.
 
    public T get()
    The method get() should be called from the done() method. It will either return the result from the doInBackground() method or it will throw an exception if an exception occurred during the doInBackground() method.
    void execute()
    This method is used to start the SwingWorker.
    */
}
