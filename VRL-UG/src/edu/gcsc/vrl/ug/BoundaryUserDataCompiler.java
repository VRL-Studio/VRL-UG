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
public class BoundaryUserDataCompiler {

    public static final String CLASS_NAME = "BoundaryUserDataImpl";
    public static final String PACKAGE_NAME = "edu.gcsc.vrl.ug";

    public static Object compile(String s) {
        GroovyCompiler c = new GroovyCompiler();
        c.addImport("import " + BoundaryUserDataCompiler.PACKAGE_NAME + ".*;");
        InstanceCreator creator = new InstanceCreator();
        Object result = creator.newInstance(c.compile(s, null));

        return result;
    }

    public static String getUserDataImplCode(String s,
            ArrayList<String> paramNames) {
        String returnType = "Boundary";
        String paramString = "";

        for (int i = 0; i < paramNames.size(); i++) {
            paramString += "double " + paramNames.get(i) + " = p[" + i + "];";
        }

        String text = "package " + PACKAGE_NAME+";" 
                +" class " + BoundaryUserDataCompiler.CLASS_NAME
                + " extends BoundaryUserData { ";
        text += returnType + " run (double[] p) { ";
        text += paramString;
        text += s + " } }";

        System.out.println(text);
        
        return text;
    }
}
