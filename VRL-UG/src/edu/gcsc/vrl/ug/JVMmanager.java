package edu.gcsc.vrl.ug;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ObjectInfo;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
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
            final Class clazz,
            final String ip,
            final Integer port) {

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                String separator = System.getProperty("file.separator");
                String classpath = System.getProperty("java.class.path");


                // DONE make general classpath update
                String pluginPath = eu.mihosoft.vrl.system.Constants.PLUGIN_DIR + "/VRL-UG.jar";
                classpath += ":" + pluginPath;

                String path = System.getProperty("java.home")
                        + separator + "bin" + separator + "java";

                String name = clazz.getName();
                String portString = port.toString();
                // to remove outOfMemoryError message: PermGen space
                // or better said resize the PermGen space area in the heap
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
    }

    /**
     * Tries to start an UG server at localhost at port
     * <code>getCurrentPort()</code>
     */
    public static void startLocalServer() {

        startAnotherJVM(UG.class, getDefaultIP(), getCurrentPort());
    }


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
