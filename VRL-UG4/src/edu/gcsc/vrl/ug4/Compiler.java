/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.VLangUtils;
import eu.mihosoft.vrl.io.vrlx.AbstractCode;
import groovy.lang.GroovyClassLoader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;

/**
 * A fast Groovy code compiler wrapper.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Compiler {

    /**
     * Compiles classes defined as groovy source code and returns them
     * as class objects.
     * @param codes codes to compile
     * @return class objects of the compiled codes
     */
    public Class<?>[] compile(String[] codes) {

//        return new Class<?>[]{Class.class};

        String packageName = "edu.gcsc.vrl.ug4";


//        String code = "class UG4_Functionality {\n"

        ArrayList<String> classNames = new ArrayList<String>();

        StringBuilder code = new StringBuilder();

                code.append("package ").append(packageName).append("\n").
                append("import eu.mihosoft.vrl.reflection.*;\n").
                append("import eu.mihosoft.vrl.types.*;\n").
                append("import eu.mihosoft.vrl.annotation.*;\n");

        for (String c : codes) {
            code.append(c).append("\n\n");// + "/*--------NEW_CLASS--------*/\n\n";
            AbstractCode aCode = new AbstractCode();
            aCode.setCode(c);

            String className = VLangUtils.classNameFromCode(aCode);

            if (className==null || className.equals("")) {
                className = VLangUtils.interfaceNameFromCode(aCode);
            }

            classNames.add(className);

//            System.out.println("ClassName[before]: "
//                    + classNames.get(classNames.size() - 1));
        }

        Collections.sort(classNames);

//        String scriptLocation = f.getPath();
        File scriptPath = null;
        try {
            scriptPath = createTempDir();
            System.out.println("TempClassDir: " + scriptPath.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(
                    Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (scriptPath.isFile()) {
            scriptPath = scriptPath.getParentFile();
        }

        try {
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(
//                    new File(scriptPath.getPath() + "/UG_Classes.groovy")));
                    new File("/Users/miho/UG_Classes.groovy")));

            writer.append(code);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }


// Configure
        CompilerConfiguration conf = new CompilerConfiguration();
        conf.setTargetDirectory(scriptPath.getPath());
//conf.setClasspath(new File[]{additonalPath});

// Compile…
        GroovyClassLoader gcl = new GroovyClassLoader();
        CompilationUnit cu = new CompilationUnit(gcl);
        cu.configure(conf);
        cu.addSource("UG_Classes", code.toString());
// Add more source files to the compilation unit if needed
        cu.compile();

// Load…
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL[] urls = null;
        try {
            urls = new URL[]{scriptPath.toURL()};
        } catch (MalformedURLException ex) {
            Logger.getLogger(
                    Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        URLClassLoader ucl = new URLClassLoader(urls, cl);
        gcl = new GroovyClassLoader(ucl, conf);

        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

        for (String className : classNames) {
            try {
                Class<?> clazz = gcl.loadClass(packageName+"."+className);
                if (clazz != null) {
                    classes.add(clazz);
//                    System.out.println("ClassName[after, ok]: " + className);
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("ClassName[after, failed]: " + className);
                Logger.getLogger(
                        Compiler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Class<?>[] result = new Class<?>[classes.size()];

        classes.toArray(result);

        deleteClassFiles(scriptPath);

        UG4.setNativeClasses(result);

        return result;
    }

    /**
     * Create a new temporary directory. Use something like
     * {@link #recursiveDelete(File)} to clean this directory up since it isn't
     * deleted automatically
     * http://stackoverflow.com/questions/617414/create-a-temporary-directory-in-java
     * @return  the new directory
     * @throws IOException if there is an error creating the temporary directory
     */
    private static File createTempDir() throws IOException {
        final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
        File newTempDir;
        final int maxAttempts = 9;
        int attemptCount = 0;
        do {
            attemptCount++;
            if (attemptCount > maxAttempts) {
                throw new IOException(
                        "The highly improbable has occurred! Failed to "
                        + "create a unique temporary directory after "
                        + maxAttempts + " attempts.");
            }
            String dirName = UUID.randomUUID().toString();
            newTempDir = new File(sysTempDir, dirName);
            newTempDir.deleteOnExit();
        } while (newTempDir.exists());

        if (newTempDir.mkdirs()) {
            return newTempDir;
        } else {
            throw new IOException(
                    "Failed to create temp dir named "
                    + newTempDir.getAbsolutePath());
        }
    }

    /**
     * Request deletion of class files or directory on exit
     * @param fileOrDir
     *          the dir to delete
     * @return
     *          true iff all files are successfully deleted
     */
    private void deleteClassFiles(File fileOrDir) {
        if (fileOrDir.isDirectory()) {
            // recursively delete contents
            for (File innerFile : fileOrDir.listFiles()) {
                innerFile.deleteOnExit();
            }
        }

        fileOrDir.deleteOnExit();
    }
}
