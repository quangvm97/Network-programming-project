package ltm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static ltm.LTM.SERVER_PORT;

public class server extends Thread {
    private ServerSocket serverSocket;

    private server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.flush();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                PrintStream printStream = new PrintStream(server.getOutputStream());
                printStream.println("ahihi");
                System.out.println("Thank you for connecting to ");
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        int port = SERVER_PORT;
        try {
            Thread t = new server(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}