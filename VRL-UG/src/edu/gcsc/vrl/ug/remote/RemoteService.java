package edu.gcsc.vrl.ug.remote;

import edu.gcsc.vrl.ug.UG;
import edu.gcsc.vrl.ug.remote.JVMmanager;
import static edu.gcsc.vrl.ug.remote.JVMmanager.getClient;
import static edu.gcsc.vrl.ug.remote.JVMmanager.getCurrentIP;
import static edu.gcsc.vrl.ug.remote.JVMmanager.getCurrentPort;
import edu.gcsc.vrl.ug.types.RemoteLoadFileType;
import edu.gcsc.vrl.ug.types.RemoteType;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ObjectInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.io.IOUtil;
import eu.mihosoft.vrl.reflection.TypeRepresentation;
import eu.mihosoft.vrl.system.VRL;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/*
 * This class should hide the complexity of remote interaction between client and server file transfer
 * for the user.
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
@ObjectInfo(name = "RemoteService")
@ComponentInfo(name = "RemoteService", category = "VRL/VRL-UG")
// do not forget to add the class in Configurator.register() via
// vApi.addComponent("THE_CLASSFILE_OF_THIS_CLASS")
public class RemoteService implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     With this method the user do not need to know / care about were the file should be store
     on server side.
    
     @param file that should be transfered to server
     @return true is transfer worked, else false
     */
    public static Boolean transferFileToServer(File file) {

        System.out.println("RemoteService.transferFileToServer()");

        if (UG.getRemoteType().equals(RemoteType.CLIENT)) {

            //get the client to communicte with the current server
            XmlRpcClient client = JVMmanager.getCurrentClient();

            String pathOnClient = file.getAbsolutePath();

            // addind all needed parameters into parameter vector in a XMLrpc usable state
            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(pathOnClient);

            try {
                //convert the file into a string
                //and add it to the parameter vector
                xmlRpcParams.addElement(IOUtil.fileToBase64(file));

            } catch (IOException ex) {
                Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

            //add checksumme
            String checksumme = IOUtil.generateMD5Sum(file);
            xmlRpcParams.addElement(checksumme);

            try {
                //call remote the server method
                Object o = client.execute("RpcHandler.saveFileWithChecksumme", xmlRpcParams);

                if (o instanceof Boolean) {

                    return ((Boolean) o).booleanValue();
                }

            } catch (XmlRpcException ex) {
                Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }

        return false;
    }

    /**
     With this method the user do not need to know / care about were the output file is stored
     on server side, he only need to know were it should be on client side. This method will get the 
     corresponding server file and return it if possible.
    
     @param fileOnClientSide the file on client side
     @return the file on client side with the data from server if all was succesful, else null.
     */
    public static File getFileFromServer(/*TypeRepresentation typeRep,*/File fileOnClientSide) {
        File result = null;

        System.out.println("RemoteService.getFileFromServer()");
        System.out.println("UG.getRemoteType() = " + UG.getRemoteType());

        if (UG.getRemoteType().equals(RemoteType.CLIENT)) {

            String pathOnClient = fileOnClientSide.getAbsolutePath();

            //get the client to communicte with the current server
            XmlRpcClient client = JVMmanager.getCurrentClient();

            // addind all needed parameters into parameter vector in a XMLrpc usable state
            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(pathOnClient);

            try {
                //call remote the server method
                Object o = client.execute("RpcHandler.getFileWithChecksumme", xmlRpcParams);

                if (o instanceof String[]) {

                    String[] convertedFileAndCecksumme = (String[]) o;

                    try {
                        //writte data from server file into client file
                        IOUtil.base64ToFile(convertedFileAndCecksumme[0], fileOnClientSide);

                    } catch (IOException ex) {
                        Logger.getLogger(RemoteService.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String checksumme = IOUtil.generateMD5Sum(fileOnClientSide);

                    //check if data is correct transfered
                    if (checksumme.equals(convertedFileAndCecksumme[1])) {

                        // data is correct so set result
                        result = fileOnClientSide;
                    }
                }// if (o instanceof String[])

            } catch (XmlRpcException ex) {
                Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return result;
    }

    /**
     With this method the user can dicede which path he want to use the client or server path to get 
     the wanted file from the server. This method will get the  corresponding server file and return it if possible.
    
     @param path of the wanted file 
     @param serverPath true if the path variable is already a server path, 
     false if the path varibale contains a client path that need to be converted into a server path
     @return the file on client side with the data from server if all was succesful, else null.
     */
    public static File getFileFromServer(String path, Boolean serverPath) {
        File result = null;

        System.out.println("RemoteService.getFileFromServer()");
        System.out.println("path = " + path);
        System.out.println("serverPath = " + serverPath);
        System.out.println("UG.getRemoteType() = " + UG.getRemoteType());

        if (UG.getRemoteType().equals(RemoteType.CLIENT)) {
            System.out.println("RemoteService.getFileFromServer() if CLIENT");

            //get the client to communicte with the current server
            XmlRpcClient client = JVMmanager.getCurrentClient();

            // addind all needed parameters into parameter vector in a XMLrpc usable state
            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(path);
            xmlRpcParams.addElement(serverPath);

            try {

                System.out.println("RemoteService.getFileFromServer() BEFORE calling RpcHandler.getFileWithChecksumme");

                //call remote the server method
                Object o = client.execute("RpcHandler.getFileWithChecksumme", xmlRpcParams);

                System.out.println("RemoteService.getFileFromServer() AFTER calling RpcHandler.getFileWithChecksumme");

                System.out.println("o.getClass().getName() = " + o.getClass().getName());
                if (o instanceof Object[]) {
                    Object[] objArray = (Object[]) o;

                    String[] convertedFileAndCecksumme = new String[objArray.length];

                    for (int i = 0; i < objArray.length; i++) {

                        if (objArray[i] instanceof String) {
                            convertedFileAndCecksumme[i] = (String) objArray[i];
                            System.out.println("convertedFileAndCecksumme[" + i + "] = " + convertedFileAndCecksumme[i]);
                        }
                    }

                    File fileOnClientSide = null;

                    if (serverPath) {
                        fileOnClientSide = new File(translateServerPathToClientPath(path));
                    } else {
                        fileOnClientSide = new File(path);
                    }

                    System.out.println("fileOnClientSide = " + fileOnClientSide);

                    try {
                        //writte data from server file into client file
                        IOUtil.base64ToFile(convertedFileAndCecksumme[0], fileOnClientSide);

                    } catch (IOException ex) {
                        Logger.getLogger(RemoteService.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String checksumme = IOUtil.generateMD5Sum(fileOnClientSide);

                    //check if data is correct transfered
                    if (checksumme.equals(convertedFileAndCecksumme[1])) {

                        System.out.println("if (checksumme.equals(convertedFileAndCecksumme[1]");

                        // data is correct so set result
                        result = fileOnClientSide;
                    } else {
                        System.out.println("RemoteService.getFileFromServer() received checksumme or file is wrong.");
                    }
                }// if (o instanceof String[])

            } catch (XmlRpcException ex) {
                Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);

            }
        } else if (UG.getRemoteType().equals(RemoteType.NONE)) {
            result = new File(path);
        }

        System.out.println("RemoteService.getFileFromServer() result = " + result);

        return result;
    }

    /**
     Calling the server and asking for the corresponding path of a client file on the server side.
    
     @param clientPath the path of a file on client side
     @return the path of the file on server side
     */
    public static String translateClientPathToServerPath(String clientPath) {
        String serverPath = clientPath;
        System.out.println("RemoteService.translateClientPathToServerPath()");
        System.out.println("clientPath = " + clientPath);

        if (UG.getRemoteType().equals(RemoteType.CLIENT)) {

            //get the client to communicte with the current server
            XmlRpcClient client = JVMmanager.getCurrentClient();

            // addind all needed parameters into parameter vector in a XMLrpc usable state
            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(clientPath);
            try {
                //call remote the server method
                Object o = client.execute("RpcHandler.getServerPath", xmlRpcParams);

                if (o instanceof String) {
                    serverPath = (String) o;
                }
            } catch (XmlRpcException ex) {
                Logger.getLogger(RemoteService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("serverPath = " + serverPath);
        return serverPath;
    }

    /**
    
     @param serverPath  the path of a file on server side
     @return the path of the file on client side
     */
    public static String translateServerPathToClientPath(String serverPath) {
        String serverPrefixPath = null;
        String clientPath = null;

        System.out.println("RemoteService.translateServerPathToClientPath()");
        System.out.println("serverPath = " + serverPath);

        if (UG.getRemoteType().equals(RemoteType.CLIENT)) {

            //get the client to communicte with the current server
            XmlRpcClient client = JVMmanager.getCurrentClient();

            // addind all needed parameters into parameter vector in a XMLrpc usable state
            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement("/");
            try {
                //call remote the server method
                Object o = client.execute("RpcHandler.getServerPath", xmlRpcParams);

                if (o instanceof String) {
                    serverPrefixPath = (String) o;
                    System.out.println("serverPrefixPath = " + serverPrefixPath);
                }
            } catch (XmlRpcException ex) {
                Logger.getLogger(RemoteService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int index = serverPrefixPath.length();
//        clientPath = serverPath.substring(0, index);
        clientPath = serverPath.substring(index, serverPath.length());

        System.out.println("clientPath = " + clientPath);

        return clientPath;
    }

}
