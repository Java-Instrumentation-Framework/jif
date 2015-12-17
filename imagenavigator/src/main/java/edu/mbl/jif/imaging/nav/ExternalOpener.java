/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.imaging.nav;

import java.io.IOException;

/**
 *
 * @author GBH
 */
public class ExternalOpener {
   public void test() {
      try {
         Process process = new ProcessBuilder(
                 "C:\\Program Files (x86)\\IrfanView\\i_view32.exe",
                 "C:\\_Dev\\_Dev_Data\\TestImages\\color-chart.png").start();
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }
   public static void main(String[] args) {
      new ExternalOpener().test();
   }
   
}

