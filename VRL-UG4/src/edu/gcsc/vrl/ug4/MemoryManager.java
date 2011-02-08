/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import java.util.HashMap;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MemoryManager {

//    private static HashMap<Long, Integer> references =
//            new HashMap<Long, Integer>();

    // no instanciation allowed
    private MemoryManager() {
        throw new AssertionError(); // not in this class either!
    }

    public static void deletePointer(Pointer p) {

        long ptr = p.getAddress();
        String className = p.getClassName();

        long exportedClassPtr = 0;

        if (className != null) {
            exportedClassPtr =
                    UG4.getUG4().getExportedClassPtrByName(className);
        }

        if (ptr != 0 && exportedClassPtr != 0) {
            System.out.println("Delete: " + className + " [" + ptr + "]");
            delete(ptr, exportedClassPtr);
        }
    }

//    public static void retain(Pointer p) {
//        Integer refs = references.get(p.getAddress());
//
//        if (refs != null) {
//            references.put(p.getAddress(), refs++);
//        }
//    }
//
//    public static void release(Pointer p) {
//
//        Integer refs = references.get(p.getAddress());
//
//        if (refs != null) {
//            if (refs > 0) {
//                references.put(p.getAddress(), refs--);
//            } else {
//                deletePointer(p);
//            }
//        }
//    }

    native static void delete(long objPtr, long exportedClassPtr);
}
