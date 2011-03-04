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
public class MethodCode implements CodeElement{

    private NativeMethodInfo method;
    private final CodeType type;
    private final boolean visual;

    public MethodCode(NativeMethodInfo method,
            CodeType type, boolean visual) {
        this.method = method;
        this.type = type;
        this.visual = visual;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }

    public CodeBuilder build(CodeBuilder builder) {
        return toString(builder, null);
    }

    public CodeBuilder toString(CodeBuilder builder,
            String customInvocationCode) {

        boolean isFunction = method instanceof NativeFunctionInfo;

        boolean asInterface = type == CodeType.INTERFACE;
        boolean asWrapper = type == CodeType.WRAP_POINTER_CLASS;
        boolean asFullClass = type == CodeType.FULL_CLASS;

        String methodPrefix = "";

        if (method.isConst() && !isFunction) {
            methodPrefix = "const_";
        }

        new MethodInfoCode(method, visual).build(builder).
                newLine().append("public "
                + method.getReturnValue().getTypeClassName() + " "
                + methodPrefix
                + CodeUtils.methodName(
                method.getName()) + " (");

        if (method.getParameters().length > 0 || visual) {
            builder.newLine().incIndentation();
        }
        new ParamListCode(method.getParameters(),
                !asInterface, visual).build(builder);

        if (method.getParameters().length > 0 || visual) {
            builder.newLine().decIndentation();
        }

        builder.append(")");

        if (asFullClass || asWrapper) {

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
                    || method.getReturnValue().getType()
                    == NativeType.POINTER
                    || method.getReturnValue().getType()
                    == NativeType.CONST_SMART_POINTER
                    || method.getReturnValue().getType()
                    == NativeType.SMART_POINTER;

            if (!method.returnsVoid()) {
                builder.append("Object result = ");
            }

            if (customInvocationCode != null) {
                builder.append(customInvocationCode).newLine();
            } else {
                builder.append("invokeMethod("
                        + isFunction + ", "
                        + method.isConst()
                        + ", \"" + method.getName() + "\", params);").newLine();
            }

            if (returnsPointer) {
                String returnTypeClassName =
                        CodeUtils.className(
                        method.getReturnValue().getClassName());

                builder.append("edu.gcsc.vrl.ug4.").append(returnTypeClassName).
                        append(" convertedResult = new ").
                        append("edu.gcsc.vrl.ug4.").
                        append(returnTypeClassName).append("();").newLine();

                builder.addLine("println result;");

                builder.append("convertedResult.setPointer((Pointer)result);").
                        newLine().
                        append("result = convertedResult;").newLine();
            }

            if (!method.returnsVoid()) {
                builder.newLine().append("return result;");
            }

            builder.newLine().decIndentation().append("}").newLine().newLine();
        } else if (asInterface) {
            builder.append(";").newLine();
        }
//        else if (asWrapper) {
//             builder.append("{").newLine().incIndentation().
//                     append("/*NO IMPLEMENTATION*/").newLine().
//                     append("throw new UnsupportedOperationException(").
//                     newLine().incIndentation().
//                     append("\"This class does not support\"").
//                     newLine().
//                     append("+\"native method execution.\");").
//                     newLine().decIndentation().decIndentation().
//                     append("}").newLine();
//        }

        return builder;
    }
}
