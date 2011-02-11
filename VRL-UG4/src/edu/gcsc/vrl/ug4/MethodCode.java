/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.CodeBuilder;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MethodCode {

    private NativeMethodInfo method;
    private final boolean asInterface;
    private final boolean asConst;
    private final boolean visual;

    public MethodCode(NativeMethodInfo method,
            boolean asInterface, boolean asConst, boolean visual) {
        this.method = method;
        this.asInterface = asInterface;
        this.asConst = asConst;
        this.visual = visual;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }

    public CodeBuilder toString(CodeBuilder builder) {

        String methodPrefix = "";

        if (asConst) {
            methodPrefix = "const_";
        }

        new MethodInfoCode(method).toString(builder).
                newLine().append("public "
                + method.getReturnValue().getTypeClassName() + " "
                + methodPrefix
                + CodeUtils.methodName(
                method.getName()) + " (");

        if (method.getParameters().length > 0) {
            builder.newLine().incIndentation();
        }
        new ParamListCode(method.getParameters(),
                !asInterface).toString(builder);
        if (method.getParameters().length > 0) {
            builder.newLine().decIndentation();
        }
        
        builder.append(")");

        if (!asInterface) {

            builder.append(" {").newLine().incIndentation();

            String params = "Object[] params = [";

            for (int i = 0; i < method.getParameters().length; i++) {

                if (i > 0) {
                    params += ", ";
                }
                params += "p" + i;
            }
            params += "]";

            builder.addLine(params).
                    addLine("edu.gcsc.vrl.ug4.UG4.getUG4().invokeMethod("
                    + "getClassName(), getPointer().getAddress(),"
                    + asConst + ", \"" + method.getName() + "\", params);");

            builder.decIndentation().addLine("}");
        } else {
            builder.append(";").newLine();
        }


        return builder;
    }
}
