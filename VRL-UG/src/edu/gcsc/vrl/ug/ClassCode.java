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

import edu.gcsc.vrl.ug.types.CodeType;
import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the code of a class.
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class ClassCode implements CodeElement {

    private NativeAPIInfo api;
    private NativeClassInfo classInfo;
    private CodeType type;
    private boolean isConst;

    /**
     * Constructor.
     *
     * @param nativeAPI native api
     * @param classInfo class info
     * @param type code type
     * @param isConst defines whether to compile as const
     */
    public ClassCode(NativeAPIInfo nativeAPI,
            NativeClassInfo classInfo, CodeType type, boolean isConst) {
        this.api = nativeAPI;
        this.classInfo = classInfo;
        this.type = type;
        this.isConst = isConst;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        String classHeaderCode = "";

        boolean asInterface = type == CodeType.INTERFACE;
        boolean asWrapper = type == CodeType.WRAP_POINTER_CLASS;
        boolean asFullClass = type == CodeType.FULL_CLASS;

        boolean weAreAGroupClass = api.isClassGroup(classInfo.getName());
        boolean weArePartOfAGroup =
                api.isInClassGroup(classInfo.getName());

        if (classInfo.isGroupClass()) {
            builder.addLine("// group");
        }

        String prefix = "";

        if (asInterface) {
            classHeaderCode =
                    "public interface "
                    + CodeUtils.interfaceName(classInfo.getName(), isConst)
                    + " extends edu.gcsc.vrl.ug.UGObjectInterface ";

            if (!isConst) {
                // we extend the const interface
                classHeaderCode += ", "
                        + CodeUtils.interfaceName(classInfo.getName(), true);
            }

            if (classInfo.getClassNames() != null
                    && classInfo.getBaseClassNames().length > 0) {

                classHeaderCode += ", "
                        + CodeUtils.namesToInterfaceNameList(
                        classInfo.getBaseClassNames(),
                        isConst);
            }

            // add the group interface
            if (!weAreAGroupClass && weArePartOfAGroup) {
                classHeaderCode += ", "
                        + CodeUtils.interfaceName(
                        NativeClassGroupInfo.convertToClassGroup(
                        api, classInfo.getName()), isConst);
            }

        } else if (asFullClass) {

//            if (isConst) {
            classHeaderCode = "public class "
                    + CodeUtils.className(classInfo.getName(), isConst)
                    + " extends edu.gcsc.vrl.ug.UGObject implements "
                    + CodeUtils.interfaceName(classInfo.getName(), isConst);
//            } else {
//                classHeaderCode = "public class "
//                        + CodeUtils.className(classInfo.getName(), isConst)
//                        + " extends " + CodeUtils.className(classInfo.getName(), true) + " implements "
//                        + CodeUtils.interfaceName(classInfo.getName(), isConst);
//
//            }
        } else if (asWrapper) {
//            if (isConst) {
            classHeaderCode = "public class "
                    + CodeUtils.className(classInfo.getName(), isConst)
                    + " extends edu.gcsc.vrl.ug.UGObject implements "
                    + CodeUtils.interfaceName(classInfo.getName(), isConst);
//            } else {
//                classHeaderCode = "public class "
//                        + CodeUtils.className(classInfo.getName(), isConst)
//                        + " extends " + CodeUtils.className(classInfo.getName(), true) + " implements "
//                        + CodeUtils.interfaceName(classInfo.getName(), isConst);
//            }
        }

        if ((asFullClass) || (asWrapper)) {
            builder.addLine("@UGObjectInfo(instantiable="
                    + classInfo.isInstantiable()
                    + ", groupRoot=" + classInfo.isGroupClass()
                    + ", groupChild=" + api.isInClassGroup(classInfo.getName())
                    + ", constClass=" + isConst + ")");

            builder.addLine(new ComponentInfoCode(
                    classInfo, isConst).toString()).
                    addLine("@ObjectInfo(name=\""
                    + prefix
                    + VLangUtils.addEscapeCharsToCode(
                    CodeUtils.classNameForParamInfo(
                    classInfo.getName(), isConst)) + "\")");
        } else {
            builder.addLine("@ComponentInfo(ignore=true)");
        }

        builder.addLine(classHeaderCode + " {").
                incIndentation();

        if (asFullClass || asWrapper) {

            String classGrpName = "";

            if (weAreAGroupClass) {
                classGrpName = classInfo.getName();
            }

//            builder.addLine(
//                    "private static final long serialVersionUID=1L").
//                    addLine("public " + CodeUtils.className(
//                    classInfo.getName(), isConst)
//                    + "() { setClassName(\"" + classInfo.getName()
//                    + "\"); setInstantiable(" + asFullClass + " );"
//                    + "setClassGroupObject(" + weAreAGroupClass + " );"
//                    + "setClassGroupName(\"" + classGrpName + "\");}").
//                    newLine();
            
            builder.addLine(
                    "private static final long serialVersionUID=1L").
                    addLine("public " + CodeUtils.className(
                    classInfo.getName(), isConst)
                    + "() { __initialize();}").
                    newLine();

            builder.newLine().addLine(
                    "private void __initialize() { setClassName(\"" + classInfo.getName()
                    + "\"); setInstantiable(" + asFullClass + " );"
                    + "setClassGroupObject(" + weAreAGroupClass + " );"
                    + "setClassGroupName(\"" + classGrpName + "\");}").
                    newLine();
        }

//        ArrayList<MethodGroupSignature> signatures =
//                new ArrayList<MethodGroupSignature>();

        ArrayList<MethodSignature> signatures =
                new ArrayList<MethodSignature>();

        NativeClassInfo[] baseClasses = api.baseClasses(classInfo);
        NativeClassInfo[] classes =
                new NativeClassInfo[baseClasses.length + 1];

        classes[0] = classInfo;

        // copy base classes to classes array with offset 1
        System.arraycopy(baseClasses, 0, classes, 1, baseClasses.length);

        boolean[] visual = new boolean[]{false, true};

        for (boolean createVisual : visual) {

            boolean inherited = false;

            for (NativeClassInfo cls : classes) {

                if (!isConst && !asWrapper && cls.getConstructors() != null) {
                    // add constructor code
                    new MethodGroupCode(api,
                            NativeConstructorInfo.toNativeMethodGroupInfo(
                            cls),
                            signatures, type, createVisual,
                            inherited).build(builder).
                            newLine();
                }

                if (!isConst && cls.getMethods() != null) {

                    // add method code
                    for (NativeMethodGroupInfo m : cls.getMethods()) {

                        new MethodGroupCode(api,
                                m, signatures, type, createVisual,
                                inherited).build(
                                builder).newLine();
                    }
                }

                for (NativeMethodGroupInfo m : cls.getConstMethods()) {
                    new MethodGroupCode(api,
                            m, signatures, type, createVisual,
                            inherited).build(
                            builder).newLine();
                }

                // from now on we generate inherited methods
                inherited = true;

            } // end fore classes

            signatures.clear();

        }  // end fore visual

        if (asFullClass) {

            String interfaceName = CodeUtils.interfaceName(
                    classInfo.getName(), isConst);

            builder.newLine().append("@ReferenceMethodInfo()").
                    newLine().append("@MethodInfo(valueName=\""
                    + CodeUtils.classNameForParamInfo(
                    classInfo.getName(), isConst)).append("\")").
                    newLine().append("public ").
                    append(interfaceName).
                    append(" vrl__reference__method(@ParamInfo(nullIsValid=true,"
                    + " name=\"" + CodeUtils.classNameForParamInfo(
                    classInfo.getName(), isConst) + "\")").
                    append(interfaceName).
                    append(" o ) { if(o!=null){setThis(o)};return this }").
                    newLine();

        } else if (asWrapper) {
            String interfaceName = CodeUtils.interfaceName(
                    classInfo.getName(), isConst);

            builder.newLine().append("@ReferenceMethodInfo()").
                    newLine().append("@MethodInfo(valueName=\""
                    + CodeUtils.classNameForParamInfo(
                    classInfo.getName(), isConst)).append("\")").
                    newLine().append("public ").
                    append(interfaceName).
                    append(" vrl__reference__method(@ParamInfo(nullIsValid=true,"
                    + " name=\"" + CodeUtils.classNameForParamInfo(
                    classInfo.getName(), isConst) + "\")").
                    append(interfaceName).
                    append(" o ) { if(o!=null){setThis(o)};return this }").
                    newLine();

        }

        builder.decIndentation();

        builder.addLine("}").newLine().addLine(
                "// ------------------------------ //").newLine();

        return builder;
    }
}
