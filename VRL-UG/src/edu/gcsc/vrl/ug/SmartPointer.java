/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import java.io.Serializable;

/**
 * This class wrapps native UG smart pointers with additional type info (class
 * name) that can be used by UGs registry to call native functions or methods.
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
final class SmartPointer extends Pointer implements Serializable {

    /**
     * Memory the native smart pointer object is stored
     */
    private byte[] smartPointer;

    private boolean invalidated = false;

    /*
     * No public instanciation allowed. This constructor is only accessible from
     * within this class or native methods.
     */
    private SmartPointer(long address, byte[] smartPtr, boolean readOnly) {
        super(address, readOnly);

        this.smartPointer = smartPtr;

//        System.out.println("[SMART Java]: " + getAddress() + ", cont=" + isConst());
    }

    /*
     * No public instanciation allowed. This constructor is only accessible from
     * within this class or native methods.
     */
    private SmartPointer(String className, long address, byte[] smartPtr, boolean readOnly) {
        super(className, address, readOnly);

        this.smartPointer = smartPtr;

//        System.out.println("[SMART Java]: " + getAddress() + ", cont=" + isConst());
    }

    /**
     * Casts the specified smartpointer to a smart pointer of the specified
     * type.
     *
     * <b>Note:</b> use with care! This performs a native cast that may lead to
     * memory errors that cannot be handled by the JVM. Both objects share the
     * same native smartpointer instance.
     *
     * @param newClassName new type
     * @return smartpointer
     */
    @Override
    public SmartPointer cast(String newClassName) {
        return cast(newClassName, isConst());
    }

    /**
     * Casts the specified smartpointer to a smart pointer of the specified
     * type.
     *
     * <b>Note:</b> use with care! This performs a native cast that may lead to
     * memory errors that cannot be handled by the JVM. Both objects share the
     * same native smartpointer instance. Also note that casting from const to
     * non-const is not supported. In this case the resulting pointer will be
     * const.
     *
     * @param newClassName new type
     * @param asConst defines whether to cast to const
     * @return smartpointer
     */
    @Override
    public SmartPointer cast(String newClassName, boolean asConst) {
        SmartPointer sp = new SmartPointer(newClassName,
                this.getAddress(), this.getSmartPointer(),
                this.isConst() || asConst);

        sp.invalidated = true;

        return sp;
    }

//    /**
//     *  !!! DO NOT USE !!! 
//     * NEEDED TO FULFIL JAVABEAN SECIFICATIONS AND
//     * BE ABLE TO BE SEND WITH XMLRPC ! ! !
//     * 
//     * @param smartPointer the smartPointer to set
//     */
//    public void setSmartPointer(byte[] smartPointer) {
//        this.smartPointer = smartPointer;
//    }
    /**
     * Returns the memory that contains the native smart pointer.
     *
     * @return the smartPointer
     */
    public byte[] getSmartPointer() {
        byte[] result = smartPointer;

        return result;
    }

    /**
     * Invalidates native smart pointer.
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        try {
//            System.out.println("~[SMART Java]: " + getAddress() + ", cont=" + isConst());
//            UG.invalidate(this);

            if (!invalidated) {
                UG.invalidate(SmartPointer.this);
            }

        } catch (Throwable ex) {
            //
        } finally {
            super.finalize();
        }
    }

    @Override
    public String toString() {
        return "SmartPointer: class=" + getClassName()
                + ", address=" + getAddress()
                + ", const=" + isConst();
    }

}
