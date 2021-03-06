/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2012–2018 Goethe Universität Frankfurt am Main, Germany
 * 
 * This file is part of Visual Reflection Library (VRL).
 *
 * VRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/VRL/src/eu/mihosoft/vrl/resources/license/lgplv3.txt
 *
 * VRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of VRL includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of VRL. Neither the VRL Canvas attribution icon nor any
 * copyright statement/attribution may be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do three things regarding copyright
 * notice and author attribution.
 *
 * First, the following text must be displayed on the Canvas:
 * "based on VRL source code". In this case the VRL canvas icon must be removed.
 * 
 * Second, the copyright notice must remain. It must be reproduced in any
 * program that uses VRL.
 *
 * Third, add an additional notice, stating that you modified VRL. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "VRL source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * M. Hoffer, C.Poliwoda, G.Wittum. Visual Reflection Library -
 * A Framework for Declarative GUI Programming on the Java Platform.
 * Computing and Visualization in Science, 2011, in press.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import edu.gcsc.vrl.ug.remote.JVMmanager;
import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    private String classGroupName;
    private ArrayList<String> classNames;
    private boolean isInstantiable;
    private boolean isClassGroupObject;
    private transient Object[] constructorParameters;
    //
    //DEBUG
    public static long counter = 0;
    private static boolean DEBUG;

    public static void enableDebug() {
        DEBUG = true;
    }

    public static void disableDebug() {
        DEBUG = false;
    }

    public UGObject() {
//
//        System.out.println(" #+#+#+# UGObject()");
//        counter++;
//        System.out.println("counter = " + counter);

        JVMmanager.addUGObjectToWeakReferences(this);
    }

    protected void setThis(UGObject o) {
//        System.out.println(className + ">> Set This: "
//                + o.getClassName() + ", " + o.getPointer());
        setPointer(o.getPointer());
        setClassName(o.getClassName());
        setClassNames(o.getClassNames());
        setClassGroupObject(o.isClassGroupObject());
        setClassGroupName(o.getClassGroupName());
    }

    private void updateClassNameIfClassGroupObject() {
        if (isClassGroupObject()) {
            if (DEBUG) {
                System.out.println(">>> class-group-name: " + getClassGroupName());
            }
            long exportedClsPtr = Pointer.NULL;
            exportedClsPtr = edu.gcsc.vrl.ug.UG.getInstance().
                    getExportedClassPtrByName(getClassGroupName(),
                            isClassGroupObject());

            if (exportedClsPtr != Pointer.NULL) {
                setClassName(edu.gcsc.vrl.ug.UG.getInstance().
                        getDefaultClassNameFromGroup(getClassGroupName()));
            }
            if (DEBUG) {
                System.out.println(">>> class-name: " + getClassName());
            }
            if (getClassGroupName().equals(getClassName())) {
                throw new IllegalStateException(
                        "class name '" + getClassName() + "'"
                        + " equals group name '" + getClassGroupName() + "'");
            }
        } else {
            if (DEBUG) {
                System.out.println(">>> no class-group: " + getClassName());
            }
        }
    }

    /**
     * Returns a pointer to this object.
     *
     * @return a pointer to this object
     */
    protected Pointer getPointer() {
        if (objPointer == null) {

            long exportedClsPtr = Pointer.NULL;

            if (isClassGroupObject()) {

                exportedClsPtr = edu.gcsc.vrl.ug.UG.getInstance().
                        getExportedClassPtrByName(getClassGroupName(),
                                isClassGroupObject());
            } else {
                exportedClsPtr = edu.gcsc.vrl.ug.UG.getInstance().
                        getExportedClassPtrByName(getClassName(),
                                isClassGroupObject());
            }

            if (isClassGroupObject() && exportedClsPtr != Pointer.NULL) {
//                setClassGroupName(className);
                setClassName(edu.gcsc.vrl.ug.UG.getInstance().
                        getDefaultClassNameFromGroup(getClassGroupName()));
            }

//            System.out.println("ClassName=" + getClassName());
            if (isInstantiable()) {
                if (exportedClsPtr == Pointer.NULL) {
//                    System.err.println(
//                            "Class \"" + getClassName()
//                            + "\" is not instantiable!");

//                    getMainCanvas().getMessageBox().addMessage(
//                            "Cannot instantiate class:",
//                            "Class \"" + getClassName()
//                            + "\" is not instantiable!",
//                            MessageType.ERROR);
                    String msg = "Class \"" + getClassName()
                            + "\" is not instantiable!";

                    if (isClassGroupObject()) {
                        msg += " Possible reason: no default class in class group!";
                    }

                    throw new IllegalStateException(msg);
                } else {

                    if (constructorParameters == null) {
                        constructorParameters = new Object[0];
                    }

//                    long address = (long) edu.gcsc.vrl.ug.UG.getInstance().
//                            newInstance(exportedClsPtr, constructorParameters);
//                    
//                    setPointer(new edu.gcsc.vrl.ug.Pointer(
//                            getClassName(), address, false));
//                    System.out.println(getClassName() + " >> New Instance: "
//                            + getClassName() + " [" + address + "]");
                    Pointer p = edu.gcsc.vrl.ug.UG.getInstance().
                            newInstance(exportedClsPtr, constructorParameters);

                    p.setClassName(getClassName());

                    setPointer(p);
                }
            }
        }
        if (objPointer == null && !isInstantiable()) {
//            System.err.println(
//                    "Class \"" + getClassName()
//                    + "\" is not instantiable via default constructor!");
//            
//            getMainCanvas().getMessageBox().addMessage(
//                    "Cannot instantiate class:",
//                    "Class \"" + getClassName()
//                    + "\" is not instantiable via default constructor!",
//                    MessageType.ERROR);

            throw new IllegalStateException("Class \"" + getClassName()
                    + "\" is not instantiable via selected constructor!");
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

            updateClassNameIfClassGroupObject();

            this.objPointer.setClassName(className);
//            System.out.println(getClassName() + " >> SetPointer: "
//                    + pointer.getClassName()
//                    + " [" + pointer.getAddress() + "]");            
        } else {
//            System.out.println(getClassName() + " >> SetPointer: [null]");
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
     * Invokes a native function.
     *
     * @param function function name
     * @param params method parameters
     * @return return value
     */
    protected static Object invokeFunction(
            String function, Object[] params) {

        Object[] convertedParams = convertParams(null, params, function);

        return edu.gcsc.vrl.ug.UG.getInstance().invokeFunction(
                function, false, convertedParams);
    }

    /**
     * Converts the specified parameters, i.e., if UGObjects are specified, they
     * will be converted to their internal pointer object.
     *
     * @param params parameters to convert
     * @return converted parameters
     */
    private static Object[] convertParams(UGObject obj,
            Object[] params, String methodName) {
        Object[] convertedParams = new Object[params.length];

        for (int i = 0; i < convertedParams.length; i++) {
            Object p = params[i];

            if (p == null) {

                String methodPrefix = "";

                if (obj != null) {
                    methodPrefix = obj.getClassName()
                            + ".";
                }

                throw new IllegalArgumentException(
                        "Method: \"" + methodPrefix + methodName
                        + "(): parameter " + i + " == NULL");
            }

            if (p instanceof UGObject) {
                UGObject o = (UGObject) p;
                convertedParams[i] = o.getPointer();
            }
            else if (p instanceof UGObject[]) {
                UGObject[] o = (UGObject[]) p;
                Pointer[] pa = new Pointer[o.length];
                for (int j = 0; j < o.length; ++j)
                    pa[j] = o[j].getPointer();
                convertedParams[i] = pa;
            }
            else {
                convertedParams[i] = p;
            }
        }

        return convertedParams;
    }

    /**
     * Invokes a native method.
     *
     * @param isConst defines whether to call a const method
     * @param methodName method name
     * @param params method parameters
     * @return return value
     */
    protected Object invokeMethod(boolean isConst,
            String methodName, Object[] params) {

        Object[] convertedParams = convertParams(this, params, methodName);

        Object result = null;

        if (getPointer() != null) {

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
     * This method will save the constructor parameters.
     * The actual construction is not performed until any method of this object
     * is called (then construction takes place in getPointer()).
     *
     * @param params method parameters
     */
    protected void invokeConstructor(Object[] params) {
        constructorParameters = convertParams(this, params, "constructor");

        releaseThis();   
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
     *
     * @param o object to check
     * @return <code>true</code> if the specified object is an ug object;
     * <code>false</code> otherwise
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

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (isParameterObject()) {
            return;
        } else {
            out.defaultWriteObject();
        }
    }

    protected boolean isParameterObject() {
        return getMainCanvas() == null;
    }

    /**
     * @param isClassGroupObject the isClassGroupObject to set
     */
    protected final void setClassGroupObject(boolean isClassGroupObject) {
        this.isClassGroupObject = isClassGroupObject;
    }

    /**
     * @return the classGroupName
     */
    public final String getClassGroupName() {
        return classGroupName;
    }

    /**
     * @param classGroupName the classGroupName to set
     */
    protected final void setClassGroupName(String classGroupName) {
        this.classGroupName = classGroupName;
    }
}
