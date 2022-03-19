package model;

import transforms.Col;
import transforms.Point3D;

import java.awt.*;
import java.util.List;

public class Pyramid extends Solid {
    private int baseEdge = 6; //pro Point3D - 6/2 -> 3 -> vrchol pyramidy pak má (height,3,3) i přes varování o dělení v integer kontextu
    private int height = 10;
    private Col barva = new Col(0,255,0);

    public Pyramid() {
        super();
        createPyramid();
    }

    private void createPyramid() {
        //vertices
        vertexBuffer.add(new Vertex(new Point3D(1,-1,0), new Col(Color.WHITE.getRGB()))); //0
        vertexBuffer.add(new Vertex(new Point3D(1,baseEdge-1,0),new Col(Color.RED.getRGB()))); //1
        vertexBuffer.add(new Vertex(new Point3D(1,baseEdge-1,baseEdge),new Col(Color.CYAN.getRGB()))); //2
        vertexBuffer.add(new Vertex(new Point3D(1,-1,baseEdge),new Col(Color.BLUE.getRGB()))); //3
        vertexBuffer.add(new Vertex(new Point3D(height, baseEdge /2,baseEdge/2),barva)); //4


        //indices - triangles
        indexBuffer.add(0); indexBuffer.add(1); indexBuffer.add(2);
        indexBuffer.add(0); indexBuffer.add(2); indexBuffer.add(3);

        indexBuffer.add(0); indexBuffer.add(1); indexBuffer.add(4);
        indexBuffer.add(1); indexBuffer.add(2); indexBuffer.add(4);
        indexBuffer.add(2); indexBuffer.add(3); indexBuffer.add(4);
        indexBuffer.add(0); indexBuffer.add(3); indexBuffer.add(4);

        //indices - lines
        indexBuffer.add(0); indexBuffer.add(1);
        indexBuffer.add(1); indexBuffer.add(2);
        indexBuffer.add(2); indexBuffer.add(3);
        indexBuffer.add(0); indexBuffer.add(3);
        indexBuffer.add(0); indexBuffer.add(4);
        indexBuffer.add(1); indexBuffer.add(4);
        indexBuffer.add(2); indexBuffer.add(4);
        indexBuffer.add(3); indexBuffer.add(4);

        //parts to partbuffer
        partBuffer.add(new Part(TopologyType.TRIANGLE,0,6));
        partBuffer.add(new Part(TopologyType.LINE,18,8));
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
