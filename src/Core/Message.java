package Core;

public class Message extends Packet{
    private String jmeno;
    private String zprava;
    public Message(String jmeno, String zprava) {
        super(1);
        this.jmeno = jmeno;
        this.zprava = zprava;
    }
    @Override
    public String toString() {
        return jmeno + " - " + zprava;
    }
    public String GetJmeno() {
        return jmeno;
    }
}