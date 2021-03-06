/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2012–2018 Goethe Universität Frankfurt am Main, Germany
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
package edu.gcsc.vrl.ug;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import edu.gcsc.vrl.ug.types.RemoteType;
import eu.mihosoft.vrl.io.Base64;
import eu.mihosoft.vrl.io.IOUtil;
import eu.mihosoft.vrl.system.VRL;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* 
 * NOTICE:
 *
 * ALL METHODS need to RETURN one of the following types:
 *
 * XML-RPC type     Simplest Java type      More complex Java type
 *
 * i4               int                     java.lang.Integer 
 * int              int                     java.lang.Integer 
 * boolean          boolean                 java.lang.Boolean 
 * string           java.lang.String        java.lang.String 
 * double           double                  java.lang.Double
 *
 * dateTime.iso8601 java.util.Date          java.util.Date 
 * struct           java.util.Hashtable     java.util.Hashtable
 * array            java.util.Vector        java.util.Vector
 * base64           byte[]                  byte[]
 *
 * nil (extension)  null                    null
 *
 * ATTENTION: void is NOT valid !!!
 */
//
//
/**
 * The RpcHandler methods were executed on a UG instance with RemoteType SERVER
 * and called remote via clients.
 *
 * The RpcHandler is the interface for a webserver which allows UG to act as a
 * server, if the webserver is started in an UG instance with RemoteType SERVER.
 *
 * So RpcHandler is more part of the server concept, but the clients need to
 * know the interface of this class (the method-heads) for remote calls.
 *
 * The implemented methods handle the result transfer of different objects via
 * network via Base64. Mark all so transfered objects need to be serializable.
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
public class RpcHandler {

    private static UG server = null;

    /**
     * @return the UG instance with RemoteType SERVER
     */
    public static UG getServer() {

//        System.out.println("CLS RemoteType of UG =" + UG.getRemoteType());
//        
//        System.out.println("RpcHandler.getServer(): CLS.RpcHandler:="
//                + RpcHandler.class.getClassLoader());
//        
//        System.out.println("RpcHandler.getServer(): CLS.Configurator:="
//                + Configurator.class.getClassLoader());
//        
//        System.out.println("RpcHandler.getServer(): CLS.UG:="
//                + UG.class.getClassLoader());
//        
//        System.out.println("RpcHandler.getServer(): CLS.System:="
//                + ClassLoader.getSystemClassLoader());
        if (server == null) {
            System.out.println("RpcHandler.getServer()==null");

            if (Configurator.isServerConfiguration()) {
                System.out.println("RpcHandler.getServer():"
                        + " Configurator.isServerConfiguration() = true");

                // set in the server JVM the server ug object return
                server = UG.getInstance(null, RemoteType.SERVER);

            } else {

                throw new UnsupportedOperationException("Calling Configurator.getUGserver"
                        + " is only allowed if isServerConfiguration()=true.");
            }
        }

//        System.out.print("---- RpcHandler.getServer() is ");
//        if (server != null) {
//            System.out.print("NOT ");
//        }
//        System.out.println("NULL.");
        return server;
    }

    /**
     * @param aServer the UG instance with RemoteType SERVER to set
     */
    public static void setServer(UG aServer) {
        server = aServer;
    }

//    //TEST FUNCTION
    public int show(String methodName) {
        System.out.println("RpcHandler." + methodName + "() invoked.");
        return 1;
    }

    // ********************************************
    // ************** NATIVE METHODS **************
    // ********************************************
    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public final String convertRegistryInfo() {

        show("convertRegistryInfo");

//        System.out.println("RpcHandler.convertRegistryInfo(): CLS.RpcHandler:="
//                + RpcHandler.class.getClassLoader());
//        
//        System.out.println("RpcHandler.convertRegistryInfo(): CLS.Configurator:="
//                + Configurator.class.getClassLoader());        
//        
//        System.out.println("RpcHandler.convertRegistryInfo(): CLS.UG:="
//                + UG.class.getClassLoader());
//        
//        System.out.println("RpcHandler.convertRegistryInfo(): CLS.System:="
//                + ClassLoader.getSystemClassLoader());
//        
//        
//        System.out.println("RpcHandler.convertRegistryInfo(): CLS.Base64:="
//                + Base64.class.getClassLoader());        
//        
//        System.out.println("RpcHandler.convertRegistryInfo(): CLS.NativeAPIInfo:="
//                + NativeAPIInfo.class.getClassLoader());
//        
//        System.out.println("RpcHandler.convertRegistryInfo(): CLS.NativeClassGroupInfo:="
//                + NativeClassGroupInfo.class.getClassLoader());
//            System.out.println("RpcHandler.convertRegistryInfo() :::: -> ");
//            System.out.println("o = Base64.decodeToObject(base64);");
//            System.out.println("leads to -> ");
//            System.out.println("java.lang.ClassNotFoundException: edu.gcsc.vrl.ug.NativeAPIInfo");
//            
//            System.out.println("RpcHandler.convertRegistryInfo() :::: -> ");
//            System.out.println("o = Base64.decodeToObject(base64, UG.class.getClassLoader());");
//            System.out.println("leads to -> ");
//            System.out.println("java.lang.ClassNotFoundException: [Ledu.gcsc.vrl.ug.NativeClassGroupInfo;");
        NativeAPIInfo napiInfo = getServer()._convertRegistryInfo();

        if (napiInfo == null) {
            System.out.println("NativeAPIInfo IS NULL");
        } else {
            System.out.println("NativeAPIInfo IS NOT NULL");
        }

        String base64 = Base64.encodeObject(napiInfo);

//        System.out.println("base64.substring(0,11): " + base64.substring(0, 11));
        return base64;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public String invokeMethod(
            String exportedClassName, String objPtr, boolean readOnly,
            String methodName, String params) {
        show("invokeMethod");

//        System.out.println("RpcHandler.invokeMethod() paramas values are:");
//        System.out.println("exportedClassName = " + exportedClassName);
//        System.out.println("objPtr = " + objPtr);
//        System.out.println("readOnly = " + readOnly);
//        System.out.println("methodName = " + methodName);
//        System.out.println("params = " + params);
        Object o = Base64.decodeToObject(params, UG.class.getClassLoader());

//        Object o = UGBase64.decodeToObject(params);
        Object[] objArray = (Object[]) o;

//        //DEBUG LOOP
//        for (int i = 0; i < objArray.length; i++) {
//            System.out.println("params[" + i + "] = " + objArray[i]);
//
//        }
        o = getServer()._invokeMethod(
                exportedClassName, new Long(objPtr), readOnly, methodName, objArray);

        //assumption object is serializable
        String base64 = Base64.encodeObject((Serializable) o);

//        System.out.println("encoded Object = " + base64);
        return base64;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public String newInstance(String exportedClassPtr, String parameters) {
        show("newInstance");

        Object o = Base64.decodeToObject(parameters, UG.class.getClassLoader());

//        Object o = UGBase64.decodeToObject(parameters);
        Object[] objArray = (Object[]) o;

        Pointer p = getServer()._newInstance(new Long(exportedClassPtr), objArray);

        String base64 = Base64.encodeObject((Serializable) p);

//        System.out.println("encoded Pointer = " + base64);
        return base64;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public String getExportedClassPtrByName(String name, boolean classGrp) {
        show("getExportedClassPtrByName");

        long result = getServer()._getExportedClassPtrByName(name, classGrp);

//        System.out.println("result =" + result);
        return String.valueOf(result);
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public String getDefaultClassNameFromGroup(String grpName) {
        show("getDefaultClassNameFromGroup");

        String result = getServer()._getDefaultClassNameFromGroup(grpName);

//        System.out.println("result =" + result);
        return result;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public String invokeFunction(String name, boolean readOnly, Object[] params) {
        show("invokeFunction");

        Object o = getServer()._invokeFunction(name, readOnly, params);

        //assumption all containing objects are serializable
        String base64 = Base64.encodeObject((Serializable) o);

//        System.out.println("RESULT: " + base64);
        return base64;

    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public String getSvnRevision() {
        show("getSvnRevision");

        String result = getServer()._getSvnRevision();

//        System.out.println("result =" + result);
        return result;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public String getDescription() {
        show("getDescription");

        String result = getServer()._getDescription();

//        System.out.println("result =" + result);
        return result;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public String getAuthors() {
        show("getAuthors");

        String result = getServer()._getAuthors();

//        System.out.println("result =" + result);
        return result;

    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public String getCompileDate() {
        show("getCompileDate");

        String result = getServer()._getCompileDate();

//        System.out.println("result =" + result);
        return result;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public String getUGVersion() {
        show("getUGVersion");

        String result = getServer()._getUGVersion();

//        System.out.println("result =" + result);
        return result;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public String getBinaryLicense() {
        show("getBinaryLicense");

        String result = getServer()._getBinaryLicense();

//        System.out.println("result =" + result);
        return result;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     */
    public Integer ugInit(List<String> args) {
        show("#### " + this.getClass().getName() + " ugInit( String[] args )");

        String[] argsArray = new String[args.size()];

        argsArray = args.toArray(argsArray);

//        return Integer.valueOf(456);
        return getServer()._ugInit(argsArray);
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     *
     * Deallocates specified memory. The destructor of the specified class will
     * be called.
     *
     * @param objPtr object pointer
     * @param exportedClassPtr pointer of the exported class
     */
    @Deprecated
    public boolean delete(String objPtr, String exportedClassPtr) {
        show("delete");

        getServer()._delete(new Long(objPtr), new Long(exportedClassPtr));

        return true;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     *
     * Invalidates the specified smart pointer.
     *
     * @param base64 smart-pointer as String to invalidate
     */
    public boolean invalidate(String base64) {
        show("invalidate");

        Object o = Base64.decodeToObject(base64, UG.class.getClassLoader());

//        Object o = UGBase64.decodeToObject(base64);
        if (o instanceof SmartPointer) {
            SmartPointer p = (SmartPointer) o;

            getServer()._invalidate(p);

            return true;
        }

        return false;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return void : because of xmlrpc return type int
     *
     * Stops the web server in the server JVM, where UG runs with RemoteType
     * server.
     */
    public int stopWebServer() {
        show("stopWebServer");

        server.stopWebServer();
        return 0;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     *
     * Tries to connect to server and returns true if a connection could be
     * established.
     */
    public boolean isServerRunning() {
//        show("isServerRunning");

        Boolean b = UG.isServerRunning();

//        System.out.println("UG.isServerRunning() = " + b);
        if (b != null && b.booleanValue()) {
            return true;
        }
        return false;
    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * The result is send remote packed as Base64-String to the calling client.
     *
     * @return the result of the on server executed method
     *
     * Tries to connect to server and returns true if a connection could be
     * established.
     */
    public String getMessages() {
//        show("getMessages");

        StringBuilder builder = getServer().getMessages();

        String base64 = Base64.encodeObject((Serializable) builder);

//        System.out.println("encoded Pointer = " + base64);
        return base64;

    }

    /**
     * This method is wrapper for the same named method which is executed on the
     * UG instance with RemoteType server.
     *
     * @return void : because of xmlrpc return type int
     */
    public int clearMessages() {
//        show("clearMessages");

        StringBuilder messages = getServer().getMessages();

        if (messages.length() > 0) {
            messages.delete(0, messages.length());
            //messages = new StringBuilder();
        }

        return 0;
    }

    /**
     This method operates on the VRL-UG server side and stores the file which is in the data string
     at the exact position on the server.
     @param pathOnServer place on server including path, file name and typ where the data should be stored.
     @param data the data that should be stored on this server.
     @return true if data could be stored, else false.
     */
    public Boolean saveFile(String pathOnServer, String data) {
        show("saveFile");

        File fileOnServer = new File(pathOnServer);

        try {
            IOUtil.base64ToFile(data, fileOnServer);
        } catch (IOException ex) {
            Logger.getLogger(RpcHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    /**
     This method operates on the VRL-UG server side and returns the file which is specified via the
     parameter pathOnServer.
     @param pathOnServer the path were the file could be found including path, file name and typ.
     @return the converted file if possible, else null.
     */
    public String getFile(String pathOnServer) throws IOException {
        show("getFile");
        System.err.println("UG.getInstance().getRemoteType() = " + UG.getInstance().getRemoteType());

        File fileOnServer = new File(pathOnServer);

        String convertedFile = null;

        if (fileOnServer.exists()) {
            System.out.println("file exists");

            try {
                convertedFile = IOUtil.fileToBase64(fileOnServer);
            } catch (IOException ex) {
                Logger.getLogger(RpcHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            String msg = "RpcHandler.getFile() : the specified path do NOT leed to an existing file."
                    + "path path of file: " + fileOnServer.getAbsolutePath();
            System.err.println(msg);

            throw new IOException(msg);
        }

        return convertedFile;
    }

    /**
     This method cares about were a client file should be on server side stored.
    
     @param pathOnClient the path of the file on client side
     @param fileData the file which should be stored
     @param checksumme the checksumme which was generated on client side
     @return true if file was transfered and the server checksumme equals the client checksumme, else false.
     */
    public Boolean saveFileWithChecksumme(String pathOnClient, String fileData, String checksumme) throws IOException {
        show("saveFileWithChecksumme");

        File fileOnClient = new File(pathOnClient);
        File fileOnServer = VRL.getPropertyFolderManager().toLocalPathInTmpFolder(fileOnClient);

        fileOnServer.getParentFile().mkdirs();
        boolean parentFoldersExist = fileOnServer.getParentFile().exists();

        if (parentFoldersExist) {
            fileOnServer.createNewFile();

            try {
                IOUtil.base64ToFile(fileData, fileOnServer);

            } catch (IOException ex) {
                Logger.getLogger(RpcHandler.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

            Boolean checksummeOK = IOUtil.verifyFileMD5(fileOnServer, checksumme);

//        return true;
            return checksummeOK;
        }//if (parentFoldersExist) 
        else {
            String msg = "Could not create client folder structure on server side."
                    + "path that shoulded be created: " + fileOnServer.getAbsolutePath();
            System.err.println(msg);
            throw new IOException(msg);
        }

    }

    /**
     This method should be used to get the outputs file from components which are executed on 
     server side. 
     @param pathOnClient the path of the wanted file on client side
     @return a string array consisting of the wanted file and the corresponding checksumme
     */
    public String[] getFileWithChecksumme(String pathOnClient) throws IOException {
        show("getFileWithChecksumme");

//        File fileOnClient = new File(pathOnClient);
//        File fileOnServer = VRL.getPropertyFolderManager().toLocalPathInTmpFolder(fileOnClient);
//        
//        fileOnServer.getParentFile().mkdirs();
        File fileOnServer = createFileOnServer(pathOnClient);

        boolean parentFoldersExist = fileOnServer.getParentFile().exists();

        String[] convertedFileAndCecksumme = null;

        System.out.println("UG.getRemoteType() = " + UG.getRemoteType());
//        System.out.println("fileOnClient.exists() = "+fileOnClient.exists()+",fileOnClient = "+ fileOnClient);
        System.out.println("fileOnServer.exists() = " + fileOnServer.exists() + ", fileOnServer = " + fileOnServer);
        System.out.println("parentFoldersExist = " + parentFoldersExist);

        if (fileOnServer.exists()) {
//        if (parentFoldersExist) {
            fileOnServer.createNewFile();

            System.out.println("fileOnServer.exists() = " + fileOnServer.exists() + ", fileOnServer = " + fileOnServer);

            convertedFileAndCecksumme = new String[2];

            try {
                convertedFileAndCecksumme[0] = IOUtil.fileToBase64(fileOnServer);
            } catch (IOException ex) {
                Logger.getLogger(RpcHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            convertedFileAndCecksumme[1] = IOUtil.generateMD5Sum(fileOnServer);
        }//fileOnServer.exists
        else {

            String msg = "RpcHandler.getFileWithChecksumme() : the specified path do NOT leed to an existing file."
                    + "path path of file: " + fileOnServer.getAbsolutePath();
            System.err.println(msg);
            throw new IOException(msg);
        }

        return convertedFileAndCecksumme;
    }

    /**
     This method should be used to get the outputs file from components which are executed on 
     server side. 
     @param path of the wanted file 
     @param serverPath true if the path variable is already a server path, 
     false if the path varibale contains a client path that need to be converted into a server path
     @return a string array consisting of the wanted file and the corresponding checksumme
     */
    public String[] getFileWithChecksumme(String path, Boolean serverPath) throws IOException {
        show("getFileWithChecksumme");

        System.out.println("path = " +path);
        System.out.println("serverPath = " +serverPath);
        
        File fileOnServer = null;
        if (serverPath) {
            fileOnServer = new File(path);
        } else {
            fileOnServer = createFileOnServer(path);
        }

        String[] convertedFileAndCecksumme = null;

        System.out.println("UG.getRemoteType() = " + UG.getRemoteType());
        System.out.println("fileOnServer.exists() = " + fileOnServer.exists() + ", fileOnServer = " + fileOnServer);

        if (fileOnServer.exists()) {

            convertedFileAndCecksumme = new String[2];

            try {
                convertedFileAndCecksumme[0] = IOUtil.fileToBase64(fileOnServer);
            } catch (IOException ex) {
                Logger.getLogger(RpcHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            convertedFileAndCecksumme[1] = IOUtil.generateMD5Sum(fileOnServer);
        }//fileOnServer.exists
        else {

            String msg = "RpcHandler.getFileWithChecksumme() : the specified path do NOT leed to an existing file."
                    + "path path of file: " + fileOnServer.getAbsolutePath();
            System.err.println(msg);
            throw new IOException(msg);
        }

        return convertedFileAndCecksumme;
    }

    /**
     Creates the corresponding path of a client file on the server side.
    
     @param clientPath the path of a file on client side
     @return the path of the file on server side
     */
    public String getServerPath(String clientPath) {
        show("getServerPath");
        System.out.println("clientPath = " +clientPath);
        
        File fileOnClient = new File(clientPath);
        File fileOnServer = VRL.getPropertyFolderManager().toLocalPathInTmpFolder(fileOnClient);
        System.out.println("fileOnServer = "+ fileOnServer);
        
        return fileOnServer.getAbsolutePath();
    }

    /**
     Creates a file on the server side depending on the client path of a file, 
     but with a corresponding path server.
    
     @param clientPath the path of a file on client side
     @return the created file on server side with server path
     @throws IOException if file could not be created
     */
    public File createFileOnServer(String clientPath) throws IOException {
        show("createFileOnServer");
        System.out.println("clientPath = " +clientPath);
        
        String pathOnServer = getServerPath(clientPath);
        File fileOnServer = new File(pathOnServer);
        fileOnServer.getParentFile().mkdirs();

        boolean parentFoldersExist = fileOnServer.getParentFile().exists();

        if (parentFoldersExist) {
            fileOnServer.createNewFile();
        }

        System.out.println("fileOnServer = " +fileOnServer);
        
        return fileOnServer;
    }

}
