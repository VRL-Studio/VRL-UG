/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.Base64;
import eu.mihosoft.vrl.io.IOUtil;
import eu.mihosoft.vrl.io.VJarUtil;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.*;
import eu.mihosoft.vrl.visual.SplashScreenGenerator;
import java.beans.XMLDecoder;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

/**
 * <p> UG class represents UG and its scripting functionality. It allows only
 * one instance which can be obtained via
 * {@link #getInstance(eu.mihosoft.vrl.reflection.VisualCanvas) }. </p> <p>
 * <b>Note:</b> this singleton must not be loaded by more than one classloader
 * per JVM! Although this is no problem for Java classes, it does not work for
 * native libraries. Unfortunately, we cannot provide an acceptable workaround.
 * Thus, it is recommended to use this class from a valid VRL plugin only. The
 * VRL plugin system is aware of this problem and handles it correctly. </p>
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UG {

    /**
     * maximum number of chars in log
     */
    private static int logMaxChars = 10000;
    /**
     * log refresh interval in milliseconds
     */
    private static long logRefreshInterval = 200;
    /**
     * native classes
     */
    private static Class<?>[] nativeClasses;
    /**
     * ug messages
     */
    private static StringBuilder messages = new StringBuilder();
    /**
     * ug instance
     */
    private static UG ugInstance;

    /**
     * @return the libloaded
     */
    public static boolean isLibloaded() {
        return libLoaded;
    }
    private static RemoteType remoteType = RemoteType.NONE;

    /**
     * Sets the RemoteType of an UG instance in a JVM
     *
     * @param remoteType the RemoteType of the UG instance
     *
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    static void setRemoteType(RemoteType remoteType) {
        if (remoteType == null) {
            UG.remoteType = RemoteType.NONE;
        } else {
            UG.remoteType = remoteType;
        }

    }

    /**
     *
     * @return the RemoteType of the UG instance
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    static RemoteType getRemoteType() {

        return UG.remoteType;
    }
    /**
     * The webserver which listen at a specific port and allows UG to interact
     * as a remote server.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    private static WebServer webServer = null;

    /**
     * Closes the webserver of the UG server instance, with the effect that the
     * UG server JVM is closed by the Operating system.
     *
     * Needs to be executed in the server JVM than works shutdown.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    public static synchronized void stopWebServer() {
        if (webServer != null) {
            System.out.println("WebServer.shutdown();");
            webServer.shutdown();

        } else {
            System.out.println("webserver==null");
        }
    }

    /**
     * Needed for testing if a connection could be established to server UG.
     *
     * @return true if UGs RemoteType is SERVER, else false.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    public static boolean isServerRunning() {
        if (UG.getRemoteType().equals(RemoteType.SERVER)) {
            return true;
        }
        return false;
    }

    /**
     * Starts a webserver, if UGs RemoteType is SERVER. This allwos these UG
     * instance to act as server listing on a specific port.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    private static void startWebServer() {

        if (UG.getRemoteType().equals(RemoteType.SERVER)) {

            PropertyHandlerMapping mapping = new PropertyHandlerMapping();

            try {
//                NEED to be started in the right JVM
//                means need to be executed in main() from new JVM
//                RpcHandler.setServer(UG.getInstance(null, RemoteType.SERVER));

                mapping.addHandler("RpcHandler", RpcHandler.class);

            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            webServer = new WebServer(JVMmanager.getCurrentPort());

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
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Executing this method starts initialization process of a VRL instance
     * with needed parameters to allow UG to act as server.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    public static void main(String[] args) {


        String serverFolderSuffix = "default";

        // check if there is a server 
        for (int i = 0; i < args.length; i++) {

            if (args[i].contains(Constants.SERVER_SUFFIX)) {
                serverFolderSuffix = args[i];
            }
        }

        String[] params = {"-property-folder-suffix", serverFolderSuffix,
            "-plugin-checksum-test", "yes",
            "-" + Constants.REMOTETYPE_KEY, RemoteType.SERVER.toString()};

        VRL.initAll(params);

//        set in the server JVM the server ug object
        RpcHandler.setServer(UG.getInstance(null, RemoteType.SERVER));


    }
    /**
     * VRL canvas used to visualize ug classes
     */
    private static VisualCanvas mainCanvas;
    /**
     * Messaging thread which displays messages generated from native UG
     * methods.
     */
    private static MessageThread messagingThread;

    /**
     * @return the messagingThread
     */
    public static MessageThread getMessagingThread() {
//        System.out.println(" +#+ #+# UG.getMessagingThread()");
        return messagingThread;
    }

    /**
     * @param aMessagingThread the messagingThread to set
     */
    public static void setMessagingThread(MessageThread aMessagingThread) {
//        System.out.println(" +#+ #+# UG.setMessagingThread( mT )");
        messagingThread = aMessagingThread;
    }
    /**
     * Indicates whether ug instance is initialized.
     */
    private boolean initialized = false;
    /**
     * Indicates whether API has been recompiled.
     */
    private boolean recompiled = false;
    /**
     * Indicates whether native ug lib loaded
     */
    private static boolean libLoaded = false;
    /**
     * temp folder conaining native libs (must be set from ug plugin, not from
     * api)
     */
    private static File nativeLibFolder;
    /**
     * api class
     */
    private Class<?> api;

    /**
     * Returns the api description
     *
     * @return api description
     */
    public String getDescriptionFromApi() {

        String result = "no description available";

        if (api != null) {
            try {

                result = (String) api.getMethod(
                        "getDescription").
                        invoke(api);

            } catch (NoSuchMethodException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * Loads all native librarties in the specified folder and optionally all of
     * its subfolders. Please ensure that all libraries in the folder are
     * compatible with the current os. <p><b>Note: </b>this method is an EXACT
     * copy of
     * {@link eu.mihosoft.vrl.system.VSysUtil#loadNativeLibrariesInFolder(java.io.File, boolean)
     * }. This is necessary as using the original method would load the native
     * libraries in the wrong classloader (the classloader that loaded
     * {@link eu.mihosoft.vrl.system.VSysUtil)}. </p>
     *
     * @param folder library folder
     * @param recursive defines whether recusrively load libraries from sub
     * folders
     *
     * @return
     * <code>true</code> if all native libraries could be loaded;
     * <code>false</code> otherwise
     */
    private static boolean loadNativeLibrariesInFolder(
            File folder, boolean recursive) {
        VParamUtil.throwIfNotValid(
                VParamUtil.VALIDATOR_EXISTING_FOLDER, folder);

        final String dylibEnding = "." + VSysUtil.getPlatformSpecificLibraryEnding();

        Collection<File> dynamicLibraries = new ArrayList<File>();

        if (recursive) {
            dynamicLibraries.addAll(
                    IOUtil.listFiles(folder, new String[]{dylibEnding}));
        } else {
            File[] libFiles = folder.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(dylibEnding);
                }
            });
            dynamicLibraries.addAll(Arrays.asList(libFiles));
        }

        System.out.println(">> loading native libraries:");

        ArrayList<String> loadedLibraries = new ArrayList<String>();

        int lastSize = -1;

        while (loadedLibraries.size() > lastSize) {

            lastSize = loadedLibraries.size();

            for (File f : dynamicLibraries) {

                String libName = f.getAbsolutePath();

//                if (!VSysUtil.isWindows()) {
//                    libName = libName.replaceFirst("lib", "");
//                }
//
//                libName = libName.substring(0,
//                        libName.length() - dylibEnding.length());

                if (!loadedLibraries.contains(libName)) {
                    System.out.print(" --> " + f.getName());
                    try {
                        System.load(libName);
                        loadedLibraries.add(libName);
                        System.out.println(" [OK]");
                    } catch (Exception ex) {
                        System.out.println(" [ERROR]");
                        ex.printStackTrace(System.err);
                    }
                }
            }
        }

        System.out.println(" --> done.");

        return loadedLibraries.size() == dynamicLibraries.size();
    }

    /**
     *
     * @param loadNativeLib
     */
    public static void connectToNativeUG(boolean loadNativeLib) {

        if (remoteType.equals(RemoteType.CLIENT)) {
            System.err.println("Cannot connect to native UG in client mode!");
            ugInit(new String[]{});  //non native
            return;
        }

        // path to the native ug plugins
        String pluginPath = getNativeLibFolder()
                + "/plugins".replace("/", File.separator);

        String[] args = {pluginPath};

        if (loadNativeLib) {

            File libFolder = getNativeLibFolder();

            loadNativeLibrariesInFolder(libFolder, false);

            libLoaded = true;
        }

        try {
            // initialize native ug libraries with the native ug plugins
            ugInit(args);

        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }

    }

    /**
     * instanciation only allowed in this class
     */
    private UG() {
        // we must set the singleton instance to prevent
        // calling multiple constructors.
        // doing this in the corresponding getter methods does not work anymore
        // as we need the instance for searching a compiled UG-API.
        ugInstance = this;

        System.out.println(" -- UG() -- ");

        // load api if compatible; rebuild otherwise
        try {
            Class<?> cls = findCompatibleAPI(ugInstance);

            api = cls;

            if (cls == null) {

                // load native library and connect to ug lib to generate api
                connectToNativeUG(true);

                NativeAPIInfo nativeAPI = convertRegistryInfo();
                Compiler compiler = new edu.gcsc.vrl.ug.Compiler();

                try {

                    recompiled = true;

                    System.err.println(
                            VTerminalUtil.red(
                            " --> VRL-UG-API missing.\n"
                            + " --> Recompiling API..."));

                    SplashScreenGenerator.printBootMessage(
                            ">> UG: recompiling API (this may take a while) ...");

                    // generates jar file in plugin path
                    compiler.compile(
                            new edu.gcsc.vrl.ug.NativeAPICode(
                            nativeAPI).getAllCodes(),
                            VRL.getPropertyFolderManager().getPluginUpdatesFolder().
                            getAbsolutePath());

                } catch (Exception ex) {
                    Logger.getLogger(UG.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(UG.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        initialized = libLoaded;
    }

    // TODO remove UG() resp. redirect to UG(RemoteType.NONE) 
    // DONE handle the RemoteType case NONE in UG(remoteType)
    /**
     * Do the same stuff as the constructor without parameters, with the
     * exeption that it checks which role the UG instance should play and diceds
     * which part of initialization should be done for the choosen role.
     *
     * @param remoteType
     */
    private UG(RemoteType remoteType) {

        setRemoteType(remoteType);

        System.out.println(" -- UG(" + getRemoteType() + ") -- ");


//         // we must set the singleton instance to prevent
//        // calling multiple constructors.
//        // doing this in the corresponding getter methods does not work anymore
//        // as we need the instance for searching a compiled UG-API.
        ugInstance = this;

        boolean isServerRunning = false;

        if (remoteType.equals(RemoteType.CLIENT)) {

            try {
                //try if there is local UG server on default port running
                isServerRunning = JVMmanager.isServerRunning(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

            } catch (Exception ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

//            System.out.println(" # + # + #  is local server with default port running= " + isServerRunning);

            //UGServer not running
            if (!isServerRunning) {
                try {

                    SplashScreenGenerator.printBootMessage(" --> starting local server (may take a while)");
                    JVMmanager.startLocalServer();


                } catch (Exception ex) {
                    Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
                }


                int wait = 15;
                int counter = 0;
                int maxWait = 14;

//                System.out.println(" Server checking time variables set:");
//                System.out.println(" wait = "+ wait);
//                System.out.println(" counter = "+ counter);
//                System.out.println(" maxWait = "+ maxWait);

                SplashScreenGenerator.printBootMessage(" . . checking every " + wait
                        + " secs for finishing start of server");

                System.out.println(" . . checking every " + wait
                        + " secs for finishing start of server");

                //wait until sever is booted and running
                while ((!isServerRunning) && (counter < maxWait)) {
                    counter++;
                    try {

                        TimeUnit.SECONDS.sleep(wait);

                        String waitString = " . . waited " + counter
                                + " time" + (wait < 2 ? "" : "s")
                                + " of max = " + maxWait + ".";

                        SplashScreenGenerator.printBootMessage(waitString);
                        System.out.println(waitString);

                        isServerRunning = JVMmanager.isServerRunning(
                                JVMmanager.getCurrentIP(), JVMmanager.getCurrentPort());

                    } catch (Exception ex) {
                        Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            System.out.println(" . . isServerRunning=" + isServerRunning);

        }//END if (remoteType.equals(RemoteType.CLIENT)) 

        if ((isServerRunning) || (remoteType.equals(RemoteType.NONE))) {

//            System.out.println("if ((isServerRunning = " + isServerRunning + ") || "
//                    + "(remoteType.equals(RemoteType.NONE))) = "
//                    + remoteType.equals(RemoteType.NONE));

            // load api if compatible; rebuild otherwise
            try {
                Class<?> cls = findCompatibleAPI(ugInstance);

                api = cls;

                //if is needed because error message if client mode
                if (remoteType.equals(RemoteType.NONE)) {
//                    System.out.println("if (remoteType.equals(RemoteType.NONE)) == true");

                    // load native library and connect to ug lib to generate api
                    connectToNativeUG(true);
                }


                if (api == null) {

                    NativeAPIInfo nativeAPIInfo = convertRegistryInfo();
                    Compiler compiler = new edu.gcsc.vrl.ug.Compiler();

//                    // new added for test in 20130522
//                    if (nativeAPIInfo == null) {
//                        System.err.println(" ## Java: VRL-UG");
//                        System.err.println(" ## package: edu.gcsc.vrl.ug");
//                        System.err.println(" ## class: UG");
//                        System.err.println(" ## method: UG(RemoteType)");
//                        System.err.println(" ## nativeAPIinfo is null");
//                    } else {
//                        System.err.println(" ## nativeAPIinfo NOT null");
//                    }

                    try {
                        recompiled = true;

                        System.err.println(
                                VTerminalUtil.red(
                                " --> VRL-UG-API missing.\n"
                                + " --> Recompiling API..."));

                        SplashScreenGenerator.printBootMessage(
                                ">> UG: recompiling API (this may take a while) ...");

                        edu.gcsc.vrl.ug.NativeAPICode nativeAPICode =
                                new edu.gcsc.vrl.ug.NativeAPICode(nativeAPIInfo);

//                        // new added for test in 20130522
//                        if (nativeAPICode == null) {
//                            System.err.println(" ## Java: VRL-UG");
//                            System.err.println(" ## package: edu.gcsc.vrl.ug");
//                            System.err.println(" ## class: UG");
//                            System.err.println(" ## method: UG(RemoteType)");
//                            System.err.println(" ## nativeAPICode is null");
//                        } else {
//                            System.err.println(" ## nativeAPICode NOT null");
//                        }

                        String[] allCodes = nativeAPICode.getAllCodes();

//                        // new added for test in 20130522
//                        if (nativeAPIInfo == null) {
//                            System.err.println(" ## Java: VRL-UG");
//                            System.err.println(" ## package: edu.gcsc.vrl.ug");
//                            System.err.println(" ## class: UG");
//                            System.err.println(" ## method: UG(RemoteType)");
//                            System.err.println(" ## String[] allCodes is null");
//                        } else {
//                            System.err.println(" ## String[] allCodes NOT null");
//                        }

                        // generates jar file in plugin path
                        compiler.compile(allCodes,
                                VRL.getPropertyFolderManager().getPluginUpdatesFolder().getAbsolutePath());

                    } catch (Exception ex) {
                        Logger.getLogger(UG.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }
                }

            } catch (Exception ex) {
                Logger.getLogger(UG.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            initialized = libLoaded;

        }//END if ((isServerRunning) || (remoteType.equals(RemoteType.NONE))) 

        if (remoteType.equals(RemoteType.SERVER)) {

//            System.out.println(" # # UG(" + remoteType + ").startWebServer() ");
            UG.startWebServer();

            // load native library and connect to ug lib to generate api
            connectToNativeUG(true);

        }//END if (remoteType.equals(RemoteType.SERVER)) 
    }

    /**
     * Tries to find a compatible api in the class path. Compatibility is
     * defined as equal svn revision and equal compile date.
     *
     * @param ug ug instance used to check for compatibility.
     * @return a compatible api class object or
     * <code>null</code> if no such api could be found
     */
    private static Class<?> findCompatibleAPI(UG ug) {
        try {
            ClassLoader cl = null;
            try {
                cl = new URLClassLoader(
                        new URL[]{new File(eu.mihosoft.vrl.system.Constants.PLUGIN_DIR
                    + "/" + Compiler.API_JAR_NAME).toURI().toURL()});
            } catch (MalformedURLException ex) {
                Logger.getLogger(VRL.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            if (cl == null) {
                System.err.println("Classloader not found: This should never"
                        + " happen. Please "
                        + eu.mihosoft.vrl.system.Constants.WRITE_VRL_BUG_REPORT_PLAIN);

                return null;
            }

            Class<?> cls = cl.loadClass("edu.gcsc.vrl.ug.api.UGAPI");

            String apiSvn = (String) cls.getMethod(
                    "getSvnRevision").
                    invoke(cls);

            String apiDate = (String) cls.getMethod(
                    "getCompileDate").
                    invoke(cls);

//            boolean revisionsAreEqual = apiSvn.equals(ug.getSvnRevision());
//            boolean datesAreEqual = apiDate.equals(ug.getCompileDate());

//            if (revisionsAreEqual && datesAreEqual) {
            System.out.println(
                    VTerminalUtil.green(" --> VRL-UG: "
                    + "API found\n"
                    + " --> svn: present=" + apiSvn + "\n"
                    + " --> date: present=" + apiDate + "\n"
                    + " --> location: " + VJarUtil.getClassLocation(cls)));

            SplashScreenGenerator.printBootMessage(">> UG: API found");

            return cls;
//            } else {
//                System.err.println(
//                        VTerminalUtil.red(" --> VRL-UG:"
//                        + "API version missmatch\n"
//                        + " --> svn: present="
//                        + apiSvn + ", requested=" + ug.getSvnRevision() + "\n"
//                        + " --> date: present="
//                        + apiDate + ", requested=" + ug.getCompileDate()));
//            }
        } catch (ClassNotFoundException ex) {
        } catch (NoSuchMethodException ex) {
        } catch (IllegalAccessException ex) {
        } catch (InvocationTargetException ex) {
        }

//        System.err.println("FAILED");
//
//        System.exit(18);

        return null;
    }

    public boolean isRecompiled() {
        return recompiled;
    }

    /**
     * Returns the api classes defined in the jar-file the specified class
     * object is loaded from.
     *
     * @param cls api class
     * @return the api classes
     */
    public static Class<?>[] getAPiClasses(Class<?> cls) {
        try {
            ClassLoader cl = cls.getClassLoader();

            URL url = cls.getResource(
                    "/edu/gcsc/vrl/ug/api/UG_INFO.XML");

            Thread.currentThread().setContextClassLoader(cl);

            XMLDecoder decoder = new XMLDecoder(url.openStream());

            AbstractUGAPIInfo apiInfo =
                    (AbstractUGAPIInfo) decoder.readObject();

            decoder.close();

            Class<?>[] result =
                    new Class<?>[apiInfo.getClassNames().size()];

            for (int i = 0; i < apiInfo.getClassNames().size(); i++) {

                String clsName = apiInfo.getClassNames().get(i);

                result[i] = cl.loadClass(
                        "edu.gcsc.vrl.ug.api."
                        + clsName);
            }

            return result;
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }

        return new Class<?>[0];
    }

    /**
     * <p> Returns the instance of this singleton. If it does not exist it will
     * be created. <p> <p> <b>Note:</b> this singleton must not be loaded by
     * more than one classloader per JVM! Although this is no problem for Java
     * classes it does not work for native libraries. Unfortunately we cannot
     * provide an acceptable workaround. Thus, it is recommended to use this
     * method from a valid VRL plugin only. The VRL plugin system is aware of
     * this problem and handles it correctly. </p>
     *
     * @param canvas VRL canvas that shall be used to visualize ug classes
     * @param remoteType declares if remote communication is activated or not
     * and how UG should act as Server or Client
     * @return the instance of this singleton
     */
    public static synchronized UG getInstance(VisualCanvas canvas,
            RemoteType remoteType) {

        if (ugInstance == null) {

            ugInstance = new UG(remoteType);

            if (canvas != null) {
                ugInstance.setMainCanvas(canvas);
            }

        } else if (canvas != null) {
            ugInstance.setMainCanvas(canvas);
        }

        return ugInstance;
    }

    /**
     * Needed for the start / create of UG with RemoteType Server in another
     * JVM.
     *
     * @param option dicedes in which role UG should interact with envirement
     *
     * @return the wished version of UG in the coresponding RemoteType
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    public static synchronized UG getInstance(String option) {

//        System.out.println("  synchronized UG.getInstance(String option) = " + option);

        if (option != null) {

            if (option.toLowerCase().equals("server")) {
//                System.out.println("SERVER");
                return getInstance(null, RemoteType.SERVER);

            } else if (option.toLowerCase().equals("client")) {
//                System.out.println("CLIENT");
                return getInstance(null, RemoteType.CLIENT);

            } else if (option.toLowerCase().equals("none")) {
//                System.out.println("NONE");
                return getInstance(null, RemoteType.NONE);
            } else {
                System.out.println("in UG.getInstance(String option),"
                        + " option could not be recognized.");
            }
        }

        return getInstance();
    }

    /**
     * <p> Returns the instance of this singleton. </p> <p> <b>Note:</b>If
     * message logging shall be used, please assign a visible canvas. For this
     * {@link #getInstance(eu.mihosoft.vrl.reflection.VisualCanvas) }
     * can be used. </p> <p> <b>Note:</b> this singleton must not be loaded by
     * more than one classloader per JVM if native libs have been loaded!
     * Although this is no problem for Java classes, it does not work for native
     * libraries. Unfortunately, we cannot provide an acceptable workaround.
     * Thus, it is recommended to use this method from a valid VRL plugin only.
     * The VRL plugin system is aware of this problem and handles it correctly.
     * </p>
     */
    public static UG getInstance() {
        return getInstance(null, getRemoteType());
    }

    /**
     * Returns the VRL canvas that is used to visualize the UG classes.
     *
     * @return the VRL canvas that is used to visualize the UG classes
     */
    public VisualCanvas getMainCanvas() {
        return mainCanvas;
    }

    /**
     * <p> The VRL canvas that shall be used to visualize the UG classes </p>
     * <p> <b>Notice:</b> as a side effect this method starts UG message logging
     * </p>
     *
     * @param mainCanvas the VRL canvas to set
     */
    public void setMainCanvas(VisualCanvas mainCanvas) {
        this.mainCanvas = mainCanvas;
        stopLogging();
        startLogging();
    }

    /**
     * Starts UG message logging.
     */
    public static void startLogging() {

        stopLogging();

        MessageThread t = new MessageThread();

        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        t.setPriority(Thread.NORM_PRIORITY);

        setMessagingThread(t);

    }

    /**
     * Stops UG message logging.
     */
    public static void stopLogging() {
        if (getMessagingThread() != null) {
            getMessagingThread().stopLogging();
        }
    }

    /**
     * @author Michael Hoffer <info@michaelhoffer.de>
     * @return the messages
     *
     * TODO Updated to work with remoteTypes
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    static StringBuilder getMessages() {

        if (remoteType.equals(RemoteType.CLIENT)
                && JVMmanager.isServerRunning(
                JVMmanager.getCurrentIP(),
                JVMmanager.getCurrentPort())) {

//            System.out.println("UG.getMessages(): \n"
//                    + "if(remoteType.equals(RemoteType.CLIENT) && "
//                    + "JVMmanager.isServerRunning(currentIP, currentPort)");

            Object o = null;

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.getMessages", voidElement);
            } catch (XmlRpcException ex) {
//                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
//                System.out.println("UG.getMessages(): catch of -> \n"
//                        + "xmlRpcClient.execute(RpcHandler.getMessages, voidElement)"
//                        + " = " + o);
            }

            String base64 = (String) o;

            o = Base64.decodeToObject(base64, UG.class.getClassLoader());

            return (StringBuilder) o;

        } else {

            return messages;
        }
    }

    public static void addMessage(String s) {
        messages.append(s);

        if (messages.length() > logMaxChars) {
            messages.delete(0, logMaxChars);
        }
    }

    public static void clearMessages() {

        if (remoteType.equals(RemoteType.CLIENT)) {

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                System.out.println("UG.clearMessages()");

                xmlRpcClient.execute("RpcHandler.clearMessages", voidElement);

            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            if (messages.length() > 0) {
                messages.delete(0, messages.length());
                //messages = new StringBuilder();
            }
        }
    }

    /**
     * @return the nativeLibFolder
     */
    static File getNativeLibFolder() {
        return nativeLibFolder;
    }

    /**
     * @param nativeLibFolder the nativeLibFolder to set
     */
    static void setNativeLibFolder(File nativeLibFolder) {
        UG.nativeLibFolder = nativeLibFolder;
    }

    static class MessageThread extends Thread {

        private boolean logging = true;

        public MessageThread() {
            //
            setName("MessageThread-" + remoteType);
        }

        @Override
        public void run() {

            if (!remoteType.equals(RemoteType.SERVER)) {
                while (logging) {

                    try {
                        Thread.sleep(logRefreshInterval);
                    } catch (InterruptedException ex) {
                        //
                    }

                    try {
                        final StringBuilder messages = getMessages();

                        if (getMessages().length() > 0) {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    if (mainCanvas != null && messages.length() > 0) {
//                                mainCanvas.getMessageBox().addMessageAsLog(
//                                        "UG-Output:",
//                                        /*"<pre>" +*/ messages.toString().replace("<br>", "\n") /*+ "</pre>"*/,
//                                        MessageType.INFO);
                                        Messaging.getStream(Messaging.MSG_OUT).print(
                                                messages.toString().replace("<br>", "\n"));
                                        clearMessages();
                                    }
                                }
                            });
                        }//if
                    } catch (Exception ex) {
                        // client Thread chrashed if local server is not available
                        // ignore it and start a new one
                        System.out.println(" . . . -> client Thread chrashed (ignoring)");
                    }
                }//end while
            }//if ( ! remoteType.equals(RemoteType.SERVER)) 

        }

        /**
         * @param logging the logging to set
         */
        public void stopLogging() {
            this.logging = false;
        }
    }

    // ********************************************
    // ************** NATIVE METHODS **************
    // ********************************************
    /**
     * The native methods which will be executed by the server version of UG
     * have for better readability of the source code the following name
     * convencien:
     * <code>_methodName</code>
     * If you change a native method you need to rebuild first this class and
     * second the binding header file (bindings_vrl_native.h), which is needed
     * for native inplementation of UG.
     * Changing means:
     * - add / remove a methode
     * - rename a method
     * - change the parameter list of a method
     * You can generate "bindings_vrl_native.h" automatically by executing
     * "create-header.sh" which should be checked out with this project and be 
     * stored in the parent folder of this project.
     */
    final native NativeAPIInfo _convertRegistryInfo();

    native Object _invokeMethod(
            String exportedClassName,
            long objPtr, boolean readOnly,
            String methodName, Object[] params);

    native Pointer _newInstance(long exportedClassPtr, Object[] parameters);

    native long _getExportedClassPtrByName(String name, boolean classGrp);

    native String _getDefaultClassNameFromGroup(String grpName);

    native Object _invokeFunction(String name,
            boolean readOnly, Object[] params);

    native String _getSvnRevision();

    native String _getUGVersion();

    native String _getDescription();

    native String _getAuthors();

    native String _getCompileDate();

    native String _getBinaryLicense();

    public static native int _ugInit(String[] args);
    
    /**
     * Deallocates specified memory. The destructor of the specified class will
     * be called.
     *
     * @param objPtr object pointer
     * @param exportedClassPtr pointer of the exported class
     */
    @Deprecated
    native static void _delete(long objPtr, long exportedClassPtr);

    /**
     * Invalidates the specified smart pointer.
     *
     * @param p smart-pointer to invalidate
     */
    native static void _invalidate(SmartPointer p);
    
    
    
    // this method is only for debug issues and should NOT be used later
    native static Object _test_debug(String name, Object[] params);

    
    //
    //
    /**
     * NOTICE:
     *
     * ALL METHODS need to RETURN one of the following types:
     *
     * XML-RPC type     Simplest Java type      More complex Java type
     *
     * i4               int                     java.lang.Integer 
     * int              int                     java.lang.Integer 
     * boolean          boolean                 java.lang.Boolean 
     * string           java.lang.String        java.lang.String 
     * double           double                  java.lang.Double
     *
     * dateTime.iso8601 java.util.Date          java.util.Date 
     * struct           java.util.Hashtable     java.util.Hashtable
     * array            java.util.Vector        java.util.Vector
     * base64           byte[]                  byte[]
     *
     * nil (extension)  null                    null
     *
     * ATTENTION: void is NOT valid !!!
     */
    // ******************************************************
    // **************     WRAPPER  METHODS     **************
    // **************    REMOTE  or  NATIVE    **************
    // ******************************************************
    //
    //
    //needed for void parammeters
    private static Vector voidElement = new Vector();

    //
    // Remember all "_methodName()" are native !!!
    //
    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    final NativeAPIInfo convertRegistryInfo() {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.convertRegistryInfo", voidElement);

            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            //@DONE String base64 remote transfer 
            //      and decode here to NativeAPIInfo !!!!!
            String base64 = (String) o;

            o = Base64.decodeToObject(base64, UG.class.getClassLoader());

            if (o instanceof NativeAPIInfo) {
                NativeAPIInfo napiInfo = (NativeAPIInfo) o;

                return napiInfo;

            } else if (o != null) {
                System.out.println(" - - - - ERROR - - - -");

                System.out.println("class of o = " + o.getClass());

                throw new IllegalArgumentException(this.getClass()
                        + ".convertRegistryInfo() got over XMLRPC an object"
                        + "which is not instance of NativeAPIInfo.");
            } else {
                System.out.println(" - - - - ERROR o==null - - - -");
                throw new IllegalArgumentException(this.getClass()
                        + ".convertRegistryInfo() got over XMLRPC an object"
                        + "which is not instance of NativeAPIInfo.");
            }
        } else {//if not RemoteType==CLIENT

            return _convertRegistryInfo();
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    Object invokeMethod(String exportedClassName, long objPtr, boolean readOnly,
            String methodName, Object[] params) {

//        System.out.println("---- ---- UG.invokeMethod() paramas values are: ---- ----");
//        System.out.println("exportedClassName = " + exportedClassName);
//        System.out.println("objPtr = " + objPtr);
//        System.out.println("readOnly = " + readOnly);
//        System.out.println("methodName = " + methodName);
//        System.out.println("params = " + params);
//
//        for (int i = 0; i < params.length; i++) {
//            System.out.println("    params[" + i + "] = " + params[i]);
//        }


        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;
            String base64 = null;

            Vector xmlRpcParams = new Vector();

            xmlRpcParams.addElement(exportedClassName);
            xmlRpcParams.addElement(String.valueOf(objPtr));//long
            xmlRpcParams.addElement(readOnly);
            xmlRpcParams.addElement(methodName);

            base64 = Base64.encodeObject(params);
            xmlRpcParams.addElement(base64);

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.invokeMethod", xmlRpcParams);

//                System.out.println("o = xmlRpcClient.execute(RpcHandler.invokeMethod, xmlRpcParams) = " + o);

                base64 = (String) o;
//                o = Base64.decodeToObject(base64, UG.class.getClassLoader());

                o = Base64.decodeToObject(base64, UG.class.getClassLoader());

            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return o;

        } else {
//            System.out.println("UG.invokeMethod() :");
//            System.out.println("  exportedClassName = " + exportedClassName);
//            System.out.println("  objPtr = " + objPtr);
//            System.out.println("  readOnly = " + readOnly);
//            System.out.println("  methodName = " + methodName);
//            System.out.println("  params = " + params);
//
//
//            for (int i = 0; i < params.length; i++) {
//                System.out.println("    params[" + i + "] = " + params[i]);
//
//            }

            Object o = _invokeMethod(exportedClassName, objPtr, readOnly, methodName, params);

//            System.out.println(" -> Object o = _invokeMethod(...) = "+ o);
//            

            return o;
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    Pointer newInstance(long exportedClassPtr, Object[] parameters) {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;
            String base64 = null;
            Pointer p = null;

            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(String.valueOf(exportedClassPtr));

            base64 = Base64.encodeObject(parameters);
            xmlRpcParams.addElement(base64);

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.newInstance", xmlRpcParams);

                base64 = (String) o;
//                o = Base64.decodeToObject(base64, UG.class.getClassLoader());

                o = Base64.decodeToObject(base64, UG.class.getClassLoader());

                if (o instanceof Pointer) {
                    p = (Pointer) o;
                }

            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return p;

        } else {

            return _newInstance(exportedClassPtr, parameters);
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    long getExportedClassPtrByName(String name, boolean classGrp) {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;

            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(name);
            xmlRpcParams.addElement(classGrp);


            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.getExportedClassPtrByName", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return new Long((String) o);
        } else {

            return _getExportedClassPtrByName(name, classGrp);
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    String getDefaultClassNameFromGroup(String grpName) {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;

            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(grpName);

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.getDefaultClassNameFromGroup", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return _getDefaultClassNameFromGroup(grpName);
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    Object invokeFunction(String name, boolean readOnly, Object[] params) {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;

            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(name);
            xmlRpcParams.addElement(readOnly);

            xmlRpcParams.addElement(params);

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.invokeFunction", xmlRpcParams);
                String base64 = (String) o;

//                o = Base64.decodeToObject(base64, UG.class.getClassLoader());

                o = Base64.decodeToObject(base64, UG.class.getClassLoader());

            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return o;

        } else {

            return _invokeFunction(name, readOnly, params);
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    String getSvnRevision() {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.getSvnRevision", voidElement);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return _getSvnRevision();
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    String getDescription() {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.getDescription", voidElement);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return _getDescription();
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    String getAuthors() {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.getAuthors", voidElement);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return _getAuthors();
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    String getCompileDate() {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.getCompileDate", voidElement);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return _getCompileDate();
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    String getUGVersion() {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.getUGVersion", voidElement);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return _getUGVersion();
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    String getBinaryLicense() {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.getBinaryLicense", voidElement);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return _getBinaryLicense();
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    static int ugInit(String[] args) {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;

            ArrayList<Object> xmlRpcParams = new ArrayList<Object>();

            xmlRpcParams.add(Arrays.asList(args));

            ArrayList<Object> xmlRpcParams2 = new ArrayList<Object>();
            xmlRpcParams2.add("Client._ugInit");

            try {

                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                o = xmlRpcClient.execute("RpcHandler.ugInit", xmlRpcParams);

            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            Integer tmp = (Integer) o;
            System.out.println(" RpcHandler.ugInit = " + tmp);

            return tmp;

        } else {

            return _ugInit(args);
        }
    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     *
     * Deallocates specified memory. The destructor of the specified class will
     * be called.
     *
     * @param objPtr object pointer
     * @param exportedClassPtr pointer of the exported class
     *
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    @Deprecated
    static void delete(long objPtr, long exportedClassPtr) {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(String.valueOf(objPtr));
            xmlRpcParams.addElement(String.valueOf(exportedClassPtr));

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                xmlRpcClient.execute("RpcHandler.delete", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            _delete(objPtr, exportedClassPtr);
        }

    }

    /**
     * If UGs RemoteType is CLIENT a remote connection is etablished and the
     * method call is redirected.
     *
     * Else if UGs RemoteType is NOT client ( means SERVER or NONE) the native
     * method is called.
     *
     *
     * Invalidates the specified smart pointer.
     *
     * @param p smart-pointer to invalidate
     *
     * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
     */
    static void invalidate(SmartPointer p) {

        if (remoteType.equals(RemoteType.CLIENT)) {

            Object o = null;
            String base64 = null;

            Vector xmlRpcParams = new Vector();

            base64 = Base64.encodeObject(p);
            xmlRpcParams.addElement(base64);

            try {
                XmlRpcClient xmlRpcClient = JVMmanager.getClient(
                        JVMmanager.getCurrentIP(),
                        JVMmanager.getCurrentPort());

                xmlRpcClient.execute("RpcHandler.invalidate", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            _invalidate(p);
        }

    }
    
    // this method is only for debug issues and should NOT be used later
    public static Object test_debug(String name, Object[] params){
        
        if (remoteType.equals(RemoteType.CLIENT)) {

            System.err.println(" NOT implemented yet test_debug() for RemoteType.CLIENT !!!");

        } else {

            System.out.println("UG.java in test_debug() !RemoteType.CLIENT");
            System.out.println("calling native _test_debug(name= "+name+", params[] )");
           return _test_debug(name, params);
        }
        
        return null;
    }

}
