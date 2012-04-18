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
public class CondUserDataCompiler {

    public static final String CLASS_NAME = "CondUserDataImpl";
    public static final String PACKAGE_NAME = "edu.gcsc.vrl.ug";

    public static Object compile(String s) {
        GroovyCompiler c = new GroovyCompiler();
        c.addImport("import " + CondUserDataCompiler.PACKAGE_NAME + ".*;");
        InstanceCreator creator = new InstanceCreator();
        Object result = creator.newInstance(c.compile(s, null));

        return result;
    }

    public static String getUserDataImplCode(String s,
            ArrayList<String> paramNames) {
        String returnType = "Cond";
        String paramString = "";

        for (int i = 0; i < paramNames.size()-1; i++) {
            paramString += "double " + paramNames.get(i) + " = _p[" + i + "];";
        }
        
        // additional subset index parameter (always last param by definition)
        paramString += "int " + paramNames.get(paramNames.size()-1) + " = _si;";

        String text = "package " + PACKAGE_NAME+";\n"
                + "import static java.lang.Math.*;\n"
                +"class " + CondUserDataCompiler.CLASS_NAME
                + " extends CondUserData {\n";
        text += returnType + " run (double[] _p, int _si) {\n";

        text += paramString;
        text += s + "\n}\n}\n";
        
        return text;
    }
}
