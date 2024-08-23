package Core;

import java.io.Serializable;

public class Packet implements Serializable{

    private int typ;

    public Packet(int typ) {
        this.typ = typ;
    }
    public int GetTyp() {
        return typ;
    }
}
