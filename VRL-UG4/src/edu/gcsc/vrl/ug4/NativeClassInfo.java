/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeClassInfo {
    private String name;
    private String category;
    private String[] classNames;
    private NativeMethodGroupInfo[] methods;
    private NativeMethodGroupInfo[] constMethods;
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
    public String getCategory() {
        return category;
    }

    /**
     * @param categoryGroup the categoryGroup to set
     */
    public void setCategory(String category) {
        this.category = category;
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
    public NativeMethodGroupInfo[] getMethods() {
        return methods;
    }

    /**
     * @return the constMethods
     */
    public NativeMethodGroupInfo[] getConstMethods() {
        return constMethods;
    }

    /**
     * @param methods the methods to set
     */
    public void setMethods(NativeMethodGroupInfo[] methods) {
        this.methods = methods;

        for (NativeMethodGroupInfo m : methods) {
            m.setConst(false);
        }
    }

    /**
     * @param constMethods the constMethods to set
     */
    public void setConstMethods(NativeMethodGroupInfo[] constMethods) {
        this.constMethods = constMethods;

        System.out.println("Const-Methods:" + constMethods.length);

        for (NativeMethodGroupInfo m : constMethods) {
            m.setConst(true);
        }
    }
}
