///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package edu.gcsc.vrl.ug4;
//
//import groovy.lang.GroovyClassLoader;
//import java.io.File;
//import org.codehaus.groovy.control.CompilationUnit;
//import org.codehaus.groovy.control.CompilerConfiguration;
//
///**
// *
// * @author Michael Hoffer <info@michaelhoffer.de>
// */
//public class Compiler {
//    public Class<?> compile(File f, String[] codes) {
//
//
//        String code = "class UG4_Functionality {\n"
//                +
//
//
//        String scriptLocation = f.getPath()
//File scriptPath = f
//
//// Configure
//CompilerConfiguration conf = new CompilerConfiguration();
//conf.setTargetDirectory(scriptPath.getPath());
////conf.setClasspath(new File[]{additonalPath});
//
//// Compile…
//GroovyClassLoader gcl = new GroovyClassLoader();
//CompilationUnit cu = new CompilationUnit(gcl);
//cu.configure(conf);
//cu.addSource(className, code);
//// Add more source files to the compilation unit if needed
//cu.compile();
//
//// Load…
//ClassLoader cl = Thread.currentThread().getContextClassLoader();
//def urls = [scriptPath.toURL()];
//URLClassLoader ucl = new URLClassLoader(urls as URL[], cl);
//gcl = new GroovyClassLoader(ucl, conf);
//Class clazz = gcl.loadClass(className);
//    return clazz
//    }
//    }
//
//}
