/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.visual.MessageType;
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

//    native long getExportedFunctionPtrBySignature(String name, Object[] params);
    native Object invokeFunction(long fPtr, boolean readOnly, Object[] params);

    native int ugInit(String[] args);

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
//        attachCanvas(mainCanvas);
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
