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
package edu.gcsc.vrl.ug;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.gcsc.vrl.ug;
//
//import eu.mihosoft.vrl.lang.CodeBuilder;
//import java.util.ArrayList;
//import java.util.Arrays;
//
///**
// *
// * @author Michael Hoffer <info@michaelhoffer.de>
// */
//public class UGAnyCode {
//
//    private NativeAPIInfo apiInfo;
//    private CodeType type;
//    static final String NAME = "UGAny";
//
//    public UGAnyCode(NativeAPIInfo apiInfo, CodeType type) {
//        this.apiInfo = apiInfo;
//        this.type = type;
//    }
//
//    @Override
//    public String toString() {
//        return toString(new CodeBuilder()).toString();
//    }
//
//    public CodeBuilder toString(CodeBuilder builder) {
//
//        String classHeaderCode = "";
//
//        String[] allClassNames = getAllClassNames();
//
//        boolean asInterface = type == CodeType.INTERFACE;
//        boolean asFullClass = type == CodeType.FULL_CLASS;
//
//        if (asInterface) {
//            classHeaderCode =
//                    "public interface "
//                    + CodeUtils.interfaceName(NAME)
//                    + " extends UGObjectInterface ";
//            if (allClassNames.length > 0) {
//                classHeaderCode += ", "
//                        + CodeUtils.namesToInterfaceNameList(
//                        allClassNames);
//            }
//        } else if (asFullClass) {
//            classHeaderCode = "public class "
//                    + CodeUtils.className(NAME)
//                    + " extends edu.gcsc.vrl.ug4.UGAnyBase implements "
//                    + CodeUtils.interfaceName(NAME);
//        }
//
//        builder.addLine(classHeaderCode + " {");
//
//
//        builder.incIndentation();
//
//        if (asFullClass) {
//            builder.addLine("private static final long serialVersionUID=1L");
//        }
//
//
//        ArrayList<MethodSignature> signatures =
//                new ArrayList<MethodSignature>();
//
//        NativeMethodInfo[] methods = getAllMethods();
//
//        for (NativeMethodInfo m : methods) {
//
//            if (!signatures.contains(new MethodSignature(m))) {
//                String paramCode = new ParamTypeListCode(
//                        m.getParameters(), false).toString();
//
//                String invocationCode = "invokeMethod(\""
//                        + CodeUtils.methodName(m.getName())
//                        + "\", [" + paramCode + "] as Class<?>[], params);";
//
//                new MethodCode(m, type, false).toString(builder, invocationCode);
//
//                signatures.add(new MethodSignature(m));
//            }
//        }
//
//        for (NativeMethodInfo m : methods) {
//
//            if (!signatures.contains(new MethodSignature(m))) {
//
//                String paramCode = new ParamTypeListCode(
//                        m.getParameters(), true).toString();
//
//                String invocationCode = "invokeMethod(\""
//                        + CodeUtils.methodName(m.getName())
//                        + "\", [" + paramCode + "] as Class<?>[], params);";
//
//                new MethodCode(m, type, true).toString(builder, invocationCode);
//
//                signatures.add(new MethodSignature(m));
//            }
//        }
//
//        builder.newLine().incIndentation();
//
//        builder.addLine("}");
//
//        return builder;
//    }
//
//    private String[] getAllClassNames() {
//        String[] result = new String[apiInfo.getClasses().length];
//
//        for (int i = 0; i < apiInfo.getClasses().length; i++) {
//            result[i] = apiInfo.getClasses()[i].getName();
//        }
//        return result;
//    }
//
//    private NativeMethodInfo[] getAllMethods() {
//
//        ArrayList<NativeMethodInfo> methods =
//                new ArrayList<NativeMethodInfo>();
//
//        for (NativeClassInfo cls : apiInfo.getClasses()) {
//            for (NativeMethodGroupInfo mG : cls.getMethods()) {
//                methods.addAll(Arrays.asList(mG.getOverloads()));
//            }
//
//            for (NativeMethodGroupInfo mG : cls.getConstMethods()) {
//                methods.addAll(Arrays.asList(mG.getOverloads()));
//            }
//        }
//
//        NativeMethodInfo[] result =
//                new NativeMethodInfo[methods.size()];
//
//        methods.toArray(result);
//
//        return result;
//    }
//}
