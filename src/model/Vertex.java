package model;

import transforms.Col;
import transforms.Point3D;

public class Vertex {
    private final Point3D point;
    private final Col color;

    public Vertex(Point3D point, Col color) {
        this.point = point;
        this.color = color;
    }

    public Point3D getPoint() {
        return point;
    }

    public Col getColor() {
        return color;
    }

    public Vertex add(Vertex otherVertex) {
        return new Vertex(
                point.add(otherVertex.getPoint()),
                color.add(otherVertex.getColor())
        );
    }
    public Vertex mul(double t) { //násobení
        return new Vertex(point.mul(t),color.mul(t));
    }

    public double getX() {
        return point.getX();
    }
    public double getY() {
        return point.getY();
    }
    public double getZ() {
        return point.getZ();
    }
    public double getW() {
        return point.getW();
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "point=" + point +
                ", color=" + color +
                '}';
    }
}
