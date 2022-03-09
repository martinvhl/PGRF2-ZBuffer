package rasterize;

import transforms.Col;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class ImageBuffer implements Raster<Col> {
    private final BufferedImage img;
    private Col clearColor;

    public BufferedImage getImg() {
        return img;
    }

    public Col getClearColor() {
        return clearColor;
    }

    public void setClearColor(Col clearColor) {
        this.clearColor = clearColor;
    }
    public ImageBuffer(int width, int height) {
        img = new BufferedImage(width,height, BufferedImage.TYPE_INT_BGR);
    }

    public void repaint(Graphics g) {
        g.drawImage(img,0,0,null );//buffered image dědí z image - můžeme použít, začínáme v [0,0] a imageObserver null
    }

    //todo takhle to vrací empty
    @Override
    public Optional<Col> getElement(int x, int y) {
        return Optional.empty();
    }

    @Override
    public void setElement(int x, int y, Col value) {
        img.setRGB(x,y,value.getRGB());
    }

    @Override
    public void clear() {
        Graphics g = img.getGraphics();
        g.setColor(new Color(clearColor.getRGB()));
        g.fillRect(0,0,img.getWidth(), img.getHeight());
    }

    @Override
    public void setClearValue(Col value) {
        clearColor = value;
    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }
}
