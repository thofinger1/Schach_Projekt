package com.jsth.chess;
import javax.swing.JFrame;

public class Chess {
    public static void main(String[] args) {
        JFrame jf = new JFrame("Schach");
        jf.setSize(600, 600);
        jf.setLocation(0,1300);
        Panel panel = new Panel();
        jf.add(panel);
        jf.setVisible(true);
    }
}
