/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.VersionInfo;
import eu.mihosoft.vrl.system.PluginAPI;
import eu.mihosoft.vrl.system.PluginConfigurator;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.PluginDependency;
import eu.mihosoft.vrl.system.PluginIdentifier;
import eu.mihosoft.vrl.system.VPluginAPI;
import eu.mihosoft.vrl.visual.VDialog;
import java.awt.image.BufferedImage;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator implements PluginConfigurator {

    public void register(PluginAPI api) {
        if (api instanceof VPluginAPI) {
//            VPluginAPI vApi = (VPluginAPI) api;
            VisualCanvas vCanvas = (VisualCanvas) api.getCanvas();
            UG.getInstance().setMainCanvas(vCanvas);
//
//            for (Class<?> cls : UG.getNativeClasses()) {
//                vApi.addComponent(cls);
//            }
//
//            vApi.addComponent(ReleaseInstances.class);
//
//            vApi.addTypeRepresentation(new UserDataType());
//            vApi.addTypeRepresentation(new BoundaryUserDataType());
//
            // request restart
            if (UG.getInstance().isRecompiled()) {
                
//                System.err.println("Recompiled");
                
                VDialog.showMessageDialog(vCanvas, "Restart neccessary:",
                        " UG-API had to be recompiled."
                        + " VRL-Studio will be closed now."
                        + " Restart it to use the new API.");
                
                System.exit(0);
            }
        }
    }

    public void unregister(PluginAPI api) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDescription() {
        return UG.getInstance().getDescription()
                + "<br><br><b>Authors:</b><br><br>"
                + UG.getInstance().getAuthors().replace("\n", "<br>");
    }

    public BufferedImage getIcon() {
        return null;
    }

    public void init() {

        // initialize ug instance
        UG.getInstance();
    }

    public PluginIdentifier getIdentifier() {
        return Constants.PLUGIN_IDENTIFIER;
    }

    public PluginDependency[] getDependencies() {
        PluginDependency[] result = {};
        return result;
    }
}
