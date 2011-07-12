/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug;

/**
 * This class contains all properties of a native method group that are 
 * necessary to generate code for wrapper methods.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeMethodGroupInfo {
    private NativeMethodInfo[] overloads;
    private boolean isConst;

    /**
     * Constructor.
     */
    public NativeMethodGroupInfo() {
    }

    public NativeMethodGroupInfo(NativeMethodGroupInfo mG) {
        this.overloads = mG.overloads.clone();
        this.isConst = mG.isConst;
    }
    
    

    /**
     * Returns the overloads of this method group.
     * @return the overloads of this method group
     */
    public NativeMethodInfo[] getOverloads() {
        return overloads;
    }

    /**
     * Defines the overloads of this method group.
     * @param overloads the overloads to set
     */
    public void setOverloads(NativeMethodInfo[] overloads) {
        this.overloads = overloads;
    }

    /**
     * Indicates whether this method group contains const methods.
     * @return <code>true</code> if this method group contains const methods;
     *         <code>false</code> otherwise
     */
    public boolean isConst() {
        return isConst;
    }

    /**
     * Defines whether this method group defines const methods.
     * @param isConst the state to set
     */
    public void setConst(boolean isConst) {
        this.isConst = isConst;

        for (NativeMethodInfo nativeMethodInfo : overloads) {
            nativeMethodInfo.setConst(isConst);
        }
    }
}
