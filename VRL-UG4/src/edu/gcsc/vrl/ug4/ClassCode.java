/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug4;

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;
import java.util.ArrayList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class ClassCode {

    private NativeAPIInfo nativeAPI;
    private NativeClassInfo classInfo;
    private boolean asInterface;

    public ClassCode(NativeAPIInfo nativeAPI,
            NativeClassInfo classInfo, boolean asInterface) {
        this.nativeAPI = nativeAPI;
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
                    + " extends UGObjectInterface ";
            if (classInfo.getClassNames() != null
                    && classInfo.getBaseClassNames().length > 0) {
                classHeaderCode += ", "
                        + CodeUtils.namesToInterfaceNameList(
                        classInfo.getBaseClassNames());
            }
        } else {
            classHeaderCode = "public class "
                    + CodeUtils.className(classInfo.getName())
                    + " extends edu.gcsc.vrl.ug4.UGObject implements "
                    + CodeUtils.interfaceName(classInfo.getName());
        }

        if (!asInterface) {
            builder.addLine(new ComponentInfoCode(classInfo).toString()).
                    addLine("@ObjectInfo(name=\""
                    + VLangUtils.addEscapeCharsToCode(classInfo.getName()) + "\")");
        } else {
            builder.addLine("@ComponentInfo(ignore=true)");
        }

        builder.addLine(classHeaderCode + " {").
                incIndentation();

        if (!asInterface) {
            builder.addLine(
                    "private static final long serialVersionUID=1L").
                    addLine("public " + CodeUtils.className(classInfo.getName())
                    + "() { setClassName(\"" + classInfo.getName()
                    + "\");}").newLine();
        }


        ArrayList<MethodGroupSignature> signatures = new ArrayList<MethodGroupSignature>();

//        Setting[] settings = new Setting[4];
//
//        // visual
//        settings[0] = new Setting(false, true);
//        settings[1] = new Setting(true, true);
//
//        //non-visual
//        settings[2] = new Setting(false, false);
//        settings[3] = new Setting(true, false);


        NativeClassInfo[] baseClasses = nativeAPI.baseClasses(classInfo);
        NativeClassInfo[] classes = new NativeClassInfo[baseClasses.length + 1];

        classes[0] = classInfo;

        // copy base classes to classes array with offset 1
        System.arraycopy(baseClasses, 0, classes, 1, baseClasses.length);

        boolean[] visual = new boolean[]{false,true};

        for (boolean b : visual) {

            for (NativeClassInfo cls : classes) {
                for (NativeMethodGroupInfo m : cls.getMethods()) {
                    if (!signatures.contains(new MethodGroupSignature(m))) {
                        new MethodGroupCode(m, asInterface, b).toString(
                                builder).newLine();
                        signatures.add(new MethodGroupSignature(m));
                    }
                }

                for (NativeMethodGroupInfo m : cls.getConstMethods()) {
                    if (!signatures.contains(new MethodGroupSignature(m))) {
                        new MethodGroupCode(m, asInterface, b).toString(
                                builder).newLine();
                        signatures.add(new MethodGroupSignature(m));
                    }
                }
            } // end fore classes

            signatures.clear();
        }  // end fore visual

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
