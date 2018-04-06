/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2012–2018 Goethe Universität Frankfurt am Main, Germany
 * 
 * This file is part of Visual Reflection Library (VRL).
 *
 * VRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/VRL/src/eu/mihosoft/vrl/resources/license/lgplv3.txt
 *
 * VRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of VRL includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of VRL. Neither the VRL Canvas attribution icon nor any
 * copyright statement/attribution may be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do three things regarding copyright
 * notice and author attribution.
 *
 * First, the following text must be displayed on the Canvas:
 * "based on VRL source code". In this case the VRL canvas icon must be removed.
 * 
 * Second, the copyright notice must remain. It must be reproduced in any
 * program that uses VRL.
 *
 * Third, add an additional notice, stating that you modified VRL. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "VRL source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * M. Hoffer, C.Poliwoda, G.Wittum. Visual Reflection Library -
 * A Framework for Declarative GUI Programming on the Java Platform.
 * Computing and Visualization in Science, 2011, in press.
 */
////package edu.gcsc.vrl.ug;
//
//import edu.gcsc.vrl.ug.types.CondUserDataType;
//import edu.gcsc.vrl.ug.types.RemoteFileType;
//import edu.gcsc.vrl.ug.types.RemoteLoadFileStringType;
//import edu.gcsc.vrl.ug.types.RemoteLoadFileType;
//import edu.gcsc.vrl.ug.types.RemoteSaveFileStringType;
//import edu.gcsc.vrl.ug.types.RemoteSaveFileType;
//import edu.gcsc.vrl.ug.types.UserDataType;
//import eu.mihosoft.vrl.annotation.ComponentInfo;
//import eu.mihosoft.vrl.reflection.VisualCanvas;
//import eu.mihosoft.vrl.system.PluginAPI;
//import eu.mihosoft.vrl.system.PluginDependency;
//import eu.mihosoft.vrl.system.PluginIdentifier;
//import eu.mihosoft.vrl.system.VPluginAPI;
//import eu.mihosoft.vrl.system.InitPluginAPI;
//import eu.mihosoft.vrl.system.VPluginConfigurator;
//import eu.mihosoft.vrl.visual.VFilter;
//import java.lang.reflect.InvocationTargetException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.tree.DefaultMutableTreeNode;
//
///**
// *
// * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
// * @author Michael Hoffer <info@michaelhoffer.de>
// */
//@ComponentInfo(ignore = true)
//public class APIPluginConfiguratorImpl extends VPluginConfigurator {
//
//    
//    public final static String PLUGIN_NAME = "VRL-UG";
//    public final static String PLUGIN_NAME_API = "VRL-UG-API";
//    public final static String REMOTE_TYPE = "NONE";//"CLIENT";
//    
//    public static final PluginIdentifier API_PLUGIN_IDENTIFIER =
//            new PluginIdentifier(PLUGIN_NAME_API, "0.2.1");//"0.1.1.x" did not work
//    public static final PluginIdentifier PLUGIN_IDENTIFIER =
//            new PluginIdentifier(PLUGIN_NAME, "0.2.1");//"0.1.1.x" did not work
//
//    public APIPluginConfiguratorImpl() {
//
//        setIdentifier(API_PLUGIN_IDENTIFIER);
//
//        addDependency(new PluginDependency(
//                PLUGIN_IDENTIFIER.getName(),
//                PLUGIN_IDENTIFIER.getVersion(),
//                PLUGIN_IDENTIFIER.getVersion()));
//
//        setDescription(PLUGIN_NAME_API);
//
//        // api classes must be fully available for all plugins that depend
//        // on this api
//        disableAccessControl(true);
//    }
//
//    public void register(PluginAPI api) {
//
//        if (api instanceof VPluginAPI) {
//
//            VPluginAPI vApi = (VPluginAPI) api;
//            VisualCanvas vCanvas = (VisualCanvas) api.getCanvas();
//
//// TODO check if there need / can always stand CLIENT
//            UG.getInstance(REMOTE_TYPE).setMainCanvas(vCanvas);
//
//            Class<?> apiCls = null;
//            boolean matchingAPI = false;
//
//            try {
//                apiCls = this.getClass().getClassLoader().
//                        loadClass("edu.gcsc.vrl.ug.api.UGAPI");
//
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
//                        log(Level.SEVERE, null, ex);
//            }
//
//            if (apiCls == null) {
//                System.err.println(">> " + PLUGIN_NAME + " : "
//                        + PLUGIN_NAME_API + " class not found!");
//            }
//
//            try {
//
//                String apiSvn = (String) apiCls.getMethod(
//                        "getSvnRevision").
//                        invoke(apiCls);
//
//                String apiDate = (String) apiCls.getMethod(
//                        "getCompileDate").
//                        invoke(apiCls);
//
//// TODO check if there need / can always stand CLIENT
//                boolean revisionsAreEqual = apiSvn.equals(
//                        UG.getInstance(REMOTE_TYPE).getSvnRevision());
//                
//// TODO check if there need / can always stand CLIENT
//                boolean datesAreEqual = apiDate.equals(
//                        UG.getInstance(REMOTE_TYPE).getCompileDate());
//
//                matchingAPI = revisionsAreEqual && datesAreEqual;
//
//            } catch (IllegalAccessException ex) {
//                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
//                        log(Level.SEVERE, null, ex);
//            } catch (IllegalArgumentException ex) {
//                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
//                        log(Level.SEVERE, null, ex);
//            } catch (InvocationTargetException ex) {
//                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
//                        log(Level.SEVERE, null, ex);
//
//            } catch (NoSuchMethodException ex) {
//                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
//                        log(Level.SEVERE, null, ex);
//            } catch (SecurityException ex) {
//                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
//                        log(Level.SEVERE, null, ex);
//            }
//
//            /*
//             * if (!matchingAPI) { VDialog.showMessageDialog(vCanvas, "UG-API
//             * not compatible to current UG version:", " UG-API has to be
//             * recompiled." + " To do so, delete the file VRL-UG-API.jar in the"
//             * + " Plugin directory and restart VRL-Studio.");
//             *
//             * VRL.exit(0);
//            }
//             */
//
//
//            for (Class<?> cls : UG.getAPiClasses(apiCls)) {
//                vApi.addComponent(cls);
//            }
//
//            vApi.addComponent(ReleaseInstances.class);
//
//            vApi.addTypeRepresentation( UserDataType.class);
//            vApi.addTypeRepresentation( CondUserDataType.class);
//            
//            vApi.addTypeRepresentation( RemoteFileType.class);
//            vApi.addTypeRepresentation( RemoteLoadFileType.class);
//            vApi.addTypeRepresentation( RemoteSaveFileType.class);
//            vApi.addTypeRepresentation( RemoteLoadFileStringType.class);
//            vApi.addTypeRepresentation( RemoteSaveFileStringType.class);
//
//            vApi.addComponentSearchFilter(new HideComponentFilter());
//        }
//    }
//
//    public void unregister(PluginAPI api) {
//        //
//    }
//
//    public void init(InitPluginAPI iApi) {
//        // UG.connectToNativeUG(isLoadNativeLibraries());
//    }
//}
//
//class HideComponentFilter implements VFilter {
//
//    public boolean matches(Object o) {
//
//        if (!(o instanceof DefaultMutableTreeNode)) {
//            return false;
//        }
//
//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
//
//        if (node.getUserObject() == null
//                || !(node.getUserObject() instanceof Class<?>)) {
//            return false;
//        }
//
//        Class<?> cls = (Class<?>) node.getUserObject();
//
//
////        boolean ugClass = UGObjectUtil.isUGAPIClass(cls);
//        boolean wrapperClass = UGObjectUtil.isWrapperClass(cls);
//        boolean constClass = UGObjectUtil.isConstClass(cls);
////        boolean groupRoot = UGObjectUtil.isGroupRoot(cls);
//        boolean groupChild = UGObjectUtil.isGroupChild(cls);
//
//        return constClass || wrapperClass || groupChild;
//    }
//
//    public String getName() {
//        return "UG Const Filter";
//    }
//
//    public boolean hideWhenMatching() {
//        return true;
//    }
//}