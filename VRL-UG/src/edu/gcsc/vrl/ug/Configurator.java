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
import java.awt.image.BufferedImage;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator implements PluginConfigurator {

    public void register(PluginAPI api) {
        if (api.getCanvas() instanceof VisualCanvas) {
            VisualCanvas vCanvas = (VisualCanvas) api.getCanvas();
            UG.getInstance().setMainCanvas(vCanvas);
            vCanvas.addClasses(UG.getNativeClasses());

            vCanvas.getTypeFactory().addType(new UserDataType());
            vCanvas.getTypeFactory().addType(new BoundaryUserDataType());
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
        UG.getInstance();
    }

    public PluginIdentifier getIdentifier() {
        return new PluginIdentifier("UG4", new VersionInfo("0.1"));
    }

    public PluginDependency[] getDependencies() {
        PluginDependency[] result = {};
        return result;
    }
}
