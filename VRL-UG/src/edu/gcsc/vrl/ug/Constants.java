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

    public final static String PLUGIN_NAME = /*<VRL_UG_PLUGIN_NAME>*/"VRL-UG"/*</VRL_UG_PLUGIN_NAME>*/;
    public final static String PLUGIN_NAME_API = /*<VRL_UG_PLUGIN_API_NAME>*/"VRL-UG-API"/*</VRL_UG_PLUGIN_API_NAME>*/;
    public final static String PLUGIN_VERSION = /*<VRL_UG_PLUGIN_VERSION>*/"0.2.1"/*</VRL_UG_PLUGIN_VERSION>*/;
    public final static String PLUGIN_VERSION_API = /*<VRL_UG_PLUGIN_API_VERSION>*/"0.2.1"/*</VRL_UG_PLUGIN_API_VERSION>*/;

    public static final PluginIdentifier PLUGIN_IDENTIFIER
            = new PluginIdentifier(PLUGIN_NAME, PLUGIN_VERSION);
    public static final PluginIdentifier API_PLUGIN_IDENTIFIER
            = new PluginIdentifier(PLUGIN_NAME_API, PLUGIN_VERSION_API);

    public final static String JAR_PATH_KEY = "jarPath";
    public final static String PATH_TO_LOCAL_SERVER_FOLDER_KEY = "localServerFolder";
    public final static String REMOTETYPE_KEY = "remoteType";
    public final static String SERVER_SUFFIX = "-server";

    public final static String DETAILED_EXCEPTION_KEY = "detailedException";
}
