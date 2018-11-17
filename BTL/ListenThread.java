/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ListenThread extends Thread{
    private DataInputStream is;
    private DataOutputStream us, data;
    private Socket connection;
    private boolean check = true;
    
    public ListenThread(Socket connection){
        try {
            this.connection = connection;
            is = new DataInputStream(connection.getInputStream());
            us = new DataOutputStream(connection.getOutputStream());
            data = new DataOutputStream(connection.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ListenThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        while (check) {
            try {
                System.out.println(connection.getPort());
                String filename = is.readUTF();// Doc ten file Client yeu cau
                if( filename.equalsIgnoreCase("quit")){
                    InetAddress address = InetAddress.getLocalHost();
                    System.out.println(address.toString());
                    connection.close();
                    data.close();
                    us.close();
                    is.close();
                    check = false;
                    break;
                }
                System.out.println("Ten file Client can yeu cau download la: " + filename);
                File file = new File(filename);
                if (!file.exists()){
                    System.out.println("File khong ton tai!");
                    us.writeLong(0);
                }else{
                    long filesize = file.length();
                    System.out.println("Kich thuoc file la : " + filesize);
                    us.writeLong(filesize);
                    us.flush();
                    FileInputStream fileReader = new FileInputStream(filename);
                    byte []dataBuffer;
                    // Doc va gui file to
                    if (filesize > 1000){
                        while (filesize >= 1000){
                            int dataLength;
                            dataBuffer = new byte[1000];
                            dataLength = fileReader.read(dataBuffer, 0, 1000);
                            data.write(dataBuffer, 0, dataLength);
                            filesize = filesize - 1000;
                        }//end while
                        int dataLength = (int)filesize;
                        dataBuffer = new byte[dataLength];
                        dataLength = fileReader.read(dataBuffer, 0, dataLength);
                        data.write(dataBuffer, 0, dataLength);
                        data.flush();
                        System.out.println("Gui file thanh cong!");
                        // Doc va gui file nho
                    }else{
                        int dataLength = (int)filesize;
                        dataBuffer = new byte[dataLength];
                        dataLength = fileReader.read(dataBuffer, 0, dataLength);
                        data.write(dataBuffer, 0, dataLength);
                        data.flush();
                        System.out.println("Gui file than cong!");
                    }//end if
                }//end if
             
            } catch (IOException ex) {
                check=false;
                Logger.getLogger(ListenThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
