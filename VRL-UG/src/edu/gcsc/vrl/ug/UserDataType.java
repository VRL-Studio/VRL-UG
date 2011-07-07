/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.VLangUtils;
import eu.mihosoft.vrl.lang.groovy.GroovyCompiler;
import eu.mihosoft.vrl.types.InputTextType;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UserDataType extends InputTextType {

    private ArrayList<String> paramNames;
    private int dim = 1;

    public UserDataType() {
        setType(String.class);
        setStyleName("user-data");
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

            String text = UserDataCompiler.getUserDataImplCode(originalInput, dim,
                    paramNames, UserData.returnTypes[dim]);

            // check if code compiles
            if (!isNoValidation()) {
                GroovyCompiler compiler = new GroovyCompiler(getMainCanvas());
                compiler.addImport("import " + UserDataCompiler.PACKAGE_NAME + ".*;");
                compiler.compile(text, getEditor());
            }

            result = text;
        }

        return result;
    }

    @Override
    public void evaluationRequest(Script s) {
        Object property = null;

        property = null;

        if (getValueOptions().contains("dim")) {
            property = getOptionEvaluator().getProperty("dim");
        }

        if (property != null) {
            dim = (Integer) property;
        }

        property = null;

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

            paramString += " \n// valid return type: " + UserData.returnTypes[dim] + " ";

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
