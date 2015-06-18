/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import edu.gcsc.vrl.ug.types.NativeType;
import java.io.Serializable;

/**
 * This class contains all properties of a parameter that are necessary to
 * generate parameter code for wrapper methods/functions.
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeParamInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private NativeType type;
    private String className;
    private String[] classNames;
    private int id;
    private String help;
    private String tooltip;
    private String[] paramInfo;
    private boolean isAVector = false;
    
    public NativeParamInfo() {
    }

    public NativeParamInfo(NativeParamInfo p) {
        this.type = p.type;
        this.className = p.className;

        if (p.classNames != null) {
            this.classNames = p.classNames.clone();
        }
        this.id = p.id;
        this.help = p.help;
        this.tooltip = p.tooltip;
        this.paramInfo = p.paramInfo;
        
        this.isAVector = p.isVector();
    }

    /**
     * Returns the native parameter type.
     *
     * @return the native parameter type
     */
    public NativeType getType() {
        return type;
    }

    /**
     * Defines the native parameter type.
     *
     * @param type the native parameter type to set
     */
    public void setType(NativeType type) {
        this.type = type;
    }

    /**
     * <p> Defines the native parameter type of this parameter via integers.
     * </p> <p> <b>Note:</b> the purpose of this method is to convert native C++
     * enums to type safe Java enums </p>
     *
     * @param type the native parameter type to set
     */
    private void setType(int type) {

        /*
         * how the old order (before 20130522) is represented by the new one
         * the numbers/oder must match with the order in NativeType.java and
         * that order must match with c++ code from ug project
         * from file trunk/ugbase/common/util/variant.cpp
         * 
         * matched by christian poliwoda
         * 
         //     // OLD //          
         //     PT_UNKNOWN = 0, -> INVALID  <- UNDEFINED
         //	PT_BOOL = 1,
         //	PT_INTEGER = 2,
         //     PT_NUMBER = 3, -> 3,4,5
         //	PT_CSTRING = 4, -> 6
         //	PT_STD_STRING = 5, -> 7
         //	PT_POINTER = 6, ->8
         //	PT_CONST_POINTER = 7, -> 9
         //	PT_SMART_POINTER = 8, -> 10
         //	PT_CONST_SMART_POINTER = 9 -> 11
        
         //    // NEW //       
         //    VT_INVALID = 0,
         //    VT_BOOL = 1,
         //    VT_INT = 2,
         //    VT_SIZE_T = 3,
         //    VT_FLOAT = 4,
         //    VT_DOUBLE = 5,
         //    VT_CSTRING = 6,
         //    VT_STDSTRING = 7,
         //    VT_POINTER = 8,
         //    VT_CONST_POINTER = 9,
         //    VT_SMART_POINTER = 10,
         //    VT_CONST_SMART_POINTER = 11
         */

        switch (type) {
            case -1:
                setType(NativeType.VOID);
                break;
            case 0:
                setType(NativeType.INVALID);
                break;
            case 1:
                setType(NativeType.BOOL);
                break;
            case 2:
                setType(NativeType.INT);
                break;
            case 3:
                setType(NativeType.SIZE_T);
                break;
            case 4:
                setType(NativeType.FLOAT);
                break;
            case 5:
                setType(NativeType.DOUBLE);
                break;
            case 6:
                setType(NativeType.CSTRING);
                break;
            case 7:
                setType(NativeType.STDSTRING);
                break;
            case 8:
                setType(NativeType.POINTER);
                break;
            case 9:
                setType(NativeType.CONST_POINTER);
                break;
            case 10:
                setType(NativeType.SMART_POINTER);
                break;
            case 11:
                setType(NativeType.CONST_SMART_POINTER);
                break;
            default:
                setType(NativeType.INVALID);
        }


        /* order/conversion before 20130522
         * commented out and not deleted to have a backup
         */
        /* order before 20130522
         BOOL,
         INTEGER,
         NUMBER,
         STRING,
         POINTER,
         CONST_POINTER,
         SMART_POINTER,
         CONST_SMART_POINTER,
         VOID,
         UNDEFINED
         */
//        // convert type
//        switch (type) {
//            case -1:
//                setType(NativeType.VOID);
//                break;
//            case 0:
//                setType(NativeType.UNDEFINED);
//                break;
//            case 1:
//                setType(NativeType.BOOL);
//                break;
//            case 2:
//                setType(NativeType.INTEGER);
//                break;
//            case 3:
//                setType(NativeType.NUMBER);
//                break;
//            case 4:
//                setType(NativeType.STRING);
//                break;
//            case 5:
//                setType(NativeType.STRING);
//                break;
//            case 6:
//                setType(NativeType.POINTER);
//                break;
//            case 7:
//                setType(NativeType.CONST_POINTER);
//                break;
//            case 8:
//                setType(NativeType.SMART_POINTER);
//                break;
//            case 9:
//                setType(NativeType.CONST_SMART_POINTER);
//                break;
//            default:
//                setType(NativeType.UNDEFINED);
//        }
    }

    /**
     * Returns the id of this parameter.
     *
     * @return the id of this parameter
     */
    public int getId() {
        return id;
    }

    /**
     * Defines the id of this parameter.
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the help string of this parameter.
     *
     * @return the help string of this parameter
     */
    public String getHelp() {
        return help;
    }

    /**
     * Defines the help string of this parameter.
     *
     * @param help the help string to set
     */
    public void setHelp(String help) {
        this.help = help;
    }

    /**
     * Returns the tooltip string of this parameter.
     *
     * @return the tooltip string of this parameter
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Defines the tooltip string of this parameter.
     *
     * @param tooltip the tooltip string to set
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * Returns the param info string (annotation string).
     *
     * @return the param info string (annotation string)
     */
    public String[] getParamInfo() {
        return paramInfo;
    }

    /**
     * Defines the param info string (annotation string).
     *
     * @param paramInfo the param info string to set
     */
    public void setParamInfo(String[] paramInfo) {
        this.paramInfo = paramInfo;
    }

    /**
     * Returns the class name of this parameter.
     *
     * @return the class name of this parameter
     */
    public String getClassName() {
        return className;
    }

    /**
     * Defines the class name of this parameter.
     *
     * @param className the class name to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Returns the class names class names of the base classes of this parameter
     * class.
     *
     * @return the class names of the base classes of this parameter class
     */
    public String[] getClassNames() {
        return classNames;
    }

    /**
     * Defines the class names class names of the base classes of this parameter
     * class
     *
     * @param classNames the class names to set
     */
    public void setClassNames(String[] classNames) {
        this.classNames = classNames;
    }

    /**
     * Returns the Java class name of this parameter type.
     *
     * @return the Java class name of this parameter type
     */
    public String getTypeClassName() {
        
        String result = "/*ERROR!!! INVALID TYPE [type=" + getType() + "]*/ edu.gcsc.vrl.ug.INVALID_TYPE";
        
        switch (getType()) {
            case VOID:
                result = "void";
                break;
            case BOOL:
                result = "java.lang.Boolean";
                break;
            case INT:
                result = "java.lang.Integer";
                break;
            case SIZE_T: //no unsigned type in java therefore cast to integer
                result = "java.lang.Integer";
                break;
            case FLOAT:
                result = "java.lang.Float";
                break;
            case DOUBLE:
                result = "java.lang.Double";
                break;
            case CSTRING: //"C"STRING cast to normal string 
                result = "java.lang.String";
                break;
            case STDSTRING: //"STD"STRING cast to normal string 
                result = "java.lang.String";
                break;
            case POINTER:
                result = CodeUtils.interfaceName(getClassName(), false);
                break;
            case CONST_POINTER:
                result = CodeUtils.interfaceName(getClassName(), true);
                break;
            case SMART_POINTER:
                result = CodeUtils.interfaceName(getClassName(), false);
                break;
            case CONST_SMART_POINTER:
                result = CodeUtils.interfaceName(getClassName(), true);
                break;
            default:
                System.err.println(result);
        }

        //
        // check now if there are lists / vectors / arrays
        //

        if (isAVector)
        {
            result = result+"[]";
            //result = "java.util.List<"+result+">";
        }
        
        return result;
    }
    
    
    /**
     * Indicates whether this parameter is const.
     *
     * @return
     * <code>true</code> if this parameter is const;
     * <code>false</code> otherwise
     */
    public boolean isConst() {
        return type == NativeType.CONST_POINTER
                || type == NativeType.CONST_SMART_POINTER;
    }

    /**
     * Determines whether the type of this parameter is a registered ug class
     * (if the type if this parameter is a (const)pointer or
     * (const)smart-pointer).
     *
     * @return
     * <code>true</code> if type of this parameter is a registered ug class;
     * <code>false</code> otherwise
     */
    public boolean isRegisteredClass() {
        return getType() == NativeType.CONST_POINTER
                || getType() == NativeType.POINTER
                || getType() == NativeType.CONST_SMART_POINTER
                || getType() == NativeType.SMART_POINTER;
    }

  /**
     * Indicates whether this parameter is a list / vector.
     * This method should be equivalent to the isVector method on c++
     * side in native ug for lua-binding.
     *
     * @return
     * <code>true</code> if this parameter is a vector;
     * <code>false</code> otherwise
     */
    public boolean isVector() {
        return isAVector;
    }

    /**
     * @param _isVector whether or not to set the isAVector flag
     */
    public void setVectorFlag(boolean _isVector) {
        this.isAVector = _isVector;
    }
}
