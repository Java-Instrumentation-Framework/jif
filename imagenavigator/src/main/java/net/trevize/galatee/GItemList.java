package net.trevize.galatee;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * GItemList.java - Feb 2, 2010
 */

public class GItemList implements Iterable<GItem> {

	private ArrayList<GItem> toProcess;
	private ArrayList<GItem> processing;
	private ArrayList<GItem> processed;
	private GItemListIterator iterator;

	public ArrayList<GItem> getToProcess() {
		return toProcess;
	}

	public void setToProcess(ArrayList<GItem> toProcess) {
		this.toProcess = toProcess;
	}

	public ArrayList<GItem> getProcessing() {
		return processing;
	}

	public void setProcessing(ArrayList<GItem> processing) {
		this.processing = processing;
	}

	public ArrayList<GItem> getProcessed() {
		return processed;
	}

	public void setProcessed(ArrayList<GItem> processed) {
		this.processed = processed;
	}

	public GItemList() {
		toProcess = new ArrayList<GItem>();
		processing = new ArrayList<GItem>();
		processed = new ArrayList<GItem>();
		iterator = new GItemListIterator(this);
	}

	@Override
	public Iterator iterator() {
		return iterator;
	}

	public void add(GItem gitem) {
		synchronized (toProcess) {
			synchronized (processing) {
				if (!processing.contains(gitem)) {
					toProcess.add(0, gitem);
				}
			}
		}
	}

	public void processingEndedFor(GItem gitem) {
		synchronized (processing) {
			processing.remove(gitem);
		}
	}

}
