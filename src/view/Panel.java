package view;

import rasterize.ImageBuffer;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    private final ImageBuffer raster;
    private static final int WIDTH=1280, HEIGHT = 720;


    public Panel(ImageBuffer raster) {
        this.raster = raster;
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g) { //g - odkaz do bufferredimage
        super.paintComponent(g);
        raster.repaint(g);
    }
}
