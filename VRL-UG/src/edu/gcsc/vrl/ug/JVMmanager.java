package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ObjectInfo;
import eu.mihosoft.vrl.io.IOUtil;
import eu.mihosoft.vrl.io.VJarUtil;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.VRL;
import eu.mihosoft.vrl.system.VSysUtil;
import eu.mihosoft.vrl.visual.Canvas;
import java.io.*;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * JVMmanager is helper class to make better seperation between methods which
 * are needed to create / manage a remote communication and the methods which
 * should be called remote in UG.
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
@ObjectInfo(name = "JVMmanager")
@ComponentInfo(name = "JVMmanager", category = "VRL/VRL-UG")
// do not forget to add the class in Configurator.register() via
// vApi.addComponent("THE_CLASSFILE_OF_THIS_CLASS")
public class JVMmanager implements Serializable {

    private static final long serialVersionUID = 1L;
    private static boolean stopJvmOutputRedirection = false;
    private static boolean isServerJVMrunning = false;
    private static Integer defaultPort = 1099;
    private static String defaultIP = "127.0.0.1"; //"localhost"
    private static Integer currentPort = 1099;
    private static String currentIP = "127.0.0.1"; //"localhost"
    // String should have the form "ip:port"
    private static HashMap<String, XmlRpcClient> clientsForConnection
            = new HashMap<String, XmlRpcClient>();
    private static String serverFolderName = null;
    /**
     * to solve references problems which could occur with handling ug objects
     * and server client concept
     */
    private static ArrayList<WeakReference<UGObject>> weakReferencesOfUGObjects
            = new ArrayList<WeakReference<UGObject>>();
    /**
     * decide if by starting a local server the local server should be updated
     * on the basis of the local client
     */
    static boolean updateLocalServer = true;

    /**
     * Starts another JVM and executes there the main method of the class which
     * is the parameter of this method.
     *
     * @param clazz The class which should be started in a new JVM.
     *
     */
    private static void startAnotherJVM(final Class clazz) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                //update the local server folder
                if (updateLocalServer) {
//                    System.out.println("JVMmanager.startAnotherJVM() :"
//                            + "updateLocalServer = true");
                    Configurator.updateLocalServerFolder();
                }

                String separator = System.getProperty("file.separator");
                String classpath = System.getProperty("java.class.path");

                // TODO ! ! ! 
                // check if correct respectively change to numeric-server path
                // make general classpath update
                // This is the wrong path (client-folder) , server path is needed
//                String pluginPath = eu.mihosoft.vrl.system.Constants.PLUGIN_DIR + "/VRL-UG.jar";
//                classpath += ":" + pluginPath;
                // TODO the path to jar of plugin in server folder
//                System.out.println(" - JVMmanager.startAnotherJVM() :");
//                System.out.println(" - - new Thread().run : ");
//                System.out.println(" - - UG.getRemoteType() =" + UG.getRemoteType());
//                File localServerFolder = Configurator.getLocalServerFolder();
                String localServerJar = Configurator.getLocalServerJar();

                String localServerUpdateJar = Configurator.getLocalServerUpdateFolder()
                        + separator + VJarUtil.getClassLocation(UG.class).getName();

//                String split = File.separator;
                String split = ":";
                if (VSysUtil.isWindows()) {
//                    System.out.println("\n - > OS is a WINDOWS ");
                    split = ";";
                }

//                System.out.println(" --- split = "+ split);
                if (localServerJar != null) {
                    classpath += split + localServerJar;

//                    System.out.println("localServerJar = " + localServerJar);
//                    System.out.println("localServerJar.exists() = "
//                            + new File(localServerJar).exists());
                } else {
                    System.out.println("ERROR in JVMmanager.startAnotherJVM():"
                            + " localServerJar is NULL");
                }

//                if (localServerUpdateJar != null) {
//                    classpath += ":" + localServerUpdateJar;
//                    
//                    System.out.println("localServerUpdateJar = " + localServerUpdateJar);
//
//                    System.out.println("localServerJar.exists() = "
//                            + new File(localServerUpdateJar).exists());
//                }else {
//                    System.out.println("ERROR in JVMmanager.startAnotherJVM():"
//                            + " localServerUpdateJar is NULL");
//                }
//                //DEBUG SOUT SART
//                System.out.println("-- --- JVMmanager.startAnotherJVM():ServerJarPath = "
//                        + ServerJarPath);
                String path = System.getProperty("java.home")
                        + separator + "bin" + separator + "java";

//                System.out.println("\n JVMmanager :");
//                System.out.println("separator = " + separator);
//                System.out.println("classpath :");
//                for (String s : classpath.split(split)) {
//                    System.out.println(" " + s + split);
//                }
//                System.out.println("path = " + path + "\n");
//                String strClassPath = System.getProperty("java.class.path");
//                 
//                 System.out.println("Classpath is =" );
//                 for (String s : strClassPath.split(split)) {
//                    System.out.println(" " + s + split);
//                }
//                //DEBUG SOUT END
                String name = clazz.getName();

                serverFolderName = Configurator.getLocalServerFolder().getName();

                // to remove outOfMemoryError message: PermGen space
                // or better said resize the PermGen space area in the heap
                String commandLineCallOptions = "-XX:MaxPermSize=512m";

                ProcessBuilder processBuilder = new ProcessBuilder(
                        path, commandLineCallOptions,
                        "-Xmx1024m",//setting max heap size
                        "-cp", classpath, name, getServerFolderName());

                //NEEDED TO READ / VIEW OUTPUT OF 2nd JVM
                processBuilder.redirectErrorStream(true);

//                //set selfdefined variable in new JVM
//                processBuilder.environment().put("APP_NAME", name);
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
     * Display the output of process p at System.out as long the observered
     * process is killed or the method stopJvmOutputRedirection() is called.
     *
     * @param p the process which output should be redirected
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
     * Stops the endless loop of displayJVMOutput(Process p)
     */
    public static void stopJvmOutputRedirection() {
        stopJvmOutputRedirection = true;
    }

    /**
     * Calls remote the stopWebServer method.
     */
    public static void stopLocalServer() {

        if (UG.getRemoteType().equals(RemoteType.CLIENT)) {

            XmlRpcClient xmlRpcClient = getClient(getDefaultIP(), getCurrentPort());
            Vector empty = new Vector();
            Object o = null;
            try {
                o = xmlRpcClient.execute("RpcHandler.stopWebServer", empty);

                System.out.println((Integer) o);

            } catch (XmlRpcException ex) {
                System.out.println("EXCEPTION: Server closed");

                isServerJVMrunning = false;

//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

//        to be save that a server is not running
            try {
                o = xmlRpcClient.execute("RpcHandler.isServerRunning", empty);

                if (o instanceof Boolean) {
                    System.out.println("webserver should be closed!");
                    isServerJVMrunning = ((Boolean) o).booleanValue();
                    System.out.println("isServerJVMrunning = " + isServerJVMrunning);
                }

            } catch (XmlRpcException ex) {
//            Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
            }

            // MAKE SOME CLEANUP
            //remove the cached client from map
            removeClient(getDefaultIP(), getCurrentPort());

            releaseUGpointers();
        }// END if (UG.getRemoteType().equals(RemoteType.CLIENT))

    }

    /**
     * Tries to start an UG server at localhost at port
     * <code>getCurrentPort()</code>
     */
    public static synchronized void startLocalServer() {
        System.out.println("startLocalServer() : isServerJVMrunning = " + isServerJVMrunning);

        if (UG.getRemoteType().equals(RemoteType.CLIENT)) {

            if (!isServerJVMrunning) {

                isServerJVMrunning = true;

                //make sure nothing from last server run is cached ! ! !
                removeClient(getDefaultIP(), getCurrentPort());
                releaseUGpointers();

                startAnotherJVM(UG.class);//, getDefaultIP(), getCurrentPort());
            }

            System.out.println("startLocalServer() : UG.startLogging()");

            UG.startLogging();
        }
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
//            System.out.println(b);

            return b.booleanValue();

        } catch (XmlRpcException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("Connection ERROR");
            return false;
        }

    }

    /**
     * For efficence reason this method should not be called from a user
     * directly. Use instead
     * <code>getClient(String ip, Integer port)</code>.
     *
     * Creates an Client which is needed for remote communication with a remote
     * server class like e.g. UG with RemoteType SERVER.
     *
     * @param ip localhost="127.0.0.1" or ip like e.g. "141.2.22.123" for remote
     * over network
     * @param port the port which should be used for the communication and the
     * server is listen to.
     *
     * @return the created client
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
     * Adds the ip of a server and the port on which the server is running to a
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
     * Removes the client with "ip" and "port" of the list of clients.
     *
     * @param ip the ip of the server
     * @param port the port at which communication is done with the server
     */
    private static void removeClient(String ip, Integer port) {

        clientsForConnection.remove(ip + ":" + port);
    }

    /**
     * Checks if there is a client with the needed parameters in a list of
     * created clients and returns the matching one or calls
     * <code>createXmlRpcClient(String ip, int port)</code>.
     *
     * @param ip the ip of the server
     * @param port the port at which communication is done with the server
     *
     * @return a client for the given ip-port-configuration
     */
    public static XmlRpcClient getClient(String ip, Integer port) {

        XmlRpcClient client = clientsForConnection.get(ip + ":" + port);

//        System.out.println("JVMmanager.getClient() :"
//                + "clientsForConnection.get(" + ip + ":" + port + ") = " + client);
        if (client == null) {
//            System.out.println("if (client == null)");
            client = createXmlRpcClient(ip, port);
            addClient(ip, port, client);
        }

//        System.out.println("return of getClient(" + ip + ":" + port + ") = " + client);
        return client;
    }

    /**
     * Releases all Pointer of UGObjects. This is needed if we want to connect
     * to another server or we need to start a chrashed server. All Object in
     * VRL-Studio needed to update their pointer, therefore we must force them
     * to do this by releasing their pointer.
     */
    private static void releaseUGpointers() {

        System.out.println("JVMmanager.releaseUGpointers()");
//        System.out.println("weakReferenceSetofUGObjects.size() = "
//                +weakReferencesOfUGObjects.size());
//        
//        long count = 0;

        for (Canvas c : VRL.getCanvases()) {
            if (c instanceof VisualCanvas) {
                MemoryManager.releaseAll((VisualCanvas) c);
            }
        }

        // TODO get releaseUGpointers() working with weak references :-)
        //did not work ! still an invalid memory acess error occur
        //to allow groovy code to interact with UGObjects after server crash
//        for (WeakReference<UGObject> weakReference : weakReferencesOfUGObjects) {
//            UGObject obj = weakReference.get();
//            
//            if(obj!=null){
//                obj.releaseThis();
//            }else{
//                count++;
//                
//            }
//        }
//        System.out.println(count +" UGObjects are NULL in weakReferencesOfUGObjects");
//        System.out.println(UGObject.counter +" UGObjects were created");
//        for (UGObject obj : weakReferenceSetofUGObjects.) {
//            
//            if(obj!=null){
//                obj.releaseThis();
//            }
//        }
    }

    /**
     *
     * @param obj the UGObject to be added
     *
     * @return true if the reference of the UGObject could be added to the
     * collection of references.
     */
    public static boolean addUGObjectToWeakReferences(UGObject ugObject) {
        return weakReferencesOfUGObjects.add(
                new WeakReference<UGObject>(ugObject));
    }

    /**
     This method transfers a file from a VRL-UG client to VRL-UG server via XMLrpc.
    
     @param pathOnServer the path including filename and filetyp ending on the server, 
     were the file should be stored. 
     Notice: 
     1) the path at the server side need to exists.
     2) if a file at these path exist it will be overriden.
     @param file the file on client side that should be transfered
     @return tue if file could be transfered, else false.
     */
    public static Boolean transferFileToServer(String pathOnServer, File file) {
//        System.out.println("JVMmanager.transferFileToServer("
//                + "String pathOnServer = "+ pathOnServer
//                + " , File file.getName() = "+ file.getName() +")");
//        
        if (UG.getRemoteType().equals(RemoteType.CLIENT)) {

            //get the client to communicte with the current server
            XmlRpcClient client = getClient(getCurrentIP(), getCurrentPort());

            // addind all needed parameters into parameter vector in a XMLrpc usable state
            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(pathOnServer);

            try {
                //convert the file into a string
                //and add it to the parameter vector
                xmlRpcParams.addElement(IOUtil.fileToBase64(file));

            } catch (IOException ex) {
                Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

            try {
                //call remote the server method
                Object o = client.execute("RpcHandler.saveFile", xmlRpcParams);

                if (o instanceof Boolean) {

                    return ((Boolean) o).booleanValue();
                }

            } catch (XmlRpcException ex) {
                Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }

        return false;
    }

    public static File getFileFromServer(String pathOnServer, String pathWhereToStore) {
        System.out.println("JVMmanager.getFileFromServer("
                + "String pathOnServer = " + pathOnServer + ")");

        File result = null;

        if (UG.getRemoteType().equals(RemoteType.CLIENT)) {

            System.out.println("if CLIENT");
            String[] splits = pathOnServer.split("\\.");

//            for (int i = 0; i < splits.length; i++) {
//                System.out.println(" splits[" + i + "] = " + splits[i]);
//            }
//            System.out.println(" splits.length = "  +splits.length);
            
            XmlRpcClient client = getClient(getCurrentIP(), getCurrentPort());

            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(pathOnServer);

            try {
                System.out.println("calling getFile() on server");

                Object o = client.execute("RpcHandler.getFile", xmlRpcParams);

                if (o instanceof String) {
                    String data = (String) o;
                    System.out.println("converting file ...");
                    try {
//                        //get the data which are stored in "data"
//                        //store them in the temp file with the right data typ ending (splits[...])
//                        //let result point to this temp file
//                        result = IOUtil.base64ToFile(data, new File(
//                                VRL.getPropertyFolderManager().getTmpFolder().getAbsolutePath()
//                                + "/tmpFile." + splits[splits.length-1]));
                        
                        //no temp file storing directly into result
                        //result will be created where pathWhereToStore points to inculuding file name and ending
                        result = new File(pathWhereToStore);
                        IOUtil.base64ToFile(data,result);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } catch (XmlRpcException ex) {
//            Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }

    /**
     * Default is: defaultIP = "localhost" = "127.0.0.1"
     *
     * @return the default ip
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

        //releaseUGpointers if port has changed to make client possible to
        //react on server change
        if (currentPort != aCurrentPort) {
            JVMmanager.releaseUGpointers();
            currentPort = aCurrentPort;
        }

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

        //releaseUGpointers if IP has changed to make client possible to
        //react on server change
        if (currentIP != aCurrentIP) {
            JVMmanager.releaseUGpointers();
            currentIP = aCurrentIP;
        }
    }

    /**
     * @return the serverFolderName
     */
    public static String getServerFolderName() {
        return serverFolderName;
    }
}
