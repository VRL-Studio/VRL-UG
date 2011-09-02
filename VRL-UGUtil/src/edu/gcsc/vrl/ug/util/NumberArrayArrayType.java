/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug.util;

import edu.gcsc.vrl.ug.C_NumberArray;
import edu.gcsc.vrl.ug.I_NumberArray;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.types.ArrayBaseType;
import java.lang.annotation.Annotation;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class NumberArrayArrayType extends ArrayBaseType {

    public NumberArrayArrayType() {
        setValueName("XY Series");
        setType(I_NumberArray[].class);
        ParamInfo info = new ParamInfo() {

            @Override
            public String name() {
                return "XY";
            }

            @Override
            public String style() {
                return "default";
            }

            @Override
            public boolean nullIsValid() {
                return false;
            }

            @Override
            public String options() {
                return "";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };
        
        setElementInputInfo(info);
    }
}
