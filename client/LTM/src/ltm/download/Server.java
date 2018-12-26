//package Server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public final static int DEFAULT_PORT = 1900;

	public static void main(String[] args) throws Exception   {
			DataInputStream is;
			DataOutputStream us, data;
			ServerSocket server = new ServerSocket(DEFAULT_PORT);
			Socket connection = null;
			while (true) {
				System.out.println("server wait ..");
				connection = server.accept();
					is = new DataInputStream(connection.getInputStream());
					us = new DataOutputStream(connection.getOutputStream());
					data = new DataOutputStream(connection.getOutputStream());
					
					String filename = is.readUTF();// Doc ten file Client yeu cau
					System.out.println("Ten file Client can yeu cau download la: " + filename);
					
					File file = new File(filename);
					if (!file.exists()){
						System.out.println("File khong ton tai!");
						us.writeLong(0);
					}else{
			            long filesize = file.length();
			            System.out.println("Kich thuoc file la: " + filesize);
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
		            
						InetAddress address = InetAddress.getLocalHost();
						System.out.println(address.toString());
					connection.close();
					data.close();
					us.close();
					is.close();
				}//end while
	}//end main
}//end class
