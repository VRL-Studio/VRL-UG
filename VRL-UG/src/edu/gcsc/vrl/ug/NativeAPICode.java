/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2012–2016 Goethe Universität Frankfurt am Main, Germany
 * 
 * This file is part of Visual Reflection Library (VRL).
 *
 * VRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/VRL/src/eu/mihosoft/vrl/resources/license/lgplv3.txt
 *
 * VRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of VRL includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of VRL. Neither the VRL Canvas attribution icon nor any
 * copyright statement/attribution may be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do three things regarding copyright
 * notice and author attribution.
 *
 * First, the following text must be displayed on the Canvas:
 * "based on VRL source code". In this case the VRL canvas icon must be removed.
 * 
 * Second, the copyright notice must remain. It must be reproduced in any
 * program that uses VRL.
 *
 * Third, add an additional notice, stating that you modified VRL. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "VRL source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * M. Hoffer, C.Poliwoda, G.Wittum. Visual Reflection Library -
 * A Framework for Declarative GUI Programming on the Java Platform.
 * Computing and Visualization in Science, 2011, in press.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import edu.gcsc.vrl.ug.types.CodeType;
import eu.mihosoft.vrl.io.IOUtil;
import eu.mihosoft.vrl.lang.CodeBuilder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public String getPluginConfiguratorCode() {
        return new PluginConfiguratorCode().build(new CodeBuilder()).toString();
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
                + classesGroupInterfaceCodes.length
                // this +1 comes from the PluginApiConfigurator
                +1;

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
        
        result[finalLength-1] = getPluginConfiguratorCode();

        return result;
    }
}
