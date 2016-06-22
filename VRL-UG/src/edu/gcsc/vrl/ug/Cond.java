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

package edu.gcsc.vrl.ug;

import groovy.lang.GroovyObjectSupport;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Cond extends GroovyObjectSupport{
    private boolean condBool;
    private double value;

    public Cond() {
        condBool = false;
        value = 0;
    }

    public Cond(boolean bndBool, double value) {
        this.condBool = bndBool;
        this.value = value;
    }

    public Cond(ArrayList<Object> data) {
        if (data==null) {
            throw new IllegalArgumentException(
                    "Argument \"null\" not supported!");
        }
        if (data.size()!=2) {
            throw new IllegalArgumentException(
                    "Range mismatch: expected 2, provided:" + data.size());
        }

        if (!(data.get(0) instanceof Boolean)) {
            throw new IllegalArgumentException(
                    "Type mismatch: first entry must be of type boolean!");
        }

        if (!(data.get(1) instanceof Double)
                && !(data.get(1) instanceof Float)
                && !(data.get(1) instanceof Integer)
                && !(data.get(1) instanceof Long)
                && !(data.get(1) instanceof Short)) {
            throw new IllegalArgumentException(
                    "Type mismatch: second entry must be a number!");
        }

        condBool = (Boolean) data.get(0);
        value = (Double) data.get(1);
    }

    /**
     * @return the bndBool
     */
    public boolean getCondBool() {
        return condBool;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }
}
