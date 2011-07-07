/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.InstanceCreator;
import eu.mihosoft.vrl.lang.groovy.GroovyCompiler;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UserDataCompiler {

    public static final String CLASS_NAME = "UserDataImpl";
    public static final String PACKAGE_NAME = "edu.gcsc.vrl.ug4";

    public static Object compile(String s, int dim) {
        GroovyCompiler c = new GroovyCompiler();
        c.addImport("import " + UserDataCompiler.PACKAGE_NAME + ".*;");
        InstanceCreator creator = new InstanceCreator();
        Object result = creator.newInstance(c.compile(s, null));

        return result;
    }

    public static String getUserDataImplCode(String s, int dim,
            ArrayList<String> paramNames, String returnType) {
        String paramString = "";

        for (int i = 0; i < paramNames.size(); i++) {
            paramString += "double " + paramNames.get(i) + " = p[" + i + "];";
        }

        String text = "package " + PACKAGE_NAME+";" 
                +"class " + UserDataCompiler.CLASS_NAME
                + " extends UserData { ";
        text += returnType + " run" + dim+ " (double[] p) { ";
        text += paramString;
        text += s + " } }";

        return text;
    }
}
