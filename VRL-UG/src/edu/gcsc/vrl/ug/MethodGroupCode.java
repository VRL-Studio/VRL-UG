/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;
import java.util.ArrayList;

/**
 * Code element that generates method group code.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MethodGroupCode implements CodeElement {

    private NativeMethodGroupInfo methodInfo;
    private boolean visual;
    private CodeType type;

    /**
     * Constructor.
     * @param methodInfo method info
     * @param type code type
     * @param visual defines whether to generate code that shall be visualized
     */
    public MethodGroupCode(NativeMethodGroupInfo methodInfo,
            CodeType type, boolean visual) {
        this.methodInfo = methodInfo;
        this.visual = visual;
        this.type = type;
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        ArrayList<MethodSignature> signatures =
                new ArrayList<MethodSignature>();

        for (NativeMethodInfo m : methodInfo.getOverloads()) {

            // forbid method duplicates
            if (!signatures.contains(new MethodSignature(m))) {
                new MethodCode(m, type, visual).build(builder);
                signatures.add(new MethodSignature(m));
            }
        }

        return builder;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }
}
