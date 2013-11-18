package edu.mbl.jif.utils.diag.mem;

public class MemoryWatcher extends Thread {

  public MemoryWatcher() {
    start();
  }

  public synchronized void run() {
    while (true) {
//      if (Runtime.getRuntime().freeMemory() < 1000000) {
//        DialogBoxes.boxError("Running Low on Memory",
//          "Free Memory: " +
//          String.valueOf(Runtime.getRuntime().freeMemory() / 1000) + "K / " +
//          String.valueOf(Runtime.getRuntime().totalMemory() / 1000) + "K" +
//          "/n Close some windows or re-start PSj.");
//      }
      long free = Runtime.getRuntime().freeMemory() / 1000;
      long alloc = Runtime.getRuntime().totalMemory() / 1000;
      long used = alloc - free;
      System.out.println(" MemKB Free/Alloc/Used: " +
        String.valueOf( free )  + " / " +
        String.valueOf( alloc ) + " / " +
        String.valueOf( used )
        );
      try {
        sleep(4000);
      } catch (InterruptedException e) {
        System.err.println("Interrupted");
      }
    }
  }

  public static void showMem(String msg) {
    long free = Runtime.getRuntime().freeMemory() / 1000;
    long alloc = Runtime.getRuntime().totalMemory() / 1000;
    long used = alloc - free;
    System.out.println(msg + " MemKB Free/Alloc/Used: " +
        String.valueOf(free) + " / " +
        String.valueOf(alloc) + " / " +
        String.valueOf(used)
        );
  }

}
