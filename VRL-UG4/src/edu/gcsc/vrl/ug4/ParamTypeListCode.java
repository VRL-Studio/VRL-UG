///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.gcsc.vrl.ug4;
//
//import eu.mihosoft.vrl.lang.CodeBuilder;
//
///**
// *
// * @author Michael Hoffer <info@michaelhoffer.de>
// */
//public class ParamTypeListCode {
//
//    private NativeParamInfo[] params;
//    private boolean visual;
//
//    public ParamTypeListCode(NativeParamInfo[] params, boolean visual) {
//        this.params = params;
//        this.visual = visual;
//    }
//
//    @Override
//    public String toString() {
//        return toString(new CodeBuilder()).toString();
//    }
//
//    public CodeBuilder toString(CodeBuilder builder) {
//
//        for (int i = 0; i < params.length; i++) {
//
//            if (i > 0) {
//                builder.append(", ");
//            }
//
//            builder.append(params[i].getTypeClassName());
//        }
//
//        if (visual) {
//
//            if (params.length > 0) {
//                builder.append(", ").append("VisualIDRequest");
//            } else {
//                builder.append("VisualIDRequest");
//            }
//        }
//
//
//        return builder;
//    }
//}
