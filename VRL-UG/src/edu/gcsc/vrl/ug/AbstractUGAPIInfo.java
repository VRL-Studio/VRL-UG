/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class AbstractUGAPIInfo {
    private ArrayList<String> classNames;

    public AbstractUGAPIInfo() {
    }

    public AbstractUGAPIInfo(ArrayList<String> classNames) {
        this.classNames = classNames;
    }
    
    

    /**
     * @return the classNames
     */
    public ArrayList<String> getClassNames() {
        return classNames;
    }

    /**
     * @param classNames the classNames to set
     */
    public void setClassNames(ArrayList<String> classNames) {
        this.classNames = classNames;
    }
}
