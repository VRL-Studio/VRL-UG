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

import java.io.Serializable;

/**
 * This class contains all properties of a native class group that are
 * necessary to generate code for wrapper classes.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeClassGroupInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String[] classes;
    private String name;
    private String defaultClass;

    /**
     * @return the classes
     */
    public String[] getClasses() {
        return classes;
    }

    /**
     * @param classes the classes to set
     */
    public void setClasses(String[] classes) {
        this.classes = classes;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the defaultClass
     */
    public String getDefaultClass() {
        return defaultClass;
    }

    /**
     * @param defaultClass the defaultClass to set
     */
    public void setDefaultClass(String defaultClass) {
        this.defaultClass = defaultClass;
    }

//    /**
//     * Returns this class group as class info which can be used for code
//     * generation.
//     * @return this class group as class info
//     */
//    public NativeClassInfo asClassInfo(NativeAPIInfo api) {
//        NativeClassInfo result = new NativeClassInfo();
//
////        NativeClassInfo template = api.getClassByName(classes[0]);
//
//        result.setName(name);
//
//
//        return result;
//    }

    /**
     * Replaces a class name with the name of its group.
     * @param api
     * @param name
     * @return  name of the group the specified class belongs to if the class 
     *          is part of a group; the unchanged class name if the class does
     *          not belong to a group; the specified name if the name does not
     *          specify a registered class.
     */
    public static String convertToClassGroup(NativeAPIInfo api, String name) {
        String result = name;
        
        if (api.isInClassGroup(name)) {
            result = api.getGroupByClassName(name).getName();
        } else if (api.isClassRegisteredByName(name)) {
            result = name;
        }

        return result;
    }

    /**
     * Replaces a ist of class names with the name of the class groups the
     * specified classes belong to.
     * @param api
     * @param name
     * @return
     */
    public static String[] convertToClassGroup(
            NativeAPIInfo api, String[] names) {
        String[] result = new String[names.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = convertToClassGroup(api, names[i]);
        }

        return result;
    }

    /**
     * 
     * @param p
     * @return 
     */
    public static NativeParamInfo convertToClassGroup(
            NativeAPIInfo api, NativeParamInfo p) {

        if (!p.isRegisteredClass()) {
            return p;
        }

        NativeParamInfo result = new NativeParamInfo(p);

        result.setClassName(convertToClassGroup(api, p.getClassName()));
        result.setClassNames(convertToClassGroup(api, p.getClassNames()));


        return result;
    }

    public static NativeParamInfo[] convertToClassGroup(
            NativeAPIInfo api, NativeParamInfo[] params) {
        NativeParamInfo[] result = new NativeParamInfo[params.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = convertToClassGroup(api, params[i]);
        }

        return result;
    }

    public static NativeMethodInfo convertToClassGroup(NativeAPIInfo api,
            NativeMethodInfo m) {
        NativeMethodInfo result = new NativeMethodInfo(m);

        result.setParameters(convertToClassGroup(api,m.getParameters()));
        result.setReturnValue(convertToClassGroup(api, m.getReturnValue()));

        return result;
    }
    
    public static NativeFunctionInfo convertToClassGroup(NativeAPIInfo api,
            NativeFunctionInfo m) {
        NativeFunctionInfo result = new NativeFunctionInfo(m);

        result.setParameters(m.getParameters());
        result.setReturnValue(convertToClassGroup(api, m.getReturnValue()));

        return result;
    }
    
    public static NativeMethodInfo[] convertToClassGroup(NativeAPIInfo api,
            NativeFunctionInfo[] methods) {
        NativeFunctionInfo[] result = new NativeFunctionInfo[methods.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = convertToClassGroup(api, methods[i]);
        }

        return result;
    }

    public static NativeMethodInfo[] convertToClassGroup(NativeAPIInfo api,
            NativeMethodInfo[] methods) {
        NativeMethodInfo[] result = new NativeMethodInfo[methods.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = convertToClassGroup(api, methods[i]);
        }

        return result;
    }
    
    public static NativeConstructorInfo convertToClassGroup(NativeAPIInfo api,
            NativeConstructorInfo c) {
        NativeConstructorInfo result = new NativeConstructorInfo(c);

        result.setParameters(convertToClassGroup(api,c.getParameters()));

        return result;
    }
    
     public static NativeConstructorInfo[] convertToClassGroup(
             NativeAPIInfo api,
            NativeConstructorInfo[] constructors) {
        NativeConstructorInfo[] result = 
                new NativeConstructorInfo[constructors.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = convertToClassGroup(api, constructors[i]);
        }

        return result;
    }

    public static NativeMethodGroupInfo convertToClassGroup(NativeAPIInfo api,
            NativeMethodGroupInfo mG) {
        NativeMethodGroupInfo result = new NativeMethodGroupInfo(mG);

        result.setOverloads(convertToClassGroup(api, mG.getOverloads()));

        return result;
    }

    public static NativeMethodGroupInfo[] convertToClassGroup(NativeAPIInfo api,
            NativeMethodGroupInfo[] mG) {
        NativeMethodGroupInfo[] result = new NativeMethodGroupInfo[mG.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = convertToClassGroup(api, mG[i]);
        }

        return result;
    }

    public static NativeClassInfo classToGroupClass(
            NativeAPIInfo api, NativeClassInfo cls) {
        NativeClassInfo result = new NativeClassInfo(cls);

        result.setName(convertToClassGroup(api, cls.getName()));
        result.setClassNames(convertToClassGroup(api, cls.getClassNames()));
        result.setConstructors(convertToClassGroup(api, cls.getConstructors()));
        result.setMethods(convertToClassGroup(api, cls.getMethods()));
        result.setConstMethods(convertToClassGroup(api, cls.getConstMethods()));
        
        result.setGroupClass(true);
        
        
        return result;
    }
}
