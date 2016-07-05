/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2012–2016 Goethe Universität Frankfurt am Main, Germany
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

import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import java.util.ArrayList;

/**
 * This interface defines the methods that can be called on a UG wrapper
 * object.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public interface UGObjectInterface {

    /**
     * Returns the class name of this object.
     * @return the class name of this object
     */
    String getClassName();

    /**
     * Returns the names of the base classes of this object.
     * @return the names of the base classes of this object
     */
    ArrayList<String> getClassNames();
 
    /**
     * Releases pointer.
     */
    @MethodInfo(noGUI = true)
    void releaseThis();

    /**
     * Defines the names of the base classes of this object
     * @param classNames the class names to set
     */
    void setClassNames(ArrayList<String> classNames);

    /**
     * Defines the VRL canvas this object belongs to. This method is
     * called automatically by VRL.
     * @param mainCanvas VRL canvas to set
     */
    @MethodInfo(noGUI = true, callOptions = "assign-canvas")
    void setMainCanvas(VisualCanvas mainCanvas);

    /**
     * Returns the VRL canvas that visualizes this object.
     * @return the VRL canvas that visualizes this object
     */
    @MethodInfo(noGUI = true)
    VisualCanvas getMainCanvas();
}
