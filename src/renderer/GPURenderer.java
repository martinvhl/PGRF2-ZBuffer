package renderer;

import model.Part;
import model.Solid;
import model.Vertex;
import shader.Shader;
import transforms.Col;
import transforms.Mat4;

import java.util.List;

public interface GPURenderer {

    void render(List<Solid> solids);
    void draw(List<Part> partsBuffer, List<Integer> indexBuffer, List<Vertex> vertexBuffer, boolean onlyWireframe);
    void clear();
    void setModel(Mat4 model);
    void setView(Mat4 view);
    void setProjection(Mat4 projection);
    void setFillLines(boolean switcher);
    boolean isFillLines();
    boolean isOnlyWireframe();
    void setOnlyWireframe(boolean onlyWireframe);
}
