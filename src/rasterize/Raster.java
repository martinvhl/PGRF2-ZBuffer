package rasterize;

import java.util.Optional;

public interface Raster<E> {

    default boolean isInsideTheBounds(int x, int y){//jsme v rozsahu šířky/výšky?
        return (x > 0 && x < getWidth()) && (y > 0 && y < getHeight());
    }

    Optional<E> getElement(int x, int y); //Optional pro řešení null hodnot - vynucenný null-check
    void setElement(int x, int y, E value);

    void clear(); //hodnota bez parametru - netřeba volat žádnou hodnotu, když bude nějaká implicitní

    void setClearValue(E value); // nastavení té hodnoty pro clear (pro případ, že by bylo třeba měnit)

    int getWidth();
    int getHeight();
}
