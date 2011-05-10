/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.visual.MessageType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 * UG4 class represents UG4 and its scripting functionality. It allows only one
 * instance which can be obtained via
 * {@link #getUG4(eu.mihosoft.vrl.reflection.VisualCanvas) }.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UG4 {

    /**
     * native classes
     */
    private static Class<?>[] nativeClasses;
    /**
     * ug messages
     */
    private static StringBuffer messages = new StringBuffer();

//    /**
//     * Loads native library.
//     */
//    static {
//        System.loadLibrary("ug4");
//    }
    /**
     * Returns all native UG4 classes that are exported via the UG registry,
     * i.e., the equivalent Java wrapper classes.
     * @return the nativeClasses
     */
    public static Class<?>[] getNativeClasses() {
        return nativeClasses;
    }

    /**
     * Defines all native UG4 classes that are exported via the UG registry,
     * i.e., the equivalent Java wrapper classes.
     * @param aNativeClasses the nativeClasses to set
     */
    static void setNativeClasses(Class<?>[] nativeClasses) {
        UG4.nativeClasses = nativeClasses;
    }

    /**
     * instanciation only allowed in this class
     */
    private UG4() {
        String[] args = {""};
        System.loadLibrary("ug4");
        ugInit(args);

        try {
            NativeAPIInfo nativeAPI = convertRegistryInfo();
            Compiler compiler = new edu.gcsc.vrl.ug4.Compiler();
            compiler.compile(new edu.gcsc.vrl.ug4.NativeAPICode(
                    nativeAPI).getAllCodes());
        } catch (Exception ex) {
            Logger.getLogger(UG4.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

    }
    /**
     * ug instance
     */
    private static UG4 ug4;
    /**
     * VRL canvas used to visualize ug classes
     */
    private VisualCanvas mainCanvas;
    /**
     * Messaging thread which displays messages generated from native UG
     * methods.
     */
    private MessageThread messagingThread;

    /**
     * Returns the instance of this singleton. If it does not exist it will be
     * created.
     * @param canvas VRL canvas that shall be used to visualize ug classes
     * @return the instance of this singleton
     */
    public static synchronized UG4 getUG4(VisualCanvas canvas) {
        if (ug4 == null) {

            ug4 = new UG4();

            if (canvas != null) {
                ug4.setMainCanvas(canvas);
            }

        } else if (canvas != null) {
            ug4.setMainCanvas(canvas);
        }

        return ug4;
    }

    /**
     * <p>
     * Returns the instance of this singleton.
     * </p>
     * <p>
     * <b>Notice:</b> It is necessary to use this method to create the UG
     * instance if the UG classes shall be visualized via VRL. If the UG
     * instance has been created the method {@link #getUG4() } can be used
     * to get a reference to it.
     * </p>
     */
    public static UG4 getUG4() {
        return getUG4(null);
    }

    /**
     * Returns the VRL canvas that is used to visualize the UG classes.
     * @return the VRL canvas that is used to visualize the UG classes
     */
    public VisualCanvas getMainCanvas() {
        return mainCanvas;
    }

    /**
     * <p>
     * The VRL canvas that shall be used to visualize the UG classes
     * </p>
     * <p>
     * <b>Notice:</b> as a side effect this method starts UG message logging
     * </p>
     * @param mainCanvas the VRL canvas to set
     */
    public void setMainCanvas(VisualCanvas mainCanvas) {
        this.mainCanvas = mainCanvas;
        stopLogging();
        startLogging();
//        attachCanvas(mainCanvas);
    }

    /**
     * Starts UG message logging.
     */
    public void startLogging() {
        stopLogging();
        messagingThread = new MessageThread();
        messagingThread.start();
    }

    /**
     * Stops UG message logging.
     */
    public void stopLogging() {
        if (messagingThread != null) {
            messagingThread.stopLogging();
        }
    }

    /**
     * @return the messages
     */
    private StringBuffer getMessages() {
        return messages;
    }

    public static void addMessage(String s) {
        messages.append(s);

        if (messages.length() > 1000000) {
            messages.delete(0, 1000000);
        }
    }

    public void clearMessages() {
        if (messages.length() > 0) {
            messages.delete(0, messages.length());
        }
    }

    class MessageThread extends Thread {

        private boolean logging = true;

        public MessageThread() {
            //
        }

        @Override
        public void run() {

            while (logging) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    //
                }

                 if (getMessages().length()>0) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            if (mainCanvas != null) {
                                mainCanvas.getMessageBox().addMessageAsLog(
                                        "UG-Output:",
                                        "<pre>" + messages + "</pre>",
                                        MessageType.INFO);
                                clearMessages();
                            }
                        }
                    });
                }
            }
        }

        /**
         * @param logging the logging to set
         */
        public void stopLogging() {
            this.logging = false;
        }
    }


    // ********************************************
    // ************** NATIVE METHODS **************
    // ********************************************
    final native NativeAPIInfo convertRegistryInfo();

    native Object invokeMethod(
            String exportedClassName,
            long objPtr, boolean readOnly,
            String methodName, Object[] params);

    native long newInstance(long objPtr);

    native long getExportedClassPtrByName(String name);

    native Object invokeFunction(String name,
            boolean readOnly, Object[] params);

    final native int ugInit(String[] args);

    native String getSvnRevision();

    native String getCompileDate();
//    native void attachCanvas(VisualCanvas canvas);
}
