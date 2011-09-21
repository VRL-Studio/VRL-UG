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
import eu.mihosoft.vrl.system.VPluginConfigurator;
import java.awt.image.BufferedImage;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator extends VPluginConfigurator {

    public Configurator() {

        setIdentifier(new PluginIdentifier("UG4-Util", new VersionInfo("0.1")));

        addDependency(new PluginDependency("UG4", "0.1", "0.1"));
        addDependency(new PluginDependency("UG4-API", "0.1", "0.1"));

        addDependency(new PluginDependency("VRL-JFreeChart", "0.1", "0.1"));

        setDescription("This plugin replaces "
                + " <b>ug_util.lua</b> on the Java Platform.");

    }

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
        //
    }

    @Override
    public void init() {
        //
    }
}
