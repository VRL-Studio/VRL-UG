/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UGObjectUtil {

    public static boolean isGroupRoot(Class<?> cls) {
        return cls.getAnnotation(UGObjectInfo.class) != null
                && cls.getAnnotation(UGObjectInfo.class).groupRoot();
    }
    
    public static boolean isGroupChild(Class<?> cls) {
        return cls.getAnnotation(UGObjectInfo.class) != null
                && cls.getAnnotation(UGObjectInfo.class).groupChild();
    }

    public static boolean isWrapperClass(Class<?> cls) {
        return cls.getAnnotation(UGObjectInfo.class) != null
                && !cls.getAnnotation(UGObjectInfo.class).instantiable();
    }

    public static boolean isConstClass(Class<?> cls) {
        return cls.getAnnotation(UGObjectInfo.class) != null
                && cls.getAnnotation(UGObjectInfo.class).constClass();
    }
    
    public static boolean isUGAPIClass(Class<?> cls) {
        return ((Object)cls instanceof UGObject);
    }
}
