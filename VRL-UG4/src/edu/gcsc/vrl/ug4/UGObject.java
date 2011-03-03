/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.types.VisualIDRequest;
import eu.mihosoft.vrl.visual.MessageType;
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
//    private transient Pointer exportedClassPointer;
    private String className;
    private ArrayList<String> classNames;
//    private transient ArrayList<UGObject> referenceList;

    protected void setThis(UGObject o) {
        System.out.println(className + ">> Set This: "
                + o.getClassName() + ", " + o.getPointer());
//        releaseAll();
        setPointer(o.getPointer());

//        if (referenceList == null) {
//            referenceList = new ArrayList<UGObject>();
//        }
//        if (o.referenceList != null) {
//            referenceList.addAll(o.referenceList);
//        }
        setClassName(o.getClassName());
        setClassNames(o.getClassNames());
    }

    /**
     * @return the pointer
     */
//    @MethodInfo(interactive = false)
    protected Pointer getPointer() {
        if (objPointer == null) {

            System.out.println("ClassName=" + getClassName());

            
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
    protected void setPointer(@ParamInfo(nullIsValid = true) Pointer pointer) {
        if (pointer != null) {
            this.objPointer = pointer;
            this.objPointer.setClassName(className);
            System.out.println(getClassName() + " >> SetPointer: "
                    + pointer.getClassName()
                    + " [" + pointer.getAddress() + "]");
        } else {
            System.out.println(getClassName() + " >> SetPointer: [null]");
        }

//        releaseReferences();
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

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    protected void setClassName(String className) {
        this.className = className;
    }

    /**
     * Adds a parameter pointer.
     * @param o pointer to add
     */
    @MethodInfo(noGUI = true)
    private void addReference(UGObject o) {
//        if (referenceList == null) {
//            referenceList = new ArrayList<UGObject>();
//        }
//        referenceList.add(o);
//        System.out.println(getClassName() + " >> Added: "
//                + o.getClassName() + " [" + o.getPointer().getAddress() + "]");
    }

    protected Object invokeMethod(boolean isFunction, boolean isConst,
            String methodName, Object[] params) {

        Object[] convertedParams = new Object[params.length];

        for (int i = 0; i < convertedParams.length; i++) {
            Object p = params[i];
            if (p instanceof UGObject) {
                UGObject o = (UGObject) p;
                convertedParams[i] = o.getPointer();
                addReference(o);
            } else {
                convertedParams[i] = p;
            }
        }

        Object result = null;

        if (isFunction) {
            result = edu.gcsc.vrl.ug4.UG4.getUG4().invokeFunction(
                    methodName, false, convertedParams);
        } else {
            result = edu.gcsc.vrl.ug4.UG4.getUG4().invokeMethod(getClassName(),
                    getPointer().getAddress(), isConst,
                    methodName, convertedParams);

            System.out.println("Pointer: " + getPointer());
        }

        System.out.println("Method Result(" + methodName + "): " + result);

        return result;
    }

//    protected abstract UGObject newInstance(Pointer p);
    /**
     * Releases pointer.
     */
    @MethodInfo(noGUI = true)
    public void releaseThis() {
        objPointer = null;
    }

//    @MethodInfo(noGUI = true)
//    public void releaseReferences() {
//        if (referenceList != null) {
//            System.out.println(getClassName() + " >> ReferenceList released! ");
//            referenceList.clear();
//        } else {
//            referenceList = new ArrayList<UGObject>();
//        }
//    }

//    /**
//     * Releases pointer.
//     */
//    @MethodInfo(noGUI = true)
//    public void releaseAll() {
//        releaseThis();
//        releaseReferences();
//    }

}
