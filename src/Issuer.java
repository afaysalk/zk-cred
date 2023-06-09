import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Base64;
import java.util.Random;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.security.SecureRandom;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.Provider.Service;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.MessageDigest;


public class Issuer {
    public static void run() throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        ServerSocket serverSocket = new ServerSocket(5000);

        // Accept a connection from Client 1
        Socket client1Socket = serverSocket.accept();
        // Create a PrintWriter object for sending messages to Client 1
        PrintWriter outToClient1 = new PrintWriter(client1Socket.getOutputStream(), true);
        // Create a BufferedReader object for receiving messages from Client 1
        BufferedReader inFromClient1 = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()));


        //Generation de la paire de clé publique et secrete coté Issuer apparently ??
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        PublicKey pk = kp.getPublic();
        PrivateKey sk = kp.getPrivate();



        // Receive message x from Client 1
        String DID = inFromClient1.readLine();





        String atts="J'ai obtenu mon Diplome de Master 2 en 2023"; //peut etre un .json pour les attributs ?


        //Random r et random salt securisé
        Random rand = new Random();
        int r = rand.nextInt();
        //System.out.println("Random choisi est :" +r);


        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        //System.out.println("Salt choisi est :"+salt);



        /// ***CALCUL DE SIGMA σ *** ///

        //Creating a Signature object
        Signature sign = Signature.getInstance("SHA256withRSA");//seul algo qui marche ?
        //Initialize the signature
        sign.initSign(sk);
        //Concatener pk,atts,r
        String cert=pk+atts+r;
        byte[] bytes = cert.getBytes();
        System.out.println("bytes"+bytes.length);

        //Adding data to the signature
        sign.update(bytes);
        //Signature de cert avec sk
        byte[] sig = sign.sign();
        //System.out.println("Sigma calculé:"+sigma);
        String sigma = new String(sig, StandardCharsets.UTF_8);

        /// *** Salted Hash des attributs certifiés *** ///
        MessageDigest md = MessageDigest.getInstance("SHA-512");//seul algo ??
        md.update(salt);
        byte[] salted_hash = md.digest(atts.getBytes());
        //System.out.println("Salted Hash est:"+salted_hash);


        //Sauvegarde du tableau des valeurs pour l'émetteur (Issuer)
        //Sauvegarde du salted hash des attributs au lieu du salt pour eviter les attaques par brute force sur la BDD
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


        String signatureStr = Base64.getEncoder().encodeToString(sigma.getBytes());
        outToClient1.println(signatureStr);
        outToClient1.println(salt);
        outToClient1.println(atts);
        outToClient1.println(r);






        // Accept a connection from Client 1
        Socket client3Socket = serverSocket.accept();
        // Create a PrintWriter object for sending messages to Client 1
        PrintWriter outToClient3 = new PrintWriter(client3Socket.getOutputStream(), true);
        // Create a BufferedReader object for receiving messages from Client 1
        BufferedReader inFromClient3 = new BufferedReader(new InputStreamReader(client3Socket.getInputStream()));



        outToClient3.println(DID);
        Base64.Encoder encoder = Base64.getEncoder();
        ObjectOutputStream out = new ObjectOutputStream(client3Socket.getOutputStream());
        out.writeObject(kp.getPublic());
        out.writeObject(r);
        out.writeObject(salted_hash);
        out.writeObject(sig);
        out.flush();
        System.out.println("sigma"+sig.length);









        // Close the connections
        outToClient3.close();
        inFromClient3.close();
        client3Socket.close();
        serverSocket.close();














    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Issuer srv = new Issuer();
        srv.run();
    }
}