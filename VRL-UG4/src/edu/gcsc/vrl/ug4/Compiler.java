/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.VLangUtils;
import eu.mihosoft.vrl.reflection.AbstractCode;
import groovy.lang.GroovyClassLoader;
import java.io.File;
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
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Compiler {

    public Class<?>[] compile(String[] codes) {


//        String code = "class UG4_Functionality {\n"

        ArrayList<String> classNames = new ArrayList<String>();

        String code =
                "import eu.mihosoft.vrl.reflection.*;\n"
                + "import eu.mihosoft.vrl.types.*;\n";

        for (String c : codes) {
            code += c + "\n\n";// + "/*--------NEW_CLASS--------*/\n\n";
            AbstractCode aCode = new AbstractCode();
            aCode.setCode(c);
            classNames.add(VLangUtils.classNameFromCode(aCode));

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

// Configure
        CompilerConfiguration conf = new CompilerConfiguration();
        conf.setTargetDirectory(scriptPath.getPath());
//conf.setClasspath(new File[]{additonalPath});

// Compile…
        GroovyClassLoader gcl = new GroovyClassLoader();
        CompilationUnit cu = new CompilationUnit(gcl);
        cu.configure(conf);
        cu.addSource("UG_Classes", code);
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
                Class<?> clazz = gcl.loadClass(className);
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
     * Delete class files or directory on exit
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
