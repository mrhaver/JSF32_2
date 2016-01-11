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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
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
    private static final Logger LOG = Logger.getLogger(KochManager.class.getName());
    
    public KochManager(){
        this.edges = new ArrayList<>();
        counter = 0;
    }
    
    public void calculateKochFractal(int level){

        try {
            ServerSocket serverSocket = new ServerSocket(8189);
            
            LOG.log(Level.INFO, "Server is running. Listening on port: {0}", serverSocket.getLocalPort());
            
            while (true) {
                
                try {
                    Socket incomingSocket = serverSocket.accept();
                    LOG.log(Level.INFO, "New Client Connected: {0}", incomingSocket.getInetAddress());
                    
                    edges.clear();
                    
                    thrdBottom = new Thread(new GenerateBottom(this, level, incomingSocket));
                    thrdLeft = new Thread(new GenerateLeft(this, level, incomingSocket));
                    thrdRight = new Thread(new GenerateRight(this, level, incomingSocket));
                    
                    thrdBottom.start();
                    thrdLeft.start();
                    thrdRight.start();
                    break;
                    
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "IOException occurred: {0}", e.getMessage());
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
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
    
    public void writeMemMappedToFile(int level){
        int length = edges.size() * 32; // one edge 4 doubles 1 double 8 byte -- so length = edges.size() * 4 * 8
        try{
            //Write
            File f = new File("C:\\Edges\\Mapped" + level + ".txt");
            if(f.exists()){
                f.delete();
            }
            RandomAccessFile ras;
            ras = new RandomAccessFile("C:\\Edges\\Mapped" + level + ".txt", "rw");
            FileChannel fc = ras.getChannel();
            MappedByteBuffer out;
            out = fc.map(FileChannel.MapMode.READ_WRITE,0,length);
            TimeStamp ts = new TimeStamp();
            ts.setBegin("Start Wrtiting Mapped");
            for(Edge e : edges){
                out.putDouble(e.X1);
                out.putDouble(e.Y1);
                out.putDouble(e.X2);
                out.putDouble(e.Y2);
            }
            ts.setEnd("End Writing Mapped");
            ras.close();
            System.out.println(ts.toString());
        }
        catch(FileNotFoundException fe){
            System.out.println(fe.toString());
        }
        catch(IOException ie){
            System.out.println(ie.toString());
        }
    }
    
    public void writeLockedMemMapped(int level){
        final String BUFFERFILE = "C:\\Edges\\buffer.bin";
        final boolean EXCLUSIVE = false;
        final boolean SHARED = true;
        final int MAXVAL = edges.size(); // We schrijven het aantal edges weg
        final int NBYTES = 44;
        int STATUS_NOT_READ = 1;
        int STATUS_READ = 0;
        
        Random r = new Random();
        FileLock exclusiveLock = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(BUFFERFILE, "rw");
            FileChannel ch = raf.getChannel();
            MappedByteBuffer out = ch.map(FileChannel.MapMode.READ_WRITE, 0, NBYTES);

            int newValue = 0; // de waarde die we naar het bestand gaan schrijven
            for (Edge e : edges) {
                // Probeer het lock te verkrijgen
                exclusiveLock = ch.lock(0, NBYTES, EXCLUSIVE);

                /**
                 * Now modify the data . . .
                 */

                // layout: 
                //      0 .. 3 :    4 bytes int with maxvalue edges
                //      4 .. 7 :    4 bytes int with status
                //      8 .. 11:    4 bytes int with the NR of the current edge
                //      12 .. 19:   8 bytes double with value X1
                //      20 .. 27:   8 bytes double with value Y1
                //      28 .. 35:   8 bytes double with value X2
                //      36 .. 43:   8 bytes double with value Y2 
                
                // Vraag waarde van status op
                out.position(4);
                int status = out.getInt();

                // Alleen als de voorgaande geproduceerde waarde is gelezen
                // dwz status != STATUS_NOT_READ
                // <of> 
                // als er nog niets geproduceerd is kunnen we schrijven
                if (((status != STATUS_NOT_READ) || (newValue == 0))) {
                    // Ga naar het begin van het bestand
                    out.position(0);
                    // Schrijf maxwaarde weg
                    out.putInt(MAXVAL);
                    // Er wordt een nieuwe waarde geschreven dus deze is
                    // nog niet gelezen door de client --> zet naar correcte
                    // status
                    out.putInt(STATUS_NOT_READ);
                    // schrijf weg hoeveel edges er al geweest zijn
                    out.putInt(newValue);
                    // Schrijf de geproduceerde edges weg
                    out.putDouble(e.X1);
                    out.putDouble(e.Y1);
                    out.putDouble(e.X2);
                    out.putDouble(e.Y2);
                    //Debug: System.out.println("EdgeNR: " + newValue + " X1: " + e.X1 + " Y1: " + e.Y1 + " X2: " + e.X2 + " Y2: " + e.Y2);
                    // De volgende waarde die we uiteindelijk weg willen schrijven
                    newValue++;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                // release the lock
                exclusiveLock.release();

            }
        } catch (java.io.IOException ioe) {
            System.err.println(ioe);
        } finally {
            if (exclusiveLock != null) {
                try {
                    exclusiveLock.release();
                } catch (IOException ex) {
                    Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("Schrijver: KLAAR");
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
