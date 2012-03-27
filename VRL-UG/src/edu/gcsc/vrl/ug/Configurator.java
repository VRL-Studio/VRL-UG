/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.IOUtil;
import eu.mihosoft.vrl.io.VJarUtil;
import eu.mihosoft.vrl.io.VPropertyFolderManager;
import eu.mihosoft.vrl.io.VersionInfo;
import eu.mihosoft.vrl.system.PluginAPI;
import eu.mihosoft.vrl.system.PluginConfigurator;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.*;
import eu.mihosoft.vrl.visual.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator extends VPluginConfigurator {

    private File templateProjectSrc;

    public Configurator() {

        setIdentifier(Constants.PLUGIN_IDENTIFIER);

        // ug is only allowed to load the native ug lib if api plugin could
        // not be found. in this case this plugin will generate it.
        setLoadNativeLibraries(false);

        exportPackage("edu.gcsc.vrl.ug");
    }

    @Override
    public void register(PluginAPI api) {
        if (api instanceof VPluginAPI) {
            VisualCanvas vCanvas = (VisualCanvas) api.getCanvas();

            // request restart
            if (UG.getInstance().isRecompiled()) {

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

    public void init(InitPluginAPI iApi) {

        templateProjectSrc = new File(iApi.getResourceFolder(), "ug-template01.vrlp");

        if (!templateProjectSrc.exists()) {
            InputStream in = Configurator.class.getResourceAsStream(
                    "/edu/gcsc/vrl/ug/resources/ug-template01.vrlp");
            try {
                IOUtil.saveStreamToFile(in, templateProjectSrc);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Configurator.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Configurator.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }

        iApi.addProjectTemplate(new ProjectTemplate() {

            public String getName() {
                return "UG - Project";
            }

            public File getSource() {
                return templateProjectSrc;
            }

            public String getDescription() {
                return "Basic UG Project";
            }

            public BufferedImage getIcon() {
                return null;
            }
        });

        // define native lib location
        UG.setNativeLibFolder(getNativeLibFolder());

        // initialize ug instance
        UG.getInstance();

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
