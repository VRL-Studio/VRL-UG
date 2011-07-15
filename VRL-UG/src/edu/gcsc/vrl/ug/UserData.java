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

    public double run0(double[] p){return 0;};
    public double[] run1(double[] p){return new double[0];};
    public double[][] run2(double[] p){return new double[0][0];};
    public double[][][] run3(double[] p){return new double[0][0][0];};
    public static final String[] returnTypes = {
        "double",
        "double[]",
        "double[][]",
        "double[][][]"};
}
