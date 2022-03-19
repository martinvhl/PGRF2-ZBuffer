package controllers;

import model.*;
import rasterize.ImageBuffer;
import renderer.FullRenderer;
import renderer.GPURenderer;
import transforms.*;
import view.Panel;
import view.Window;
import java.awt.event.*;

public class MyController {
    private final Window window;
    private final Panel panel;

    private Scene scene;
    private GPURenderer renderer;

    private Camera cam;
    private int w,h,oldX,oldY,xMove,yMove,zMove, camMoveX, camMoveY;
    private Mat4 translation, rotation, scaleChange, perspective, orthogonal, projection;
    private double xRot, yRot, zRot, enlarge, shrink;

    public MyController(Window window, Panel panel) {
        this.window = window;
        this.panel = panel;
        renderer = new FullRenderer(panel.getImageBuffer());
        scene = new Scene();
        initObjects(panel.getImageBuffer());
        initButtonListeners();
        initListeners();

        display();
    }

    /**
     * Metoda pro inicializaci tlačítek
     */
    private void initButtonListeners() {
        //translace +X
        window.getxTranslationPlus().addActionListener((e) -> initializeButton(new Mat4Transl(xMove,0,0)));
        //translace -X
        window.getxTranslationMinus().addActionListener((e) -> initializeButton(new Mat4Transl(-xMove,0,0)));
        //translace +Y
        window.getyTranslationPlus().addActionListener((e) -> initializeButton(new Mat4Transl(0,yMove,0)));
        //translace -Y
        window.getyTranslationMinus().addActionListener((e) -> initializeButton(new Mat4Transl(0,-yMove,0)));
        //translace +Z
        window.getzTranslationPlus().addActionListener((e) -> initializeButton(new Mat4Transl(0,0,zMove)));
        //translace -Z
        window.getzTranslationMinus().addActionListener((e) -> initializeButton(new Mat4Transl(0,0,-zMove)));
        //rotace +X
        window.getxRotationPlus().addActionListener((e) -> initializeButton(new Mat4RotX(xRot)));
        //rotace -X
        window.getxRotationMinus().addActionListener((e) -> initializeButton(new Mat4RotX(-xRot)));
        //rotace +Y
        window.getyRotationPlus().addActionListener((e) -> initializeButton(new Mat4RotY(yRot)));
        //rotace -Y
        window.getyRotationMinus().addActionListener((e) -> initializeButton(new Mat4RotY(-yRot)));
        //rotace +Z
        window.getzRotationPlus().addActionListener((e) -> initializeButton(new Mat4RotZ(zRot)));
        //rotace -Z
        window.getzRotationMinus().addActionListener((e) -> initializeButton(new Mat4RotZ(-zRot)));
        //enlarge
        window.getEnlargeSolid().addActionListener((e)-> initializeButton(new Mat4Scale(enlarge,enlarge,enlarge)));
        //shrink
        window.getShrinkSolid().addActionListener((e) -> initializeButton(new Mat4Scale(shrink,shrink,shrink)));

        //wireframe
        window.getWireframe().addActionListener((e) -> {
            if (window.getCubeBox().isSelected()) {
                for (Solid s : scene.getSolids()) {
                    if (s instanceof Cube) {
                        s.setOnlyWF(!s.isOnlyWF());
                    }
                }
            }
            if (window.getPyramidBox().isSelected()) {
                for (Solid s : scene.getSolids()) {
                    if (s instanceof Pyramid) {
                        s.setOnlyWF(!s.isOnlyWF());
                    }
                }
            }
            if (window.getBezierSurfaceBox().isSelected()) {
                for (Solid s : scene.getSolids()) {
                    if (s instanceof BezierSurface) {
                        s.setOnlyWF(!s.isOnlyWF());
                    }
                }
            }
            display();
        });
    }

    /**
     * Univerzální metoda pro inicializaci tlačítek transalce a rotace
     * @param mat4 Společná nadtřída matic pro translaci i rotaci
     */
    private void initializeButton(Mat4 mat4) {
        if (window.getCubeBox().isSelected()) {
            for (Solid s : scene.getSolids()) {
                if (s instanceof Cube) {
                    s.setModelMatrix(s.getModelMatrix().mul(mat4));
                }
            }
        }
        if (window.getPyramidBox().isSelected()) {
            for (Solid s : scene.getSolids()) {
                if (s instanceof Pyramid) {
                    s.setModelMatrix(s.getModelMatrix().mul(mat4));
                }
            }
        }
        if (window.getBezierSurfaceBox().isSelected()) {
            for (Solid s : scene.getSolids()) {
                if (s instanceof BezierSurface) {
                    s.setModelMatrix(s.getModelMatrix().mul(mat4));
                }
            }
        }
        display();
    }

    /**
     * Metoda pro základní inicializaci proměnných kontroleru
     * @param imageBuffer ImageBuffer z panelu
     */
    private void initObjects(ImageBuffer imageBuffer) {
        renderer = new FullRenderer(imageBuffer);
        cam = new Camera().withPosition(new Vec3D(9.3,6.7,9.5)) // kamera sleduje z pozice x
                .withAzimuth(3.6526721103838624).withZenith(-0.6145191465655627)
                .withFirstPerson(true).withRadius(60);
        w = imageBuffer.getWidth();
        h = imageBuffer.getHeight();
        oldX = w/2;
        oldY = h/2;
        camMoveX = 0;
        camMoveY = 0;
        xMove = 1;
        yMove = 1;
        zMove = 1;
        xRot = 0.2;
        yRot = 0.2;
        zRot = 0.2;
        enlarge = 1.1;
        shrink = 0.9;
        scene = new Scene();
        scene.getSolids().add(new Axis());
        translation = new Mat4Identity();
        rotation = new Mat4Identity();
        scaleChange = new Mat4Identity();
        perspective = new Mat4PerspRH(Math.PI/2,(double)h/w,0.1,50);
        orthogonal = new Mat4OrthoRH(50,50,0.1,50);
        projection = perspective;
    }

    /**
     * Metoda pro inicializaci listenerů myši i klávesnice
     */
    private void initListeners() {
        panel.addMouseMotionListener(new MouseAdapter() {

            //pohyb kamery tažením myší
             @Override
             public void mouseDragged(MouseEvent e) {
                 camMoveX = oldX - e.getXOnScreen();
                 camMoveY = oldY - e.getYOnScreen();
                 cam = cam.addAzimuth((Math.PI * camMoveX) / w);
                 cam = cam.addZenith((Math.PI * camMoveY) / h);
                 oldX = e.getXOnScreen();
                 oldY = e.getYOnScreen();
                 display();
             }
         });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) { //reset pozice kamery na výchozí hodnotu
                    cam = cam.withPosition(new Vec3D(9.3,6.7,9.5)).withZenith(-0.6145191465655627)
                            .withAzimuth(3.6526721103838624).withRadius(60);
                }
                if (e.getKeyCode() == KeyEvent.VK_W) { //kamera dopředu
                    cam = cam.forward(.5);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) { //kamera dozadu
                    cam = cam.backward(.5);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) { //kamera doleva
                    cam = cam.left(.5);
                }
                if (e.getKeyCode() == KeyEvent.VK_D) { //kamera doprava
                    cam = cam.right(.5);
                }
                if (e.getKeyCode() == KeyEvent.VK_O) { //ortogonální projekce
                    projection = orthogonal;
                }
                if (e.getKeyCode() == KeyEvent.VK_P) { //perspektivní projekce
                    projection = perspective;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) { //wireframe switcher
                    renderer.setOnlyWireframe(!renderer.isOnlyWireframe());
                    renderer.setFillLines(!renderer.isFillLines());
                }
                display();
            }
        });
    }

    /**
     * Metoda pro znovuvykreslení obsahu imagebufferu v panelu
     */
    private void display() {
        renderer.clear();
        renderer.setView(cam.getViewMatrix());
        renderer.setProjection(projection);
        renderer.render(scene.getSolids());
        panel.repaint();
        panel.grabFocus();
    }
}
