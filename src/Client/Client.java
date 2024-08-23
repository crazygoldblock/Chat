package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Scanner;

import Core.*;

public class Client extends Thread{
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean ukoncit;

    public Client() {
        while (socket == null) {
            try {
                socket = new Socket("localhost", 7777);

                Thread.sleep(500);
            }
            catch (Exception e) { }
        }
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Připojen k serveru, nyní můžeš psát zprávy:");

        start();
    }
    @Override
    public void run() {
        while(!ukoncit) {
            try {
                Thread.sleep(1000);

                while(true) { 
                    Packet p = Communication.ReadPacketNonBlocking(in);

                    if (p == null) 
                        break;
                    
                    switch (p.GetTyp()) {
                        case 0:
                            Communication.SendPacket(out, new Ping());
                            break;
                        case 1:
                            Message m = (Message)p;
                            System.out.println(m);
                            break;
                        default:
                            //System.out.println("Neznámí typ packetu: " + p.GetTyp());
                    }   
                }
            } catch (InterruptedException e) { }
        }
    }
    private void Ukoncit() {
        ukoncit = true;
        try {
            join();
        } catch (InterruptedException e) { }
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        
        Scanner sc = new Scanner(System.in, "Cp852");

        String jmeno = "";

        System.out.println("Zadej svoje jméno:");

        while(jmeno.equals("")) {
            try {
                jmeno = sc.nextLine().trim();
            }
            catch (InputMismatchException e) { 
                sc.next();
            }
        }

        Client client = new Client();

        while(true) {
            String zprava;
            try {
                zprava = sc.nextLine().trim();

                if (zprava.equals("ukoncit"))
                    break;

                if (!zprava.equals(""))
                    Communication.SendPacket(client.out, new Message(jmeno, zprava));
            }
            catch (InputMismatchException e) { 
                sc.next();
            }
        }
        client.Ukoncit();
        sc.close(); 
    }
}
