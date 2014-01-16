/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug.types;

import java.io.Serializable;

/**
 * Enum that represents native parameter types.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public enum NativeType implements Serializable {

    VOID,
    INVALID,
    BOOL,
    INT,
    SIZE_T,
    FLOAT,
    DOUBLE,
    CSTRING,
    STDSTRING,
    POINTER,
    CONST_POINTER,
    SMART_POINTER,
    CONST_SMART_POINTER
    
    
    /* copied at 20130522 from c++ code from ug project
     * from file trunk/ugbase/common/util/variant.cpp
     VT_INVALID = 0,
     VT_BOOL = 1,
     VT_INT = 2,
     VT_SIZE_T = 3,
     VT_FLOAT = 4,
     VT_DOUBLE = 5,
     VT_CSTRING = 6,
     VT_STDSTRING = 7,
     VT_POINTER = 8,
     VT_CONST_POINTER = 9,
     VT_SMART_POINTER = 10,
     VT_CONST_SMART_POINTER = 11
     */
    /* order before 20130522
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
     */
}
