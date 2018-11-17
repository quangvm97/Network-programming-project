package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		String hostname = "127.0.0.1";
		int port = 1900;
		String inputFileName = null;
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		File inputFile = null; 
		FileOutputStream fileReader  = null;
		Boolean rep = true;
		DataOutputStream os;
		DataInputStream data, is;
		byte []dataBuffer;
		File newFile = new File("download");// Tao folder luu file Download
		newFile.mkdir();
		
		
		try {
			while (rep){
				Socket theSocket = new Socket(hostname, port);
				os = new DataOutputStream(theSocket.getOutputStream());
				data = new DataInputStream(theSocket.getInputStream());
				is = new DataInputStream(theSocket.getInputStream());
					System.out.println("Nhap ten file can download: ");
					inputFileName = input.readLine();
					os.writeUTF(inputFileName);
					
					long filesize = is.readLong();// Doc kich thuoc file Server gui
					if (filesize == 0){
						System.out.println("File Khong ton tai!");
					}else{
						inputFile = new File(newFile.getPath() + "/" + inputFileName);// Luu file download theo duong dan
						if (!inputFile.exists()){
								inputFile.createNewFile();
							}
						fileReader = new FileOutputStream(inputFile);
						System.out.println("Kich thuoc file la: " + filesize + " byte");
						// Doc va luu file to
						if (filesize > 1000){
							while (filesize >= 1000){
				            	int dataLength;
				            	dataBuffer = new byte[1000];
				            	if ((dataLength = data.read(dataBuffer, 0, 1000)) == 1000){ 
				            		fileReader.write(dataBuffer, 0, dataLength);
				            	}else{
				            		System.out.println("Download loi! Vui long download lai!");
				            		break;
				            	}
				            	filesize = filesize - 1000;
							}	
							int dataLength = (int)filesize;
			            	dataBuffer = new byte[dataLength];
			            	if ((dataLength = data.read(dataBuffer, 0, dataLength)) == dataLength){
			            		fileReader.write(dataBuffer, 0, dataLength);
			            		System.out.println("Download file " + inputFileName + " thanh cong!");
			            	}else{
			            		System.out.println("Download loi! Vui long download lai!");
			            		break;
			            	}
			            // Doc va luu file nho
						}else{
			            	int dataLength = (int)filesize;
			            	dataBuffer = new byte[dataLength];
			            	if ((dataLength = data.read(dataBuffer, 0, dataLength)) == dataLength){
			            		fileReader.write(dataBuffer, 0, dataLength);
			            		System.out.println("Download file " + inputFileName + " thanh cong");
			            	}else{
			            		System.out.println("Download loi! Vui long download lai!");
			            		break;
			            	}
			            }//end else
					}// end else
					System.out.println("Ban co muon tiep tuc download khong? Y/N: ");
					String x = input.readLine();
					if (!x.equals("Y")){
						rep = false;
					}
					theSocket.close();
					data.close();
					is.close();
					os.close();
				}			
			System.out.println("Bye Bye!");
		} // end try
		catch (Exception ex) {
			System.err.println(ex);
		}
	} // end main

}