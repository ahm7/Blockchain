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
import java.sql.Timestamp;
import java.util.concurrent.ThreadLocalRandom;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.Timestamp;

public class main {

     public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, ClassNotFoundException, ParseException {
         Timestamp timestamp = new Timestamp(System.currentTimeMillis());

         int nodeNumber = Integer.parseInt(args[0]);
         parsing p = new parsing();
         NodePeers node = p.readPort(nodeNumber);
         int port = node.getPort();
         Node n;
         if(port == 4001){
             n = new Miner(port);
         }else{
             n = new Node(port);
         }
         /*
         if(port == 4000){
             Block b = new Block();
             b.setMerkleTreeRoot("dummy");
             PeerToPeer conn = new PeerToPeer();

            conn.broadcastBlock(b,nodeNumber);
         }

          */



         JSONObject trans = new JSONObject();
         trans.put("hash","076cab0107c9f06661f3d42fb83719aff7b7d98c04d10176d2268e2dff92a6d9");
         trans.put("inputCounter",1);
         trans.put("signature","[B@43814d18");
         trans.put("outputCounter",1);
         trans.put("outputCounter",1);
         JSONArray inputs = new JSONArray();
         JSONObject input = new JSONObject();
         input.put("prevTxHash","a6864eb339b0e1f6e00d75293a8840abf069a2c0fe82e6e53af6ac099793c1d5");
         input.put("outputIndex",-1);
         inputs.add(input);
         trans.put("inputs",inputs);

         JSONArray outputs = new JSONArray();
         JSONObject output = new JSONObject();
         output.put("publicKey","ahmed");
         output.put("index",1);
         outputs.add(output);
         trans.put("outputs",outputs);

         JSONObject trans2 = new JSONObject();
         trans2.put("hash","096cab0107c9f06661f3d42fb83719aff7b7d98c04d10176d2268e2dff92a6d9");
         trans2.put("inputCounter",1);
         trans2.put("signature","[B@43814d18");
         trans2.put("outputCounter",1);
         trans2.put("outputCounter",1);
         JSONArray inputs2 = new JSONArray();
         JSONObject input2 = new JSONObject();
         input2.put("prevTxHash","a6864eb339b0epj6e00d75293a8840abf069a2c0fe82e6e53af6ac099793c1d5");
         input2.put("outputIndex",-1);
         inputs2.add(input2);
         trans2.put("inputs",inputs2);

         JSONArray outputs2 = new JSONArray();
         JSONObject output2 = new JSONObject();
         output2.put("publicKey","ahmed");
         output2.put("index",1);
         outputs2.add(output2);
         trans2.put("outputs",outputs2);

         JSONObject trans3 = new JSONObject();
         trans3.put("hash","096cab0107c9f0666156742fb83719aff7b7d98c04d10176d2268e2dff92a6d9");
         trans3.put("inputCounter",1);
         trans3.put("signature","[B@43814d18");
         trans3.put("outputCounter",1);
         trans3.put("outputCounter",1);
         JSONArray inputs3 = new JSONArray();
         JSONObject input3 = new JSONObject();
         input3.put("prevTxHash","a6864eb339b0epj6e00d75293a8840abf01292c0fe82e6e53af6ac099793c1d5");
         input3.put("outputIndex",-1);
         inputs3.add(input3);
         trans3.put("inputs",inputs3);

         JSONArray outputs3 = new JSONArray();
         JSONObject output3 = new JSONObject();
         output3.put("publicKey","ahmed");
         output3.put("index",1);
         outputs3.add(output3);
         trans3.put("outputs",outputs3);




         if(port == 4000){
             PeerToPeer conn = new PeerToPeer();

             conn.broadcastTx(trans,nodeNumber);
             conn.broadcastTx(trans2,nodeNumber);
             conn.broadcastTx(trans3,nodeNumber);
         }
         n.recBlocks();
         /*
         Block b = new Block();
         Class className = b.getClass();
         System.out.println(className.getName());
       */

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
     /*    ArrayList<JSONObject> transactions_objects = constructTransactions();
         JSONArray a = new JSONArray();
       for(int k=0;k<transactions_objects.size();k++){
             a.add(transactions_objects.get(k));
         }
         System.out.println(transactions_objects);
         FileWriter file = new FileWriter("testFiles/txs.json");
         file.write(a.toJSONString());
         file.close();
        //testSplitNodeTransactions();
*/
         //testTransactionsWithBlock();
/*         Node n = new Node();
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
         */
        testSortFile();
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

    public static void testSortFile() throws IOException {
         for(int i = 1;i < 50;i++){
             parsing p = new parsing();
             p.createSortedFile(i);
             SortTextFile sorting = new SortTextFile();
             sorting.sortTransactionsFile(i);
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
        Node node = new Node(1);
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
        System.out.println("traparse"+" "+ transaction_parse.size());


        Map<Integer,String> map_txNum_to_hash = new HashMap<Integer,String>();
        String hash_temp = hasher.generateHash("temp");
        map_txNum_to_hash.put(-1,hash_temp);
        // initialize 50 nodes
        Node[] nodes = new Node[50];
        for(int i=0;i<nodes.length;i++){
            nodes[i] = new Node(1);
        }

        ArrayList<JSONObject> transactions_objects = new ArrayList<JSONObject>();
        for(int i=0;i<transaction_parse.size();i++){

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

    public static  void testTransactionsWithBlock() throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        ArrayList<JSONObject> transactions_objects = constructTransactions();
        System.out.println("trasize"+" "+ transactions_objects.size());
        JSONArray a = new JSONArray();
        for(int k=0;k<transactions_objects.size();k++){
            a.add(transactions_objects.get(k));
        }
        System.out.println(transactions_objects.size());


        Node n = new Node(1);
        Timestamp time = new Timestamp(System.currentTimeMillis());

        Block b0 = new Block();

        b0.setNonce(0);
        ArrayList<JSONObject> t0 = new ArrayList<>();
        t0.add(transactions_objects.get(0));
        t0.add(transactions_objects.get(1));
        b0.setTransactions(t0);
        b0.generateBlockHash();
        //b0.setMerkleTreeRoot("0");
        b0.setTimestamp(time);
        b0.setPreviousBlockHash("0");

        String blockHashValue1 = "";
        blockHashValue1 += b0.getPreviousBlockHash();
        blockHashValue1 += b0.getMerkleTreeRoot();
        blockHashValue1 += b0.getTimestamp();
        blockHashValue1 += b0.getNonce();

        Block b1 = new Block();

        b1.setPreviousBlockHash(blockHashValue1);
        b1.setNonce(1);
        ArrayList<JSONObject> t1 = new ArrayList<>();
        t1.add(transactions_objects.get(2));
        t1.add(transactions_objects.get(3));
        b1.setTransactions(t1);
        b1.generateBlockHash();
        //b1.setMerkleTreeRoot("1");
        b1.setTimestamp(time);

//
        String blockHashValue2 = "";
        blockHashValue2 += b1.getPreviousBlockHash();
        blockHashValue2 += b1.getMerkleTreeRoot();
        blockHashValue2 += b1.getTimestamp();
        blockHashValue2 += b1.getNonce();

        Block b2 = new Block();



        b2.setPreviousBlockHash(blockHashValue2);
        b2.setNonce(2);
        //b2.setMerkleTreeRoot("2");
        ArrayList<JSONObject> t2 = new ArrayList<>();
        t2.add(transactions_objects.get(4));
        t2.add(transactions_objects.get(5));
        b2.setTransactions(t2);
        b2.generateBlockHash();
        b2.setTimestamp(time);


        String blockHashValue3 = "";
        blockHashValue3 += b2.getPreviousBlockHash();
        blockHashValue3 += b2.getMerkleTreeRoot();
        blockHashValue3 += b2.getTimestamp();
        blockHashValue3 += b2.getNonce();

        Block b3 = new Block();

        b3.setPreviousBlockHash(blockHashValue3);
        b3.setNonce(3);
        //b3.setMerkleTreeRoot("3");
        b3.setTimestamp(time);

        ArrayList<JSONObject> t3 = new ArrayList<>();
        t3.add(transactions_objects.get(6));
        t3.add(transactions_objects.get(7));
        b3.setTransactions(t3);
        b3.generateBlockHash();

        String blockHashValue4 = "";
        blockHashValue4 += b3.getPreviousBlockHash();
        blockHashValue4 += b3.getMerkleTreeRoot();
        blockHashValue4 += b3.getTimestamp();
        blockHashValue4 += b3.getNonce();


        Block b4 = new Block();

        b4.setPreviousBlockHash(blockHashValue4);
        b4.setNonce(4);
        //b4.setMerkleTreeRoot("4");
        b4.setTimestamp(time);
        ArrayList<JSONObject> t4 = new ArrayList<>();
        t4.add(transactions_objects.get(8));
        t4.add(transactions_objects.get(9));
        b4.setTransactions(t4);
        b4.generateBlockHash();

        String blockHashValue5 = "";
        blockHashValue5 += b4.getPreviousBlockHash();
        blockHashValue5 += b4.getMerkleTreeRoot();
        blockHashValue5 += b4.getTimestamp();
        blockHashValue5 += b4.getNonce();

        Block b5 = new Block();

        b5.setPreviousBlockHash(blockHashValue5);
        b5.setNonce(5);
        //b5.setMerkleTreeRoot("5");
        b5.setTimestamp(time);
        ArrayList<JSONObject> t5 = new ArrayList<>();
        t5.add(transactions_objects.get(10));
        t5.add(transactions_objects.get(11));
        b5.setTransactions(t5);
        b5.generateBlockHash();


        String blockHashValue6 = "";
        blockHashValue6 += b5.getPreviousBlockHash();
        blockHashValue6 += b5.getMerkleTreeRoot();
        blockHashValue6 += b5.getTimestamp();
        blockHashValue6 += b5.getNonce();

        Block b7 = new Block();

        b7.setPreviousBlockHash(blockHashValue6);
        b7.setNonce(7);
       // b7.setMerkleTreeRoot("7");
        b7.setTimestamp(time);
        ArrayList<JSONObject> t7 = new ArrayList<>();
        t7.add(transactions_objects.get(12));
        t7.add(transactions_objects.get(13));
        b7.setTransactions(t7);
        b7.generateBlockHash();

        String blockHashValue8 = "";
        blockHashValue8 += b7.getPreviousBlockHash();
        blockHashValue8 += b7.getMerkleTreeRoot();
        blockHashValue8 += b7.getTimestamp();
        blockHashValue8 += b7.getNonce();

        Block b8 = new Block();

        b8.setPreviousBlockHash(blockHashValue8);
        b8.setNonce(8);
        //b8.setMerkleTreeRoot("8");
        b8.setTimestamp(time);
        ArrayList<JSONObject> t8 = new ArrayList<>();
        t8.add(transactions_objects.get(14));
        t8.add(transactions_objects.get(15));
        b8.setTransactions(t8);
        b8.generateBlockHash();

        String blockHashValue9 = "";
        blockHashValue9 += b8.getPreviousBlockHash();
        blockHashValue9 += b8.getMerkleTreeRoot();
        blockHashValue9 += b8.getTimestamp();
        blockHashValue9 += b8.getNonce();

        Block b9 = new Block();

        b9.setPreviousBlockHash(blockHashValue9);
        b9.setNonce(9);
        //b9.setMerkleTreeRoot("9");
        b9.setTimestamp(time);
        ArrayList<JSONObject> t9 = new ArrayList<>();
        t9.add(transactions_objects.get(16));
        t9.add(transactions_objects.get(17));
        b9.setTransactions(t9);
        b9.generateBlockHash();

        String blockHashValue10 = "";
        blockHashValue10 += b9.getPreviousBlockHash();
        blockHashValue10 += b9.getMerkleTreeRoot();
        blockHashValue10 += b9.getTimestamp();
        blockHashValue10 += b9.getNonce();

    /*    Block b10 = new Block();

        b10.setPreviousBlockHash(blockHashValue10);
        b10.setNonce(10);
        //b10.setMerkleTreeRoot("10");
        b10.setTimestamp(time);
        ArrayList<JSONObject> t10 = new ArrayList<>();
        t10.add(transactions_objects.get(16));
        t10.add(transactions_objects.get(17));
        b10.setTransactions(t10);
        b10.generateBlockHash();
*/

        //n.validateBlock(b0);
        //n.validateBlock(b1);
        //n.validateBlock(b2);
        n.addToBlockchain(true,b0);
        n.addToBlockchain(true,b1);
        n.addToBlockchain(true,b2);
        System.out.println("b7");
        n.validateBlock(b3);
        System.out.println("b3");
        n.validateBlock(b4);
        System.out.println("b4");
        n.validateBlock(b5);
        System.out.println("b8");
        n.validateBlock(b7);
        System.out.println("b5");
        n.validateBlock(b8);
        System.out.println("b9");
        n.validateBlock(b9);
        System.out.println("b10");
        //n.validateBlock(b10);
        //System.out.println("b");



    }
}
