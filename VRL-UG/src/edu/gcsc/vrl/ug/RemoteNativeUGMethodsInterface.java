/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
 interface RemoteNativeUGMethodsInterface extends Remote {

    // ********************************************
    // ************** NATIVE METHODS **************
    // ********************************************
    NativeAPIInfo convertRegistryInfo();

    Object invokeMethod(
            String exportedClassName,
            long objPtr, boolean readOnly,
            String methodName, Object[] params) throws RemoteException;

    long newInstance(long exportedClassPtr, Object[] parameters)
            throws RemoteException;

    long getExportedClassPtrByName(String name, boolean classGrp)
            throws RemoteException;

    String getDefaultClassNameFromGroup(String grpName)
            throws RemoteException;

    Object invokeFunction(String name,
            boolean readOnly, Object[] params) throws RemoteException;

    String getSvnRevision() throws RemoteException;

    String getDescription() throws RemoteException;

    String getAuthors() throws RemoteException;

    String getCompileDate() throws RemoteException;

    //
    // static native methods from here
    //
    int ugInit(String[] args) throws RemoteException;

    /**
     * Deallocates specified memory. The destructor of the specified class
     * will be called.
     * @param objPtr object pointer
     * @param exportedClassPtr pointer of the exported class
     */
    @Deprecated
    void delete(long objPtr, long exportedClassPtr) throws RemoteException;

    /**
     * Invalidates the specified smart pointer.
     * @param p smart-pointer to invalidate
     */
    void invalidate(SmartPointer p) throws RemoteException;
}
