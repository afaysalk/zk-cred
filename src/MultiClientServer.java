import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class MultiClientServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started");

            while (true) {
                Socket client1 = serverSocket.accept();
                System.out.println("Issuer connected");
                Issuer.run();

                Socket client2 = serverSocket.accept();
                System.out.println("Holder connected");
                Holder.run();

                Socket client3 = serverSocket.accept();
                System.out.println("Verifier connected");
                Verifier.run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private String clientName;

    public ClientHandler(Socket clientSocket, String clientName) {
        this.clientSocket = clientSocket;
        this.clientName = clientName;
    }

    public void run() {
        try {
            Scanner in = new Scanner(clientSocket.getInputStream());
            while (true) {
                String message = in.nextLine();
                System.out.println(clientName + " received: " + message);

                // Send the message to the other clients
                if (clientName.equals("Client 1")) {
                    sendMessageTo("Client 2", message);
                } else if (clientName.equals("Client 2")) {
                    sendMessageTo("Client 3", message);
                } else if (clientName.equals("Client 3")) {
                    sendMessageTo("Client 2", message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageTo(String clientName, String message) throws IOException {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 12345);
            Scanner in = new Scanner(socket.getInputStream());
            System.out.println(clientName + " connected to forward message");

            // Send the message to the specified client
            //socket.getOutputStream().println(message);
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
