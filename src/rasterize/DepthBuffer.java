package rasterize;

import java.util.Arrays;
import java.util.Optional;

public class DepthBuffer implements Raster<Double>{
    private final double[][] zBuffer;
    private double clearValue;
    private final int width, height;

    public DepthBuffer(int width, int height) {
        zBuffer = new double[width][height];
        this.width = width;
        this.height = height;
        setClearValue(1.0);
        clear();
    }

    @Override
    public Optional<Double> getElement(int x, int y) {
        return Optional.of(zBuffer[x][y]);
    }

    @Override
    public void setElement(int x, int y, Double value) {
        zBuffer[x][y] = value;
    }

    @Override
    public void clear() {
        for (double[] zArray : zBuffer) {
            Arrays.fill(zArray, clearValue);
        }
    }

    @Override
    public void setClearValue(Double value) {
        this.clearValue = value;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
