package edu.mbl.jif.utils.diag.trace;

import java.awt.AWTEvent;
import java.awt.EventQueue;
/*
 * 	Usage:  To watch for Deadlock or EDT hogging...
 *          Toolkit.getDefaultToolkit().getSystemEventQueue().push(
				new TracingEventQueue());
 */
public class TracingEventQueue extends EventQueue {

	private TracingEventQueueThread tracingThread;

	public TracingEventQueue() {
		this.tracingThread = new TracingEventQueueThread(500);
		this.tracingThread.start();
	}

	@Override
	protected void dispatchEvent(AWTEvent event) {
		this.tracingThread.eventDispatched(event);
		super.dispatchEvent(event);
		this.tracingThread.eventProcessed(event);
	}
}
