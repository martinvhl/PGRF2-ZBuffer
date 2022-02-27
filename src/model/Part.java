package model;

public class Part { // může se předělat na record - od Javy16 - jen weak class s gettery a constructorem
    private final TopologyType typ;
    private final int start; // alternativně offset
    private final int count; // počet entit daného typu

    public Part(TopologyType typ, int start, int count) {
        this.typ = typ;
        this.start = start;
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public int getCount() {
        return count;
    }

    public TopologyType getTyp() {
        return typ;
    }
}
