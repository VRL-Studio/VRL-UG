/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.CodeBuilder;
import java.util.ArrayList;

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
                new ClassCode(apiInfo, classInfo, false).toString(builder);
            }
        }

        for (NativeClassInfo classInfo : apiInfo.getClasses()) {
            new ClassCode(apiInfo, classInfo, true).toString(builder);
        }

        return builder;
    }

    private String[] getCodes(boolean interfaces) {

        ArrayList<String> codes = new ArrayList<String>();

        for (int i = 0; i < apiInfo.getClasses().length; i++) {
            NativeClassInfo classInfo = apiInfo.getClasses()[i];

            if (classInfo.isInstantiable() || interfaces) {
                codes.add(new ClassCode(
                        apiInfo, classInfo, interfaces).toString(
                        new CodeBuilder()).toString());
            }
        }

        return codes.toArray(new String[codes.size()]);
    }

    public String[] getInterfacesCodes() {
        return getCodes(true);
    }

    public String[] getClassesCodes() {
        return getCodes(false);
    }

    public String[] getAllCodes() {
        String[] classesCodes = getClassesCodes();
        String[] interfaceCodes = getInterfacesCodes();

        int finalLength = classesCodes.length+interfaceCodes.length;

        String[] result = new String[finalLength];

        System.arraycopy(classesCodes, 0, result, 0, classesCodes.length);
        System.arraycopy(interfaceCodes, 0, result, classesCodes.length, interfaceCodes.length);

        return result;
    }

}
