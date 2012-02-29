/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Modified copy of VObjectInputStream of VRL project.
 * This version was created to fix a ClassNotFoundExceptions on both sides
 * (client & server) via remote communication.
 * 
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 *
 * ORIGINAL:
 * VRL uses this input stream to ensure that classes defined as abstract code
 * can be loaded. Otherwise "class not found" exceptions will be thrown. There
 * is usally no need to use this input stream outside of VRL core classes.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UGObjectInputStream extends ObjectInputStream {

    private ClassLoader classLoader;

    /**
     * Constructor.
     * @param in the input stream to use
     * @param classLoader the class loader to use
     * @throws java.io.IOException
     */
    public UGObjectInputStream(
            InputStream in, ClassLoader classLoader) throws IOException {
        super(in);
        this.classLoader = classLoader;
    }

    @Override
    protected Class<?> resolveClass(
            ObjectStreamClass desc) throws ClassNotFoundException {        
        return Class.forName(desc.getName(), false, classLoader);
//        return classLoader.loadClass(desc.getName());
    }
}
