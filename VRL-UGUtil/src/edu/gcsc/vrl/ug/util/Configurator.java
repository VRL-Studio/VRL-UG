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
import eu.mihosoft.vrl.system.VPluginAPI;
import java.awt.image.BufferedImage;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator implements PluginConfigurator {

    @Override
    public void register(PluginAPI api) {
        if (api instanceof VPluginAPI) {
            VPluginAPI vapi = (VPluginAPI) api;

            vapi.addComponent(UGUtil.class);
            vapi.addComponent(NumberArrayToJFreeChart.class);
            vapi.addComponent(ConvergencePlotter.class);
            
            vapi.addTypeRepresentation(new NumberArrayArrayType());
        }
    }

    @Override
    public void unregister(PluginAPI api) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDescription() {
        return "This plugin replaces "
                + " <b>ug_util.lua</b> on the Java Platform.";
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
