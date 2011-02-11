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
public class ParamListCode {

    private NativeParamInfo[] params;
    private boolean withParamInfo;
    private boolean visual;

    public ParamListCode(NativeParamInfo[] params, boolean withParamInfo, boolean visual) {
        this.params = params;
        this.withParamInfo = withParamInfo;
        this.visual = visual;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }

    public CodeBuilder toString(CodeBuilder builder) {


        for (int i = 0; i < params.length; i++) {

            if (i > 0) {
                builder.append(", ").newLine();
            }

            new ParamCode(params[i], i, withParamInfo).toString(builder);
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
