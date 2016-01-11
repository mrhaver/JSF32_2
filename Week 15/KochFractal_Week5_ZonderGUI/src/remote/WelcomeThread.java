package remote;

import interfaces.IThread;
import java.net.Socket;

public class WelcomeThread extends ClientConnector implements IThread {

    /**
     * This is the constructor for OpenThread.
     *
     * @param clientSocket is the open socket to the client.
     */
    public WelcomeThread(Socket clientSocket) {
        super(clientSocket);
    }

    @Override
    public void run() {
        String message = null;

        message = "Connected";
        sendObject(message);
        
        message = readMessage();
        if (!message.equals("Request")) {
            message = "No request recieved";
            sendObject(message);
            return;
        }
        
        message = "Level";
        sendObject(message);
        int level = (int) readObject();
        
        message = readMessage();
        if (message.equals("Short")) {
            new FastThread(clientSocket, level).start();
        } else {
            new SlowThread(clientSocket, level).start();
        }
    }
}
