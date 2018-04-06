/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2012–2018 Goethe Universität Frankfurt am Main, Germany
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
package edu.gcsc.vrl.ug.types;

import edu.gcsc.vrl.ug.CondUserDataCompiler;
import eu.mihosoft.vrl.annotation.TypeInfo;
import eu.mihosoft.vrl.lang.VLangUtils;
import eu.mihosoft.vrl.lang.groovy.GroovyCompiler;
import eu.mihosoft.vrl.types.InputCodeType;
import groovy.lang.Script;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@TypeInfo(type=String.class, input=true, output=false, style="cond-user-data")
public class CondUserDataType extends InputCodeType {

    private ArrayList<String> paramNames;

    public CondUserDataType() {
//        setType(String.class);
//        setStyleName("cond-user-data");
        setValueName(" ");
    }

    @Override
    public void setViewValue(Object o) {
        super.setViewValue(o);
    }

    @Override
    public Object getViewValue() {
        String result = "";
        if (getMainCanvas().isSavingSession()) {
            result = (String) super.getViewValue();
        } else {
            String originalInput = (String) super.getViewValue();

            String text = CondUserDataCompiler.getUserDataImplCode(
                    originalInput, paramNames);

            // check if code compiles
            if (!isNoValidation()) {
                GroovyCompiler compiler = new GroovyCompiler(getMainCanvas());
                compiler.addImport(
                        "import "
                        + CondUserDataCompiler.PACKAGE_NAME + ".*;");
                compiler.compile(text, getEditor());
            }

            result = text;
        }

        return result;
    }

    @Override
    public void evaluationRequest(Script s) {
        Object property = null;

        if (getValueOptions().contains("params")) {
            property = getOptionEvaluator().getProperty("params");
        }

        if (property != null) {
            paramNames = (ArrayList<String>) property;

            String paramString = "// available input parameters: ";

            for (int i = 0; i < paramNames.size(); i++) {
                if (i > 0) {
                    paramString += ", ";
                }
                paramString += paramNames.get(i);
            }

            paramString += " \n// valid return type: Cond(boolean,number) ";

            if (getMainCanvas() != null && !getMainCanvas().isLoadingSession()) {
                setViewValue(paramString + "\n\n");
            }
        }
    }

    @Override
    public String getValueAsCode() {
        Object o = getViewValueWithoutValidation();
        if (o != null) {
            return "\"" + VLangUtils.addEscapeNewLinesToCode(
                    VLangUtils.addEscapeCharsToCode((String) o)) + "\"";
        }

        return "";
    }
}
