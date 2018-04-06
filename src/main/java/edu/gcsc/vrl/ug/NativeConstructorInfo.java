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

import edu.gcsc.vrl.ug.types.NativeType;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * This class contains all properties of a native method that are
 * necessary to generate code for wrapper methods.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeConstructorInfo implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private String options;
    private NativeParamInfo[] parameters;
    private String toolTip;
    private String help;

    public NativeConstructorInfo() {
    }

    public NativeConstructorInfo(NativeConstructorInfo m) {
        this.options = m.options;
        if (m.parameters != null) {
            this.parameters = m.parameters.clone();
        }
        this.toolTip = m.toolTip;
        this.help = m.help;
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
     * Converts this native constructor info to an equivalent method info.
     * @return this native constructor info as equivalent method info
     */
    public NativeMethodInfo toNativeMethodInfo() {
        NativeMethodInfo result = new NativeMethodInfo();
        
        NativeParamInfo returnType = new NativeParamInfo();
        returnType.setType(NativeType.VOID);
        
        result.setName("constructor");
        result.setConstructor(true);
        result.setConst(false);
        result.setHelp(help);
        result.setToolTip(toolTip);
        
        // currently ignored, see MethodInfoCode for details
        result.setOptions("initializer=true"); 
        
        result.setParameters(parameters);
        result.setReturnValue(returnType);
        
        return result;
    }
   
    /**
     * Converts this native constructor info to an equivalent method info.
     * @param 
     * @return this native constructor info as equivalent method info
     */
    public static NativeMethodGroupInfo toNativeMethodGroupInfo(
            NativeClassInfo cls) {
        
        NativeConstructorInfo[] constructors = cls.getConstructors();
        
        ArrayList<NativeMethodInfo> constructorMethodList = 
                new ArrayList<NativeMethodInfo>();
        
        for (int i = 0; i < constructors.length;i++) {
            constructorMethodList.add(constructors[i].toNativeMethodInfo());
            
            if (constructors[i].getParameters().length>0) {
                NativeMethodInfo contructorMethod = 
                        constructors[i].toNativeMethodInfo();
                
                contructorMethod.setName(cls.getName());
                contructorMethod.setJavaConstructor(true);
                constructorMethodList.add(contructorMethod);
            }

        }
        
        NativeMethodGroupInfo result = new NativeMethodGroupInfo();
        
        result.setConst(false);
        
        NativeMethodInfo[] constructorMethodArray = 
                new NativeMethodInfo[constructorMethodList.size()];
        
        constructorMethodArray = constructorMethodList.
                toArray(constructorMethodArray);
        
        result.setOverloads(constructorMethodArray);
        
        return result;
    }
}
