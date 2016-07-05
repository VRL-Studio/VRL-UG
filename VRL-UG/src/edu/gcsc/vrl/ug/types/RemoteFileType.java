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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug.types;

import edu.gcsc.vrl.ug.remote.JVMmanager;
import edu.gcsc.vrl.ug.remote.RemoteService;
import edu.gcsc.vrl.ug.UG;
import eu.mihosoft.vrl.annotation.TypeInfo;
import eu.mihosoft.vrl.system.VRL;
import eu.mihosoft.vrl.types.FileType;
import java.io.File;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
//@TypeInfo(type=File.class, input = true, output = true, style="remote-default")
//@TypeInfo(type=File.class, input = true, output = true, style="remote")
//@TypeInfo(type = File.class, input = true, output = true, style = "default") // nicht wegen 1)
@TypeInfo(type=File.class, input = false, output = true, style="remote")
public class RemoteFileType extends FileType {

   @Override
    public void setValue(Object o) {
        super.setValue(o); //To change body of generated methods, choose Tools | Templates.

        System.out.println("RemoteFileType.setValue()");
        
        if (!getMainCanvas().isLoadingSession() && !getMainCanvas().isSavingSession()) {
            File transferedFile = (File) o;

            System.out.println("transferedFile ="+ transferedFile);
            
//            RemoteService.transferFileToServer(transferedFile);
            RemoteService.getFileFromServer(transferedFile);
        }
    }

    
    
    
//    private RemoteType ugRemoteType = RemoteType.NONE;
//    private String pathOnServer = null;
//    private String pathOnClient = null;
//
//    public RemoteFileType() {
//        super();
//        ugRemoteType = UG.getRemoteType();
//    }
//
//    /*
//     fragen:
//     1) wenn ich style "default" habe (quasie ein override??)
//     was passiert mit file componenten die nicht an ug gebunden sind
//     bzw wie unterscheide ich zwischen UG-Komponenten und NICHT-UG-komponenten
//     2) wenn ich style "remote" habe wie stelle ich sicher das alle UG komponenten diesen typ im remote 
//     modus verwenden ohne das das den komponenten explizit mit ParamInfo mitteilt
//     3) wie stelle ich sicher dass wenn mehrere dateien in einer ug-remote-session verwendet werden diese
//     auf der server seite wieder zur richtigen ug-komponente zugeordnet bzw verwendet werden.
//     */
//    @Override
//    public void setViewValue(Object o) {
////        if (ugRemoteType.equals(RemoteType.NONE)) {
//            // act like before / super class
//            super.setViewValue(o);
////        }
//        // if we are NOT none -> server or client 
//        // and we need to do some super stuff different !?
////        else{
//            File f = (File) o;
//
//            if (ugRemoteType.equals(RemoteType.CLIENT)) {
////                File f = (File) o;
//                
//                //split to get the name of the file, which is in the last element of the array
//                String[] splits = f.getAbsolutePath().split("\\/");
//
//                pathOnClient = f.getAbsolutePath();
////                input.setText(pathOnClient);
//
//                //set the name and the temporal storage position on server
//                // ERROR / PROBLEM 
//                //here the client need to know the path on the sever side but we are on client side !!!
//                pathOnServer = VRL.getPropertyFolderManager().getTmpFolder().getAbsolutePath()
//                        + splits[splits.length - 1];
//
//            } //CLIENT
//            // not needed because on server side we already have the file with it path in tmp folder
////            else if (ugRemoteType.equals(RemoteType.SERVER)) {
////                pathOnServer = f.getAbsolutePath();
////            }//SERVER
////        }// ! NONE
//    }
//
//    @Override
//    public Object getViewValue() {
//        File result = null;
//
//        if (ugRemoteType.equals(RemoteType.NONE)) {
//            result = (File) super.getViewValue();
//
//        } else if (ugRemoteType.equals(RemoteType.CLIENT)) {
//            //
//            //if output get file from server
//            //
//            
//            if (getConnector().isOutput()) {
//                result = JVMmanager.getFileFromServer(pathOnServer, pathOnClient);
//            }
//            else{//input
//                // ?? 
//                // nothing to do ? or just do super ?
//                result = (File) super.getViewValue();
//                
//                if (getConnector().isOutput()) {
//                    File clientFile =new File(pathOnClient);
//                    //or send the file to server into the tmp folder with the same name as on client side
//                    //so if server needs the file it is already there
//                 JVMmanager.transferFileToServer(pathOnServer, clientFile);
//            }
//            }
//
//        } else if (ugRemoteType.equals(RemoteType.SERVER)) {
//            //
//            //if input get file from client
//            //
////            if (getConnector().isInput()) {
////                result = JVMmanager.getFileFromClient(pathOnServer, pathOnClient);
//                /// ERROR / PROBLEM  !!!
//                //but were we get the value of pathOnServer if we are already on server side and client is setting it ???
//                //if we are server we know client has send us all files into tmp folder
//                 String[] splits = pathOnClient.split("\\/");
//                pathOnServer = VRL.getPropertyFolderManager().getPluginFolder()+ splits[splits.length - 1];
//                result = new File(pathOnServer);
////            }
////            else{//output
////                // ?? 
//////                // nothing to do ? or just do super ?
//////                result = (File) super.getViewValue();
////// put all date into tmp folder were client could get it
////            }
//            
//        }
//        return result;
//    }

}
