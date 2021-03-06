//package edu.gcsc.vrl.ug;

import edu.gcsc.vrl.ug.types.CondUserDataType;
import edu.gcsc.vrl.ug.types.RemoteFileType;
import edu.gcsc.vrl.ug.types.RemoteLoadFileStringType;
import edu.gcsc.vrl.ug.types.RemoteLoadFileType;
import edu.gcsc.vrl.ug.types.RemoteSaveFileStringType;
import edu.gcsc.vrl.ug.types.RemoteSaveFileType;
import edu.gcsc.vrl.ug.types.UserDataType;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.PluginAPI;
import eu.mihosoft.vrl.system.PluginDependency;
import eu.mihosoft.vrl.system.PluginIdentifier;
import eu.mihosoft.vrl.system.VPluginAPI;
import eu.mihosoft.vrl.system.InitPluginAPI;
import eu.mihosoft.vrl.system.VPluginConfigurator;
import eu.mihosoft.vrl.visual.VFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@ComponentInfo(ignore = true)
public class APIPluginConfiguratorImpl extends VPluginConfigurator {

    public final static String PLUGIN_NAME = /*<VRL_UG_PLUGIN_NAME>*/"VRL-UG"/*</VRL_UG_PLUGIN_NAME>*/;
    public final static String PLUGIN_NAME_API = /*<VRL_UG_PLUGIN_API_NAME>*/"VRL-UG-API"/*</VRL_UG_PLUGIN_API_NAME>*/;
    public final static String PLUGIN_VERSION = /*<VRL_UG_PLUGIN_VERSION>*/"0.2.2"/*</VRL_UG_PLUGIN_VERSION>*/;
    public final static String PLUGIN_VERSION_API = /*<VRL_UG_PLUGIN_API_VERSION>*/"0.2.2"/*</VRL_UG_PLUGIN_API_VERSION>*/;



    public final static String REMOTE_TYPE = "NONE";//"CLIENT";

    public static final PluginIdentifier API_PLUGIN_IDENTIFIER =
            new PluginIdentifier(PLUGIN_NAME_API, PLUGIN_VERSION_API);
    public static final PluginIdentifier PLUGIN_IDENTIFIER =
            new PluginIdentifier(PLUGIN_NAME, PLUGIN_VERSION);


    public APIPluginConfiguratorImpl() {

        setIdentifier(API_PLUGIN_IDENTIFIER);

        addDependency(new PluginDependency(
                PLUGIN_IDENTIFIER.getName(),
                PLUGIN_IDENTIFIER.getVersion(),
                PLUGIN_IDENTIFIER.getVersion()));

        setDescription(PLUGIN_NAME_API);

        // api classes must be fully available for all plugins that depend
        // on this api
        disableAccessControl(true);
    }

    public void register(PluginAPI api) {

        if (api instanceof VPluginAPI) {

            VPluginAPI vApi = (VPluginAPI) api;
            VisualCanvas vCanvas = (VisualCanvas) api.getCanvas();

// TODO check if there need / can always stand CLIENT
            UG.getInstance(REMOTE_TYPE).setMainCanvas(vCanvas);

            Class<?> apiCls = null;
            boolean matchingAPI = false;

            try {
                apiCls = this.getClass().getClassLoader().
                        loadClass("edu.gcsc.vrl.ug.api.UGAPI");

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(APIPluginConfiguratorImpl.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            if (apiCls == null) {
                System.err.println(">> " + PLUGIN_NAME + " : "
                        + PLUGIN_NAME_API + " class not found!");
            }

            try {

                String apiSvn = (String) apiCls.getMethod(
                        "getSvnRevision").
                        invoke(apiCls);

                String apiDate = (String) apiCls.getMethod(
                        "getCompileDate").
                        invoke(apiCls);

// TODO check if there need / can always stand CLIENT
                boolean revisionsAreEqual = apiSvn.equals(
                        UG.getInstance(REMOTE_TYPE).getSvnRevision());

// TODO check if there need / can always stand CLIENT
                boolean datesAreEqual = apiDate.equals(
                        UG.getInstance(REMOTE_TYPE).getCompileDate());

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

            /*
             * if (!matchingAPI) { VDialog.showMessageDialog(vCanvas, "UG-API
             * not compatible to current UG version:", " UG-API has to be
             * recompiled." + " To do so, delete the file VRL-UG-API.jar in the"
             * + " Plugin directory and restart VRL-Studio.");
             *
             * VRL.exit(0);
            }
             */


            for (Class<?> cls : UG.getAPiClasses(apiCls)) {
                vApi.addComponent(cls);
            }

            vApi.addComponent(ReleaseInstances.class);

            vApi.addTypeRepresentation( UserDataType.class);
            vApi.addTypeRepresentation( CondUserDataType.class);

            vApi.addTypeRepresentation( RemoteFileType.class);
            vApi.addTypeRepresentation( RemoteLoadFileType.class);
            vApi.addTypeRepresentation( RemoteSaveFileType.class);
            vApi.addTypeRepresentation( RemoteLoadFileStringType.class);
            vApi.addTypeRepresentation( RemoteSaveFileStringType.class);

            vApi.addComponentSearchFilter(new HideComponentFilter());
        }
    }

    public void unregister(PluginAPI api) {
        //
    }

    public void init(InitPluginAPI iApi) {
        // UG.connectToNativeUG(isLoadNativeLibraries());
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