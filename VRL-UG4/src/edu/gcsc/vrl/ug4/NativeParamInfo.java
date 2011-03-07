/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

/**
 * This class contains all properties of a parameter that are
 * necessary to generate parameter code for wrapper methods/functions.
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
     * Returns the native parameter type.
     * @return the native parameter type
     */
    public NativeType getType() {
        return type;
    }

    /**
     * Defines the native parameter type.
     * @param type the native parameter type to set
     */
    public void setType(NativeType type) {
        this.type = type;
    }

    /**
     * <p>
     * Defines the native parameter type of this parameter via integers.
     * </p>
     * <p>
     * <b>Note:</b> the purpose of this method is to convert native C++ enums
     * to type safe Java enums
     * </p>
     * @param type the native parameter type to set
     */
    private void setType(int type) {
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
            case 7:
                setType(NativeType.SMART_POINTER);
                break;
            case 8:
                setType(NativeType.CONST_SMART_POINTER);
                break;
            default:
                setType(NativeType.UNDEFINED);
        }
    }

    /**
     * Returns the id of this parameter.
     * @return the id of this parameter
     */
    public int getId() {
        return id;
    }

    /**
     * Defines the id of this parameter.
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the help string of this parameter.
     * @return the help string of this parameter
     */
    public String getHelp() {
        return help;
    }

    /**
     * Defines the help string of this parameter.
     * @param help the help string to set
     */
    public void setHelp(String help) {
        this.help = help;
    }

    /**
     * Returns the tooltip string of this parameter.
     * @return the tooltip string of this parameter
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Defines the tooltip string of this parameter.
     * @param tooltip the tooltip string to set
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * Returns the param info string (annotation string).
     * @return the param info string (annotation string)
     */
    public String[] getParamInfo() {
        return paramInfo;
    }

    /**
     * Defines the param info string (annotation string).
     * @param paramInfo the param info string to set
     */
    public void setParamInfo(String[] paramInfo) {
        this.paramInfo = paramInfo;
    }

    /**
     * Returns the class name of this parameter.
     * @return the class name of this parameter
     */
    public String getClassName() {
        return className;
    }

    /**
     * Defines the class name of this parameter.
     * @param className the class name to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Returns the class names class names of the base classes of this
     * parameter class.
     * @return the class names of the base classes of this parameter class
     */
    public String[] getClassNames() {
        return classNames;
    }

    /**
     * Defines the class names class names of the base classes of this
     * parameter class
     * @param classNames the class names to set
     */
    public void setClassNames(String[] classNames) {
        this.classNames = classNames;
    }

    /**
     * Returns the Java class name of this parameter type.
     * @return the Java class name of this parameter type
     */
    public String getTypeClassName() {
        String prefix = "Const";

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
                return prefix + CodeUtils.interfaceName(getClassName());
            case SMART_POINTER:
                return CodeUtils.interfaceName(getClassName());
            case CONST_SMART_POINTER:
                return prefix + CodeUtils.interfaceName(getClassName());
        }

        return "/*ERROR!!! INVALID TYPE*/ void";
    }
}
