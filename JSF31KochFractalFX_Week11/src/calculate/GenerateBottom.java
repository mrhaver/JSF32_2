/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import jsf31kochfractalfx.JSF31KochFractalFX;

/**
 *
 * @author Frank Haver
 */
public class GenerateBottom extends Task<Void> implements Observer {
    
    final private KochFractal koch;
    final private KochManager km;
    final private JSF31KochFractalFX application;
    private double edges = 0;
    
    public GenerateBottom(KochManager km, JSF31KochFractalFX application, int level){
        this.koch = new KochFractal();
        koch.addObserver(this);
        koch.setLevel(level);
        this.km = km;
        this.application = application;
    }
    
    @Override
    public void update(Observable o, final Object arg) {
        edges++;
        km.voegEdgeToe((Edge)arg);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {

        }
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                application.drawEdge((Edge)arg, true);
            }           
        });
        updateProgress(edges,koch.getNrOfEdges() / 3);
        updateMessage("Nr edges: " + String.valueOf(edges));
    }

    @Override
    synchronized protected Void call() throws Exception {
        koch.generateBottomEdge();
        km.IncreaseCounter();
        if(km.getCounter() == 3){
            application.requestDrawEdges();
            km.setCounter(0);
            //km.notifyWait();
        }
        return null;
    }
}
