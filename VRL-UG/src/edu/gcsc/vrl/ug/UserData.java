/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public abstract class UserData {

    public double run0(double[] _p, int _si){return 0;};
    public double[] run1(double[] _p, int _si){return new double[0];};
    public double[][] run2(double[] _p, int _si){return new double[0][0];};
    public double[][][] run3(double[] _p, int _si){return new double[0][0][0];};
    public static final String[] returnTypes = {
        "double",
        "double[]",
        "double[][]",
        "double[][][]"};
}
