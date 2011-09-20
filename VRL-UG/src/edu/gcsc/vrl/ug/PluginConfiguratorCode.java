/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class PluginConfiguratorCode implements CodeElement{

    public CodeBuilder build(CodeBuilder builder) {
        return builder.addLine(
                "public class APIPluginConfigurator extends APIPluginConfiguratorImpl implements eu.mihosoft.vrl.system.PluginConfigurator { static {println \"----- rUNNING ------\"}}");
    }
    
}
