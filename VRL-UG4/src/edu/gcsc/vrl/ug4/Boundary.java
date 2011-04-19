/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaClass;
import java.util.ArrayList;
import org.codehaus.groovy.runtime.wrappers.GroovyObjectWrapper;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Boundary extends GroovyObjectSupport{
    private boolean bndBool;
    private double value;

    public Boundary() {
        bndBool = false;
        value = 0;
    }

    public Boundary(boolean bndBool, double value) {
        this.bndBool = bndBool;
        this.value = value;
    }

    public Boundary(ArrayList<Object> data) {
        if (data==null) {
            throw new IllegalArgumentException(
                    "Argument \"null\" not supported!");
        }
        if (data.size()!=2) {
            throw new IllegalArgumentException(
                    "Range mismatch: expected 2, provided:" + data.size());
        }

        if (!(data.get(0) instanceof Boolean)) {
            throw new IllegalArgumentException(
                    "Type mismatch: first entry must be of type boolean!");
        }

        if (!(data.get(1) instanceof Double)
                && !(data.get(1) instanceof Float)
                && !(data.get(1) instanceof Integer)
                && !(data.get(1) instanceof Long)
                && !(data.get(1) instanceof Short)) {
            throw new IllegalArgumentException(
                    "Type mismatch: second entry must be a number!");
        }

        bndBool = (Boolean) data.get(0);
        value = (Double) data.get(1);
    }

    /**
     * @return the bndBool
     */
    public boolean getBndBool() {
        return bndBool;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }
}
