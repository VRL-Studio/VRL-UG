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
public class MethodInfoCode {

    private NativeMethodInfo method;
    private boolean visual;

    public MethodInfoCode(NativeMethodInfo method, boolean visual) {
        this.method = method;
        this.visual = visual;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }

    public CodeBuilder toString(CodeBuilder builder) {

        if (!method.returnsVoid() && visual) {
            builder.append("@MethodInfo(");

            if (method.getOptions() != null && !method.getOptions().isEmpty()) {
                builder.append(method.getOptions()).append(", ");
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
                    + method.getReturnValue().getParamInfo()[2] + "\")");
        } else if (!visual) {
            builder.append("@MethodInfo(noGUI=true)");
        }

        return builder;
    }
}
