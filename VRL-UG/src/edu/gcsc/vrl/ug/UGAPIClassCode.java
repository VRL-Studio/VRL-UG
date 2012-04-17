/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;
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
                addLine("return \"" + VLangUtils.addEscapesToCode(UG.getInstance().getSvnRevision()) + "\";").
                decIndentation().
                addLine("}").
                
                // get compile date
                addLine("public static java.lang.String getCompileDate() {").
                incIndentation().
                addLine("return \"" + VLangUtils.addEscapesToCode(UG.getInstance().getCompileDate()) + "\";").
                decIndentation().
                addLine("}").
                
                // get description
                addLine("public static java.lang.String getDescription() {").
                incIndentation().
                addLine("return \"" + VLangUtils.addEscapesToCode(UG.getInstance().getDescription())
                + "<br><br><b>Authors:</b><br><br>"
                + VLangUtils.addEscapesToCode(UG.getInstance().getAuthors()).replace("\n", "<br>") + "\";").
                decIndentation().
                addLine("}").
        
                // get ug version
                addLine("public static java.lang.String getVersion() {").
                incIndentation().
                addLine("return \"" + getVersion() +"\";").
                decIndentation().
                addLine("}").
                
                decIndentation().
                addLine("}");



        return builder;

    }

    private String getVersion() {
        // TODO: remove this method if ug4.0.1 is released!
        
        String version = "4.0.0"; // fixme !
        
        try {
            return VLangUtils.addEscapesToCode(UG.getInstance().getUGVersion());
        }catch(Throwable tr) {
            //
        }
        
        return VLangUtils.addEscapesToCode(version);
    }
}
