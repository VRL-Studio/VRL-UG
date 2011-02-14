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

        for (NativeClassInfo classInfo : apiInfo.getClasses()) {

            if (classInfo.isInstantiable() || interfaces) {
                codes.add(new ClassCode(
                        apiInfo, classInfo, interfaces).toString(
                        new CodeBuilder()).toString());
            }
        }

        return codes.toArray(new String[codes.size()]);
    }

    public String[] getInterfaceCodes() {
        return getCodes(true);
    }

    public String[] getClassCodes() {
        return getCodes(false);
    }

    public String[] getFunctionCodes() {
        ArrayList<String> codes = new ArrayList<String>();

        for (NativeFunctionGroupInfo group : apiInfo.getFunctions()) {

            if (group == null) {
                System.out.println("GROUP==NULL!!!");
            } else {
                System.out.println("Group.numOverloads" + group.getOverloads().length);
            }

//            for (NativeMethodInfo f : group.getOverloads()) {
//                codes.add(new FunctionCode((NativeFunctionInfo) f).toString(
//                        new CodeBuilder()).toString());
//            }

            codes.add(new FunctionCode(group).toString(
                        new CodeBuilder()).toString());
        }
        return codes.toArray(new String[codes.size()]);
    }

    public String[] getAllCodes() {
        String[] classesCodes = getClassCodes();
        String[] interfaceCodes = getInterfaceCodes();
        String[] functionCodes = getFunctionCodes();

        int finalLength = classesCodes.length
                + interfaceCodes.length + functionCodes.length;

        String[] result = new String[finalLength];

        System.arraycopy(classesCodes, 0, result, 0, classesCodes.length);
        System.arraycopy(interfaceCodes, 0, result,
                classesCodes.length, interfaceCodes.length);
        System.arraycopy(functionCodes, 0, result,
                classesCodes.length + interfaceCodes.length,
                functionCodes.length);

        return result;
    }
}
