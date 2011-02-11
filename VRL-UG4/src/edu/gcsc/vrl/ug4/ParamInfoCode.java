/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class ParamInfoCode {
    private NativeParamInfo param;

    public ParamInfoCode(NativeParamInfo param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }

    public CodeBuilder toString(CodeBuilder builder) {

        builder.append("@ParamInfo(name=\""
                + VLangUtils.addEscapeCharsToCode(param.getParamInfo()[0])
                + "\", style=\""
                + VLangUtils.addEscapeCharsToCode(
                param.getParamInfo()[1]) + "\") ");

        return builder;
    }
}
