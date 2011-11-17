/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.io.TextLoader;
import eu.mihosoft.vrl.lang.CodeBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class PluginConfiguratorCode implements CodeElement {

    public CodeBuilder build(CodeBuilder builder) {



//                    return builder.addLine(
//                            "@ComponentInfo(ignore=true)\n"
//                            + "public class APIPluginConfigurator extends APIPluginConfiguratorImpl"
//                            + " implements eu.mihosoft.vrl.system.PluginConfigurator {}");


        try {

            InputStream input = getClass().getResourceAsStream(
                    "/edu/gcsc/vrl/ug/APIPluginConfiguratorImpl.txt");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input));

            while (reader.ready()) {
                builder.addLine(reader.readLine());
            }

        } catch (IOException ex) {
            Logger.getLogger(PluginConfiguratorCode.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return builder;
    }
}
