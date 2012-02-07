package edu.gcsc.vrl.ug;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ObjectInfo;
import eu.mihosoft.vrl.system.VRL;
import eu.mihosoft.vrl.visual.Canvas;
import eu.mihosoft.vrl.visual.MessageBox;
import eu.mihosoft.vrl.visual.MessageType;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
@ObjectInfo(name = "ServerManager")
@ComponentInfo(name = "ServerManager", category = "VRL-UG/remote")
public class ServerManager implements Serializable {

    private static final long serialVersionUID = 1L;
    //port which could used for gui interaction and start local servers at different ports
    private static int port = 1099;
    private static int defaultPort = 1099;
    //host which could used for gui interaction and establishing connectionwith different servers    
    private static String host = "127.0.0.1";//"localhost";
    private static String defaultHost = "127.0.0.1";//"localhost";
//    the local server
    private static WebServer webServer = null;
////    tmp object to make code easier in UG remote calls
//    private static XmlRpcClient xmlRpcClient= null;
//    
    private static boolean stopJvmOutputRedirection = false;

    /**
     * Stops the local JVM, where UG runs in server mode. Must be called over
     * remote call by stopLocalServerRemotely().
     */
    @MethodInfo(noGUI = true)
    private static int stopLocalServer() {

        webServer.shutdown();

        return 0;
    }

    /**
     * Calls remote the stopServer methode of a local seperate JVM.
     */
    public static void stopLocalServerRemotely() {
//        System.out.println(JVMmanager.class.getName()+".stopServerRemotely() START");

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

        try {
            config.setServerURL(new URL("http://" + getDefaultHost() + ":" + getDefaultPort()));

        } catch (MalformedURLException ex) {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        config.setEnabledForExtensions(true);

        XmlRpcClient xmlRpcClient = new XmlRpcClient();
        xmlRpcClient.setConfig(config);

        try {
            Object o = xmlRpcClient.execute("RpcHandler.stopLocalServer", new Vector());

            System.out.println((Integer) o);

        } catch (XmlRpcException ex) {
            System.out.println("EXCEPTION: Server closed");
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println(JVMmanager.class.getName()+".stopServerRemotely() END");
    }

    /**
     * Starts a local JVM, where UG runs in server mode.
     *
     * @param port the port where server should listen for client calls
     */
    private static void startLocalServer(int port) {

        PropertyHandlerMapping mapping = new PropertyHandlerMapping();

        try {
            mapping.addHandler("RpcHandler", RpcHandler.class);

        } catch (XmlRpcException ex) {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        webServer = new WebServer(port);

//        //allow only connection with listed clients
//        webserver.setParanoid(true);
//        webserver.acceptClient("141.2.38.37");
//        webserver.acceptClient("141.2.38.91");
//        //end allow list

        XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();

        //aktiviere erweiterungen
        config.setEnabledForExtensions(true);

        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();


        xmlRpcServer.setConfig(config);
        xmlRpcServer.setHandlerMapping(mapping);

        try {
            webServer.start();

        } catch (IOException ex) {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Tries to connect to server and returns true if a connection could be
     * established.
     *
     * @param host is an IP like "192.123.0.34" or the String "localhost"
     * @param port the port were the server should run on
     *
     * @return true is server is accessible, false else
     *
     */
    public static boolean isServerRunning(String host, int port) {

        XmlRpcClient tmpClient = createXmlRpcClient(host, port);

        try {
            Object o = tmpClient.execute("RpcHandler.isServerRunning", new Vector());

            Boolean b = (Boolean) o;

            System.out.println("Is UG-Server running: " + b);

            return b.booleanValue();

        } catch (XmlRpcException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connection ERROR");
            return false;
        }

    }

    /**
     *
     * @param host localhost="127.0.0.1" or ip like e.g. "141.2.22.123"
     * @param port the port which should be used
     *
     * @return the created client for
     */
    public static XmlRpcClient createXmlRpcClient(String host, int port) {
        boolean result = false;

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

        try {
            config.setServerURL(new URL("http://" + host + ":" + port));

        } catch (MalformedURLException ex) {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        //aktiviere erweiterungen
        config.setEnabledForExtensions(true);

        XmlRpcClient xmlRpcClient = new XmlRpcClient();
        xmlRpcClient.setConfig(config);

        return xmlRpcClient;
    }
//

    /**
     * TEST: Not sure if getting the right MessageBox or NULL !?!
     *
     * @return a MessageBox
     */
    @MethodInfo(noGUI = true)
    private static MessageBox getMessageBox() throws Exception {
        List<Canvas> canvaslist = (List<Canvas>) VRL.getCanvases();
        Canvas c = null;


//        visible &&  toplevelparent
        for (Canvas canvas : canvaslist) {
            if (canvas.isVisible()) {

                return canvas.getMessageBox();
//            Component[] components =canvas.getComponents();


            }
        }
//        //try if there is a canvas with MB
//        return canvaslist.get(0).getMessageBox();
        return null;
    }

    /**
     * Starts another JVM and executes there the main methode of this class.
     *
     * @throws Exception
     */
    public static void startAnotherJVM(final int port) throws Exception {

        System.out.println(ServerManager.class.getSimpleName() + ".startAnotherJVM() START:");

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                String separator = System.getProperty("file.separator");
                String classpath = System.getProperty("java.class.path");

                // TODO make classpath update automatic
//                //manuelles update to find classes 
                String projectPath = "/Users/christianpoliwoda/Apps/VRL-UG4/VRL-UG/dist/VRL-UG.jar";

                classpath += ":" + projectPath;
//                classpath += ":/Users/christianpoliwoda/Apps/VRL-UG/xVRL-UG/build/classes";

//                System.out.println("edu/gcsc/vrl/ug/ServerManager");
//                String projectPath = System.getenv().get("HOME");


                String path = System.getProperty("java.home")
                        + separator + "bin" + separator + "java";

                String name = ServerManager.class.getName();
                String portString = String.valueOf(port);

                String commandLineCallOptions =
                        //                        "-Xms256m -Xmx1024m "
                        //                + "-XX:+UseConcMarkSweepGC "
//                        "-XX:+CMSClassUnloadingEnabled "                      //not known option ! ? ! ? !
//                        + "-XX:+CMSPermGenSweepingEnabled "                   //not known option ! ? ! ? !
//                        + "-XX:PermSize=512m "                                //not known option ! ? ! ? !
                         "-XX:MaxPermSize=512m";


                ProcessBuilder processBuilder = new ProcessBuilder(path,
                        commandLineCallOptions, // to remove outOfMemoryError message: PermGen space
                        "-cp", classpath, name, portString);


                //NEEDED TO READ / VIEW OUTPUT OF 2nd JVM
                processBuilder.redirectErrorStream(true);

                //set selfdefined variable in new JVM
                processBuilder.environment().put("APP_NAME", name);

                Process process = null;

                try {
                    process = processBuilder.start();

                    displayJVMOutput(process);


                } catch (IOException ex) {
                    Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

//        t.setDaemon(true);
        t.start();
    }

    /**
     * display the output of process p at System.out.
     *
     * @param p the process which output shoult be redirected
     */
    private static void displayJVMOutput(final Process p) {

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

    @MethodInfo(noGUI = true)
    public static void main(String[] args) {


        String[] params = {"-property-folder-suffix", "numerics-server",
            "-plugin-checksum-test", "yes", "-rpc", "server"};

        VRL.initAll(params);

//        startLocalServer(getPort());

//        String portForNewServer =args[0];
//        String portForNewServer ="1099";
//        
//        startLocalServer(new Integer(portForNewServer));
    }

    /**
     * @return the port
     */
    public static int getPort() {
        return port;
    }

    /**
     * @param aPort the port to set
     */
    public static void setPort(int aPort) {
        port = aPort;
    }

    /**
     * @return the defaultPort
     */
    public static int getDefaultPort() {
        return defaultPort;
    }

    /**
     * @return the defaultHost
     */
    public static String getDefaultHost() {
        return defaultHost;
    }

    /**
     * @return the host
     */
    public static String getHost() {
        return host;
    }

    /**
     * @param aHost the host to set
     */
    public static void setHost(String aHost) {
        host = aHost;
    }
//    @MethodInfo(noGUI = true)
//    public static boolean isServerRunning() throws Exception {
//        boolean result = false;
//
//        if ((checkoutID("UGServer") != null) || (checkoutID("Main") != null)) {
////            if(checkoutID("edu.gcsc.vrl.ug")!=null){
//            result = true;
//
//
//        }
//
//        return result;
//    }    
//    
//    @MethodInfo(noGUI = true)
//    public static void startAnotherJVM() throws Exception {
//
//        Thread t = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                System.out.println("IN method: startAnotherJVM();");
//                System.err.println("IN method: startAnotherJVM();");
//
//
//                String separator = System.getProperty("file.separator");
//                String classpath = System.getProperty("java.class.path");
//
//                //manuelles update to find classes like ServerManager
//                classpath += ":/Users/christianpoliwoda/Apps/VRL-UG4/VRL-UG/build/classes";
//
//
//                String path = System.getProperty("java.home")
//                        + separator + "bin" + separator + "java";
//
//                System.out.println("separator = " + separator);
//                System.out.println("classpath = " + classpath);
//                System.out.println("path = " + path);
//
//                ProcessBuilder processBuilder = new ProcessBuilder(path,
//                        //                        " -Xms256m"," -Xmx1024m",
//                        //                        " -XX:+UseConcMarkSweepGC",
//                        //                        " -XX:+CMSClassUnloadingEnabled",
//                        //                        " -XX:+CMSPermGenSweepingEnabled",
//                        //                        " -XX:MaxPermSize=256m", 
//                        "-cp", classpath, ServerManager.class.getName());
//
//
//                processBuilder.redirectErrorStream(true);//NEEDED TO READ / VIEW OUTPUT OF 2nd JVM
//
//                Process process = null;
//                try {
//                    process = processBuilder.start();
//                } catch (IOException ex) {
//                    Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//
//                //NEEDED TO READ / VIEW OUTPUT OF 2nd JVM
//                try {
//
//                    // hook up child process output to parent
//                    InputStream inStream = process.getInputStream();
//                    InputStreamReader inStreamRead = new InputStreamReader(inStream);
//                    BufferedReader buffInStreamRead = new BufferedReader(inStreamRead);
//
//                    // hook up child process error output to parent
//                    InputStream errStream = process.getErrorStream();
//                    InputStreamReader errStreamRead = new InputStreamReader(errStream);
//                    BufferedReader buffErrStreamRead = new BufferedReader(errStreamRead);
//
//
//                    // read the child process' output
//                    String line;
//                    while ((line = buffInStreamRead.readLine()) != null) {
//                        System.out.println(line);
//                    }
//
//                    String line2;
//                    // read the child process' error output
//                    while ((line2 = buffErrStreamRead.readLine()) != null) {
//                        System.err.println(line2);
//                    }
//
//                } catch (Exception e) { // exception thrown
//
//                    System.out.println("Command failed!");
//
//                }
//
////        process.waitFor();
//            }
//        });
//
//        t.setDaemon(true);
//        t.start();
//    }
//
//    @MethodInfo(hide = false)
//    public static List<VirtualMachineDescriptor> listJVMs() {
//
//        List<VirtualMachineDescriptor> vms = VirtualMachine.list();
//
//        String name = null;
//        String searchedID = null;
//        String text = null;
//
//        for (VirtualMachineDescriptor vmd : vms) {
//
//            name = vmd.displayName();
//            searchedID = vmd.id();
//            text = " NAME: " + name + " ,ID: " + searchedID;
//
//            try {
//                getMessageBox().addMessage("JVM-Infos", text, MessageType.INFO);
//
//            } catch (Exception ex) {
//
//                System.out.println(ServerManager.class.getName()
//                        + " NO MessageBox found !!!");
////                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            System.out.println(ServerManager.class.getName()
//                    + ".listJVMs()" + text);
//        }
//
//        return vms;
//    }
//
//    /**
//     *
//     * @param shortName the name which should be contained in the name of the
//     * searched JVM
//     *
//     * @return the ID as String or NULL if not found
//     */
//    @MethodInfo(noGUI = true)
//    public static String checkoutID(String shortName) {
//
//        ArrayList<VirtualMachineDescriptor> vms =
//                (ArrayList<VirtualMachineDescriptor>) containingJVMs(shortName);
//
//        String name = null;
//        String searchedID = null;
//
//        for (VirtualMachineDescriptor vmd : vms) {
//            if (vmd.displayName().contains(shortName)) {
//
//                name = vmd.displayName();
//                searchedID = vmd.id();
//
//                System.out.println(ServerManager.class.getName()
//                        + ".checkoutID()" + " NAME: " + name + " ,ID: " + searchedID);
//            }
//        }
//
//        return searchedID;
//    }
//
//    /**
//     *
//     * @param shortName the name which should be contain in the searched JVM
//     *
//     * @return a list with all JVMs which contains
//     * <code>shortName</code> in their name
//     */
//    @MethodInfo(noGUI = true)
//    public static List<VirtualMachineDescriptor> containingJVMs(String shortName) {
//
//        ArrayList<VirtualMachineDescriptor> result =
//                new ArrayList<VirtualMachineDescriptor>();
//
//        for (VirtualMachineDescriptor vmd : listJVMs()) {
//            if (vmd.displayName().contains(shortName)) {
//
//                result.add(vmd);
//            }
//        }
//
//        return result;
//    }
}
