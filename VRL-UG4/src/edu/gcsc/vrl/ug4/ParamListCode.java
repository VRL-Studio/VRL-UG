/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.CodeBuilder;

/**
 * Code element that gererates code for parameter lists as used in method
 * headers.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class ParamListCode implements CodeElement {

    private NativeParamInfo[] params;
    private boolean withParamInfo;
    private boolean visual;

    /**
     * Constructor.
     * @param params parameters
     * @param withParamInfo indicates whether parameter code shall include
     *                      param info (annotation code)
     * @param visual defines whether to generate code that shall be visualized
     */
    public ParamListCode(NativeParamInfo[] params, boolean withParamInfo,
            boolean visual) {
        this.params = params;
        this.withParamInfo = withParamInfo;
        this.visual = visual;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        for (int i = 0; i < params.length; i++) {

            if (i > 0) {
                builder.append(", ").newLine();
            }

            new ParamCode(params[i], i, withParamInfo).build(builder);
        }

        if (visual) {

            if (params.length > 0) {
                builder.append(", ").newLine().append("VisualIDRequest id");
            } else {
                builder.append("VisualIDRequest id");
            }
        }

        return builder;
    }
}
