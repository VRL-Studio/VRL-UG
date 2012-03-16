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
            new PluginIdentifier("UG4", "0.1.1");//+"."+Math.abs((int)System.currentTimeMillis()));//"0.1.1."+System.currentTimeMillis() did not work
    
    
    public final static String JAR_PATH_KEY = "jarPath";
    public final static String PATH_TO_LOCAL_SERVER_FOLDER_KEY = "localServerFolder";
    public final static String REMOTETYPE_KEY = "remoteType";
    public final static String SERVER_SUFFIX = "-server";
}
