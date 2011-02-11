/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.CodeBuilder;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class ClassCode {

    private NativeClassInfo classInfo;
    private boolean asInterface;

    public ClassCode(NativeClassInfo classInfo, boolean asInterface) {
        this.classInfo = classInfo;
        this.asInterface = asInterface;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }

    public CodeBuilder toString(CodeBuilder builder) {

        String classHeaderCode = "";

        if (asInterface) {
            classHeaderCode =
                    "public interface "
                    + CodeUtils.interfaceName(classInfo.getName())
                    +  " extends UGObjectInterface ";
            if (classInfo.getClassNames() != null
                    && classInfo.getBaseClassNames().length > 0) {
                classHeaderCode +=", "
                        + CodeUtils.namesToInterfaceNameList(
                        classInfo.getBaseClassNames());
            }
        } else {
            classHeaderCode = "public class "
                    + CodeUtils.className(classInfo.getName())
                    + " extends edu.gcsc.vrl.ug4.UGObject implements "
                    + CodeUtils.interfaceName(classInfo.getName());
        }

        builder.addLine(new ComponentInfoCode(classInfo).toString()).
                addLine(classHeaderCode + " {").
                incIndentation();
        if (!asInterface) {
            builder.addLine(
                    "private static final long serialVersionUID=1L").newLine();
        }

        for (NativeMethodInfo m : classInfo.getMethods()) {
            new MethodCode(m, asInterface, false, true).toString(builder).newLine();
        }

        for (NativeMethodInfo m : classInfo.getConstMethods()) {
            new MethodCode(m, asInterface, true, true).toString(builder).newLine();
        }

        builder.decIndentation();

        builder.addLine("}\n").addLine("// ------------------------------ //\n");

        return builder;
    }

    /**
     * @return the asInterface
     */
    public boolean isAsInterface() {
        return asInterface;
    }

    /**
     * @param asInterface the asInterface to set
     */
    public void setAsInterface(boolean asInterface) {
        this.asInterface = asInterface;
    }
}
