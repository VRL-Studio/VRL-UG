/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug.util;

import edu.gcsc.vrl.ug.C_CG;
import edu.gcsc.vrl.ug.C_ILU;
import edu.gcsc.vrl.ug.C_MatrixOperator;
import edu.gcsc.vrl.ug.C_StandardConvergenceCheck;
import edu.gcsc.vrl.ug.I_ApproximationSpace;
import edu.gcsc.vrl.ug.I_CG;
import edu.gcsc.vrl.ug.I_DomainDiscretization;
import edu.gcsc.vrl.ug.I_GridFunction;
import edu.gcsc.vrl.ug.I_ILU;
import edu.gcsc.vrl.ug.I_MatrixOperator;
import edu.gcsc.vrl.ug.I_StandardConvergenceCheck;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import java.io.Serializable;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@ComponentInfo(name = "UG Utility", category = "/UG4/util")
public class AlgebraTest implements Serializable{
    private static final long serialVersionUID = 1L;
    @MethodInfo(valueName="Solution")
    public I_GridFunction test(
            @ParamInfo(name="Approx.Space")
            I_ApproximationSpace approxSpace,
            @ParamInfo(name="DomainDisc")
            I_DomainDiscretization domainDisc) {
        I_GridFunction u = approxSpace.create_surface_function();
        I_GridFunction b = approxSpace.create_surface_function();
        
        I_StandardConvergenceCheck convCheck = new C_StandardConvergenceCheck();
        convCheck.set_maximum_steps(100);
        convCheck.set_minimum_defect(1e-11);
        convCheck.set_reduction(1e-12);
        
        I_ILU ilu = new C_ILU();
        I_CG cgSolver = new C_CG();
        cgSolver.set_preconditioner(ilu);
        cgSolver.set_convergence_check(convCheck);
        
        I_MatrixOperator A = new C_MatrixOperator();
        
        domainDisc.assemble_linear(A, b, u);
        
        u.set(0.0);
        domainDisc.assemble_solution(u);
        
        cgSolver.init(A);
        
        if (!cgSolver.apply_return_defect(u, b)) {
            System.err.println("ERROR???");
        }
        
        return u;
    }
}
