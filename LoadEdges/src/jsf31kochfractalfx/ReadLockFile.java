/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf31kochfractalfx;

import calculate.Edge;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.paint.Color;

/**
 *
 * @author Frank Haver
 */
public class ReadLockFile implements Runnable{

    private final String BUFFERFILE = "C:\\Edges\\buffer.bin";
    private final boolean EXCLUSIVE = false;
    private final boolean SHARED = true;
    private final int STATUS_NOT_READ = 1;
    private final int STATUS_READ = 0;
    private final int NBYTES = 44;
    private final JSF31KochFractalFX application;
    
    public ReadLockFile(JSF31KochFractalFX application){
        this.application = application;
    }
    
    @Override
    public void run() {
        System.out.println("Reading locked file");
        FileLock exclusiveLock = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(BUFFERFILE, "rw");
            FileChannel ch = raf.getChannel();

            MappedByteBuffer out = ch.map(FileChannel.MapMode.READ_WRITE, 0, NBYTES);

            boolean finished = false;
            while (!finished) {
                
                exclusiveLock = ch.lock(0, NBYTES, EXCLUSIVE);

                /**
                 * Try to read the data . . .
                 */
 
                // layout: 
                //      0 .. 3 :    4 bytes int with maxvalue edges
                //      4 .. 7 :    4 bytes int with status
                //      8 .. 11:    4 bytes int with the NR of the current edge
                //      12 .. 19:   8 bytes double with value X1
                //      20 .. 27:   8 bytes double with value Y1
                //      28 .. 35:   8 bytes double with value X2
                //      36 .. 43:   8 bytes double with value Y2 
                
                // Vraag de maximumwaarde, status en geproduceerde waarde op
                out.position(0);
                int maxVal = out.getInt();
                int status = out.getInt();
                int value = out.getInt();
                if(value == 0){
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            application.clearKochPanel();
                        }
                    });
                }
                double X1 = out.getDouble();
                double Y1 = out.getDouble();
                double X2 = out.getDouble();
                double Y2 = out.getDouble();
                // Alleen als er iets "nieuws" geproduceerd is verwerken we de
                // gelezen value
                if (status == STATUS_NOT_READ) {
                    final Edge edge = new Edge(X1, Y1, X2, Y2, Color.hsb(180, 1.0, 1.0));
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            application.drawEdge(edge);
                        }
                    });
                    // Nieuwe waarde gelezen. Zet status in bestand
                    out.position(4);
                    out.putInt(STATUS_READ);
                    //Debug: System.out.println("EdgeNR: " + value );
                    // Bepaal of we klaar zijn, dat is als de gelezen waarde
                    // gelijk is aan de maxVal in bytes 0 .. 3 van het bestand
                    finished = (value == maxVal);
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ReadLockFile.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(ReadLockFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("CONSUMER: KLAAR");
    }
    
}
