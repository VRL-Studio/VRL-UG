/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.lang.CodeBuilder;
import eu.mihosoft.vrl.lang.VLangUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the code of a class.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class ClassCode implements CodeElement {

    private NativeAPIInfo api;
    private NativeClassInfo classInfo;
    private CodeType type;
    private boolean isConst;

    /**
     * Constructor.
     * @param nativeAPI native api
     * @param classInfo class info
     * @param type code type
     * @param isConst defines whether to compile as const
     */
    public ClassCode(NativeAPIInfo nativeAPI,
            NativeClassInfo classInfo, CodeType type, boolean isConst) {
        this.api = nativeAPI;
        this.classInfo = classInfo;
        this.type = type;
        this.isConst = isConst;
    }

    @Override
    public String toString() {
        return build(new CodeBuilder()).toString();
    }

    @Override
    public CodeBuilder build(CodeBuilder builder) {

        String classHeaderCode = "";

        boolean asInterface = type == CodeType.INTERFACE;
        boolean asWrapper = type == CodeType.WRAP_POINTER_CLASS;
        boolean asFullClass = type == CodeType.FULL_CLASS;

        boolean weAreAGroupClass = api.isClassGroup(classInfo.getName());
        boolean weArePartOfAGroup =
                api.isInClassGroup(classInfo.getName());

        if (classInfo.isGroupClass()) {
            builder.addLine("// group");
        }

        String prefix = "";

        if (asInterface) {
            classHeaderCode =
                    "public interface "
                    + CodeUtils.interfaceName(classInfo.getName(), isConst)
                    + " extends UGObjectInterface ";

            if (!isConst) {
                // we extend the const interface
                classHeaderCode += ", "
                        + CodeUtils.interfaceName(classInfo.getName(), true);
            }

            if (classInfo.getClassNames() != null
                    && classInfo.getBaseClassNames().length > 0) {

                classHeaderCode += ", "
                        + CodeUtils.namesToInterfaceNameList(
                        classInfo.getBaseClassNames(),
                        isConst);
            }

            // add the group interface
            if (!weAreAGroupClass && weArePartOfAGroup) {
                classHeaderCode += ", "
                        + CodeUtils.interfaceName(
                        NativeClassGroupInfo.convertToClassGroup(
                        api, classInfo.getName()), isConst);
            }

        } else if (asFullClass) {

//            if (isConst) {
                classHeaderCode = "public class "
                        + CodeUtils.className(classInfo.getName(), isConst)
                        + " extends edu.gcsc.vrl.ug.UGObject implements "
                        + CodeUtils.interfaceName(classInfo.getName(), isConst);
//            } else {
//                classHeaderCode = "public class "
//                        + CodeUtils.className(classInfo.getName(), isConst)
//                        + " extends " + CodeUtils.className(classInfo.getName(), true) + " implements "
//                        + CodeUtils.interfaceName(classInfo.getName(), isConst);
//
//            }
        } else if (asWrapper) {
//            if (isConst) {
                classHeaderCode = "public class "
                        + CodeUtils.className(classInfo.getName(), isConst)
                        + " extends edu.gcsc.vrl.ug.UGObject implements "
                        + CodeUtils.interfaceName(classInfo.getName(), isConst);
//            } else {
//                classHeaderCode = "public class "
//                        + CodeUtils.className(classInfo.getName(), isConst)
//                        + " extends " + CodeUtils.className(classInfo.getName(), true) + " implements "
//                        + CodeUtils.interfaceName(classInfo.getName(), isConst);
//            }
        }

        if ((asFullClass) || (asWrapper)) {
            builder.addLine("@UGObjectInfo(instantiable="
                    + classInfo.isInstantiable()
                    + ", groupRoot=" + classInfo.isGroupClass()
                    + ", groupChild=" + api.isInClassGroup(classInfo.getName())
                    + ", constClass=" + isConst + ")");

            builder.addLine(new ComponentInfoCode(
                    classInfo, isConst).toString()).
                    addLine("@ObjectInfo(name=\""
                    + prefix
                    + VLangUtils.addEscapeCharsToCode(
                    CodeUtils.classNameForParamInfo(
                    classInfo.getName(), isConst)) + "\")");
        } else {
            builder.addLine("@ComponentInfo(ignore=true)");
        }

        builder.addLine(classHeaderCode + " {").
                incIndentation();

        if (asFullClass || asWrapper) {

            String classGrpName = "";

            if (weAreAGroupClass) {
                classGrpName = classInfo.getName();
            }

            builder.addLine(
                    "private static final long serialVersionUID=1L").
                    addLine("public " + CodeUtils.className(
                    classInfo.getName(), isConst)
                    + "() { setClassName(\"" + classInfo.getName()
                    + "\"); setInstantiable(" + asFullClass + " );"
                    + "setClassGroupObject(" + weAreAGroupClass + " );"
                    + "setClassGroupName(\"" + classGrpName + "\");}").
                    newLine();
        }

//        ArrayList<MethodGroupSignature> signatures =
//                new ArrayList<MethodGroupSignature>();

        ArrayList<MethodSignature> signatures =
                new ArrayList<MethodSignature>();

        NativeClassInfo[] baseClasses = api.baseClasses(classInfo);
        NativeClassInfo[] classes =
                new NativeClassInfo[baseClasses.length + 1];

        classes[0] = classInfo;

        // copy base classes to classes array with offset 1
        System.arraycopy(baseClasses, 0, classes, 1, baseClasses.length);

        boolean[] visual = new boolean[]{false, true};

        for (boolean createVisual : visual) {

            for (NativeClassInfo cls : classes) {

                if (!isConst && cls.getMethods() != null) {

                    // add constructor code
                    new MethodGroupCode(api,
                            NativeConstructorInfo.toNativeMethodGroupInfo(
                            cls.getConstructors()),
                            signatures, type, createVisual).build(builder).
                            newLine();

                    // add method code
                    for (NativeMethodGroupInfo m : cls.getMethods()) {
//                        if (!signatures.contains(new MethodGroupSignature(m))) {
                        new MethodGroupCode(api,
                                m, signatures, type, createVisual).build(
                                builder).newLine();
//                            signatures.add(new MethodSignature(m));
//                        }
                    }
                }

                for (NativeMethodGroupInfo m : cls.getConstMethods()) {
                    new MethodGroupCode(api,
                            m, signatures, type, createVisual).build(
                            builder).newLine();
                }

            } // end fore classes

            signatures.clear();

        }  // end fore visual

        if (asFullClass) {

            String interfaceName = CodeUtils.interfaceName(
                    classInfo.getName(), isConst);

            builder.newLine().append("@ReferenceMethodInfo()").
                    newLine().append("@MethodInfo(valueName=\""
                    + CodeUtils.classNameForParamInfo(
                    classInfo.getName(), isConst)).append("\")").
                    newLine().append("public ").
                    append(interfaceName).
                    append(" vrl__reference__method(@ParamInfo(nullIsValid=true)").
                    append(interfaceName).
                    append(" o ) { if(o!=null){setThis(o)};return this }").
                    newLine();

        } else if (asWrapper) {
            String interfaceName = CodeUtils.interfaceName(
                    classInfo.getName(), isConst);

            builder.newLine().append("@ReferenceMethodInfo()").
                    newLine().append("@MethodInfo(valueName=\""
                    + CodeUtils.classNameForParamInfo(
                    classInfo.getName(), isConst)).append("\")").
                    newLine().append("public ").
                    append(interfaceName).
                    append(" vrl__reference__method(@ParamInfo(nullIsValid=true)").
                    append(interfaceName).
                    append(" o ) { if(o!=null){setThis(o)};return this }").
                    newLine();

        }

        builder.decIndentation();

        builder.addLine("}").newLine().addLine(
                "// ------------------------------ //").newLine();

        return builder;
    }
}
