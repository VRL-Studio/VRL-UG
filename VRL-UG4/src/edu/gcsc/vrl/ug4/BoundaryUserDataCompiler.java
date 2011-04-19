/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.groovy.GroovyCompiler;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class BoundaryUserDataCompiler {

    public static final String CLASS_NAME = "BoundaryUserDataImpl";
    public static final String PACKAGE_NAME = "edu.gcsc.vrl.ug4";

    public static Object compile(String s) {
        GroovyCompiler c = new GroovyCompiler();
        c.addImport("import " + BoundaryUserDataCompiler.PACKAGE_NAME + ".*;");
        Object result = c.newInstanceWithoutErrorNotification(
                c.compileClass("edu.gcsc.vrl.ug4", s, null));

        return result;
    }

    public static String getUserDataImplCode(String s,
            ArrayList<String> paramNames) {
        String returnType = "Boundary";
        String paramString = "";

        for (int i = 0; i < paramNames.size(); i++) {
            paramString += "double " + paramNames.get(i) + " = p[" + i + "];";
        }

        String text = "class " + BoundaryUserDataCompiler.CLASS_NAME
                + " extends BoundaryUserData { ";
        text += returnType + " run (double[] p) { ";
        text += paramString;
        text += s + " } }";

        return text;
    }
}
