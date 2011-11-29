/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.VJarUtil;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.Constants;
import eu.mihosoft.vrl.system.VRL;
import eu.mihosoft.vrl.system.VSysUtil;
import eu.mihosoft.vrl.system.VTerminalUtil;
import eu.mihosoft.vrl.visual.MessageType;
import eu.mihosoft.vrl.visual.SplashScreenGenerator;
import java.beans.XMLDecoder;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

/**
 * <p>
 * UG class represents UG and its scripting functionality. It allows only one
 * instance which can be obtained via
 * {@link #getInstance(eu.mihosoft.vrl.reflection.VisualCanvas) }.
 * </p>
 * <p>
 * <b>Note:</b> this singleton must not be loaded by more than one
 * classloader per JVM! Although this is no problem for Java classes, it
 * does not work for native libraries. Unfortunately, we cannot provide an
 * acceptable workaround. Thus, it is recommended to use this class from
 * a valid VRL plugin only. The VRL plugin system is aware of this problem
 * and handles it correctly.
 * </p>
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UG {

    /**
     * native classes
     */
    private static Class<?>[] nativeClasses;
    /**
     * ug messages
     */
    private static StringBuffer messages = new StringBuffer();
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
    private static RemoteType remoteType = null;

    /**
     * 
     * @param remoteType 
     */
    private static void setRemoteType(RemoteType remoteType) {
        UG.remoteType = remoteType;
    }

    /**
     *
     * @return 
     */
    private static RemoteType getRemoteType() {

        return UG.remoteType;
    }
    
    private static int defaultPort = 1099;
    private static String defaultHost = "localhost";
    private static XmlRpcClient xmlRpcClient;
    private static XmlRpcServer xmlRpcServer;
    private static WebServer webServer;
    /**
     * VRL canvas used to visualize ug classes
     */
    private VisualCanvas mainCanvas;
    /**
     * Messaging thread which displays messages generated from native UG
     * methods.
     */
    private MessageThread messagingThread;
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
     * temp folder conaining native libs 
     * (must be set from ug plugin, not from api)
     */
    private static File nativeLibFolder;
    /**
     * api class
     */
    private Class<?> api;

    /**
     * Returns the api description
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

//    /**
//     * Returns all native UG classes that are exported via the UG registry,
//     * i.e., the equivalent Java wrapper classes.
//     * @return the nativeClasses
//     */
//    public static Class<?>[] getNativeClasses() {
//        
//        return nativeClasses;
//    }
//
    /**
     * Defines all native UG classes that are exported via the UG registry,
     * i.e., the equivalent Java wrapper classes.
     * @param aNativeClasses the nativeClasses to set
     */
//    static void setNativeClasses(Class<?>[] nativeClasses) {
//        UG.nativeClasses = nativeClasses;
//    }
    public static void connectToNativeUG(boolean loadNativeLib) {
        // initialize native ug libraries
        String pluginPath = getNativeLibFolder() + "/eu/mihosoft/vrl/natives/"
                + VSysUtil.getPlatformSpecificPath() + "plugins".replace("/", File.separator);

        String[] args = {pluginPath};

        System.out.println(" --> UG: connecting to native ug.");

        if (loadNativeLib) { // check if remote, e.g. loadNative && !remote
//        if (loadNativeLib && !remote) {
            System.loadLibrary("ug4");
            libLoaded = true;
        } 
//        // new experimental Code
//        else if (loadNativeLib && remote) {
//
////            System.load("PATH TO REMOTE UG ?!?!");
//            System.loadLibrary("ug4");//??
//            libLoaded = true;
//        }

        try {

            _ugInit(args);  //native
            
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        
////        // check if remote, e.g. loadNative && !remote
////        if (loadNativeLib && !remote &&
////                ( remoteType.equals(RemoteType.SERVER) || 
////                remoteType.equals(RemoteType.NONE) )) { 
////            
////            System.loadLibrary("ug4");
////            libLoaded = true;
////        } 
////        // new experimental Code
////        else if (loadNativeLib && remote &&
////                remoteType.equals(RemoteType.CLIENT)) {
////
//////            System.load("PATH TO REMOTE UG ?!?!");
////            System.loadLibrary("ug4");//??
////            
//////            xmlRpcClient..... 
////            
////            libLoaded = true;
////        }
////
////        try {
////
////            _ugInit(args);  //native
////            
////        } catch (Exception ex) {
////            ex.printStackTrace(System.err);
////        }
            
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

        // load api if compatible; rebuild otherwise
        try {
            Class<?> cls = findCompatibleAPI(ugInstance);

            api = cls;

            if (cls == null) {

                // load native library and connect to ug lib to generate api
                connectToNativeUG(true);

                NativeAPIInfo nativeAPI = _convertRegistryInfo();
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
                            nativeAPI).getAllCodes(), Constants.PLUGIN_DIR);

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

    private UG(RemoteType remoteType) {
      
        
        if (remoteType.equals(RemoteType.CLIENT)) {

            //execute(java -jar params)
          createXmlRpcClient(defaultHost, defaultPort);

        } else if (remoteType.equals(RemoteType.SERVER)) {

           createXmlRpcServer(defaultPort);
        }
        
          
//         // we must set the singleton instance to prevent
//        // calling multiple constructors.
//        // doing this in the corresponding getter methods does not work anymore
//        // as we need the instance for searching a compiled UG-API.
        ugInstance = this;
        

        // load api if compatible; rebuild otherwise
        try {
            Class<?> cls = findCompatibleAPI(ugInstance);

            api = cls;

            if (cls == null) {

                // load native library and connect to ug lib to generate api
                connectToNativeUG(true);

                NativeAPIInfo nativeAPI = _convertRegistryInfo();
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
                            nativeAPI).getAllCodes(), Constants.PLUGIN_DIR);

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

    /**
     * Tries to find a compatible api in the class path. Compatibility is
     * defined as equal svn revision and equal compile date.
     * @param ug ug instance used to check for compatibility.
     * @return a compatible api class object or <code>null</code> if no such
     * api could be found
     */
    private static Class<?> findCompatibleAPI(UG ug) {
        try {
            ClassLoader cl = null;
            try {
                cl = new URLClassLoader(
                        new URL[]{new File(Constants.PLUGIN_DIR
                            + "/" + Compiler.API_JAR_NAME).toURI().toURL()});
            } catch (MalformedURLException ex) {
                Logger.getLogger(VRL.class.getName()).
                        log(Level.SEVERE, null, ex);
            }

            if (cl == null) {
                System.err.println("Classloader not found: This should never"
                        + " happen. Please "
                        + Constants.WRITE_VRL_BUG_REPORT_PLAIN);

                return null;
            }

            Class<?> cls = cl.loadClass("edu.gcsc.vrl.ug.UGAPI");

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
     * @param cls api class
     * @return the api classes
     */
    public static Class<?>[] getAPiClasses(Class<?> cls) {
        try {
            ClassLoader cl = cls.getClassLoader();

            URL url = cls.getResource(
                    "/edu/gcsc/vrl/ug/UG_INFO.XML");

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
                        "edu.gcsc.vrl.ug."
                        + clsName);
            }

            return result;
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }

        return new Class<?>[0];
    }

    /**
     * <p>
     * Returns the instance of this singleton. If it does not exist it will be
     * created.
     * <p>
     * <p>
     * <b>Note:</b> this singleton must not be loaded by more than one
     * classloader per JVM! Although this is no problem for Java classes it
     * does not work for native libraries. Unfortunately we cannot provide an
     * acceptable workaround. Thus, it is recommended to use this method from
     * a valid VRL plugin only. The VRL plugin system is aware of this problem
     * and handles it correctly.
     * </p>
     * @param canvas VRL canvas that shall be used to visualize ug classes
     * @param remoteType declares if remote communication is activated or not and
     *        how UG should act as Server or Client
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
    
//    /**
//     * <p>
//     * Returns the instance of this singleton. If it does not exist it will be
//     * created.
//     * <p>
//     * <p>
//     * <b>Note:</b> this singleton must not be loaded by more than one
//     * classloader per JVM! Although this is no problem for Java classes it
//     * does not work for native libraries. Unfortunately we cannot provide an
//     * acceptable workaround. Thus, it is recommended to use this method from
//     * a valid VRL plugin only. The VRL plugin system is aware of this problem
//     * and handles it correctly.
//     * </p>
//     * @param canvas VRL canvas that shall be used to visualize ug classes
//     * @return the instance of this singleton
//     */
//    public static synchronized UG getInstance(VisualCanvas canvas) {
//        if (ugInstance == null) {
//
//            ugInstance = new UG();
//
//            if (canvas != null) {
//                ugInstance.setMainCanvas(canvas);
//            }
//
//        } else if (canvas != null) {
//            ugInstance.setMainCanvas(canvas);
//        }
//
//        return ugInstance;
//    }

    /**
     * <p>
     * Returns the instance of this singleton.
     * </p>
     * <p>
     * <b>Note:</b>If message
     * logging shall be used, please assign a visible canvas. For this
     * {@link #getInstance(eu.mihosoft.vrl.reflection.VisualCanvas)  }
     * can be used.
     * </p>
     * <p>
     * <b>Note:</b> this singleton must not be loaded by more than one
     * classloader per JVM if native libs have been loaded! Although this is
     * no problem for Java classes, it
     * does not work for native libraries. Unfortunately, we cannot provide an
     * acceptable workaround. Thus, it is recommended to use this method from
     * a valid VRL plugin only. The VRL plugin system is aware of this problem
     * and handles it correctly.
     * </p>
     */
    public static UG getInstance() {
        return getInstance(null,RemoteType.NONE);
    }

    /**
     * Returns the VRL canvas that is used to visualize the UG classes.
     * @return the VRL canvas that is used to visualize the UG classes
     */
    public VisualCanvas getMainCanvas() {
        return mainCanvas;
    }

    /**
     * <p>
     * The VRL canvas that shall be used to visualize the UG classes
     * </p>
     * <p>
     * <b>Notice:</b> as a side effect this method starts UG message logging
     * </p>
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
    public void startLogging() {
        stopLogging();
        messagingThread = new MessageThread();
        messagingThread.start();
    }

    /**
     * Stops UG message logging.
     */
    public void stopLogging() {
        if (messagingThread != null) {
            messagingThread.stopLogging();
        }
    }

    /**
     * @return the messages
     */
    private StringBuffer getMessages() {
        return messages;
    }

    public static void addMessage(String s) {
        messages.append(s);

        if (messages.length() > 1000000) {
            messages.delete(0, 1000000);
        }
    }

    public void clearMessages() {
        if (messages.length() > 0) {
            messages.delete(0, messages.length());
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

    class MessageThread extends Thread {

        private boolean logging = true;

        public MessageThread() {
            //
        }

        @Override
        public void run() {

            while (logging) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    //
                }

                if (getMessages().length() > 0) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            if (mainCanvas != null) {
                                mainCanvas.getMessageBox().addMessageAsLog(
                                        "UG-Output:",
                                        "<pre>" + messages + "</pre>",
                                        MessageType.INFO);
                                clearMessages();
                            }
                        }
                    });
                }
            }
        }

        /**
         * @param logging the logging to set
         */
        public void stopLogging() {
            this.logging = false;
        }
    }

    /**
     * 
     * @return true if xmlRpcServer could be created and webServer started
     */
    private static boolean createXmlRpcServer(int port) {
        boolean result = false;


        PropertyHandlerMapping mapping = new PropertyHandlerMapping();

        try {
            mapping.addHandler("NativeRpcHandler", NativeRpcHandler.class);
        } catch (XmlRpcException ex) {
            Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
        }

        webServer = new WebServer(port);

//        //allow only connection with listed clients
//        webserver.setParanoid(true);
//        webserver.acceptClient("141.2.38.37");
//        webserver.acceptClient("141.2.38.91");
//        //end allow list

        XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();

        config.setEnabledForExtensions(true);

        xmlRpcServer = webServer.getXmlRpcServer();


        xmlRpcServer.setConfig(config);
        xmlRpcServer.setHandlerMapping(mapping);

        try {
            webServer.start();

        } catch (IOException ex) {
            Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    /**
     * 
     * @param host localhost or ip like e.g. 141.2.22.123
     * @param port
     * 
     * @return true if client could be created
     */
    private static boolean createXmlRpcClient(String host, int port) {
        boolean result = false;


        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

        try {
            config.setServerURL(new URL("http://" + host + ":" + port));

        } catch (MalformedURLException ex) {
            Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
        }

        config.setEnabledForExtensions(true);

        xmlRpcClient = new XmlRpcClient();
        xmlRpcClient.setConfig(config);

        return result;
    }

    // ********************************************
    // ************** NATIVE METHODS **************
    // ********************************************
    final native NativeAPIInfo convertRegistryInfo();

    native Object invokeMethod(
            String exportedClassName,
            long objPtr, boolean readOnly,
            String methodName, Object[] params);

    native long newInstance(long exportedClassPtr, Object[] parameters);

    native long getExportedClassPtrByName(String name, boolean classGrp);

    native String getDefaultClassNameFromGroup(String grpName);

    native Object invokeFunction(String name,
            boolean readOnly, Object[] params);

    native String getSvnRevision();

    native String getDescription();

    native String getAuthors();

    native String getCompileDate();

    static native int ugInit(String[] args);

    /**
     * Deallocates specified memory. The destructor of the specified class
     * will be called.
     * @param objPtr object pointer
     * @param exportedClassPtr pointer of the exported class
     */
    @Deprecated
    native static void delete(long objPtr, long exportedClassPtr);

    /**
     * Invalidates the specified smart pointer.
     * @param p smart-pointer to invalidate
     */
    native static void invalidate(SmartPointer p);
    //
    //
    /**
     * NOTICE: 
     * 
     * ALL METHODS need to RETURN one of the following types:
     * 
     * XML-RPC type      Simplest Java type     More complex Java type
     * 
     * i4                int                    java.lang.Integer
     * int               int                    java.lang.Integer
     * boolean           boolean                java.lang.Boolean
     * string            java.lang.String       java.lang.String
     * double            double                 java.lang.Double
     * 
     * dateTime.iso8601  java.util.Date         java.util.Date
     * struct            java.util.Hashtable    java.util.Hashtable
     * array             java.util.Vector       java.util.Vector
     * base64            byte[]                 byte[]
     * 
     * nil (extension)   null                   null
     *
     * ATTENTION: void is NOT valid !!!
     * 
     */
    // ******************************************************
    // ************** REMOTE or NATIVE METHODS **************
    // ******************************************************
    //
    //
    //needed for void parammeters
    private static Vector nullVector = new Vector();

    //
    // Remenber all _functions() are native !!!
    //
    final NativeAPIInfo _convertRegistryInfo() {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Object o = null;

            try {
                o = xmlRpcClient.execute("UG.convertRegistryInfo", nullVector);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            //@TODO String base64 remote transfer 
            //      and decode here to NativeAPIInfo !!!!!

            return (NativeAPIInfo) o;

        } else {

            return convertRegistryInfo();
        }
    }

    Object _invokeMethod(String exportedClassName, long objPtr, boolean readOnly,
            String methodName, Object[] params) {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Object o = null;

            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(exportedClassName);
            xmlRpcParams.addElement(String.valueOf(objPtr));
            xmlRpcParams.addElement(String.valueOf(objPtr));
            xmlRpcParams.addElement(String.valueOf(readOnly));
            xmlRpcParams.addElement(methodName);

            for (Object op : params) {
                xmlRpcParams.addElement(op);
            }


            try {
                o = xmlRpcClient.execute("UG.invokeMethod", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return o;

        } else {

            return invokeMethod(exportedClassName, objPtr, readOnly, methodName, params);
        }
    }

    long _newInstance(long exportedClassPtr, Object[] parameters) {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Object o = null;

            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(String.valueOf(exportedClassPtr));

            for (Object op : parameters) {
                xmlRpcParams.addElement(op);
            }

            try {
                o = xmlRpcClient.execute("UG.newInstance", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            //@TODO String base64 remote transfer 
            //      and decode here to long !!!!!

            return (Long) o;

        } else {

            return newInstance(exportedClassPtr, parameters);
        }
    }

    long _getExportedClassPtrByName(String name, boolean classGrp) {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Object o = null;

            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(name);
            xmlRpcParams.addElement(String.valueOf(classGrp));


            try {
                o = xmlRpcClient.execute("UG.getExportedClassPtrByName", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            //@TODO String base64 remote transfer 
            //      and decode here to long !!!!!

            return (Long) o;
        } else {

            return getExportedClassPtrByName(name, classGrp);
        }
    }

    String _getDefaultClassNameFromGroup(String grpName) {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Object o = null;

            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(grpName);

            try {
                o = xmlRpcClient.execute("UG.getDefaultClassNameFromGroup", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return getDefaultClassNameFromGroup(grpName);
        }
    }

    Object _invokeFunction(String name, boolean readOnly, Object[] params) {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Object o = null;

            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(name);
            xmlRpcParams.addElement(String.valueOf(readOnly));

            for (Object op : params) {
                xmlRpcParams.addElement(op);
            }

            try {
                o = xmlRpcClient.execute("UG.invokeFunction", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return o;

        } else {

            return invokeFunction(name, readOnly, params);
        }
    }

    String _getSvnRevision() {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Object o = null;

            try {
                o = xmlRpcClient.execute("UG.getSvnRevision", nullVector);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return getSvnRevision();
        }
    }

    String _getDescription() {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Object o = null;

            try {
                o = xmlRpcClient.execute("UG.getDescription", nullVector);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return getDescription();
        }
    }

    String _getAuthors() {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Object o = null;

            try {
                o = xmlRpcClient.execute("UG.getAuthors", nullVector);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return getAuthors();
        }
    }

    String _getCompileDate() {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Object o = null;

            try {
                o = xmlRpcClient.execute("UG.getCompileDate", nullVector);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            return (String) o;

        } else {

            return getCompileDate();
        }
    }

    static int _ugInit(String[] args) {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Object o = null;

            Vector xmlRpcParams = new Vector();

            for (Object op : args) {
                xmlRpcParams.addElement(op);
            }

            try {
                o = xmlRpcClient.execute("UG.ugInit", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

            //@TODO String base64 remote transfer 
            //      and decode here to int !!!!!


            return (Integer) o;

        } else {

            return ugInit(args);
        }
    }

    /**
     * Deallocates specified memory. The destructor of the specified class
     * will be called.
     * @param objPtr object pointer
     * @param exportedClassPtr pointer of the exported class
     */
    @Deprecated
    static void _delete(long objPtr, long exportedClassPtr) {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(String.valueOf(objPtr));
            xmlRpcParams.addElement(String.valueOf(exportedClassPtr));

            try {
                xmlRpcClient.execute("UG.delete", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            delete(objPtr, exportedClassPtr);
        }

    }

    /**
     * Invalidates the specified smart pointer.
     * @param p smart-pointer to invalidate
     */
    static void _invalidate(SmartPointer p) {

        if (remoteType.equals(RemoteType.NONE)) {
            
            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(p);

            try {
                xmlRpcClient.execute("UG.invalidate", xmlRpcParams);
            } catch (XmlRpcException ex) {
                Logger.getLogger(UG.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            invalidate(p);
        }

    }
}
