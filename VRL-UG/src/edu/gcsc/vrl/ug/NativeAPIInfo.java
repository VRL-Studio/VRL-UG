/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeAPIInfo implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private NativeClassGroupInfo[] classGroups;
    private NativeClassInfo[] classes;
    private NativeFunctionGroupInfo[] functions;
    private Map<String, NativeClassGroupInfo> classGroupsByClassName;
    private Map<String, NativeClassGroupInfo> classGroupsbyName;
//    private Map<String, NativeFunctionGroupInfo> functionGroupsbyName;
//    private Map<String, NativeFunctionGroupInfo> functionGroupsbyFunctionName;

    /**
     * @return the classes
     */
    public NativeClassInfo[] getClasses() {
        return classes;
    }

    /**
     * @param classes the classes to set
     */
    public void setClasses(NativeClassInfo[] classes) {
        this.classes = classes;
    }

    /**
     * @return the functions
     */
    public NativeFunctionGroupInfo[] getFunctions() {
        return functions;
    }

    /**
     * @param functions the functions to set
     */
    public void setFunctions(NativeFunctionGroupInfo[] functions) {
        this.functions = functions;
    }

    /**
     * Returns the class info by name.
     * @param name
     * @return 
     */
    public NativeClassInfo getClassByName(String name) {

        NativeClassInfo result = null;

        for (NativeClassInfo nC : classes) {
            if (nC.getName().equals(name)) {
                result = nC;
                break;
            }
        }

        return result;
    }

    /**
     * Returns the class info by name.
     * @param name
     * @return 
     */
    public boolean isClassRegisteredByName(String name) {

        return getClassByName(name) != null;
    }

    /**
     * Returns the base class info objects of the specified base class string.
     * @param classInfo
     * @return the base class info objects of the specified base class string
     */
    public NativeClassInfo[] baseClasses(NativeClassInfo classInfo) {

        String[] baseClassNames = classInfo.getBaseClassNames();

        NativeClassInfo[] result =
                new NativeClassInfo[baseClassNames.length];

        for (int i = 0; i < baseClassNames.length; i++) {

            String baseClsName = baseClassNames[i];

            if (!isClassGroup(baseClsName)) {
                // class not in group, retrieve class directly
                result[i] = getClassByName(baseClsName);
            } else {
                // name specifies group. thus, retrieve first class in group
                // which will later be substituted with group info
                result[i] = getClassByName(getGroupByName(baseClsName).
                        getClasses()[0]);
            }
        }

        return result;
    }

    /**
     * @return the classGroups
     */
    public NativeClassGroupInfo[] getClassGroups() {
        return classGroups;
    }

    /**
     * @param classGroups the classGroups to set
     */
    public void setClassGroups(NativeClassGroupInfo[] classGroups) {
        this.classGroups = classGroups;
    }

    /**
     * Returns the class group by classname or <code>null</code> if the
     * specified class does not exist or is not part of a class group.
     * @param className class name
     * @return the class group by classname or <code>null</code> if the
     * specified class does not exist or is not part of a class group
     */
    public NativeClassGroupInfo getGroupByClassName(String className) {

        // we cannot do anything as no information about grous is available
        if (classGroups == null) {
            return null;
        }

        // initialize map if necessary
        if (classGroupsByClassName == null) {
            classGroupsByClassName =
                    new HashMap<String, NativeClassGroupInfo>();

            for (NativeClassGroupInfo grp : classGroups) {
                for (String n : grp.getClasses()) {
                    classGroupsByClassName.put(n, grp);
                }
            }
        }

        return classGroupsByClassName.get(className);
    }

    /**
     * Returns the class group by name or <code>null</code> if the
     * specified class group does not exist or is not part of a class group.
     * @param className class name
     * @return the class group by name or <code>null</code> if the
     * specified class group does not exist or is not part of a class group
     */
    public NativeClassGroupInfo getGroupByName(String className) {

        // we cannot do anything as no information about grous is available
        if (classGroups == null) {
            return null;
        }

        // initialize map if necessary
        if (classGroupsbyName == null) {
            classGroupsbyName = new HashMap<String, NativeClassGroupInfo>();

            for (NativeClassGroupInfo grp : classGroups) {
                classGroupsbyName.put(grp.getName(), grp);
            }
        }

        return classGroupsbyName.get(className);
    }

    /**
     * Indicates whether the specified class is child of a class group.
     * @param className class name
     * @return <code>true</code> if the specified class is child of
     *         a class group; <code>false</code> otherwise
     */
    public boolean isInClassGroup(String className) {
        return getGroupByClassName(className) != null;
    }

    /**
     * Determines whether a class group with the specified name exists.
     * @param name
     * @return <code>true</code> if a class group with the specified name
     *         exists; <code>false</code> otherwise
     */
    public boolean isClassGroup(String name) {
        return getGroupByName(name) != null;
    }

//    /**
//     * Returns the function group by name or <code>null</code> if the
//     * specified function group does not exist or is not part of a function group.
//     * @param functionName function group name
//     * @return the function group by name or <code>null</code> if the
//     * specified function group does not exist or is not part of a function group
//     */
//    public NativeFunctionGroupInfo getFunctionGroupByName(String functionName) {
//
//        // we cannot do anything as no information about grous is available
//        if (functions == null) {
//            return null;
//        }
//
//        // initialize map if necessary
//        if (functionGroupsbyName == null) {
//            functionGroupsbyName = new HashMap<String, NativeFunctionGroupInfo>();
//
//            for (NativeFunctionGroupInfo grp : functions) {
//                functionGroupsbyName.put(grp.getName(), grp);
//            }
//        }
//
//        return functionGroupsbyName.get(functionName);
//    }
//
//    /**
//     * Returns the function group by functionname or <code>null</code> if the
//     * specified function group does not exist or is not part of a function group.
//     * @param functionName function name
//     * @return the function group by functionname or <code>null</code> if the
//     * specified function group does not exist or is not part of a function group
//     */
//    public NativeFunctionGroupInfo getFunctionGroupByFunctionName(String functionName) {
//
//        // we cannot do anything as no information about grous is available
//        if (functions == null) {
//            return null;
//        }
//
//        // initialize map if necessary
//        if (functionGroupsbyFunctionName == null) {
//            functionGroupsbyFunctionName =
//                    new HashMap<String, NativeFunctionGroupInfo>();
//
//            for (NativeFunctionGroupInfo grp : functions) {
//
//                for (NativeFunctionInfo fInfo : grp.getOverloads()) {
//                    functionGroupsbyFunctionName.put(fInfo.getName(), grp);
//                }
//            }
//        }
//
//        return functionGroupsbyFunctionName.get(functionName);
//    }
//
//    /**
//     * Indicates whether the specified function is child of a function group.
//     * @param functionName function name
//     * @return <code>true</code> if the specified function is child of
//     *         a function group; <code>false</code> otherwise
//     */
//    public boolean isInFunctionGroup(String functionName) {
//        return getFunctionGroupByFunctionName(functionName) != null;
//    }
//
//    /**
//     * Determines whether a function group with the specified name exists.
//     * @param name
//     * @return <code>true</code> if a function group with the specified name
//     *         exists; <code>false</code> otherwise
//     */
//    public boolean isFunctionGroup(String name) {
//        return getFunctionGroupByName(name) != null;
//    }
}
