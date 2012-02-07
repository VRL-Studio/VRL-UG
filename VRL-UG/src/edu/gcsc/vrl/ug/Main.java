/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.system.VRL;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
public class Main {

//    private static Integer port = 1099; // default port

    public static void main(String[] args) {

//        System.out.println("");
//        System.out.println("");

        initUGServer();

//        System.out.println("");
//        System.out.println("");
//
//
//        String separator2 = System.getProperty("file.separator");
//        String classpath2 = System.getProperty("java.class.path");
//        String path2 = System.getProperty("java.home")
//                + separator2 + "bin" + separator2 + "java";
//
//        System.out.println("separator2 = " + separator2);
//        System.out.println("classpath2 = " + classpath2);
//        System.out.println("path2 = " + path2);
//        System.out.println("");
//
//        ProcessBuilder processBuilder = new ProcessBuilder();
//        Map env = processBuilder.environment();
//
//        System.out.println("env.toString() = " + env.toString());
//        System.out.println("");




//        try {
//            TimeUnit.SECONDS.sleep(10);
//            
//            System.out.println("isServerRunnuning ="+ConnectToUGserver.isServerRunnuning());
//            
//            for ( VirtualMachineDescriptor vmd : ConnectToUGserver.containingJVMs("edu.gcsc.vrl.ug")) {
//                System.out.println("JVM-name="+vmd.displayName());
//            }
//            
//            
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }


    protected static void startAnJVM(int port) {

        String commandLineCallOptions = "-Xms256m -Xmx1024m "
                + "-XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled "
                + "-XX:+CMSPermGenSweepingEnabled -XX:MaxPermSize=256m";

        String addionalCommandLineCallPath =
                "-Dsun.boot.library.path=/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Libraries"
                + ":natives/osx:custom-lib/osx"
                + " -Xbootclasspath/p"
                + ":natives/jars/j3dcore.jar"
                + ":natives/jars/j3dutils.jar"
                + ":natives/osx/jogl.jar"
                + ":natives/jars/vecmath.jar"
                + ":natives/osx/gluegen-rt.jar"
                + " -Djava.library.path=\"natives/osx\"";

        String separator = System.getProperty("file.separator");
        String classpath = System.getProperty("java.class.path");
        String path = System.getProperty("java.home")
                + separator + "bin" + separator + "java";


        System.out.println("separator = " + separator);
        System.out.println("classpath = " + classpath);
        System.out.println("path = " + path);


        ProcessBuilder processBuilder = new ProcessBuilder(path,
                addionalCommandLineCallPath, commandLineCallOptions, //von mir, abweichung von bsp
                "-cp", classpath, Main.class.getName());


        processBuilder.redirectErrorStream(true);//NEEDED TO READ / VIEW OUTPUT OF 2nd JVM

        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException ex) {
            Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
        }


        //NEEDED TO READ / VIEW OUTPUT OF 2nd JVM
        try {

            // hook up child process output to parent
            InputStream inStream = process.getInputStream();
            InputStreamReader inStreamRead = new InputStreamReader(inStream);
            BufferedReader buffInStreamRead = new BufferedReader(inStreamRead);

            // hook up child process error output to parent
            InputStream errStream = process.getErrorStream();
            InputStreamReader errStreamRead = new InputStreamReader(errStream);
            BufferedReader buffErrStreamRead = new BufferedReader(errStreamRead);


            // read the child process' output
            String line;
            while ((line = buffInStreamRead.readLine()) != null) {
                System.out.println(line);
            }

            String line2;
            // read the child process' error output
            while ((line2 = buffErrStreamRead.readLine()) != null) {
                System.err.println(line2);
            }

        } catch (Exception e) { // exception thrown

            System.out.println("ERROR during reading stream of Server JVM !");

        }

        try {

            process.waitFor();

        } catch (InterruptedException ex) {
            Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static void initUGServer() {

        String[] params = {"-property-folder-suffix", "numerics-server",
            "-plugin-checksum-test", "yes", "-rpc", "server"};

        VRL.initAll(params);

//        if (args != null) {
//
//            if (args[0] != null) {
//                Integer tmp = Integer.parseInt(args[0]);
//
//                // check port range between min and max
//                if (0 <= tmp && tmp <= 65535) {
//                    port = tmp;
//                }
//            }
//        }
//        
//        UG.createXmlRpcServer(port);



//                if (tmp == 1) {

//                    String[] params = {"-property-folder-suffix", "numerics-studio"};
//                    VRL.initAll(params);
//                    UG.setRemoteType(RemoteType.SERVER);

//                } 
//                else if (tmp == 2) {
//                    
//                    VRL.initAll(args);
//                    UG.setRemoteType(RemoteType.CLIENT);
//                    
//                } else {
//                    
//                    VRL.initAll(args);
//                    UG.setRemoteType(RemoteType.NONE);
//                }

    }
}
