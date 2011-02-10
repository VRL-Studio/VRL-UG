/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeAPIInfo {
    private NativeClassInfo[] classes;
    private NativeMethodInfo[] functions;

    /**
     * @return the classes
     */
    public NativeClassInfo[] getClasses() {
        return classes;
    }

    /**
     * @param classes the classes to set
     */
    public void setClasses(NativeClassInfo[] classes) {
        this.classes = classes;
    }

    /**
     * @return the functions
     */
    public NativeMethodInfo[] getFunctions() {
        return functions;
    }

    /**
     * @param functions the functions to set
     */
    public void setFunctions(NativeMethodInfo[] functions) {
        this.functions = functions;
    }
}
