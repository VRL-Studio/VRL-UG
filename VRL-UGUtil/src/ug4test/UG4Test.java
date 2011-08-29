/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ug4test;

import edu.gcsc.vrl.ug.C_CPUAlgebraSelector;
import edu.gcsc.vrl.ug.C_Domain;
import edu.gcsc.vrl.ug.F_InitUG;
import edu.gcsc.vrl.ug.F_LoadDomain;
import eu.mihosoft.vrl.system.VRL;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class UG4Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        VRL.initAll(args);

        F_InitUG initUG = new F_InitUG();
        initUG.invoke(2, new C_CPUAlgebraSelector(), "P1");

        C_Domain d = new C_Domain();

        new F_LoadDomain().invoke(d,
                "/home/miho/installs/ug4/trunk/data/grids/unit_square/unit_square_quads_4x4.ugx");

        System.out.println("Domain.get_dim(): " + d.const_get_dim());
        System.out.println("Domain.const_num_edges(): " 
                + d.get_grid().const_num_edges());
        
        
    }
}
