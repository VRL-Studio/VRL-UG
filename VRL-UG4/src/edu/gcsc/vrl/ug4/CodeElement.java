/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.CodeBuilder;

/**
 * Code element.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public interface CodeElement {
    /**
     * Builds this code element.
     * @param builder code builder
     * @return code builder
     */
    public CodeBuilder build(CodeBuilder builder);
    
    @Override
    public String toString();
}
