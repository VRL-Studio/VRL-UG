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
    private boolean inherited;
    private CodeType type;
    private ArrayList<MethodSignature> signatures;
    private NativeAPIInfo api;

    /**
     * Constructor.
     * @param methodInfo method info
     * @param type code type
     * @param visual defines whether to generate code that shall be visualized
     * @param inherited indicates whether this method is inherited
     *                  from base class
     */
    public MethodGroupCode(NativeAPIInfo api,
            NativeMethodGroupInfo methodInfo,
            ArrayList<MethodSignature> signatures,
            CodeType type, boolean visual, boolean inherited) {
        this.methodInfo = methodInfo;
        this.visual = visual;
        this.type = type;
        this.signatures = signatures;
        this.api = api;
        this.inherited = inherited;
    }

    /**
     * Constructor.
     * @param methodInfo method info
     * @param type code type
     * @param visual defines whether to generate code that shall be visualized
     * @param inherited indicates whether this method is inherited
     *                  from base class
     */
    public MethodGroupCode(NativeAPIInfo api,
            NativeMethodGroupInfo methodInfo,
            CodeType type, boolean visual, boolean inherited) {
        this.methodInfo = methodInfo;
        this.visual = visual;
        this.type = type;
        this.api = api;
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        boolean signaturesWereNull = signatures == null;

        if (signatures == null) {
            signatures = new ArrayList<MethodSignature>();
        }

        for (NativeMethodInfo m : methodInfo.getOverloads()) {

            m = NativeClassGroupInfo.convertToClassGroup(api, m);

            // forbid method duplicates
            if (signatures == null || !signatures.contains(new MethodSignature(m))) {
                new MethodCode(m, methodInfo instanceof NativeFunctionGroupInfo,
                        type, visual, inherited).build(builder);
                if (signatures != null) {
                    signatures.add(new MethodSignature(m));
                }
            }
        }

        if (signaturesWereNull) {
            signatures = null;
        }

        return builder;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }
}
