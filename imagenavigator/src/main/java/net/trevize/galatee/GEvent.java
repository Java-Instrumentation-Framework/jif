package net.trevize.galatee;

import java.awt.AWTEvent;

/**
 * This class provides a derivation of AWTEvent for making events/listeners for the Galatee.
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * GEvent.java - May 14, 2009
 */

public class GEvent extends AWTEvent {
	
	public static final int GALATEE_EVENT = AWTEvent.RESERVED_ID_MAX + 1010;

	public static final String E_ITEM_DOUBLECLICKED = "E_ITEM_DOUBLECLICKED";

	public static final String E_SELECTION_CHANGED = "E_SELECTION_CHANGED";

	private String type;

	private GItem gi;

	public GEvent(String type, Object g, GItem gi) {
		super(g, GALATEE_EVENT);
		this.type = type;
		this.gi = gi;
	}

	public String getType() {
		return type;
	}

	public GItem getSelectedItem() {
		return gi;
	}
	
}
