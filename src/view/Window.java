package view;

import rasterize.ImageBuffer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Window extends JFrame {
    private Dimension d = new Dimension(1280,720);
    private Panel panel;
    private ImageBuffer raster;

    Window() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        setResizable(false);
        setLocationRelativeTo(null);

        panel = new Panel(raster);
        add(panel,BorderLayout.CENTER);
    }
}
