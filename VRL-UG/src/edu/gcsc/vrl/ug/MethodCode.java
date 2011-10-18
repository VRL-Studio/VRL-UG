/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;

/**
 * Code element that generates method code.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MethodCode implements CodeElement {

    private NativeMethodInfo method;
    private final CodeType type;
    private final boolean visual;
    private final boolean function;
    private final boolean inherited;

    /**
     * Constructor
     * @param method method
     * @param type code type
     * @param visual defines whether to generate code that shall be visualized
     * @param inherited indicates whether this method is inherited
     *                  from base class
     */
    public MethodCode(NativeMethodInfo method, boolean function,
            CodeType type, boolean visual, boolean inherited) {
        this.method = method;
        this.type = type;
        this.visual = visual;
        this.function = function;
        this.inherited = inherited;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {
        return build(builder, null);
    }

    /**
     * Builds this code element.
     * @param builder bulder to use
     * @param customInvocationCode optional custom invocation code;
     *        if <code>null</code> is specified default invocation code is used
     * @return specified code builder
     */
    public CodeBuilder build(CodeBuilder builder,
            String customInvocationCode) {

        boolean isFunction = function;

        boolean asInterface = type == CodeType.INTERFACE;
        boolean asWrapper = type == CodeType.WRAP_POINTER_CLASS;
        boolean asFullClass = type == CodeType.FULL_CLASS;

        String methodName = "";

        if (method.isConst() && !isFunction) {
            methodName = "const__";
        }

        String modifier = "public";

        if (isFunction) {
            modifier += " static";
            methodName += "invoke";
        } else {
            methodName += CodeUtils.methodName(method.getName());
        }

        new MethodInfoCode(method, visual, inherited).build(builder).
                newLine().append(modifier + " "
                + method.getReturnValue().getTypeClassName() + " "
                + methodName + " (");

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
            } else if (method.isConstructor()) {
                if (inherited) {
                    builder.append("throw new UnsupportedOperationException(\""
                            + "This constructor is inherited from base class. "
                            + "Inherited constructor methods must "
                            + "not be called!\");").newLine();
                } else {
                    builder.append("invokeConstructor(params);").newLine();
                }
            } else if (isFunction) {
                builder.append("invokeFunction(\""
                        + method.getName() + "\", params);").newLine();
            } else {
                builder.append("invokeMethod("
                        + method.isConst()
                        + ", \"" + method.getName() + "\", params);").newLine();
            }

            if (returnsPointer) {
                String returnTypeClassName =
                        CodeUtils.className(
                        method.getReturnValue().getClassName(), method.isConst());

                builder.append("edu.gcsc.vrl.ug.").append(returnTypeClassName).
                        append(" convertedResult = new ").
                        append("edu.gcsc.vrl.ug.").
                        append(returnTypeClassName).append("();").newLine();

//                builder.addLine("println result;");

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

        return builder;
    }
}
