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
 * This class contains all properties of a native class that are necessary to
 * generate code for wrapper classes.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeClassInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String name;
    private String category;
    private String[] classNames;
    private NativeConstructorInfo[] constructors;
    private NativeMethodGroupInfo[] methods;
    private NativeMethodGroupInfo[] constMethods;
    private boolean instantiable;
    private boolean groupClass = false;

    public NativeClassInfo() {
    }

    public NativeClassInfo(NativeClassInfo c) {
        this.name = c.name;
        this.category = c.category;
        this.classNames = c.classNames.clone();
        this.constructors = c.constructors;
        this.methods = c.methods;
        this.constMethods = c.constMethods;
        this.instantiable = c.instantiable;
        this.groupClass = c.groupClass;
    }

    /**
     * Returns the class name.
     * @return the class name
     */
    public String getName() {
        return name;
    }

    /**
     * Defines the class name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the component category.
     * @return the component category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Defines the component category of this class info.
     * @param category the category to set
     */
    public void setCategory(String category) {
        if (category == null || category.equals("")) {
            category = "UG4";
        } else {

            if (category.startsWith("/")) {
                category = category.substring(1);
            }

            if (category.toUpperCase().startsWith("UG4")) {
                category = category.substring(3);
                category = "UG4" + category;
            }
        }

        this.category = category;
    }

    /**
     * Returns the names of the base classes.
     * @return names of the base classes
     */
    public String[] getClassNames() {
        return classNames;
    }

    public String[] getBaseClassNames() {
        String[] baseClassNames = null;
        if (getClassNames().length > 0) {
            baseClassNames = new String[getClassNames().length - 1];

            for (int i = 0; i < getClassNames().length - 1; i++) {
                baseClassNames[i] = getClassNames()[i + 1];
            }
        }

        return baseClassNames;
    }

    /**
     * Defines the names of the base classes.
     * @param classNames the names to set
     */
    public void setClassNames(String[] classNames) {
        this.classNames = classNames;
    }

    /**
     * Indicates wehther this class is instantiable.
     * @return <code>true</code> if this class is instantiable;
     *         <code>false</code> otherwise
     */
    public boolean isInstantiable() {
        return instantiable;
    }

    /**
     * Defines whether this class shall be instantiable.
     * @param isInstantiable the state to set
     */
    public void setInstantiable(boolean isInstantiable) {
        this.instantiable = isInstantiable;
    }

    /**
     * Returns the method groups of this class.
     * @return method groups of this class
     */
    public NativeMethodGroupInfo[] getMethods() {
        return methods;
    }

    /**
     * Returns the const method groups of this class.
     * @return const method groups of this class
     */
    public NativeMethodGroupInfo[] getConstMethods() {
        return constMethods;
    }

    /**
     * Defines the method groups of this class
     * @param methods method groups to set
     */
    public void setMethods(NativeMethodGroupInfo[] methods) {
        this.methods = methods;

        for (NativeMethodGroupInfo m : methods) {
            m.setConst(false);
        }
    }

    /**
     * Defines the const method groups of this class
     * @param constMethods const method groups to set
     */
    public void setConstMethods(NativeMethodGroupInfo[] constMethods) {
        this.constMethods = constMethods;

        for (NativeMethodGroupInfo m : constMethods) {
            m.setConst(true);
        }
    }

    /**
     * @return the groupClass
     */
    public boolean isGroupClass() {
        return groupClass;
    }

    /**
     * @param groupClass the groupClass to set
     */
    void setGroupClass(boolean groupClass) {
        this.groupClass = groupClass;
    }

    /**
     * @return the constructors
     */
    public NativeConstructorInfo[] getConstructors() {
        return constructors;
    }

    /**
     * @param constructors the constructors to set
     */
    public void setConstructors(NativeConstructorInfo[] constructors) {
        this.constructors = constructors;
    }
}
