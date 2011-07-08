/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeAPIInfo {
    
    private NativeClassGroupInfo[] classGroups;
    private NativeClassInfo[] classes;
    private NativeFunctionGroupInfo[] functions;

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

    public NativeClassInfo[] baseClasses(NativeClassInfo classInfo) {

        String[] baseClassNames = classInfo.getBaseClassNames();

        NativeClassInfo[] result =
                new NativeClassInfo[baseClassNames.length];

        for (int i = 0; i < baseClassNames.length;i++) {
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
}
