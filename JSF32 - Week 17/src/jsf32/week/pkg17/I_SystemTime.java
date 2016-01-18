/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32.week.pkg17;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 *
 * @author Alex Ras
 */
public interface I_SystemTime extends Library{
    public I_SystemTime INSTANCE=(I_SystemTime) Native.loadLibrary("Kernel32",I_SystemTime.class);
    
    public void GetSystemTime(SYSTEMTIME result);
    
}
