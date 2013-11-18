package edu.mbl.jif.imaging.nav.util;

import java.io.IOException;
import java.util.List;
import name.pachler.nio.file.*;
import name.pachler.nio.file.ext.ExtendedWatchEventModifier;

/**
 * Watch for file changes in a directory...
 *
 * Uses: http://jpathwatch.wordpress.com/ jpathwatch is open source under the GNU General Public
 * License with Classpath Exception.
 *
 * @author GBH
 */
public class PathWatcher {

   WatchKey watchingKey;
   WatchService watchService;
   WatchingThread watchingThread;
   PathWatcherObserver oneWhoCares;

   public PathWatcher(PathWatcherObserver oneWhoCares) {
      this.oneWhoCares = oneWhoCares;
   }

   public void setWatchedPath(String path, boolean recursive) {
      Path watchedPath = Paths.get(path);
      try {
         if (recursive) {
            WatchEvent.Kind<?>[] events = new WatchEvent.Kind<?>[]{
               StandardWatchEventKind.ENTRY_CREATE, StandardWatchEventKind.ENTRY_DELETE};
            watchingKey = watchedPath.register(watchService, events, ExtendedWatchEventModifier.FILE_TREE);
         } else {
            watchingKey = watchedPath.register(watchService,
                    StandardWatchEventKind.ENTRY_CREATE, StandardWatchEventKind.ENTRY_DELETE);
         }
      } catch (UnsupportedOperationException uox) {
         System.err.println("file watching not supported!");
         // handle this error here
      } catch (IOException iox) {
         System.err.println("I/O errors");
         // handle this error here
      }
   }

   public void startWatching(String path, boolean recursive) {
      stopWatching();
      if (watchService == null) {
         watchService = FileSystems.getDefault().newWatchService();
      }
      setWatchedPath(path, recursive);
      new WatchingThread().start();
      //System.out.println("started WatchingThread");
   }

   class WatchingThread extends Thread {

      public void run() {
         while (watchService != null) {
            // take() will block until a file has been created/deleted
            WatchKey signalledKey;
            try {
               signalledKey = watchService.take();
            } catch (InterruptedException ix) {
               // we'll ignore being interrupted
               continue;
            } catch (ClosedWatchServiceException cwse) {
               // other thread closed watch service
               //System.out.println("watch service closed, terminating.");
               break;
            }
            // get list of events from key
            List<WatchEvent<?>> list = signalledKey.pollEvents();
            // VERY IMPORTANT! call reset() AFTER pollEvents() to allow the
            // key to be reported again by the watch service
            signalledKey.reset();
            // we'll simply print what has happened; real applications
            // will do something more sensible here
            for (WatchEvent e : list) {
               String message = "";
               if (e.kind() == StandardWatchEventKind.ENTRY_CREATE) {
                  Path context = (Path) e.context();
                  oneWhoCares.fileCreated(context.toString());
                  message = context.toString() + " created";
               } else if (e.kind() == StandardWatchEventKind.ENTRY_DELETE) {
                  Path context = (Path) e.context();
                  oneWhoCares.fileDeleted(context.toString());
                  message = context.toString() + " deleted";
               } else if (e.kind() == StandardWatchEventKind.OVERFLOW) {
                  message = "OVERFLOW: more changes happened than we could retreive";
               }
               System.out.println(message);
               System.out.flush();
            }
         }
         //System.out.println("Watching stopped.");
      }
   }

   public void stopWatching() {
      if (watchService != null) {
         try {
            watchService.close();
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      }
      watchService = null;
   }

   /* Usage:
    PathWatcher w = new PathWatcher();
    w.startWatching("/SciSoft");
    ...
    w.startWatching("/Temp");
    ...
    w.stopWatching();
    */
   // test...
   static class Cares implements PathWatcherObserver {

      @Override
      public void fileCreated(String file) {
         System.out.println("Created = " + file);
      }

      @Override
      public void fileDeleted(String file) {
         System.out.println("Deleted = " + file);
      }
   }

   public static void main(String[] args) {
      Cares cares = new Cares();
      PathWatcher w = new PathWatcher(cares);
      //w.simple();
      System.out.println("watching /SciSoft...");
      w.startWatching("/SciSoft", true);
      try {
         Thread.sleep(10000);
      } catch (InterruptedException ex) {
      }
      System.out.println("watching /Temp...");
      //w.stopWatching();
      w.startWatching("/Temp", false);
      try {
         Thread.sleep(10000);
      } catch (InterruptedException ex) {
      }
      w.stopWatching();
      System.out.println("end");


   }
}
