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
    public static final String PACKAGE_NAME = "edu.gcsc.vrl.ug";

    /**
     * 
     * @param s
     * @param dim the dimension of the used (User-)DataType (an Integer between 0 and 3)
     * 0 = CondNumber or Number,
     * 1 = Vector, 
     * 2 = Matrix, 
     * 3 = Tensor
     * @return 
     */
    public static Object compile(String s, int dim) {
        GroovyCompiler c = new GroovyCompiler();
        c.addImport("import " + UserDataCompiler.PACKAGE_NAME + ".*;");
        InstanceCreator creator = new InstanceCreator();
        Object result = creator.newInstance(c.compile(s, null));

        return result;
    }

    /**
     * 
     * 
     * @param s
     * @param dim the dimension of the used (User-)DataType (an Integer between 0 and 3)
     * 0 = CondNumber or Number,
     * 1 = Vector, 
     * 2 = Matrix, 
     * 3 = Tensor
     * 
     * @param paramNames
     * @param returnType
     * @return 
     */
    public static String getUserDataImplCode(String s, int dim,
            ArrayList<String> paramNames, String returnType) {
        String paramString = "";

        for (int i = 0; i < paramNames.size()-1; i++) {
            paramString += "double " + paramNames.get(i) + " = _p[" + i + "];";
        }
        
        // additional subset index parameter (always last param by definition)
        paramString += "int " + paramNames.get(paramNames.size()-1) + " = _si;";

        String text = "package " + PACKAGE_NAME+";\n"
                + "import static java.lang.Math.*;\n"
                +"class " + UserDataCompiler.CLASS_NAME
                + " extends UserData {\n";
        text += returnType + " run" + dim+ " (double[] _p, int _si) {\n";

        text += paramString;
        text += s + "\n}\n}\n";

        return text;
    }
}
