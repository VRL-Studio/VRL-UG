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
public class MethodGroupCode implements CodeElement {

    private NativeMethodGroupInfo methodInfo;
    private boolean visual;
    private CodeType type;

    public MethodGroupCode(NativeMethodGroupInfo methodInfo,
            CodeType type, boolean visual) {
        this.methodInfo = methodInfo;
        this.visual = visual;
        this.type = type;
    }

    public CodeBuilder build(CodeBuilder builder) {

        for (NativeMethodInfo m : methodInfo.getOverloads()) {
            new MethodCode(m, type, visual).build(builder);
        }

        return builder;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }
}
