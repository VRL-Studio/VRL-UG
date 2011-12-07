package edu.gcsc.vrl.ug;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import eu.mihosoft.vrl.io.Base64;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import sun.misc.BASE64Encoder;

/**
 * NOTICE: 
 * 
 * ALL METHODS need to RETURN one of the following types:
 * 
 * XML-RPC type      Simplest Java type     More complex Java type
 * 
 * i4                int                    java.lang.Integer
 * int               int                    java.lang.Integer
 * boolean           boolean                java.lang.Boolean
 * string            java.lang.String       java.lang.String
 * double            double                 java.lang.Double
 * 
 * dateTime.iso8601  java.util.Date         java.util.Date
 * struct            java.util.Hashtable    java.util.Hashtable
 * array             java.util.Vector       java.util.Vector
 * base64            byte[]                 byte[]
 * 
 * nil (extension)   null                   null
 *
 * ATTENTION: void is NOT valid !!!
 * 
 * 
 * The PpcHandler methods were executed on the Server.
 * So result came from server calculation.
 * 
 * But the parameters of the methods are filed from the clients.
 * 
 * So RpcHandler is more a part of the server, but the clients
 * need to know the interface of this class (the method-heads).
 * 
 * @author christianpoliwoda
 */
public class RpcHandler {

    static String message = "first call";
//    private static UG server = null;
    private static final UG server = UG.getInstance(null, RemoteType.SERVER);

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
    public final Object convertRegistryInfo() {
        show("convertRegistryInfo");

        NativeAPIInfo napiInfo=server._convertRegistryInfo();
        
//        Base64.encodeObject(napiInfo);
        
        return napiInfo;
    }

    public Object invokeMethod(
            String exportedClassName, long objPtr, boolean readOnly,
            String methodName, Object[] params) {
        show("invokeMethod");

        return server._invokeMethod(
                exportedClassName, objPtr, readOnly, methodName, params);
    }

    public long newInstance(long exportedClassPtr, Object[] parameters) {
        show("newInstance");

        return server._newInstance(exportedClassPtr, parameters);
    }

    public long getExportedClassPtrByName(String name, boolean classGrp) {
        show("getExportedClassPtrByName");

        return server._getExportedClassPtrByName(name, classGrp);
    }

    public String getDefaultClassNameFromGroup(String grpName) {
        show("getDefaultClassNameFromGroup");

        return server._getDefaultClassNameFromGroup(grpName);
    }

    public Object invokeFunction(String name, boolean readOnly, Object[] params) {
        show("invokeFunction");

        return server._invokeFunction(name, readOnly, params);
    }

    public String getSvnRevision() {
        show("getSvnRevision");

        return server._getSvnRevision();
    }

    public String getDescription() {
        show("getDescription");

        return server._getDescription();
    }

    public String getAuthors() {
        show("getAuthors");

        return server._getAuthors();
    }

    public String getCompileDate() {
        show("getCompileDate");

        return server._getCompileDate();
    }

    public Integer ugInit(List<String> args) {
        show("#### " + this.getClass().getName() + " ugInit( String[] args )");

        String[] argsArray = new String[args.size()];

        argsArray = args.toArray(argsArray);

//        return Integer.valueOf(456);
        return server._ugInit(argsArray);
    }
//    //TEST DUMMY FUNCTION
//    public Integer ugInit() {
//        show("**** "+this.getClass().getName() +" ugInit( void )");
//
//        return Integer.valueOf(123);
//    }

    /**
     * Deallocates specified memory. The destructor of the specified class
     * will be called.
     * @param objPtr object pointer
     * @param exportedClassPtr pointer of the exported class
     */
    @Deprecated
    public boolean delete(long objPtr, long exportedClassPtr) {
        show("delete");

        server._delete(objPtr, exportedClassPtr);

        return true;
    }

    /**
     * Invalidates the specified smart pointer.
     * @param o smart-pointer as Object to invalidate
     */
    public boolean invalidate(Object o) {
        show("invalidate");

        if (o instanceof SmartPointer) {
            SmartPointer p = (SmartPointer) o;

            server._invalidate(p);

            return true;
        }

        return false;
    }
}
