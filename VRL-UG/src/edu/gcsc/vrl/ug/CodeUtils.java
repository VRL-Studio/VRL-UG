/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

/**
 * This util class provides several methods for code processing and analyzing.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class CodeUtils {
    // no instanciation allowed

    /**
     * No instanciation allowed.
     */
    private CodeUtils() {
        throw new AssertionError(); // not in this class either!
    }

    /**
     * Returns a name as method name. The specified string must only contain
     * valid characters.
     * @param name name to process
     * @return a name as method name, i.e., first character is a lowercase
     * character
     */
    public static String methodName(String name) {
        String result = name;

        // since 15.07.2011 we want to allow uppercase methodnames
        // due to possible name clashes
//        if (!result.isEmpty()) {
//            result = Character.toLowerCase(name.charAt(0)) + name.substring(1);
//        }
        return result;
    }

    /**
     * Returns a name as const method name. The specified string must only 
     * contain valid characters. The prefix "const" is added to
     * the specified name.
     * @param name name to process
     * @return a name as const method name, i.e., the prefix "const" is added
     */
    public static String constMethodName(String name) {
        String result = "const__";

        // since 15.07.2011 we want to allow uppercase and lowercase methodnames
        // due to possible name clashes
//        if (!name.isEmpty()) {
//            result = "const" + Character.toUpperCase(name.charAt(0))
//                    + name.substring(1);
//        }

        if (!name.isEmpty()) {
            result = "const__" + name;
        }

        return result;
    }

    /**
     * Returns a name as class name. The specified string must only contain
     * valid characters. 
     * @param name name to process
     * @return a name as class name, i.e., first character is a uppercase
     * character
     */
    public static String className(String name, boolean isConst) {
//        String result = "C_";
        
        String result = ""; // classes do not have a prefix

        if (isConst) {
            result += "Const__";
        }

        result += name;

        // since 15.07.2011 we want to allow lowercase classnames
        // due to possible name clashes
//        if (!result.isEmpty()) {
//            result = Character.toUpperCase(name.charAt(0)) + name.substring(1);
//        }

        return result;
    }
    
    /**
     * Returns a name as class name. The specified string must only contain
     * valid characters. 
     * @param name name to process
     * @return a name as class name, i.e., first character is a uppercase
     * character
     */
    public static String classNameForParamInfo(String name, boolean isConst) {
        String result = "";

        if (isConst) {
            result += "Const__";
        }

        result += name;

        // since 15.07.2011 we want to allow lowercase classnames
        // due to possible name clashes
//        if (!result.isEmpty()) {
//            result = Character.toUpperCase(name.charAt(0)) + name.substring(1);
//        }

        return result;
    }

    /**
     * Returns the specified name as interface name. This method uses
     * {@link CodeUtils#className(java.lang.String) } and appends "Interface"
     * to the specified name.
     * @param name name to process
     * @return the specified name as interface name
     */
    public static String interfaceName(String name, boolean isConst) {
        String result = "I_";

        if (isConst) {
            result += "Const__";
        }

        result += name;

        // since 15.07.2011 we want to allow lowercase classnames
        // due to possible name clashes
//        if (!result.isEmpty()) {
//            result = Character.toUpperCase(name.charAt(0)) + name.substring(1);
//        }

        return result;
    }

    /**
     * Returns a name as function name. The specified string must only contain
     * valid characters.
     * @param name name to process
     * @return a name as class name, i.e., first character is a uppercase
     * character
     */
    public static String functionName(String name) {
        String result = "F_" + name;

        // since 15.07.2011 we want to allow lowercase classnames
        // due to possible name clashes
//        if (!result.isEmpty()) {
//            result = Character.toUpperCase(name.charAt(0)) + name.substring(1);
//        }

        return result;
    }

    /**
     * Converts an array of names to a string containing classnames seperated
     * by ", ". Each entry in the specified name array will be processed
     * with the method {@link CodeUtils#className(java.lang.String) }.
     * @param names array of names to convert
     * @return a string containing classnames seperated by ", "
     */
    public static String namesToClassNameList(String[] names, boolean isConst) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < names.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }

            builder.append(className(names[i], isConst));
        }

        return builder.toString();
    }

    /**
     * Converts an array of names to a string containing interface names 
     * seperated by ", ". Each entry in the specified name array will be 
     * processed with the method {@link CodeUtils#className(java.lang.String) }.
     * Additionally the specified prefix will be added to each name.
     * @param classNames array of names to convert
     * @param prefix prefix to add
     * @return a string containing interfacenames with specified prefix,
     *        seperated by ", "
     */
    public static String namesToInterfaceNameList(
            String[] classNames, boolean isConst) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < classNames.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }

            builder.append(interfaceName(classNames[i], isConst));
//            builder.append(prefix).append(interfaceName(classNames[i]));
        }

        return builder.toString();
    }
}
