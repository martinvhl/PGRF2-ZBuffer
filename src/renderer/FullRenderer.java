package renderer;

import model.Part;
import model.Solid;
import model.TopologyType;
import model.Vertex;
import rasterize.DepthBuffer;
import rasterize.ImageBuffer;
import transforms.*;
import java.util.List;
import java.util.Optional;

public class FullRenderer implements GPURenderer {

    private final ImageBuffer imageBuffer;
    private final DepthBuffer depthBuffer;

    private Mat4 model;
    private Mat4 view;
    private Mat4 projection;

    private boolean fillLines = true;
    private boolean onlyWireframe = false;

    public FullRenderer(ImageBuffer imageBuffer) {
        this.imageBuffer = imageBuffer;
        depthBuffer = new DepthBuffer(imageBuffer.getWidth(),imageBuffer.getHeight());
    }

    /**
     * Metoda prochází tělesa scény a postupně je vykresluje
     * @param solids Seznam těles získaný ze scény
     */
    public void render(List<Solid> solids) {
        for (Solid s:solids) {
            model = s.getModelMatrix();
            draw(s.getPartBuffer(),s.getIndexBuffer(),s.getVertexBuffer(),s.isOnlyWF());
        }
    }

    /**
     * Metodě předáme z tělesa scény potřebné parametry a ta je dále zpracovává podle zobrazovacího řetězce
     * @param partsBuffer PartBuffer předaný právě vykreslovaným tělesem
     * @param indexBuffer IndexBuffer předaný právě vykreslovaným tělesem
     * @param vertexBuffer VertexBuffer předaný právě vykreslovaným tělesem
     * @param onlyWireframe Uživatelem zvolené nastavení pro vykreslení pouhého drátového modelu
     */
    @Override
    public void draw(List<Part> partsBuffer, List<Integer> indexBuffer, List<Vertex> vertexBuffer, boolean onlyWireframe) {
        for (Part part : partsBuffer) {
            TopologyType type = part.getTyp();
            int start = part.getStart();
            int count = part.getCount();

            if (type == TopologyType.TRIANGLE && !onlyWireframe && !isOnlyWireframe()) {
                for (int i = start; i < start + count*3; i+=3) {
                    int index1 = indexBuffer.get(i);
                    int index2 = indexBuffer.get(i+1);
                    int index3 = indexBuffer.get(i+2);
                    Vertex vertex1 = vertexBuffer.get(index1);
                    Vertex vertex2 = vertexBuffer.get(index2);
                    Vertex vertex3 = vertexBuffer.get(index3);
                    prepareTriangle(vertex1,vertex2,vertex3);
                }
            } else if (type == TopologyType.LINE && (isFillLines() || isOnlyWireframe())) {
                for (int i = start; i < start+count*2 ; i+=2) {
                    int index1 = indexBuffer.get(i);
                    int index2 = indexBuffer.get(i+1);

                    Vertex vertex1 = vertexBuffer.get(index1);
                    Vertex vertex2 = vertexBuffer.get(index2);

                    prepareLine(vertex1,vertex2);
                }
            }
        }
    }

    /**
     * Metoda pro přípravu linky k vykreslení - jak celkový wireframe, tak částečný s řešenou z-viditelností
     * @param vertex1 První vertex
     * @param vertex2 Druhý vertex
     */
    private void prepareLine(Vertex vertex1, Vertex vertex2) {
        Vertex a = new Vertex(vertex1.getPoint().mul(model).mul(view).mul(projection),vertex1.getColor());
        Vertex b = new Vertex(vertex2.getPoint().mul(model).mul(view).mul(projection),vertex2.getColor());

        if (a.getX() > a.getW() && b.getX() > b.getW()) return;
        if (a.getY() > a.getW() && b.getY() > b.getW()) return;
        if (a.getZ() < 0 && b.getZ() < 0) return;
        if (a.getX() < -a.getW() && b.getX() < -b.getW()) return;
        if (a.getY() < -a.getW() && b.getY() < -b.getW()) return;
        if (a.getZ() < -a.getW() && b.getZ() < -b.getW()) return;

        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a=b;
            b=temp;
        }

        if (a.getZ() < 0) {
            return;
        } else if (b.getZ() < 0) {
            double t12 = (0-a.getZ())/(b.getZ()-a.getZ());
            Vertex vAB = a.mul(1-t12).add(b.mul(t12));
            drawLine(a,vAB);
        } else {
            drawLine(a,b);
        }
    }

    /**
     * Metoda pro vykreslování linky - celkový nebo částečný wireframe dle nastavení programu
     * @param a První zpracovaný vertex
     * @param b Druhý zpracovaný vertex
     */
    private void drawLine(Vertex a, Vertex b) {
        Optional<Vec3D> o1 = a.getPoint().dehomog();
        if (o1.isEmpty()) return;

        Optional<Vec3D> o2 = b.getPoint().dehomog();
        if (o2.isEmpty()) return;

        double x1 = o1.get().getX();
        double y1 = o1.get().getY();
        double X1W = 0.5 * (imageBuffer.getWidth() - 1) * (x1 + 1);
        double Y1W = 0.5 * (imageBuffer.getHeight() - 1) * (1 - y1);

        double x2 = o2.get().getX();
        double y2 = o2.get().getY();
        double X2W = 0.5 * (imageBuffer.getWidth() - 1) * (x2 + 1);
        double Y2W = 0.5 * (imageBuffer.getHeight() - 1) * (1 - y2);

        o1 = Optional.of(new Vec3D(X1W, Y1W, o1.get().getZ()));
        o2 = Optional.of(new Vec3D(X2W, Y2W, o2.get().getZ()));

        Vertex newV1 = new Vertex(new Point3D(o1.get()), a.getColor());
        Vertex newV2 = new Vertex(new Point3D(o2.get()), b.getColor());

        if (newV1.getY() > newV2.getY()) {
            Vertex temp = newV1;
            newV1 = newV2;
            newV2 = temp;
        }
        //možné řešení pro nespojité linie při rasterizaci stejné jako u výplní - použít zabudovanou metodu třídy Graphics
        if (onlyWireframe) {
            imageBuffer.getImg().getGraphics().drawLine((int) X1W, (int) Y1W, (int) X2W, (int) Y2W);
        } else {
            //zde pokračování pro případ, že je třeba řešit viditelnost - horší vykreslení linií
            long startAB = (long) Math.max(Math.ceil(newV1.getY()), 0);
            double endAB = Math.min(newV2.getY(), imageBuffer.getHeight() - 1);

            for (long y = startAB; y <= endAB; y++) {
                double s12 = (y - newV1.getY()) / (newV2.getY() - newV1.getY());
                Vertex v12 = newV1.mul(1 - s12).add(newV2.mul(s12));

                if (v12.getX() < 0 || v12.getX() > imageBuffer.getWidth()) {
                    return;
                } else {
                    drawPixel((int) v12.getX(), (int) v12.getY(), v12.getZ(), v12.getColor());
                }
            }
        }
    }

    /**
     * Tato metoda implementuje část zobrazovacího řetězce
     * @param vertex1 Z metody draw() předaný vertex ke zpracování
     * @param vertex2 Z metody draw() předaný vertex ke zpracování
     * @param vertex3 Z metody draw() předaný vertex ke zpracování
     */
    private void prepareTriangle(Vertex vertex1, Vertex vertex2, Vertex vertex3) {
        //1. transformace vrcholů
        Vertex a = new Vertex(vertex1.getPoint().mul(model).mul(view).mul(projection),vertex1.getColor());
        Vertex b = new Vertex(vertex2.getPoint().mul(model).mul(view).mul(projection),vertex2.getColor());
        Vertex c = new Vertex(vertex3.getPoint().mul(model).mul(view).mul(projection),vertex3.getColor());

        //2. ořezání
        if (a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) return;
        if (a.getY() > a.getW() && b.getY() > b.getW() && c.getY() > c.getW()) return;
        if (a.getZ() < 0 && b.getZ() < 0 && c.getZ() < 0) return;
        if (a.getX() < -a.getW() && b.getX() < -b.getW() && b.getX() < -b.getW()) return;
        if (a.getY() < -a.getW() && b.getY() < -b.getW() && c.getY() < -c.getW()) return;
        if (a.getZ() < -a.getW() && b.getZ() < -b.getW() && c.getZ() < -c.getW()) return;

        //seřadíme podle z-souřadnice, abychom mohli provést interpolaci hodnot
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a=b;
            b=temp;
        }
        if (a.getZ() < c.getZ()) {
            var temp = a;
            a = c;
            c = temp;
        }
        if (b.getZ() < c.getZ()) {
            var temp = c;
            c=b;
            b=temp;
        }

        if (a.getZ() < 0) {
            return;
        } else if (b.getZ() < 0) {
            double t12 = (0-a.getZ())/(b.getZ()-a.getZ());
            Vertex vAB = a.mul(1-t12).add(b.mul(t12));

            double t13 = (0-a.getZ())/(c.getZ()-a.getZ());
            Vertex vAC = a.mul(1-t13).add(c.mul(t13));

            drawTriangle(a,vAB,vAC);

        } else if (c.getZ() < 0) {
            double t23 = (0-b.getZ()/(c.getZ()-b.getZ()));
            Vertex vBC = (b.mul(1-t23).add(c.mul(t23)));

            double t13 = (0-a.getZ())/(c.getZ() - a.getZ());
            Vertex vAC = a.mul(1-t13).add(c.mul(t13));

            drawTriangle(a,b,vBC);
            drawTriangle(a,vBC,vAC);

        } else {
            drawTriangle(a,b,c);
        }
    }

    /**
     * Metoda získá vertexy z metody prepareTriangle() a pokračuje ve zpracovávání
     * @param a Vertex získaný z prepareTriangle()
     * @param b Vertex získaný z prepareTriangle()
     * @param c Vertex získaný z prepareTriangle()
     */
    private void drawTriangle(Vertex a, Vertex b, Vertex c) {
        Optional<Vec3D> o1 = a.getPoint().dehomog();
        if (o1.isEmpty()) return;

        Optional<Vec3D> o2 = b.getPoint().dehomog();
        if (o2.isEmpty()) return;

        Optional<Vec3D> o3 = c.getPoint().dehomog();
        if(o3.isEmpty()) return;

        //transformace do okna (width,height)
        double x1 = o1.get().getX();
        double y1 = o1.get().getY();
        double X1W = 0.5*(imageBuffer.getWidth()-1) * (x1+1);
        double Y1W = 0.5*(imageBuffer.getHeight()-1) * (1-y1);

        double x2 = o2.get().getX();
        double y2 = o2.get().getY();
        double X2W = 0.5*(imageBuffer.getWidth()-1) * (x2+1);
        double Y2W = 0.5*(imageBuffer.getHeight()-1) * (1-y2);

        double x3 = o3.get().getX();
        double y3 = o3.get().getY();
        double X3W = 0.5*(imageBuffer.getWidth()-1) * (x3+1);
        double Y3W = 0.5*(imageBuffer.getHeight()-1) * (1-y3);

        o1 = Optional.of(new Vec3D(X1W,Y1W,o1.get().getZ()));
        o2 = Optional.of(new Vec3D(X2W,Y2W,o2.get().getZ()));
        o3 = Optional.of(new Vec3D(X3W,Y3W,o3.get().getZ()));

        Vertex newV1 = new Vertex(new Point3D(o1.get()),a.getColor());
        Vertex newV2 = new Vertex(new Point3D(o2.get()),b.getColor());
        Vertex newV3 = new Vertex(new Point3D(o3.get()),c.getColor());

        if (newV1.getY() > newV2.getY()) {
            Vertex temp = newV1;
            newV1 = newV2;
            newV2 = temp;
        }
        if (newV2.getY() > newV3.getY()) {
            Vertex temp = newV2;
            newV2 = newV3;
            newV3 = temp;
        }
        if (newV1.getY() > newV2.getY()) {
            Vertex temp = newV1;
            newV1 = newV2;
            newV2 = temp;
        }
        //A=>B
        long startAB = (long) Math.max(Math.ceil(newV1.getY()), 0);
        double endAB = Math.min(newV2.getY(), imageBuffer.getHeight() - 1);
        for (long y = startAB; y <= endAB; y++) {
            double s12 = (y- newV1.getY()) / (newV2.getY() - newV1.getY());
            Vertex v12 = newV1.mul(1-s12).add(newV2.mul(s12));

            double s13 = (y- newV1.getY()) / (newV3.getY() - newV1.getY());
            Vertex v13 = newV1.mul(1-s13).add(newV3.mul(s13));

            fillLine(y,v12,v13);
        }
        //B=>C
        long startBC = (long) Math.max(Math.ceil(newV2.getY()),0);
        double endBC = Math.min(newV3.getY(), imageBuffer.getHeight()-1);
        for (long y = startBC; y <= endBC; y++) {
            double s23 = (y-newV2.getY()) / (newV3.getY()-newV2.getY());
            Vertex v23 = newV2.mul(1-s23).add(newV3.mul(s23));

            double s13 = (y- newV1.getY()) / (newV3.getY() - newV1.getY());
            Vertex v13 = newV1.mul(1-s13).add(newV3.mul(s13));

            fillLine(y,v23,v13);
        }
    }

    /**
     * Metoda implementuje část algoritmu scan-line pro vyplňování trojúhelníků
     * @param y Parametr y určuje kde se zrovna nacházíme
     * @param a První vertex k interpolaci
     * @param b Druhý vertex k interpolaci
     */
    private void fillLine(long y, Vertex a, Vertex b) {
        if (a.getX() > b.getX()) {
            Vertex temp = a;
            a = b;
            b=temp;
        }
        long start = Math.round(Math.max(0,a.getX()));
        long end = Math.round(Math.min(b.getX(), imageBuffer.getWidth() -1));

        for (long x = start; x <= end; x++) {
            double t = (x-a.getX()) / (b.getX() - a.getX());
            Vertex finalOne = a.mul(1-t).add(b.mul(t));
            drawPixel((int)x, (int)y, finalOne.getZ(), finalOne.getColor());
        }
    }

    /**
     * Metoda pro samotné vykreslení a kontrolu viditelnosti
     * @param x Parametr x z vertexu
     * @param y Parametr y z vertexu
     * @param z Parametr z z vertexu
     * @param color Parametr barvy z vertexu
     */
    private void drawPixel(int x, int y, double z, Col color) {
        Optional<Double> depthBufferElement = depthBuffer.getElement(x, y);
        if (depthBufferElement.isEmpty()) return;

        if (z < depthBufferElement.get()) {
            imageBuffer.setElement(x,y,color);
            depthBuffer.setElement(x,y,z);
        }
    }

    @Override
    public void clear() {
        imageBuffer.clear();
        depthBuffer.clear();
    }

    @Override
    public void setModel(Mat4 model) {
        this.model = model;
    }

    @Override
    public void setView(Mat4 view) {
        this.view = view;
    }

    @Override
    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }

    public boolean isFillLines() {
        return fillLines;
    }

    public void setFillLines(boolean fillLines) {
        this.fillLines = fillLines;
    }

    public boolean isOnlyWireframe() {
        return onlyWireframe;
    }

    public void setOnlyWireframe(boolean onlyWireframe) {
        this.onlyWireframe = onlyWireframe;
    }
}
