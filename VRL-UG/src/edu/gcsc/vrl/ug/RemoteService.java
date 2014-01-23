package edu.gcsc.vrl.ug;

import static edu.gcsc.vrl.ug.JVMmanager.getClient;
import static edu.gcsc.vrl.ug.JVMmanager.getCurrentIP;
import static edu.gcsc.vrl.ug.JVMmanager.getCurrentPort;
import edu.gcsc.vrl.ug.types.RemoteLoadFileType;
import edu.gcsc.vrl.ug.types.RemoteType;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.io.IOUtil;
import eu.mihosoft.vrl.reflection.TypeRepresentation;
import eu.mihosoft.vrl.system.VRL;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/*
 * This class should hide the complexity of remote interaction between client and server file transfer
 * for the user.
 */
/**
 *
 * @author Christian Poliwoda <christian.poliwoda@gcsc.uni-frankfurt.de>
 */
public class RemoteService {

    /**
    With this method the user do not need to know / care about were the file should be store
    on server side.
    
    @param file that should be transfered to server
    @return true is transfer worked, else false
    */
    public static Boolean transferFileToServer(File file) {
        
        if (UG.getRemoteType().equals(RemoteType.CLIENT)) {

            //get the client to communicte with the current server
            XmlRpcClient client = JVMmanager.getCurrentClient();

            String pathOnClient = file.getAbsolutePath();

            // addind all needed parameters into parameter vector in a XMLrpc usable state
            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(pathOnClient);

            try {
                //convert the file into a string
                //and add it to the parameter vector
                xmlRpcParams.addElement(IOUtil.fileToBase64(file));

            } catch (IOException ex) {
                Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

            //add checksumme
            String checksumme = IOUtil.generateMD5Sum(file);
            xmlRpcParams.addElement(checksumme);

            try {
                //call remote the server method
                Object o = client.execute("RpcHandler.saveFileWithChecksumme", xmlRpcParams);

                if (o instanceof Boolean) {

                    return ((Boolean) o).booleanValue();
                }

            } catch (XmlRpcException ex) {
                Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }

        return false;
    }

    /**
    With this method the user do not need to know / care about were the output file is stored
    on server side, he only need to know were it should be on client side. This method will get the 
    corresponding server file and return it if possible.
    
    @param fileOnClientSide the file on client side
    @return the file on client side with the data from server if all was succesful, else null.
    */
    public static File getFileFromServer(/*TypeRepresentation typeRep,*/File fileOnClientSide) {
        File result = null;

        if (UG.getRemoteType().equals(RemoteType.CLIENT)) {

            String pathOnClient = fileOnClientSide.getAbsolutePath();

            //get the client to communicte with the current server
            XmlRpcClient client = JVMmanager.getCurrentClient();

            // addind all needed parameters into parameter vector in a XMLrpc usable state
            Vector xmlRpcParams = new Vector();
            xmlRpcParams.addElement(pathOnClient);

            try {
                //call remote the server method
                Object o = client.execute("RpcHandler.getFileWithChecksumme", xmlRpcParams);

                if (o instanceof String[]) {

                    String[] convertedFileAndCecksumme = (String[]) o;

                    try {
                        //writte data from server file into client file
                        IOUtil.base64ToFile(convertedFileAndCecksumme[0], fileOnClientSide);

                    } catch (IOException ex) {
                        Logger.getLogger(RemoteService.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String checksumme = IOUtil.generateMD5Sum(fileOnClientSide);

                    //check if data is correct transfered
                    if (checksumme.equals(convertedFileAndCecksumme[1])) {

                        // data is correct so set result
                        result = fileOnClientSide;
                    }
                }// if (o instanceof String[])

            } catch (XmlRpcException ex) {
                Logger.getLogger(JVMmanager.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return result;
    }

}
