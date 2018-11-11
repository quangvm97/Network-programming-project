/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author aaa
 */
public class LTM {
    private static Socket socket;
    private static final int SERVERPORT = 4000;
    private static final String SERVER_IP = "192.168.8.112";
        /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

//        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
//        socket = new Socket(serverAddr, SERVERPORT);      
//        PrintWriter out = new PrintWriter(new BufferedWriter(
//                new OutputStreamWriter(socket.getOutputStream())), true);
//        out.println("ahihi");

    }
    
}
