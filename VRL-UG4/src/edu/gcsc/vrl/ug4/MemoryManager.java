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

        long exportedClassPtr = 0;

        if (className != null) {
            UG4.getUG4().getExportedClassPtrByName(className);
        }

        if (ptr != 0 && exportedClassPtr != 0) {
            delete(ptr, exportedClassPtr);
        }
    }

    native static void delete(long objPtr, long exportedClassPtr);
}
