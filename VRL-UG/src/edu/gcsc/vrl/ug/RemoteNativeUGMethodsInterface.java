/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;


/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
 interface RemoteNativeUGMethodsInterface {

    // ********************************************
    // ************** NATIVE METHODS **************
    // ********************************************
    NativeAPIInfo convertRegistryInfo();

    Object invokeMethod(
            String exportedClassName,
            long objPtr, boolean readOnly,
            String methodName, Object[] params);

    long newInstance(long exportedClassPtr, Object[] parameters);

    long getExportedClassPtrByName(String name, boolean classGrp);

    String getDefaultClassNameFromGroup(String grpName);

    Object invokeFunction(String name,
            boolean readOnly, Object[] params);

    String getSvnRevision();

    String getDescription();

    String getAuthors();

    String getCompileDate();

    //
    // static native methods from here
    //
    int ugInit(String[] args);

    /**
     * Deallocates specified memory. The destructor of the specified class
     * will be called.
     * @param objPtr object pointer
     * @param exportedClassPtr pointer of the exported class
     */
    @Deprecated
    void delete(long objPtr, long exportedClassPtr);

    /**
     * Invalidates the specified smart pointer.
     * @param p smart-pointer to invalidate
     */
    void invalidate(SmartPointer p);
}
