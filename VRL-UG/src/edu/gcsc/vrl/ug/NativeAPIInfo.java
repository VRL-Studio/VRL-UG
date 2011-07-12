/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeAPIInfo {

    private NativeClassGroupInfo[] classGroups;
    private NativeClassInfo[] classes;
    private NativeFunctionGroupInfo[] functions;
    private Map<String, NativeClassGroupInfo> classGroupsByClassName;
    private Map<String, NativeClassGroupInfo> classGroupsbyName;

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

    public NativeClassInfo[] baseClasses(NativeClassInfo classInfo) {

        String[] baseClassNames = classInfo.getBaseClassNames();

        NativeClassInfo[] result =
                new NativeClassInfo[baseClassNames.length];

        for (int i = 0; i < baseClassNames.length; i++) {
            result[i] = getClassByName(baseClassNames[i]);
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
            classGroupsByClassName = new HashMap<String, NativeClassGroupInfo>();

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

    public boolean isInClassGroup(String className) {
        return getGroupByClassName(className) != null;
    }

    public boolean groupExists(String grpName) {
        return getGroupByName(grpName) != null;
    }
}
