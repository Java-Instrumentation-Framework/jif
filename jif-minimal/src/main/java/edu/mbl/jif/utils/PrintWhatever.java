/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.utils;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GBH
 */
public class PrintWhatever {

//   public static void out(int... objs) {
//      for (int i : objs) {
//         System.out.print("" + i );
//         if(i==objs.length) {
//            System.out.print(".\n");
//         } else {
//            System.out.print(", ");
//         }
//      }
//   }
   
   public static void out(Object... objs) {
      int n = 0;
      for (Object obj : objs) {
         try {
            System.out.print(toStringRecursive(obj));
         } catch (Exception ex) {
            Logger.getLogger(PrintWhatever.class.getName()).log(Level.SEVERE, null, ex);
         }
         n++;
         if (n == objs.length) {
            System.out.print(".\n");
         } else {
            System.out.print(", ");
         }
      }
   }

   public static void out(Number... objs) {  
      int n = 0;
      for (Number i : objs) {
         System.out.print("" + i);
         n++;
         if (n == objs.length) {
            System.out.print(".\n");
         } else {
            System.out.print(", ");
         }
      }
   }

//   public static void out(Object... objs) {
//      for (Object object : objs) {
//         try {
//            printFields(object);
//            break;
//         } catch (Exception ex) {}
//         try {
//            printFields(object);
//            break;
//         } catch (Exception ex) {}
//      }
//      
//   }
   public static void printFields(Object obj)
           throws Exception {
      for (Field field : obj.getClass().getDeclaredFields()) {
         field.setAccessible(true);
         String name = field.getName();
         Object value = field.get(obj);
         System.out.printf("%s: %s%n", name, value);
      }
   }
   //===================================================================
   // from http://stackoverflow.com/questions/3905382/recursivley-print-an-objects-details
   private static final List LEAVES = Arrays.asList(
           Boolean.class, Character.class, Byte.class, Short.class,
           Integer.class, Long.class, Float.class, Double.class, Void.class,
           String.class);

   public static String toStringRecursive(Object o)
           throws Exception {

      if (o == null) {
         return "null";
      }

      if (LEAVES.contains(o.getClass())) {
         return o.toString();
      }

      StringBuilder sb = new StringBuilder();
      sb.append(o.getClass().getSimpleName()).append(": [");
      for (Field f : o.getClass().getDeclaredFields()) {
         if (Modifier.isStatic(f.getModifiers())) {
            continue;
         }
         f.setAccessible(true);
         sb.append(f.getName()).append(": ");
         sb.append(toStringRecursive(f.get(o))).append(" ");
      }
      sb.append("]\n");
      return sb.toString();
   }

   public static void main(String[] args) throws Exception {
      out(1, 2, 3);
      out("this","that", "those");
      System.out.println(toStringRecursive(1));
      Main.printFields(new Main(), "");
   }
   
   static class Main {
    private static Set<Object> visited = new HashSet<Object>();
    public String s = "abc";
    public int i = 10;
    public Main INSTANCE = this;
   


    private static void printFields(Object obj, String pre) throws Exception{
      Field[] fields = obj.getClass().getFields();
      for (Field field:fields) {
         String value = "";
         String type = field.getType().toString();

         // handle primitve values
         if (type.equals("int")) {
           value += field.getInt(obj);
         } 

         // handle special types, you may add Wrapper classes
          else if (type.equals("class java.lang.String")) {
           value = field.get(obj).toString();  
         } 

         // handle all object that you want to inspect
          else {
           if (visited.contains(field.get(obj))) {
             // necessary to prevent stack overflow
             value = "CYCLE DETECTED";
           } else {
             // recursing deeper
             visited.add(field.get(obj));
             pre += field.getName() + ".";
             printFields(field.get(obj), pre);
           }
         }     

         System.out.printf("%s%s = %s%n", pre, field.getName(), value);
      }
    }
}
}
