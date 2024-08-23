package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import Core.Communication;
import Core.Packet;

public class ClientData {
    private Socket socket;
    private long lastPacketTime;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public ClientData(Socket socket) throws IOException {
        this.socket = socket;
        this.lastPacketTime = System.currentTimeMillis();
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }
    public void Close() throws IOException {
        in.close(); 
        out.close(); 
        socket.close();
    }
    public long TimeSinceLastPacket() {
        return System.currentTimeMillis() - lastPacketTime;
    }
    public void updateLastPacketTime() {
        lastPacketTime = System.currentTimeMillis();
    }
    public Packet ReadPacket() {
        return Communication.ReadPacketNonBlocking(in);
    }
    public boolean SendPacket(Packet p) {
        return Communication.SendPacket(out, p);
    }
}