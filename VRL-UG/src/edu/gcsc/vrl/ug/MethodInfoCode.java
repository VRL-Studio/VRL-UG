/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;

/**
 * Code element that generates method code.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MethodInfoCode implements CodeElement {

    private NativeMethodInfo method;
    private boolean visual;

    /**
     * Constructor
     * @param method method
     * @param visual defines whether to visualize this method
     */
    public MethodInfoCode(NativeMethodInfo method, boolean visual) {
        this.method = method;
        this.visual = visual;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        if (!method.returnsVoid() && visual) {
            builder.append("@MethodInfo(");

            if (method.getOptions() != null && !method.getOptions().isEmpty()) {
                builder.append(method.getOptions()).append(", ");
            } else {
                builder.append("hide=true, ");
            }

            String valueName = VLangUtils.addEscapeCharsToCode(
                    method.getReturnValue().getParamInfo()[0]);

            if (valueName.isEmpty()) {
                valueName = CodeUtils.className(
                        method.getReturnValue().getClassName());
            }

            builder.append("valueName=\""
                    + valueName
                    + "\", valueStyle=\""
                    + method.getReturnValue().getParamInfo()[1]
                    + "\", valueOptions=\""
                    + method.getReturnValue().getParamInfo()[2]);

            if (method.getReturnValue().isRegisteredClass()) {
                builder.append(";serialization=false");
            }
            
            builder.append("\")");;
        } else if (!visual) {
            builder.append("@MethodInfo(noGUI=true)");
        }

        return builder;
    }
}
