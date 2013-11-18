package edu.mbl.jif.imaging.nav.util;

/**
 * Called-back by PathWatcher
 * 
 * @author GBH
 */
public interface PathWatcherObserver {

   void fileCreated(String file);

   void fileDeleted(String file);
}
