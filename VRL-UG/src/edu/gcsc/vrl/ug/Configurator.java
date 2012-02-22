/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.VArgUtil;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.*;
import eu.mihosoft.vrl.visual.VDialog;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator extends VPluginConfigurator {
    
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
//            UG.getInstance().setMainCanvas(vCanvas);
        
            VPluginAPI vApi = (VPluginAPI) api;
            
             //TEST: component for starting an UG on an other JVM
            vApi.addComponent(JVMmanager.class);

            
//            VPluginAPI vApi = (VPluginAPI) api;
//            
//            vApi.addAction(new VAction("SetConfig") {
//
//                @Override
//                public void actionPerformed(ActionEvent e, Object owner) {
//                    getInitAPI().getConfiguration().setProperty("time", ""+System.nanoTime()).save();
//                }
//            }, ActionDelelator.EDIT_MENU);
//            
//             vApi.addAction(new VAction("GetConfig") {
//
//                @Override
//                public void actionPerformed(ActionEvent e, Object owner) {
//                    System.out.println("time: " + getInitAPI().getConfiguration().getProperty("time"));
//                }
//            }, ActionDelelator.EDIT_MENU);
//             
//              vApi.addAction(new VAction("RemoveConfig") {
//
//                @Override
//                public void actionPerformed(ActionEvent e, Object owner) {
//                    getInitAPI().getConfiguration().removeProperty("time").save();
//                }
//            }, ActionDelelator.EDIT_MENU);
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

        // define native lib location
        UG.setNativeLibFolder(getNativeLibFolder());
        
//        String option = VArgUtil.getArg(VRL.getCommandLineOptions(),"-rpc");
        
         PluginConfiguration pConf = iApi.getConfiguration();
//         pConf.setProperty("-rpc", "client");
        
        System.out.println(this.getClass().getName() +
                " **** OPTION: -rpc = " + pConf.getProperty("-rpc"));
        
        
        // initialize ug instance
        UG.getInstance(pConf.getProperty("-rpc"));

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
