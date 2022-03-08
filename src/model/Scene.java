package model;

import renderer.GPURenderer;
import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4Identity;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<Solid> solids = new ArrayList<>(3);
    private Mat4 modelMatrix = new Mat4Identity();
    private Mat4 viewMatrix = new Mat4Identity();
    private Mat4 projectionMatrix = new Mat4Identity();
    private Camera cam;
    private GPURenderer renderer;
}
