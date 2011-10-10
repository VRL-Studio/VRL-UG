/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ug4test;

import edu.gcsc.vrl.ug.C_AlgebraType;
import edu.gcsc.vrl.ug.C_Domain;
import edu.gcsc.vrl.ug.F_InitUG;
import edu.gcsc.vrl.ug.F_LoadDomain;
import edu.gcsc.vrl.ug.I_AlgebraType;
import edu.gcsc.vrl.ug.I_Domain;
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
        
        I_AlgebraType aType = new C_AlgebraType();
        aType.constructor("CPU");

        F_InitUG.invoke(2, aType, "P1");

        I_Domain d = new C_Domain();

        F_LoadDomain.invoke(d,
                "/home/miho/installs/ug4/trunk/data/grids/unit_square/unit_square_quads_4x4.ugx");

        System.out.println("Domain.get_dim(): " + d.const__get_dim());
        System.out.println("Domain.const_num_edges(): " 
                + d.get_grid().const__num_edges());
        
        
    }
}
