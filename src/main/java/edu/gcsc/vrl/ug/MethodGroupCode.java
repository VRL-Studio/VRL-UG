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

import edu.gcsc.vrl.ug.types.CodeType;
import eu.mihosoft.vrl.lang.CodeBuilder;
import java.util.ArrayList;

/**
 * Code element that generates method group code.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MethodGroupCode implements CodeElement {

    private NativeMethodGroupInfo methodInfo;
    private boolean visual;
    private boolean inherited;
    private CodeType type;
    private ArrayList<MethodSignature> signatures;
    private NativeAPIInfo api;

    /**
     * Constructor.
     * @param methodInfo method info
     * @param type code type
     * @param visual defines whether to generate code that shall be visualized
     * @param inherited indicates whether this method is inherited
     *                  from base class
     */
    public MethodGroupCode(NativeAPIInfo api,
            NativeMethodGroupInfo methodInfo,
            ArrayList<MethodSignature> signatures,
            CodeType type, boolean visual, boolean inherited) {
        this.methodInfo = methodInfo;
        this.visual = visual;
        this.type = type;
        this.signatures = signatures;
        this.api = api;
        this.inherited = inherited;
    }

    /**
     * Constructor.
     * @param methodInfo method info
     * @param type code type
     * @param visual defines whether to generate code that shall be visualized
     * @param inherited indicates whether this method is inherited
     *                  from base class
     */
    public MethodGroupCode(NativeAPIInfo api,
            NativeMethodGroupInfo methodInfo,
            CodeType type, boolean visual, boolean inherited) {
        this.methodInfo = methodInfo;
        this.visual = visual;
        this.type = type;
        this.api = api;
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        boolean function = methodInfo instanceof NativeFunctionGroupInfo;

        boolean signaturesWereNull = signatures == null;

        if (signatures == null) {
            signatures = new ArrayList<MethodSignature>();
        }

        boolean showMethod = false;

        if (function && visual) {

            // check number of overloads
            int numMethods = 0;
            ArrayList<MethodSignature> tmpSignatures =
                    new ArrayList<MethodSignature>();
            for (NativeMethodInfo m : methodInfo.getOverloads()) {

                m = NativeClassGroupInfo.convertToClassGroup(api, m);
                
                // forbid method duplicates
                if (tmpSignatures == null
                        || !tmpSignatures.contains(new MethodSignature(m))) {
                    numMethods++;
                    if (tmpSignatures != null) {
                        tmpSignatures.add(new MethodSignature(m));
                    }
                }
            }

            // if only one function available show it if not requested otherwise
            // by ug registry
            showMethod = numMethods == 1;
        }



        for (NativeMethodInfo m : methodInfo.getOverloads()) {

            m = NativeClassGroupInfo.convertToClassGroup(api, m);

            // forbid method duplicates
            if (signatures == null
                    || !signatures.contains(new MethodSignature(m))) {
                new MethodCode(m, function,
                        type, visual, inherited, showMethod).build(builder);
                if (signatures != null) {
                    signatures.add(new MethodSignature(m));
                }
            }
        }

        if (signaturesWereNull) {
            signatures = null;
        }

        return builder;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }
}
