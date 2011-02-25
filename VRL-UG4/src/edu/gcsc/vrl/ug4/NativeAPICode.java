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

    private String[] getCodes(CodeType type) {

        ArrayList<String> codes = new ArrayList<String>();

        boolean interfaces = type == CodeType.INTERFACE;

        for (NativeClassInfo classInfo : apiInfo.getClasses()) {

            if ((classInfo.isInstantiable() && type == CodeType.FULL_CLASS)
                    || interfaces) {
                codes.add(new ClassCode(
                        apiInfo, classInfo, type).toString(
                        new CodeBuilder()).toString());

            } else if (!classInfo.isInstantiable()
                    && type == CodeType.WRAP_POINTER_CLASS) {
                
                codes.add(new ClassCode(
                        apiInfo, classInfo, type).toString(
                        new CodeBuilder()).toString());
            }
        }

//        if (type != CodeType.WRAP_POINTER_CLASS) {
//            codes.add(new UGAnyCode(apiInfo, type).toString());
//        }

        return codes.toArray(new String[codes.size()]);
    }

    public String[] getInterfaceCodes() {
        return getCodes(CodeType.INTERFACE);
    }

    public String[] getClassCodes() {
        return getCodes(CodeType.FULL_CLASS);
    }

    public String[] getWrapperCodes() {
        return getCodes(CodeType.WRAP_POINTER_CLASS);
    }

    public String[] getFunctionCodes() {
        ArrayList<String> codes = new ArrayList<String>();

        for (NativeFunctionGroupInfo group : apiInfo.getFunctions()) {

//            if (group == null) {
//                System.out.println("GROUP==NULL!!!");
//            } else {
//                System.out.println("Group.numOverloads" + group.getOverloads().length);
//            }

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

        String[] interfaceCodes = getInterfaceCodes();
        String[] wrapperCodes = getWrapperCodes();
        String[] classesCodes = getClassCodes();
        String[] functionCodes = getFunctionCodes();

        int finalLength = classesCodes.length
                + interfaceCodes.length + functionCodes.length + wrapperCodes.length;

        String[] result = new String[finalLength];

        System.arraycopy(classesCodes, 0, result, 0, classesCodes.length);
        System.arraycopy(interfaceCodes, 0, result,
                classesCodes.length, interfaceCodes.length);
        System.arraycopy(functionCodes, 0, result,
                classesCodes.length + interfaceCodes.length,
                functionCodes.length);
        System.arraycopy(wrapperCodes, 0, result,
                classesCodes.length + interfaceCodes.length + functionCodes.length,
                wrapperCodes.length);

        return result;
    }
}
