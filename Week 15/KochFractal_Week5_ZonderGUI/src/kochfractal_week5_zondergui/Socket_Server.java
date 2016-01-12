
package kochfractal_week5_zondergui;

import callculate.Edge;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import remote.WelcomeThread;

public class Socket_Server {

    private static ServerSocket serverSocket;
    private static List<Edge> level1;
    private static List<Edge> level2;
    private static List<Edge> level3;
    private static List<Edge> level4;
    private static List<Edge> level5;
    private static List<Edge> level6;
    private static List<Edge> level7;
    private static List<Edge> level8;
    private static List<Edge> level9;
    private static List<Edge> level10;

    public static List<Edge> getList(int level) {
        switch (level) {
            case 1: {
                return level1;
            }
            case 2: {
                return level2;
            }
            case 3: {
                return level3;
            }
            case 4: {
                return level4;
            }
            case 5: {
                return level5;
            }
            case 6: {
                return level6;
            }
            case 7: {
                return level7;
            }
            case 8: {
                return level8;
            }
            case 9: {
                return level9;
            }
            case 10: {
                return level10;
            }
            default: {
                return null;
            }
        }
    }

    public static void setList(int level, List<Edge> list) {
        switch (level) {
            case 1: {
                level1 = list;
                break;
            }
            case 2: {
                level2 = list;
                break;
            }
            case 3: {
                level3 = list;
                break;
            }
            case 4: {
                level4 = list;
                break;
            }
            case 5: {
                level5 = list;
                break;
            }
            case 6: {
                level6 = list;
                break;
            }
            case 7: {
                level7 = list;
                break;
            }
            case 8: {
                level8 = list;
                break;
            }
            case 9: {
                level9 = list;
                break;
            }
            case 10: {
                level10 = list;
                break;
            }
        }
    }

    public static void main(String[] args) {
        int port = 6752;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Socket_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Port " + serverSocket.getLocalPort());
        while (true) {
            try {
                new WelcomeThread(serverSocket.accept()).start();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                break;
            }
        }
    }
}
