/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.groovy.GroovyCompiler;
import eu.mihosoft.vrl.types.InputTextType;
import groovy.lang.Script;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UserDataType extends InputTextType {

    private ArrayList<String> paramNames;
    private String returnType;

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
        String s = (String) super.getViewValue();

        String paramString = "";

        for (int i = 0; i < paramNames.size(); i++) {
            if (i > 0) {
                paramString += ", ";
            }
            paramString += "double " + paramNames.get(i) + " = p[" + i +"];";
        }

        String text = "class UserData { ";
        text += returnType + " run (double[] p) { ";
        text += paramString;
        text += s + " } }";

        // check if code compiles
        if (!isNoValidation()) {
            GroovyCompiler compiler = new GroovyCompiler(getMainCanvas());
            compiler.compileClass("edu.gcsc.ug4", text, getEditor());
        }

        return text;
    }

    @Override
    public void evaluationRequest(Script s) {
        Object property = null;


        if (getValueOptions().contains("returnType")) {
            property = getOptionEvaluator().getProperty("returnType");
        }

        if (property != null) {
            returnType = (String) property;
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

            if (returnType == null) {
                returnType = "void";
            }

            paramString += " \n// valid return type: " + returnType + " ";

            setViewValue(paramString + "\n\n");
        }
    }
}
