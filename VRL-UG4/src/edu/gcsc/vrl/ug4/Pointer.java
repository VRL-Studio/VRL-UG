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

    public Pointer(Pointer p) {
        this.address = p.address;
        this.readOnly = p.readOnly;
        this.className = p.className;

        init();
    }

    public Pointer(String className, long address, boolean readOnly) {
        this.address = address;
        this.readOnly = readOnly;
        this.className = className;

        init();
    }

    public Pointer(String className, long address) {
        this.address = address;
        this.readOnly = false;
        this.className = className;

        init();
    }

    public Pointer(long address, boolean readOnly) {
        this.address = address;
        this.readOnly = readOnly;

        init();
    }

    public Pointer(long address) {
        this.address = address;
        this.readOnly = false;

        init();
    }

    private void init() {
        MemoryManager.retain(this);
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

//    @Override
//    protected void finalize() throws Throwable {
//
//        try {
//            MemoryManager.deletePointer(this);
//        } catch (Throwable ex) {
//            //
//        } finally {
//            super.finalize();
//        }
//    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    public boolean isClsNameLocked() {
        return className != null;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        if (!isClsNameLocked()) {
            this.className = className;
        }
    }

    @Override
    public String toString() {
        return "Pointer: class=" + className
                + ", address=" + address + ", const=" + isConst();
    }
}
