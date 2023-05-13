import javax.swing.*;
import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

public class Verifier {
    public static void run() throws IOException, NoSuchAlgorithmException, SignatureException {
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


        ServerSocket serverSocket = new ServerSocket(3000);

        // Accept a connection from Client 1
        Socket client1Socket = serverSocket.accept();
        // Create a PrintWriter object for sending messages to Client 1
        PrintWriter outToClient1 = new PrintWriter(client1Socket.getOutputStream(), true);
        // Create a BufferedReader object for receiving messages from Client 1
        BufferedReader inFromClient1 = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()));

        String DIDRP = inFromClient1.readLine();
        System.out.println("Utilisateur authentifié en tant que propriétaire de l'identité numérique DID(U,RP)");

        //Calcul du pseudonyme et envoie a RP
        Hashtable<Integer, String>
                pseudonym = new Hashtable<Integer, String>();
        pseudonym.put(r, DIDRP);


        /*Verification du Sigma
        sign.initVerify(pk);
        sign.update(bytes);
        boolean bool = sign.verify(sigma);
        if(bool) {
            System.out.println("Signature verified");
        } else {
            System.out.println("Signature failed");
        }

        //Ou verification du h*/
        /*Verification du pseudonyme
        Hashtable<Integer, String>
        pseudonym = new Hashtable<Integer, String>();
        pseudonym.put(r, DIDRP);
        if(pseudonym==pseudonym) {
            System.out.println("Pseudo verified");
         } else {
            System.out.println("Pseudo failed");
         }*/

        //**Schnorr pour verifier la paire de clés**
        // create a challenge
        byte[] challenge = new byte[10000];
        ThreadLocalRandom.current().nextBytes(challenge);

        /* sign using the private key
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(sk);
        sig.update(challenge);
        byte[] signature = sig.sign();

        // verify signature using the public key
        sig.initVerify(pk);
        sig.update(challenge);

        boolean keyPairMatches = sig.verify(signature);
        if(keyPairMatches) {
            System.out.println("Paire verified");
        } else {
            System.out.println("Paire failed");
        }
        */

    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, SignatureException {
        Verifier srv = new Verifier();
        srv.run();
    }

}