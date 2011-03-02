/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.types.VisualIDRequest;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public interface UGObjectInterface {

    /**
     * @return the className
     */
    String getClassName();

    /**
     * @return the classNames
     */
    ArrayList<String> getClassNames();

//    Pointer getExportedClassPointer();

    /**
     * @return the pointer
     */
//    Pointer getPointer();




    /**
     * Releases pointer.
     */
//    @MethodInfo(noGUI = true)
//    void releaseAll();

//    @MethodInfo(noGUI = true)
//    void releaseReferences();



    
    /**
     * Releases pointer.
     */
    @MethodInfo(noGUI = true)
    void releaseThis();

    /**
     * @param className the className to set
     */
//    void setClassName(String className);

    /**
     * @param classNames the classNames to set
     */
    void setClassNames(ArrayList<String> classNames);

    @MethodInfo(noGUI = true, callOptions = "assign-canvas")
    void setMainCanvas(VisualCanvas mainCanvas);

//    /**
//     * @param pointer the pointer to set
//     */
//    void setPointer(@ParamInfo(nullIsValid = true)
//    Pointer pointer);

//    /**
//     * Invokes <code>setPopinter()</code> and <code>getPointer()</code> methods
//     * from GUI.
//     */
//    void updatePointer(VisualIDRequest visualID);

}
