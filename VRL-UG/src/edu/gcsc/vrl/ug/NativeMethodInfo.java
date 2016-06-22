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

import edu.gcsc.vrl.ug.types.NativeType;
import java.io.Serializable;

/**
 * This class contains all properties of a native method that are
 * necessary to generate code for wrapper methods.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeMethodInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String options;
    private NativeParamInfo returnValue;
    private NativeParamInfo[] parameters;
    private String toolTip;
    private String help;
    private boolean isConst;
    private boolean constructor;
    private boolean javaConstructor;

    public NativeMethodInfo() {
    }

    public NativeMethodInfo(NativeMethodInfo m) {
        //christian poliwoda start
//        System.out.println(getClass().getSimpleName() + " NativeMethodInfo(NativeMethodInfo m)");
//        System.out.println(" name = " + name);
        if (parameters != null) {
            int parametersLength = parameters.length;
            for (int i = 0; i < parametersLength; i++) {
                System.out.println(" parameters[" + i + "] = " + parameters[i]);
            }
        }

        //christian poliwoda end
        this.name = m.name;
        this.options = m.options;
        this.returnValue = new NativeParamInfo(m.returnValue);
        if (m.parameters != null) {
            this.parameters = m.parameters.clone();
        }
        this.toolTip = m.toolTip;
        this.help = m.help;
        this.isConst = m.isConst;
        this.constructor = m.constructor;
        this.javaConstructor = m.javaConstructor;
    }

    /**
     * Returns the method name.
     * @return the method name
     */
    public String getName() {
        if (name.equals("synapse_at_location"))
        {
            if (parameters[0].getClassName().equals("Vec1d"))
            {
                int i = 0;
            }
        }
        
        return name;
    }

    /**
     * Defines the name of this method.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the VRL method options.
     * @return the method options
     */
    public String getOptions() {
        return options;
    }

    /**
     * Defines the VRL method options.
     * @param options the options to set
     */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * Returns the return value info of this method.
     * @return the return value info of this method
     */
    public NativeParamInfo getReturnValue() {
        return returnValue;
    }

    /**
     * Defines the return value info of this method.
     * @param returnValue the value info to set
     */
    public void setReturnValue(NativeParamInfo returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * Returns the method parameter infos.
     * @return the method parameter infos
     */
    public NativeParamInfo[] getParameters() {
        return parameters;
    }

    /**
     * Defines the method parameter infos.
     * @param params the parameter infos to set
     */
    public void setParameters(NativeParamInfo[] params) {
        this.parameters = params;
    }

    /**
     * Returns the tooltip string of this method.
     * @return the tooltip string of this method
     */
    public String getToolTip() {
        return toolTip;
    }

    /**
     * Defines the tooltip string of this method.
     * @param tooltip the tooltip string to set
     */
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    /**
     * Returns the help string of this method.
     * @return the help string of this method
     */
    public String getHelp() {
        return help;
    }

    /**
     * Defines the help string of this method.
     * @param help the help string to set
     */
    public void setHelp(String help) {
        this.help = help;
    }

    /**
     * Indicates whether this method does not return a value.
     * @return <code>true</code> if this method does not return a value;
     *         <code>false</code> otherwise
     */
    public boolean returnsVoid() {
        return getReturnValue().getType() == NativeType.VOID;
    }

    /**
     * Indicates whether this is a const method.
     * @return <code>true</code> if this method is const;
     *         <code>false</code> otherwise
     */
    public boolean isConst() {
        return isConst;
    }

    /**
     * Defines whether this shall be a const method.
     * @param asConst the state to set
     */
    public void setConst(boolean isConst) {
        this.isConst = isConst;
    }

    /**
     * @return <code>true</code> if this is a constructor;
     *         <code>false</code> otherwise
     */
    public boolean isConstructor() {
        return constructor;
    }

    /**
     * Indicates whether this method represents a constructor method,
     * uses method name <code>constructor</code>.
     * @param constructor the constructor to set
     */
    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    /**
     * @return <code>true</code> if this is a java constructor;
     *         <code>false</code> otherwise
     */
    public boolean isJavaConstructor() {
        return javaConstructor;
    }

    /**
     * Indicates whether this method represents a java constructor. In contrast
     * to constructor method it does not use <code>constructor</code> as method
     * name. Rather than that it's name is equal to the name of the class it
     * belongs to.
     * @param javaConstructor the javaConstructor to set
     */
    public void setJavaConstructor(boolean javaConstructor) {
        this.javaConstructor = javaConstructor;
    }
}
