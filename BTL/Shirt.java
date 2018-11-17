import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class Shirt{
    private DataInputStream server_is, client_is;
    private DataOutputStream server_us, server_data, client_us, client_data;
    public Socket client_connection;
    public ServerSocket server;
    
    public Shirt(int my_port){
        try {
            server = new ServerSocket(my_port);
            new ServerThread(server).start();
            new DownloadThread(this).start();
        } catch (IOException ex) {
            Logger.getLogger(Shirt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void download(String hostname, int host_port){
        try {
            client_connection = new Socket(hostname, host_port);
        } catch (IOException ex) {
            Logger.getLogger(Shirt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        Shirt huong = new Shirt(1900);
    }
}
