/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2006–2012 by Michael Hoffer
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
package edu.gcsc.vrl.ug.types;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.gcsc.vrl.ug;
//
//import eu.mihosoft.vrl.annotation.MethodInfo;
//import eu.mihosoft.vrl.annotation.ParamInfo;
//import eu.mihosoft.vrl.lang.VLangUtils;
//import eu.mihosoft.vrl.reflection.RepresentationType;
//import eu.mihosoft.vrl.reflection.TypeRepresentationBase;
//import eu.mihosoft.vrl.system.VSysUtil;
//import eu.mihosoft.vrl.visual.ConnectionResult;
//import eu.mihosoft.vrl.visual.ConnectionStatus;
//import groovy.lang.Script;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.ArrayList;
//import javax.swing.JMenuItem;
//import javax.swing.JPopupMenu;
//
///**
// *
// * @author Michael Hoffer <info@michaelhoffer.de>
// */
//public class PointerType extends TypeRepresentationBase {
//
//    private String className;
//    private ArrayList<String> classNames;
//    private boolean readOnly;
//    private static final String UNDEFINED_NAME = "Pointer";
//
//    public PointerType() {
//        setValueName(UNDEFINED_NAME);
//        setType(Pointer.class);
//        addSupportedRepresentationType(RepresentationType.INPUT);
//        addSupportedRepresentationType(RepresentationType.OUTPUT);
//
//        add(nameLabel);
//
//
//        final JPopupMenu menu = new JPopupMenu("Pointer");
//
//        JMenuItem item = new JMenuItem("Copy Param/Method-Info to Clipboard");
//
//        item.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//
//                String infoString = "";
//
//                if (isInput()) {
//                    ArrayList<TypeRepresentationBase> params =
//                            getParentMethod().getParameters();
//
//                    int id = params.indexOf(PointerType.this);
//
//                    ParamInfo info =
//                            getParentMethod().getDescription().
//                            getParamInfos()[id];
//
//                    infoString = "@ParamInfo( style=\""
//                            + VLangUtils.addEscapeCharsToCode(info.style())
//                            + "\", name=\""
//                            + VLangUtils.addEscapeCharsToCode(info.name())
//                            + "\", options=\""
//                            + VLangUtils.addEscapeCharsToCode(info.options())
//                            + "\", nullIsValid=" + info.nullIsValid() + ")";
//                } else {
//                    MethodInfo mInfo = getParentMethod().getDescription().
//                            getMethodInfo();
//
//                    infoString = "@MethodInfo("
////                            + " name=\""
////                            + VLangUtils.addEscapeCharsToCode(mInfo.name())
////                            + "\","
//                            + "valueName=\""
//                            + VLangUtils.addEscapeCharsToCode(mInfo.valueName())
//                            + "\", valueOptions=\""
//                            + VLangUtils.addEscapeCharsToCode(
//                            mInfo.valueOptions())
//                            +"\")";
//                }
//
//                VSysUtil.copyToClipboard(infoString);
//            }
//        });
//
//        menu.add(item);
//
//        addMouseListener(new MouseAdapter() {
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getButton() == MouseEvent.BUTTON3) {
//                    menu.show(PointerType.this, e.getX(), e.getY());
//                }
//            }
//        });
//    }
//
//    @Override
//    public Object getViewValue() {
//        Object v = super.getViewValue();
//
//        if (v instanceof Pointer) {
//            Pointer p = (Pointer) v;
//            p.setConst(readOnly);
//            // important for garbage collection. this will not work if
//            // classname is not defined. for return values this is the case
//            // because the method that creates the object does not know the
//            // class name.
////            p.setClassName(className);
//        }
//
//        return v;
//    }
//
//    @Override
//    public ConnectionResult compatible(TypeRepresentationBase tRep) {
//        PointerType pT = null;
//        if (!(tRep instanceof PointerType)) {
//            return new ConnectionResult(
//                    null, ConnectionStatus.ERROR_VALUE_TYPE_MISSMATCH);
//        } else {
//            pT = (PointerType) tRep;
//        }
//
//        boolean result = pT.classNames == null || classNames == null
//                || className == null
//                || pT.classNames.contains(className)
//                && (readOnly == pT.readOnly || readOnly && !pT.readOnly);
//
//        if (result) {
//            return new ConnectionResult(
//                    null, ConnectionStatus.VALID);
//        }
//
//        return new ConnectionResult(
//                null, ConnectionStatus.ERROR_VALUE_TYPE_MISSMATCH);
//    }
//
//    @Override
//    protected void evaluationRequest(Script script) {
//        Object property = null;
//
//        if (getValueOptions().contains("className")) {
//            property = getOptionEvaluator().getProperty("className");
//
//        }
//
//        if (property != null) {
//            className = (String) property;
//            if (getValueName().equals(UNDEFINED_NAME)) {
//                setValueName(className);
//            }
//        }
//
//        property = null;
//
//        if (getValueOptions().contains("classNames")) {
//            property = getOptionEvaluator().getProperty("classNames");
//
//        }
//
//        if (property != null) {
//            classNames = (ArrayList<String>) property;
//        }
//
//        property = null;
//
//        if (getValueOptions().contains("readOnly")) {
//            property = getOptionEvaluator().getProperty("readOnly");
//
//        }
//
//        if (property != null) {
//            readOnly = (Boolean) property;
//        }
//    }
//
//    @Override
//    public boolean noSerialization() {
//        return true;
//    }
//}
