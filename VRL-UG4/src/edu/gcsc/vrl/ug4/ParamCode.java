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
public class ParamCode {

    private NativeParamInfo param;
    private boolean withParamInfo;
    private int index;

    public ParamCode(NativeParamInfo param, int index, boolean withParamInfo) {
        this.param = param;
        this.withParamInfo = withParamInfo;
        this.index = index;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }

    public CodeBuilder toString(CodeBuilder builder) {

        builder.incIndentation();

        if (withParamInfo) {
            new ParamInfoCode(param).toString(builder);
        }

        builder.append(param.getTypeClassName()).append(" p"+index);

        builder.decIndentation();

        return builder;
    }
}
