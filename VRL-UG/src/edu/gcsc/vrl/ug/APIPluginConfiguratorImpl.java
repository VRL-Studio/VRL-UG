/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.PluginAPI;
import eu.mihosoft.vrl.system.PluginConfigurator;
import eu.mihosoft.vrl.system.PluginDependency;
import eu.mihosoft.vrl.system.PluginIdentifier;
import eu.mihosoft.vrl.system.VPluginAPI;
import eu.mihosoft.vrl.system.VPluginConfigurator;
import eu.mihosoft.vrl.visual.VDialog;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    }
    
    

    public void register(PluginAPI api) {
        if (api instanceof VPluginAPI) {
            VPluginAPI vApi = (VPluginAPI) api;
            VisualCanvas vCanvas = (VisualCanvas) api.getCanvas();
//            UG.getInstance().setMainCanvas(vCanvas);
            
            Class<?> apiCls = null;
            
            try {
                apiCls = this.getClass().getClassLoader()
               .loadClass("edu.gcsc.vrl.ug.UGAPI");

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(APIPluginConfiguratorImpl.
                        class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (apiCls == null) {
                System.err.println("UG-API class not found!");
                return;
            }
            
            

            for (Class<?> cls : UG.getAPiClasses(apiCls)) {
                vApi.addComponent(cls);
            }

            vApi.addComponent(ReleaseInstances.class);

            vApi.addTypeRepresentation(new UserDataType());
            vApi.addTypeRepresentation(new BoundaryUserDataType());
            
//
//            // request restart
//            if (UG.getInstance().isRecompiled()) {
//
////                System.err.println("Recompiled");
//
//                VDialog.showMessageDialog(vCanvas, "Restart neccessary:",
//                        " UG-API had to be recompiled."
//                        + " VRL-Studio will be closed now."
//                        + " Restart it to use the new API.");
//
//                System.exit(0);
//            }
        }
    }

    public void unregister(PluginAPI api) {
        //
    }  

    public void init() {
        UG.connectToNativeUG(isLoadNativeLibraries());
    }
}