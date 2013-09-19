/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import edu.gcsc.vrl.ug.UG;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ObjectInfo;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
@ObjectInfo(name = "TestParameterArray")
@ComponentInfo(name = "TestParameterArray", category = "VRL/VRL-UG")
// do not forget to add the class in Configurator.register() via
// vApi.addComponent("THE_CLASSFILE_OF_THIS_CLASS")
public class TestParameterArray implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void testBooleanArray() {

        System.out.println("java side: start");

        Boolean[] boolArray1 = {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};

        UG.test_debug("TestParameterArray.testBooleanArray()", boolArray1);

        System.out.println("java side: end");
    }

    public static void testArrayOFBooleanArrays() {

        System.out.println("java side: start");

        ArrayList<Boolean[]> arrayOfBooleanArrays = new ArrayList<Boolean[]>();

        Boolean[] boolArray1 = {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        Boolean[] boolArray2 = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
        Boolean[] boolArray3 = {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE};

        arrayOfBooleanArrays.add(boolArray1);
        arrayOfBooleanArrays.add(boolArray2);
        arrayOfBooleanArrays.add(boolArray3);

        for (int i = 0; i < arrayOfBooleanArrays.size(); i++) {

            System.out.println("Array " + i + ":");
            Boolean[] boolArray = arrayOfBooleanArrays.get(i);

            for (int j = 0; j < boolArray.length; j++) {
                System.out.println("  value " + j + " = " + boolArray[j]);
            }
        }

        Object[] array;
        array = arrayOfBooleanArrays.toArray();

        System.out.println("Java sout() -> UG.test_debug() called by TestParameterArray.testArrayOFBooleanArrays()");
//        UG.test_debug("Java -> UG.test_debug()", array);

        System.out.println("java side: PART 2 !! ");


        //mist FALSCH ... studio schmiert ab
//        System.out.println("Java sout() -> UG.getInstance().invokeFunction() called by TestParameterArray.testArrayOFBooleanArrays()");
//        UG.getInstance().invokeFunction("jobjectArray2ParamStack", true, array);


//        UG.test_debug("F_ChrisPoliTest.invoke()", array);// DONT WORK
//        UG.getInstance().invokeFunction("F_ChrisPoliTest.invoke()",true, null);// DONT WORK
//        UG.getInstance().invokeFunction("ChrisPoliTest",true, null);// DONT WORK
//        UG.getInstance().invokeFunction("ChrisPoliTest",true, void);// DONT WORK
//        

//        //changing function-parameterlist from "()" to "(int)"
//        int numberOfFunctionParameters = 1;
//        Object[] params = new Object[numberOfFunctionParameters];
//        params[0] = new Integer(2);
////        UG.getInstance().invokeFunction("F_ChrisPoliTest.invoke()",true, params);// DONT WORK
//        UG.getInstance().invokeFunction("ChrisPoliTest",true, params);

        
        //method ChrisPoliTest() registered in "trunk/ugbase/bridge/misc_bridges/test_bridge.cpp"
        int numberOfFunctionParameters = 0;
        Object[] params = new Object[numberOfFunctionParameters];
        UG.getInstance().invokeFunction("ChrisPoliTest",true, params);


        System.out.println("java side: end");
    }
}
