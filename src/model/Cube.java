package model;

import transforms.Col;
import transforms.Point3D;

import java.util.List;

public class Cube extends Solid {

    //základní délka hrany pro kostku
    private int edge = 5;

    //TODO - upravit barvy (př. kolekce barev) pro barevnou interpolaci
    //vytvoříme červenou pro kostku
    private final Col barva = new Col(255,0,0);

    public Cube(List<Vertex> vertexBuffer, List<Integer> indexBuffer, List<Part> partBuffer) {
        super(vertexBuffer, indexBuffer, partBuffer);
        createCube();
    }

    private void createCube() {
        //vertices
        vertexBuffer.add(new Vertex(new Point3D(0,0,0), barva)); //0
        vertexBuffer.add(new Vertex(new Point3D(0,edge,0),barva)); //1
        vertexBuffer.add(new Vertex(new Point3D(edge,edge,0),barva)); //2
        vertexBuffer.add(new Vertex(new Point3D(edge,0,0),barva)); //3
        vertexBuffer.add(new Vertex(new Point3D(0,0,edge),barva)); //4
        vertexBuffer.add(new Vertex(new Point3D(0,edge,edge),barva)); //5
        vertexBuffer.add(new Vertex(new Point3D(edge,0,edge),barva)); //6
        vertexBuffer.add(new Vertex(new Point3D(edge,edge,edge),barva)); //7

        //TODO indices - triangles

        //TODO indices - lines

        //TODO parts to partBuffer

    }

    public int getEdge() {
        return edge;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }
}
