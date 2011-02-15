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
public class MethodGroupCode {

    private NativeMethodGroupInfo methodInfo;
    private boolean visual;
    private CodeType type;

    public MethodGroupCode(NativeMethodGroupInfo methodInfo,
            CodeType type, boolean visual) {
        this.methodInfo = methodInfo;
        this.visual = visual;
        this.type = type;
    }

    public CodeBuilder toString(CodeBuilder builder) {

        for (NativeMethodInfo m : methodInfo.getOverloads()) {
            new MethodCode(m, type, visual).toString(builder);
        }

        return builder;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }
}
