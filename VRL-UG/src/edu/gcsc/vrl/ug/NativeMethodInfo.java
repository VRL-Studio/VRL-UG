/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

/**
 * This class contains all properties of a native method that are
 * necessary to generate code for wrapper methods.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeMethodInfo {

    private String name;
    private String options;
    private NativeParamInfo returnValue;
    private NativeParamInfo[] parameters;
    private String toolTip;
    private String help;
    private boolean isConst;
    private boolean constructor;

    public NativeMethodInfo() {
    }

    public NativeMethodInfo(NativeMethodInfo m) {
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
    }

    /**
     * Returns the method name.
     * @return the method name
     */
    public String getName() {
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
     * @return the constructor
     */
    public boolean isConstructor() {
        return constructor;
    }

    /**
     * @param constructor the constructor to set
     */
    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }
}
