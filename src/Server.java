import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            // Start server and wait for clients to connect
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server is running and waiting for clients to connect...");
            Socket client1Socket = serverSocket.accept();
            System.out.println("Client 1 connected.");
            Socket client2Socket = serverSocket.accept();
            System.out.println("Client 2 connected.");
            Socket client3Socket = serverSocket.accept();
            System.out.println("Client 3 connected.");

            // Set up input/output streams for each client
            BufferedReader client1In = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()));
            PrintWriter client1Out = new PrintWriter(client1Socket.getOutputStream(), true);
            BufferedReader client2In = new BufferedReader(new InputStreamReader(client2Socket.getInputStream()));
            PrintWriter client2Out = new PrintWriter(client2Socket.getOutputStream(), true);
            BufferedReader client3In = new BufferedReader(new InputStreamReader(client3Socket.getInputStream()));
            PrintWriter client3Out = new PrintWriter(client3Socket.getOutputStream(), true);

            // Start the conversation
            while (true) {
                // Client 1 sends a message
                if (client1In.ready()) {
                    String message = client1In.readLine();
                    System.out.println("Client 1: " + message);
                    client2Out.println("Client 1: " + message);
                    client3Out.println("Client 1: " + message);
                }
                // Client 2 sends a message
                if (client2In.ready()) {
                    String message = client2In.readLine();
                    System.out.println("Client 2: " + message);
                    client1Out.println("Client 2: " + message);
                    client3Out.println("Client 2: " + message);
                }
                // Client 3 sends a message
                if (client3In.ready()) {
                    String message = client3In.readLine();
                    System.out.println("Client 3: " + message);
                    client1Out.println("Client 3: " + message);
                    client2Out.println("Client 3: " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

