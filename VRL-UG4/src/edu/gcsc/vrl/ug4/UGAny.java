/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

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
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class UGAny implements UGObjectInterface, Serializable {

    private static final long serialVersionUID = 1L;
    private transient VisualCanvas mainCanvas;
    private UGObject reference;
    private boolean throwException = true;
    private boolean enableMessaging = true;

    public UGAny(UGObject reference) {
        if (reference == null) {
            throw new IllegalArgumentException("References must not be null!");
        }
        this.reference = reference;
    }

    public UGAny enableExceptions(boolean throwExceptions) {
        this.throwException = throwExceptions;
        return this;
    }

    public UGAny enableMessaging(boolean enableMessaging) {
        this.enableMessaging = enableMessaging;
        return this;
    }

    @MethodInfo(noGUI = true, callOptions = "assign-canvas")
    public void setMainCanvas(VisualCanvas mainCanvas) {
        this.mainCanvas = mainCanvas;
    }

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

    public String getClassName() {
        return reference.getClassName();
    }

    public ArrayList<String> getClassNames() {
        return reference.getClassNames();
    }

    public void releaseAll() {
        reference.releaseAll();
    }

    public void releaseReferences() {
        reference.releaseReferences();
    }

    public void releaseThis() {
        reference.releaseThis();
    }

    public void setClassNames(ArrayList<String> classNames) {
        reference.setClassNames(classNames);
    }
}
