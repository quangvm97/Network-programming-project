package ltm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class SendMessage extends Thread {
    protected List<ClientHandler> clients;
    protected String userInput;
    protected BufferedReader console;

    public SendMessage(List<ClientHandler> clients) {
        this.clients = clients;
        this.userInput = null;
        this.start();
    }

    public void run() {
        System.out.println("New Communication Thread Started");
        if (clients.size() == 1) {
            System.out.println("Enter message:");
        }
        try {
            if (clients.size() > 0) {
                this.console = new BufferedReader(new InputStreamReader(
                        System.in));
                Scanner reader = new Scanner(System.in);
//                while ((this.userInput = reader.next()) != null) {
//                    if (userInput != null & userInput.length() > 0) {
                        for (ClientHandler client : clients) {
                            client.out.println("ahihi");
                            client.out.flush();
                            Thread.currentThread();
                            Thread.sleep(1 * 1000);
                        }
                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
