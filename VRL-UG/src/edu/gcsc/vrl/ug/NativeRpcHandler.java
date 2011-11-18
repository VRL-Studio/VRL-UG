/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import edu.gcsc.vrl.ug.NativeAPIInfo;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
abstract class NativeRpcHandler {
    
    // ********************************************
    // ************** NATIVE METHODS **************
    // ********************************************
    abstract   NativeAPIInfo convertRegistryInfo();

    abstract Object invokeMethod(
            String exportedClassName,
            long objPtr, boolean readOnly,
            String methodName, Object[] params);

    abstract long newInstance(long exportedClassPtr, Object[] parameters);

    abstract long getExportedClassPtrByName(String name, boolean classGrp);

    abstract String getDefaultClassNameFromGroup(String grpName);

    abstract Object invokeFunction(String name,
            boolean readOnly, Object[] params);

    abstract String getSvnRevision();

    abstract String getDescription();

    abstract String getAuthors();

    abstract String getCompileDate();
    
    
    abstract int ugInit(String[] args);
    
    /**
     * Deallocates specified memory. The destructor of the specified class
     * will be called.
     * @param objPtr object pointer
     * @param exportedClassPtr pointer of the exported class
     */
    @Deprecated
    abstract void delete(long objPtr, long exportedClassPtr);

    /**
     * Invalidates the specified smart pointer.
     * @param p smart-pointer to invalidate
     */
    abstract void invalidate(SmartPointer p);
    
}
