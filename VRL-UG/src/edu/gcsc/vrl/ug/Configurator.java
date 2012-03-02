/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.VArgUtil;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.*;
import eu.mihosoft.vrl.visual.VDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator extends VPluginConfigurator {

    private static String SERVER_JAR_PATH_KEY = "serverJarPath";
    private static String REMOTETYP_KEY = "rpc";
    
    private static boolean serverConfiguration = false;
    private static PluginConfiguration pluginConfiguration = null;
    
    
    /**
     * @return the serverJarPath
     */
    public static String getServerJarPath() {
        String serverJarPath = eu.mihosoft.vrl.system.Constants.PLUGIN_DIR + "/VRL-UG.jar";
        
        System.out.println("serverJarPath = "+ serverJarPath);
        
        return serverJarPath;
                
    }

    /**
     * @return the pluginConfiguration
     */
    public static PluginConfiguration getPluginConfiguration() {
        if(pluginConfiguration==null){
            System.err.println("ERROR Configurator: pluginConfiguration == null");
        }
        
        return pluginConfiguration;
    }

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

            VPluginAPI vApi = (VPluginAPI) api;

            //TEST: component for starting an UG on an other JVM
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

    public void init(InitPluginAPI iApi) {
        System.out.println(" ****CONFIGURATOR.init( iAPI)");

        setConfigurationEntries();

        // define native lib location
        UG.setNativeLibFolder(getNativeLibFolder());

        String option = VArgUtil.getArg(VRL.getCommandLineOptions(), 
                "-"+Configurator.REMOTETYP_KEY);
        
        System.out.println("VRL.getCommandLineOptions("+
                Configurator.REMOTETYP_KEY+") = "+ option);

        if (!option.toLowerCase().equals("server")) {

            if(getPluginConfiguration()==null){
            pluginConfiguration  = iApi.getConfiguration();
            }
//         pConf.setProperty("-rpc", "client");

            option = pluginConfiguration.getProperty(Configurator.REMOTETYP_KEY);
            System.out.println(" ****CONFIGURATOR.init( iAPI) OPTION: -"
                    +Configurator.REMOTETYP_KEY+" = "  + option);

        }

        // initialize ug instance
        UG.getInstance(option);

//        DID NOT WORK
//        //setting boolean for checking and setting serverConfigurationProperties
//        //this should be only executed in server JVM
//        if (option.toLowerCase().equals("server")) {
//            setServerConfiguration(true);
//        }


        if (UG.isLibloaded()) {
            setDescription(UG.getInstance().getDescription()
                    + "<br><br><b>Authors:</b><br><br>"
                    + UG.getInstance().getAuthors().replace("\n", "<br>"));
        }

        
        setPreferencePane(new PreferencePane() {

            public String getTitle() {
                return "VRL-UG Preferences";
            }

            public JComponent getInterface() {
                JPanel p = new JPanel();
                Box outerBox =Box.createVerticalBox();
                p.add(outerBox);
                
                outerBox.add(new JLabel("VRL-UG Preferences"));
                
                //REMOTETYPE
                
                //PATH TO VRL-UG JAR
                final JTextField path = new JTextField(getServerJarPath());
                path.setEditable(true);
                
                outerBox.add(path);
                
                
                //BUTTONS
                Box innerBox =Box.createHorizontalBox();
                outerBox.add(innerBox);
                
                JButton save = new JButton("Save");
                save.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        
                        // TODO hole die Infos von JTextField und ComboBox und
                        // speichere die werte in PluginConfiguration-Datei
                        // ...
                        
                        getPluginConfiguration().setProperty(
                                Configurator.REMOTETYP_KEY, 
                                path.getText());
                        
                        getPluginConfiguration().save();
                    }
                });
                
                JButton cancel = new JButton("Cancel");
                //... update of vrl needed

                
                innerBox.add(Box.createHorizontalGlue());
                innerBox.add(save);
                innerBox.add(Box.createHorizontalGlue());
                innerBox.add(cancel);
                innerBox.add(Box.createHorizontalGlue());
                
                return p;
            }

            public String getKeywords() {
                return "VRL-UG, Preferences";
            }
        });
    }

    @Override
    public String getDescription() {
        return UG.getInstance().getDescriptionFromApi();
    }

    /**
     *
     * @param server
     */
    private void setConfigurationEntries() {


        String option = VArgUtil.getArg(VRL.getCommandLineOptions(), "-"
                +Configurator.REMOTETYP_KEY);

        if (option.toLowerCase().equals("server")) {
            setServerConfiguration(true);
        }

//        System.out.println("Configurator.setConfigurationEntries()");
//        System.out.println("isServerConfiguration() = " + isServerConfiguration());

//        InitPluginAPI iApi = getInitAPI();
        
//         if(getPluginConfiguration()==null){
//            pluginConfiguration  = iApi.getConfiguration();
//            }
         
        if (isServerConfiguration()) {

            if (getPluginConfiguration().getProperty(
                    Configurator.REMOTETYP_KEY) == null) {
                
                getPluginConfiguration().setProperty(
                        Configurator.REMOTETYP_KEY, "server");
            }

            if (getPluginConfiguration().getProperty(
                    Configurator.SERVER_JAR_PATH_KEY) == null) {
                
                getPluginConfiguration().setProperty(
                        Configurator.SERVER_JAR_PATH_KEY, getServerJarPath());
            }

        } 
//        else {
//            if (pConf.getProperty("rpc") == null) {
//                
//                pConf.setProperty("rpc", "client");
//            }
//
//        }
        getPluginConfiguration().save();
    }

    /**
     * @return the serverConfiguration
     */
    public static boolean isServerConfiguration() {
        return serverConfiguration;
    }

    /**
     * @param serverConfiguration the serverConfiguration to set
     */
    public static void setServerConfiguration(boolean serverConfiguration) {
        Configurator.serverConfiguration = serverConfiguration;
    }
}
