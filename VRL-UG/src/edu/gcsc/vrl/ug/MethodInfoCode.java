/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;

/**
 * Code element that generates method code.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MethodInfoCode implements CodeElement {

    private NativeMethodInfo method;
    private boolean visual;
    private boolean inherited;
    private boolean showMethod;

    /**
     * Constructor
     * @param method method
     * @param visual defines whether to visualize this method
     * @param inherited indicates whether this method is inherited
     *                  from base class
     * @param showMethod defines whether to show this method (hide=false), is
     *                   ignored if the method provides custom options
     */
    public MethodInfoCode(NativeMethodInfo method, boolean visual,
            boolean inherited, boolean showMethod) {
        this.method = method;
        this.visual = visual;
        this.inherited = inherited;
        this.showMethod = showMethod;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        // constructor
        if (method.isConstructor() && visual) {
            if (inherited) {
                builder.addLine("@Deprecated");
                builder.append("@MethodInfo(/*inherited*/initializer=true, noGUI=true)");
            } else {
                builder.append(
                        "@MethodInfo(/*impl*/initializer=true, interactive=false)");
            }
            return builder;
        } else if (method.isConstructor() && !visual) {
            if (inherited) {
                builder.addLine("@Deprecated");
                builder.append("@MethodInfo(/*inherited*/initializer=true, noGUI=true)");
            } else {
                builder.append("@MethodInfo(/*impl*/initializer=true, noGUI=true)");
            }
            return builder;
        }

        // regular method
        if (visual) {

            boolean needsComma = false;

            if (inherited) {
                builder.append("@MethodInfo(/*inherited*/");
            } else {
                builder.append("@MethodInfo(/*impl*/");
            }

            if (method.getOptions() != null && !method.getOptions().isEmpty()) {

                if (!method.getOptions().matches(".*hide\\s*=.*")) {
                    builder.append(method.getOptions() + ", hide=" + !showMethod);
                } else {
                    builder.append(method.getOptions());
                }
                needsComma = true;
            } else {
                builder.append("hide=" + !showMethod);
                needsComma = true;
            }

            // use interactive=false as default (no invoke-button)
            if (!method.isConstructor()
                    && !method.getOptions().matches(".*interactive\\s*=.*")) {

                if (needsComma) {
                    builder.append(", ");
                }

                builder.append("interactive=false");
            }

            if (!method.returnsVoid()) {

                String valueName = "";

                if (method.getReturnValue().isRegisteredClass()) {

                    valueName = VLangUtils.addEscapeCharsToCode(
                            method.getReturnValue().getParamInfo()[0]);

                    if (valueName.isEmpty()) {
                        valueName = CodeUtils.classNameForParamInfo(
                                method.getReturnValue().getClassName(),
                                method.isConst());
                    }
                }

                if (needsComma) {
                    builder.append(", ");
                }

                String typePrefix = "";

                if (method.getReturnValue().isConst()) {
                    typePrefix = "const ";
                }

                builder.append("valueName=\""
                        + valueName
                        + "\", valueStyle=\""
                        + method.getReturnValue().getParamInfo()[1] + "\", "
                        + "valueTypeName = \""
                        + typePrefix + method.getReturnValue().getClassName()
                        + "\", valueOptions=\""
                        + method.getReturnValue().getParamInfo()[2]);

                if (method.getReturnValue().isRegisteredClass()) {
                    builder.append(";serialization=false");
                }

                builder.append("\")");
            } else {
                builder.append(")");
            }


        } else if (!visual) {
            if (inherited) {
                builder.append("@MethodInfo(/*inherited*/noGUI=true)");
            } else {
                builder.append("@MethodInfo(/*impl*/noGUI=true)");
            }
        }

        return builder;
    }
}
