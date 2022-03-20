package model;

import transforms.Col;
import transforms.Point3D;
import java.awt.*;

public class Axis extends Solid {

    public Axis() {
        super();
        createAxes();
    }

    private void createAxes() {
        //vertices
        vertexBuffer.add(new Vertex(new Point3D(0,0,0),new Col(Color.WHITE.getRGB()))); //0
        //pozn. délka 10 kvůli přehlednosti vůči velikosti ostatních těles
        int edge = 10;
        vertexBuffer.add(new Vertex(new Point3D(edge,0,0),new Col(Color.RED.getRGB()))); //1
        vertexBuffer.add(new Vertex(new Point3D(0, edge,0),new Col(Color.GREEN.getRGB()))); //2
        vertexBuffer.add(new Vertex(new Point3D(0,0, edge),new Col(Color.BLUE.getRGB()))); //3

        //indices
        indexBuffer.add(0); indexBuffer.add(1);
        indexBuffer.add(0); indexBuffer.add(2);
        indexBuffer.add(0); indexBuffer.add(3);

        //parts
        partBuffer.add(new Part(TopologyType.LINE,0,3));
    }
}
