package model;

import java.util.List;

public abstract class Solid {
    List<Vertex> vertexBuffer;
    List<Integer> indexBuffer;
    List<Part> partBuffer;

    public Solid(List<Vertex> vertexBuffer, List<Integer> indexBuffer, List<Part> partBuffer) {
        this.indexBuffer = indexBuffer;
        this.vertexBuffer = vertexBuffer;
        this.partBuffer = partBuffer;
    }

    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public void setVertexBuffer(List<Vertex> vertexBuffer) {
        this.vertexBuffer = vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public void setIndexBuffer(List<Integer> indexBuffer) {
        this.indexBuffer = indexBuffer;
    }

    public List<Part> getPartBuffer() {
        return partBuffer;
    }

    public void setPartBuffer(List<Part> partBuffer) {
        this.partBuffer = partBuffer;
    }
}
