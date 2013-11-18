package edu.mbl.jif.utils.diag;

import edu.mbl.jif.utils.diag.TestObject;
import java.lang.reflect.*;

/**
 * This class analyses the state of any Object.
 * Specificly, it uses reflection to query an Object about all it's Fields and their values.
 * <p>
 * Since the purpose of the toString method in most classes is simply to return a String
 * representing the Object's state, <i>this class makes it extremely trivial to write
 * a toString method</i>. In particular,
 * <blockquote>
 * <code>
 * public String toString() throws IllegalArgumentException, SecurityException {<br>
 * return StateAnalyser.getState(this);<br>
 * }<br>
 * </code>
 * </blockquote>
 * is all most classes will need to write.
 * <p>
 * There are a few limitations with the above quick toString implementation.
 * First, see the warnings in the getState method.
 * Second, the above implementation will be slower than a custom written toString implementation.
 * (This is rarely a concern, as toString is usually used for diagnostics, but if speed is a factor,
 * then you will have to custom write toString.)
 * <p>
 * Unless otherwise noted, all the methods of this class are multithread safe.
 * <p>
 * <b>Warning</b>: the above statement is true only with respect to the operation
 * of this class itself. A major caveat must be given for any argument
 * that is a mutable Object. Here, the possibility exists that another Thread
 * (besides the Thread calling our method) could be simultaneously modifying the Object.
 * Unpredictable results might occur.
 * <p>
 * @see #getState
 * @author Brent Boyer
 */
public class StateAnalyzer {

// -------------------- constants --------------------

  /**
   * A guesstimate for the number of chars that will be needed to describe
   * the state of a typical Object.
   */
  protected static final int TYPICAL_STATE_CHAR_SIZE = 256;

// -------------------- constructor --------------------

  /** This private constructor suppresses the default (public) constructor, ensuring non-instantiability. */
  private StateAnalyzer() {}

// -------------------- getState --------------------

  /**
   * This method returns a String which describes the target Object (classname and hashcode)
   * and all of it's declared Fields (names and values).
   * (A declared field is one that is declared in the source code of a class -- it does not include
   * Fields defined in any superclasses.)
   * Additionally, it prints out the current Thread which is executing this method.
   * <p>
   * In order to get the values of otherwise inaccessible Fields (e.g. private ones),
   * this method will call <code>setAccessible(true)</code> on every Field.
   * When this method is thru with a given Field, however, it will restore the original
   * accessibility setting.
   * <p>
   * <b>Caveats and Warnings</b>:
   * <ul>
   * <li>
   * <i>the formatting of the String returned by this method was designed for
   * Fields of primitive or String type</i>; if the Field is an Object type which
   * implements toString and returns a multi-line result, it may not look great
   * </li>
   * <li>
   * this method will not work at all if there are security policies against reflecting
   * on the target Object and/or getting it's Field names and values
   * </li>
   * <li>
   * <i>there is the danger that infinite callbacks could occur</i> (e.g. if a Field is of a type
   * that implements toString, and if that type's toString method calls back this method);
   * even if that does not happen, you could still generate an enormous result
   * for sufficiently complicated object graphs
   * </li>
   * </ul>
   * <p>
   * @throws IllegalArgumentException if arg target is null
   * @throws SecurityException if a SecurityManager forbids some action (e.g. accessing the Fields, or calling a Field's setAccessible method)
   */
  public static final String getState(Object target) throws
      IllegalArgumentException, SecurityException {
    if (target == null) {
      throw new IllegalArgumentException("arg target is null");
    }

    StringBuffer sb = new StringBuffer(TYPICAL_STATE_CHAR_SIZE);
    appendObjectInfo(target, sb);
    sb.append(" [\n");
    appendFieldsInfo(target.getClass().getDeclaredFields(), target, sb);
    sb.append("]");
    appendThreadInfo(sb);

    return sb.toString();
  }

  protected static final void appendObjectInfo(Object target, StringBuffer sb) {
    sb.append(target.getClass().getName())
        .append(" (hashcode: ")
        .append(target.hashCode())
        .append(")");
  }

  protected static final void appendFieldsInfo(Field[] fields, Object target,
      StringBuffer sb) {
    for (int i = 0; i < fields.length; i++) {
      boolean originalAccessibility = fields[i].isAccessible();
      fields[i].setAccessible(true); // force accessibility for the duration of this method

      sb.append("\t")
          .append(fields[i].getName())
          .append(": ")
          .append(extractFieldValue(fields[i], target))
          .append("\n");

      fields[i].setAccessible(originalAccessibility); // restore original accessibility
    }
  }

  protected static final Object extractFieldValue(Field field, Object target) {
    try {
      return field.get(target);
    } catch (Throwable t) {
      return handleGetProblem(t);
    }
  }

  /**
   * Returns a useful diagnostic message. (See the description in the javadocs
   * for the get method of Field for where I got the descriptions.)
   * <p>
   * This method was written genericly, for any invocation of the get method.
   * However, in this class, other than an ExceptionInInitializerError,
   * the other Exceptions in the code below should never happen if the appendFieldsInfo
   * method works as expected.
   */
  protected static final String handleGetProblem(Throwable t) {
    if (t instanceof IllegalAccessException) {
      return "<problem: argument is inaccessible>";
    }

    else if (t instanceof IllegalArgumentException) {
      return "<problem: it appears that the Object supplied to the Field's get method is not an instance of the class or interface corresponding to the Field>";
    }

    else if (t instanceof NullPointerException) {
      return "<problem: the Object supplied to the Field's get method is null, but the Field is an instance member>";
    }

    else if (t instanceof ExceptionInInitializerError) {
      return "<problem: the initialization provoked by the get method failed>";
    }

    else {
      return "<the following unexpected problem happened: "; // +
      // ThrowableUtil.getTypeAndMessage(t) + ">";
    }
  }

  protected static final void appendThreadInfo(StringBuffer sb) {
    sb.append("\n")
        .append("{Current Thread: ")
        .append(Thread.currentThread().toString())
        .append("}");
  }

// -------------------- Test (inner class) --------------------

  /**
   * An inner class that consists solely of test code for the parent class.
   * <p>
   * Putting all the test code in an inner class rather than a <code>main</code>
   * method of the parent class has the following benefits:
   * <ul>
   * <li>the test code is cleanly separated from the working code</li>
   * <li>any <code>main</code> in the parent class is now reserved for a true program entry point</li>
   * <li>all the test code may be easily excluded from the final shippable product
   * by removing all the Test class files
   * (e.g. on Windoze, delete all files that end with <i>$Test.class</i>)</li>
   * </ul>
   */

  class ReflectionTest {

    public ReflectionTest() {
       final String SOME_STRING = "yabba dabba do";
       boolean someBoolean = true;
       int someInt = 3;
    }
  }

  public static void main(String[] args) throws Exception {
    System.out.println(
        StateAnalyzer.getState(new TestObject())
      );
  }

}
