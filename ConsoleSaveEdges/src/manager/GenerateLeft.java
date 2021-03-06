/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import calculate.Edge;
import calculate.KochFractal;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Frank Haver
 */
public class GenerateLeft implements Runnable, Observer {

    final private KochFractal koch;
    final private KochManager km;
    final private int level;
    
    public GenerateLeft(KochManager km, int level){
        this.level = level;
        this.koch = new KochFractal();
        koch.addObserver(this);
        koch.setLevel(level);
        this.km = km;
    }

    @Override
    public void update(Observable o, Object arg) {
        km.voegEdgeToe((Edge)arg);
    }

    @Override
    public void run() {
        koch.generateLeftEdge();
        km.IncreaseCounter();
        if(km.getCounter() == 3){
            System.out.println("Calculating finished with " + km.getAmountEdges() + " edges");
            km.writeBTextEdgesToFile(level);
            System.out.println("Enter the kochlevel / q to stop : ");
            km.setCounter(0);
        }
        
    }
    
}
