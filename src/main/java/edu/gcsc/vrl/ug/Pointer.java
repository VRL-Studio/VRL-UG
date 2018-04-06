/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2012–2018 Goethe Universität Frankfurt am Main, Germany
 * 
 * This file is part of Visual Reflection Library (VRL).
 *
 * VRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/VRL/src/eu/mihosoft/vrl/resources/license/lgplv3.txt
 *
 * VRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of VRL includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of VRL. Neither the VRL Canvas attribution icon nor any
 * copyright statement/attribution may be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do three things regarding copyright
 * notice and author attribution.
 *
 * First, the following text must be displayed on the Canvas:
 * "based on VRL source code". In this case the VRL canvas icon must be removed.
 * 
 * Second, the copyright notice must remain. It must be reproduced in any
 * program that uses VRL.
 *
 * Third, add an additional notice, stating that you modified VRL. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "VRL source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * M. Hoffer, C.Poliwoda, G.Wittum. Visual Reflection Library -
 * A Framework for Declarative GUI Programming on the Java Platform.
 * Computing and Visualization in Science, 2011, in press.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import java.io.Serializable;

/**
 * This class wrapps native C/C++ pointers with additional type info
 * (class name) that can be used by UGs registry to call native
 * functions or methods.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Pointer implements Serializable{
    
    
    private static final long serialVersionUID = 1L;

    private boolean readOnly;
    private long address;
    private String className;
    
    public static final long NULL = 0;

    /**
     * Constructor.
     * @param className class name
     * @param address address (native pointer)
     * @param readOnly indicates whether this shall be a const pointer
     */
    public Pointer(String className, long address,
            boolean readOnly) {
        this.address = address;
        this.readOnly = readOnly;
        this.className = className;
        init();
    }

    /**
     * Constructor.
     * @param address address (native pointer)
     * @param readOnly indicates whether this shall be a const pointer
     */
    public Pointer(long address, boolean readOnly) {
        this.address = address;
        this.readOnly = readOnly;

        init();
    }
    
    /**
     * Casts the specified pointer to a smart pointer of the specified type.
     * 
     * <b>Note:</b> use with care! This performs a native cast that may lead to
     * memory errors that cannot be handled by the JVM. Both objects share the
     * same native pointer instance.
     *
     * @param newClassName new type
     * @return smartpointer
     */
    Pointer cast(String newClassName) {
        return cast(newClassName, isConst());
    }
    
    /**
     * Casts the specified pointer to a smart pointer of the specified type.
     * 
     * <b>Note:</b> use with care! This performs a native cast that may lead to
     * memory errors that cannot be handled by the JVM. Both objects share the
     * same native pointer instance. Also note that casting from const to
     * non-const is not supported. In this case the resulting pointer will be
     * const.
     * 
     *
     * @param newClassName new type
     * @param asConst defines whether to cast to const
     * @return smartpointer
     */
    public Pointer cast(String newClassName, boolean asConst) {
        return new Pointer(newClassName,
                this.getAddress(), this.isConst() || asConst);
    }

    /**
     * Initializes this pointer.
     */
    private void init() {
//        MemoryManager.retain(this);
    }

    /**
     * Returns the pointer address.
     * @return the pointer address
     */
    public long getAddress() {
        return address;
    }

    /**
     * Indicates whether this pointer is const.
     * @return <code>true</code> if this pointer is const;
     *         <code>false</code> otherwise
     */
    public boolean isConst() {
        return readOnly;
    }

    /**
     * Defines whether this pointer is const.
     * @param b state to set
     */
    public void setConst(boolean b) {
        readOnly = b;
    }

    /**
     * Returns the class name
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Indicates whether the class name of the type is locked.
     * @return <code>true</code> if the class name of the type is locked;
     *         <code>false</code> otherwise
     */
    public boolean isClsNameLocked() {
        return className != null;
    }

    /**
     * <p>
     * Defines the class name.
     * </p>
     * <p>
     * <b>Note:</b> This method only has an effect if
     * {@link #isClsNameLocked() } returns false
     * </p>
     * @param className the class name to set
     */
    public void setClassName(String className) {
        if (!isClsNameLocked()) {
            this.className = className;
        }
    }

    @Override
    public String toString() {
        return "Pointer: class=" + className
                + ", address=" + address
                + ", const=" + isConst();
    }

    /**
     * Indicates whether the specified object is an ug pointer.
     * @param o object to check
     * @return <code>true</code> if the specified object is an ug pointer;
     *         <code>false</code> otherwise
     */
    public static boolean isInstance(Object o) {
        boolean result = Pointer.class.isInstance(o);
        return result;
    }
}
