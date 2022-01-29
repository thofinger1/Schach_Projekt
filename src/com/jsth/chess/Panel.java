package com.jsth.chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Panel extends JPanel {
    int x = 55;
    int y = 45;

    int size = 60;
    private static final long serialVersionUID = -12345677L;

    @Override
    protected void paintChildren(Graphics graphics) {
        super.paintChildren(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        //img laden und anzeigen
        ClassLoader cl = getClass().getClassLoader();
        URL rURL = cl.getResource("/Users/jonasstarck/Desktop/POS1/AndroidStudio/Schach_Projekt/src/com/jsth/chess/img/bishop_black.png");
        System.out.println(rURL);
        if (rURL == null) {
            System.out.println("Bild wurde nicht geladen");
        } else {
            try {
                File image = new File(rURL.toURI());
                try {
                    Image img = ImageIO.read(image);
                    graphics2D.drawImage(img, 0, 0, size, size, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }


        for (int j = 0; j < 4; j++) {
            graphics2D.setColor(Color.darkGray);
            for (int i = 0; i < 4; i++) {
                graphics2D.fillRect(x + 2 * i * size, y + (0 + 2 * j) * size, size, size);
                graphics2D.fillRect(x + (1 + 2 * i) * size, y + (1 + 2 * j) * size, size, size);
            }


            graphics2D.setColor(Color.lightGray);
            for (int i = 0; i < 4; i++) {
                graphics2D.fillRect(x + (1 + 2 * i) * size, y + (0 + 2 * j) * size, size, size);
                graphics2D.fillRect(x + 2 * i * size, y + (1 + 2 * j) * size, size, size);
            }
        }
    }
}
