package edu.gcsc.vrl.ug;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Vector;

/**
 * NOTICE: 
 * 
 * ALL METHODS need to RETURN one of the following types:
 * 
 * XML-RPC type      Simplest Java type     More complex Java type
 * 
 * i4                int                    java.lang.Integer
 * int               int                    java.lang.Integer
 * boolean           boolean                java.lang.Boolean
 * string            java.lang.String       java.lang.String
 * double            double                 java.lang.Double
 * 
 * dateTime.iso8601  java.util.Date         java.util.Date
 * struct            java.util.Hashtable    java.util.Hashtable
 * array             java.util.Vector       java.util.Vector
 * base64            byte[]                 byte[]
 * 
 * nil (extension)   null                   null
 *
 * ATTENTION: void is NOT valid !!!
 * 
 * 
 * The PpcHandler methods were executed on the Server.
 * So result came from server calculation.
 * 
 * But the parameters of the methods are filed from the clients.
 * 
 * So RpcHandler is more a part of the server, but the clients
 * need to know the interface of this class (the method-heads).
 * 
 * @author christianpoliwoda
 */
public class RpcHandler {
    static String message = "hello123";
    
    

    public int showMessage(){
        System.out.println(message);
        return 1;
    }
    
    public int changeMessage(String message){
        System.out.println("old: "+ RpcHandler.message);
        
        RpcHandler.message = message;
        
        System.out.println("new: "+ RpcHandler.message);
        
        return 2;
    }
    
//    public Vector doOnServerAndSendResults(Vector param){//dont work because signature of method is not clear 
     public Vector doOnServerAndSendResults(String s1, String s2, Integer int1){
         
        System.out.println("START doOnServerAndSendResults SERVER");
        
        Vector results = new Vector(3*2);
        
        results.add(s1);
        results.add(s2);
        results.add(int1);
        
        results.add(s1+"server");
        results.add(s2+"int -234563");
        results.add(int1 - 234563);
        
        for (Object o : results) {
            System.out.println("on server ->"+ o);
        }
        
        System.out.println("END doOnServerAndSendResults SERVER");
        
        return results;
    }
}
