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
 * The RpcHandler is the interface for a webserver which allows UG to act
 * as a server, if the webserver is started in an UG instance with RemoteType SERVER.
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

    static String message = "first call";
    private static UG server = null;

    /**
     * @return the UG instance with RemoteType SERVER
     */
    public static UG getServer() {
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
        System.out.println("RpcHandler." + methodName+"() invoked.");
        return 1;
    }

////    //TEST FUNCTION
//    public int showMessage() {
//        System.out.println("RpcHandler.showMessage: " + message);
//        return 1;
//    }

////    //TEST FUNCTION
//    public int changeMessage(String message) {
//        System.out.println("RpcHandler.changeMessage: " + "old: " + RpcHandler.message);
//
//        RpcHandler.message = message;
//
//        System.out.println("RpcHandler.changeMessage: " + "new: " + RpcHandler.message);
//
//        return 2;
//    }

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

        NativeAPIInfo napiInfo = getServer()._convertRegistryInfo();

        if (napiInfo == null) {
            System.err.println("NativeAPIInfo IS NULL");
        }

        String base64 = Base64.encodeObject(napiInfo);

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
    public String invokeMethod(
            String exportedClassName, String objPtr, boolean readOnly,
            String methodName, String params) {
        show("invokeMethod");
        
        
        System.out.println("RpcHandler.invokeMethod() paramas values are:");
        System.out.println("exportedClassName = " + exportedClassName);
        System.out.println("objPtr = " + objPtr);
        System.out.println("readOnly = " + readOnly);
        System.out.println("methodName = " + methodName);
        System.out.println("params = " + params);
        

        Object o = Base64.decodeToObject(params);

        Object[] objArray = (Object[]) o;

        o = getServer()._invokeMethod(
                exportedClassName, new Long(objPtr), readOnly, methodName, objArray);

        //assumption object is serializable
        String base64 = Base64.encodeObject((Serializable) o);

        System.out.println("encoded Object = " + base64);
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

        Object o = Base64.decodeToObject(parameters);
        Object[] objArray = (Object[]) o;

        Pointer p = getServer()._newInstance(new Long(exportedClassPtr), objArray);

        String base64 = Base64.encodeObject((Serializable) p);

        System.out.println("encoded Pointer = " + base64);
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

        System.out.println("result =" + result);
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

        System.out.println("result =" + result);
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

        System.out.println("RESULT: " + base64);
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

        System.out.println("result =" + result);
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

        System.out.println("result =" + result);
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

        System.out.println("result =" + result);
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

        System.out.println("result =" + result);
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

        Object o = Base64.decodeToObject(base64);

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
     * @return the result of the on server executed method
     *
     * Stops the web server in the server JVM, where UG runs with RemoteType server.
     */
    public int stopWebServer() {

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
        System.out.println("RpcHandler.isServerRunning()");
        
        Boolean b = UG.isServerRunning();
        
        System.out.println("UG.isServerRunning() = "+ b);

        if (b != null && b.booleanValue()) {
            return true;
        }
        return false;
    }
}
