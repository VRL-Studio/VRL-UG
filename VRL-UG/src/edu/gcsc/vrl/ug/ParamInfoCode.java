/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;

/**
 * Code element that gererates code for parameter infos (annotation code).
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class ParamInfoCode implements CodeElement {
    private NativeParamInfo param;

    /**
     * Constructor.
     * @param param parameter
     */
    public ParamInfoCode(NativeParamInfo param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        builder.append("@ParamInfo(name=\""
                + VLangUtils.addEscapeCharsToCode(param.getParamInfo()[0])
                + "\", style=\""
                + VLangUtils.addEscapeCharsToCode(
                param.getParamInfo()[1]) + "\", "
                + "options=\"" 
                + VLangUtils.addEscapeCharsToCode(param.getParamInfo()[2])
                + "\")");

        return builder;
    }
}
