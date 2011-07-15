/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import java.util.ArrayList;

/**
 * This interface defines the methods that can be called on a UG wrapper
 * object.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public interface UGObjectInterface {

    /**
     * Returns the class name of this object.
     * @return the class name of this object
     */
    String getClassName();

    /**
     * Returns the names of the base classes of this object.
     * @return the names of the base classes of this object
     */
    ArrayList<String> getClassNames();
 
    /**
     * Releases pointer.
     */
    @MethodInfo(noGUI = true)
    void releaseThis();

    /**
     * Defines the names of the base classes of this object
     * @param classNames the class names to set
     */
    void setClassNames(ArrayList<String> classNames);

    /**
     * Defines the VRL canvas this object belongs to. This method is
     * called automatically by VRL.
     * @param mainCanvas VRL canvas to set
     */
    @MethodInfo(noGUI = true, callOptions = "assign-canvas")
    void setMainCanvas(VisualCanvas mainCanvas);

    /**
     * Returns the VRL canvas that visualizes this object.
     * @return the VRL canvas that visualizes this object
     */
    @MethodInfo(noGUI = true)
    VisualCanvas getMainCanvas();
}
