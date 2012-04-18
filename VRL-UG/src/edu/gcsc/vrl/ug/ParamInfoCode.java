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

        String valueName = param.getParamInfo()[0];

        if (valueName.isEmpty() && param.isRegisteredClass()) {
//            valueName = param.getTypeClassName();
            valueName = CodeUtils.classNameForParamInfo(param.getClassName(),
                    param.isConst());
        }
        
        String typePrefix = "";
        
        if (param.isConst()) {
            typePrefix = "const ";
        }

        builder.append("@ParamInfo(name=\""
                + VLangUtils.addEscapeCharsToCode(valueName)
                + "\", style=\""
                + VLangUtils.addEscapeCharsToCode(
                param.getParamInfo()[1]) + "\", "
                + " typeName = \"" + typePrefix + param.getClassName() + "\", "
                + "options=\""
                + VLangUtils.addEscapeCharsToCode(param.getParamInfo()[2]));

        if (param.isRegisteredClass()) {
            builder.append(";serialization=false");
        }

        builder.append("\")");

        return builder;
    }
}
