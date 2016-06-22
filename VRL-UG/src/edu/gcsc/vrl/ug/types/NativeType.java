/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2006–2012 by Michael Hoffer
 * 
 * This file is part of Visual Reflection Library (VRL).
 *
 * VRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/VRL/src/eu/mihosoft/vrl/resources/license/lgplv3.txt
 *
 * VRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of VRL includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of VRL. Neither the VRL Canvas attribution icon nor any
 * copyright statement/attribution may be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do three things regarding copyright
 * notice and author attribution.
 *
 * First, the following text must be displayed on the Canvas:
 * "based on VRL source code". In this case the VRL canvas icon must be removed.
 * 
 * Second, the copyright notice must remain. It must be reproduced in any
 * program that uses VRL.
 *
 * Third, add an additional notice, stating that you modified VRL. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "VRL source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * M. Hoffer, C.Poliwoda, G.Wittum. Visual Reflection Library -
 * A Framework for Declarative GUI Programming on the Java Platform.
 * Computing and Visualization in Science, 2011, in press.
 */
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
