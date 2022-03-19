package model;

import transforms.Col;
import transforms.Point3D;

import java.awt.*;
import java.util.List;

public class Cube extends Solid {

    //základní délka hrany pro kostku
    private int edge = 5;

    //TODO - upravit barvy (př. kolekce barev) pro barevnou interpolaci
    //vytvoříme červenou pro kostku
    private final Col barva = new Col(255,0,0);

    public Cube() {
        super();
        createCube();
    }

    private void createCube() {
        //vertices
        vertexBuffer.add(new Vertex(new Point3D(0,0,0), barva)); //0
        vertexBuffer.add(new Vertex(new Point3D(0,edge,0),new Col(Color.GREEN.getRGB()))); //1
        vertexBuffer.add(new Vertex(new Point3D(edge,edge,0),new Col(Color.BLUE.getRGB()))); //2
        vertexBuffer.add(new Vertex(new Point3D(edge,0,0),new Col(Color.CYAN.getRGB()))); //3
        vertexBuffer.add(new Vertex(new Point3D(0,0,edge),new Col(Color.pink.getRGB()))); //4
        vertexBuffer.add(new Vertex(new Point3D(0,edge,edge),new Col(Color.LIGHT_GRAY.getRGB()))); //5
        vertexBuffer.add(new Vertex(new Point3D(edge,edge,edge),new Col(Color.blue.getRGB()))); //6
        vertexBuffer.add(new Vertex(new Point3D(edge,0,edge),barva)); //7

        // indices - triangles - TRIANGLE LIST
        indexBuffer.add(0); indexBuffer.add(1); indexBuffer.add(2);
        indexBuffer.add(0); indexBuffer.add(2); indexBuffer.add(3);

        indexBuffer.add(0); indexBuffer.add(1); indexBuffer.add(5);
        indexBuffer.add(0); indexBuffer.add(5); indexBuffer.add(4);

        indexBuffer.add(1); indexBuffer.add(5); indexBuffer.add(6);
        indexBuffer.add(1); indexBuffer.add(6); indexBuffer.add(2);

        indexBuffer.add(0); indexBuffer.add(4); indexBuffer.add(7);
        indexBuffer.add(0); indexBuffer.add(7); indexBuffer.add(3);

        indexBuffer.add(4); indexBuffer.add(5); indexBuffer.add(6);
        indexBuffer.add(4); indexBuffer.add(6); indexBuffer.add(7);

        indexBuffer.add(3); indexBuffer.add(2); indexBuffer.add(6);
        indexBuffer.add(3); indexBuffer.add(6); indexBuffer.add(7);

        //indices - lines - LINE_LIST!!! (pro linestrip by to šlo zkrátit)
        indexBuffer.add(0); indexBuffer.add(1);
        indexBuffer.add(1); indexBuffer.add(2);
        indexBuffer.add(2); indexBuffer.add(3);
        indexBuffer.add(3); indexBuffer.add(0);

        indexBuffer.add(0); indexBuffer.add(4);
        indexBuffer.add(1); indexBuffer.add(5);
        indexBuffer.add(2); indexBuffer.add(6);
        indexBuffer.add(3); indexBuffer.add(7);

        indexBuffer.add(4); indexBuffer.add(5);
        indexBuffer.add(5); indexBuffer.add(6);
        indexBuffer.add(6); indexBuffer.add(7);
        indexBuffer.add(4); indexBuffer.add(7);

       // parts to partBuffer
        partBuffer.add(new Part(TopologyType.TRIANGLE,0,12));
        partBuffer.add(new Part(TopologyType.LINE,36,12));
    }

    public int getEdge() {
        return edge;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }
}
