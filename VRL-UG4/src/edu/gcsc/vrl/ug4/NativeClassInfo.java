/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeClassInfo {
    private String name;
    private String categoryGroup;
    private String[] classNames;
    private NativeMethodInfo[] methods;
    private NativeMethodInfo[] constMethods;
    private boolean instantiable;

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
     * @return the categoryGroup
     */
    public String getCategoryGroup() {
        return categoryGroup;
    }

    /**
     * @param categoryGroup the categoryGroup to set
     */
    public void setCategoryGroup(String categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    /**
     * @return the classNames
     */
    public String[] getClassNames() {
        return classNames;
    }

    public String[] getBaseClassNames() {
        String[] baseClassNames = null;
        if (getClassNames().length>0) {
            baseClassNames = new String[getClassNames().length-1];

            for (int i = 0; i < getClassNames().length-1; i++) {
                baseClassNames[i] = getClassNames()[i+1];
            }
        }

        return baseClassNames;
    }

    /**
     * @param classNames the classNames to set
     */
    public void setClassNames(String[] classNames) {
        this.classNames = classNames;
    }

    /**
     * @return the isInstantiable
     */
    public boolean isInstantiable() {
        return instantiable;
    }

    /**
     * @param isInstantiable the isInstantiable to set
     */
    public void setInstantiable(boolean isInstantiable) {
        this.instantiable = isInstantiable;
    }

    /**
     * @return the methods
     */
    public NativeMethodInfo[] getMethods() {
        return methods;
    }

    /**
     * @return the constMethods
     */
    public NativeMethodInfo[] getConstMethods() {
        return constMethods;
    }

    /**
     * @param methods the methods to set
     */
    public void setMethods(NativeMethodInfo[] methods) {
        this.methods = methods;
    }

    /**
     * @param constMethods the constMethods to set
     */
    public void setConstMethods(NativeMethodInfo[] constMethods) {
        this.constMethods = constMethods;
    }
}
