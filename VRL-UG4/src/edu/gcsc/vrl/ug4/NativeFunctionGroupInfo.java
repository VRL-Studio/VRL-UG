/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeFunctionGroupInfo extends NativeMethodGroupInfo{

    public NativeFunctionGroupInfo() {
    }

    /**
     * @return the overloads
     */
    @Override
    public NativeFunctionInfo[] getOverloads() {
        return (NativeFunctionInfo[]) super.getOverloads();
    }

    /**
     * @param overloads the overloads to set
     */
    public void setOverloads(NativeFunctionInfo[] overloads) {
        super.setOverloads(overloads);
    }
}
