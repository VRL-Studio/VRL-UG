/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

/**
 * Defines a method signature. This is used to check whether a method has
 * already been generated.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MethodSignature {

    private NativeMethodInfo method;
    private String signature;

    public MethodSignature(NativeMethodInfo method) {
        this.method = method;
        createSignature(method);
    }

    private void createSignature(NativeMethodInfo method) {

        String prefix = "";

        if (method.isConst()) {
            prefix = "const";
        }

        signature = method.getReturnValue().getTypeClassName() + ";"
                + prefix + method.getName()+"(";
        for(NativeParamInfo p : method.getParameters()) {
            signature+=p.getTypeClassName()+";)";
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

        if (!(o instanceof MethodSignature)) {
            return false;
        }

        MethodSignature other = (MethodSignature) o;

        boolean result = this.signature == null && other.signature == null
                || ((this.signature != null && other.signature != null)
                && this.signature.equals(other.signature));

        return result;
    }
    
    @Override
    public String toString() {
        return signature;
    }
}
