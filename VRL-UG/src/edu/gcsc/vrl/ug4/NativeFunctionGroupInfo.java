/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

/**
 * This class contains all properties of a native function group that are 
 * necessary to generate code for wrapper functions.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeFunctionGroupInfo extends NativeMethodGroupInfo{

    /**
     * Constructor.
     */
    public NativeFunctionGroupInfo() {
    }

    @Override
    public NativeFunctionInfo[] getOverloads() {
        return (NativeFunctionInfo[]) super.getOverloads();
    }

    /**
     * Defines the overloads of this function group.
     * @param overloads the overloads to set
     */
    public void setOverloads(NativeFunctionInfo[] overloads) {
        super.setOverloads(overloads);
    }
}
