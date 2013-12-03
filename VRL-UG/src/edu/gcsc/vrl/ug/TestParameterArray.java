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

    public static Object testBooleanArray() {

        System.out.println("java side: start");

        Boolean[] boolArray1 = {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};

        Object obj = UG.test_debug("Java TestParameterArray.testBooleanArray() -> UG.test_debug()", boolArray1);

        System.out.println("java side: end");

        return obj;
    }

    /**
     * Here we call a native (test/debug) method [JAVA: test_debug() -> _test_debug() :CPP]
     * which calls other cpp-methods VIA an instance of ug.
     * 
     * Bootle-Neck problem:
     * all java test call need to go through [JAVA: test_debug() -> _test_debug() :CPP]
     * and can than be an cpp side redirected to other test-/debug-calls.
     */
    public static Object testArrayOFBooleanArrays() {

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

        System.out.println("Java sout() -> UG.test_debug() with array of 3 Boolean[]");

        Object obj = UG.test_debug("Java TestParameterArray.testArrayOFBooleanArrays() -> UG.test_debug()", array);

        System.out.println("java side: end");

        return obj;
    }

    /**
     * Here we call methods which are registered in the registry of native UG
     * WITHOUT using/needing an instance of ug.
     *
     * Direct Call:
     * call all cpp-registered test-/debug-methods directly without a bootle-neck like
     * [JAVA: test_debug() -> _test_debug() :CPP].
     */
    public static Object callingDirectCppMethodsWithParameterArrayList() {
        System.out.println("java side: callingDirectCppMethodsWithParameterArrayList() ");

//        System.out.println("JAVA: ChrisPoliTest 1");
//        //method ChrisPoliTest() registered in "trunk/ugbase/bridge/misc_bridges/test_bridge.cpp"
//        int numberOfFunctionParameters = 0;
//        Object[] params = new Object[numberOfFunctionParameters];
//        UG.getInstance().invokeFunction("ChrisPoliTest",true, params);
//
//        System.out.println("JAVA: ChrisPoliTest 2");
//        //method ChrisPoliTest(int i) registered in "trunk/ugbase/bridge/misc_bridges/test_bridge.cpp"
//        int numberOfFunctionParameters1 = 1;
//        Object[] params1 = new Object[numberOfFunctionParameters1];
//        params1[0] = new Integer(2);
//        UG.getInstance().invokeFunction("ChrisPoliTest",true, params1);
//
//        System.out.println("JAVA: ChrisPoliTest 3");
//        //method ChrisPoliTest(bool array[]) registered in "trunk/ugbase/bridge/misc_bridges/test_bridge.cpp"
//        int numberOfFunctionParameters2 = 2;
//        Object[] params2 = new Object[numberOfFunctionParameters2];
//        //FIRST START
////        params2[0] = new ArrayList<Boolean>();
////        ((ArrayList<Boolean>)params2[0]).add(Boolean.TRUE);
////        ((ArrayList<Boolean>)params2[0]).add(Boolean.FALSE);
//        //FIRST END
//        //SECOND START
//        params2[0] = Boolean.TRUE;
//        params2[1] = Boolean.FALSE;
//        //SECOND END
//        UG.getInstance().invokeFunction("ChrisPoliTest",true, params2);
        System.out.println("JAVA: ChrisPoliTest 4 ArrayList");
        //method ChrisPoliTest(bool array[]) registered in "trunk/ugbase/bridge/misc_bridges/test_bridge.cpp"
        int numberOfFunctionParameters2 = 1;
        Object[] params2 = new Object[numberOfFunctionParameters2];
//       
//        // Versuch 1 start
//        params2[0] = new ArrayList<Boolean>();
//        ((ArrayList<Boolean>)params2[0]).add(Boolean.TRUE);
//        ((ArrayList<Boolean>)params2[0]).add(Boolean.FALSE);
//        // Versuch 1 ende
//        
        // Versuch 2 start
        ArrayList<Boolean> array = new ArrayList<Boolean>();
        array.add(Boolean.TRUE);
        array.add(Boolean.FALSE);
        params2[0] = array;
        // Versuch 2 ende

        Object obj = UG.getInstance().invokeFunction("ChrisPoliTest", true, params2);

        return obj;
    }

    /**
     * Here we call methods which are registered in the registry of native UG
     * WITHOUT using/needing an instance of ug.
     *
     * Direct Call:
     * call all cpp-registered test-/debug-methods directly without a bootle-neck like
     * [JAVA: test_debug() -> _test_debug() :CPP].
     */
    public static Object callingDirectCppMethodsWithParameterArray() {
        System.out.println("java side: callingDirectCppMethodsWithParameterArray() ");

        System.out.println("JAVA: ChrisPoliTest Array");

        int numberOfFunctionParameters = 1;
        Object[] params = new Object[numberOfFunctionParameters];

        Boolean[] array = new Boolean[4];
        array[0] = Boolean.TRUE;
        array[1] = Boolean.FALSE;
        array[2] = Boolean.FALSE;
        array[3] = Boolean.TRUE;
        params[0] = array;

        //method ChrisPoliTest(bool array[]) registered in "trunk/ugbase/bridge/misc_bridges/test_bridge.cpp"
//        Object obj = UG.getInstance().invokeFunction("ChrisPoliTest", true, params);
        Object obj = UG.getInstance().invokeFunction("ChrisPoliTestReturn", true, params);

        return obj;
    }

    public static Boolean[] convertObjectToBoolenArray(Object object) {
        System.out.println("java side: convertObjectToBoolenArray() ");

        Boolean[] boolArray = (Boolean[]) object;

        System.out.println("boolArray = " + boolArray);

        return boolArray;
    }

    public static void printBooleanArray(Boolean[] array) {
        System.out.println("java side: printBooleanArray() ");

        for (int i = 0; i < array.length; i++) {
            System.out.println("array[" + i + "] = " + array[i]);

        }
    }
}
