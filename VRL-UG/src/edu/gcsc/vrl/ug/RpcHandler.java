package edu.gcsc.vrl.ug;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import eu.mihosoft.vrl.io.Base64;
import java.io.Serializable;
import java.util.List;

/*
 * NOTICE:
 *
 * ALL METHODS need to RETURN one of the following types:
 *
 * XML-RPC type Simplest Java type More complex Java type
 *
 * i4 int java.lang.Integer int int java.lang.Integer boolean boolean
 * java.lang.Boolean string java.lang.String java.lang.String double double
 * java.lang.Double
 *
 * dateTime.iso8601 java.util.Date java.util.Date struct java.util.Hashtable
 * java.util.Hashtable array java.util.Vector java.util.Vector base64 byte[]
 * byte[]
 *
 * nil (extension) null null
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
    public int clearMessages()
    {
//        show("clearMessages");
        
        StringBuilder messages = getServer().getMessages();
        
        if (messages.length() > 0) {
            messages.delete(0, messages.length());
            //messages = new StringBuilder();
        }
        
        return 0;
    }
}
