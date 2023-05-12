import java.io.*;
import java.net.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

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

public class Holder {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        // Connect to the server
        Socket Client2Socket = new Socket("localhost", 5000);
        // Create a PrintWriter object for sending messages to the server
        PrintWriter outToClient2 = new PrintWriter(Client2Socket.getOutputStream(), true);
        // Create a BufferedReader object for receiving messages from the server
        BufferedReader inFromClient2 = new BufferedReader(new InputStreamReader(Client2Socket.getInputStream()));



        //Generation de la paire de clé publique et secrete coté utilisateur
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        PublicKey pk = kp.getPublic();
        PrivateKey sk = kp.getPrivate();



        //A faire : identité numérique ETH contract adress, verifier identité grace au Protocole de Schnorr
        String DID= "0x71C7656EC7ab88b098defB751B7401B5f6d8976F";
        System.out.println("Utilisateur authentifié en tant que propriétaire de l'identité numérique DID(U,I)");


        String atts="J'ai obtenu mon Diplome de Master 2 en 2023"; //peut etre un .json pour les attributs ?


        // Send message x to Issuer
        outToClient2.println(DID);
        outToClient2.println(atts);

        // Receive message y from Client 2
        //String responseFromClient2 = inFromClient2.readLine();


        // Close the connection
        outToClient2.close();
        inFromClient2.close();
        Client2Socket.close();


    }



}