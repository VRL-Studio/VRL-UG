/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ObjectInfo;
import eu.mihosoft.vrl.reflection.VisualCanvas;
import eu.mihosoft.vrl.types.CanvasRequest;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@ComponentInfo(name = "Release Instances", category = "UG4/util")
@ObjectInfo(name="Release Instances")
public class ReleaseInstances {

    @MethodInfo()
    public void release(CanvasRequest cReq) {
        if (cReq != null) {
            MemoryManager.releaseAll(cReq.getCanvas());
        }
    }

    @MethodInfo(noGUI=true)
    public void release(VisualCanvas canvas) {
        MemoryManager.releaseAll(canvas);
    }
}
