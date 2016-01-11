
package remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnector extends Thread {

    ObjectInputStream objectInputStream = null;
    ObjectOutputStream objectOutputStream = null;
    Socket clientSocket = null;
  
    /**
     * 
     */
    public ClientConnector(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String readMessage() {
        String returner = null;
        try {
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            returner = (String) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
        }
        return returner;
    }

    public Object readObject() {
        Object returner = null;
        try {
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            returner = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(WelcomeThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returner;
    }

    public void sendObject(Object object) {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutputStream.writeObject(object);
        } catch (IOException ex) {
            Logger.getLogger(ClientConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
