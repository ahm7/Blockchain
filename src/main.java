import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.Timestamp;

public class main {

     public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, ClassNotFoundException, ParseException {
         /*System.out.println(args[0]);
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
         Block b = connect.receiveBlock(socket);
         System.out.println(b.getMerkleTreeRoot());
         //while(true){
           //  connect.receiveBlock(s);
         //}
         //testConnection();
         //s.close();
*/
         /*
         JSONParser parser = new JSONParser();
         Node node  = new Node();
         //JSONArray a = (JSONArray) parser.parse(new FileReader("testFiles/txs.json"));

         int i=0;
         ArrayList<JSONObject> transactions_objects = constructTransactions();

             node.add_UTXO(transactions_objects.get(40));
             node.update_UTXO(transactions_objects.get(49));
        System.out.println(node.validateTransaction(transactions_objects.get(49)));
        */
        /* ArrayList<JSONObject> transactions_objects = constructTransactions();
         JSONArray a = new JSONArray();
         a.add(transactions_objects.get(40));
         a.add(transactions_objects.get(49));
         //a.add(transactions_objects.get(49));

         FileWriter file = new FileWriter("testFiles/txs.json");
         file.write(a.toJSONString());
         file.close();*/
        //testSplitNodeTransactions();

         Node n = new Node();
         Timestamp time = new Timestamp(System.currentTimeMillis());

         Block b0 = new Block();

         b0.setNonce(20);
         b0.setMerkleTreeRoot("0");
         b0.setTimestamp(time);
         b0.setPreviousBlockHash("0");

         String blockHashValue1 = "";
         blockHashValue1 += b0.getPreviousBlockHash();
         blockHashValue1 += b0.getMerkleTreeRoot();
         blockHashValue1 += b0.getTimestamp();
         blockHashValue1 += b0.getNonce();

         Block b1 = new Block();

         b1.setPreviousBlockHash(blockHashValue1);
         b1.setNonce(20);
         b1.setMerkleTreeRoot("1");
         b1.setTimestamp(time);

         String blockHashValue2 = "";
         blockHashValue2 += b1.getPreviousBlockHash();
         blockHashValue2 += b1.getMerkleTreeRoot();
         blockHashValue2 += b1.getTimestamp();
         blockHashValue2 += b1.getNonce();

         Block b2 = new Block();



         b2.setPreviousBlockHash(blockHashValue2);
         b2.setNonce(20);
         b2.setMerkleTreeRoot("2");
         b1.setTimestamp(time);

         String blockHashValue3 = "";
         blockHashValue3 += b2.getPreviousBlockHash();
         blockHashValue3 += b2.getMerkleTreeRoot();
         blockHashValue3 += b2.getTimestamp();
         blockHashValue3 += b2.getNonce();

         Block b3 = new Block();

         b3.setPreviousBlockHash(blockHashValue3);
         b3.setNonce(20);
         b3.setMerkleTreeRoot("3");
         b3.setTimestamp(time);

         String blockHashValue4 = "";
         blockHashValue4 += b3.getPreviousBlockHash();
         blockHashValue4 += b3.getMerkleTreeRoot();
         blockHashValue4 += b3.getTimestamp();
         blockHashValue4 += b3.getNonce();


         Block b4 = new Block();

         b4.setPreviousBlockHash(blockHashValue4);
         b4.setNonce(20);
         b4.setMerkleTreeRoot("4");
         b4.setTimestamp(time);

         String blockHashValue5 = "";
         blockHashValue5 += b4.getPreviousBlockHash();
         blockHashValue5 += b4.getMerkleTreeRoot();
         blockHashValue5 += b4.getTimestamp();
         blockHashValue5 += b4.getNonce();

         Block b5 = new Block();

         b5.setPreviousBlockHash(blockHashValue5);
         b5.setNonce(20);
         b5.setMerkleTreeRoot("5");
         b5.setTimestamp(time);

         String blockHashValue6 = "";
         blockHashValue6 += b5.getPreviousBlockHash();
         blockHashValue6 += b5.getMerkleTreeRoot();
         blockHashValue6 += b5.getTimestamp();
         blockHashValue6 += b5.getNonce();

         Block b7 = new Block();

         b7.setPreviousBlockHash(blockHashValue1);
         b7.setNonce(20);
         b7.setMerkleTreeRoot("7");
         b7.setTimestamp(time);

         String blockHashValue8 = "";
         blockHashValue8 += b7.getPreviousBlockHash();
         blockHashValue8 += b7.getMerkleTreeRoot();
         blockHashValue8 += b7.getTimestamp();
         blockHashValue8 += b7.getNonce();

         Block b8 = new Block();

         b8.setPreviousBlockHash(blockHashValue4);
         b8.setNonce(20);
         b8.setMerkleTreeRoot("8");
         b8.setTimestamp(time);

         String blockHashValue9 = "";
         blockHashValue9 += b8.getPreviousBlockHash();
         blockHashValue9 += b8.getMerkleTreeRoot();
         blockHashValue9 += b8.getTimestamp();
         blockHashValue9 += b8.getNonce();

         Block b9 = new Block();

         b9.setPreviousBlockHash(blockHashValue9);
         b9.setNonce(20);
         b9.setMerkleTreeRoot("9");
         b9.setTimestamp(time);

         String blockHashValue10 = "";
         blockHashValue10 += b9.getPreviousBlockHash();
         blockHashValue10 += b9.getMerkleTreeRoot();
         blockHashValue10 += b9.getTimestamp();
         blockHashValue10 += b9.getNonce();

         Block b10 = new Block();

         b10.setPreviousBlockHash(blockHashValue10);
         b10.setNonce(20);
         b10.setMerkleTreeRoot("10");
         b10.setTimestamp(time);

         String blockHashValue11 = "";
         blockHashValue11 += b10.getPreviousBlockHash();
         blockHashValue11 += b10.getMerkleTreeRoot();
         blockHashValue11 += b10.getTimestamp();
         blockHashValue11 += b10.getNonce();

         Block b11 = new Block();

         b11.setPreviousBlockHash(blockHashValue11);
         b11.setNonce(20);
         b11.setMerkleTreeRoot("11");
         b11.setTimestamp(time);

         String blockHashValue12 = "";
         blockHashValue12 += b11.getPreviousBlockHash();
         blockHashValue12 += b11.getMerkleTreeRoot();
         blockHashValue12 += b11.getTimestamp();
         blockHashValue12 += b11.getNonce();

         n.validateBlock(b0);
         n.validateBlock(b1);
         n.validateBlock(b2);
         n.validateBlock(b7);
         n.validateBlock(b3);
         n.validateBlock(b4);
         n.validateBlock(b8);
         n.validateBlock(b5);
         n.validateBlock(b9);
         n.validateBlock(b10);
         n.validateBlock(b11);
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

    public static void testSplitNodeTransactions() throws IOException {
         parsing p = new parsing();
         for(int i = 0;i < 50;i++){
             p.createFile(i);
         }
         p.writeNodeTransactions();
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

    public  static  ArrayList<JSONObject>  constructTransactions() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException {
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
        for(int i=0;i<53;i++){

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

            //System.out.println(transaction.getTransactionObject().get("hash"));
            map_txNum_to_hash.put(i+1,transaction.getTransactionObject().get("hash").toString());

            JSONObject test =  transaction.getTransactionObject();
           // System.out.println(test);


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
