/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.system.PluginIdentifier;

/**
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Constants {
    public static final PluginIdentifier PLUGIN_IDENTIFIER =
            new PluginIdentifier("UG4", "0.1");//"0.1.1."+System.currentTimeMillis() did not work
    
    
    public static String SERVER_JAR_PATH_KEY = "serverJarPath";
    public static String REMOTETYP_KEY = "rpc";
}
