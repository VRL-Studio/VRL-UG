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

    public ComponentInfoCode(NativeClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public String toString() {
        return "@ComponentInfo(name=\""
                +VLangUtils.addEscapeCharsToCode(classInfo.getName())
                +"\", category=\""
                + VLangUtils.addEscapeCharsToCode(classInfo.getCategoryGroup())
                +"\")";

    }
}
