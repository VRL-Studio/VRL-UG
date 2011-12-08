/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import java.io.Serializable;

/**
 * Enum that represents native parameter types.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public enum NativeType implements Serializable{

    BOOL,
    INTEGER,
    NUMBER,
    STRING,
    POINTER,
    CONST_POINTER,
    SMART_POINTER,
    CONST_SMART_POINTER,
    VOID,
    UNDEFINED
}
