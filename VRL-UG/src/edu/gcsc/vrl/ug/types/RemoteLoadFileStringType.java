/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.gcsc.vrl.ug.types;

import eu.mihosoft.vrl.annotation.TypeInfo;
import eu.mihosoft.vrl.types.LoadFileStringType;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
@TypeInfo(type = String.class, input = true, output = false, style = "remote-load-dialog")
public class RemoteLoadFileStringType extends LoadFileStringType{
    
}
