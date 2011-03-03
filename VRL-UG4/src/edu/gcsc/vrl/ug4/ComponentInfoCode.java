/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.VLangUtils;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class ComponentInfoCode {
    private NativeClassInfo classInfo;
    private String prefix;

    public ComponentInfoCode(NativeClassInfo classInfo, String prefix) {
        this.classInfo = classInfo;
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "@ComponentInfo(name=\"" + prefix
                +VLangUtils.addEscapeCharsToCode(classInfo.getName())
                +"\", category=\""
                + VLangUtils.addEscapeCharsToCode(classInfo.getCategory())
                +"\", allowRemoval=false)";
    }
}
