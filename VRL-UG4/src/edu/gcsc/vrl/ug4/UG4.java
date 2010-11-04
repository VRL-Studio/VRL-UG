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
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UG4 {

    static {
        System.loadLibrary("ug4");
    }

    private UG4() {
        //
    }
    private static UG4 ug4;
    private VisualCanvas mainCanvas;
    private MessageThread messagingThread;

    public static UG4 getUG4(VisualCanvas canvas) {
        if (ug4 == null) {

            if (canvas == null) {
                throw new IllegalArgumentException(
                        "UG4 not initialized."
                        + "Thus, a valid canvas instance must be assigned!");
            }

            ug4 = new UG4();

            ug4.setMainCanvas(canvas);

            String[] args = {""};
            ug4.ugInit(args);

        } else if (canvas != null) {
            ug4.setMainCanvas(canvas);
        }

        return ug4;
    }

    public static UG4 getUG4() {
        return getUG4(null);
    }

    native String[] createJavaBindings();

    native Object invokeMethod(
            String exportedClassName,
            long objPtr, boolean readOnly, String methodName, Object[] params);

    native long newInstance(long objPtr);

    native long getExportedClassPtrByName(String name);

    native Object invokeFunction(String name, boolean readOnly, Object[] params);

    native int ugInit(String[] args);

    native String getSvnRevision();

    native String getCompileDate();

//    native void attachCanvas(VisualCanvas canvas);
    /**
     * @return the mainCanvas
     */
    public VisualCanvas getMainCanvas() {
        return mainCanvas;
    }

    /**
     * @param mainCanvas the mainCanvas to set
     */
    public void setMainCanvas(VisualCanvas mainCanvas) {
        this.mainCanvas = mainCanvas;
        stopLogging();
        startLogging();
//        attachCanvas(mainCanvas);
    }

    public void startLogging() {
        stopLogging();
        messagingThread = new MessageThread();
        messagingThread.start();
    }

    public void stopLogging() {
        if (messagingThread != null) {
            messagingThread.stopLogging();
        }
    }

    native String getMessages();

    class MessageThread extends Thread {

        private boolean logging = true;
        String messages = getMessages();
        String oldMessages = "";

        public MessageThread() {
            //
        }

        @Override
        public void run() {
            messages = getMessages();
            oldMessages = "";

            while (logging) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    //
                }

                if (!messages.equals(oldMessages)) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            mainCanvas.getMessageBox().addUniqueMessage(
                                    "UG-Output:", messages, null, MessageType.INFO);
                        }
                    });
                }

                oldMessages = messages;
                messages = getMessages();
            }
        }

        /**
         * @param logging the logging to set
         */
        public void stopLogging() {
            this.logging = false;
        }
    }
//
//    public void addMessage(final String msg) {
//        SwingUtilities.invokeLater(new Runnable() {
//
//            public void run() {
//                mainCanvas.getMessageBox().
//                        addMessage("UG4:", msg, MessageType.INFO);
//            }
//        });
//    }
}
