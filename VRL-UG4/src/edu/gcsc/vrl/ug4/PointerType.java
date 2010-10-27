/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.reflection.RepresentationType;
import eu.mihosoft.vrl.reflection.TypeRepresentationBase;
import groovy.lang.Script;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class PointerType extends TypeRepresentationBase {

    private String className;
    private ArrayList<String> classNames;
    private boolean readOnly;

    public PointerType() {
        setValueName("Pointer");
        setType(Pointer.class);
        addSupportedRepresentationType(RepresentationType.INPUT);
        addSupportedRepresentationType(RepresentationType.OUTPUT);

        add(nameLabel);
    }

    @Override
    public Object getViewValue() {
        Object v = super.getViewValue();

        if (v instanceof Pointer) {
            Pointer p = (Pointer) v;
            v = new Pointer(p.getAddress(), readOnly);
        }

        return v;
    }

    @Override
    public boolean compatible(TypeRepresentationBase tRep) {
        PointerType pT = null;
        if (!(tRep instanceof PointerType)) {
            return false;
        } else {
            pT = (PointerType) tRep;
        }

        return pT.classNames.contains(className)
                && (readOnly == pT.readOnly || readOnly && !pT.readOnly);
    }

    @Override
    protected void evaluationRequest(Script script) {
        Object property = null;

        if (getValueOptions().contains("className")) {
            property = getOptionEvaluator().getProperty("className");

        }

        if (property != null) {
            className = (String) property;
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
}
