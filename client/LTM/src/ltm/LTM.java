/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author aaa
 */
public class LTM {
    static final int SERVER_PORT = 4001;
    private static final String SERVER_IP = "127.0.0.1";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

//        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
//        Socket socket = new Socket(serverAddr, SERVER_PORT);
//        new Client(socket);
//        InputStream inFromServer = socket.getInputStream();
//        DataInputStream in = new DataInputStream(inFromServer);
//        PrintStream printStream = new PrintStream(socket.getOutputStream());
//        printStream.println("Readme.odt");
//        socket.getOutputStream().flush();
//        Scanner scanner = new Scanner(socket.getInputStream());
//        System.out.println(scanner.next());
        String json = "{\"client\":[{\"ip\":\"127.0.0.1\"}, {\"ip\":\"127.0.0.1\"}, {\"ip\":\"127.0.0.1\"}]}";
        parseJson(json);
    }

    static class ThreadManager {
        static final ExecutorService t = Executors.newCachedThreadPool();
    }

    public static List<ClientDetails> parseJson(String json) throws JSONException {
        List<ClientDetails> clients = new ArrayList<>();
        JSONObject jsonRoot = new JSONObject(json);
        JSONArray jsonCollection = jsonRoot.getJSONArray("client");
        for (int i = 0; i < jsonCollection.length(); i++) {
            ClientDetails client =
                    new ClientDetails(jsonCollection.getJSONObject(i).getString("ip"));
            System.out.println(client.getIp());
            clients.add(client);
        }
        return clients;
    }

}
