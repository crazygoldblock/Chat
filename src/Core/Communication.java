package Core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Communication {
    public static boolean SendPacket(ObjectOutputStream out, Packet p) {
        try {
            out.writeByte(-1);
            out.writeObject(p);
            out.flush();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public static Packet ReadPacketNonBlocking(ObjectInputStream in) {
        try {
            if (in.available() > 0) {
                in.readByte();
                return (Packet)in.readObject();
            }
        } catch (Exception e) { }
        return null;
    }   
    public static Packet ReadPacket(ObjectInputStream in) {
        try {
            in.readByte();
            return (Packet)in.readObject();
        } catch (Exception e) { }
        return null;
    }
}
