/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import calculate.Edge;
import calculate.KochFractal;
import calculate.Triangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Frank Haver
 */
public class GenerateLeft implements Runnable, Observer {

    final private KochFractal koch;
    final private KochManager km;
    final private int level;
    
    private static final Logger LOG = Logger.getLogger(GenerateLeft.class.getName());
    
    private Socket socket = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    
    public GenerateLeft(KochManager km, int level, Socket s){
        this.level = level;
        this.koch = new KochFractal();
        koch.addObserver(this);
        koch.setLevel(level);
        this.km = km;
        this.socket = s;
    }

    @Override
    public void update(Observable o, Object arg) {
        km.voegEdgeToe((Edge)arg);
        try{
            if(km.getCounter() == 3){
                ArrayList<Edge> edges = km.getEdges();
                Triangle value = new Triangle(edges.get(0),edges.get(1),edges.get(2));
                LOG.log(Level.INFO, "verzonden: {0}", value);
                out.writeObject(value);
                socket.close();
            }
        }
        catch(IOException ex){
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            LOG.log(Level.INFO, "Port: {0}", socket.getPort());
            
            this.out = new ObjectOutputStream(socket.getOutputStream());
            
            koch.generateLeftEdge();
            
            synchronized(this){
                km.IncreaseCounter();
                if(km.getCounter() == 3){
                    System.out.println("Calculating finished with " + km.getAmountEdges() + " edges");
                    //km.writeLockedMemMapped(level);
                    System.out.println("Enter the kochlevel / q to stop : ");
                    km.setCounter(0);
                }
            }
            
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
    }
    
}
