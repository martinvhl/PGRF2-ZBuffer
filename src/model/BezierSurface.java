package model;

import transforms.*;

import java.util.Random;

public class BezierSurface extends Solid {
    private static final Mat4 bezierMatrix = Cubic.BEZIER;
    private static final int length = 2;

    //inicializace řídících bodů pro bikubickou plochu
    private final Point3D[] points = {
        new Point3D(0,0,0), new Point3D(length,length,0), new Point3D(length,2*length,0), new Point3D(0,3*length,0), //řídící body c11-c14
        new Point3D(0,0,length), new Point3D(-3*length,3*length,3*length), new Point3D(-3*length,3*length,3*length), new Point3D(0,3*length,length), //řídící body c21-c24
        new Point3D(0,0,length), new Point3D(-3*length,3*length,3*length), new Point3D(-3*length,3*length,3*length), new Point3D(0,3*length,length), //řídící body c31-c34
        new Point3D(0,0,3*length), new Point3D(length,length,3*length), new Point3D(length,2*length,3*length), new Point3D(0,3*length,3*length) //řídící body c41-c44
    };

    //barvy vertexů jsou náhodně a následně interpolované
    private final Random colorRandomizer = new Random();

    public BezierSurface() {
        super();
        createBezierSurface();
    }

    private void createBezierSurface() {
        //vertices
        Bicubic bikubika = new Bicubic(bezierMatrix, points);
        for (double u = 0; u < 1.1; u+=0.1) {
            Col barva = new Col(colorRandomizer.nextInt(255), colorRandomizer.nextInt(255), colorRandomizer.nextInt(255));
            for (double v = 0; v < 1.1; v+=0.1) {
                vertexBuffer.add(new Vertex(bikubika.compute(u,v), barva));
            }
        }
        //indices - triangles 1/2
        //vytvoří síť trojúhelníků s trojúhelníkovými mezerami
        for (int k = 0; k < 11; k++) {
            for (int i = k*12; i < k*12+11; i++) {
                indexBuffer.add(i);
                indexBuffer.add(i+1);
                indexBuffer.add(i+1+12);
            }
        }
        //indices - triangles 2/2
        //zaplníme mezery stejným postupem jen z opačného konce
        for (int k = 132; k >=12; k-=12) {
            for (int i = 0; i < 11; i++) {
                indexBuffer.add(k+i);
                indexBuffer.add(k+i+1);
                indexBuffer.add(k+i-12);
            }
        }

        //indices - lines 1/2
        for (int k = 0; k < 11; k++) {
            for (int i = k*12; i < k*12+11; i++) {
                indexBuffer.add(i);
                indexBuffer.add(i+1);
                indexBuffer.add(i+1);
                indexBuffer.add(i+1+12);
                indexBuffer.add(i+1+12);
                indexBuffer.add(i);
            }
        }
        //indices - lines 2/2
        for (int k = 132; k >=12; k-=12) {
            for (int i = 0; i < 11; i++) {
                indexBuffer.add(k+i);
                indexBuffer.add(k+i+1);
                indexBuffer.add(k+i+1);
                indexBuffer.add(k+i-12);
                indexBuffer.add(k+i-12);
                indexBuffer.add(k+i);
            }
        }
        //parts to partbuffer
        partBuffer.add(new Part(TopologyType.TRIANGLE,0, 242));
        partBuffer.add(new Part(TopologyType.LINE, 242*3, 242*3));
    }
}
