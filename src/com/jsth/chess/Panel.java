package com.jsth.chess;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    int x = 55;
    int y = 45;
    int size = 60;
    private static final long serialVersionUID = -12345677L;
    @Override
    protected void paintChildren(Graphics graphics)
    {
        super.paintChildren(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;





        for (int j = 0; j < 4; j++) {
            graphics2D.setColor(Color.darkGray);
            for (int i = 0; i < 4; i++) {
                graphics2D.fillRect(x + 2 * i *size, y + (0 + 2 *j) * size, size, size);
                graphics2D.fillRect(x + (1 + 2 *i) * size, y +(1 + 2*j) * size, size, size);
            }


            graphics2D.setColor(Color.lightGray);
            for (int i = 0; i < 4; i++) {
                graphics2D.fillRect(x + (1+2*i)*size, y + (0 + 2 *j) * size, size, size);
                graphics2D.fillRect(x + 2 * i * size, y + (1+2*j)* size, size, size);
            }
        }
    }
}
