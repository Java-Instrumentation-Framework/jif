/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.utils;
/*
 * File: EntityInspectorTest.java
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
 * $Id: EntityInspectorTest.java,v 1.9 2013/04/24 22:51:31 mg Exp $
 */


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({ "unused", "serial" })
public class EntityInspectorTest {

    private static int id = 0;

    @Before
    public void prepare() {
        // Comment out to test array output limit:
        //EntityInspector.MAX_ARRAY_SIZE = 2;
    }
    
    private void printHeader(String title) {
        System.out.printf("\n==== %s ====", title);
    }
    
    @Test
    public void testPrimitives() {
        printHeader("Testing primitives");
        System.out.println(EntityInspector.getContent(new PrimitivesEntity()));
    }
    
    @Test
    public void testWrapperClasses() {
        printHeader("Testing primitive wrappers");
        System.out.println(EntityInspector.getContent(new WrappersEntity()));
    }
    
    @Test
    public void testBasicClasses() {
        printHeader("Testing basic classes");
        System.out.println(EntityInspector.getContent(new BasicsEntity()));
    }
    
    @Test
    public void testPrimitivesArrays() {
        printHeader("Testing primitive arrays");
        System.out.println(EntityInspector.getContent(new PrimitiveArraysEntity()));
    }
    
    @Test
    public void testWrappersArrays() {
        printHeader("Testing wrapper arrays");
        System.out.println(EntityInspector.getContent(new WrapperArraysEntity()));
    }
    
    @Test
    public void testBasicArrays() {
        printHeader("Testing basic arrays");
        System.out.println(EntityInspector.getContent(new BasicArraysEntity()));
    }
    
    @Test
    public void testPerson() {
        //EntityInspector.MAX_OUTPUT_LINES = 20;
        printHeader("Testing Person");
        System.out.println(EntityInspector.getContent(new Person()));
    }
    
    @Test
    public void testEnum() {
        printHeader("Testing enumerations");
        System.out.println(EntityInspector.getContent(new Worker()));
    }
    
    @Test
    public void testWithObject() {
        printHeader("Testing declared as Object");
        System.out.println(EntityInspector.getContent(new WithObject()));
    }
    
    public abstract static class BaseEntity {
        private int nID = ++id;
    }
    public static class PrimitivesEntity extends BaseEntity {
        private byte byValue = 41;
        private short shValue = 56;
        private long  lValue = 78;
        private float fValue = 12.34f;
        private double dValue = 56.78d;
        private boolean bValue = true;
        private char chValue = 'm';
    }
    public static class WrappersEntity extends BaseEntity {
        private Byte byValue = 41;
        private Short shValue = 56;
        private Integer intValue = 56;
        private Long lValue = 78L;
        private Float fValue = 12.34f;
        private Double dValue = 56.78d;
        private Boolean bValue = true;
        private Character chValue = 'm';
    }
    public static class BasicsEntity extends BaseEntity {
        private String sValue = "This is string";
        private Timestamp timestamp = new Timestamp(System.currentTimeMillis()); 
        private java.util.Date dateUtil = new java.util.Date(); 
        private java.sql.Date dateSql = new java.sql.Date(System.currentTimeMillis()); 
    }
    public static class PrimitiveArraysEntity extends BaseEntity {
        private byte[] ar_byValue = new byte[] { 1, 2, 3, 4, 5, 6 };
        private short[] ar_shValue = new short[] { 10, 20, 30, 40, 50, 60 };
        private int[] ar_nValue = new int[] { 11, 22, 33, 44, 55, 66 };
        private long[] ar_lValue = new long[] { 12, 23, 34, 44, 54, 64 };
        private float[] ar_fValue = new float[] { 12.34f, 56.78f };
        private double[] ar_dValue = new double[] { 11.22d, 56.78d };
        private boolean[] ar_bValue = new boolean[] { true, false, false };
        private char[] ar_chValue = new char[] { 'a', 'b', 'x', 'z' };
    }
    public static class WrapperArraysEntity extends BaseEntity {
        private Byte[] ar_ByteValue = new Byte[] { 1, 2, 3, 4, 5, 6 };
        private Short[] ar_ShValue = new Short[] { 11, 22, 33, 44, 55, 66 };
        private Integer[] ar_IntValue = new Integer[] { 10, 20, 30, 40, 50, 60 };
        private Long[] ar_LValue = new Long[] { 11L, 22L, 33L, 44L, 55L, 66L };
        private Float[] ar_FloatValue = new Float[] { 12.34f, 56.78f };
        private Double[] ar_DoubleValue = new Double[] { 11.22d, 56.78d };
        private Boolean[] ar_BoolValue = new Boolean[] { true, false, false };
        private Character[] ar_CharValue = new Character[] { 'a', 'b', 'x', 'z' };
    }
    public static class BasicArraysEntity extends BaseEntity {
        private String[] ar_sValue = new String[] { 
                "This is string", "One", "Two" 
        };
        private Timestamp[] ar_timestamp = new Timestamp[] { 
            new Timestamp(System.currentTimeMillis()), 
            new Timestamp(getNextDaysMillis(1)), 
            new Timestamp(getNextDaysMillis(2)), 
        };
        private java.util.Date[] ar_dateUtil = new java.util.Date[] {
                new java.util.Date(),
                new java.util.Date(getNextDaysMillis(3)),
                new java.util.Date(getNextDaysMillis(4)),
        };
        private java.sql.Date[] ar_dateSql = new java.sql.Date[] {
                new java.sql.Date(System.currentTimeMillis()),
                new java.sql.Date(getNextDaysMillis(5)),
                new java.sql.Date(getNextDaysMillis(6))
        };
        private List<java.sql.Date> lstDate = new ArrayList<java.sql.Date>() {
            {
                add(new java.sql.Date(System.currentTimeMillis()));
                add(new java.sql.Date(getNextDaysMillis(7)));
                add(new java.sql.Date(getNextDaysMillis(8)));
            }
        };
    }
    public static class Person extends BaseEntity {
        Person() {
            parent = new Person("Grand Joe");
        }
        Person(String parentName) {
            name = parentName;
        }
        private Person parent = null;
        private String name = "Joe";
        private Address address = new Address();
        private Child[] kidsArray = { new Child(), new Child() };
        private int[] years = new int[] { 2001, 2002, 2003 };
        @SuppressWarnings("rawtypes")
        private List[] kidsList = new List[] { 
            new ArrayList<Child>() {
               { add(new Child()); add(new Child()); }
            },
            new ArrayList<Child>() {
                { add(new Child()); add(new Child()); }
            }
        };
        private int[][] marks = new int[][] {
            new int[] {1,2,3,4,5,6},
            new int[] {9,8,7,6,5,4}
        };
    }
    public static class Address extends BaseEntity {
        private String city = "Washington-" + id;
        private String street = "Main Street";
    }
    public static class Child extends BaseEntity {
        private String name = "Child-" + id;
    }
    private static long getNextDaysMillis(int nDays) {
        return System.currentTimeMillis() + 1000 * 60 * 60 * 24 * nDays;
    }
    public static class Worker extends BaseEntity {
        private String name = "Child-" + id;
        private WeekDaysEnum nightShift = WeekDaysEnum.WEDNESDAY; 
    }
    public enum WeekDaysEnum {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY;
    }
    public static class WithObject extends BaseEntity {
        private Address address1 = new Address();
        private Object address2 = new Address();
    }
    
    public static void main(String[] args) {
       String output = EntityInspector.getContent(new Person()) ;
       System.out.println(output);
   }
} // class EntityInspectorTest