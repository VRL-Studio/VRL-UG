/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import edu.gcsc.vrl.ug.types.CodeType;
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

        boolean function = methodInfo instanceof NativeFunctionGroupInfo;

        boolean signaturesWereNull = signatures == null;

        if (signatures == null) {
            signatures = new ArrayList<MethodSignature>();
        }

        boolean showMethod = false;

        if (function && visual) {

            // check number of overloads
            int numMethods = 0;
            ArrayList<MethodSignature> tmpSignatures =
                    new ArrayList<MethodSignature>();
            for (NativeMethodInfo m : methodInfo.getOverloads()) {

                m = NativeClassGroupInfo.convertToClassGroup(api, m);
                
                // forbid method duplicates
                if (tmpSignatures == null
                        || !tmpSignatures.contains(new MethodSignature(m))) {
                    numMethods++;
                    if (tmpSignatures != null) {
                        tmpSignatures.add(new MethodSignature(m));
                    }
                }
            }

            // if only one function available show it if not requested otherwise
            // by ug registry
            showMethod = numMethods == 1;
        }



        for (NativeMethodInfo m : methodInfo.getOverloads()) {

            m = NativeClassGroupInfo.convertToClassGroup(api, m);

            // forbid method duplicates
            if (signatures == null
                    || !signatures.contains(new MethodSignature(m))) {
                new MethodCode(m, function,
                        type, visual, inherited, showMethod).build(builder);
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
