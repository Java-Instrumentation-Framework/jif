/*
 * JPrintStackTrace.java
 *
 * Created on October 11, 2006, 12:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.utils.diag;
//import com.sun.jdi.connect.*;
//import com.sun.jdi.event.*;
//import com.sun.jdi.request.*;


class EventProcessor extends Thread {
/*   private VirtualMachine vm = null;
   private PrintWriter pw = null;
   private boolean stop = false;

   public EventProcessor(VirtualMachine vm, PrintWriter pw) {
      this.vm = vm;
      this.pw = pw;
   }

   private void Info(String msg) {
      try {
         pw.println(msg);
      } catch (Throwable t) {
         t.printStackTrace();
      }
   }


   private void handleExceptionEvent(ExceptionEvent event)
      throws Throwable {
      int lineNo = 0;
      int catchLineNo = 0;
      ObjectReference objRef = null;
      ReferenceType refType = null;
      Iterator methodsListItr = null;
      Method method = null;
      Vector argList = new Vector();

      try {
         lineNo = event.location().lineNumber();
         catchLineNo = event.catchLocation().lineNumber();

         Info(
               "Exception event received: lineNo: " + lineNo + ", catchLineNo: "
               + catchLineNo);

         objRef = event.exception();
         refType = objRef.referenceType();

         methodsListItr = refType.allMethods().iterator();
         while (methodsListItr.hasNext()) {
            method = (Method)methodsListItr.next();

            if (method.name().equals("printStackTrace")
                   && method.signature().equals("()V")) {
               objRef.invokeMethod(
                  event.thread(),
                  method,
                  argList,
                  ObjectReference.INVOKE_SINGLE_THREADED);
               break;
            }
         }
      } catch (Throwable t) {
         throw t;
      }
   }


   private void handleEvent(Event event) throws Throwable {
      try {
         if (event instanceof VMStartEvent) {
            return;
         }

         if ((event instanceof VMDisconnectEvent) || (event instanceof VMDeathEvent)) {
            stop = true;
            return;
         }

         if (event instanceof ExceptionEvent) {
            handleExceptionEvent((ExceptionEvent)event);
            return;
         }

         Info("Error: Unexpected event '" + event + "' received.");
      } catch (Throwable t) {
         throw t;
      }
   }


   public void run() {
      EventQueue eventQ = null;
      EventSet eventSet = null;
      EventIterator eventItr = null;

      try {
         eventQ = vm.eventQueue();

         while (!stop) {
            try {
               eventSet = eventQ.remove();
               eventItr = eventSet.eventIterator();

               while (eventItr.hasNext()) {
                  handleEvent(eventItr.nextEvent());
               }

               eventSet.resume();
            } catch (Throwable t) {
               t.printStackTrace();
               break;
            }
         }

         Info("JPrintStackTrace terminated.");
      } catch (Throwable t) {
         t.printStackTrace();
      }
   }
}


public class JPrintStackTrace {
   public static final String VERSION =
      "JPrintStackTrace 1.0 - A JPDA based stack trace logging tool.\n"
      + "(C) Copyright 2005, Raja R.K (rajark_hcl@yahoo.co.in)\n"
      + "All rights reserved.\n";
   public static final String JPDA_CONNECTOR_NAME = "com.sun.jdi.SocketAttach";
   public static final String JPDA_HOSTNAME_ARG = "hostname";
   public static final String JPDA_PORT_ARG = "port";

   //Arguments
   private String hostName = null;

   public void setHostName(String hostName) {
      this.hostName = hostName;
   }

   private String portNo = null;

   public void setPortNo(String portNo) {
      this.portNo = portNo;
   }

   private String logFile = null;

   public void setLogFile(String logFile) {
      this.logFile = logFile;
   }

   private String classExcludeFile = null;

   public void setClassExcludeFile(String classExcludeFile) {
      this.classExcludeFile = classExcludeFile;
   }

   //Target VM
   private VirtualMachine vm = null;

   //Log file handler
   PrintWriter pw = null;

   //Class exclude list
   Vector classExcludeList = new Vector();

   private void Info(String msg) {
      try {
         if (pw == null) {
            pw = new PrintWriter(new FileWriter(new File(this.logFile)), true);
         }

         pw.println(msg);
      } catch (Throwable t) {
         t.printStackTrace();
      }
   }


   private void loadClassExcludeList() throws Throwable {
      BufferedReader br = null;
      String line = null;

      try {
         Info("Loading class exclude list from '" + classExcludeFile + "'");

         classExcludeList.clear();

         br = new BufferedReader(new FileReader(classExcludeFile));
         while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
               continue;
            }

            if (line.startsWith("#")) {
               continue;
            }

            classExcludeList.add(line);
         }
      } catch (Throwable t) {
         throw t;
      } finally {
         try {
            if (br != null) {
               br.close();
            }
         } catch (Throwable t1) {
            t1.printStackTrace();
         }
      }
   }


   private void enableEvents() throws Throwable {
      EventRequestManager erMgr = null;
      ExceptionRequest exReq = null;
      int i = 0;

      try {
         erMgr = vm.eventRequestManager();

         exReq = erMgr.createExceptionRequest(null, true, true);
         exReq.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
         for (i = 0; i < classExcludeList.size(); i++) {
            exReq.addClassExclusionFilter((String)classExcludeList.get(i));
         }

         exReq.enable();
      } catch (Throwable t) {
         throw t;
      }
   }


   private VirtualMachine connectToRemoteVM(Connector con, Map conArgs)
      throws Throwable {
      VirtualMachine vm = null;

      try {
         vm = ((AttachingConnector)con).attach(conArgs);

         Info(
               "JPrintStackTrace connected to remote VM (" + vm.name() + " "
               + vm.version() + ").");

         return vm;
      } catch (Throwable t) {
         throw t;
      }
   }


   private Map setConnectorArgs(Connector con, String hostName, String portNo)
      throws Throwable {
      Map conArgs = null;
      Iterator conArgsItr = null;
      Connector.Argument arg = null;

      try {
         //Get default arguments
         conArgs = con.defaultArguments();

         //Set arguments
         conArgsItr = conArgs.keySet().iterator();
         while (conArgsItr.hasNext()) {
            arg = (Connector.Argument)conArgs.get((String)conArgsItr.next());

            if (arg.name().equals(JPrintStackTrace.JPDA_HOSTNAME_ARG)) {
               arg.setValue(hostName);
            }

            if (arg.name().equals(JPrintStackTrace.JPDA_PORT_ARG)) {
               arg.setValue(portNo);
            }
         }

         return conArgs;
      } catch (Throwable t) {
         throw t;
      }
   }


   private Connector getConnector(String conName) throws Throwable {
      Iterator conItr = null;
      Connector con = null;

      try {
         conItr = Bootstrap.virtualMachineManager().allConnectors().iterator();
         while (conItr.hasNext()) {
            con = (Connector)conItr.next();

            if (con.name().equals(conName)) {
               return con;
            }
         }

         throw new Exception("Invalid connection '" + conName + "' specified.");
      } catch (Throwable t) {
         throw t;
      }
   }


   public void runJPrintStackTrace() throws Throwable {
      EventProcessor ep = null;
      Map conArgs = null;
      Connector con = null;

      try {
         //Load class exclude list
         loadClassExcludeList();

         //Get connector
         con = getConnector(JPrintStackTrace.JPDA_CONNECTOR_NAME);

         //Set connector arguments
         conArgs = setConnectorArgs(con, hostName, portNo);

         //Connect to remote VM
         vm = connectToRemoteVM(con, conArgs);

         //Start event processor thread
         ep = new EventProcessor(vm, pw);
         ep.start();

         //Enable events
         enableEvents();

         //Resume if suspended
         vm.resume();
      } catch (Throwable t) {
         throw t;
      }
   }


   public static void printInvalidFileUsage(String msg) {
      System.out.println("\nERROR: " + msg);
      JPrintStackTrace.printUsage(1);
   }


   public static void printInvalidUsage(String arg) {
      System.out.println("\nERROR: Invalid argument " + arg + " specified");
      JPrintStackTrace.printUsage(1);
   }


   public static void printUsage(int exitError) {
      System.out.println("\nUsage:\n");
      System.out.println("JPrintStackTrace <options>\n");
      System.out.println("Options: ");
      System.out.println("[-V] : Display version and exit");
      System.out.println("[-h] : Display help and exit");
      System.out.println("-r <remote_host_name> : Remote host name");
      System.out.println("-p <port_no> : Port number");
      System.out.println(
            "-e <class_exclude_list_file> : File containing list of classes to be excluded");
      System.out.println("-l <log_file> : JPrintStackTrace log file");

      System.exit(exitError);
   }


   public static void printVersion() {
      System.out.println("\n" + JPrintStackTrace.VERSION);
      System.exit(0);
   }


   public static void main(String[] args) {
      JPrintStackTrace jpst = null;
      int i = 0;

      try {
         if (args.length == 0) {
            JPrintStackTrace.printUsage(1);
         }

         jpst = new JPrintStackTrace();

         while (i < args.length) {
            if (args[i].equals("-V")) {
               JPrintStackTrace.printVersion();
            }

            if (args[i].equals("-h")) {
               JPrintStackTrace.printUsage(0);
            }

            if (args[i].equals("-r")) {
               if ((i + 1) >= args.length) {
                  JPrintStackTrace.printInvalidFileUsage(args[i]);
               }

               i++;
               jpst.setHostName(args[i]);
               i++;
               continue;
            }

            if (args[i].equals("-p")) {
               if ((i + 1) >= args.length) {
                  JPrintStackTrace.printInvalidFileUsage(args[i]);
               }

               i++;
               jpst.setPortNo(args[i]);
               i++;
               continue;
            }

            if (args[i].equals("-l")) {
               if ((i + 1) >= args.length) {
                  JPrintStackTrace.printInvalidFileUsage(args[i]);
               }

               i++;
               jpst.setLogFile(args[i]);
               i++;
               continue;
            }

            if (args[i].equals("-e")) {
               if ((i + 1) >= args.length) {
                  JPrintStackTrace.printInvalidFileUsage(args[i]);
               }

               i++;
               jpst.setClassExcludeFile(args[i]);
               i++;
               continue;
            }

            JPrintStackTrace.printInvalidUsage(args[i]);
         }

         jpst.runJPrintStackTrace();
      } catch (Throwable t) {
         t.printStackTrace();
      }
   }
*/
}
