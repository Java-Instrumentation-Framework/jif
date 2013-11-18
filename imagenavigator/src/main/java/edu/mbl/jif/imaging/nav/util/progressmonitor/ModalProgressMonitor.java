/*
 * http://code.google.com/p/nova-nits/source/browse/trunk/Imaging+Trial+Submitter/src/com/novartis/nims/trialsubmitter/ModalProgressMonitor.java?r=118
 */

package edu.mbl.jif.imaging.nav.util.progressmonitor;

import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

/**
 * A copy of javax.swing.ProgressMonitor. This version supports setting the
 * mode of the progress bar to indeterminate, and shows a modal rather than a
 * modeless dialog.
 */
public class ModalProgressMonitor extends Object
{
	private ModalProgressMonitor root;
	private JDialog         dialog;
	private JOptionPane     pane;
	private JProgressBar    myBar;
	private JLabel          noteLabel;
	private Component       parentComponent;
	private String          note;
	private Object[]        cancelOption = null;
	private Object          message;
	private long            T0;
	private int             millisToDecideToPopup = 500;
	private int             millisToPopup = 2000;
	private int             min;
	private int             max;
	private boolean			isIndeterminate = false;


	/**
	 * Constructs a graphic object that shows progress, typically by filling
	 * in a rectangular bar as the process nears completion.
	 *
	 * @param parentComponent the parent component for the dialog box
	 * @param message a descriptive message that will be shown
	 *        to the user to indicate what operation is being monitored.
	 *        This does not change as the operation progresses.
	 *        See the message parameters to methods in
	 *        {@link JOptionPane#message}
	 *        for the range of values.
	 * @param note a short note describing the state of the
	 *        operation.  As the operation progresses, you can call
	 *        setNote to change the note displayed.  This is used,
	 *        for example, in operations that iterate through a
	 *        list of files to show the name of the file being processes.
	 *        If note is initially null, there will be no note line
	 *        in the dialog box and setNote will be ineffective
	 * @param min the lower bound of the range
	 * @param max the upper bound of the range
	 * @see JDialog
	 * @see JOptionPane
	 */
	public ModalProgressMonitor(Component parentComponent,
			Object message,
			String note,
			int min,
			int max) {
		this(parentComponent, message, note, min, max, null);
	}


	private ModalProgressMonitor(Component parentComponent,
			Object message,
			String note,
			int min,
			int max,
			ModalProgressMonitor group) {
		this.min = min;
		this.max = max;
		this.parentComponent = parentComponent;

		cancelOption = new Object[1];
		cancelOption[0] = UIManager.getString("OptionPane.cancelButtonText");

		this.message = message;
		this.note = note;
		if (group != null) {
			root = (group.root != null) ? group.root : group;
			T0 = root.T0;
			dialog = root.dialog;
		}
		else {
			T0 = System.currentTimeMillis();
		}
	}

	/** 
	 * Indicate the progress of the operation being monitored.
	 * If the specified value is >= the maximum, the progress
	 * monitor is closed. 
	 * @param nv an int specifying the current value, between the
	 *        maximum and minimum specified for this component
	 * @see #setMinimum
	 * @see #setMaximum
	 * @see #close
	 */
	public void setProgress(int nv) {
		if (nv >= max) {
			// regular progress monitor closes when progress reachs max
			// this one will only close when close() is explicity invoked by a
			// caller. in the meantime, it resets to 0
			nv = 0;
                  
		}
		if (myBar != null) {
			myBar.setValue(nv);
		}
		else {
			long T = System.currentTimeMillis();
			long dT = (int)(T-T0);
			if (dT >= millisToDecideToPopup) {
				int predictedCompletionTime;
				if (nv > min) {
					predictedCompletionTime = (int)((long)dT *
							(max - min) /
							(nv - min));
				}
				else {
					predictedCompletionTime = millisToPopup;
				}
				if (predictedCompletionTime >= millisToPopup || isIndeterminate) {
					myBar = new JProgressBar();
					myBar.setIndeterminate(isIndeterminate);
					myBar.setMinimum(min);
					myBar.setMaximum(max);
					myBar.setValue(nv);
					if (note != null) noteLabel = new JLabel(note);
					pane = new JOptionPane(new Object[] {message,
							noteLabel,
							myBar},
							JOptionPane.INFORMATION_MESSAGE,
							JOptionPane.DEFAULT_OPTION,
							null,
							ModalProgressMonitor.this.cancelOption,
							null);
					dialog = pane.createDialog(parentComponent,
							UIManager.getString(
									"FlexibleProgressMonitor.progressText"));
					dialog.setVisible(true);
				}
			}
		}
	}


	/** 
	 * Indicate that the operation is complete.  This happens automatically
	 * when the value set by setProgress is >= max, but it may be called
	 * earlier if the operation ends early.
	 */
	public void close() {
		if (dialog != null) {
			dialog.setVisible(false);
			dialog.dispose();
			dialog = null;
			pane = null;
			myBar = null;
		}
	}


	/**
	 * Returns the minimum value -- the lower end of the progress value.
	 *
	 * @return an int representing the minimum value
	 * @see #setMinimum
	 */
	public int getMinimum() {
		return min;
	}


	/**
	 * Specifies the minimum value.
	 *
	 * @param m  an int specifying the minimum value
	 * @see #getMinimum
	 */
	public void setMinimum(int m) {
		if (myBar != null) {
			myBar.setMinimum(m);
		}
		min = m;
	}


	/**
	 * Returns the maximum value -- the higher end of the progress value.
	 *
	 * @return an int representing the maximum value
	 * @see #setMaximum
	 */
	public int getMaximum() {
		return max;
	}


	/**
	 * Specifies the maximum value.
	 *
	 * @param m  an int specifying the maximum value
	 * @see #getMaximum
	 */
	public void setMaximum(int m) {
		if (myBar != null) {
			myBar.setMaximum(m);
		}
		max = m;
	}


	/** 
	 * Returns true if the user hits the Cancel button in the progress dialog.
	 */
	public boolean isCanceled() {
		if (pane == null) return false;
		Object v = pane.getValue();
		return ((v != null) &&
				(cancelOption.length == 1) &&
				(v.equals(cancelOption[0])));
	}


	/**
	 * Specifies the amount of time to wait before deciding whether or
	 * not to popup a progress monitor.
	 *
	 * @param millisToDecideToPopup  an int specifying the time to wait,
	 *        in milliseconds
	 * @see #getMillisToDecideToPopup
	 */
	public void setMillisToDecideToPopup(int millisToDecideToPopup) {
		this.millisToDecideToPopup = millisToDecideToPopup;
	}


	/**
	 * Returns the amount of time this object waits before deciding whether
	 * or not to popup a progress monitor.
	 *
	 * @see #setMillisToDecideToPopup
	 */
	public int getMillisToDecideToPopup() {
		return millisToDecideToPopup;
	}


	/**
	 * Specifies the amount of time it will take for the popup to appear.
	 * (If the predicted time remaining is less than this time, the popup
	 * won't be displayed.)
	 *
	 * @param millisToPopup  an int specifying the time in milliseconds
	 * @see #getMillisToPopup
	 */
	public void setMillisToPopup(int millisToPopup) {
		this.millisToPopup = millisToPopup;
	}


	/**
	 * Returns the amount of time it will take for the popup to appear.
	 *
	 * @see #setMillisToPopup
	 */
	public int getMillisToPopup() {
		return millisToPopup;
	}


	/**
	 * Specifies the additional note that is displayed along with the
	 * progress message. Used, for example, to show which file the
	 * is currently being copied during a multiple-file copy.
	 *
	 * @param note  a String specifying the note to display
	 * @see #getNote
	 */
	public void setNote(String note) {
		this.note = note;
		if (noteLabel != null) {
			noteLabel.setText(note);
			if (dialog != null) {
				dialog.pack();
			}
		}
	}


	/**
	 * Specifies the additional note that is displayed along with the
	 * progress message.
	 *
	 * @return a String specifying the note to display
	 * @see #setNote
	 */
	public String getNote() {
		return note;
	}


	public boolean isIndeterminate() {
		return isIndeterminate;
	}


	public void setIndeterminate(boolean isIndeterminate) {
		this.isIndeterminate = isIndeterminate;
		if (myBar != null) {
			myBar.setIndeterminate(isIndeterminate);
		}
	}

}