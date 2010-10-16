/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UGObject implements Serializable{
    private static final long serialVersionUID=1L;

    private transient Pointer objPointer;
    private transient Pointer exportedClassPointer;
    private String className;
    private ArrayList<String> classNames;

    /**
     * @return the pointer
     */
    public Pointer getPointer() {
        if (objPointer == null) {
            long address = (long) edu.gcsc.vrl.ug4.UG4.getUG4().
                    newInstance(
                    edu.gcsc.vrl.ug4.UG4.getUG4().
                    getExportedClassPtrByName(getClassName()));
            setPointer(new edu.gcsc.vrl.ug4.Pointer(address));
        }
        return objPointer;
    }

    /**
     * @param pointer the pointer to set
     */
    public void setPointer(Pointer pointer) {
        this.objPointer = pointer;
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
                    new Pointer(
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
}
