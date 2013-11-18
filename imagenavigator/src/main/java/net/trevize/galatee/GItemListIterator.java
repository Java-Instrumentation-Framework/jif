package net.trevize.galatee;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * GItemListIterator.java - Feb 2, 2010
 */

public class GItemListIterator implements Iterator<GItem> {

	private GItemList gi_list;

	public GItemListIterator(GItemList gi_list) {
		this.gi_list = gi_list;
	}

	@Override
	public boolean hasNext() {
		synchronized (gi_list.getToProcess()) {
			boolean res = false;
			if (gi_list.getToProcess().size() != 0) {
				res = true;
			}
			return res;
		}
	}

	@Override
	public GItem next() {
		GItem res = null;
		synchronized (gi_list.getToProcess()) {
			if (gi_list.getToProcess().size() != 0) {
				res = gi_list.getToProcess().get(0);
				synchronized (gi_list.getProcessing()) {
					gi_list.getProcessing().add(res);
				}
				gi_list.getToProcess().remove(res);
			} else {
				throw new NoSuchElementException();
			}
		}
		return res;
	}

	@Override
	public void remove() {
	}

}
