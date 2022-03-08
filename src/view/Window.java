package view;

import rasterize.ImageBuffer;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private final Panel panel;
    Dimension d = new Dimension(1280,720);
    private ImageBuffer raster = new ImageBuffer(d.width,d.height); //!tady vytváříme raster pro celou appku a předáváme ji do panelu

    public Window() {
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("PGRF 2: Z-Buffer | Martin Vahala");

        panel = new Panel(raster);
        add(panel,BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        panel.grabFocus();
    }
}
