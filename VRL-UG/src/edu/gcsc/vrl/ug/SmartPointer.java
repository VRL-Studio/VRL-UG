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
class SmartPointer extends Pointer implements Serializable{

    /**
     * Memory the native smart pointer object is stored
     */
    private byte[] smartPointer;

    /*
     * No public instanciation allowed. This constructor is only accessible from
     * within this class or native methods.
     */
    private SmartPointer(long address, byte[] smartPtr, boolean readOnly) {
        super(address, readOnly);

        this.smartPointer = smartPtr;
        
//        System.out.println("[SMART Java]: " + getAddress() + ", cont=" + isConst());
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

            UG.invalidate(SmartPointer.this);

        } catch (Throwable ex) {
            //
        } finally {
            super.finalize();
        }
    }

    
}
