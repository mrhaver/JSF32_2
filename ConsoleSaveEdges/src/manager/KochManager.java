/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import calculate.Edge;
import calculate.KochFractal;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import timeutil.TimeStamp;

/**
 *
 * @author Frank Haver
 */
public class KochManager {
    private final ArrayList<Edge> edges;
    private int counter;
    private Thread thrdLeft;
    private Thread thrdRight;
    private Thread thrdBottom;
    
    public KochManager(){
        this.edges = new ArrayList<>();
        counter = 0;
    }
    
    public void calculateKochFractal(int level){
        edges.clear();
        thrdBottom = new Thread(new GenerateBottom(this, level));
        thrdLeft = new Thread(new GenerateLeft(this, level));
        thrdRight = new Thread(new GenerateRight(this, level));
        thrdBottom.start();
        thrdLeft.start();
        thrdRight.start();
    }
    
    public void writeByteEdgesToFile(int level){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("edges.byte");
            // DataOutputStream voor de mooie methodes
            DataOutputStream dos = new DataOutputStream(out);
            // schrijf velden van edge  
            TimeStamp ts = new TimeStamp();
            ts.setBegin("Start writing byte (no buffer)");
            dos.writeInt(level);           
            for(Edge e : edges){
                dos.writeDouble(e.X1);
                dos.writeDouble(e.Y1);
                dos.writeDouble(e.X2);
                dos.writeDouble(e.Y2);
                //dos.writeDouble(e.color.getHue());
            }
            ts.setEnd("End writing byte (no buffer)");
            System.out.println(ts.toString());
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public void writeBByteEdgesToFile(int level){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("edgesColor11.byte");
            // DataOutputStream voor de mooie methodes
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(out));
            // schrijf velden van edge  
            TimeStamp ts = new TimeStamp();
            ts.setBegin("Start writing byte (buffer)");
            dos.writeInt(level);           
            for(Edge e : edges){
                dos.writeDouble(e.X1);
                dos.writeDouble(e.Y1);
                dos.writeDouble(e.X2);
                dos.writeDouble(e.Y2);
                dos.writeDouble(e.color.getHue());
            }
            ts.setEnd("End writing byte (buffer)");
            System.out.println(ts.toString());
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public void writeTextEdgesToFile(int level) {
        FileWriter fw;
        try {
            fw = new FileWriter("edges.txt");
            //fw.write(persoon.toString()); //kan niet.
            // maar ik wil regel schrijven!!!
            PrintWriter pr = new PrintWriter(fw);
            TimeStamp ts = new TimeStamp();
            ts.setBegin("Start writing text (no buffer)");
            pr.println(level);            
            for(Edge e : edges){
                pr.println(e.toString());
            }
            ts.setEnd("End writing (no buffer)");
            System.out.println(ts.toString());
            pr.close();
        } catch (IOException ex) {
           System.out.println(ex.getMessage());
        }
    }
    
    public void writeBTextEdgesToFile(int level) {
        FileWriter fw;
        try {
            fw = new FileWriter("edgesNoColor11.txt");
            //fw.write(persoon.toString()); //kan niet.
            // maar ik wil regel schrijven!!!
            PrintWriter pr = new PrintWriter(new BufferedWriter(fw));
            TimeStamp ts = new TimeStamp();
            ts.setBegin("Start writing text (buffer)");
            pr.println(level);            
            for(Edge e : edges){
                pr.println(e.toString());
            }
            ts.setEnd("End writing (buffer)");
            System.out.println(ts.toString());
            pr.close();
        } catch (IOException ex) {
           System.out.println(ex.getMessage());
        }
    }
    
    synchronized public void voegEdgeToe(Edge edge){        
        edges.add(edge);
    }
    
    public ArrayList<Edge> getEdges(){
        return edges;
    }
    
    synchronized public void IncreaseCounter(){
        counter++;
    }
    
    synchronized public int getCounter(){
        return counter;
    }
    
    synchronized public void setCounter(int value){
        counter = value;
    }
    
    public int getAmountEdges(){
        return edges.size();
    }
}
