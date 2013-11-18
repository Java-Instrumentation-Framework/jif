/**
 *
 */
package org.dart.imagej;

import ij.IJ;
import ij.ImageJ;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Factory class to fetch a remote or local IJClient interface
 * that supports the basic command line features of ImageJ.
 *
 * @author Tony O'Hagan, ITEE, University of Queensland
 */
public final class IJClientFactory{
	private IJClientFactory() {
		super();
	}

	/**
	 * Returns a simple client interface to ImageJ useful applications that embed ImageJ.
	 * If ImageJ has already been started within the current JVM or a another JVM
	 * on localhost it will transparently invoke the method calls to this ImageJ instance.
	 * If ImageJ is not found, it will start up a new instance in the current JVM.
	 * @param allowRemote
	 * @return IJClient interface to open images or invoke macros or menu commands.
	 */
	public static IJClient getIJClient(boolean allowRemote) {
		if (allowRemote && !IJClientLocal.hasLocalInstance()) {
			try {
				// Try to socket connect remote instance (usually in another JVM on localhost)
				return new IJClientRemote();
			} catch (IOException e) {
				// Use a local instance
			}
		}

		// Find or create a local instance of ImageJ
		return new IJClientLocal();
	}
}
