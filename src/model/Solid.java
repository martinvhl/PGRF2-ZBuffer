package model;

import transforms.Mat4;
import transforms.Mat4Identity;
import java.util.ArrayList;
import java.util.List;

public class Solid {
    protected List<Vertex> vertexBuffer;
    protected List<Integer> indexBuffer;
    protected List<Part> partBuffer;
    protected Mat4 modelMatrix;
    protected boolean onlyWF = false;

    public Solid() {
        vertexBuffer = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        partBuffer = new ArrayList<>();
        modelMatrix = new Mat4Identity();
    }

    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }
    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }
    public List<Part> getPartBuffer() {
        return partBuffer;
    }
    public Mat4 getModelMatrix() {
        return modelMatrix;
    }
    public void setModelMatrix(Mat4 modelMatrix) {
        this.modelMatrix = modelMatrix;
    }
    public boolean isOnlyWF() {
        return onlyWF;
    }
    public void setOnlyWF(boolean onlyWF) {
        this.onlyWF = onlyWF;
    }
}
