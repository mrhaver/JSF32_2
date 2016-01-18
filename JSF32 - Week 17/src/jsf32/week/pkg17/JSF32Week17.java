/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32.week.pkg17;

import com.sun.jna.ptr.IntByReference;

/**
 *
 * @author Alex Ras
 */
public class JSF32Week17 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Opdracht 1
        System.out.println("GetSystemTime vs System.nanoTime:");
        System.out.println(" ");
        I_SystemTime libTime = I_SystemTime.INSTANCE;
        
        SYSTEMTIME firstSystemTime = new SYSTEMTIME();
        SYSTEMTIME secondSystemTime = new SYSTEMTIME();
        
        libTime.GetSystemTime(firstSystemTime);
        for(int i=0;i<1000000000;i++);
        libTime.GetSystemTime(secondSystemTime);
        
        long passedTime = secondSystemTime.getTimeInMillis() - firstSystemTime.getTimeInMillis();
        
        System.out.println("GetSystemTime: " + passedTime + "ms");
        
        long startTime = System.nanoTime();
        for(int i=0;i<1000000000;i++);
        long estimatedTime = System.nanoTime() - startTime;
        
        System.out.println("System.nanoTime: " + estimatedTime / 1000000 + "ms");
        System.out.println(" ");
        
        //Opdracht 2
        System.out.println("GetDiskFreeSpace: ");
        System.out.println(" ");
        
        I_DiskFreeSpaceA libSpace = I_DiskFreeSpaceA.INSTANCE;
        
        IntByReference sectorsPerCluster = new IntByReference();
        IntByReference bytesPerSector = new IntByReference();
        IntByReference freeClusters = new IntByReference();
        IntByReference totalClusters = new IntByReference();
        
        
        libSpace.GetDiskFreeSpaceA("C:\\", sectorsPerCluster, bytesPerSector, freeClusters, totalClusters);
        
        long lSectorsPerCluster = sectorsPerCluster.getValue();
        long lBytesPerSector = bytesPerSector.getValue();
        long lFreeClusters = freeClusters.getValue();
        long freeBytes = lFreeClusters * lSectorsPerCluster * lBytesPerSector;
        long freeGigaBytes = freeBytes / 1000000000;
        
        System.out.println("Free Bytes on C:\\: " + freeBytes);
        System.out.println("This is " + freeGigaBytes +"GB.");
    }
    
}
