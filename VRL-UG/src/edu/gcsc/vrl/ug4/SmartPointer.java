/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

/**
 * This class wrapps native UG smart pointers with additional type info
 * (class name) that can be used by UGs registry to call native
 * functions or methods.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class SmartPointer extends Pointer {

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

    /**
     * Returns the memory that contains the native smart pointer.
     * @return the smartPointer
     */
    public byte[] getSmartPointer() {
        byte[] result = smartPointer;

        return result;
    }

    /**
     * Invalidates native smart pointer.
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        try {
//            System.out.println("~[SMART Java]: " + getAddress() + ", cont=" + isConst());
            MemoryManager.invalidate(this);
        } catch (Throwable ex) {
            //
        } finally {
            super.finalize();
        }
    }
}
