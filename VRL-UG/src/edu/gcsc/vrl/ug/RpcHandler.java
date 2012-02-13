package edu.gcsc.vrl.ug;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import eu.mihosoft.vrl.io.Base64;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
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
 *
 *
 * The PpcHandler methods were executed on the Server. So result came from
 * server calculation.
 *
 * But the parameters of the methods are filed from the clients.
 *
 * So RpcHandler is more a part of the server, but the clients need to know the
 * interface of this class (the method-heads).
 *
 * @author christianpoliwoda
 */
public class RpcHandler {

    static String message = "first call";
    private static UG server = null;

    /**
     * @return the server
     */
    public static UG getServer() {
        return server;
    }

    /**
     * @param aServer the server to set
     */
    public static void setServer(UG aServer) {
        server = aServer;
    }

//    //TEST FUNCTION
    public int show(String message) {
        System.out.println("RpcHandler.show: " + message);
        return 1;
    }

//    //TEST FUNCTION
    public int showMessage() {
        System.out.println("RpcHandler.showMessage: " + message);
        return 1;
    }
//    //TEST FUNCTION

    public int changeMessage(String message) {
        System.out.println("RpcHandler.changeMessage: " + "old: " + RpcHandler.message);

        RpcHandler.message = message;

        System.out.println("RpcHandler.changeMessage: " + "new: " + RpcHandler.message);

        return 2;
    }

    // ********************************************
    // ************** NATIVE METHODS **************
    // ********************************************
//    public final NativeAPIInfo convertRegistryInfo() {
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

    public String invokeMethod(
            String exportedClassName, String objPtr, boolean readOnly,
            String methodName, String params) {
        show("invokeMethod");

        Object o = Base64.decodeToObject(params);

        Object[] objArray = (Object[]) o;

        o = getServer()._invokeMethod(
                exportedClassName, new Long(objPtr), readOnly, methodName, objArray);

        //assumption object is serializable
        String base64 = Base64.encodeObject((Serializable) o);

        System.out.println("encoded Object = " + base64);
        return base64;
    }

    public String newInstance(String exportedClassPtr, String parameters) {
        show("newInstance");

        Object o = Base64.decodeToObject(parameters);
        Object[] objArray = (Object[]) o;

        Pointer p = getServer()._newInstance(new Long(exportedClassPtr), objArray);

        String base64 = Base64.encodeObject((Serializable) p);

        System.out.println("encoded Pointer = " + base64);
        return base64;
    }

    public String getExportedClassPtrByName(String name, boolean classGrp) {
        show("getExportedClassPtrByName");

        long result = getServer()._getExportedClassPtrByName(name, classGrp);

        System.out.println("result =" + result);
        return String.valueOf(result);
    }

    public String getDefaultClassNameFromGroup(String grpName) {
        show("getDefaultClassNameFromGroup");

        String result = getServer()._getDefaultClassNameFromGroup(grpName);

        System.out.println("result =" + result);
        return result;
    }

    public String invokeFunction(String name, boolean readOnly, Object[] params) {
        show("invokeFunction");

        Object o = getServer()._invokeFunction(name, readOnly, params);

        //assumption all containing objects are serializable
        String base64 = Base64.encodeObject((Serializable) o);

        System.out.println("RESULT: " + base64);
        return base64;


    }

    public String getSvnRevision() {
        show("getSvnRevision");

        String result = getServer()._getSvnRevision();

        System.out.println("result =" + result);
        return result;
    }

    public String getDescription() {
        show("getDescription");

        String result = getServer()._getDescription();

        System.out.println("result =" + result);
        return result;
    }

    public String getAuthors() {
        show("getAuthors");

        String result = getServer()._getAuthors();

        System.out.println("result =" + result);
        return result;

    }

    public String getCompileDate() {
        show("getCompileDate");

        String result = getServer()._getCompileDate();

        System.out.println("result =" + result);
        return result;
    }

    public Integer ugInit(List<String> args) {
        show("#### " + this.getClass().getName() + " ugInit( String[] args )");

        String[] argsArray = new String[args.size()];

        argsArray = args.toArray(argsArray);

//        return Integer.valueOf(456);
        return getServer()._ugInit(argsArray);
    }

    /**
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
     * Stops the web server in the local JVM, where UG runs in server mode.
     */
    public int stopWebServer() {
//        System.out.println("START "+RpcHandler.class.getName() + ".stopServer()");

        server.stopWebServer();
//        System.out.println("END "+RpcHandler.class.getName() + ".stopServer()");

        return 0;
    }

    /**
     * Tries to connect to server and returns true if a connection could be
     * established.
     */
    public boolean isServerRunning() {
        Boolean b = UG.isServerRunning();

        if (b != null && b.booleanValue()) {
            return true;
        }
        return false;
    }
}
