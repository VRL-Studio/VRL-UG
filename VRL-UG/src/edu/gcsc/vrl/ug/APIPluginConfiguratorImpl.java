/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.VPropertyFolderManager;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.PluginAPI;
import eu.mihosoft.vrl.system.PluginConfigurator;
import eu.mihosoft.vrl.system.PluginDependency;
import eu.mihosoft.vrl.system.PluginIdentifier;
import eu.mihosoft.vrl.system.VPluginAPI;
import eu.mihosoft.vrl.system.VPluginConfigurator;
import eu.mihosoft.vrl.system.VRL;
import eu.mihosoft.vrl.visual.VDialog;
import eu.mihosoft.vrl.visual.VFilter;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public abstract class APIPluginConfiguratorImpl extends VPluginConfigurator {

    public APIPluginConfiguratorImpl() {

        setIdentifier(Constants.API_PLUGIN_IDENTIFIER);

        addDependency(new PluginDependency(
                Constants.PLUGIN_IDENTIFIER.getName(),
                Constants.PLUGIN_IDENTIFIER.getVersion(),
                Constants.PLUGIN_IDENTIFIER.getVersion()));

        setDescription("UG-API");

        // api classes must be fully available for all plugins that depend
        // on this api
        disableAccessControl(true);
    }

    public void register(PluginAPI api) {
        if (api instanceof VPluginAPI) {
            VPluginAPI vApi = (VPluginAPI) api;
            VisualCanvas vCanvas = (VisualCanvas) api.getCanvas();
            UG.getInstance().setMainCanvas(vCanvas);

            Class<?> apiCls = null;

            boolean matchingAPI = false;

            try {
                apiCls = this.getClass().getClassLoader().
                        loadClass("edu.gcsc.vrl.ug.UGAPI");

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            if (apiCls == null) {
                System.err.println(">> VRL-UG: UG-API class not found!");
            }

            try {

                String apiSvn = (String) apiCls.getMethod(
                        "getSvnRevision").
                        invoke(apiCls);

                String apiDate = (String) apiCls.getMethod(
                        "getCompileDate").
                        invoke(apiCls);

                boolean revisionsAreEqual =
                        apiSvn.equals(UG.getInstance().getSvnRevision());
                boolean datesAreEqual =
                        apiDate.equals(UG.getInstance().getCompileDate());

                matchingAPI = revisionsAreEqual && datesAreEqual;

            } catch (IllegalAccessException ex) {
                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
                        log(Level.SEVERE, null, ex);

            } catch (NoSuchMethodException ex) {
                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            if (!matchingAPI) {
                VDialog.showMessageDialog(vCanvas,
                        "UG-API not compatible to current UG version:",
                        " UG-API has to be recompiled."
                        + " To do so, delete the file VRL-UG-API.jar in the"
                        + " Plugin directory and restart VRL-Studio.");

                VRL.exit(0);
            }


            for (Class<?> cls : UG.getAPiClasses(apiCls)) {
                vApi.addComponent(cls);
            }

            vApi.addComponent(ReleaseInstances.class);

            vApi.addTypeRepresentation(new UserDataType());
            vApi.addTypeRepresentation(new BoundaryUserDataType());

            vApi.addComponentSearchFilter(new HideComponentFilter());
        }
    }

    public void unregister(PluginAPI api) {
        //
    }

    public void init() {
        UG.connectToNativeUG(isLoadNativeLibraries());
    }
}

class HideComponentFilter implements VFilter {

    public boolean matches(Object o) {

        if (!(o instanceof DefaultMutableTreeNode)) {
            return false;
        }

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;

        if (node.getUserObject() == null
                || !(node.getUserObject() instanceof Class<?>)) {
            return false;
        }

        Class<?> cls = (Class<?>) node.getUserObject();


//        boolean ugClass = UGObjectUtil.isUGAPIClass(cls);
        boolean wrapperClass = UGObjectUtil.isWrapperClass(cls);
        boolean constClass = UGObjectUtil.isConstClass(cls);
//        boolean groupRoot = UGObjectUtil.isGroupRoot(cls);
        boolean groupChild = UGObjectUtil.isGroupChild(cls);

        return constClass || wrapperClass || groupChild;
    }

    public String getName() {
        return "UG Const Filter";
    }

    public boolean hideWhenMatching() {
        return true;
    }
}