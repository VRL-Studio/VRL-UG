/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;

/**
 * Code element that gererates code for UG functions.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class FunctionCode implements CodeElement {

    private NativeFunctionGroupInfo function;
    private NativeAPIInfo api;

    /**
     * Constructor.
     * @param function function
     */
    public FunctionCode(NativeAPIInfo api, NativeFunctionGroupInfo function) {
        this.function = function;
        this.api = api;
    }

    public FunctionCode(NativeAPIInfo api) {
        this.api = api;
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        String functionName = function.getOverloads()[0].getName();
        String functionCategory = function.getOverloads()[0].getCategory();

        String className = CodeUtils.functionName(functionName);
//        String methodName = CodeUtils.methodName(function.getName());


        builder.append("@ComponentInfo(name=\"").append(
                VLangUtils.addEscapeCharsToCode(
                functionName)).append("\", category=\"").
                append(VLangUtils.addEscapeCharsToCode(functionCategory)).
                append("\", allowRemoval=false)").newLine().
                append("@ObjectInfo(name=\"").
                append(VLangUtils.addEscapeCharsToCode(functionName)).
                append("\", referenceIn=false, referenceOut=false)").
                newLine();

        builder.append("public class ").append(className).append(
                " extends edu.gcsc.vrl.ug.UGObject {").newLine().
                incIndentation().
                append("private static final long serialVersionUID=1L;").
                newLine().newLine().
                append("public ").append(className).append("() {").newLine().
                incIndentation().append("setClassName(\"").append(className).
                append("\");").newLine().
                decIndentation().append("}").newLine().newLine();


//        new MethodCode(function, false, true).toString(builder);
//        new MethodCode(function, false, false).toString(builder);
        
        

        new MethodGroupCode(api, function,
                CodeType.FULL_CLASS, true,false).build(builder);
        new MethodGroupCode(api, function,
                CodeType.FULL_CLASS, false,false).build(builder);

//        builder.newLine().
//                append("protected UGObject newInstance(Pointer p) {").
//                newLine().incIndentation().
//                append("UGObject result = new edu.gcsc.vrl.ug.").
//                append(className).append("();").
//                newLine().append("result.setPointer(p);").
//                newLine().append("return result;").newLine().
//                decIndentation().append("}").newLine();

        builder.newLine().decIndentation();
        builder.append("}").newLine().
                addLine("// ------------------------------ //\n");

        return builder;
    }
}
