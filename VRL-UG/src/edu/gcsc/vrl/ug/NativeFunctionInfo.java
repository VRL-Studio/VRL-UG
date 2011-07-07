/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug;

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
        if (category == null || category.equals("")) {
            category = "UG4";
        } else {

            if (category.startsWith("/")) {
                category = category.substring(1);
            }

            if (category.toUpperCase().startsWith("UG4")) {
                category = category.substring(3);
                category = "UG4" + category;
            }
        }
        
        this.category = category;
    }
}
