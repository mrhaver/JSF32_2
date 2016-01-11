package remote;

import callculate.Edge;
import callculate.KochFractal;
import interfaces.IThread;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import kochfractal_week5_zondergui.Socket_Server;

public class SlowThread extends ClientConnector implements IThread, Observer {
    
    private List<Edge> edges;
    private final int level;
    private int nrOfEdges;

    /**
     */
    public SlowThread(Socket clientSocket, int level) {
        super(clientSocket);
        this.level = level;
    }

    @Override
    public void run() {
        edges = Socket_Server.getList(level);
        if (edges == null) {
            edges = new ArrayList<>();
            KochFractal kochFractal = new KochFractal();
            kochFractal.setLevel(level);
            nrOfEdges = kochFractal.getNrOfEdges();
            kochFractal.addObserver(this);
            kochFractal.generateLeftEdge();
            kochFractal.generateBottomEdge();
            kochFractal.generateRightEdge();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Edge e = (Edge) arg;
        edges.add(e);
        if (edges.size() == nrOfEdges) {
            sendObject(edges);
            Socket_Server.setList(level, edges);
        }
    }
}
