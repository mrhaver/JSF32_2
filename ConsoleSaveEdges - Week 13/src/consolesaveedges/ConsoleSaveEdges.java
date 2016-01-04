/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consolesaveedges;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.KochManager;
/**
 *
 * @author Frank Haver
 */
public class ConsoleSaveEdges {

    /**
     * @param args the command line arguments
     */
    
    private static KochManager kochManager = new KochManager();
    private static final String instructionLine = "Enter the kochlevel / q to stop: ";
    
    public static void main(String[] args) {
        
        System.out.println(instructionLine);
    
        String inputString;
     
        Scanner scanIn = new Scanner(System.in);
        
        boolean retry = true;
        while(retry){
            inputString = scanIn.nextLine();

            if(inputString.equals("q")){
                System.exit(0);
            }
            else{
                if(isInteger(inputString)){
                    int level = Integer.valueOf(inputString);
                    if(level >= 1 && level < 13){
                        kochManager.calculateKochFractal(Integer.valueOf(inputString));                        
                    }
                    else{
                        System.out.println("level must be between 1 and 13");
                        System.out.println(instructionLine);
                    }

                }
                else{
                    System.out.println("input is no integer");
                    System.out.println(instructionLine);
                }
            }
        }
    }
    
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
    
}
