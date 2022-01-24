package com.jsth.chess;

import javax.swing.*;
import java.awt.Graphics2D;
import java.awt.Graphics;

public class Panel extends JPanel {
    @Override
    protected void paintChildren(Graphics graphics)
    {
        super.paintChildren(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.fillRect(0,0,600/8,600/8);
    }
}
