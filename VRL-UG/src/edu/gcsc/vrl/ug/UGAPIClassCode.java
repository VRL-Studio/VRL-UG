/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UGAPIClassCode implements CodeElement {

    public UGAPIClassCode() {
    }

    public CodeBuilder build(CodeBuilder builder) {
        builder.addLine("class UGAPI {").
                incIndentation().

                // get svn revision
                addLine("public static java.lang.String getSvnRevision() {").
                incIndentation().
                addLine("return \"" + UG.getInstance()._getSvnRevision() + "\";").
                decIndentation().
                addLine("}").
                
                // get compile date
                addLine("public static java.lang.String getCompileDate() {").
                incIndentation().
                addLine("return \"" + UG.getInstance()._getCompileDate() + "\";").
                decIndentation().
                addLine("}").
                
                // get description
                addLine("public static java.lang.String getDescription() {").
                incIndentation().
                addLine("return \"" + UG.getInstance()._getDescription()
                + "<br><br><b>Authors:</b><br><br>"
                + UG.getInstance()._getAuthors().replace("\n", "<br>") + "\";").
                decIndentation().
                addLine("}").
                
                decIndentation().
                addLine("}");



        return builder;

    }
}
