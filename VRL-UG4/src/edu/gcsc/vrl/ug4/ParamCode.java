/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.CodeBuilder;

/**
 * Code element that gererates code for native parameters.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class ParamCode implements CodeElement {

    private NativeParamInfo param;
    private boolean withParamInfo;
    private int index;

    /**
     * Constructor.
     * @param param parameter
     * @param index parameter index
     * @param withParamInfo indicates whether parameter code shall include
     *                      param info (annotation code)
     */
    public ParamCode(NativeParamInfo param, int index, boolean withParamInfo) {
        this.param = param;
        this.withParamInfo = withParamInfo;
        this.index = index;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        builder.incIndentation();

        if (withParamInfo) {
            new ParamInfoCode(param).build(builder);
        }

        builder.append(param.getTypeClassName()).append(" p"+index);

        builder.decIndentation();

        return builder;
    }
}
