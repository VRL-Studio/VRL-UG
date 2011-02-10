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
public class NativeAPICode {

    private NativeAPIInfo apiInfo;

    public NativeAPICode(NativeAPIInfo apiInfo) {
        this.apiInfo = apiInfo;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }

    public CodeBuilder toString(CodeBuilder builder) {

        for (NativeClassInfo classInfo : apiInfo.getClasses()) {
            if (classInfo.isInstantiable()) {
                new ClassCode(classInfo, false).toString(builder);
            }
        }

        for (NativeClassInfo classInfo : apiInfo.getClasses()) {
            new ClassCode(classInfo, true).toString(builder);
        }

//        for(NativeClassInfo classInfo : apiInfo.getClasses()) {
//            new ClassCode(classInfo, false).toString(builder);
//        }

        return builder;
    }
}
