/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.VArgUtil;
import eu.mihosoft.vrl.io.VPropertyFolderManager;
import eu.mihosoft.vrl.io.VersionInfo;
import eu.mihosoft.vrl.system.PluginAPI;
import eu.mihosoft.vrl.system.PluginConfigurator;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.PluginDependency;
import eu.mihosoft.vrl.system.PluginIdentifier;
import eu.mihosoft.vrl.system.VPluginAPI;
import eu.mihosoft.vrl.system.VPluginConfigurator;
import eu.mihosoft.vrl.system.VRL;
import eu.mihosoft.vrl.visual.VDialog;
import eu.mihosoft.vrl.visual.VSwingUtil;
import java.awt.image.BufferedImage;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator extends VPluginConfigurator {

    public Configurator() {

        setIdentifier(Constants.PLUGIN_IDENTIFIER);

        // ug is only allowed to load the native ug lib if api plugin could
        // not be found. in this case this plugin will generate it.
        setLoadNativeLibraries(false);
        
        exportPackage("edu.gcsc.vrl.ug");
    }

    public void register(PluginAPI api) {
        if (api instanceof VPluginAPI) {
            VisualCanvas vCanvas = (VisualCanvas) api.getCanvas();
//            UG.getInstance().setMainCanvas(vCanvas);
            
            VPluginAPI vApi = (VPluginAPI) api;
            
             //TEST: component for starting an UG on an other JVM
//            vApi.addComponent(ServerManager.class);
            vApi.addComponent(JVMmanager.class);
//
            // request restart
            if (UG.getInstance().isRecompiled()) {

//                System.err.println("Recompiled");
                VDialog.showMessageDialog(vCanvas, "Restart neccessary:",
                        " UG-API had to be recompiled."
                        + " VRL-Studio will be closed now."
                        + " Restart it to use the new API.");
                
                VRL.exit(0);
            }
        }
    }

    public void unregister(PluginAPI api) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void init() {

        // define native lib location
        UG.setNativeLibFolder(getNativeLibFolder());
        
        String option = VArgUtil.getArg(VRL.getCommandLineOptions(),"-rpc");
        
        System.out.println(this.getClass().getName() +
                " **** OPTION: -rpc = " + option);
        
        
        // initialize ug instance
        UG.getInstance(option);

        if (UG.isLibloaded()) {
            setDescription(UG.getInstance().getDescription()
                    + "<br><br><b>Authors:</b><br><br>"
                    + UG.getInstance().getAuthors().replace("\n", "<br>"));
        }
    }
    
  
    @Override
    public String getDescription() {
        return UG.getInstance().getDescriptionFromApi();
    }
}
