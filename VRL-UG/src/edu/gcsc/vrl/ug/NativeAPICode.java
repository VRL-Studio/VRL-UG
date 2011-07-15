/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;
import java.util.ArrayList;

/**
 * This class provides methods to generate the code of the native API.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NativeAPICode {

    private NativeAPIInfo api;

    /**
     * Constructor.
     * @param apiInfo api
     */
    public NativeAPICode(NativeAPIInfo apiInfo) {
        this.api = apiInfo;
    }

    /**
     * Returns API codes depending on the specified code type.
     * @param type code type that indicates whether to generate interface code,
     *        class code, etc.
     * @return API codes
     */
    private String[] getCodes(CodeType type) {

        ArrayList<String> codes = new ArrayList<String>();

        boolean interfaces = type == CodeType.INTERFACE;

        for (NativeClassInfo classInfo : api.getClasses()) {
            
            if ((classInfo.isInstantiable()
                    && type == CodeType.FULL_CLASS)) {
                codes.add(new ClassCode(
                        api, classInfo, type, false).build(
                        new CodeBuilder()).toString());
                codes.add(new ClassCode(
                        api, classInfo, type, true).build(
                        new CodeBuilder()).toString());

            } else if (interfaces) {
                codes.add(new ClassCode(
                        api, classInfo, type, false).build(
                        new CodeBuilder()).toString());
                codes.add(new ClassCode(
                        api, classInfo, type, true).build(
                        new CodeBuilder()).toString());

            } else if (!classInfo.isInstantiable()
                    && type == CodeType.WRAP_POINTER_CLASS) {
                codes.add(new ClassCode(
                        api, classInfo, type, false).build(
                        new CodeBuilder()).toString());
                codes.add(new ClassCode(
                        api, classInfo, type, true).build(
                        new CodeBuilder()).toString());
            }
        }

//        if (type != CodeType.WRAP_POINTER_CLASS) {
//            codes.add(new UGAnyCode(apiInfo, type).toString());
//        }

        return codes.toArray(new String[codes.size()]);
    }

    public String[] getClassGroupInterfaceCodes() {

        ArrayList<String> codes = new ArrayList<String>();

        for (NativeClassGroupInfo grp : api.getClassGroups()) {

            // classgroup interfaces
            NativeClassInfo groupCls =
                    NativeClassGroupInfo.classToGroupClass(
                    api, api.getClassByName(grp.getClasses()[0]));

            codes.add(new ClassCode(
                    api, groupCls, CodeType.FULL_CLASS, false).build(
                    new CodeBuilder()).toString());
            codes.add(new ClassCode(
                    api, groupCls, CodeType.FULL_CLASS, true).build(
                    new CodeBuilder()).toString());
            
            codes.add(new ClassCode(
                    api, groupCls, CodeType.INTERFACE, false).build(
                    new CodeBuilder()).toString());
            codes.add(new ClassCode(
                    api, groupCls, CodeType.INTERFACE, true).build(
                    new CodeBuilder()).toString());
        }

        return codes.toArray(new String[codes.size()]);
    }

    /**
     * Returns the API interfaces code.
     * @return the API interfaces code
     */
    private String[] getInterfaceCodes() {
        return getCodes(CodeType.INTERFACE);
    }

    /**
     * Returns the API class code.
     * @return the API class code
     */
    private String[] getClassCodes() {
        return getCodes(CodeType.FULL_CLASS);
    }

    /**
     * Returns the API wrapper class code
     * @return the API wrapper class code
     */
    private String[] getWrapperCodes() {
        return getCodes(CodeType.WRAP_POINTER_CLASS);
    }

    /**
     * Returns the API function code.
     * @return the API function code
     */
    private String[] getFunctionCodes() {
        ArrayList<String> codes = new ArrayList<String>();

        for (NativeFunctionGroupInfo group : api.getFunctions()) {

            codes.add(new FunctionCode(api, group).build(
                    new CodeBuilder()).toString());
        }
        return codes.toArray(new String[codes.size()]);
    }

    /**
     * Returns all API codes.
     * @return all API codes
     */
    public String[] getAllCodes() {

        String[] interfaceCodes = getInterfaceCodes();
        String[] wrapperCodes = getWrapperCodes();
        String[] classesCodes = getClassCodes();
        String[] functionCodes = getFunctionCodes();
        String[] classesGroupInterfaceCodes = getClassGroupInterfaceCodes();

        int finalLength = classesCodes.length
                + interfaceCodes.length
                + functionCodes.length + wrapperCodes.length
                + classesGroupInterfaceCodes.length;

        String[] result = new String[finalLength];

        System.arraycopy(classesCodes, 0, result, 0, classesCodes.length);
        System.arraycopy(interfaceCodes, 0, result,
                classesCodes.length, interfaceCodes.length);
        System.arraycopy(functionCodes, 0, result,
                classesCodes.length + interfaceCodes.length,
                functionCodes.length);
        System.arraycopy(wrapperCodes, 0, result,
                classesCodes.length
                + interfaceCodes.length + functionCodes.length,
                wrapperCodes.length);
        System.arraycopy(classesGroupInterfaceCodes, 0, result,
                classesCodes.length
                + interfaceCodes.length + functionCodes.length
                + wrapperCodes.length,
                classesGroupInterfaceCodes.length);

        return result;
    }
}
