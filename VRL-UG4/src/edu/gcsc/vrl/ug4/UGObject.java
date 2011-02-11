/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.types.VisualIDRequest;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UGObject implements Serializable, UGObjectInterface {

    private static final long serialVersionUID = 1L;
    private transient VisualCanvas mainCanvas;
    private transient Pointer objPointer;
    private transient Pointer exportedClassPointer;
    private String className;
    private ArrayList<String> classNames;
    private transient ArrayList<Pointer> pointerList;

    /**
     * @return the pointer
     */
//    @MethodInfo(interactive = false)
    public Pointer getPointer() {
        if (objPointer == null) {
            long address = (long) edu.gcsc.vrl.ug4.UG4.getUG4().
                    newInstance(
                    edu.gcsc.vrl.ug4.UG4.getUG4().
                    getExportedClassPtrByName(getClassName()));
            setPointer(new edu.gcsc.vrl.ug4.Pointer(getClassName(), address));
            System.out.println(getClassName() + " >> New Instance: "
                    + getClassName() + " [" + address + "]");
        }
        return objPointer;
    }

    @MethodInfo(noGUI = true, callOptions = "assign-canvas")
    public void setMainCanvas(VisualCanvas mainCanvas) {
        this.mainCanvas = mainCanvas;
    }

    /**
     * @param pointer the pointer to set
     */
//    @MethodInfo(interactive = false)
    public void setPointer(@ParamInfo(nullIsValid = true) Pointer pointer) {
        if (pointer != null) {
            this.objPointer = pointer;
            System.out.println(getClassName() + " >> SetPointer: "
                    + pointer.getClassName()
                    + " [" + pointer.getAddress() + "]");
        } else {
            System.out.println(getClassName() + " >> SetPointer: [null]");
        }

        releasePointerList();
    }

    /**
     * @return the classNames
     */
    public ArrayList<String> getClassNames() {
        return classNames;
    }

    /**
     * @param classNames the classNames to set
     */
    public void setClassNames(ArrayList<String> classNames) {
        this.classNames = classNames;
    }

    public Pointer getExportedClassPointer() {
        if (exportedClassPointer == null) {
            exportedClassPointer =
                    new Pointer(null,
                    UG4.getUG4().getExportedClassPtrByName(getClassName()));
        }
        return exportedClassPointer;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Invokes <code>setPopinter()</code> and <code>getPointer()</code> methods
     * from GUI.
     */
    public void updatePointer(VisualIDRequest visualID) {
        System.out.println(getClassName() + " >> UpdatePointer:");
        if (visualID != null) {
            mainCanvas.getInspector().invokeFromGUI(
                    this, visualID.getID(), "setPointer", Pointer.class);
            mainCanvas.getInspector().invokeFromGUI(
                    this, visualID.getID(), "getPointer");
        }
    }

    /**
     * Adds a parameter pointer.
     * @param p pointer to add
     */
    @MethodInfo(noGUI = true)
    public void addPointer(Pointer p) {
        if (pointerList == null) {
            pointerList = new ArrayList<Pointer>();
        }
        pointerList.add(p);
        System.out.println(getClassName() + " >> Added: "
                + p.getClassName() + " [" + p.getAddress() + "]");
    }

    /**
     * Releases pointer.
     */
    @MethodInfo(noGUI = true)
    public void releaseThis() {
        objPointer = null;
    }

    @MethodInfo(noGUI = true)
    public void releasePointerList() {
        if (pointerList != null) {
            System.out.println(getClassName() + " >> PointerList released! ");
            pointerList.clear();
        }
    }

    /**
     * Releases pointer.
     */
    @MethodInfo(noGUI = true)
    public void releaseAll() {
        releaseThis();
        releasePointerList();
    }
}
