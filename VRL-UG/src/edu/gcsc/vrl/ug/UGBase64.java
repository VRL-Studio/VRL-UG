/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.Base64;

/**
 * Modified copy of Base64 of VRL project.
 * This version was created to fix a ClassNotFoundExceptions on both sides
 * (client & server) via remote communication, at decode part of file transfer.
 * 
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 *
 * ORIGINAL:
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UGBase64 {
    /**
     * Attempts to decode Base64 data and deserialize a Java Object within.
     * Returns <tt>null</tt> if there was an error.
     *
     * @param encodedObject The Base64 data to decode
     * @return The decoded and deserialized object
     */
    public static Object decodeToObject(String encodedObject) {
        
        // Decode and gunzip if necessary
        byte[] objBytes = Base64.decode(encodedObject);

        java.io.ByteArrayInputStream bais = null;
        java.io.ObjectInputStream ois = null;
        Object obj = null;

        try {
            bais = new java.io.ByteArrayInputStream(objBytes);
            
                ois = new UGObjectInputStream(bais, UGBase64.class.getClassLoader());

            obj = ois.readObject();
        } // end try
        catch (java.io.IOException e) {
            e.printStackTrace();
            obj = null;
        } // end catch
        catch (java.lang.ClassNotFoundException e) {
            e.printStackTrace();
            obj = null;
        } // end catch
        finally {
            try {
                bais.close();
            } catch (Exception e) {
            }
            try {
                ois.close();
            } catch (Exception e) {
            }
        }   // end finally

        return obj;
    }   // end decodeObject
}
