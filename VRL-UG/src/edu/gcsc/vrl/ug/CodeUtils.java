/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

/**
 * This util class provides several methods for code processing and analyzing.
 *
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
     * characters that are allowed for identifier names.
     *
     * @param name name to process
     * @return a name as method name, i.e., first character is a lowercase
     * character
     */
    public static String methodName(String name) {

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Empty methodnames not supported!");
        }

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
     * contain characters that are allowed for identifier names. The prefix
     * "const__" is added to the specified name.
     *
     * @param name name to process
     * @return a name as const method name, i.e., the prefix "const" is added
     */
    public static String constMethodName(String name) {

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Empty methodnames not supported!");
        }

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
     * characters that are allowed for identifier names.
     *
     * @param name name to process
     * @return a name as class name, i.e., first character is a uppercase
     * character
     */
    public static String className(String name, boolean isConst) {

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Empty classnames not supported!");
        }

//        String result = "C_";
        String result = ""; // classes do not have a prefix

        // since 15.07.2011 we want to allow lowercase classnames
        // due to possible name clashes
        // since 17.01.2012 we allow only upper case classes because we need
        // to support operating systems without case sensitive filesystems
//        if (!result.isEmpty()) {
        result = Character.toUpperCase(name.charAt(0)) + name.substring(1);
//        }

        if (isConst) {
            result = "Const__" + result;
        }

        return result;
    }

    /**
     * Returns a name as class name. The specified string must only contain
     * characters that are allowed for identifier names.
     *
     * @param name name to process
     * @return a name as class name, i.e., first character is a uppercase
     * character
     */
    public static String classNameForParamInfo(String name, boolean isConst) {

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Empty classnames not supported!");
        }

        String result = "";

        // since 15.07.2011 we want to allow lowercase classnames
        // due to possible name clashes
        // since 17.01.2012 we allow only upper case classes because we need
        // to support operating systems without case sensitive filesystems
//        if (!result.isEmpty()) {
        result = Character.toUpperCase(name.charAt(0)) + name.substring(1);
//        }

        if (isConst) {
            result = "Const__" + result;
        }

        return result;
    }

    /**
     * Returns the specified name as interface name. The specified string must
     * only contain characters that are allowed for identifier names. to the
     * specified name.
     *
     * @param name name to process
     * @return the specified name as interface name
     */
    public static String interfaceName(String name, boolean isConst) {

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Empty interfacenames not supported!");
        }

        String result = "";

        // since 15.07.2011 we want to allow lowercase classnames
        // due to possible name clashes
        // since 17.01.2012 we allow only upper case classes because we need
        // to support operating systems without case sensitive filesystems
//        if (!result.isEmpty()) {
        result = Character.toUpperCase(name.charAt(0)) + name.substring(1);
//        }

        result = "I_" + result;

        if (isConst) {
            result = "Const__" + result;
        }

        return result;
    }

    /**
     * Returns a name as function name. The specified string must only contain
     * characters that are allowed for identifier names.
     *
     * @param name name to process
     * @return a name as class name, i.e., first character is a uppercase
     * character
     */
    public static String functionName(String name) {

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Empty functionname not supported!");
        }

        String result = "";

        // since 15.07.2011 we want to allow lowercase classnames
        // due to possible name clashes
        // since 17.01.2012 we allow only upper case functions because we need
        // to support operating systems without case sensitive filesystems
//        if (!result.isEmpty()) {
        result = Character.toUpperCase(name.charAt(0)) + name.substring(1);
//        }

        result = "F_" + result;

        return result;
    }

    /**
     * Converts an array of names to a string containing classnames seperated by
     * ", ". Each entry in the specified name array will be processed with the
     * method {@link CodeUtils#className(java.lang.String) }.
     *
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
     *
     * @param classNames array of names to convert
     * @param prefix prefix to add
     * @return a string containing interfacenames with specified prefix,
     * seperated by ", "
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
