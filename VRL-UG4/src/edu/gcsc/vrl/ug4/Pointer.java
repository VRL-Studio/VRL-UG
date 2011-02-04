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
            if (getClassName() != null) {
                MemoryManager.deletePointer(this.getAddress(), getClassName());
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

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }
}
