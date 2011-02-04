/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MemoryManager {

    // no instanciation allowed
    private MemoryManager() {
        throw new AssertionError(); // not in this class either!
    }

    public static void deletePointer(long ptr, String className) {
        
        delete(ptr,UG4.getUG4().getExportedClassPtrByName(className));
    }

    native static void delete(long objPtr, long exportedClassPtr);
}
