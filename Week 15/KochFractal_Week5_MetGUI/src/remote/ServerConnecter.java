package remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnecter {

    private static final int PORT = 6752;
    private static final String SERVERNAME = "192.168.194.1";
   
    private Socket clientSocket = null;
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;

    
    public boolean connect() {
        boolean returner = false;
        try {
            String message;
            clientSocket = new Socket(SERVERNAME, PORT);
            message = readMessageFromServer();
            if (message.equals("Connected")) {
                returner = true;
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerConnecter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returner;
    }
    public String readMessageFromServer() {
        String returner = null;
        try {
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            returner = (String) objectInputStream.readObject();
            System.out.println("Server:\t" + returner);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerConnecter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returner;
    }
    
    public Object readObjectFromServer() {
        Object returner = null;
        try {
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            returner = objectInputStream.readObject();
            if(returner.toString().contains("Connected") || returner.toString().contains("Level") || returner.toString().contains("No")
                     || returner.toString().contains("Yes") || returner.toString().contains("Done")){
                System.out.println("Server:\t" + returner);
            }
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerConnecter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returner;
    }

    public void sendObjectToServer(Object object) {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("You:\t" + object.toString());
            objectOutputStream.writeObject(object);
        } catch (IOException ex) {
            Logger.getLogger(ServerConnecter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
