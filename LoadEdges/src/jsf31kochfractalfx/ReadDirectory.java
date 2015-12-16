/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf31kochfractalfx;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Frank Haver
 */
public class ReadDirectory implements Runnable{

    final private JSF31KochFractalFX application;
    
    public ReadDirectory(JSF31KochFractalFX application){
        this.application = application;
    }
    
    @Override
    public void run() {
         directoryNotification();
    }
    
    private void directoryNotification(){
        final WatchService watcher;
        // Voorbeelden van interessante locaties
        // Path dir = Paths.get("D:\\");
        System.out.println("WatchService Running...");
        Path dir = Paths.get("D:\\Edges\\");
        WatchKey key;
        try {
            watcher = FileSystems.getDefault().newWatchService();
            dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            
            while (true) {
                key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    
                    Path filename = ev.context();
                    final Path child = dir.resolve(filename);
                    
                    WatchEvent.Kind kind = ev.kind();
                    if (kind == ENTRY_CREATE) {
                        System.out.println(child + " created");
                    }
                    if (kind == ENTRY_DELETE) {
                        System.out.println(child + " deleted");
                    }
                    if (kind == ENTRY_MODIFY) {
                        System.out.println(child.toString() + " modified");
                        System.out.println("Waiting...");
                        Thread.sleep(1000);
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                application.drawFromMemMapped(child.toString());
                            }                    
                        });
                    }
                }
                key.reset();
            }

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ReadDirectory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
