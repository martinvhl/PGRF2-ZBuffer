package model;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<Solid> solids;

    public Scene() {
        solids = new ArrayList<>(4);
        initializeSolids();
    }

    private void initializeSolids() {
        Cube cube = new Cube();
        Pyramid pyramid = new Pyramid();
        Axis axis = new Axis();
        BezierSurface surface = new BezierSurface();

        solids.add(cube);
        solids.add(pyramid);
        solids.add(axis);
        solids.add(surface);
    }


    public List<Solid> getSolids() {
        return solids;
    }
}
