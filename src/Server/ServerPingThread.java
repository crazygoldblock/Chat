package Server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Core.Ping;

public class ServerPingThread extends Thread{

    private ConcurrentHashMap<Integer, ClientData> clients;
    private boolean ukoncit;

    public ServerPingThread(ConcurrentHashMap<Integer, ClientData> clients) {
        this.clients = clients;

        start();
    }

    @Override
    public void run() {
        while(!ukoncit) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) { }
            
            for (Map.Entry<Integer, ClientData> entry : clients.entrySet()) {
                ClientData clientData = entry.getValue();
                if (clientData.TimeSinceLastPacket() > 13000 ) {
                    clients.remove(entry.getKey());
                    System.out.println("Klient odpojen");
                }
                else {
                    if (clientData.TimeSinceLastPacket() > 4500)
                        clientData.SendPacket(new Ping());
                }
            }
        }
    }
    public void Ukoncit() {
        interrupt();
        ukoncit = true;
    }
}
