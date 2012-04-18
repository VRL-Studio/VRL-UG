/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

/**
 * Code type.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public enum CodeType {

    /**
     * Interface code
     */
    INTERFACE,
    /**
     * Code for a class that cannot be instantiated manually and only occurs
     * as parameter or return value
     */
    WRAP_POINTER_CLASS,
    /**
     * Code for concrete classes with default constructor
     */
    FULL_CLASS
}
