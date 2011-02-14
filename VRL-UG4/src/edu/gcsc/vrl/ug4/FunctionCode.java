/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class FunctionCode {

    private NativeFunctionGroupInfo function;

    public FunctionCode(NativeFunctionGroupInfo function) {
        this.function = function;
    }

    public CodeBuilder toString(CodeBuilder builder) {

        String functionName = function.getOverloads()[0].getName();
        String functionCategory = function.getOverloads()[0].getCategory();

        String className = CodeUtils.className(functionName);
//        String methodName = CodeUtils.methodName(function.getName());

        builder.append("@ComponentInfo(name=\"").append(
                VLangUtils.addEscapeCharsToCode(
                functionName)).append("\", category=\"").
                append(VLangUtils.addEscapeCharsToCode(functionCategory)).
                append("\")").newLine().
                append("@ObjectInfo(name=\"").
                append(VLangUtils.addEscapeCharsToCode(functionName)).
                append("\")").newLine();

        builder.append("public class ").append(className).append(
                " extends edu.gcsc.vrl.ug4.UGObject {").newLine().
                incIndentation().
                append("private static final long serialVersionUID=1L;").
                newLine().newLine().
                append("public ").append(className).append("() {").newLine().
                incIndentation().append("setClassName(\"").append(className).
                append("\");").newLine().
                decIndentation().append("}").newLine().newLine();


//        new MethodCode(function, false, true).toString(builder);
//        new MethodCode(function, false, false).toString(builder);

        new MethodGroupCode(function, false, true).toString(builder);
        new MethodGroupCode(function, false, false).toString(builder);

        builder.newLine().decIndentation();
        builder.append("}").newLine().
                addLine("// ------------------------------ //\n");

        return builder;
    }
}
