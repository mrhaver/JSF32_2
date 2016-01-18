/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32.week.pkg17;

import com.sun.jna.Structure;

/**
 *
 * @author Alex Ras
 */
public class SYSTEMTIME extends Structure{
    public short wYear;
    public short wMonth;
    public short wDayOfWeek;
    public short wDay;
    public short wHour;
    public short wMinute;
    public short wSecond;
    public short wMilliseconds;
    
    public long getTimeInMillis(){
        long temp = ((long) (wYear * 365.125 * 24 * 60 * 60 * 1000) + (long) (wMonth * 12 * 30.4375 * 24 * 60 * 60 * 1000) + 
                (long) (wDay * 24 *60 * 60 * 1000) + (long) (wHour * 60 * 60 * 1000) + (long) (wMinute * 60 * 1000) + 
                (long) (wSecond * 1000) + (long) wMilliseconds);
        return temp;
    }
}
