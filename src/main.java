import org.json.simple.JSONObject;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class main {

     public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, ClassNotFoundException {
         System.out.println(args[0]);
         int nodeNumber = Integer.parseInt(args[0]);
         parsing p = new parsing();
         NodePeers node = p.readPort(nodeNumber);
         int port = node.getPort();
         System.out.println(port);
         PeerToPeer connect = new PeerToPeer();
         ServerSocket s = connect.openConnection(port);
         if(port == 4000){
             testConnection();
         }
         Socket socket = s.accept();
         System.out.println(socket);
         Block b = connect.recieveblock(socket);
         System.out.println(b.getMerkleTreeRoot());
         //while(true){
           //  connect.recieveblock(s);
         //}
         //testConnection();
         //s.close();

         //ArrayList<JSONObject> transactions_objects = constructTransactions();
    }
    public static void testConnection() throws IOException {
        PeerToPeer conn = new PeerToPeer();
        Block dummy = new Block();
        dummy.setMerkleTreeRoot("Hello 4000");
        parsing p = new parsing();
        ArrayList<NodePeers> peers = p.readNodePeers(1);
        for(int i = 0 ; i < peers.size() ; i++){
            String ip = peers.get(i).getIP();
            int port = peers.get(i).getPort();
            conn.sendBlock(ip,port,dummy);
        }
    }


    public static void testCreateTransaction() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, FileNotFoundException {
        Node node = new Node();
        PublicKey publicKey = node.getPublicKey();
        PrivateKey privateKey = node.getPrivateKey();

        transaction transaction = new transaction();

        transaction.setInputCounter(1);

        SHA256 hasher = new SHA256();
        String prev_hash = hasher.generateHash("ahmedHesham");
        input  input = new input(prev_hash,1);
        ArrayList<input> inputs  = new ArrayList<input>();
        inputs.add(input);
        transaction.setInputs(inputs);

        transaction.setOutputCounter(2);

        output  output = new output();
        output.setIndex(1);
        output.setValue(14.55);
        output.setPublicKey(publicKey);

        output  output1 = new output();
        output1.setIndex(2);
        output1.setValue(16.55);
        output1.setPublicKey(publicKey);

        ArrayList<output> outputs  = new ArrayList<output>();
        outputs.add(output);
        outputs.add(output1);
        transaction.setOutputs(outputs);

        transaction.setHash();
        transaction.setSignature(privateKey);

        JSONObject test =  transaction.getTransactionObject();


        PrintWriter pw = new PrintWriter("JSONExample.json");
        pw.write(test.toJSONString());

        pw.flush();
        pw.close();


    }

    public  static  ArrayList<JSONObject>  constructTransactions() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        SHA256 hasher = new SHA256();
        parsing parse = new parsing();
        ArrayList<TransactionFromText> transaction_parse = parse.readDataset();

        Map<Integer,String> map_txNum_to_hash = new HashMap<Integer,String>();
        String hash_temp = hasher.generateHash("temp");
        map_txNum_to_hash.put(-1,hash_temp);
        // initialize 50 nodes
        Node[] nodes = new Node[50];
        for(int i=0;i<nodes.length;i++){
            nodes[i] = new Node();
        }

        ArrayList<JSONObject> transactions_objects = new ArrayList<JSONObject>();
        for(int i=0;i<100;i++){

            ArrayList<OutputsFromText>  outputs_parse = transaction_parse.get(i).getOutputs();
            InputsFromText input_parse    =  transaction_parse.get(i).getInputs();

            PublicKey publicKey = nodes[input_parse.getInput()].getPublicKey();
            PrivateKey privateKey = nodes[input_parse.getInput()].getPrivateKey();

            transaction transaction = new transaction();

            transaction.setInputCounter(1);

            String prev_hash = map_txNum_to_hash.get(input_parse.getPreviousTX());
            input  input = new input(prev_hash,input_parse.getOutputIndex());
            ArrayList<input> inputs  = new ArrayList<input>();
            inputs.add(input);
            transaction.setInputs(inputs);

            transaction.setOutputCounter(outputs_parse.size());
            ArrayList<output> outputs  = new ArrayList<output>();

            for(int j=0;j<outputs_parse.size();j++){
                output  output = new output();
                output.setIndex(j);
                output.setValue(outputs_parse.get(j).getValue());
                output.setPublicKey(nodes[outputs_parse.get(j).getOutput()].getPublicKey());
                outputs.add(output);

            }

            transaction.setOutputs(outputs);

            transaction.setHash();
            transaction.setSignature(nodes[input_parse.getInput()].getPrivateKey());

            JSONObject test =  transaction.getTransactionObject();
            System.out.println(test);
            transactions_objects.add(test);

        }
        return transactions_objects;
    }

    public static void testDatasetParser(){
         parsing parse = new parsing();
         ArrayList<TransactionFromText> transactions = parse.readDataset();
         System.out.println("done");
    }


    /*  public private key draft  */
    //  testDatasetParser();
    //  testCreateTransaction();
                 /*
         Node node = new Node();
         PublicKey publicKey = node.getPublicKey();
         PrivateKey privateKey = node.getPrivateKey();


         byte[] challenge = new byte[10000];
         ThreadLocalRandom.current().nextBytes(challenge);

         SHA256 test = new SHA256();
         String temp = test.generateHash(challenge.toString());
         Signature sig = Signature.getInstance("SHA256withRSA");
         sig.initSign(privateKey);
         byte[] b = temp.getBytes();
         sig.update(b);
         byte[] signature = sig.sign();
         System.out.println(signature);
// verify signature using the public key

         sig.initVerify(publicKey);
         sig.update(challenge);

         boolean keyPairMatches = sig.verify(signature);

         System.out.println(keyPairMatches);

    */



}
