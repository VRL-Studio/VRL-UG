package edu.gcsc.vrl.ug;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ObjectInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
@ObjectInfo(name = "JVMmanager")
@ComponentInfo(name = "JVMmanager", category = "VRL/VRL-UG")
public class JVMmanager implements Serializable {

    private static final long serialVersionUID = 1L;
    private static boolean stopJvmOutputRedirection = false;
    private static Integer defaultPort = 1099;
    private static String defaultIP = "127.0.0.1"; //"localhost"
    private static Integer currentPort = 1099;
    private static String currentIP = "127.0.0.1"; //"localhost"
    // String should have the form "ip:port"
    private static HashMap<String, XmlRpcClient> clientsForConnection = new HashMap<String, XmlRpcClient>();

//    private static HashMap<String,Process> createdProcesses = new HashMap<String, Process>();
    /**
     * Starts another JVM and executes there the main methode of the class which
     * the parameter of this methode.
     *
     * @param clazz The class which should be started in a new JVM.
     *
     * @throws Exception
     */
    public static void startAnotherJVM(
//            @ParamInfo(name = "Class", options = "value=edu.gcsc.vrl.ug.UG.class")//as default UG.class, is possible???
            final Class clazz,
            final String ip,
            final Integer port) {

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                String separator = System.getProperty("file.separator");
                String classpath = System.getProperty("java.class.path");


                // TODO make classpath update automatic
                //manuel update to find VRL-UG classes 
                String projectPath = "/Users/christianpoliwoda/Apps/VRL-UG4/VRL-UG/dist/VRL-UG.jar";
                classpath += ":" + projectPath;

                String path = System.getProperty("java.home")
                        + separator + "bin" + separator + "java";

                String name = clazz.getName();
                String portString = port.toString();
                // to remove outOfMemoryError message: PermGen space
                String commandLineCallOptions = "-XX:MaxPermSize=512m";



                ProcessBuilder processBuilder = new ProcessBuilder(
                        path, commandLineCallOptions,
                        "-cp", classpath,
                        name, portString, ip);


                //NEEDED TO READ / VIEW OUTPUT OF 2nd JVM
                processBuilder.redirectErrorStream(true);

                //set selfdefined variable in new JVM
                processBuilder.environment().put("APP_NAME", name);

                Process process = null;

                try {
                    process = processBuilder.start();


                    displayJVMOutput(process);


                } catch (IOException ex) {
                    Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        t.setDaemon(true);
        t.start();
    }

    //NO EFFECT ??
    /**
     * display the output of process p at System.out.
     *
     * @param p the process which output shoult be redirected
     */
    public static void displayJVMOutput(final Process p) {

        stopJvmOutputRedirection = false;

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                //NEEDED TO READ / VIEW OUTPUT OF 2nd JVM
                try {

                    // hook up child process output to parent
                    InputStream inStream = p.getInputStream();
                    InputStreamReader inStreamRead = new InputStreamReader(inStream);
                    BufferedReader buffInStreamRead = new BufferedReader(inStreamRead);

                    // hook up child process error output to parent
                    InputStream errStream = p.getErrorStream();
                    InputStreamReader errStreamRead = new InputStreamReader(errStream);
                    BufferedReader buffErrStreamRead = new BufferedReader(errStreamRead);

                    while (!stopJvmOutputRedirection) {
                        String lineOut = buffInStreamRead.readLine();

                        if (lineOut != null) {
                            System.out.println(lineOut);
                        }

                        String lineErr = buffErrStreamRead.readLine();

                        if (lineErr != null) {
                            System.err.println(lineErr);
                        }
                    }

                } catch (Exception e) { // exception thrown
                    //System.out.println("Command failed!");
                }
            }
        });

//        thread.setPriority(Thread.MIN_PRIORITY);//set low priority to save cpu time and energie
        thread.setDaemon(true);
        thread.start();
    }

//    /**
//     * SEEMS TO HAVE NO EFFECT YET!!
//     *
//     * print the message on the stream were the process is redirecting it.
//     *
//     * @param process the process which should be used.
//     * @param string the message which should be printed.
//     */
//    public static void printMessage(Process process, String string) {
//        if (process != null) {
//            PrintStream ps = new PrintStream(process.getOutputStream());
//            ps.println(string);
//            ps.close();
//        }
//    }
    /**
     * stops the endless loop of displayJVMOutput(Process p)
     */
    public static void stopJvmOutputRedirection() {
        stopJvmOutputRedirection = true;
    }

    /**
     * Calls remote the stopServer methode.
     */
    public static void stopServerRemotely() {

        XmlRpcClient xmlRpcClient = getClient(getDefaultIP(), getCurrentPort());

        try {
            Object o = xmlRpcClient.execute("RpcHandler.stopWebServer", new Vector());

            System.out.println((Integer) o);

        } catch (XmlRpcException ex) {
            System.out.println("EXCEPTION: Server closed");
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println(JVMmanager.class.getName()+".stopServerRemotely() END");
    }

    /**
     * Tries to start an UG server at localhost at port
     * <code>getCurrentPort()</code>
     */
    public static void startLocalServer() {

        startAnotherJVM(UG.class, getDefaultIP(), getCurrentPort());
    }

    /**
     * WORKS ONLY FOR LOCALHOST!
     *
     * @return a list with all currently running JVMs
     */
    public static List<VirtualMachineDescriptor> listLocalJVMs() {

        List<VirtualMachineDescriptor> vms = VirtualMachine.list();

        String name = null;
        String searchedID = null;
        String text = null;

        for (VirtualMachineDescriptor vmd : vms) {

            name = vmd.displayName();
            searchedID = vmd.id();
            text = " NAME: " + name + " ,ID: " + searchedID;

            try {
//                getMessageBox().addMessage("JVM-Infos", text, MessageType.INFO);
            } catch (Exception ex) {

                System.out.println(JVMmanager.class.getName()
                        + " NO MessageBox found !!!");
//                Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println(JVMmanager.class.getName()
                    + ".listLocalJVMs()" + text);
        }

        return vms;
    }

//    /**
//     * WORKS ONLY FOR LOCALHOST!
//     *
//     * Checks if at least one running JVM name contains shortname in its name
//     * and returns the PID of the first running found JVM.
//     *
//     * @param shortName the name which should be contained in the name of the
//     * searched JVM
//     *
//     * @return the ID as String or NULL if not found
//     */
//    public static String checkoutLocalID(String shortName) {
//
//        ArrayList<VirtualMachineDescriptor> vms =
//                (ArrayList<VirtualMachineDescriptor>) containingLocalJVMs(shortName);
//
//        String searchedID = null;
//
//        for (VirtualMachineDescriptor vmd : vms) {
//            if (vmd.displayName().contains(shortName)) {
//
//                searchedID = vmd.id();
//
//                System.out.println(JVMmanager.class.getName()
//                        + ".checkoutLocalID() ID: " + searchedID);
//                break;
//            }
//        }
//
//        return searchedID;
//    }
//    /**
//     * WORKS ONLY FOR LOCALHOST!
//     *
//     * @param shortName the name which should be contain in the searched JVM
//     *
//     * @return a list with all JVMs which contains
//     * <code>shortName</code> in their name
//     */
//    public static List<VirtualMachineDescriptor> containingLocalJVMs(String shortName) {
//
//        ArrayList<VirtualMachineDescriptor> result =
//                new ArrayList<VirtualMachineDescriptor>();
//
//        for (VirtualMachineDescriptor vmd : listLocalJVMs()) {
//            if (vmd.displayName().contains(shortName)) {
//
//                result.add(vmd);
//                System.out.println("containingLocalJVMs: " + vmd.displayName());
//            }
//        }
//
//        return result;
//    }
//    /**
//     * WORKS ONLY FOR LOCALHOST!
//     *
//     * Checks if at least one running JVM name contains shortname in its name
//     * and returns true if one found.
//     *
//     * @param shortName the name which should be contain in the searched JVM
//     *
//     * @return true if one JVM found
//     *
//     * @throws Exception
//     */
//    public static boolean isLocalServerRunning(String shortName) throws Exception {
//        boolean result = false;
//
//        if ((checkoutLocalID(shortName) != null)) {
//            result = true;
//            System.out.println("isLocalServerRunning(" + " " + shortName
//                    + " " + ")= " + result);
//
//        } else {
//            System.out.println("(checkoutLocalID(shortName) == null");
//        }
//
//        return result;
//    }
    /**
     * Tries to connect to server and returns true if a connection could be
     * established.
     *
     * @param ip is an IP like "192.123.0.34" or the String "localhost"
     * @param port the port were the server should run on
     *
     * @return true is server is accessible, false else
     *
     */
    public static boolean isServerRunning(String ip, Integer port) {

        XmlRpcClient xmlRpcClient = getClient(ip, port);

        try {
            Object o = xmlRpcClient.execute("RpcHandler.isServerRunning", new Vector());

            Boolean b = (Boolean) o;
            System.out.println(b);

            return b.booleanValue();

        } catch (XmlRpcException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connection ERROR");
            return false;
        }

    }

//    /**
//     * Prints information about the thread if thread!=null
//     *
//     * @param thread about information are wished
//     */
//    public static void printThreadInfos(Thread thread) {
//        if (thread != null) {
//            System.out.println("threadID = " + thread.getId());
//            System.out.println("threadName = " + thread.getName());
//            System.out.println("threadPriority = " + thread.getPriority());
//            System.out.println("isAlive = " + thread.isAlive());
//            System.out.println("isInterrupted = " + thread.isInterrupted());
//            System.out.println("ThreadGroupName = " + thread.getThreadGroup().getName());
//            System.out.println("ThreadGroup.activeCount = " + thread.getThreadGroup().activeCount());
//            System.out.println("ThreadGroup.getParentName = " + thread.getThreadGroup().getParent().getName());
//            System.out.println("");
//            thread.getThreadGroup().list();
//        }
//    }
    /**
     *
     * @param ip localhost="127.0.0.1" or ip like e.g. "141.2.22.123" for remote
     * @param port the port which should be used
     *
     * @return the created client for
     */
    private static XmlRpcClient createXmlRpcClient(String ip, int port) {
        boolean result = false;

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

        try {
            config.setServerURL(new URL("http://" + ip + ":" + port));

        } catch (MalformedURLException ex) {
            Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
        }

        //aktiviere erweiterungen
        config.setEnabledForExtensions(true);

        XmlRpcClient xmlRpcClient = new XmlRpcClient();
        xmlRpcClient.setConfig(config);

        return xmlRpcClient;
    }

    /**
     * Adds the ip of a server and the port on which the sever is running to a
     * list of clients.
     *
     * @param ip the ip of the server
     * @param port the port at which communication is done with the server
     * @param client the client which should be used for the communication
     */
    private static void addClient(String ip, Integer port, XmlRpcClient client) {

        clientsForConnection.put(ip + ":" + port, client);
    }

    /**
     *
     * @param ip the ip of the server
     * @param port the port at which communication is done with the server
     *
     * @return a client for the given ip-port-configuration
     */
    public static XmlRpcClient getClient(String ip, Integer port) {

        XmlRpcClient client = clientsForConnection.get(ip + ":" + port);

        if (client == null) {
            client = createXmlRpcClient(ip, port);
            addClient(ip, port, client);
        }

        return client;
    }

    /**
     * Default is: host = "localhost" = "127.0.0.1"
     *
     * @return the defaul host
     */
    public static String getDefaultIP() {
        return defaultIP;
    }

    /**
     * Default is: port = "1099"
     *
     * @return the default port
     */
    public static Integer getDefaultPort() {
        return defaultPort;
    }

    /**
     * @return the currentPort
     */
    public static Integer getCurrentPort() {
        return currentPort;
    }

    /**
     * @param aCurrentPort the currentPort to set
     */
    public static void setCurrentPort(Integer aCurrentPort) {
        currentPort = aCurrentPort;
    }

    /**
     * @return the currentIP
     */
    public static String getCurrentIP() {
        return currentIP;
    }

    /**
     * @param aCurrentIP the currentIP to set
     */
    public static void setCurrentIP(String aCurrentIP) {
        currentIP = aCurrentIP;
    }
}
