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
    private final boolean visual;

    public MethodCode(NativeMethodInfo method,
            boolean asInterface, boolean visual) {
        this.method = method;
        this.asInterface = asInterface;
        this.visual = visual;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }

    public CodeBuilder toString(CodeBuilder builder) {

        boolean isFunction = method instanceof NativeFunctionInfo;

        String methodPrefix = "";

        if (method.isConst() && !isFunction) {
            methodPrefix = "const_";
        }

        new MethodInfoCode(method, visual).toString(builder).
                newLine().append("public "
                + method.getReturnValue().getTypeClassName() + " "
                + methodPrefix
                + CodeUtils.methodName(
                method.getName()) + " (");

        if (method.getParameters().length > 0 || visual) {
            builder.newLine().incIndentation();
        }
        new ParamListCode(method.getParameters(),
                !asInterface, visual).toString(builder);

        if (method.getParameters().length > 0 || visual) {
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

            builder.addLine(params);

//            if (visual) {
//                builder.addLine("updatePointer(id);");
//            }

//            if (isFunction) {
//                builder.addLine(params).
//                        addLine("edu.gcsc.vrl.ug4.UG4.getUG4().invokeFunction("
//                        + "\"" + method.getName() + "\", false, params);");
//            } else {
//                builder.addLine(params).
//                        addLine("edu.gcsc.vrl.ug4.UG4.getUG4().invokeMethod("
//                        + "getClassName(), getPointer().getAddress(),"
//                        + method.isConst()
//                        + ", \"" + method.getName() + "\", params);");
//            }

            boolean returnsPointer =
                    method.getReturnValue().getType()
                    == NativeType.CONST_POINTER
                    || method.getReturnValue().getType() == NativeType.POINTER;

            if (!method.returnsVoid()) {
                builder.append("result = ");
            }

            builder.append("invokeMethod("
                    + isFunction + ", "
                    + method.isConst()
                    + ", \"" + method.getName() + "\", params);").newLine();

            if (returnsPointer) {
                String returnTypeClassName =
                        CodeUtils.className(
                        method.getReturnValue().getClassName());

                builder.append("edu.gcsc.vrl.ug4.").append(returnTypeClassName).
                        append(" convertedResult = new ").
                        append("edu.gcsc.vrl.ug4.").
                        append(returnTypeClassName).append("();").newLine();

                builder.append("convertedResult.newInstance((Pointer)result);").
                        newLine().
                        append("result = convertedResult;").newLine();
            }

            if (!method.returnsVoid()) {
                builder.newLine().append("return result;");
            }

            builder.newLine().decIndentation().append("}").newLine().newLine();
        } else {
            builder.append(";").newLine();
        }


        return builder;
    }
}
