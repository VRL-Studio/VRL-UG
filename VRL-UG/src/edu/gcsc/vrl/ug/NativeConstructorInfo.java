/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

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
