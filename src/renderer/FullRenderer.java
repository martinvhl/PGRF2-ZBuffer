package renderer;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Mat4;

import java.util.List;

public class FullRenderer implements GPURenderer {

    //TODO constructor

    @Override
    public void draw(List<Part> partsBuffer, List<Integer> indexBuffer, List<Vertex> vertexBuffer) {
        for (Part part : partsBuffer) {
            TopologyType type = part.getTyp(); //topology type
            int start = part.getStart();
            int count = part.getCount();
            if (type == TopologyType.TRIANGLE) {
                for (int i = start; i < start + count*3; i+=3) {
                    int index1 = indexBuffer.get(i);
                    int index2 = indexBuffer.get(i+1);
                    int index3 = indexBuffer.get(i+2);
                    Vertex vertex1 = vertexBuffer.get(index1);
                    Vertex vertex2 = vertexBuffer.get(index2);
                    Vertex vertex3 = vertexBuffer.get(index3);
                    drawTriangle(vertex1,vertex2,vertex3);
                }
            } else if (type == TopologyType.LINE) {
                //todo
            }
        }
    }

    private void drawTriangle(Vertex vertex1, Vertex vertex2, Vertex vertex3) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void setModel(Mat4 model) {

    }

    @Override
    public void setView(Mat4 view) {

    }

    @Override
    public void setProjection(Mat4 projection) {

    }
}
