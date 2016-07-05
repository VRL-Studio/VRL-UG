/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2012–2016 Goethe Universität Frankfurt am Main, Germany
 * 
 * This file is part of Visual Reflection Library (VRL).
 *
 * VRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/VRL/src/eu/mihosoft/vrl/resources/license/lgplv3.txt
 *
 * VRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of VRL includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of VRL. Neither the VRL Canvas attribution icon nor any
 * copyright statement/attribution may be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do three things regarding copyright
 * notice and author attribution.
 *
 * First, the following text must be displayed on the Canvas:
 * "based on VRL source code". In this case the VRL canvas icon must be removed.
 * 
 * Second, the copyright notice must remain. It must be reproduced in any
 * program that uses VRL.
 *
 * Third, add an additional notice, stating that you modified VRL. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "VRL source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * M. Hoffer, C.Poliwoda, G.Wittum. Visual Reflection Library -
 * A Framework for Declarative GUI Programming on the Java Platform.
 * Computing and Visualization in Science, 2011, in press.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import edu.gcsc.vrl.ug.remote.JVMmanager;
import edu.gcsc.vrl.ug.remote.RemoteService;
import edu.gcsc.vrl.ug.types.RemoteType;
import eu.mihosoft.vrl.io.*;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.*;
import eu.mihosoft.vrl.visual.VDialog;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.apache.commons.io.FileUtils;

/**
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator extends VPluginConfigurator {

    
    private File kineticProjectSrc;
    private String kineticTemplateName = "ug-advect-diff-kinetic.vrlp";
//    private File d3fProjectSrc;
//    private String d3fTemplateName = "ug-d3f.vrlp";
    private File staticProjectSrc;
    private String staticTemplateName = "ug-advect-diff-static.vrlp";
    private static boolean serverConfiguration = false;
    private static ConfigurationFile pluginConfiguration = null;
    private static String jarPath = null;
    private static File serverFolder = null;
    private static File serverUpdateFolder = null;
    private static String serverJar = null;
    private static final VPropertyFolderManager propertyFolderManager =
            new VPropertyFolderManager();
    private final static String UG_LICENSE = "license.txt";

    /**
     * @return the path to the folder where the jar file of this plugin is and
     * adds /VRL-UG.jar
     */
    public static String getVrlUgJarPath() {
        if (jarPath == null) {
            jarPath = getVRLStudioDir().getAbsolutePath() + "/VRL-UG.jar";
        }

//        System.out.println("## Configurator : jarPath = " + jarPath);

        return jarPath;

    }

    /**
     *
     * @return the path to the folder of this plugin
     */
    public static File getVRLStudioDir() {
        File studioDir = VRL.getPropertyFolderManager().getPropertyFolder();//.getAbsolutePath();

//        System.out.println("## Configurator : studioDir = " + studioDir);

        return studioDir;

    }

    /**
     * @return the folder where the local server should be run in and manage its
     * config file and etc.
     */
    public static File getLocalServerFolder() {
        if (serverFolder == null) {
//            serverFolder = new File(getVRLStudioDir().getAbsolutePath() + "-server");
            serverFolder = new File(VRL.getPropertyFolderManager().getPropertyFolder()
                    + Constants.SERVER_SUFFIX);
        }
//        System.out.println("## Configurator : serverFolder = " + serverFolder);

        return serverFolder;

    }

    /**
     * @return the folder where the local server should be run in and manage its
     * config file and etc.
     */
    public static File getLocalServerUpdateFolder() {
        if (serverUpdateFolder == null) {
            serverUpdateFolder = getPropertyFolderManager().getPluginUpdatesFolder();
        }
//        System.out.println("## Configurator : serverUpdateFolder = " + serverUpdateFolder);

        return serverUpdateFolder;

    }

    /**
     * @return the folder where the local server should be run in and manage its
     * config file and etc.
     */
    public static String getLocalServerJar() {
        if (serverJar == null) {
            serverJar = getPropertyFolderManager().
                    getPluginFolder().getAbsoluteFile()
                    + System.getProperty("file.separator")
                    + VJarUtil.getClassLocation(UG.class).getName();
        }
//        System.out.println("## Configurator : serverJar = " + serverJar);

        return serverJar;

    }

    /**
     * This method updates the local server folder and creates one if no one
     * exists.
     */
    public static void updateLocalServerFolder() {
//        System.out.println("Configurator.updateLocalServerFolder() : START");

        File serverFolder = getLocalServerFolder();

//        System.out.println(" serverFolder = " + serverFolder.getAbsolutePath());


        if (!getPropertyFolderManager().isInitialized()) {

            getPropertyFolderManager().init(serverFolder.getName(), true);
        }

        if (!serverFolder.exists()) {
            try {
                // create folders
                getPropertyFolderManager().create();
            } catch (IOException ex) {
                Logger.getLogger(Configurator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }






        try {
            // check which jar to copy and do so to local server update folder

            //
            // VRL-UG.jar - - -
            //
            File jarToCopy = VJarUtil.getClassLocation(UG.class);
//            File jarTarget = new File(getPropertyFolderManager().getPluginUpdatesFolder(),    //PluginUpdate-Folder
            File jarTarget = new File(getPropertyFolderManager().getPluginFolder(), //Plugin-Folder
                    VJarUtil.getClassLocation(UG.class).getName());


//            System.out.println("Configurator: UG.getRemoteType() = " + UG.getRemoteType());
//
//            System.out.println("Configurator: jarToCopy = " + jarToCopy);
//            System.out.println("Configurator: jarTarget = " + jarTarget);

            IOUtil.copyFile(jarToCopy, jarTarget);//copy VRL-UG.jar

            // TODO copy
            // UG4 plugin folder , subfolders and files - - -
            //
            File ug4PluginFolderToCopy = new File(VRL.getPropertyFolderManager().getPluginFolder()
                    + System.getProperty("file.separator")
                    + Constants.PLUGIN_IDENTIFIER.getName());

//            System.out.println(" ug4PluginFolderToCopy = " + ug4PluginFolderToCopy);
//
//            System.out.println(" -- getPropertyFolderManager() = " + getPropertyFolderManager());
//
//            System.out.println(" !! -- getPluginFolder() = " + getPropertyFolderManager().getPluginFolder());

            File ug4PluginFolderTarget = new File(getPropertyFolderManager().getPluginFolder()
                    + System.getProperty("file.separator")
                    + Constants.PLUGIN_IDENTIFIER.getName());

//            System.out.println(" ug4PluginFolderTarget = " + ug4PluginFolderTarget);

            IOUtil.copyDirectory(ug4PluginFolderToCopy, ug4PluginFolderTarget);//copy UG4 folder


            //
            // change config.xml to server configuration
            //

            File serverConfigFile = new File(getPropertyFolderManager().getPluginFolder()
                    + System.getProperty("file.separator")
                    + Constants.PLUGIN_IDENTIFIER.getName()
                    + System.getProperty("file.separator")
                    + PluginDataController.CONFIG
                    + System.getProperty("file.separator")
                    + PluginDataController.CONFIG_FILENAME);

//            System.out.println("serverConfigFile = " + serverConfigFile);

//            System.out.println("serverConfigFile.delete() = " + serverConfigFile.delete());
//            serverConfigFile.delete();

            ConfigurationFile pConfig = IOUtil.newConfigurationFile(serverConfigFile);
            pConfig.setProperty(Constants.REMOTETYPE_KEY, RemoteType.SERVER.toString());
            pConfig.save();


        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configurator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configurator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return the pluginConfiguration
     */
    public static ConfigurationFile getConfigurationFile() {
        // check if it is possible to garanty there is always a config file
        //      or to create one if no one exists -> if init(final InitPluginAPI iApi)
        //      is called before this method there is a config file created
        //  find place where config file is created ! -> VRL:PluginDataController
        //
        if (pluginConfiguration == null) {
            System.err.println("ERROR Configurator: pluginConfiguration == null");
            System.err.println("Calling Configurator.getConfigurationFile() before"
                    + "init(InitPluginAPI iApi) of Plugin is NOT a good idea");
        }

        return pluginConfiguration;
    }

    /**
     * @return the propertyFolderManager
     */
    public static VPropertyFolderManager getPropertyFolderManager() {
        return propertyFolderManager;
    }

    public Configurator() {

        setIdentifier(Constants.PLUGIN_IDENTIFIER);

        addDependency(new PluginDependency("VRL", "0.4.2", "0.4.x"));

        // ug is only allowed to load the native ug lib if api plugin could
        // not be found. in this case this plugin will generate it.
        setLoadNativeLibraries(false);

        exportPackage("edu.gcsc.vrl.ug");

    }

    @Override
    public void register(PluginAPI api) {

        if (api instanceof VPluginAPI) {
            VisualCanvas vCanvas = (VisualCanvas) api.getCanvas();

            VPluginAPI vApi = (VPluginAPI) api;

            //TEST: component for starting an UG on an other JVM
            vApi.addComponent(JVMmanager.class);
            vApi.addComponent(RemoteService.class);
//            // test classes
//            vApi.addComponent(TestParameterArray.class);
//            vApi.addComponent(TestRemoteFile.class);

            // request restart
            if (UG.getInstance().isRecompiled()) {

                VDialog.showMessageDialog(vCanvas, "Restart neccessary:",
                        " UG-API had to be recompiled."
                        + " VRL-Studio will be closed now."
                        + " Restart it to use the new API.");

                VRL.exit(0);
            }
  
            // Attribution:
            // - will be shown if image is defined and plugin is selected in
            //   project
            // - insert custom image
            //   (preferred attribution image size: 140x36px,
            //    you may also change the implementation of UGAttributionDisplay) 
            //   
            Image image = null;

            addAttributionDisplay(vApi, image);
        }
    }

    private void addAttributionDisplay(VPluginAPI vApi, Image image) {
        if (image != null) {
            UGAttributionDisplay ugAttributionDisplay =
                    new UGAttributionDisplay(vApi.getCanvas(),
                    image);
            vApi.getCanvas().add(ugAttributionDisplay);
        } else {
            System.out.println(">> VRL-UG: attribution icon missing!");
        }
    }

    @Override
    public void unregister(PluginAPI api) {
        shutdown();
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void init(final InitPluginAPI iApi) {
//        System.out.println(" ****CONFIGURATOR.init( iAPI) UG.getRemoteType() = "
//                + UG.getRemoteType());

        //set pluginConfiguration first possiblity here to set / get it
        //is needed for interaction with config file
        pluginConfiguration = iApi.getConfiguration();

        setConfigurationEntries();
  

        kineticProjectSrc = installTemplate(kineticTemplateName, iApi);
        
        iApi.addProjectTemplate(new ProjectTemplate() {

            public String getName() {
                return "UG - Advection-Diffusion - Kinetic";
            }

            public File getSource() {
                return kineticProjectSrc;
            }

            public String getDescription() {
                return "Basic UG kinetic Project";
            }

            public BufferedImage getIcon() {
                return null;
            }
        });

        
//        d3fProjectSrc = installTemplate(d3fTemplateName, iApi);
//        
//
//        iApi.addProjectTemplate(new ProjectTemplate() {
//
//            public String getName() {
//                return "UG - Density-Driven-Flow";
//            }
//
//            public File getSource() {
//                return d3fProjectSrc;
//            }
//
//            public String getDescription() {
//                return "UG Density-Driven-Flow Project";
//            }
//
//            public BufferedImage getIcon() {
//                return null;
//            }
//        });

        staticProjectSrc = installTemplate(staticTemplateName, iApi);
        

        iApi.addProjectTemplate(new ProjectTemplate() {

            public String getName() {
                return "UG - Advection-Diffusion - Static";
            }

            public File getSource() {
                return staticProjectSrc;
            }

            public String getDescription() {
                return "Basic UG static Project";
            }

            public BufferedImage getIcon() {
                return null;
            }
        });

        // define native lib location
        UG.setNativeLibFolder(getNativeLibFolder());


        // initialize ug instance with remotetype
        // starting UG the first time leads to run UG with remoteType NONE
        UG.getInstance(checkRemoteTypeOption());



        if (UG.isLibloaded()) {
            setDescription(UG.getInstance().getDescription()
                    + "<br><br><b>Authors:</b><br><br>"
                    + UG.getInstance().getAuthors().replace("\n", "<br>"));
        }

        File copyRightSrc = new File(iApi.getResourceFolder(), UG_LICENSE);
        setCopyrightInfoAsPlainText(FileUtils.readFileToString(copyRightSrc));

        setPreferencePane(new PreferencePane() {

            private javax.swing.JCheckBox showDetailedExceptionBox = new javax.swing.JCheckBox();
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

                showDetailedExceptionBox.setText("<html>Show detailed <b>UG-Exceptions</b></html>");
                showDetailedExceptionBox.setToolTipText("Shows file and line traceback of error propagation");
                showDetailedExceptionBox.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        getConfigurationFile().setProperty(
                                Constants.DETAILED_EXCEPTION_KEY, "" + showDetailedExceptionBox.isSelected());
                        getConfigurationFile().save();

                        UGException.setShowDetailedException(showDetailedExceptionBox.isSelected());
                    }
                });

                if (getConfigurationFile().containsProperty(Constants.DETAILED_EXCEPTION_KEY)) {
                    boolean b = Boolean.parseBoolean(
                            getConfigurationFile().getProperty(Constants.DETAILED_EXCEPTION_KEY));
                    UGException.setShowDetailedException(b);
                    showDetailedExceptionBox.setSelected(b);
                }

                outerBox.add(showDetailedExceptionBox);

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
//                        System.out.println("IF ComboBox RemoteType.NONE");
                    }

                    if (currentRTstr.toLowerCase().
                            equals(
                            RemoteType.CLIENT.toString().toLowerCase())) {

                        remoteTypeChoise.setSelectedItem(RemoteType.CLIENT);
//                        System.out.println("IF ComboBox RemoteType.CLIENT");
                    }


//                    if (currentRTstr.toLowerCase().
//                            equals(
//                            RemoteType.SERVER.toString().toLowerCase())) {
//                    remoteTypChoise.setSelectedItem(RemoteType.SERVER);
//                }
                } else //if no remotetype is set use default type NONE
                {
                    remoteTypeChoise.setSelectedItem(RemoteType.NONE);
//                    System.out.println("ELSE ComboBox RemoteType.NONE");
                }

                outerBox.add(remoteTypeChoise);

//                //PATH TO VRL-UG JAR
//                final JTextField path = new JTextField(getVrlUgJarPath());
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
    public void install(InitPluginAPI iApi) {
        super.install(iApi);
        
//        installTemplate(d3fTemplateName, iApi);
        installTemplate(staticTemplateName, iApi);
        installTemplate(kineticTemplateName, iApi);
        
    }
    
    

    @Override
    public String getDescription() {
        return UG.getInstance().getDescriptionFromApi();
    }

    public void shutdown() {
        JVMmanager.stopLocalServer();
    }

    /**
     * Here are the entries of the configuration file checked and if not
     * available set with default values.
     *
     */
    private void setConfigurationEntries() {

        //check if config file entry exists for remoteType
        if (getConfigurationFile().getProperty(Constants.REMOTETYPE_KEY) == null) {

//            if (isServerConfiguration()) {
//
//                getConfigurationFile().setProperty(
//                        Constants.REMOTETYPE_KEY,
//                        RemoteType.SERVER.toString());
//            } else {
            //set/write default remoteType in config file
            getConfigurationFile().setProperty(
                    Constants.REMOTETYPE_KEY,
                    RemoteType.NONE.toString());
//            }
        }

//        //check if config file entry exists for path to jar file
//        if (getConfigurationFile().getProperty(Constants.JAR_PATH_KEY) == null) {
//
//            getConfigurationFile().setProperty(
//                    Constants.JAR_PATH_KEY, getVrlUgJarPath());
//        }
//
//        //check if config file entry exists for path to local server folder
//        if (getConfigurationFile().getProperty(Constants.PATH_TO_LOCAL_SERVER_FOLDER_KEY) == null) {
//
//            getConfigurationFile().setProperty(
//                    Constants.PATH_TO_LOCAL_SERVER_FOLDER_KEY,
//                    getLocalServerFolder().getAbsolutePath());
//        }

        getConfigurationFile().save();

    }

    /**
     * Checks the commandline and config file for remoteType and preferes the
     * value of the config file.
     *
     * @return the remoteType that should be used
     */
    private static String checkRemoteTypeOption() {
//        System.out.println("Configurator.checkRemoteTypeOption()");

        String tmp = null;
        String option = null;

        //check if there is an information on commandline
        //needed at the moment for the sever
        tmp = VArgUtil.getArg(VRL.getCommandLineOptions(),
                "-" + Constants.REMOTETYPE_KEY);

//        System.out.println("VRL.getCommandLineOptions("
//                + Constants.REMOTETYPE_KEY + ") = " + tmp);

        if (tmp != null) {
            option = tmp;
        }

//        if (!option.toLowerCase().
//                equals(
//                RemoteType.SERVER.toString().toLowerCase())) {


        //check if there is an information in config file
        //this should be the prefered way of getting informations
        tmp = pluginConfiguration.getProperty(Constants.REMOTETYPE_KEY);

//        System.out.println(" **** checkRemoteTypeOption( iAPI) OPTION: "
//                + Constants.REMOTETYPE_KEY + " = " + tmp);

//        }

        if (tmp != null) {
            option = tmp;
        }

//        System.out.println(" # # # after checking commandline and property file");
//        System.out.println(" # # # REMOTETYPE option = " + option);


//        //setting boolean for checking and setting serverConfigurationProperties
//        //this should be only executed in server JVM
        if ((option != null)
                && (option.toLowerCase().equals(
                RemoteType.SERVER.toString().toLowerCase()))) {

//            System.out.println(" # # # if option!=null && SERVER.toLowerCase()");

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

//        System.out.println("configurator.setServerConfiguration(" + serverConfiguration + ")");
        Configurator.serverConfiguration = serverConfiguration;
    }

    private File installTemplate(String templateName, InitPluginAPI iApi ) {
        InputStream in = Configurator.class.getResourceAsStream(
                "/edu/gcsc/vrl/ug/resources/"+templateName);
        
        File projectSrc = new File(iApi.getResourceFolder(), templateName);
        
        try {
            IOUtil.saveStreamToFile(in, projectSrc);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configurator.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configurator.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        
        return projectSrc;
    }
}
