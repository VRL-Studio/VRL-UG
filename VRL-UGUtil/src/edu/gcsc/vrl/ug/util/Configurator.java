/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug.util;


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

    @Override
    public void register(PluginAPI api) {
        if (api.getCanvas() instanceof VisualCanvas) {
            VisualCanvas vCanvas = (VisualCanvas) api.getCanvas();

            vCanvas.addClass(UGUtil.class);
        }
    }

    @Override
    public void unregister(PluginAPI api) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDescription() {
        return "UG4-Util Plugin which provides"
                + "the functionality of ug_util.lua.";
    }

    @Override
    public BufferedImage getIcon() {
        return null;
    }

    @Override
    public void init() {
        //
    }

    @Override
    public PluginIdentifier getIdentifier() {
        return new PluginIdentifier("UG4-Util", new VersionInfo("0.1"));
    }

    @Override
    public PluginDependency[] getDependencies() {
        PluginDependency[] result = {new PluginDependency("UG4", "0.1", "0.1")};
        return result;
    }
}
