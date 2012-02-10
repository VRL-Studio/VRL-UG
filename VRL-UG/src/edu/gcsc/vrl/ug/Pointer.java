/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import java.io.Serializable;

/**
 * This class wrapps native C/C++ pointers with additional type info
 * (class name) that can be used by UGs registry to call native
 * functions or methods.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Pointer implements Serializable{
    
    
    private static final long serialVersionUID = 1L;

    private boolean readOnly;
    private long address;
    private String className;
    
    public static final long NULL = 0;

    /**
     * Constructor.
     * @param className class name
     * @param address address (native pointer)
     * @param readOnly indicates whether this shall be a const pointer
     */
    public Pointer(String className, long address,
            boolean readOnly) {
        this.address = address;
        this.readOnly = readOnly;
        this.className = className;
        init();
    }

    /**
     * Constructor.
     * @param address address (native pointer)
     * @param readOnly indicates whether this shall be a const pointer
     */
    public Pointer(long address, boolean readOnly) {
        this.address = address;
        this.readOnly = readOnly;

        init();
    }

    /**
     * Initializes this pointer.
     */
    private void init() {
//        MemoryManager.retain(this);
    }

    /**
     * Returns the pointer address.
     * @return the pointer address
     */
    public long getAddress() {
        return address;
    }

    /**
     * Indicates whether this pointer is const.
     * @return <code>true</code> if this pointer is const;
     *         <code>false</code> otherwise
     */
    public boolean isConst() {
        return readOnly;
    }

    /**
     * Defines whether this pointer is const.
     * @param b state to set
     */
    public void setConst(boolean b) {
        readOnly = b;
    }

    /**
     * Returns the class name
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Indicates whether the class name of the type is locked.
     * @return <code>true</code> if the class name of the type is locked;
     *         <code>false</code> otherwise
     */
    public boolean isClsNameLocked() {
        return className != null;
    }

    /**
     * <p>
     * Defines the class name.
     * </p>
     * <p>
     * <b>Note:</b> This method only has an effect if
     * {@link #isClsNameLocked() } returns false
     * </p>
     * @param className the class name to set
     */
    public void setClassName(String className) {
        if (!isClsNameLocked()) {
            this.className = className;
        }
    }

    @Override
    public String toString() {
        return "Pointer: class=" + className
                + ", address=" + address
                + ", const=" + isConst();
    }

    /**
     * Indicates whether the specified object is an ug pointer.
     * @param o object to check
     * @return <code>true</code> if the specified object is an ug pointer;
     *         <code>false</code> otherwise
     */
    public static boolean isInstance(Object o) {
        boolean result = Pointer.class.isInstance(o);
        return result;
    }
}
