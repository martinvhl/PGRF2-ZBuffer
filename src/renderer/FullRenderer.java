package renderer;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Mat4;
import transforms.Mat4Identity;

import java.util.List;

public class FullRenderer implements GPURenderer {

    private Mat4 model = new Mat4Identity();
    private Mat4 view = new Mat4Identity();
    private Mat4 projection = new Mat4Identity();

    public FullRenderer(Mat4 model, Mat4 view, Mat4 projection) {
        this.model = model;
        this.view = view;
        this.projection = projection;
    }

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
                    prepareTriangle(vertex1,vertex2,vertex3);
                }
            } else if (type == TopologyType.LINE) {
                //todo HW
            }
        }
    }

    private void prepareTriangle(Vertex vertex1, Vertex vertex2, Vertex vertex3) {
        //1. transformace vrcholů
        Vertex a = new Vertex(vertex1.getPoint().mul(model).mul(view).mul(projection),vertex1.getColor());
        Vertex b = new Vertex(vertex2.getPoint().mul(model).mul(view).mul(projection),vertex2.getColor());
        Vertex c = new Vertex(vertex3.getPoint().mul(model).mul(view).mul(projection),vertex3.getColor());

        //2. ořezání
        //nejprve přísný fastclip - optimalizační krok, odstraníme to co by stejně nebylo vidět (kvůli výpočetně náročným operacím co následují - interpolace a pod.)
        //toto je první krok pro zakomentování v případě problému s vykreslováním, mělo by to fungovat i bez toho, jen pomaleji
        if((a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) ||
           (a.getY() > a.getW() && b.getY() > b.getW() && c.getY() > c.getW()) ||
           (a.getZ() > 0 && b.getZ() > 0 && c.getZ() > 0) ||
           (a.getX() < -a.getW() && b.getX() < -b.getW() && b.getX() < -b.getW()) ||
           (a.getY() < -a.getW() && b.getY() < -b.getW() && c.getY() < -c.getW()) ||
           (a.getZ() < -a.getW() && b.getZ() < -b.getW() && c.getZ() < -c.getW()))
                return;
        //seřadíme podle z-souřadnice, abychom mohli provést interpolaci hodnot - max. 3x prohodíme, ještě jde o malý počet a můžeme to dělat ručně a ne přes metody Javy - sort, Comparator
        if (a.getZ() < b.getZ()) {
            //var temp = a; zápis od Javy 11, typ si to zpětně domyslí samo, jako v JS !!!
            Vertex temp = a;
            a=b;
            b=temp; // prohodili jsme reference
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
        /*
            alternativa pro více než 3 - vytvoříme List<Vertex>
            pozor - Collections.sort() mění původní kolekci (return void)
            List<Vertex> sortedVertices = Stream.of(a,b,c).sorted(new Comparator.comparingDouble(Vertex::getZ).toList()
            })
         */
        /*
        chceme spočítat vrcholy, kdy strany trojúhelníka protínají z= 0 - lineární interpolace s z = 0 (slide "Ořezání trojúhelníku před dehomogenizací")
        t=(0-v1.z)/(v2.z-v1.z)
        va = v1*(1-t) + v2*t -> va = v1.mul(1-t).add(v2.mul(t))
        stejným způsobem se dopočítá x, y, barva, souřadnice do textury apod.
         */
        if (a.getZ() < 0) {
            return;
        } else if (b.getZ() < 0) {
            double t12 = (0-a.getZ())/(b.getZ()-a.getZ());
            Vertex vAB = a.mul(1-t12).add(b.mul(t12));

            double t13 = (0-a.getZ())/(c.getZ()-a.getZ());
            Vertex vAC = a.mul(1-t13).add(c.mul(t13));

            drawTriangle(a,vAB,vAC);

        } else if (c.getZ() < 0) {
            //TODO
            double t23 = (0-b.getZ()/(c.getZ()-b.getZ()));
            Vertex vBC = (b.mul(1-t23).add(c.mul(t23)));
        } else {
            drawTriangle(a,b,c);
        }

    }

    private void drawTriangle(Vertex a, Vertex b, Vertex c) {
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
