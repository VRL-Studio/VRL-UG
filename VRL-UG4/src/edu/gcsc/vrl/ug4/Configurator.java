/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.io.AbstractVersionInfo;
import eu.mihosoft.vrl.io.PluginAPI;
import eu.mihosoft.vrl.io.PluginConfigurator;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import java.awt.image.BufferedImage;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator implements PluginConfigurator{
    private static UG4 ug4;

    public void register(PluginAPI api) {
        if (api.getCanvas()instanceof VisualCanvas) {
            VisualCanvas vCanvas = (VisualCanvas) api.getCanvas();
            UG4.getUG4();
            vCanvas.addClasses(UG4.getNativeClasses());
        }
    }

    public void unregister(PluginAPI api) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDescription() {
        return "UG4 Plugin";
    }

    public BufferedImage getIcon() {
        return null;
    }

    public AbstractVersionInfo getVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void init() {
        UG4.getUG4();
    }

}
