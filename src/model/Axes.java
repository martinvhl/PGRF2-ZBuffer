package model;

import transforms.Col;

import java.util.List;

public class Axes extends Solid {

    private Col barva = new Col(255,255,255);

    public Axes(List<Vertex> vertexBuffer, List<Integer> indexBuffer, List<Part> partBuffer) {
        super(vertexBuffer, indexBuffer, partBuffer);
        createAxes();
    }

    private void createAxes() {

    }

    public Col getBarva() {
        return barva;
    }

    public void setBarva(Col barva) {
        this.barva = barva;
    }
}
