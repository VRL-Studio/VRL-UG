/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug;

import groovy.lang.GroovyObjectSupport;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Cond extends GroovyObjectSupport{
    private boolean condBool;
    private double value;

    public Cond() {
        condBool = false;
        value = 0;
    }

    public Cond(boolean bndBool, double value) {
        this.condBool = bndBool;
        this.value = value;
    }

    public Cond(ArrayList<Object> data) {
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

        condBool = (Boolean) data.get(0);
        value = (Double) data.get(1);
    }

    /**
     * @return the bndBool
     */
    public boolean getCondBool() {
        return condBool;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }
}
