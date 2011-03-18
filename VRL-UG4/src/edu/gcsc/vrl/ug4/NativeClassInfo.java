/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

/**
 * This class contains all properties of a native class that are necessary to
 * generate code for wrapper classes.
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
     * Returns the class name.
     * @return the class name
     */
    public String getName() {
        return name;
    }

    /**
     * Defines the class name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the component category.
     * @return the component category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Defines the component category of this class info.
     * @param category the category to set
     */
    public void setCategory(String category) {
        if (category==null || category.equals("")) {
            category = "ug4";
        }
        this.category = category;
    }

    /**
     * Returns the names of the base classes.
     * @return names of the base classes
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
     * Defines the names of the base classes.
     * @param classNames the names to set
     */
    public void setClassNames(String[] classNames) {
        this.classNames = classNames;
    }

    /**
     * Indicates wehther this class is instantiable.
     * @return <code>true</code> if this class is instantiable;
     *         <code>false</code> otherwise
     */
    public boolean isInstantiable() {
        return instantiable;
    }

    /**
     * Defines whether this class shall be instantiable.
     * @param isInstantiable the state to set
     */
    public void setInstantiable(boolean isInstantiable) {
        this.instantiable = isInstantiable;
    }

    /**
     * Returns the method groups of this class.
     * @return method groups of this class
     */
    public NativeMethodGroupInfo[] getMethods() {
        return methods;
    }

    /**
     * Returns the const method groups of this class.
     * @return const method groups of this class
     */
    public NativeMethodGroupInfo[] getConstMethods() {
        return constMethods;
    }

    /**
     * Defines the method groups of this class
     * @param methods method groups to set
     */
    public void setMethods(NativeMethodGroupInfo[] methods) {
        this.methods = methods;

        for (NativeMethodGroupInfo m : methods) {
            m.setConst(false);
        }
    }

    /**
     * Defines the const method groups of this class
     * @param constMethods const method groups to set
     */
    public void setConstMethods(NativeMethodGroupInfo[] constMethods) {
        this.constMethods = constMethods;

        for (NativeMethodGroupInfo m : constMethods) {
            m.setConst(true);
        }
    }
}
