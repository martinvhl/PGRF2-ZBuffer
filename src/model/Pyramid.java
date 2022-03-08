package model;

import transforms.Col;

import java.util.List;

public class Pyramid extends Solid {
    private int baseEdge = 5;
    private int height = 8;
    private Col barva = new Col(0,255,0);

    public Pyramid(List<Vertex> vertexBuffer, List<Integer> indexBuffer, List<Part> partBuffer) {
        super(vertexBuffer, indexBuffer, partBuffer);
        createPyramid();
    }

    private void createPyramid() {
        //TODO vertices

        //TODO indices - triangles

        //TODO indices - lines

        //TODO parts to partbuffer
    }

    public int getBaseEdge() {
        return baseEdge;
    }

    public void setBaseEdge(int baseEdge) {
        this.baseEdge = baseEdge;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Col getBarva() {
        return barva;
    }

    public void setBarva(Col barva) {
        this.barva = barva;
    }
}
