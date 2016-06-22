/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2006–2012 by Michael Hoffer
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

import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.visual.MessageType;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class simplifies access to the Java Reflection API.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class UGAny implements UGObjectInterface, Serializable {

    private static final long serialVersionUID = 1L;
    private transient VisualCanvas mainCanvas;
    private UGObject reference;
    private boolean throwException = true;
    private boolean enableMessaging = true;

    /**
     * Constructor.
     * @param reference UG object to operate on
     */
    public UGAny(UGObject reference) {
        if (reference == null) {
            throw new IllegalArgumentException("References must not be null!");
        }
        this.reference = reference;
    }

    /**
     * Enables/disables exceptions related to the Java Reflection API.
     * @param throwExceptions state to set
     * @return reference to this Object
     */
    public UGAny enableExceptions(boolean throwExceptions) {
        this.throwException = throwExceptions;
        return this;
    }

    /**
     * Enables/disables VRL messaging for messages generated by this object.
     * @param enableMessaging state to set
     * @return reference to this Object
     */
    public UGAny enableMessaging(boolean enableMessaging) {
        this.enableMessaging = enableMessaging;
        return this;
    }

    @MethodInfo(noGUI = true, callOptions = "assign-canvas")
    public void setMainCanvas(VisualCanvas mainCanvas) {
        this.mainCanvas = mainCanvas;
    }

    /**
     * Invokes a UG method.
     * @param methodName method name
     * @param params method parameters
     * @return return value
     */
    public Object invokeUGMethod(String methodName, Object[] params) {

        Class<?>[] types = new Class<?>[params.length];

        String exceptionMessage = null;

        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                String methodString =
                        getMethodSignature(methodName, types, params);

                String messageString =
                        getClass().getName()
                        + ": >> method \""
                        + methodString + "\" invoked with ambiguous arguments!";

                exceptionMessage = messageString;
                System.err.println(messageString);

                Logger.getLogger(UGAny.class.getName()).
                        log(Level.SEVERE, messageString);
                break;
            }

            types[i] = params[i].getClass();
        }

        if (exceptionMessage != null
                && enableMessaging && mainCanvas != null) {
            mainCanvas.getMessageBox().
                    addMessage("Cannot invoke Method:",
                    exceptionMessage, MessageType.ERROR);
        }
        if (throwException && exceptionMessage != null) {
            throw new RuntimeException(exceptionMessage);
        }

        return invokeUGMethod(methodName, types, params);
    }

    /**
     * Invokes a UG method.
     * @param methodName method name
     * @param types parameter types
     * @param params method parameters
     * @return return value
     */
    public Object invokeUGMethod(
            String methodName, Class<?>[] types, Object[] params) {
        Object result = null;
        Method method = null;

        String exceptionMessage = null;

        try {
            method = reference.getClass().getMethod(methodName, types);
        } catch (NoSuchMethodException ex) {

            String methodString = getMethodSignature(methodName, types, params);

            String messageString =
                    getClass().getName()
                    + ": >> method \"" + methodString + "\" not available!";

            exceptionMessage = messageString;

            System.err.println(messageString);

            Logger.getLogger(
                    UGAny.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {

            String methodString = getMethodSignature(methodName, types, params);
            String messageString =
                    getClass().getName()
                    + ": >> method \"" + methodString
                    + "\" cannot be invoked. Security exception has been thrown!";
            exceptionMessage = messageString;
            Logger.getLogger(
                    UGAny.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (exceptionMessage != null
                    && enableMessaging && mainCanvas != null) {
                mainCanvas.getMessageBox().
                        addMessage("Cannot invoke Method:",
                        exceptionMessage, MessageType.ERROR);
            }
            if (throwException && exceptionMessage != null) {
                throw new RuntimeException(exceptionMessage);
            }
        }

        if (method != null) {
            try {
                result = method.invoke(reference, params);
            } catch (IllegalAccessException ex) {

                String methodString =
                        getMethodSignature(methodName, types, params);

                String messageString =
                        getClass().getName()
                        + ": >> method \""
                        + methodString + "\" cannot be accessed!";

                exceptionMessage = messageString;

                System.err.println(messageString);

                Logger.getLogger(UGAny.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                String methodString =
                        getMethodSignature(methodName, types, params);

                String messageString =
                        getClass().getName()
                        + ": >> method \""
                        + methodString + "\" invoked with illegal arguments!";

                exceptionMessage = messageString;
                System.err.println(messageString);

                Logger.getLogger(UGAny.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                String methodString =
                        getMethodSignature(methodName, types, params);

                String messageString =
                        getClass().getName()
                        + ": >> method \""
                        + methodString + "\" cannot be invoked!";

                exceptionMessage = messageString;

                System.err.println(messageString);

                Logger.getLogger(UGAny.class.getName()).
                        log(Level.SEVERE, null, ex);
            } finally {
                if (exceptionMessage != null
                        && enableMessaging && mainCanvas != null) {
                    mainCanvas.getMessageBox().
                            addMessage("Cannot invoke Method:",
                            exceptionMessage, MessageType.ERROR);
                }
                if (throwException && exceptionMessage != null) {
                    throw new RuntimeException(exceptionMessage);
                }
            }
        }

        return result;
    }

    /**
     * Returns the method signature.
     * @param methodName mehtod name
     * @param types parameter types
     * @param params method parameters
     * @return the method signature
     */
    private static String getMethodSignature(String methodName,
            Class<?>[] types, Object[] params) {
        String methodString = methodName + "(";

        for (int i = 0; i < types.length; i++) {

            if (i > 0) {
                methodName += ", ";
            }
            methodName += types[i].getName();
        }

        methodString+=")";

        return methodString;
    }

    /**
     * Returns the name of the referenced UG object.
     * @return the name of the referenced UG object
     */
    @Override
    public String getClassName() {
        return reference.getClassName();
    }

    /**
     * Returns the names of the base classes of the referenced UG object.
     * @return the names of the base classes of the referenced UG object
     */
    @Override
    public ArrayList<String> getClassNames() {
        return reference.getClassNames();
    }

    /**
     * Calls {@link UGObject#releaseThis() } on the referenced UG object.
     */
    @Override
    public void releaseThis() {
        reference.releaseThis();
    }

    @Override
    public void setClassNames(ArrayList<String> classNames) {
        reference.setClassNames(classNames);
    }

    @MethodInfo(noGUI = true)
    public VisualCanvas getMainCanvas() {
        return reference.getMainCanvas();
    }
}
