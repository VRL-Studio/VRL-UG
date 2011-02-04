/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Pointer {

    private boolean readOnly;
    private long address;

    public Pointer(long address, boolean readOnly) {
        this.address = address;
        this.readOnly = readOnly;
    }

    public Pointer(long address) {
        this.address = address;
        this.readOnly = false;
    }

    /**
     * @return the ptr
     */
    public long getAddress() {
        return address;
    }

    /**
     *
     * @return
     */
    public boolean isConst() {
        return readOnly;
    }

    public void setConst(boolean b) {
        readOnly = b;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            MemoryManager.deletePointer(this.getAddress());
        } catch (Throwable ex) {
            //
        } finally {
            super.finalize();
        }
    }
}
