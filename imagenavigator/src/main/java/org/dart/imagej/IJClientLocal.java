package org.dart.imagej;

import ij.IJ;
import ij.ImageJ;
import ij.io.OpenDialog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Invokes commands in a local instance of ImageJ
 *
 * @author Tony O'Hagan, ITEE, University of Queensland
 */
public class IJClientLocal implements IJClient {
	public IJClientLocal() {
		super();
		ImageJ ij = IJ.getInstance();
		if (ij == null) {
			// Sets IJ.getInstance() !?
			// Initialises ImageJ GUI
			// Creates ij.SocketListner to listens for remote commands
			ij = new ImageJ(null);

			// TODO: Replace in ImageJ v1.38l or later with: 
                        //ij.exitWhenQuitting(false);
			disableExitWhenQuitting();
		}

		if (!ij.isShowing()) {
			ij.setVisible(true);
		}
	}

	public static boolean hasLocalInstance() {
		return IJ.getInstance() != null;
	}

	/**
	 * TODO: ImageJ v1.38l this can be replaced with ij.exitWhenQuitting(false);
	 */
	private void disableExitWhenQuitting() {
		// Ensures that System.exit(0) is NOT invoked when ImageJ exists.
		// This can occur via this.close() or when the user selects Quit from the IJ menu.
		try {
		    Field f = ImageJ.class.getDeclaredField("exitWhenQuitting");
		    f.setAccessible(true);
		    f.set(IJ.getInstance(), new Boolean(false));
		} catch (Exception e) {
			// ignore
		}
	}

	public void quit() {
		try {
			// Attempt to quit application
    			runMenuCommand("Quit");
		} catch (IOException e) {
			// ignore
		}
	}


	public void setDefaultDirectory(File dir) throws IOException {
		OpenDialog.setDefaultDirectory(dir.getAbsolutePath());
	}

	public void openImage(File file) throws IOException {
		IJ.open(file.getAbsolutePath());
	}

	public void runMacro(String macro) throws IOException {
		IJ.runMacro(macro);
	}
	public void runMacro(String macro, String args) throws IOException {
		IJ.runMacro(macro, args);
	}
	public void runMacroFile(File file) throws IOException {
		IJ.runMacroFile(file.getAbsolutePath());
	}

	public void runMacroFile(File file, String arg) throws IOException {
		IJ.runMacroFile(file.getAbsolutePath(), arg);
	}

	public void runMenuCommand(String menuCommand) throws IOException {
		IJ.run(menuCommand);
	}
}
