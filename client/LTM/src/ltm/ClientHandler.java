package ltm;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler {
    protected Socket client;
    protected PrintWriter out;

    public ClientHandler(Socket client) {
        this.client = client;
        try {
            this.out = new PrintWriter(client.getOutputStream());
            out.println("ahiahi");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}