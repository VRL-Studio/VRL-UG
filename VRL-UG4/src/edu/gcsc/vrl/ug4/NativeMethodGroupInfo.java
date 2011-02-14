/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeMethodGroupInfo {
    private NativeMethodInfo[] overloads;
    private boolean isConst;

    public NativeMethodGroupInfo() {
    }

    /**
     * @return the overloads
     */
    public NativeMethodInfo[] getOverloads() {
        return overloads;
    }

    /**
     * @param overloads the overloads to set
     */
    public void setOverloads(NativeMethodInfo[] overloads) {
        this.overloads = overloads;
    }

    /**
     * @return the isConst
     */
    public boolean isConst() {
        return isConst;
    }

    /**
     * @param isConst the isConst to set
     */
    public void setConst(boolean isConst) {
        this.isConst = isConst;

        for (NativeMethodInfo nativeMethodInfo : overloads) {
            nativeMethodInfo.setConst(isConst);
        }
    }
}
