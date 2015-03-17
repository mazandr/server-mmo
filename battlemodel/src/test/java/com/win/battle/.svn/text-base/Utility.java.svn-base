/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.win.battle;

/**
 *
 * @author omelnyk
 */
public class Utility {
    
    public static String addCharTo(String ch, String line, int len) {
        String sOut = "";
        if (line.trim().length() >= len) {
            sOut = line;
        } else {
            for (int i = 0; i < (len - line.trim().length()); i++) {
                if ("".equals(sOut)) {
                    sOut = ch + line;
                } else {
                    sOut = ch + sOut;
                }
            }
        }
        return sOut;
    }
    
}
