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

    public MethodInfoCode(NativeMethodInfo method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }

    public CodeBuilder toString(CodeBuilder builder) {

        if (!method.returnsVoid()) {
            builder.append("@MethodInfo(options=\""
                    + VLangUtils.addEscapeCharsToCode(method.getOptions())
                    + "\", valueName=\""
                    + VLangUtils.addEscapeCharsToCode(
                    method.getReturnValue().getParamInfo()[0])
                    + "\", valueStyle=\""
                    + method.getReturnValue().getParamInfo()[1]
                    + "\", valueOptions=\""
                    + method.getReturnValue().getParamInfo()[2] + "\")");
        }

        return builder;
    }
}
