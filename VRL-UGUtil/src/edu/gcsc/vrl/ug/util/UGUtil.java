/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug.util;

import edu.gcsc.vrl.ug.C_ConstUserMatrix;
import edu.gcsc.vrl.ug.C_Domain;
import edu.gcsc.vrl.ug.C_GlobalMultiGridRefiner;
import edu.gcsc.vrl.ug.F_DistributeDomain;
import edu.gcsc.vrl.ug.F_GlobalDomainRefiner;
import edu.gcsc.vrl.ug.F_LoadDomain;
import edu.gcsc.vrl.ug.I_ConstUserMatrix;
import edu.gcsc.vrl.ug.I_Domain;
import edu.gcsc.vrl.ug.I_GlobalMultiGridRefiner;
import edu.gcsc.vrl.ug.I_IRefiner;
import edu.gcsc.vrl.ug.I_MGSubsetHandler;
import edu.gcsc.vrl.ug.I_SubsetHandler;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@ComponentInfo(name = "UG Utility", category = "/UG4/util")
public class UGUtil {

    @MethodInfo(valueName = "Domain")
    public static I_Domain CreateAndDistributeDomain(
            @ParamInfo(name = "Grid-Name:") String gridName,
            @ParamInfo(name = "numRefs:", options = "min=0") int numRefs,
            @ParamInfo(name = "numPreRefs:", options = "min=0") int numPreRefs,
            @ParamInfo(name = "Subsets:", style = "array") String[] neededSubsets) {

        // -- create Instance of a Domain
        I_Domain dom = new C_Domain();

        if (new F_LoadDomain().invoke(dom, gridName) == false) {
            print("Loading Domain failed. Aborting.");
            return null;
        }

        // -- create Refiner
        if (numPreRefs > numRefs) {
            print("numPreRefs must be smaller than numRefs. Aborting.");
            exit();
        }

        // -- Create a refiner instance. This is a factory method
        // -- which automatically creates a parallel refiner if required.

        I_IRefiner refiner = new F_GlobalDomainRefiner().invoke(dom);

        // -- Performing pre-refines
        for (int i = 0; i < numPreRefs; i++) {
            refiner.refine();
        }

        //-- Distribute the domain to all involved processes
        if (new F_DistributeDomain().invoke(dom) == false) {
            print("Error while Distributing Grid. Aborting.");
            exit();
        }

        // -- Perform post-refine
        for (int i = numPreRefs; i < numRefs; i++) {
            refiner.refine();
        }

        // -- Now we loop all subsets an search for it in the SubsetHandler of the domain
        if (neededSubsets != null) {
            if (CheckSubsets(dom, neededSubsets) == false) {
                print("Wrong subsets detected.");
            }
        }

        return dom;
    }

    @MethodInfo()
    public static boolean CheckSubsets(
            @ParamInfo(name = "Domain") I_Domain dom,
            @ParamInfo(name = "Subsets:", style = "array") String[] subsets) {
        I_MGSubsetHandler sh = dom.get_subset_handler();

        for (String subset : subsets) {
            if (sh.const_get_subset_index(subset) == -1) {
                print("Domain does not contain subset \"" + subset + "\".");
                return false;
            }
        }

        return true;
    }
    
    @MethodInfo(valueName="ConstUserMatrix")
    public static I_ConstUserMatrix CreateConstDiagUserMatrix(
            @ParamInfo(name = "diag. val") Double diagVal,
            @ParamInfo(name = "dim:", options="min=1") int dim) {
        
       I_ConstUserMatrix mat = new C_ConstUserMatrix();
       mat.set_diag_tensor(diagVal);
       
       return mat;
    }

    @MethodInfo(noGUI=true)
    public static void print(String s) {
        System.err.println(s);
    }

    @MethodInfo(noGUI=true)
    public static void exit() {
        System.exit(1);
    }
}
