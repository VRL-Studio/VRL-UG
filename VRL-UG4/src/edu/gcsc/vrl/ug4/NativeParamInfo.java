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
    private String className;
    private String[] classNames;
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
        switch (type) {
            case -1:
                setType(NativeType.VOID);
                break;
            case 0:
                setType(NativeType.UNDEFINED);
                break;
            case 1:
                setType(NativeType.BOOL);
                break;
            case 2:
                setType(NativeType.INTEGER);
                break;
            case 3:
                setType(NativeType.NUMBER);
                break;
            case 4:
                setType(NativeType.STRING);
                break;
            case 5:
                setType(NativeType.POINTER);
                break;
            case 6:
                setType(NativeType.CONST_POINTER);
                break;
            default:
                setType(NativeType.UNDEFINED);
        }
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

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the classNames
     */
    public String[] getClassNames() {
        return classNames;
    }

    /**
     * @param classNames the classNames to set
     */
    public void setClassNames(String[] classNames) {
        this.classNames = classNames;
    }

    public String getTypeClassName() {

        switch (getType()) {
            case VOID:
                return "void";
            case BOOL:
                return "Boolean";
            case INTEGER:
                return "Integer";
            case NUMBER:
                return "Double";
            case STRING:
                return "String";
            case POINTER:
                return CodeUtils.interfaceName(getClassName());
            case CONST_POINTER:
                return CodeUtils.interfaceName(getClassName());
        }

        return "/*ERROR!!! INVALID TYPE*/ void";
    }
}
