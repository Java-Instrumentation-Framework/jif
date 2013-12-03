/*
from http://www.jdotsoft.com/download/EntityInspector.java
 */

package edu.mbl.jif.utils;
/*
 * File: EntityInspector.java
 * 
 * Copyright (C) 2013 JDotSoft. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 * $Id: EntityInspector.java,v 1.9 2013/04/24 22:51:31 mg Exp $
 */

/*
A simple class to inspect Java objects content. It provides the following functionality:

    Inspection result is generated as a String. This is useful for logging.
    Each inspected variable displayed as class name = value in familiar Java object declaration style.
    All variables are inspected recursively. Child variables are indented for nice formatting.
    The variable inspection recursion stops after reaching specified class or package.
    The list with excluded from inspection classes could be modified.
    The default list with excluded from inspection classes contains standard Java packages, 
         some common libraries and Hibernate byte code generation libraries.
    Arrays and collections inspection is supported for any types of objects.
    Arrays and collections content in the output could be limited.

*/

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class to inspect Java objects content.
 */
@SuppressWarnings("serial")
public class EntityInspector {

    /**
     * Limit collections and arrays output. 
     */
    public static final int NO_LIMIT_SIZE = -1;
    public static int MAX_ARRAY_SIZE = NO_LIMIT_SIZE;
    
    /**
     * Limit output to NN rows. 
     * This might be required to debug huge output and eventually modify
     * {@link #DEFAULT_EXCLUSION_LIST}
     */
    public static int MAX_OUTPUT_LINES = NO_LIMIT_SIZE;
    
    /**
     * Array elements are displayed in a single string if element displayed 
     * as toString() and its size is less than specified, otherwise in a column.
     */
    private static final int DEFAULT_MAX_ELEMENT_SIZE = 10;
    public static int MAX_ELEMENT_SIZE = DEFAULT_MAX_ELEMENT_SIZE;
    
    /**
     * Use simple class name for field class declaration.
     */
    private static final boolean DEFAULT_CLASS_SIMPLE = true;
    public static boolean CLASS_SIMPLE = DEFAULT_CLASS_SIMPLE;
    
    /**
     * Default exclusion list. See details in {@link #EXCLUSION_LIST}.
     * <p>
     * Feel free to add/modify this list in your own copy of this class.  
     */
    private static List<String> DEFAULT_EXCLUSION_LIST = new ArrayList<String>() {
        { add("java.");
          add("javax.");
          add("org.apache.");
          add("org.hibernate.");
          // Hibernate instruments classes with ByteCodeProvider.
          // CGLIB was replaced with javassist in Hibernate 3.3.0 
          // See http://opensource.atlassian.com/projects/hibernate/browse/HHH-2506
          // Any instrumentation should NOT be inspected:
          add("javassist."); // do not inspect javassist class created by new Hibernate
          add("net.sf."); // do not inspect "net.sf.cglib" class created by old Hibernate
        }
    };
    
    /**
     * List with class names which are not inspected. The list contains string 
     * entries which are compared one-by-one to the field class name in the 
     * object to inspect. The field is not inspected if its class name starts 
     * with any one listed in the exclusion list.
     * <p>
     * Update this list as required by calling 
     * <code>EntityInspector.EXCLUSION_LIST.add("com.abc.");</code> to exclude
     * some "com.abc." class from inspection.  
     */
    public static List<String> EXCLUSION_LIST = new ArrayList<String>() {
        { addAll(DEFAULT_EXCLUSION_LIST); }
    };
    
    private enum FieldShowType { 
        SHOW_AS_TO_STRING, // for primitives and in exclusion list 
        SHOW_AS_COLLECTION, 
        SHOW_AS_ARRAY, 
        SHOW_CONTENT, 
    }
    
    /** Indent for nested output. */
    private static String INDENT = "   ";

    /** Collection with already inspected elements (to prevent recursion). */
    private Set<String> recursionGuard;
    
    /** Output buffer. */
    private StringBuilder sbOut;
    
    /** Output lines counter. */
    private int outLinesCount;
    
    /**
     * Do not instantiate from outside.
     */
    private EntityInspector() {
        recursionGuard = new HashSet<String>();
        sbOut = new StringBuilder();
    }

    /**
     * Resets static values to default.
     */
    public static void reset() {
        MAX_ARRAY_SIZE = NO_LIMIT_SIZE;
        MAX_ELEMENT_SIZE = DEFAULT_MAX_ELEMENT_SIZE;
        CLASS_SIMPLE = DEFAULT_CLASS_SIMPLE;
        EXCLUSION_LIST.clear();
        EXCLUSION_LIST.addAll(DEFAULT_EXCLUSION_LIST);
    }
    
    /**
     * Call this method to get object's content as a string.
     * 
     * @param obj the object to inspect.
     * @return content as string (for logging).
     */
    public static String getContent(Object obj) {
        return getContent(null, obj);
    }

    /**
     * Call this method to get object's content as a string.
     * 
     * @param title this title will appear before the output.
     * @param obj the object to inspect.
     * @return content as string (for logging).
     */
    public static String getContent(String title, Object obj) {
        return new EntityInspector().getContentPrivate(title, obj);
    }

    private String getContentPrivate(String title, Object obj) {
        if (title != null) {
            addContent(title, ": ");
        }
        if (obj == null) {
            addContent("=== The object is null ===");
        } else {
            getFields(true, obj, 0);
        }
        return sbOut.toString();
    }    
    
    private void getFields(boolean newLine, Object obj, int level) {
        Class<?> clazz = obj.getClass();
        if (newLine) addNewLine(level);
        if (getFieldShowType(clazz) == FieldShowType.SHOW_AS_TO_STRING) {
            addContent(obj.toString());
        } else {
            String clazzName = clazz.getName();
            String identityHashCode = Integer.toHexString(System.identityHashCode(obj));
            addContent(clazzName, "@", identityHashCode);
            // Potential recursion: getFields() -> populateFields() -> getFields() 
            // For example, inspecting parent entity which has a child entity with
            // a reference to a parent entity. Avoid recursion:
            if (recursionGuard.add(identityHashCode)) {
                populateFields(obj, level); // safe, no recursion
            } else {
                addContent(" <-- this object was already processed");
            }
        }
    }

    private void populateFields(Object obj, int level) {
        // Collect classes hierarchy
        List<Class<?>> lstClass = new ArrayList<Class<?>>(); 
        for (Class<?> clazz = obj.getClass();  clazz != null;  clazz = clazz.getSuperclass()) {
            if (clazz.equals(Object.class)) {
                break;
            }
            lstClass.add(0, clazz);
        }        
        
        // Process classes in reverse order for nice members order
        for (Class<?> clazz : lstClass) {
            Field[] fldAll = clazz.getDeclaredFields();
            for (Field fld : fldAll) {
                Class<?> cFld = fld.getType(); // variable declaration in a class
                if (cFld.equals(Method.class)) {
                    continue; // CGLIB injects methods
                }
                addNewLine(level + 1);
                fld.setAccessible(true);
                Class<?> cElement = cFld.getComponentType(); // null for NOT array 
                FieldShowType showType = getFieldShowType(cFld);
                // Field class name:
                if (showType == FieldShowType.SHOW_AS_ARRAY) {
                    addContent(CLASS_SIMPLE ? cElement.getSimpleName() : cElement.getName(), "[]");
                } else {
                    addContent(CLASS_SIMPLE ? cFld.getSimpleName() : cFld.getName());
                    TypeVariable<?>[] types = cFld.getTypeParameters();
                    if (types.length > 0) { // displays: "<E>"
                        addContent("<", types[0].getName(), ">"); 
                    }
                }
                // Field member (variable) name:
                addContent(" ", fld.getName(), " = ");
                // Field member (variable) value:
                try {
                    Object value = fld.get(obj);
                    if (value == null) {
                        addContent("null");
                    } else {
                        if (showType == FieldShowType.SHOW_AS_TO_STRING) {
                            // Update 'showType' based on object class instead of declaration:
                            showType = getFieldShowType(value.getClass());
                        }
                        switch (showType) {
                            case SHOW_CONTENT:
                                getFields(false, value, (level + 1));                                  
                                break;
                            case SHOW_AS_ARRAY:
                                FieldShowType showFieldType = getFieldShowType(cElement);
                                int arraySize = Array.getLength(value);
                                addContent("(size=", arraySize, ") ");
                                switch (showFieldType) {
                                    case SHOW_CONTENT:
                                        for (int i = 0;  i < getShowArraySize(arraySize);  i++) {
                                            addNewLine(level + 2);
                                            getFields(false, Array.get(value, i), (level + 2));
                                        }
                                        if (isArraySizeOverLimit(arraySize)) {
                                            addNewLine(level + 3);
                                            addContent("..");
                                            break;
                                        }
                                        break;
                                    case SHOW_AS_ARRAY:
                                        // Does not work if processed as "case SHOW_CONTENT / SHOW_AS_TO_STRING"
                                        // Displays as "[I@1ea2dfe", "[I@17182c1"
                                        addContent("<array of arrays is not implemented>");
                                        break;
                                    case SHOW_AS_COLLECTION:
                                        // Does not work if processed as "case SHOW_CONTENT"
                                        // Exception: StackOverflowError
                                        addContent("<array of collections is not implemented>");
                                        break;
                                    case SHOW_AS_TO_STRING:
                                        // Array elements are displayed in a single string if element 
                                        // size is < MAX_ELEMENT_SIZE, otherwise in a column.
                                        int elementSize = 0;
                                        for (int i = 0;  i < getShowArraySize(arraySize);  i++) {
                                            elementSize = Math.max(elementSize, Array.get(value, i).toString().length());
                                        }
                                        boolean singleString = (elementSize < MAX_ELEMENT_SIZE); 
                                        if (singleString) addContent("{");
                                        for (int i = 0;  i < getShowArraySize(arraySize);  i++) {
                                            if (singleString) addContent(" "); else addNewLine(level + 2);
                                            addContent(Array.get(value, i));
                                        }
                                        if (isArraySizeOverLimit(arraySize)) {
                                            if (singleString) addContent(" "); else addNewLine(level + 2);
                                            addContent("..");
                                        }
                                        if (singleString) addContent(" }");
                                        break;
                                }
                                break;
                            case SHOW_AS_COLLECTION:
                                Collection<?> collection = (Collection<?>)value;
                                int size = collection.size();
                                addContent("(size=", size, ")");
                                int count = 0;
                                for (Object o : collection) {
                                    count++;
                                    if (count > getShowArraySize(size)) {
                                        break;
                                    }
                                    getFields(true, o, level + 2);
                                }
                                if (isArraySizeOverLimit(size)) {
                                    addNewLine(level + 2);
                                    addContent("..");
                                }
                                break;
                            case SHOW_AS_TO_STRING:
                                addContent(value.toString());
                                break;
                        }
                    }
                } catch (Exception e) {
                    // IllegalArgumentException, IllegalAccessException
                    addContent(e.toString());
                }
            }
        } // for () fields
    }

    private static FieldShowType getFieldShowType(Class<?> c) {
        for (Class<?> inf : c.getInterfaces()) {
            if (inf.equals(java.util.Collection.class)) {
                return FieldShowType.SHOW_AS_COLLECTION;
            }
        }
        if (c.isArray()) {
            return FieldShowType.SHOW_AS_ARRAY;
        }
        if (c.isPrimitive()) {
            return FieldShowType.SHOW_AS_TO_STRING;
        }
        if (c.isEnum()) {
            return FieldShowType.SHOW_AS_TO_STRING;
        }
        String cName = c.getName(); 
        for (String s : EXCLUSION_LIST) {
            if (cName.startsWith(s)) {
                return FieldShowType.SHOW_AS_TO_STRING;
            }
        }
        return FieldShowType.SHOW_CONTENT;
    }    
    
    /**
     * Calculates how many array or collection elements will be displayed.
     * 
     * @param sizeActual actual size of array or collection.
     * @return number of elements to display.
     */
    private int getShowArraySize(int sizeActual) {
        return MAX_ARRAY_SIZE == NO_LIMIT_SIZE ? sizeActual : Math.min(MAX_ARRAY_SIZE, sizeActual);
    }
    
    /**
     * Returns <code>true</code> to show ".." for not displayed elements in 
     * array or collection.  
     * 
     * @param sizeActual actual size of array or collection.
     * @return true if not all elements are displayed.
     */
    private boolean isArraySizeOverLimit(int sizeActual) {
        return MAX_ARRAY_SIZE == NO_LIMIT_SIZE ? false : sizeActual > MAX_ARRAY_SIZE;          
    }
    
    private void addNewLine(int indentLevel) {
        if (outLinesCount == Integer.MAX_VALUE) {
            return;
        }
        outLinesCount++;
        if (MAX_OUTPUT_LINES > 0 && outLinesCount > MAX_OUTPUT_LINES) {
            sbOut.append("\n=== Output is limited to ").append(MAX_OUTPUT_LINES).append(" lines ===");
            outLinesCount = Integer.MAX_VALUE;
            return;
        }
        sbOut.append("\n");
        for (int i = 0;  i < indentLevel;  i++) {
            sbOut.append(INDENT);
        }
    }

    private void addContent(Object ... obj) {
        if (outLinesCount == Integer.MAX_VALUE) {
            return;
        }
        for (Object o : obj) {
            sbOut.append(o);
        }
    }
    
} // class EntityInspector