/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.ConfigurationFile;
import eu.mihosoft.vrl.io.VArgUtil;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.*;
import eu.mihosoft.vrl.visual.VDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator extends VPluginConfigurator {

    private static boolean serverConfiguration = false;
    private static ConfigurationFile pluginConfiguration = null;

    /**
     * @return the serverJarPath
     */
    public static String getJarPath() {
        String jarPath = eu.mihosoft.vrl.system.Constants.PLUGIN_DIR + "/VRL-UG.jar";

        System.out.println("jarPath = " + jarPath);

        return jarPath;

    }

    /**
     * @return the pluginConfiguration
     */
    public static ConfigurationFile getConfigurationFile() {
        if (pluginConfiguration == null) {
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

    public void init(final InitPluginAPI iApi) {
        System.out.println(" ****CONFIGURATOR.init( iAPI)");

        //set pluginConfiguration if not done already
        //is needed for interaction with config file
        if (getConfigurationFile() == null) {
            pluginConfiguration = iApi.getConfiguration();
        }

        setConfigurationEntries();

        // define native lib location
        UG.setNativeLibFolder(getNativeLibFolder());


        // initialize ug instance with remotetype ->option
        UG.getInstance(checkRemoteTypeOption());


        if (UG.isLibloaded()) {
            setDescription(UG.getInstance().getDescription()
                    + "<br><br><b>Authors:</b><br><br>"
                    + UG.getInstance().getAuthors().replace("\n", "<br>"));
        }


        setPreferencePane(new PreferencePane() {

            private PreferencePaneControl control;

            @Override
            public String getTitle() {
                return "VRL-UG Preferences";
            }

            @Override
            public JComponent getInterface() {
                JPanel p = new JPanel();
                Box outerBox = Box.createVerticalBox();
                p.add(outerBox);

                outerBox.add(new JLabel("VRL-UG Preferences"));

                //REMOTETYPE
                Vector<RemoteType> remoteTypVec = new Vector<RemoteType>();
                remoteTypVec.add(RemoteType.NONE);
                remoteTypVec.add(RemoteType.CLIENT);
//                remoteTypVec.add(RemoteType.SERVER);

                final JComboBox remoteTypeChoise = new JComboBox(remoteTypVec);
                remoteTypeChoise.setEnabled(true);

                //set in combobox the current remotetyp as selected one
                String currentRTstr = getConfigurationFile().getProperty(
                        Constants.REMOTETYPE_KEY);
                if (currentRTstr != null) {

                    if (currentRTstr.toLowerCase().
                            equals(
                            RemoteType.NONE.toString().toLowerCase())) {

                        remoteTypeChoise.setSelectedItem(RemoteType.NONE);
                        System.out.println("IF ComboBox RemoteType.NONE");
                    }

                    if (currentRTstr.toLowerCase().
                            equals(
                            RemoteType.CLIENT.toString().toLowerCase())) {

                        remoteTypeChoise.setSelectedItem(RemoteType.CLIENT);
                        System.out.println("IF ComboBox RemoteType.CLIENT");
                    }


//                    if (currentRTstr.toLowerCase().
//                            equals(
//                            RemoteType.SERVER.toString().toLowerCase())) {
//                    remoteTypChoise.setSelectedItem(RemoteType.SERVER);
//                }
                } else //if no remotetype is set use default type NONE
                {
                    remoteTypeChoise.setSelectedItem(RemoteType.NONE);
                    System.out.println("ELSE ComboBox RemoteType.NONE");
                }

                outerBox.add(remoteTypeChoise);

//                //PATH TO VRL-UG JAR
//                final JTextField path = new JTextField(getJarPath());
//                path.setEditable(true);
//                path.setEnabled(true);

//                outerBox.add(path);


                //BUTTONS
                Box innerBox = Box.createHorizontalBox();
                outerBox.add(innerBox);

                JButton save = new JButton("Save");
                save.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        // get info of ComboBox 
                        RemoteType rt = (RemoteType) remoteTypeChoise.getSelectedItem();

                        getConfigurationFile().setProperty(
                                Constants.REMOTETYPE_KEY,
                                rt.toString());


//                        //get info of JTextField and set in config file
//                        getConfigurationFile().setProperty(
//                                Constants.JAR_PATH_KEY,
//                                path.getText());

                        //save change in config file and close window
                        getConfigurationFile().save();
                        control.close();
                    }
                });

                JButton cancel = new JButton("Cancel");
                cancel.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        control.close();
                    }
                });

                innerBox.add(Box.createHorizontalGlue());
                innerBox.add(save);
                innerBox.add(Box.createHorizontalGlue());
                innerBox.add(cancel);
                innerBox.add(Box.createHorizontalGlue());

                return p;
            }

            @Override
            public String getKeywords() {
                return "VRL-UG, Preferences";
            }

            @Override
            public void setControl(PreferencePaneControl ctrl) {
                this.control = ctrl;
            }
        });
    }

    @Override
    public String getDescription() {
        return UG.getInstance().getDescriptionFromApi();
    }

    /**
     * Here are the entries of the configuration file checked and if not
     * available set.
     *
     */
    private void setConfigurationEntries() {

//        System.out.println("Configurator.setConfigurationEntries()");
//        System.out.println("isServerConfiguration() = " + isServerConfiguration());

//        if (isServerConfiguration()) {

//        boolean isNull = getConfigurationFile().getProperty(Constants.REMOTETYPE_KEY) == null;
//        boolean isNONE = false;
//        
//        if(!isNull){
//         isNONE = getConfigurationFile().getProperty(Constants.REMOTETYPE_KEY).
//                 equals(RemoteType.NONE);
//        }
//        
//        if (isNull || isNONE) {

        if (isServerConfiguration() && (getConfigurationFile().getProperty(
                Constants.REMOTETYPE_KEY) == null)) {

            getConfigurationFile().setProperty(
                    Constants.REMOTETYPE_KEY,
                    RemoteType.SERVER.toString());
        }

////            getConfigurationFile().setProperty(
////                    Constants.REMOTETYPE_KEY,
////                    RemoteType.NONE.toString());
//        }

        if (getConfigurationFile().getProperty(Constants.JAR_PATH_KEY) == null) {

            getConfigurationFile().setProperty(
                    Constants.JAR_PATH_KEY, getJarPath());
        }

        getConfigurationFile().save();

//        }
    }

    /**
     * Checks the commandline and config file for remoteType and preferes the
     * value of the config file.
     *
     * @return the remoteType that should be used
     */
    private static String checkRemoteTypeOption() {
        System.out.println("Configurator.checkRemoteTypeOption()");

        String tmp = null;
        String option = null;

        //check if there is an information on commandline
        //needed at the moment for the sever
        tmp = VArgUtil.getArg(VRL.getCommandLineOptions(),
                "-" + Constants.REMOTETYPE_KEY);

        System.out.println("VRL.getCommandLineOptions("
                + Constants.REMOTETYPE_KEY + ") = " + tmp);

        if (tmp != null) {
            option = tmp;
        }

//        if (!option.toLowerCase().
//                equals(
//                RemoteType.SERVER.toString().toLowerCase())) {


        //check if there is an information in config file
        //this should be the prefered way of getting informations
        tmp = pluginConfiguration.getProperty(Constants.REMOTETYPE_KEY);

        System.out.println(" **** checkRemoteTypeOption( iAPI) OPTION: "
                + Constants.REMOTETYPE_KEY + " = " + tmp);

//        }

        if (tmp != null) {
            option = tmp;
        }

        System.out.println(" # # # after checking commandline and property file");
        System.out.println(" # # # REMOTETYPE option = "+ option);
        
        
//        //setting boolean for checking and setting serverConfigurationProperties
//        //this should be only executed in server JVM
        if ((option != null)
                && (option.toLowerCase().
                equals(
                RemoteType.SERVER.toString().toLowerCase()))) {
            System.out.println(" # # # if option!=null && SERVER.toLowerCase()");

            setServerConfiguration(true);
        }

        return option;
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

        System.out.println("configurator.setServerConfiguration(" + serverConfiguration + ")");
        Configurator.serverConfiguration = serverConfiguration;
    }
}
