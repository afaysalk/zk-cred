import javax.swing.*;
import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class Verifier {
    public static void run() throws IOException, NoSuchAlgorithmException {
        // Connect to the server
        Socket Client2Socket = new Socket("localhost", 5000);
        // Create a PrintWriter object for sending messages to the server
        PrintWriter outToClient2 = new PrintWriter(Client2Socket.getOutputStream(), true);
        // Create a BufferedReader object for receiving messages from the server
        BufferedReader inFromClient2 = new BufferedReader(new InputStreamReader(Client2Socket.getInputStream()));



        String DID = inFromClient2.readLine();
        System.out.println(DID);
        String pk =inFromClient2.readLine();
        int r = Integer.parseInt(inFromClient2.readLine());
        byte[] salted_hash = inFromClient2.readLine().getBytes();

        String[] columnIssuer = {"DID", "DID:pk(U,I)","r","Salted hash"};
        Object[][] dataIssuer = {
                {DID, pk, r, salted_hash} //,
        };
        JTable tableIssuer = new JTable(dataIssuer, columnIssuer);
        for(int row = 0; row < tableIssuer.getRowCount(); row++) {

            for(int column = 0; column < tableIssuer.getColumnCount(); column++) {
                System.out.print(tableIssuer.getColumnName(column) + ": ");
                System.out.println(tableIssuer.getValueAt(row, column));
            }
            System.out.println(""); // Add line space
        }


        // Close the connection
        outToClient2.close();
        inFromClient2.close();
        Client2Socket.close();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Verifier srv = new Verifier();
        srv.run();
    }

}
