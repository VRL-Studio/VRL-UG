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
    private CodeType type;

    public ClassCode(NativeAPIInfo nativeAPI,
            NativeClassInfo classInfo, CodeType type) {
        this.nativeAPI = nativeAPI;
        this.classInfo = classInfo;
        this.type = type;
    }

    @Override
    public String toString() {
        return toString(new CodeBuilder()).toString();
    }

    public CodeBuilder toString(CodeBuilder builder) {

        String classHeaderCode = "";

        boolean asInterface = type == CodeType.INTERFACE;
        boolean asWrapper = type == CodeType.WRAP_POINTER_CLASS;
        boolean asFullClass = type == CodeType.FULL_CLASS;

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
        } else if (asFullClass) {
            classHeaderCode = "public class "
                    + CodeUtils.className(classInfo.getName())
                    + " extends edu.gcsc.vrl.ug4.UGObject implements "
                    + CodeUtils.interfaceName(classInfo.getName());
        } else if (asWrapper) {
            classHeaderCode = "public final class "
                    + CodeUtils.className(classInfo.getName())
                    + " extends edu.gcsc.vrl.ug4.UGObject implements "
                    + CodeUtils.interfaceName(classInfo.getName());
        }

        if (asFullClass) {
            builder.addLine(new ComponentInfoCode(classInfo).toString()).
                    addLine("@ObjectInfo(name=\""
                    + VLangUtils.addEscapeCharsToCode(classInfo.getName()) + "\")");
        } else {
            builder.addLine("@ComponentInfo(ignore=true)");
        }

        builder.addLine(classHeaderCode + " {").
                incIndentation();

        if (asFullClass) {
            builder.addLine(
                    "private static final long serialVersionUID=1L").
                    addLine("public " + CodeUtils.className(classInfo.getName())
                    + "() { setClassName(\"" + classInfo.getName()
                    + "\");}").newLine();
        } else if (asWrapper) {
            builder.addLine(
                    "private static final long serialVersionUID=1L").
                    addLine("protected " + CodeUtils.className(classInfo.getName())
                    + "() { setClassName(\"" + classInfo.getName()
                    + "\");}").newLine();
        }

//        if (asInterface || asFullClass) {

        ArrayList<MethodGroupSignature> signatures =
                new ArrayList<MethodGroupSignature>();

        NativeClassInfo[] baseClasses = nativeAPI.baseClasses(classInfo);
        NativeClassInfo[] classes =
                new NativeClassInfo[baseClasses.length + 1];

        classes[0] = classInfo;

        // copy base classes to classes array with offset 1
        System.arraycopy(baseClasses, 0, classes, 1, baseClasses.length);

        boolean[] visual = new boolean[]{false, true};

        for (boolean createVisual : visual) {

            for (NativeClassInfo cls : classes) {
                for (NativeMethodGroupInfo m : cls.getMethods()) {
                    if (!signatures.contains(new MethodGroupSignature(m))) {
                        new MethodGroupCode(m, type, createVisual).toString(
                                builder).newLine();
                        signatures.add(new MethodGroupSignature(m));
                    }
                }

                for (NativeMethodGroupInfo m : cls.getConstMethods()) {
                    if (!signatures.contains(new MethodGroupSignature(m))) {
                        new MethodGroupCode(m, type, createVisual).toString(
                                builder).newLine();
                        signatures.add(new MethodGroupSignature(m));
                    }
                }
            } // end fore classes

            signatures.clear();
        }  // end fore visual

//        } // end if (asInterface || asFullClass)

        if (asFullClass) {

            String interfaceName = CodeUtils.interfaceName(classInfo.getName());
            String className = CodeUtils.className(classInfo.getName());

            builder.newLine().append("@MethodInfo()").
                    newLine().append("public ").
                    append("void setThis(@ParamInfo(name=\"").
                    append(classInfo.getName()).append("\") ").
                    append(interfaceName).
                    append(" o ) {super.setThis(o)}").newLine();

            builder.newLine().append("@MethodInfo(valueName=\"").
                    append(classInfo.getName()).append("\")").
                    newLine().append("public ").
                    append(CodeUtils.interfaceName(classInfo.getName())).
                    append(" getThis() {return this;}").newLine();

//            builder.newLine().
//                    append("protected UGObject newInstance(Pointer p) {").
//                    newLine().incIndentation().
//                    append("UGObject result = new edu.gcsc.vrl.ug4.").
//                    append(className).append("();").
//                    newLine().append("result.setPointer(p);").
//                    newLine().append("return result;").newLine().
//                    decIndentation().append("}").newLine();
        }
//        else if (type == CodeType.WRAP_POINTER_CLASS) {
//
//        }

        builder.decIndentation();

        builder.addLine("}").newLine().addLine(
                "// ------------------------------ //").newLine();

        return builder;
    }
}
