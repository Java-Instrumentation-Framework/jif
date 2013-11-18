package net.trevize.galatee;

import java.util.EventListener;

/**
 * This class provides a listener for the Galatee.
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * GListener.java - May 17, 2009
 */

public interface GListener extends EventListener {
	
	public void itemDoubleClicked(GEvent e);
	
	public void selectionChanged(GEvent e);
	
}