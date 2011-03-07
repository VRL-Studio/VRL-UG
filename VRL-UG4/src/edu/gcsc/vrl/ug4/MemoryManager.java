/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.reflection.VisualCanvas;
import java.util.Collection;

/**
 * Memory manager for handling native memory allocation/deallocation.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MemoryManager {

    // no instanciation allowed
    private MemoryManager() {
        throw new AssertionError(); // not in this class either!
    }

    /**
     * Releases native pointer instances of all UG objects that are visualized
     * with the specified VRL canvas.
     * @param canvas canvas
     */
    public static void releaseAll(VisualCanvas canvas) {

        if (canvas != null) {
            Collection<Object> objects =
                    canvas.getInspector().
                    getObjects();

            for (Object o : objects) {
                if (o instanceof UGObject) {
                    UGObject obj = (UGObject) o;
                    obj.releaseThis();
                }
            }
        }
    }

    /**
     * Deallocates specified memory. The destructor of the specified class
     * will be called.
     * @param objPtr object pointer
     * @param exportedClassPtr pointer of the exported class
     */
    @Deprecated
    native static void delete(long objPtr, long exportedClassPtr);

    /**
     * Invalidates the specified smart pointer.
     * @param p smart-pointer to invalidate
     */
    native static void invalidate(SmartPointer p);
}
