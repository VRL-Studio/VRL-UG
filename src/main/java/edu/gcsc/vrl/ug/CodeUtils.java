/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2012–2018 Goethe Universität Frankfurt am Main, Germany
 * 
 * This file is part of Visual Reflection Library (VRL).
 *
 * VRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/VRL/src/eu/mihosoft/vrl/resources/license/lgplv3.txt
 *
 * VRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of VRL includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of VRL. Neither the VRL Canvas attribution icon nor any
 * copyright statement/attribution may be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do three things regarding copyright
 * notice and author attribution.
 *
 * First, the following text must be displayed on the Canvas:
 * "based on VRL source code". In this case the VRL canvas icon must be removed.
 * 
 * Second, the copyright notice must remain. It must be reproduced in any
 * program that uses VRL.
 *
 * Third, add an additional notice, stating that you modified VRL. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "VRL source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * M. Hoffer, C.Poliwoda, G.Wittum. Visual Reflection Library -
 * A Framework for Declarative GUI Programming on the Java Platform.
 * Computing and Visualization in Science, 2011, in press.
 */
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
