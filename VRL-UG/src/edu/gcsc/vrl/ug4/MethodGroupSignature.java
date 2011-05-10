/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

/**
 * Defines a method group signature. This is used to check whether a method 
 * group has already been generated.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MethodGroupSignature {

    private NativeMethodGroupInfo method;
    private String signature;

    /**
     * Constructor.
     * @param method method group info
     */
    public MethodGroupSignature(NativeMethodGroupInfo method) {
        this.method = method;
        createSignature(method);
    }

    /**
     * Creates the signature.
     * @param method method
     */
    private void createSignature(NativeMethodGroupInfo method) {
        for (NativeMethodInfo m : method.getOverloads()) {
            signature+=new MethodSignature(m);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash
                + (this.signature != null ? this.signature.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof MethodGroupSignature)) {
            return false;
        }

        MethodGroupSignature other = (MethodGroupSignature) o;

        boolean result = this.signature == null && other.signature == null
                || ((this.signature != null && other.signature != null)
                && this.signature.equals(other.signature));

        return result;
    }
}
