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
    private String className;

    public Pointer(String className, long address, boolean readOnly) {
        this.address = address;
        this.readOnly = readOnly;
        this.className = className;
    }

    public Pointer(String className, long address) {
        this.address = address;
        this.readOnly = false;
        this.className = className;
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
            if (className != null) {
                MemoryManager.deletePointer(this.getAddress(), className);
            } else {
                System.out.println("Bad name: " + this.getAddress());
            }
        } catch (Throwable ex) {
            //
        } finally {
            super.finalize();
        }
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }
}
