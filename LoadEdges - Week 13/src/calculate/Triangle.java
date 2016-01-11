/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;

/**
 *
 * @author Alex Ras
 */
public class Triangle {
    private ArrayList<Edge> edges;
    
    public Triangle(Edge e1, Edge e2, Edge e3){
        this.edges.add(e1);
        this.edges.add(e2);
        this.edges.add(e3);
    }
    
    public ArrayList<Edge> getEdges(){
        return edges;
    }
}
