package edu.gcsc.vrl.ug;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.gcsc.vrl.ug;
//
//import eu.mihosoft.vrl.lang.CodeBuilder;
//import java.util.ArrayList;
//import java.util.Arrays;
//
///**
// *
// * @author Michael Hoffer <info@michaelhoffer.de>
// */
//public class UGAnyCode {
//
//    private NativeAPIInfo apiInfo;
//    private CodeType type;
//    static final String NAME = "UGAny";
//
//    public UGAnyCode(NativeAPIInfo apiInfo, CodeType type) {
//        this.apiInfo = apiInfo;
//        this.type = type;
//    }
//
//    @Override
//    public String toString() {
//        return toString(new CodeBuilder()).toString();
//    }
//
//    public CodeBuilder toString(CodeBuilder builder) {
//
//        String classHeaderCode = "";
//
//        String[] allClassNames = getAllClassNames();
//
//        boolean asInterface = type == CodeType.INTERFACE;
//        boolean asFullClass = type == CodeType.FULL_CLASS;
//
//        if (asInterface) {
//            classHeaderCode =
//                    "public interface "
//                    + CodeUtils.interfaceName(NAME)
//                    + " extends UGObjectInterface ";
//            if (allClassNames.length > 0) {
//                classHeaderCode += ", "
//                        + CodeUtils.namesToInterfaceNameList(
//                        allClassNames);
//            }
//        } else if (asFullClass) {
//            classHeaderCode = "public class "
//                    + CodeUtils.className(NAME)
//                    + " extends edu.gcsc.vrl.ug4.UGAnyBase implements "
//                    + CodeUtils.interfaceName(NAME);
//        }
//
//        builder.addLine(classHeaderCode + " {");
//
//
//        builder.incIndentation();
//
//        if (asFullClass) {
//            builder.addLine("private static final long serialVersionUID=1L");
//        }
//
//
//        ArrayList<MethodSignature> signatures =
//                new ArrayList<MethodSignature>();
//
//        NativeMethodInfo[] methods = getAllMethods();
//
//        for (NativeMethodInfo m : methods) {
//
//            if (!signatures.contains(new MethodSignature(m))) {
//                String paramCode = new ParamTypeListCode(
//                        m.getParameters(), false).toString();
//
//                String invocationCode = "invokeMethod(\""
//                        + CodeUtils.methodName(m.getName())
//                        + "\", [" + paramCode + "] as Class<?>[], params);";
//
//                new MethodCode(m, type, false).toString(builder, invocationCode);
//
//                signatures.add(new MethodSignature(m));
//            }
//        }
//
//        for (NativeMethodInfo m : methods) {
//
//            if (!signatures.contains(new MethodSignature(m))) {
//
//                String paramCode = new ParamTypeListCode(
//                        m.getParameters(), true).toString();
//
//                String invocationCode = "invokeMethod(\""
//                        + CodeUtils.methodName(m.getName())
//                        + "\", [" + paramCode + "] as Class<?>[], params);";
//
//                new MethodCode(m, type, true).toString(builder, invocationCode);
//
//                signatures.add(new MethodSignature(m));
//            }
//        }
//
//        builder.newLine().incIndentation();
//
//        builder.addLine("}");
//
//        return builder;
//    }
//
//    private String[] getAllClassNames() {
//        String[] result = new String[apiInfo.getClasses().length];
//
//        for (int i = 0; i < apiInfo.getClasses().length; i++) {
//            result[i] = apiInfo.getClasses()[i].getName();
//        }
//        return result;
//    }
//
//    private NativeMethodInfo[] getAllMethods() {
//
//        ArrayList<NativeMethodInfo> methods =
//                new ArrayList<NativeMethodInfo>();
//
//        for (NativeClassInfo cls : apiInfo.getClasses()) {
//            for (NativeMethodGroupInfo mG : cls.getMethods()) {
//                methods.addAll(Arrays.asList(mG.getOverloads()));
//            }
//
//            for (NativeMethodGroupInfo mG : cls.getConstMethods()) {
//                methods.addAll(Arrays.asList(mG.getOverloads()));
//            }
//        }
//
//        NativeMethodInfo[] result =
//                new NativeMethodInfo[methods.size()];
//
//        methods.toArray(result);
//
//        return result;
//    }
//}
