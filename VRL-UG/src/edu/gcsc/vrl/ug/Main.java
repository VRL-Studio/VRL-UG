/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.ug;

/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
public class Main {

    private static Integer defaultPort = 1099; // default port

    public static void main(String[] args) {

        if (args != null) {

            if (args[0] != null) {
                Integer tmp = Integer.parseInt(args[0]);

                // check port range between min and max
                if (0 <= tmp && tmp <= 65535) {
                    defaultPort = tmp;
                }
            }
        }

    }
}
