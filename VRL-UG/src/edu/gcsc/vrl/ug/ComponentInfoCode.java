/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;

/**
 * Code element that generates component info code.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class ComponentInfoCode implements CodeElement {

    private NativeClassInfo classInfo;
//    private String prefix;
    private boolean isConst;

    /**
     * Constructor.
     * @param classInfo class info
     * @param prefix class name prefix
     */
    public ComponentInfoCode(NativeClassInfo classInfo, boolean isConst) {
        this.classInfo = classInfo;
//        this.prefix = prefix;
        this.isConst = isConst;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).getCode();
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {
        builder.append("@ComponentInfo(name=\"").
                append(VLangUtils.addEscapeCharsToCode(
                CodeUtils.classNameForParamInfo(classInfo.getName(), isConst))).
                append("\", category=\"").
                append(VLangUtils.addEscapeCharsToCode(
                classInfo.getCategory())).
                append("\", allowRemoval=false)");

        return builder;
    }
}
