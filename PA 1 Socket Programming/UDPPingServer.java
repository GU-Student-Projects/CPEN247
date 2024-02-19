import java.net.*;
import java.io.*;

public class UDPPingServer {

    public static void main(String[] args) {
        DatagramSocket serverPing;
        DatagramPacket query;
        DatagramPacket response = new DatagramPacket(new byte[128], 128);
        int serverPort = 12000;
        double rand;
        try {
            serverPing = new DatagramSocket(serverPort);
            while (true) {
                query = new DatagramPacket(new byte[128], 128);
                serverPing.receive(query);
                rand = 10 * Math.random();
                if (rand < 3) {
                    continue; // Simulate packet loss
                }
                InetAddress clientIP = query.getAddress();
                int clientPort = query.getPort();

                // Printing received message
                String receivedMessage = new String(query.getData(), 0, query.getLength());
                System.out.println(receivedMessage);

                // Sending response
                response.setData(query.getData());
                response.setLength(query.getLength());
                response.setAddress(clientIP);
                response.setPort(clientPort);
                serverPing.send(response);
            }
        } catch (IOException e) {
            System.out.println("Could not establish server!");
        }
    }
}
