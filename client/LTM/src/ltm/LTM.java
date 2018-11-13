/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltm;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author aaa
 */
public class LTM {
    private static Socket socket;
    private static final int SERVERPORT = 4000;
    private static final String SERVER_IP = "127.0.0.1";
        /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
        socket = new Socket(serverAddr, SERVERPORT);
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
        out.println("ahihi");

    }

    static class ThreadManager {
        static final ExecutorService t = Executors.newCachedThreadPool();
    }

    class Server {
        public static final int port = 11111;
        private final ServerSocket mServerSocket;
        private final Collection<Client> mClients;
        private boolean acceptNewClients;

        public Server() throws IOException {
            mServerSocket = new ServerSocket(port);
            mClients = new ArrayList<>();
            acceptNewClients = true;

            ThreadManager.t.execute(() -> {
                Client c;
                while (acceptNewClients) {
                    // On new client connected
                    try {
                        c = new Client(mServerSocket.accept());
                        mClients.add(c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void broadcast(String message) {
            for (Client c : mClients)
                c.sendMessage(message);
        }
    }

    final class Client implements Runnable {

        private Socket mSocket;
        private OutputStreamWriter mWriter;
        private InputStreamReader mReader;
        private boolean mContinueNetworking;
        private LinkedBlockingDeque<String> mCommandsToSend;

        public Client(Socket s) throws IOException {
            mSocket = s;
            mWriter = new OutputStreamWriter(socket.getOutputStream());
            mReader = new InputStreamReader(socket.getInputStream());
            mCommandsToSend = new LinkedBlockingDeque<>();
            mContinueNetworking = true;
            ThreadManager.t.execute(this);
        }

        void sendMessage(String message) {
            mCommandsToSend.addLast(message);
        }

        @Override
        public void run() {
            String message;
            while (mContinueNetworking) {
                try {
                    message = mCommandsToSend.take();
                    mWriter.write(message);
                    mWriter.write('\n');
                    mWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    mContinueNetworking = false;
                }
            }
        }
    }
    
}
