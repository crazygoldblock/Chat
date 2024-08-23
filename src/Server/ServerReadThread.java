package Server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Core.Message;
import Core.Packet;

public class ServerReadThread extends Thread{

    private ConcurrentHashMap<Integer, ClientData> clients;
    private boolean ukoncit;

    @Override
    public void run() {
        while(!ukoncit) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { }
            
            for (Map.Entry<Integer, ClientData> entry : clients.entrySet()) {
                ClientData client = entry.getValue();
                while(true) {
                    Packet p = client.ReadPacket();
    
                    if (p == null) 
                        break;
    
                    client.updateLastPacketTime();
                    switch (p.GetTyp()) {
                        case 0:
                            break;
                        case 1:
                            System.out.println("Zpráva: " + (Message)p);
                            WriteMessageToAllUsers((Message)p, entry.getKey());
                            break;
                        default:
                            System.out.println("Neznámí typ packetu: " + p.GetTyp());
                    }   
                }
            }
        }
    }
    private void WriteMessageToAllUsers(Message m, Integer authorId) {
        for (Map.Entry<Integer, ClientData> entry : clients.entrySet()) {
            if (entry.getKey() != authorId)
                entry.getValue().SendPacket(m);
        }
    }
    public ServerReadThread(ConcurrentHashMap<Integer, ClientData> clients) {
        this.clients = clients;
        start();
    }
    public void Ukoncit() {
        ukoncit = true;
    }
}
