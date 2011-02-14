/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import groovy.xml.streamingmarkupsupport.Builder;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class CodeUtils {
    // no instanciation allowed

    private CodeUtils() {
        throw new AssertionError(); // not in this class either!
    }

    public static String methodName(String name) {
        String result = name;

        if (!result.isEmpty()) {
            result = Character.toLowerCase(name.charAt(0)) + name.substring(1);
        }
        return result;
    }

    public static String constMethodName(String name) {
        String result = "const";

        if (!name.isEmpty()) {
            result = "const" + Character.toUpperCase(name.charAt(0))
                    + name.substring(1);
        }
        return result;
    }

    public static String className(String name) {
        String result = name;

        if (!result.isEmpty()) {
            result = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }

        return result;
    }

    public static String interfaceName(String name) {
        return className(name + "Interface");
    }

    public static String namesToClassNameList(String[] names) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < names.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }

            builder.append(className(names[i]));
        }

        return builder.toString();
    }

    public static String namesToInterfaceNameList(String[] classNames) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < classNames.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }

            builder.append(interfaceName(classNames[i]));
        }

        return builder.toString();
    }
}
