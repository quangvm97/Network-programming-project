package ltm;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

final class Client implements Runnable {

    private Socket mSocket;
    private OutputStreamWriter mWriter;
    private InputStreamReader mReader;
    private boolean mContinueNetworking;
    private LinkedBlockingDeque<String> mCommandsToSend;

    public Client(Socket s) throws IOException {
        mSocket = s;
        mWriter = new OutputStreamWriter(mSocket.getOutputStream());
        mReader = new InputStreamReader(mSocket.getInputStream());
        mCommandsToSend = new LinkedBlockingDeque<>();
        mContinueNetworking = true;
        LTM.ThreadManager.t.execute(this);
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