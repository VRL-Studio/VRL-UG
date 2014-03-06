/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug.types;

import edu.gcsc.vrl.ug.remote.RemoteService;
import edu.gcsc.vrl.ug.UG;
import eu.mihosoft.vrl.annotation.TypeInfo;
import eu.mihosoft.vrl.types.SaveFileType;
import java.io.File;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
@TypeInfo(type = File.class, input = true, output = false, style = "remote-save-dialog")
public class RemoteSaveFileType extends SaveFileType {

    File transferedFile = null;

    @Override
    public Object getValue() {
        Object result = super.getValue();

        System.out.println("RemoteSaveFileType.getValue()");

        if (!getMainCanvas().isLoadingSession() && !getMainCanvas().isSavingSession()) {
            /*
             TODO:
             here we need to "listen some how" to remote component when it is finised
             and we can get/transfer the remote file to client.
            NO WE DON'T!
            Because if we have executed the code of a input TypeRepo we just entering the method
            we want to execute and we can't wait because elsewise 
            - we would not go inside the methode,
            - we would not execute the methode and
            - we would not get the methode result we are waiting for !!!!
            
            But we need to transfer the file to server so it can be used on server side.
             */
            System.out.println("result" + result);
            if (result instanceof File) {
                transferedFile = (File) result;
                System.out.println("transferedFile = " + transferedFile);
                System.out.println("transferedFile.getAbsolutePath() = " + transferedFile.getAbsolutePath());

                RemoteService.transferFileToServer(transferedFile);
            }

        }
        return result;
    }

//    @Override
//    public void setValue(Object o) {
//        super.setValue(o); //To change body of generated methods, choose Tools | Templates.
//
//        System.out.println("RemoteSaveFileType.setValue()");
//        
//        if (!getMainCanvas().isLoadingSession() && !getMainCanvas().isSavingSession()) {
//            transferedFile = (File) o;
//
//            System.out.println("transferedFile ="+ transferedFile);
//            
////            RemoteService.transferFileToServer(transferedFile);
//            RemoteService.getFileFromServer(transferedFile);
//        }
//    }
//    @Override
//    public Object getViewValue() {
//        Object result = super.getViewValue(); 
//        
//        System.out.println("RemoteSaveFileType.getViewValue()");
//
//        if (!getMainCanvas().isLoadingSession() && !getMainCanvas().isSavingSession()) {
//            /*
//             TODO:
//             here we need to "listen some how" to remote component when it is finised
//             and we can get/transfer the remote file to client.
//             */
//            System.out.println("transferedFile = "+ transferedFile);
//            result = RemoteService.getFileFromServer(transferedFile);
//            
//            System.out.println("result"+ result);
//            if(result instanceof File){
//               File f = (File) result;
//                System.out.println("f.getAbsolutePath() = "+ f.getAbsolutePath());
//            }
//        }
//        return result;
//    }
//    @Override
//    public Object getValue() {
//        Object tmp= super.getValue(); //To change body of generated methods, choose Tools | Templates.
//        System.out.println("RemoteSaveFileType.getValue()");
//        System.out.println("tmp = "+ tmp);
//        System.out.println("UG.getRemoteType() = "+UG.getRemoteType());
//        return tmp;
//    }
}
