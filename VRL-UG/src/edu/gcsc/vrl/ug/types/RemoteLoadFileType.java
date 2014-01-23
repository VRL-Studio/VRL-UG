/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug.types;

import edu.gcsc.vrl.ug.RemoteService;
import edu.gcsc.vrl.ug.JVMmanager;
import eu.mihosoft.vrl.annotation.TypeInfo;
import eu.mihosoft.vrl.types.LoadFileType;
import java.io.File;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
@TypeInfo(type = File.class, input = true, output = false, style = "remote-load-dialog")
public class RemoteLoadFileType extends LoadFileType{
    private static final long serialVersionUID = 500442740078037575L;

//    private JVMmanager jvmManager = null;
//    
//    public RemoteLoadFileType() {
//        super();
//        jvmManager = JVMmanager.getInstance();
//        
////        jvmManager.isServerRunning(jvmManager.getCurrentIP(), jvmManager.getCurrentPort());
//    }

    @Override
    public Object getViewValue() {
        Object result = super.getViewValue(); 
        
        if (result!=null && !getMainCanvas().isLoadingSession() && !getMainCanvas().isSavingSession()) {
            File f = (File) result;
            
            result = RemoteService.getFileFromServer(f);
        }
        
        return result;
    }
    
    
    
    
}
