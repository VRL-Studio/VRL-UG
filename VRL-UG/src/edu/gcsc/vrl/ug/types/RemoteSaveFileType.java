/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug.types;

import edu.gcsc.vrl.ug.RemoteService;
import eu.mihosoft.vrl.annotation.TypeInfo;
import eu.mihosoft.vrl.types.SaveFileType;
import java.io.File;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
@TypeInfo(type = File.class, input = true, output = false, style = "remote-save-dialog")
public class RemoteSaveFileType extends SaveFileType{

    @Override
    public void setViewValue(Object o) {
        super.setViewValue(o); //To change body of generated methods, choose Tools | Templates.
        
        if (!getMainCanvas().isLoadingSession() && !getMainCanvas().isSavingSession()) {
            File f = (File) o;
            
            RemoteService.transferFileToServer(f); 
        }
    }
    
    /*
    TODO:
    here we need to "listen some how" to remote component when it is finised
    and we can get/transfer the remote file to client.
    */
    
}
