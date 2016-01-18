/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32.week.pkg17;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

/**
 *
 * @author Alex Ras
 */
public interface I_DiskFreeSpaceA extends Library{
    public I_DiskFreeSpaceA INSTANCE =(I_DiskFreeSpaceA) Native.loadLibrary("Kernel32", I_DiskFreeSpaceA.class);
    
    public boolean GetDiskFreeSpaceA(String path, IntByReference resultSectorsPerCluster, IntByReference resultBytesPerSector,
            IntByReference resultNumberFreeClusters, IntByReference resultTotalNumberClusters);
    
}
