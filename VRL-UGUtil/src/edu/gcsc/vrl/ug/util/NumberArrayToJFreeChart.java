/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug.util;

import edu.gcsc.vrl.ug.C_Const__NumberArray;
import edu.gcsc.vrl.ug.C_NumberArray;
import edu.gcsc.vrl.ug.I_NumberArray;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ObjectInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import java.io.Serializable;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@ComponentInfo(name="NumberArrayToJFreeChart", category="JFreeChart")
@ObjectInfo(name="NumberArrayToJFreeChart")
public class NumberArrayToJFreeChart implements Serializable {

    private static final long serialVersionUID = 1L;

    public NumberArrayToJFreeChart() {
    }

    @MethodInfo(valueName = "Conv.Rates")
    public XYSeries convert(
            @ParamInfo(name = "Conv.Rates") I_NumberArray numArray,
            @ParamInfo(name = "Id") String id) {
        XYSeries result = new XYSeries(id);
        
        double x = 0.0;
        double stepsize_x = 1.0;

	for (int i = 0; i < numArray.const__size();i++) {
            
            result.add(x,Math.log10(numArray.const__get(i)));
            x += stepsize_x;
	}
        
        

        return result;
    }
}
