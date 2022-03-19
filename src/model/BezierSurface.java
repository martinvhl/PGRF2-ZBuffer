package model;

import transforms.*;

import java.util.List;

public class BezierSurface extends Solid {
    private static final Mat4 bezierMatrix = Cubic.BEZIER;
    private static int length = 1;
    private final Point3D[] points = {
        new Point3D(0,0,0), new Point3D(length,length,0), new Point3D(length,2*length,0), new Point3D(0,3*length,0), //řídící body c11-c14
        new Point3D(0,0,length), new Point3D(length,length,length), new Point3D(length,2*length,length), new Point3D(0,3*length,length), //řídící body c21-c24
        new Point3D(0,0,2*length), new Point3D(length,length,2*length), new Point3D(length,2*length,2*length), new Point3D(0,3*length,2*length), //řídící body c31-c34
        new Point3D(0,0,3*length), new Point3D(length,length,3*length), new Point3D(length,2*length,3*length), new Point3D(0,3*length,3*length) //řídící body c41-c44
    };
    private Col barva = new Col(0,0,255);
    private Bicubic bikubika;

    public BezierSurface() {
        super();
        createBezierSurface();
    }

    private void createBezierSurface() {
        bikubika = new Bicubic(bezierMatrix,points);
        for (double u = 0; u < 1.1; u+=0.1) {
            for (double v = 0; v < 1.1; v+=0.1) {
                vertexBuffer.add(new Vertex(bikubika.compute(u,v),barva));
            }
        }
        //todo indexbuffer - triangles

        //todo indexbuffer - lines

        //todo partbuffer
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Col getBarva() {
        return barva;
    }

    public void setBarva(Col barva) {
        this.barva = barva;
    }
}
