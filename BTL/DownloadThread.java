/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class DownloadThread extends Thread{
    private Shirt shirt;
    
    public DownloadThread(Shirt shirt){
        this.shirt = shirt;
    }
    
    public void run(){
        while (true) {            
            try {
                System.out.println("Nhap dia chi ip: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                String inputIp = input.readLine();
                Socket theSocket = new Socket(inputIp, 1900);
                File inputFile = null; 
                FileOutputStream fileReader  = null;
                String inputFileName = null;
                Boolean rep = true;
                DataOutputStream os;
                DataInputStream data, is;
                byte []dataBuffer;
                File newFile = new File("download");// Tao folder luu file Download
                newFile.mkdir();
                os = new DataOutputStream(theSocket.getOutputStream());
                data = new DataInputStream(theSocket.getInputStream());
                is = new DataInputStream(theSocket.getInputStream());
                while (rep){
                    System.out.println("Nhap ten file can download hoac QUIT de ket thuc: ");
                    inputFileName = input.readLine();
                    if (inputFileName.equalsIgnoreCase("QUIT")){
                            os.writeUTF(inputFileName);
                            theSocket.close();
                            data.close();
                            is.close();
                            os.close();
                            break;
                    }else {
                            os.writeUTF(inputFileName);
                            long filesize = is.readLong();// Doc kich thuoc file Server gui
                            if (filesize == 0) {
                                    System.out.println("File Khong ton tai!");
                            } else {
                                    inputFile = new File(newFile.getPath() + "/" + inputFileName);// Luu file download theo duong dan
                                    if (!inputFile.exists()) {
                                            inputFile.createNewFile();
                                    }
                                    fileReader = new FileOutputStream(inputFile);
                                    System.out.println("Kich thuoc file la: " + filesize + " byte");
                                    // Doc va luu file to
                                    if (filesize > 1000) {
                                            while (filesize >= 1000) {
                                                    int dataLength;
                                                    dataBuffer = new byte[1000];
                                                    if ((dataLength = data.read(dataBuffer, 0, 1000)) == 1000) {
                                                            fileReader.write(dataBuffer, 0, dataLength);
                                                    } else {
                                                            System.out.println("Download loi! Vui long download lai!");
                                                            break;
                                                    }
                                                    filesize = filesize - 1000;
                                            }
                                            int dataLength = (int) filesize;
                                            dataBuffer = new byte[dataLength];
                                            if ((dataLength = data.read(dataBuffer, 0, dataLength)) == dataLength) {
                                                    fileReader.write(dataBuffer, 0, dataLength);
                                                    System.out.println("Download file " + inputFileName + " thanh cong!");
                                            } else {
                                                    System.out.println("Download loi! Vui long download lai!");
                                                    break;
                                            }
                                            // Doc va luu file nho
                                    } else {
                                            int dataLength = (int) filesize;
                                            dataBuffer = new byte[dataLength];
                                            if ((dataLength = data.read(dataBuffer, 0, dataLength)) == dataLength) {
                                                    fileReader.write(dataBuffer, 0, dataLength);
                                                    System.out.println("Download file " + inputFileName + " thanh cong");
                                            } else {
                                                    System.out.println("Download loi! Vui long download lai!");
                                                    break;
                                            }
                                    }//end else
                                    }
                            }// end else
                    } // end while
            System.out.println("Bye Bye!");
            } catch (IOException ex) {
                Logger.getLogger(DownloadThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
