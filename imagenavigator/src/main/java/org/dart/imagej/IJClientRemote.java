package org.dart.imagej;

import ij.ImageJ;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

/**
 * Invokes commands in a remote instance of ImageJ.
 * @author Tony O'Hagan, ITEE, University of Queensland
 */
public class IJClientRemote
        implements IJClient {

//    private static final Log log = LogFactory.getLog(IJClientRemote.class);
//    private static final boolean isDebugging = log.isDebugEnabled();


    /**
     * @throws IOException Thrown if we cannot connect to a running instance of ImageJ
     */
    public IJClientRemote() throws IOException {
        // IJClientFactory.getIJClient() uses the fact that this contructor
        // throws an exception to test when the ImageJ server (ij.SocketListener) is active.
        setDefaultDirectory(new File(System.getProperty("user.dir")));
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
        send("user.dir " + dir.getAbsolutePath());
    }


    public void openImage(File file) throws IOException {
        send("open " + file.getAbsolutePath());
    }


    public void runMacro(String macro, String macroArg) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void runMacro(String macro) throws IOException {
        // An alternative compromise if temporary files prove to be a problem
//		String[] macroLines = macro.split("\n");
//		for (int line = 0; line < macroLines.length; line++) {
//			send("eval " + macroLines[line]);
//		}

        if (macro.indexOf('\n') == -1) {
            send("eval " + macro);	// Remote protocol only works for single line macros

        } else {
            // Hack to avoid that problem that the ImageJ server only supports single line commands
            // TODO: Fix ImageJ server to support multiline commands.  Possibly replace it with an RMI solution.
            // Write macro to a temporary file and execute the file
            final File tempFile = writeMacroToTempFile(macro);

            runMacroFile(tempFile);

            // Delete the temporary file after 10 seconds (in a new thread)
            // Plenty of time for the ImageJ server to read the file.
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                public void run() {
                    tempFile.delete();
                }


            }, 10 * 1000);
        }
    }


    private File writeMacroToTempFile(String macro) {
        File tempFile = null;
        PrintStream out = null;
        try {
            tempFile = File.createTempFile("ij", ".ijm");
            FileOutputStream fout = new FileOutputStream(tempFile);
            out = new PrintStream(fout);
            out.println(macro);
        } catch (IOException e) {
//            if (tempFile == null) {
//                log.error("Failed to create or write to temporary ImageJ macro file");
//            } else {
//                log.error(tempFile.getAbsoluteFile() + ": Failed to create or write to temporary ImageJ macro file");
//            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return tempFile;
    }


    public void runMacroFile(File file) throws IOException {
        runMacroFile(file, null);
    }


    public void runMacroFile(File file, String arg) throws IOException {
        String cmd = "macro " + file.getAbsolutePath() + (arg == null ? "" : ("(" + arg + ")"));
        send(cmd);
    }


    public void runMenuCommand(String menuCommand) throws IOException {
        send("run " + menuCommand);
    }


    /**
     * Send commands to ImageJ SocketLister
     * @param arg
     * @throws IOException
     */
    private void send(String cmd) throws IOException {
        int port = ImageJ.getPort();
        Socket socket = null;
        PrintWriter out = null;
        try {
            socket = new Socket("localhost", port);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.println(cmd);
        } finally {
            if (out != null) {
                out.close();
            }

            if (socket != null) {
                socket.close();
            }
        }
    }


}

