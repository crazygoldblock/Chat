package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Server extends Thread{ 
    private ConcurrentHashMap<Integer, ClientData> clients = new ConcurrentHashMap<>();
    private ServerReadThread readThread;
    private boolean ukoncit;
    private ServerPingThread pingThread;

    public Server() {
        readThread = new ServerReadThread(clients);

        pingThread = new ServerPingThread(clients);
        
        start();

        System.out.println("Server spuštěn");
    }
    @Override
    public void run() {
        int klientId = 1;

        while(!ukoncit) {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(7777);
                serverSocket.setSoTimeout(1000);
    
                Socket socket = serverSocket.accept();

                ClientData c = new ClientData(socket);

                clients.put(klientId++, c);
        
                System.out.println("Klient připojen");
            } 
            catch (SocketTimeoutException e) { }
            catch (IOException e) {
                System.out.println("Chyba při připojování klienta");
                e.printStackTrace();
            }

            try {
                serverSocket.close();
            } catch (Exception e) { }
        }
    }
    private void Ukoncit() {
        try {
            readThread.Ukoncit();
            readThread.join();
            pingThread.Ukoncit();
            pingThread.join();
            ukoncit = true;
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Integer, ClientData> entry : clients.entrySet()) {
            try {
                entry.getValue().Close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {

        Server server = new Server();

        Scanner sc = new Scanner(System.in);

        sc.nextLine();

        server.Ukoncit();

        sc.close();
    }
}

