/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2012–2018 Goethe Universität Frankfurt am Main, Germany
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

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;

/**
 * Code element that generates component info code.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class ComponentInfoCode implements CodeElement {

    private NativeClassInfo classInfo;
//    private String prefix;
    private boolean isConst;

    /**
     * Constructor.
     * @param classInfo class info
     * @param prefix class name prefix
     */
    public ComponentInfoCode(NativeClassInfo classInfo, boolean isConst) {
        this.classInfo = classInfo;
//        this.prefix = prefix;
        this.isConst = isConst;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).getCode();
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {
        builder.append("@ComponentInfo(name=\"").
                append(VLangUtils.addEscapeCharsToCode(
                CodeUtils.classNameForParamInfo(classInfo.getName(), isConst))).
                append("\", category=\"").
                append(VLangUtils.addEscapeCharsToCode(
                classInfo.getCategory())).
                append("\", allowRemoval=false)");

        return builder;
    }
}
