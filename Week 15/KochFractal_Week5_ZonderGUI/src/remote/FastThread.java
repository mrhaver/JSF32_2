package remote;

import callculate.Edge;
import callculate.KochFractal;
import interfaces.IThread;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import kochfractal_week5_zondergui.Socket_Server;

public class FastThread extends ClientConnector implements IThread, Observer {

    private List<Edge> edges = null;
    private final int level;
    private int nrOfEdges;

    /**
     */
    public FastThread(Socket clientSocket, int level) {
        super(clientSocket);
        this.level = level;
    }

    @Override
    public void run() {
        String message = null;
        message = readMessage();
        if (message.equals("Already Calculated")) {
            edges = Socket_Server.getList(level);
            if (edges == null) {
                message = "No";
            } else {
                message = "Yes";
            }
            sendObject(message);
        }
        if (edges == null) {
            edges = new ArrayList<>();
            KochFractal kochFractal = new KochFractal();
            kochFractal.setLevel(level);
            nrOfEdges = kochFractal.getNrOfEdges();
            sendObject(nrOfEdges);
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(FastThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            kochFractal.addObserver(this);
            kochFractal.generateLeftEdge();
            kochFractal.generateBottomEdge();
            kochFractal.generateRightEdge();
        } else {
            sendObject(edges);
            finish();
        }
    }

    private void finish() {
        Socket_Server.setList(level, edges);
        String message = "Done";
        sendObject(message);
    }

    @Override
    public void update(Observable o, Object arg) {
        sendObject(arg);
        Edge e = (Edge) arg;
        edges.add(e);
        if (edges.size() == nrOfEdges) {
            finish();
        }
    }
}
