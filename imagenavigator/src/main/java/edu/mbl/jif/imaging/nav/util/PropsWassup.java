package edu.mbl.jif.imaging.nav.util;

// Wassup.java the CMP What's Up Amanuensis
/*
Applet to display all the Java system properties May also be
run as an application.

Wassup 1.9 let's you know what's up with your Java
environment. It will tell you the System Properties, as many
as the Security system will let you peek at. This includes
such things as which JVM is running, which version of Java,
which vendor. It may be run as either an applet or an
application. When you run Wassup as an application it shows
you considerably more information. The Java sandbox
considers it a security risk to reveal that information to
an Applet. As an application, you can see all the possible
System Properties. When you run as an unsigned Applet, your
view is much more limited. Version 1.6 allows you to see the
restricted properties if you use the Java Plug-in 1.2 and
grant permission, or if you run it as an application.
The restricted properties include all the properties there
are. The safe ones include only those you can see in an
Applet with out signing and a security grant. Version 1.7
updates the jar signing certificate.

System.getProperty() vs System.getPropreties.getProperty()
You need write access to be able to use the second form that
lets you list all possible properties, even if you don't
know their names.

It will produce output something like this:

awt.toolkit = sun.awt.windows.WToolkit
file.encoding = Cp1252
file.encoding.pkg = sun.io
file.separator = \
java.awt.fonts =
java.awt.graphicsenv = sun.awt.Win32GraphicsEnvironment
java.awt.printerjob = sun.awt.windows.WPrinterJob
java.class.path = ..\..\
java.class.version = 46.0
java.compiler = symcjit
java.ext.dirs = D:\JDK1.2\jre\lib\ext
java.home = D:\JDK1.2\jre
java.io.tmpdir = D:\TEMP\
java.library.path = D:\JDK1.2\BIN;.;D:\WINNT\System32;D:\WINNT;D:\jdk1.2\bin
java.specification.name = Java Platform API Specification
java.specification.vendor = Sun Microsystems Inc.
java.specification.version = 1.2
java.vendor = Sun Microsystems Inc.
java.vendor.url = http://java.sun.com/
java.vendor.url.bug = http://java.sun.com/cgi-bin/bugreport.cgi
java.version = 1.2
java.vm.info = build JDK-1.2-V, native threads, symcjit
java.vm.name = Classic VM
java.vm.specification.name = Java Virtual Machine Specification
java.vm.specification.vendor = Sun Microsystems Inc.
java.vm.specification.version = 1.0
java.vm.vendor = Sun Microsystems Inc.
java.vm.version = 1.2
line.separator = [binary chars: 0x0d 0x0a i.e. CrLf, \r\n]
os.arch = x86
os.name = Windows NT
os.version = 4.0
path.separator = ;
sun.boot.class.path = D:\JDK1.2\jre\lib\rt.jar;D:\JDK1.2\jre\lib\i18n.jar;D:\JDK1.2\jre\classes
sun.boot.library.path = D:\JDK1.2\jre\bin
sun.io.unicode.encoding = UnicodeLittle
user.dir = C:\com\mindprod\wassup
user.home = D:\WINNT\Profiles\Administrator
user.language = en
user.name = Administrator
user.region = US
user.timezone = America/Los_Angeles
 */
/**
 * @author  copyright (c) 1998-2003 Roedy Green, Canadian Mind Products
 * may be copied and used freely for any purpose but military.
 *
 * Roedy Green
 * Canadian Mind Products
 * #327 - 964 Heywood Avenue
 * Victoria, BC Canada V8V 2Y5
 * tel: (250) 361-9093
 * mailto:roedy@mindprod.com
 * http://mindprod.com
 *
 * version 1.9 2002 April 8
 *                   - lower case package
 * version 1.8 2002 March 30
 *                   - use phony Sun cert, instead of Netscape code signing.
 *                   - switch to SunSort from HeapSort
 *
 * version 1.7 2000 December 28
 *                   - update jar signing certificate
 *
 * version 1.6 1999 October 3
 *                   - use JDK 1.2 RSA signed jar
 *                   - select restricted or safe.
 *
 * version 1.5 1999 January 7
 *                   - allow Wassup to execute without a GUI, based on code by
 *                   - David B. Gleason (mailto:David.Gleason@bull.com)
 *                   - Bull HN Information Systems, Inc.
 *
 * version 1.4 1998 December 28
 *                   - use new Comparator interface
 *
 * version 1.3 1998 December 21
 *                   - add test BAT and HTML files for various JVMs
 *                     and browsers.
 *
 * version 1.2 1998 December 14
 *                   - add codebase
 *                   - colors done a different way
 *                   - check for proper JVM version.
 * version 1.1 1998 December 1
 *                   - try exhaustive list of system properties for
 *                     Applets.
 *                   - bigger screen.
 *
 * version 1.0 1998 November 30
 *
 */
import java.applet.Applet;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Properties;

public class PropsWassup
    extends Applet {

    private static final String EmbeddedCopyright =
        "copyright (c) 1998-2003 Roedy Green, Canadian Mind Products, http://mindprod.com";
    private static final String title = "CMP wassup 1.9";
    private static final String appletProperties =
        "Java System Properties accessible to unsigned Applets";
    private static final String applicationProperties =
        "Java System Properties accessible to signed Applets and applications";    // Use our own colours so Symantec won't mess with them or create dups.
    private static final Color black = Color.black;
    private static final Color blue = Color.blue;
    private static final Color darkGreen = new Color(0, 128, 0);
    private static final Color red = Color.red;
    private static final Color white = Color.white;    // true if permit Restricted  mode where we get at restricted properties
    private boolean allowRestricted = false;

    /**
     * constructor
     */
    public PropsWassup() {
    }

    /**
     * Constructor
     *
     * @param allowRestricted true if we can get at restricted properties
     */
    private PropsWassup(boolean allowRestricted) {
        this.allowRestricted = allowRestricted;
    }

    /**
     * Allow this applet to run as as application as well.
     *
     * @param args command line argument, nogui if want output to System.out
     */
    public static void main(String args[]) {

        boolean gui = true;
        // check for nogui command line argument
        if (args.length > 0 && (args[0].equalsIgnoreCase("nogui") || args[0].equalsIgnoreCase("/nogui") || args[0].equalsIgnoreCase("-nogui")))
          {
            gui = false;
          }
        // check for gui=false system property.
        // Can only do this in an application.
        if (System.getProperty("GUI", "true").equals("false"))
          {
            gui = false;
          }

        if (gui)
          {
            // display results on screen
            final PropsWassup applet = new PropsWassup(true /* allowRestricted */);
            Frame frame = new Frame("Wassup");
            frame.setSize(460, 270);
            applet.init();
            frame.add(applet);
            frame.validate();
            frame.setVisible(true);
            applet.start();
            frame.addWindowListener(
                new WindowAdapter() {

                    /**
                     * Handle request to shutdown.
                     * @param e event giving details of closing.
                     */
                    public void windowClosing(WindowEvent e) {
                        applet.stop();
                        applet.destroy();
                        System.exit(0);
                    } // end WindowClosing

                } // end anonymous class
                ); // end addWindowListener line
          } else
          {
            // non gui version
            /* display on System.out */
            outputProperties(true);
          }
    } // end main

    /**
     * start the applet
     */
    public void init() {

        setBackground(white);

        /* really should ask permission and only set true if get the ok. !!! */
        /* This is a quick hack to get this code working again.  It used to work with Netscape
        code signing. */
        allowRestricted = true;

        GridBagLayout gridBagLayout;
        GridBagConstraints gbc;

        gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);
        titleLabel = new Label(title, Label.CENTER);
        titleLabel.setBounds(218, 0, 158, 30);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        titleLabel.setForeground(red);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        ((GridBagLayout) getLayout()).setConstraints(titleLabel, gbc);
        add(titleLabel);

        safe = new Choice();
        safe.setFont(new Font("Dialog", Font.PLAIN, 12));
        safe.setForeground(blue);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 10, 10, 10);
        ((GridBagLayout) getLayout()).setConstraints(safe, gbc);
        safe.addItem("safe");
        safe.select(0);
        if (allowRestricted)
          {
            safe.addItem("restricted");
          }
        safe.addItemListener(new ItemListener() {

            /**
             * Notice any change to safe/restricted choice
             *
             * @param event details of just what the user clicked.
             *
             */
            public void itemStateChanged(ItemEvent event) {
                Object object = event.getSource();
                if (object == safe)
                  {
                    showProperties();
                  } // end if
            } // end itemStateChanged

        } // end anonymous class
            ); // end addActionListener line
        add(safe);

        keyValuePairs = new TextArea("", 0, 0,
            TextArea.SCROLLBARS_VERTICAL_ONLY);
        keyValuePairs.setEditable(false);
        keyValuePairs.setBounds(10, 40, 574, 93);
        keyValuePairs.setFont(new Font("Dialog", Font.PLAIN, 15));
        keyValuePairs.setForeground(black);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 4;
        gbc.weightx = 100.0;
        gbc.weighty = 100.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        ((GridBagLayout) getLayout()).setConstraints(keyValuePairs, gbc);
        add(keyValuePairs);

        captionLabel = new Label(allowRestricted ? applicationProperties : appletProperties, Label.CENTER);
        captionLabel.setFont(new Font("Dialog", Font.BOLD, 11));
        captionLabel.setForeground(blue);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        ((GridBagLayout) getLayout()).setConstraints(captionLabel, gbc);
        add(captionLabel);
        showProperties();
        this.validate();
        this.setVisible(true);

    } // end init

    /**
     * Show properties and update caption based on current safe/restricted selection.
     */
    void showProperties() {
        // Fill the textarea with the properties
        boolean allowRestricted = safe.getSelectedIndex() != 0;
        captionLabel.setText(allowRestricted ? applicationProperties : appletProperties);
        keyValuePairs.setText(allowRestricted ? displayAllProperties("\n\n") : displaySafeProperties("\n\n"));
        this.invalidate();
        this.validate();
        this.repaint();
    } // end showProperties


    // field declarations
    Label titleLabel;
    Choice safe;
    Label captionLabel;
    TextArea keyValuePairs;

    /**
     * output system properties to System.out
     */
    public static void outputProperties(boolean allowRestricted) {
        /* display on System.out */
        System.out.println(title);
        System.out.println(allowRestricted ? applicationProperties : appletProperties);
        System.out.println();
        String lineSeparator = System.getProperties().getProperty(
            "line.separator");
        lineSeparator += lineSeparator;
        System.out.println(allowRestricted ? displayAllProperties(lineSeparator) : displaySafeProperties(lineSeparator));

    } // end outPutProperties

    /**
     * Get a sorted list of all the system properties.
     * Only works in applications.
     * @param separator usually "\n\n"
     */
    public static String displayAllProperties(String separator) {
        try
          {

            Properties sysprops = System.getProperties();

            // Count properties
            int count = sysprops.size();

            // prepare Matrix to hold the properties
            String[][] matrix = new String[count][2];

            // read System properties into the matrix
            int j = 0; // Java won't let me put this in the for loop,  Ouch!
            for (Enumeration e = sysprops.propertyNames(); j < count; j++)
              {
                String key = (String) e.nextElement();
                String value = sysprops.getProperty(key);
                matrix[j][0] = key;
                matrix[j][1] = value;
              } // end for

            // sort by key
            Arrays.sort(matrix, new StringComparator());

            // concatenate all key value pairs.
            StringBuffer result = new StringBuffer(4096);
            for (int i = 0; i < count; i++)
              {
                String key = matrix[i][0];
                if (key != null)
                  {
                    String value = matrix[i][1];
                    if (value != null)
                      {
                        if (value.equals("\r\n"))
                          {
                            value = "[hex chars: 0x0d 0x0a i.e. CrLf, \\r\\n]";
                          } else if (value.equals("\n"))
                          {
                            value = "[hex char: 0x0a i.e. Lf, \\n]";
                          } else if (value.equals("\r"))
                          {
                            value = "[hex char: 0x0d i.e. Cr, \\r]";
                          }
                        result.append(key);
                        result.append(" = ");
                        result.append(value);
                        result.append(separator);
                      }
                  }

              } // end for
            return result.toString();
          } catch (Exception e)
          {
            return "No security clearance to see the restricted System properties.";
          }
    } // end displayAllProperties

    /**
     * Get a sorted list of all the safe system properties.
     * Only works in Applets and applications.
     * @param separator usually \n\n
     */
    public static String displaySafeProperties(String separator) {
        // for documentation on System properties see "properties" in the Java glossary
        // at http://mindprod.com/gloss.html or in the JavaDoc for System.getProperties.
        String[] safeNames =
          {
            "awt.toolkit",
            "file.encoding",
            "file.encoding.pkg",
            "file.separator",
            "java.awt.fonts",
            "java.awt.graphicsenv",
            "java.awt.printerjob",
            "java.class.path",
            "java.class.version",
            "java.compiler",
            "java.ext.dirs",
            "java.home",
            "java.io.tmpdir",
            "java.library.path",
            "java.specification.name",
            "java.specification.vendor",
            "java.specification.version",
            "java.vendor",
            "java.vendor.url",
            "java.vendor.url.bug",
            "java.version",
            "java.vm.info",
            "java.vm.name",
            "java.vm.specification.name",
            "java.vm.specification.vendor",
            "java.vm.specification.version",
            "java.vm.vendor",
            "java.vm.version",
            "line.separator",
            "os.arch",
            "os.name",
            "os.version",
            "path.separator",
            "sun.boot.class.path",
            "sun.boot.library.path",
            "sun.io.unicode.encoding",
            "user.dir",
            "user.home",
            "user.language",
            "user.name",
            "user.region",
            "user.timezone"
          };
        int count = safeNames.length;
        // no need to sort, already in alpha order
        // concatenate all key value pairs.
        StringBuffer result = new StringBuffer(4096);
        for (int i = 0; i < count; i++)
          {
            String key = safeNames[i];
            if (key != null)
              {
                try
                  {
                    String value = System.getProperty(key);
                    if (value != null)
                      {
                        if (value.equals("\r\n"))
                          {
                            value =
                                "[binary chars: 0x0d 0x0a i.e. CrLf, \\r\\n]";
                          } else if (value.equals("\n"))
                          {
                            value = "[binary char: 0x0a i.e. Lf, \\n]";
                          }
                        result.append(key);
                        result.append(" = ");
                        result.append(value);
                        result.append(separator);
                      } // end if
                  } catch (Exception e)
                  {
                    /* if not allowed to peek, we don't display anything */
                  }
              } // end if
          } // end for
        return result.toString();
    } // end displaySafeProperties


    // Callback delegate to describe collating sequence
    static protected class StringComparator
        implements Comparator {

        // Compare column 0 of the matrix, case insensitive.
        // e.g. +1 (or any +ve number) if a > b
        //       0                     if a == b
        //      -1 (or any -ve number) if a < b
        public final int compare(Object a, Object b) {
            String aa = (((String[]) a)[0]).toLowerCase();
            String bb = (((String[]) b)[0]).toLowerCase();
            return aa.compareTo(bb);
        } // end compare

        public final boolean equals(Object a, Object b) {
            String aa = (((String[]) a)[0]).toLowerCase();
            String bb = (((String[]) b)[0]).toLowerCase();
            return aa.equals(bb);
        }

    } // end class StringComparator
} // end class Wassup
