/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.IOUtil;
import eu.mihosoft.vrl.lang.VLangUtils;
import eu.mihosoft.vrl.io.vrlx.AbstractCode;
import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.system.Constants;
import groovy.lang.GroovyClassLoader;
import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;

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
    public Class<?>[] compile(String[] codes)  
            throws MultipleCompilationErrorsException {
        return compile(codes, null);
    }

    /**
     * Compiles classes defined as groovy source code and returns them
     * as class objects.
     * @param codes codes to compile
     * @return class objects of the compiled codes
     */
    public Class<?>[] compile(String[] codes, String jarLocation) 
            throws MultipleCompilationErrorsException{

        String packageName = "edu.gcsc.vrl.ug";

        ArrayList<String> classNames = new ArrayList<String>();

        StringBuilder code = new StringBuilder();

        code.append("package ").append(packageName).append("\n").
                append("import eu.mihosoft.vrl.reflection.*;\n").
                append("import eu.mihosoft.vrl.types.*;\n").
                append("import eu.mihosoft.vrl.annotation.*;\n");

        code.append("\n").append(new UGAPIClassCode().build(
                new CodeBuilder()).toString());

        for (String c : codes) {
            code.append(c).append("\n\n");
            AbstractCode aCode = new AbstractCode();
            aCode.setCode(c);

            String className = VLangUtils.classNameFromCode(aCode);

            if (className == null || className.equals("")) {
                className = VLangUtils.interfaceNameFromCode(aCode);
            }

            classNames.add(className);
        }

        Collections.sort(classNames);

        File scriptPath = null;
        try {
            scriptPath = createTempDir();
            scriptPath.mkdir();

            System.out.println("UG4-ClassDir: " + scriptPath.getAbsolutePath());
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
                    new File("/home/miho/UG_Classes.groovy")));

            writer.append(code);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }


        // Configure
        CompilerConfiguration conf = new CompilerConfiguration();
        conf.setTargetDirectory(scriptPath.getPath());

        // Compile…
        GroovyClassLoader gcl = new GroovyClassLoader();
        CompilationUnit cu = new CompilationUnit(gcl);
        cu.configure(conf);
        cu.addSource("UG_Classes", code.toString());
        cu.compile();

        // Load classes via URL classloader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL[] urls = null;
        try {
            urls = new URL[]{scriptPath.toURI().toURL()};
        } catch (MalformedURLException ex) {
            Logger.getLogger(
                    Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        URLClassLoader ucl = new URLClassLoader(urls, cl);
        gcl = new GroovyClassLoader(ucl, conf);

        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

        for (String className : classNames) {
            try {
                Class<?> clazz = gcl.loadClass(packageName + "." + className);
                if (clazz != null) {
                    classes.add(clazz);
//                    System.out.println("ClassName[after, ok]: " + className);
                }
            } catch (ClassNotFoundException ex) {
//                System.out.println("ClassName[after, failed]: " + className);
                Logger.getLogger(
                        Compiler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Class<?>[] result = new Class<?>[classes.size()];
        classes.toArray(result);

        try {

            // Construct a string version of a manifest
            StringBuilder sbuf = new StringBuilder();
            sbuf.append("Manifest-Version: 1.0\n");
            sbuf.append("Created-By: VRL-" + Constants.VERSION + "\n");
            // sbuf.append("Java-Bean: False\n");

            // Convert the string to a input stream
            InputStream is = new ByteArrayInputStream(sbuf.toString().
                    getBytes("UTF-8"));

            File meta_inf = new File(scriptPath.getAbsolutePath() + "/META-INF");
            meta_inf.mkdir();

            Manifest manifest = new Manifest(is);

            manifest.write(new FileOutputStream(
                    new File(meta_inf.getAbsolutePath() + "/MANIFEST.MF")));


            // write ug classes
            
            File ugInfoPath = new File(scriptPath.getAbsolutePath()
                    +"/edu/gcsc/vrl/ug/");
            
            ugInfoPath.mkdirs();

            XMLEncoder encoder = new XMLEncoder(
                    new FileOutputStream(
                    ugInfoPath.getAbsolutePath() + "/UG_INFO.XML"));

            encoder.writeObject(new AbstractUGAPIInfo(classNames));

            encoder.close();

        } catch (IOException ex) {
            Logger.getLogger(
                    Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            IOUtil.zipContentOfFolder(scriptPath.getAbsolutePath(),
                    "/home/miho/tmp/VRL-UG-API.jar");
        } catch (IOException ex) {
            Logger.getLogger(Compiler.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        if (jarLocation != null) {
            try {
                IOUtil.zipContentOfFolder(scriptPath.getAbsolutePath(),
                        jarLocation + "/VRL-UG-API.jar");
            } catch (IOException ex) {
                Logger.getLogger(Compiler.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }

        deleteClassFiles(scriptPath);

//        UG.setNativeClasses(result);

        return result;
    }

    /**
     * Create a new temporary directory. Use something like
     * {@link #deleteClassFiles(File)} to clean this directory up since it isn't
     * deleted automatically.
     * @see http://stackoverflow.com/questions/617414/create-a-temporary-directory-in-java
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
     * @param fileOrDir the dir to delete
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
