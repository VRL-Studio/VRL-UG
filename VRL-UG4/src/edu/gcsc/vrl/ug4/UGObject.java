/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UGObject {

    private transient Pointer pointer;
    private String className;
    private ArrayList<String> classNames;

    /**
     * @return the pointer
     */
    public Pointer getPointer() {
        return pointer;
    }

    /**
     * @param pointer the pointer to set
     */
    public void setPointer(Pointer pointer) {
        this.pointer = pointer;
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
        return new Pointer(
                UG4.getUG4().getExportedClassPtrByName(getClassName()));
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
}
