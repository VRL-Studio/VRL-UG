/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeMethodInfo {
    private String name;
    private String options;
    private NativeParamInfo returnValue;
    private NativeParamInfo[] parameters;
    private String toolTip;
    private String help;

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
     * @return the options
     */
    public String getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * @return the returnValue
     */
    public NativeParamInfo getReturnValue() {
        return returnValue;
    }

    /**
     * @param returnValue the returnValue to set
     */
    public void setReturnValue(NativeParamInfo returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * @return the parameters
     */
    public NativeParamInfo[] getParameters() {
        return parameters;
    }

    public void setParameters(NativeParamInfo[] params) {
        this.parameters = params;
    }
    

    /**
     * @return the tooltip
     */
    public String getToolTip() {
        return toolTip;
    }

    /**
     * @param tooltip the tooltip to set
     */
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    /**
     * @return the help
     */
    public String getHelp() {
        return help;
    }

    /**
     * @param help the help to set
     */
    public void setHelp(String help) {
        this.help = help;
    }
}
