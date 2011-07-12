/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

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
    private boolean isInstantiable;
    private boolean isClassGroupObject;

    protected void setThis(UGObject o) {
        System.out.println(className + ">> Set This: "
                + o.getClassName() + ", " + o.getPointer());
        setPointer(o.getPointer());
        setClassName(o.getClassName());
        setClassNames(o.getClassNames());
    }

    /**
     * Returns a pointer to this object.
     * @return a pointer to this object
     */
    protected Pointer getPointer() {
        if (objPointer == null && isInstantiable()) {

            long exportedClsPtr = edu.gcsc.vrl.ug.UG.getInstance().
                    getExportedClassPtrByName(getClassName(),
                    isClassGroupObject);
            
            if (isClassGroupObject() && exportedClsPtr != Pointer.NULL) {
                setClassName(edu.gcsc.vrl.ug.UG.getInstance().
                        getDefaultClassNameFromGroup(className));
            }
            
            System.out.println("ClassName=" + getClassName());

            if (exportedClsPtr == Pointer.NULL) {
                System.err.println(
                        "Class \"" + getClassName()
                        + "\" is not instantiable!");
                getMainCanvas().getMessageBox().addMessage(
                        "Cannot instantiate class:",
                        "Class \"" + getClassName()
                        + "\" is not instantiable!",
                        MessageType.ERROR);
            } else {
                long address = (long) edu.gcsc.vrl.ug.UG.getInstance().
                        newInstance(exportedClsPtr);
                setPointer(new edu.gcsc.vrl.ug.Pointer(
                        getClassName(), address, false));
                System.out.println(getClassName() + " >> New Instance: "
                        + getClassName() + " [" + address + "]");
            }

        } else if (objPointer == null && !isInstantiable()) {
            System.err.println(
                    "Class \"" + getClassName()
                    + "\" is not instantiable via default constructor!");
            getMainCanvas().getMessageBox().addMessage(
                    "Cannot instantiate class:",
                    "Class \"" + getClassName()
                    + "\" is not instantiable via default constructor!",
                    MessageType.ERROR);
        }
        return objPointer;
    }

    @MethodInfo(noGUI = true, callOptions = "assign-canvas")
    @Override
    public void setMainCanvas(VisualCanvas mainCanvas) {
        this.mainCanvas = mainCanvas;
    }

    @MethodInfo(noGUI = true)
    public VisualCanvas getMainCanvas() {
        return mainCanvas;
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
    }

    @Override
    public ArrayList<String> getClassNames() {
        return classNames;
    }

    @Override
    public void setClassNames(ArrayList<String> classNames) {
        this.classNames = classNames;
    }

    @Override
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
     * Invokes a native method.
     * @param isFunction defines whether to invoke a function
     * @param isConst defines whether to call a const method
     * @param methodName method name
     * @param params method parameters
     * @return return value
     */
    protected Object invokeMethod(boolean isFunction, boolean isConst,
            String methodName, Object[] params) {

        Object[] convertedParams = new Object[params.length];

        for (int i = 0; i < convertedParams.length; i++) {
            Object p = params[i];
            if (p instanceof UGObject) {
                UGObject o = (UGObject) p;
                convertedParams[i] = o.getPointer();
            } else {
                convertedParams[i] = p;
            }
        }

        Object result = null;

        if (isFunction) {
            result = edu.gcsc.vrl.ug.UG.getInstance().invokeFunction(
                    methodName, false, convertedParams);
        } else if (getPointer() != null) {

//            System.out.println("**CLS:" + getClassName());
//            System.out.println("**PTR:" + getPointer().getAddress());
//            System.out.println("**CONST:" + isConst);asFullClass
//            System.out.println("**M:" + methodName);
//            System.out.println("**P:" + convertedParams);

            result = edu.gcsc.vrl.ug.UG.getInstance().invokeMethod(
                    getClassName(),
                    getPointer().getAddress(), isConst,
                    methodName, convertedParams);
        } else {
            // cannot invoke method because instantiation is impossible
        }

        return result;
    }

    /**
     * Releases pointer.
     */
    @MethodInfo(noGUI = true)
    public void releaseThis() {
        objPointer = null;
    }

    /**
     * @return the isInstantiable
     */
    public boolean isInstantiable() {

        return isInstantiable;
    }

    /**
     * Indicates whether the specified object is an ug object.
     * @param o object to check
     * @return <code>true</code> if the specified object is an ug object;
     *         <code>false</code> otherwise
     */
    public static boolean isInstance(Object o) {
        boolean result = UGObject.class.isInstance(o);
        return result;
    }

    /**
     * @param isInstantiable the isInstantiable to set
     */
    protected final void setInstantiable(boolean isInstantiable) {
        this.isInstantiable = isInstantiable;
    }

    /**
     * @return the isClassGroupObject
     */
    public final boolean isClassGroupObject() {
        return isClassGroupObject;
    }

    /**
     * @param isClassGroupObject the isClassGroupObject to set
     */
    protected final void setClassGroupObject(boolean isClassGroupObject) {
        this.isClassGroupObject = isClassGroupObject;
    }
}
