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

    public ParamListCode(NativeParamInfo[] params, boolean withParamInfo) {
        this.params = params;
        this.withParamInfo = withParamInfo;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }

    public CodeBuilder toString(CodeBuilder builder) {

        StringBuilder sB = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                sB.append(builder.getIndentString()).
                        append(builder.getIndentString()).append(",\n");
            }
            sB.append(builder.getIndentString()).
                    append(new ParamCode(params[i],i,withParamInfo));
        }

        builder.addLine(sB.toString());

        return builder;
    }
}
