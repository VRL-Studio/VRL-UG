/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug.util;

import edu.gcsc.vrl.jfreechart.GraphParameterSet;
import edu.gcsc.vrl.jfreechart.JFXYChartParameters;
import edu.gcsc.vrl.ug.I_NumberArray;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ObjectInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import java.io.Serializable;
import java.util.ArrayList;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@ComponentInfo(name = "ConvergencePlotter", category = "UG4/Util")
@ObjectInfo(name = "ConvergencePlotter")
public class ConvergencePlotter implements Serializable {

    private static final long serialVersionUID = 1L;

    @MethodInfo(hide = true)
    public JFXYChartParameters show(
            @ParamInfo(name = "Defects", style = "array") I_NumberArray[] numArray) {

        ArrayList<GraphParameterSet> graphs =
                new ArrayList<GraphParameterSet>();

        
        Integer i = 0;
        for (I_NumberArray g : numArray) {
            GraphParameterSet graph = new GraphParameterSet(
                    convert(g, i.toString()));
            graphs.add(graph);
            i++;
        }

        JFXYChartParameters result = new JFXYChartParameters(graphs);
        
        result.setTitle("Convergence");
        result.setAutoRangeX(true);
        result.setAutoRangeY(true);
        result.setLegendVisible(true);
        result.setXAxisLabel("# Steps");
        result.setYAxisLabel("defect");
//        result.setNumberFormat_x("#");
        
        result.setLogscaleY(true);
        
        return result;
    }

    private XYSeries convert(
            @ParamInfo(name = "Conv.Rates") I_NumberArray numArray,
            @ParamInfo(name = "Id") String id) {
        XYSeries result = new XYSeries(id);
        
        double x = 0.0;
        double stepsize_x = 1.0;

	for (int i = 0; i < numArray.const__size();i++) {
            
            result.add(x,numArray.const__get(i));
            
            System.out.println("Defect: " + numArray.const__get(i));
            
            
            x += stepsize_x;
	}
        
        

        return result;
    }
}
