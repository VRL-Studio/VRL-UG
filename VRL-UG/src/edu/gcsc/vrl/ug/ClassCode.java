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

    private NativeAPIInfo nativeAPI;
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
        this.nativeAPI = nativeAPI;
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

        String prefix = "";

        if (isConst) {
            prefix = "Const";
        }

        if (asInterface) {
            classHeaderCode =
                    "public interface "
                    + prefix + CodeUtils.interfaceName(classInfo.getName())
                    + " extends UGObjectInterface ";

            if (!isConst) {
                classHeaderCode += ", Const"
                        + CodeUtils.interfaceName(classInfo.getName());
            }

            if (classInfo.getClassNames() != null
                    && classInfo.getBaseClassNames().length > 0) {

                classHeaderCode += ", "
                        + CodeUtils.namesToInterfaceNameList(
                        classInfo.getBaseClassNames(), prefix);
            }
        } else if (asFullClass) {
            classHeaderCode = "public class "
                    + prefix + CodeUtils.className(classInfo.getName())
                    + " extends edu.gcsc.vrl.ug.UGObject implements "
                    + prefix + CodeUtils.interfaceName(classInfo.getName());
        } else if (asWrapper) {
            classHeaderCode = "public final class "
                    + prefix + CodeUtils.className(classInfo.getName())
                    + " extends edu.gcsc.vrl.ug.UGObject implements "
                    + prefix + CodeUtils.interfaceName(classInfo.getName());
        }

        if ((asFullClass && !isConst) || (asWrapper && !isConst)) {
            builder.addLine(new ComponentInfoCode(
                    classInfo, prefix).toString()).
                    addLine("@ObjectInfo(name=\""
                    + prefix
                    + VLangUtils.addEscapeCharsToCode(
                    classInfo.getName()) + "\")");
        } else {
            builder.addLine("@ComponentInfo(ignore=true)");
        }

        builder.addLine(classHeaderCode + " {").
                incIndentation();

        if (asFullClass || asWrapper) {
            builder.addLine(
                    "private static final long serialVersionUID=1L").
                    addLine("public " + prefix + CodeUtils.className(
                    classInfo.getName())
                    + "() { setClassName(\"" + classInfo.getName()
                    + "\"); setInstantiable(" + asFullClass + " );}").newLine();
        }


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

                if (!isConst && cls.getMethods() != null) {
                    for (NativeMethodGroupInfo m : cls.getMethods()) {
                        if (!signatures.contains(new MethodGroupSignature(m))) {
                            new MethodGroupCode(m, type, createVisual).build(
                                    builder).newLine();
                            signatures.add(new MethodGroupSignature(m));
                        }
                    }
                }

                for (NativeMethodGroupInfo m : cls.getConstMethods()) {
                    if (!signatures.contains(new MethodGroupSignature(m))) {

                        new MethodGroupCode(m, type, createVisual).build(
                                builder).newLine();

                        if (cls.getName().contains("Grid")
                                && asFullClass && isConst
//                                && new MethodGroupSignature(m).toString().contains("quadr")
                                ) {
//                            System.out.println(new MethodGroupCode(m, type, createVisual).toString());
                            
                            System.out.println(">> " + cls.getName()+ "visual: "  + createVisual  + "  , "+ new MethodGroupSignature(m));
                            
                            System.out.println(builder.getCode());
                        }

                        signatures.add(new MethodGroupSignature(m));
                    }
                }

            } // end fore classes

            signatures.clear();

        }  // end fore visual

        if (isConst && asFullClass && classInfo.getName().equals("MultiGrid")) {
            String s = builder.getCode();
            System.out.println(builder.getCode());
//                System.exit(12);
            System.out.println("HELLO");
        }

        if (asFullClass) {

            String interfaceName = prefix + CodeUtils.interfaceName(
                    classInfo.getName());

            builder.newLine().append("@MethodInfo(valueName=\"").
                    append(prefix + classInfo.getName()).append("\")").
                    newLine().append("public ").
                    append(interfaceName).
                    append(" This(@ParamInfo(nullIsValid=true, name=\"").
                    append(prefix + classInfo.getName()).append("\") ").
                    append(interfaceName).
                    append(" o ) { if(o!=null){setThis(o)}else{return this} }").
                    newLine();

            builder.newLine().append("@MethodInfo(noGUI=true)").
                    newLine().append("public ").
                    append(interfaceName).
                    append(" This() {return this;}").newLine();
        } else if (asWrapper) {
            String interfaceName = prefix + CodeUtils.interfaceName(
                    classInfo.getName());

            builder.newLine().append("@MethodInfo(valueName=\"").
                    append(prefix + classInfo.getName()).append("\")").
                    newLine().append("public ").
                    append(interfaceName).
                    append(" This(@ParamInfo(nullIsValid=true, name=\"").
                    append(prefix + classInfo.getName()).append("\") ").
                    append(interfaceName).
                    append(" o ) { if(o!=null){setThis(o)}else{return this} }").
                    newLine();

            builder.newLine().append("@MethodInfo(noGUI=true)").
                    newLine().append("public ").
                    append(interfaceName).
                    append(" This() {return this;}").newLine();
        }

        builder.decIndentation();

        builder.addLine("}").newLine().addLine(
                "// ------------------------------ //").newLine();

        return builder;
    }
}
