/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.InstanceCreator;
import eu.mihosoft.vrl.lang.groovy.GroovyCompiler;
import eu.mihosoft.vrl.system.VRL;
import eu.mihosoft.vrl.visual.MessageType;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        c.setCatchCompileException(false);
        InstanceCreator creator = new InstanceCreator();
        
        Object result = null;
        try{
            result = creator.newInstance(c.compile(s, null));
        } catch (Exception ex) {
                String msg = adaptErrorMessage(ex.getMessage());
                
                throw new RuntimeException(msg);
        }
        
        return result;
    }

    /**
     * Returns the line number where the error occured.
     * @param message the error message from the compiler
     * @return the line number
     */
    public static int getErrorLine(String message) {
        Pattern linePattern =
                Pattern.compile(" @ line \\d+");

        Matcher matcher = linePattern.matcher(message);

        int line = 0;

        if (matcher.find()) {
            String lineString = matcher.group();

            Pattern numberPattern =
                    Pattern.compile("\\d+");

            matcher = numberPattern.matcher(lineString);

            if (matcher.find()) {

                line = new Integer(matcher.group());
            }
        }
        return line;
    }

    /**
     * Returns the column number where the error occured.
     * @param message the error message from the compiler
     * @return the column number
     */
    public static int getErrorColumn(String message) {
        Pattern linePattern =
                Pattern.compile(", column \\d+");

        Matcher matcher = linePattern.matcher(message);

        int line = 0;

        if (matcher.find()) {
            String lineString = matcher.group();

            Pattern numberPattern =
                    Pattern.compile("\\d+");

            matcher = numberPattern.matcher(lineString);

            if (matcher.find()) {

                line = new Integer(matcher.group());
            }
        }
        return line;
    }

    /**
     * Adapts an error message.
     * @param message the message
     * @return adapted error message
     */
    public static String adaptErrorMessage(String message) {
        
        int line = getErrorLine(message);
        int column = getErrorColumn(message);

        message = message.replaceFirst(
                "startup failed:",
                "");

        message = message.replaceAll(
                "script\\d+.groovy: \\d+:",
                "");

        message = message.replaceAll(
                "@ line \\d+, column \\d+.",
                "");

        message = message.replaceAll(
                "\\d+ errors",
                "");

        message = message.replaceAll(
                "\\d+ error",
                "");

        return "<br>@ line " + (line-5) + ", column " + column + ":<br>"
                + "<pre><code>"
                + message
                + "</code></pre>";

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
        paramString += "int " + paramNames.get(paramNames.size()-1) + " = _si;\n";

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
