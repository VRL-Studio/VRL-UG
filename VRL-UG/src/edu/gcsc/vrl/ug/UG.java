/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.IOUtil;
import eu.mihosoft.vrl.io.VJarUtil;
import eu.mihosoft.vrl.io.VPropertyFolderManager;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.system.*;
import eu.mihosoft.vrl.visual.MessageType;
import eu.mihosoft.vrl.visual.SplashScreenGenerator;
import java.beans.XMLDecoder;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

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

                String libName = f.getName();

                if (!VSysUtil.isWindows()) {
                    libName = libName.replaceFirst("lib", "");
                }

                libName = libName.substring(0,
                        libName.length() - dylibEnding.length());

                if (!loadedLibraries.contains(libName)) {
                    System.out.print(" --> " + f.getName());
                    try {
                        System.loadLibrary(libName);
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
     * Defines all native UG classes that are exported via the UG registry,
     * i.e., the equivalent Java wrapper classes.
     *
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

        if (loadNativeLib) {

            File libFolder = new File(
                    getNativeLibFolder() + "/eu/mihosoft/vrl/natives/"
                    + VSysUtil.getPlatformSpecificPath());

            loadNativeLibrariesInFolder(libFolder, false);

            libLoaded = true;
        }

        try {

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
                            VPropertyFolderManager.getPluginUpdatesFolder().
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
     * @return the instance of this singleton
     */
    public static synchronized UG getInstance(VisualCanvas canvas) {
        if (ugInstance == null) {

            ugInstance = new UG();

            if (canvas != null) {
                ugInstance.setMainCanvas(canvas);
            }

        } else if (canvas != null) {
            ugInstance.setMainCanvas(canvas);
        }

        return ugInstance;
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
        return getInstance(null);
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
    private StringBuilder getMessages() {
        return messages;
    }

    public static void addMessage(String s) {
        messages.append(s);

        if (messages.length() > logMaxChars) {
            messages.delete(0, logMaxChars);
        }
    }

    public void clearMessages() {
        if (messages.length() > 0) {
            messages.delete(0, messages.length());
            //messages = new StringBuilder();
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
                    Thread.sleep(logRefreshInterval);
                } catch (InterruptedException ex) {
                    //
                }

                if (getMessages().length() > 0) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            if (mainCanvas != null && messages.length() > 0) {
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

    // ********************************************
    // ************** NATIVE METHODS **************
    // ********************************************
    final native NativeAPIInfo convertRegistryInfo();

    native Object invokeMethod(
            String exportedClassName,
            long objPtr, boolean readOnly,
            String methodName, Object[] params);

//    native long newInstance(long exportedClassPtr, Object[] parameters);
    native Pointer newInstance(long exportedClassPtr, Object[] parameters);

    native long getExportedClassPtrByName(String name, boolean classGrp);

    native String getDefaultClassNameFromGroup(String grpName);

    native Object invokeFunction(String name,
            boolean readOnly, Object[] params);

    static native int ugInit(String[] args);

    native String getSvnRevision();

    native String getDescription();

    native String getAuthors();

    native String getCompileDate();
}
