/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import edu.gcsc.vrl.ug.types.RemoteType;
import eu.mihosoft.vrl.annotation.ParamInfo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
public class TestRemoteFile {

    public static File doSomething(@ParamInfo(name ="writeInto",style = "remote") File file) {
        File result = file;

        RemoteType rt = UG.getRemoteType();

//        result = new File("example.txt");
        BufferedWriter output = null;
        String text = null;

        try {
            output = new BufferedWriter(new FileWriter(file));
            
        } catch (IOException ex) {
            Logger.getLogger(TestRemoteFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        switch (rt) {
            case NONE:
                text = "remote type NONE " + System.nanoTime() +"\n";
                break;

            case CLIENT:
                text = "remote type CLIENT " + System.nanoTime()+"\n";
                break;

            case SERVER:
                text = "remote type SERVER " + System.nanoTime()+"\n";
                break;
                
            default:
                text = "remote type is unknown ERROR " + System.nanoTime()+"\n";
                System.err.println(text);
        }
        try {
            output.append(text);
            output.close();
            
        } catch (IOException ex) {
            Logger.getLogger(TestRemoteFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return result;
    }
    
    
    public File selectFile(@ParamInfo(name = "choose", style = "load-dialog")File file){
        return file;
    }

}

/*

if (o!=null && !getMainCanvas().isLoadingSession() && !getMainCanvas().isSavingSession()) {
            File f = (File) o;
            
            IOUtil.createTempDir(); + localPath
            
            RemoteService.getFile(this, f);
        }

*/
