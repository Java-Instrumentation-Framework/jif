<nabble_a href="ijclient_10.zip">ijclient_10.zip</nabble_a>

The attached API provides a simple Client API for 
embedding ImageJ into an application.

Use IJClientFactory to get a local or remote interface reference:
  IJClient ijClient = IJClientFactory.getIJClient();

If ImageJ has already been started within the current JVM 
or another JVM on localhost  it will transparently invoke the 
method calls to this ImageJ instance.  If ImageJ is not found, 
it will start up a new instance in the current JVM.

public interface IJClient {
  void quit();
  void setDefaultDirectory(File dir) throws IOException;
  void openImage(File file) throws IOException;
  void runMenuCommand(String menuCommand) throws IOException;
  void runMacro(String macro) throws IOException;
  void runMacroFile(File file) throws IOException;
  void runMacroFile(File file, String arg) throws IOException;
}


Example Usage:
-------------
runImageJ(File[] imageFiles, String macro) {

    try {
        // Get either current JVM or external JVM connection to ImageJ.
        // Creates an instance of ImageJ if none exists in current JVM.
        ijClient = IJClientFactory.getIJClient();

        // Load up image files
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (!file.exists()) {
                log.error(file + ": Image file not found");
            } else {
                log.debug("open: " + file.getPath());
                ijClient.openImage(file);
            }
        }

        // Execute the optional macro
        if (macro != null && macro.trim().length() > 0) {
	    log.debug("macro: " + macro);
            ijClient.runMacro(macro);
        }
    } catch (IOException e) {
        log.error("Failed to connect to or execute ImageJ commands.  " + e.getMessage());
    }
}


This source code is submitted to the public domain.
Use at your own risk.  No liability is expressed or implied.
Tony O'Hagan  http://www.itee.uq.edu.au/~tohagan

I'd be delighted if IJClient was added to the ImageJ source code.
I'd recommend that the following improvements be considered 
to better support IJClient.

* I added support for remote execution of multi-line macros in IJClientRemote.runMacro();
  It implementats a 'hack' that converts them to a temporary file and invokes runMacroFile().
  The file is removed after a time delay.
  This workaround would not be needed if SocketListener supported mult-line macros.

  An alternative hack might be to replace '\n' with ' ' or maybe ';' 
  but I'm uncertain that this would work in all cases.
    
* Allow IJClientRemote.runMacroFile() to respond with 
  the String result returned by IJ.runMacroFile().
  SocketListener does not currently support this.

* IJClient API might be used to simplify existing 
  code by removing the need to decode the command line 
  parameters twice in ImageJ.main() and ImageJ.isRunning().
