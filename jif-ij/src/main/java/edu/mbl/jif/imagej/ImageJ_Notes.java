package edu.mbl.jif.imagej;

/**
 This plugin demonstrates how plugins can be packaged in JAR files and installed
 in multiple menus. The JAR file must have an underscore in its name and contain
 a file named "plugins.config". This is what "plugins.config" in the JAR file
 containing this plugin ("jar_demo.jar") looks like:

 # Name: JAR_Plugin
 # Author: Wayne Rasband
 # Version: 1.2
 # Date: 2003/12/15
 # Requires: ImageJ 1.31s
 # Generate the jar file using: "jar cvfM jar_demo.jar *"

 File>Import, "JAR Demo (Import 1)...", JAR_Plugin("import1")
 File>Import, "JAR Demo (Import 2)...", JAR_Plugin("import2")
 File>Save As, "JAR Demo (Save As)...", JAR_Plugin("save")
 Edit>Options, "JAR Demo...", JAR_Plugin("options1")
 Analyze>Tools, "JAR Demo (Tools)...", JAR_Plugin("tools")
 Plugins>JAR Demo, "Run JAR Demo...", JAR_Plugin("run")
 Plugins>JAR Demo, "JAR Demo Options...", JAR_Plugin("options2")
 Plugins>JAR Demo, "About JAR Demo...", JAR_Plugin("about1")
 Plugins>JAR Demo, "Uninstall JAR Demo...", JAR_Plugin("uninstall")
 Plugins, "JAR Demo (Plugins)...", JAR_Plugin("plugins")
 Help>About Plugins, "JAR_Plugin...", JAR_Plugin("about2")

 Lines starting with '#" are comments that may be used by a future Plugin
 Manager. Each line in the form

     Menu>Submenu, "Command", Plugin

 creates an ImageJ menu command. There are three parts, separated by commas. The
 first specifies the menu where the command will be installed, the second is the
 command name, and the third is the plugin. As an example, the line

     File>Save As, "JAR Demo (Save As)...", JAR_Plugin("save")

 installs the command "JAR Demo (Save As)..." in the File>Save As menu. When the
 user selects this command ImageJ runs the plugin JAR_Plugin.class, passing it
 the argument "save".

 Plugins can be installed in the File>Import, File>Save As, Edit>Options,
 Analyze>Tools, Help>About Plugins and Plugins menu and submenus. If
 Plugins>Submenu is specified, the command will installed in the submenu of the
 Plugins manu named Submenu. If this submenu does not exist, it is created. If
 Plugins is specified, the command will be installed in the submenu of the
 Plugins menu that corresponds to the directory containing the JAR file. For
 example, if the JAR file is in ImageJ/plugins/jars, the command will be
 installed in Plugins>jars.

 JAR files are basically the same as ZIP files. They are created using the jar
 utility or a ZIP utility such WinZip. The jar utility is included with Sun's
free Java SDK (aka JDK). It is pre-installed on Mac OS X.
 */


public class ImageJ_Notes
{
    public ImageJ_Notes () {
    }}
