/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class SmartPointer extends Pointer {

    private byte[] smartPointer;

    private SmartPointer(long address, byte[] smartPtr, boolean readOnly) {
        super(address, readOnly);

        this.smartPointer = smartPtr;

//        System.out.println("[SMART Java]: " + getAddress() + ", cont=" + isConst());
    }

    /**
     * @return the smartPointer
     */
    public byte[] getSmartPointer() {
        byte[] result = smartPointer;

        return result;
    }

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
