import java.net.*;
import java.io.*;
import java.util.Date;

public class UDPPingClient {
    public static void main(String[] args) {
        DatagramSocket clientPing = null;
        DatagramPacket query;
        DatagramPacket response = new DatagramPacket(new byte[128], 128);
        int serverPort = 12000;
        Date now = new Date();

        try {
            clientPing = new DatagramSocket();
            clientPing.setSoTimeout(1000); // Set the timeout to 1 second
            InetAddress serverAddress = InetAddress.getByName("localhost");

            StringBuilder messageBuilder = new StringBuilder();

            for (int i = 1; i <= 10; i++) {
                long sendTime = System.currentTimeMillis(); // Record the time before sending

                messageBuilder.setLength(0);
                messageBuilder.append("Ping ").append(i).append(" ").append(now.toString());
                String message = messageBuilder.toString();

                byte[] sendData = message.getBytes();
                query = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);

                // Sending the packet
                clientPing.send(query);

                // Receiving response
                try {
                    clientPing.receive(response);

                    long receiveTime = System.currentTimeMillis(); // Record the time after receiving
                    long rtt = receiveTime - sendTime;

                    String receivedMessage = new String(response.getData(), 0, response.getLength());
                    System.out.println("Received from server: " + receivedMessage);
                    System.out.println("RTT for Ping " + i + ": " + rtt + " ms");

                } catch (SocketTimeoutException e) {
                    System.out.println("Packet lost for Ping " + i);
                }

                // Sleep for 1 second between pings
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (clientPing != null) {
                clientPing.close();
            }
        }
    }
}
