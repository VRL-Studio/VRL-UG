/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.VLangUtils;
import eu.mihosoft.vrl.lang.groovy.GroovyCompiler;
import eu.mihosoft.vrl.types.InputCodeType;
import eu.mihosoft.vrl.types.InputTextType;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class CondUserDataType extends InputCodeType {

    private ArrayList<String> paramNames;

    public CondUserDataType() {
        setType(String.class);
        setStyleName("cond-user-data");
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
