package rasterize;

import java.util.Optional;

public interface Raster<E> {

    Optional<E> getElement(int x, int y);
    void setElement(int x, int y, E value);

    void clear();

    void setClearValue(E value);

    int getWidth();
    int getHeight();
}
