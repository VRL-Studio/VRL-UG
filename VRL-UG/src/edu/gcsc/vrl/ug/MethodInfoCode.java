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

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;

/**
 * Code element that generates method code.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MethodInfoCode implements CodeElement {

    private NativeMethodInfo method;
    private boolean visual;
    private boolean inherited;
    private boolean showMethod;

    /**
     * Constructor
     * @param method method
     * @param visual defines whether to visualize this method
     * @param inherited indicates whether this method is inherited
     *                  from base class
     * @param showMethod defines whether to show this method (hide=false), is
     *                   ignored if the method provides custom options
     */
    public MethodInfoCode(NativeMethodInfo method, boolean visual,
            boolean inherited, boolean showMethod) {
        this.method = method;
        this.visual = visual;
        this.inherited = inherited;
        this.showMethod = showMethod;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        // constructor
        if (method.isConstructor() && visual) {
            if (inherited) {
                builder.addLine("@Deprecated");
                builder.append("@MethodInfo(/*inherited*/initializer=true, noGUI=true)");
            } else {
                builder.append(
                        "@MethodInfo(/*impl*/initializer=true, interactive=false)");
            }
            return builder;
        } else if (method.isConstructor() && !visual) {
            if (inherited) {
                builder.addLine("@Deprecated");
                builder.append("@MethodInfo(/*inherited*/initializer=true, noGUI=true)");
            } else {
                builder.append("@MethodInfo(/*impl*/initializer=true, noGUI=true)");
            }
            return builder;
        }

        // regular method
        if (visual) {

            boolean needsComma = false;

            if (inherited) {
                builder.append("@MethodInfo(/*inherited*/");
            } else {
                builder.append("@MethodInfo(/*impl*/");
            }

            if (method.getOptions() != null && !method.getOptions().isEmpty()) {

                if (!method.getOptions().matches(".*hide\\s*=.*")) {
                    builder.append(method.getOptions() + ", hide=" + !showMethod);
                } else {
                    builder.append(method.getOptions());
                }
                needsComma = true;
            } else {
                builder.append("hide=" + !showMethod);
                needsComma = true;
            }

            // use interactive=false as default (no invoke-button)
            if (!method.isConstructor()
                    && !method.getOptions().matches(".*interactive\\s*=.*")) {

                if (needsComma) {
                    builder.append(", ");
                }

                builder.append("interactive=false");
            }

            if (!method.returnsVoid()) {

                String valueName = "";

                if (method.getReturnValue().isRegisteredClass()) {

                    valueName = VLangUtils.addEscapeCharsToCode(
                            method.getReturnValue().getParamInfo()[0]);

                    if (valueName.isEmpty()) {
                        valueName = CodeUtils.classNameForParamInfo(
                                method.getReturnValue().getClassName(),
                                method.isConst());
                    }
                }

                if (needsComma) {
                    builder.append(", ");
                }

                String typePrefix = "";

                if (method.getReturnValue().isConst()) {
                    typePrefix = "const ";
                }

                builder.append("valueName=\""
                        + valueName
                        + "\", valueStyle=\""
                        + method.getReturnValue().getParamInfo()[1] + "\", "
                        + "valueTypeName = \""
                        + typePrefix + method.getReturnValue().getClassName()
                        + "\", valueOptions=\""
                        + method.getReturnValue().getParamInfo()[2]);

                if (method.getReturnValue().isRegisteredClass()) {
                    builder.append(";serialization=false");
                }

                builder.append("\")");
            } else {
                builder.append(")");
            }


        } else if (!visual) {
            if (inherited) {
                builder.append("@MethodInfo(/*inherited*/noGUI=true)");
            } else {
                builder.append("@MethodInfo(/*impl*/noGUI=true)");
            }
        }

        return builder;
    }
}
