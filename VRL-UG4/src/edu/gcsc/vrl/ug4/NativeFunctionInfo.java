/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

/**
 * This class contains all properties of a native function that are necessary to
 * generate code for wrapper functions.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeFunctionInfo extends NativeMethodInfo {
    private String category;

    /**
     * Returns the component category.
     * @return the component category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Defines the component category.
     * @param categoryGroup the component category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
}
