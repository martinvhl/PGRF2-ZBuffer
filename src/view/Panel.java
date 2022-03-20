package view;

import rasterize.ImageBuffer;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    private final ImageBuffer raster;

    public Panel(ImageBuffer raster) {
        this.raster = raster;
        this.setPreferredSize(new Dimension(raster.getWidth(),raster.getHeight()));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.repaint(g);
    }

    public ImageBuffer getImageBuffer() {
        return raster;
    }
}
