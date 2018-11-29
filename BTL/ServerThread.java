/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ServerThread extends Thread{
    private ServerSocket server;
    public ServerThread(ServerSocket server){
        this.server = server;
    }
    
    public void run(){
        try {
            while (true) {
                System.out.println("Server wait ...");
                new ListenThread(server.accept()).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
