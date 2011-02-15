/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.lang.VLangUtils;
import eu.mihosoft.vrl.reflection.RepresentationType;
import eu.mihosoft.vrl.reflection.TypeRepresentationBase;
import eu.mihosoft.vrl.system.VSysUtil;
import eu.mihosoft.vrl.visual.ConnectionResult;
import eu.mihosoft.vrl.visual.ConnectionStatus;
import groovy.lang.Script;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class PointerType extends TypeRepresentationBase {

    private String className;
    private ArrayList<String> classNames;
    private boolean readOnly;
    private static final String UNDEFINED_NAME = "Pointer";

    public PointerType() {
        setValueName(UNDEFINED_NAME);
        setType(Pointer.class);
        addSupportedRepresentationType(RepresentationType.INPUT);
        addSupportedRepresentationType(RepresentationType.OUTPUT);

        add(nameLabel);


        final JPopupMenu menu = new JPopupMenu("Pointer");

        JMenuItem item = new JMenuItem("Copy Param/Method-Info to Clipboard");

        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                String infoString = "";

                if (isInput()) {
                    ArrayList<TypeRepresentationBase> params =
                            getParentMethod().getParameters();

                    int id = params.indexOf(PointerType.this);

                    ParamInfo info =
                            getParentMethod().getDescription().
                            getParamInfos()[id];

                    infoString = "@ParamInfo( style=\""
                            + VLangUtils.addEscapeCharsToCode(info.style())
                            + "\", name=\""
                            + VLangUtils.addEscapeCharsToCode(info.name())
                            + "\", options=\""
                            + VLangUtils.addEscapeCharsToCode(info.options())
                            + "\", nullIsValid=" + info.nullIsValid() + ")";
                } else {
                    MethodInfo mInfo = getParentMethod().getDescription().
                            getMethodInfo();

                    infoString = "@MethodInfo("
//                            + " name=\""
//                            + VLangUtils.addEscapeCharsToCode(mInfo.name())
//                            + "\","
                            + "valueName=\""
                            + VLangUtils.addEscapeCharsToCode(mInfo.valueName())
                            + "\", valueOptions=\""
                            + VLangUtils.addEscapeCharsToCode(
                            mInfo.valueOptions())
                            +"\")";
                }

                VSysUtil.copyToClipboard(infoString);
            }
        });

        menu.add(item);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    menu.show(PointerType.this, e.getX(), e.getY());
                }
            }
        });
    }

    @Override
    public Object getViewValue() {
        Object v = super.getViewValue();

        if (v instanceof Pointer) {
            Pointer p = (Pointer) v;
            p.setConst(readOnly);
            // important for garbage collection. this will not work if
            // classname is not defined. for return values this is the case
            // because the method that creates the object does not know the
            // class name.
//            p.setClassName(className);
        }

        return v;
    }

    @Override
    public ConnectionResult compatible(TypeRepresentationBase tRep) {
        PointerType pT = null;
        if (!(tRep instanceof PointerType)) {
            return new ConnectionResult(
                    null, ConnectionStatus.ERROR_VALUE_TYPE_MISSMATCH);
        } else {
            pT = (PointerType) tRep;
        }

        boolean result = pT.classNames == null || classNames == null
                || className == null
                || pT.classNames.contains(className)
                && (readOnly == pT.readOnly || readOnly && !pT.readOnly);

        if (result) {
            return new ConnectionResult(
                    null, ConnectionStatus.VALID);
        }

        return new ConnectionResult(
                null, ConnectionStatus.ERROR_VALUE_TYPE_MISSMATCH);
    }

    @Override
    protected void evaluationRequest(Script script) {
        Object property = null;

        if (getValueOptions().contains("className")) {
            property = getOptionEvaluator().getProperty("className");

        }

        if (property != null) {
            className = (String) property;
            if (getValueName().equals(UNDEFINED_NAME)) {
                setValueName(className);
            }
        }

        property = null;

        if (getValueOptions().contains("classNames")) {
            property = getOptionEvaluator().getProperty("classNames");

        }

        if (property != null) {
            classNames = (ArrayList<String>) property;
        }

        property = null;

        if (getValueOptions().contains("readOnly")) {
            property = getOptionEvaluator().getProperty("readOnly");

        }

        if (property != null) {
            readOnly = (Boolean) property;
        }
    }

    @Override
    public boolean noSerialization() {
        return true;
    }
}
