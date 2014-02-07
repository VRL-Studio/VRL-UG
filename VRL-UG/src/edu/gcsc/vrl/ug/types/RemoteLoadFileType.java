/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug.types;

import edu.gcsc.vrl.ug.RemoteService;
import edu.gcsc.vrl.ug.JVMmanager;
import edu.gcsc.vrl.ug.UG;
import eu.mihosoft.vrl.annotation.TypeInfo;
import eu.mihosoft.vrl.types.LoadFileType;
import java.io.File;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
@TypeInfo(type = File.class, input = true, output = false, style = "remote-load-dialog")
public class RemoteLoadFileType extends LoadFileType {

    private static final long serialVersionUID = 500442740078037575L;

//    private JVMmanager jvmManager = null;
//    
//    public RemoteLoadFileType() {
//        super();
//        jvmManager = JVMmanager.getInstance();
//        
////        jvmManager.isServerRunning(jvmManager.getCurrentIP(), jvmManager.getCurrentPort());
//    }
//    File transferedFile = null;

    @Override
    public Object getValue() {
        Object result = super.getValue();

        System.out.println("RemoteLoadFileType.getValue()");
        System.out.println("result = "+ result);

        File transferedFile = null;
        
        if (result!=null && !getMainCanvas().isLoadingSession() && !getMainCanvas().isSavingSession()) {
            transferedFile = (File) result;

            System.out.println("transferedFile = " + transferedFile);
            if (transferedFile != null) {
                System.out.println("transferedFile.getAbsolutePath() = " + transferedFile.getAbsolutePath());
            }

//            result = RemoteService.getFileFromServer(transferedFile);
             RemoteService.transferFileToServer(transferedFile);
        }

        return result;
    }

//    @Override
//    public void setViewValue(Object o) {
//        super.setViewValue(o); //To change body of generated methods, choose Tools | Templates.
//
//        System.out.println("RemoteLoadFileType.setViewValue()");
//
//        if (!getMainCanvas().isLoadingSession() && !getMainCanvas().isSavingSession()) {
//            transferedFile = (File) o;
//            
//            System.out.println("transferedFile = " + transferedFile);
//            if (transferedFile != null) {
//                System.out.println("transferedFile.getAbsolutePath() = " + transferedFile.getAbsolutePath());
//            }
//
//            RemoteService.transferFileToServer(transferedFile);
//        }
//    }

//    @Override
//    public Object getValue() {
//        Object tmp = super.getValue(); //To change body of generated methods, choose Tools | Templates.
//        System.out.println("RemoteLoadFileType.getValue()");
//        System.out.println("tmp = " + tmp);
//        System.out.println("UG.getRemoteType() = " + UG.getRemoteType());
//        return tmp;
//    }

}
