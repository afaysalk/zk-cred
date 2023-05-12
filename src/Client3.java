import java.io.*;
import java.net.*;

public class Client3 {
    public static void main(String[] args) throws IOException {
        // Connect to the server
        Socket serverSocket = new Socket("localhost", 5000);

        // Create a PrintWriter object for sending messages to the server
        PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);

        // Create a BufferedReader object for receiving messages from the server
        BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        // Send message y to Client 2
        out.println("y");

        // Close the connection
        out.close();
        in.close();
        serverSocket.close();
    }
}
