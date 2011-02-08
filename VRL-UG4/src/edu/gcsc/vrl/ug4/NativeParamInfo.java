/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeParamInfo {
    private NativeType type;
    private int id;
    private String help;
    private String tooltip;
    private String[] paramInfo;

    /**
     * @return the type
     */
    public NativeType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(NativeType type) {
        this.type = type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        // convert type
    }
    

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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

    /**
     * @return the tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * @param tooltip the tooltip to set
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * @return the paramInfo
     */
    public String[] getParamInfo() {
        return paramInfo;
    }

    /**
     * @param paramInfo the paramInfo to set
     */
    public void setParamInfo(String[] paramInfo) {
        this.paramInfo = paramInfo;
    }
}
