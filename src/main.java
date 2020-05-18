import BFTVariant.LeaderBFT;
import BFTVariant.MinerBFT;
import BFTVariant.NodeBFT;
import Entities.*;
import Helper.PeerToPeer;
import Helper.SHA256;
import POWVariant.Node;
import Parsing.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Scanner;

import Entities.*;
import Helper.*;
import Parsing.*;
import BFTVariant.*;
import POWVariant.*;


import org.json.simple.parser.ParseException;

public class main {

     public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, ClassNotFoundException, ParseException, InvalidKeySpecException, URISyntaxException, NoSuchProviderException, InterruptedException {
         Timestamp timestamp = new Timestamp(System.currentTimeMillis());
         int nodeNumber = Integer.parseInt(args[0]);
         parsing p = new parsing();
         NodePeers node = p.readPort(nodeNumber);
         int port = node.getPort();

         // set transaction
         String  path = "testFiles/Node"+nodeNumber+"Transactions.txt";
         // String path = "testFiles/txdataset_v2.txt";
         ArrayList<JSONObject> transactions = new ArrayList<>();
         transactions = constructTransactions(path, nodeNumber);

         System.out.println("end reading ");
         System.out.println(transactions.size());

         //Miner n = new Miner(port,nodeNumber);

         Node n;
         if(port == 4000 || port == 4001){
             n = new Miner(port,nodeNumber);
         }else{
             n = new Node(port,nodeNumber);
         }
         n.initializeServer();
         n.setTrasactions(transactions);

         ///
         int k =0;
         String blockHashValue1 = "";
         Timestamp time = new Timestamp(50);

         // create first block in the chain in each Node
         ArrayList<JSONObject> transactions2 = new ArrayList<>();
         path = "testFiles/Node0Transactions.txt";
         transactions2 = constructTransactions(path, 0);
         System.out.println("fisrt transactions");
         System.out.println(transactions2.get(0));
         System.out.println(transactions2.get(1));
         Block b0 = new Block();
         b0.setNonce(0);
         b0.setTransactions(transactions2);
         b0.generateBlockHash();
         b0.setTimestamp(time);
         b0.setPreviousBlockHash(blockHashValue1);
         blockHashValue1 = "";

         blockHashValue1 += b0.getPreviousBlockHash();
         blockHashValue1 += b0.getMerkleTreeRoot();
         blockHashValue1 += b0.getTimestamp();
         blockHashValue1 += b0.getNonce();
         SHA256 hash = new SHA256();
         blockHashValue1 = hash.generateHash(blockHashValue1);
         System.out.println(" EL hash bt3 awl block   : " + blockHashValue1);
         n.addToBlockchain(true,b0);

       /*  if(port == 4000){
             //n.validateBlock(blocks.get(3));
             //n.validateBlock(blocks.get(4));
             //n.validateBlock(blocks.get(5));
             //n.validateBlock(blocks.get(6));
         }
       */ /* if(port == 4002) {
             PeerToPeer conn = new PeerToPeer();

             conn.broadcastTx(transactions.get(6), nodeNumber);
             System.out.println(transactions.get(6).get("hash"));
             conn.broadcastTx(transactions.get(7), nodeNumber);
             System.out.println(transactions.get(7).get("hash"));

             conn.broadcastTx(transactions.get(8), nodeNumber);
             System.out.println(transactions.get(8).get("hash"));
             conn.broadcastTx(transactions.get(9), nodeNumber);
             System.out.println(transactions.get(9).get("hash"));

             conn.broadcastTx(transactions.get(10), nodeNumber);
             System.out.println(transactions.get(10).get("hash"));
             conn.broadcastTx(transactions.get(11), nodeNumber);
             System.out.println(transactions.get(11).get("hash"));

             conn.broadcastTx(transactions.get(12), nodeNumber);
             System.out.println(transactions.get(12).get("hash"));
             conn.broadcastTx(transactions.get(13), nodeNumber);
             System.out.println(transactions.get(13).get("hash"));
         }*/
         /*
         Timestamp timestamp = new Timestamp(System.currentTimeMillis());
         int nodeNumber = Integer.parseInt(args[0]);
         parsing p = new parsing();
         NodePeers node = p.readPort(nodeNumber);
         int port = node.getPort();
         NodeBFT n;
         if(port == 4001){
             n = new MinerBFT(port,nodeNumber);
         }else if( port == 4000){
             n = new NodeBFT(port,nodeNumber);
         }else {
             n = new LeaderBFT(port,nodeNumber);
         }
         n.initializeServer();
         ArrayList<JSONObject> transactions=  constructTransactions();

         ArrayList<Block> blocks  = new ArrayList<Block>();
         int k =0;
         String blockHashValue1 = "";
         Timestamp time = new Timestamp(50);

         for(int i=0;i<6;i++){
             ArrayList<JSONObject> transaction = new ArrayList<JSONObject>();
             transaction.add(transactions.get(k));
             k++;
             transaction.add(transactions.get(k));
             k++;
             Block b0 = new Block();

             b0.setNonce(0);
             b0.setTransactions(transaction);
             b0.generateBlockHash();
             b0.setTimestamp(time);
             b0.setPreviousBlockHash(blockHashValue1);
             blockHashValue1 = "";
             blockHashValue1 += b0.getPreviousBlockHash();
             blockHashValue1 += b0.getMerkleTreeRoot();
             blockHashValue1 += b0.getTimestamp();
             blockHashValue1 += b0.getNonce();
             SHA256 hash = new SHA256();
             blockHashValue1 = hash.generateHash(blockHashValue1);
             System.out.println(i + " EL hash bt3ha  : " + blockHashValue1);

             blocks.add(b0);
         }



         n.addToBlockchain(true,blocks.get(0));
         n.addToBlockchain(true,blocks.get(1));
         n.addToBlockchain(true,blocks.get(2));
         System.out.println("Blockchain size : " + n.blockChain.size());
         if(port == 4002){
             //n.validateBlock(blocks.get(3));
             //n.validateBlock(blocks.get(4));
             //n.validateBlock(blocks.get(5));
         }
         if(port == 4002){
             PeerToPeer conn = new PeerToPeer();

             conn.broadcastTx(transactions.get(15),nodeNumber);
             System.out.println(transactions.get(15).get("hash"));
             conn.broadcastTx(transactions.get(16),nodeNumber);
             System.out.println(transactions.get(16).get("hash"));
         }



*/
/*
         for (int i=0;i<50;i++){
         KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
         KeyPair keyPair = keyGen.generateKeyPair();
         PublicKey publicKey = keyPair.getPublic();
         PrivateKey privateKey = keyPair.getPrivate();
         OutputStream outputStream = new FileOutputStream("testFiles/keys"+i+"c"+".txt");
         outputStream.write(publicKey.getEncoded());
         //System.out.println(publicKey);
         outputStream = new FileOutputStream("testFiles/keys"+i+"p"+".txt");
             outputStream.write(privateKey.getEncoded());
         System.out.println(privateKey);
         }

         for(int i=0;i<50;i++){

             InputStream inputStream = new FileInputStream("testFiles/keys"+i+"c"+".txt");
             long fileSize = new File("testFiles/keys"+i+"c"+".txt").length();
             byte[] allBytes = new byte[(int) fileSize];
             inputStream.read(allBytes);
             KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
             PublicKey publicKey1 = kf.generatePublic(new X509EncodedKeySpec(allBytes));
             //System.out.println(publicKey1);


              inputStream = new FileInputStream("testFiles/keys"+i+"p"+".txt");
              fileSize = new File("testFiles/keys"+i+"p"+".txt").length();
             allBytes = new byte[(int) fileSize];
             inputStream.read(allBytes);
             KeyFactory kf1 = KeyFactory.getInstance("RSA"); // or "EC" or whatever
             //PrivateKey privateKey = kf1.generatePrivate(new X509EncodedKeySpec(allBytes));
             PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(allBytes);
             //PrivateKey privateKey = kf1.generatePrivate(new X509EncodedKeySpec(spec.getEncoded()));
             PrivateKey privateKey = kf1.generatePrivate(spec);
             System.out.println(privateKey);

         }






        ArrayList<JSONObject> transactions=  constructTransactions();
        //System.out.println(transactions.size());


         Timestamp timestamp = new Timestamp(System.currentTimeMillis());
         int nodeNumber = Integer.parseInt(args[0]);
         Parsing.parsing p = new Parsing.parsing();
         Entities.NodePeers node = p.readPort(nodeNumber);
         int port = node.getPort();

         //POWVariant.Miner n = new POWVariant.Miner(port,nodeNumber);

         POWVariant.Node n;
         if(port == 4001){
             n = new POWVariant.Miner(port,nodeNumber);
         }else{
             n = new POWVariant.Node(port,nodeNumber);
         }
         n.initializeServer();


         ///

         ArrayList<Entities.Block> blocks  = new ArrayList<Entities.Block>();
         int k =0;
         String blockHashValue1 = "";
         Timestamp time = new Timestamp(50);

         for(int i=0;i<6;i++){
             ArrayList<JSONObject> Entities.transaction = new ArrayList<JSONObject>();
             Entities.transaction.add(transactions.get(k));
             k++;
             Entities.transaction.add(transactions.get(k));
             k++;
             Entities.Block b0 = new Entities.Block();

             b0.setNonce(i);
             b0.setTransactions(Entities.transaction);
             b0.generateBlockHash();
             b0.setTimestamp(time);
             b0.setPreviousBlockHash(blockHashValue1);
             blockHashValue1 = "";
             blockHashValue1 += b0.getPreviousBlockHash();
             blockHashValue1 += b0.getMerkleTreeRoot();
             blockHashValue1 += b0.getTimestamp();
             blockHashValue1 += b0.getNonce();
             Helper.SHA256 hash = new Helper.SHA256();
             blockHashValue1 = hash.generateHash(blockHashValue1);
             System.out.println(i + " EL hash bt3ha  : " + blockHashValue1);

             blocks.add(b0);
         }

         n.addToBlockchain(true,blocks.get(0));
         n.addToBlockchain(true,blocks.get(1));
         n.addToBlockchain(true,blocks.get(2));
         if(port == 4000){
             n.validateBlock(blocks.get(3));
             n.validateBlock(blocks.get(4));
             n.validateBlock(blocks.get(5));
         }


         if(port == 4000){
              Helper.PeerToPeer conn = new Helper.PeerToPeer();

              conn.broadcastTx(transactions.get(15),nodeNumber);
              System.out.println(transactions.get(15).get("hash"));
              conn.broadcastTx(transactions.get(16),nodeNumber);
             System.out.println(transactions.get(16).get("hash"));
         }
*/
/*

         ///
         if(port == 4000){
             Entities.Block b = new Entities.Block();
             b.setMerkleTreeRoot("dummy");
             Helper.PeerToPeer conn = new Helper.PeerToPeer();

            conn.broadcastBlock(b,nodeNumber);
         }




         JSONObject trans = new JSONObject();
         trans.put("hash","076cab0107c9f06661f3d42fb83719aff7b7d98c04d10176d2268e2dff92a6d9");
         trans.put("inputCounter",1);
         trans.put("signature","[B@43814d18");
         trans.put("outputCounter",1);
         trans.put("outputCounter",1);
         JSONArray inputs = new JSONArray();
         JSONObject Entities.input = new JSONObject();
         Entities.input.put("prevTxHash","a6864eb339b0e1f6e00d75293a8840abf069a2c0fe82e6e53af6ac099793c1d5");
         Entities.input.put("outputIndex",-1);
         inputs.add(Entities.input);
         trans.put("inputs",inputs);

         JSONArray outputs = new JSONArray();
         JSONObject Entities.output = new JSONObject();
         Entities.output.put("publicKey","ahmed");
         Entities.output.put("index",1);
         outputs.add(Entities.output);
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
             Helper.PeerToPeer conn = new Helper.PeerToPeer();

             conn.broadcastTx(trans,nodeNumber);
             conn.broadcastTx(trans2,nodeNumber);
             conn.broadcastTx(trans3,nodeNumber);
         }
         //n.recBlocks();

  */
         /*
         Entities.Block b = new Entities.Block();
         Class className = b.getClass();
         System.out.println(className.getName());
       */

         /*System.out.println(args[0]);
         int nodeNumber = Integer.parseInt(args[0]);
         Parsing.parsing p = new Parsing.parsing();
         Entities.NodePeers node = p.readPort(nodeNumber);
         int port = node.getPort();
         System.out.println(port);

         Helper.PeerToPeer connect = new Helper.PeerToPeer();
         ServerSocket s = connect.openConnection(port);
         if(port == 4000){
             testConnection();
         }
         Socket socket = s.accept();
         System.out.println(socket);
         Entities.Block b = connect.receiveBlock(socket);
         System.out.println(b.getMerkleTreeRoot());
         //while(true){
           //  connect.receiveBlock(s);
         //}
         //testConnection();
         //s.close();
*/
         /*
         JSONParser parser = new JSONParser();
         POWVariant.Node node  = new POWVariant.Node();
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
/*         POWVariant.Node n = new POWVariant.Node();
         Timestamp time = new Timestamp(System.currentTimeMillis());

         Entities.Block b0 = new Entities.Block();

         b0.setNonce(20);
         b0.setMerkleTreeRoot("0");
         b0.setTimestamp(time);
         b0.setPreviousBlockHash("0");

         String blockHashValue1 = "";
         blockHashValue1 += b0.getPreviousBlockHash();
         blockHashValue1 += b0.getMerkleTreeRoot();
         blockHashValue1 += b0.getTimestamp();
         blockHashValue1 += b0.getNonce();

         Entities.Block b1 = new Entities.Block();

         b1.setPreviousBlockHash(blockHashValue1);
         b1.setNonce(20);
         b1.setMerkleTreeRoot("1");
         b1.setTimestamp(time);

         String blockHashValue2 = "";
         blockHashValue2 += b1.getPreviousBlockHash();
         blockHashValue2 += b1.getMerkleTreeRoot();
         blockHashValue2 += b1.getTimestamp();
         blockHashValue2 += b1.getNonce();

         Entities.Block b2 = new Entities.Block();



         b2.setPreviousBlockHash(blockHashValue2);
         b2.setNonce(20);
         b2.setMerkleTreeRoot("2");
         b1.setTimestamp(time);

         String blockHashValue3 = "";
         blockHashValue3 += b2.getPreviousBlockHash();
         blockHashValue3 += b2.getMerkleTreeRoot();
         blockHashValue3 += b2.getTimestamp();
         blockHashValue3 += b2.getNonce();

         Entities.Block b3 = new Entities.Block();

         b3.setPreviousBlockHash(blockHashValue3);
         b3.setNonce(20);
         b3.setMerkleTreeRoot("3");
         b3.setTimestamp(time);

         String blockHashValue4 = "";
         blockHashValue4 += b3.getPreviousBlockHash();
         blockHashValue4 += b3.getMerkleTreeRoot();
         blockHashValue4 += b3.getTimestamp();
         blockHashValue4 += b3.getNonce();


         Entities.Block b4 = new Entities.Block();

         b4.setPreviousBlockHash(blockHashValue4);
         b4.setNonce(20);
         b4.setMerkleTreeRoot("4");
         b4.setTimestamp(time);

         String blockHashValue5 = "";
         blockHashValue5 += b4.getPreviousBlockHash();
         blockHashValue5 += b4.getMerkleTreeRoot();
         blockHashValue5 += b4.getTimestamp();
         blockHashValue5 += b4.getNonce();

         Entities.Block b5 = new Entities.Block();

         b5.setPreviousBlockHash(blockHashValue5);
         b5.setNonce(20);
         b5.setMerkleTreeRoot("5");
         b5.setTimestamp(time);

         String blockHashValue6 = "";
         blockHashValue6 += b5.getPreviousBlockHash();
         blockHashValue6 += b5.getMerkleTreeRoot();
         blockHashValue6 += b5.getTimestamp();
         blockHashValue6 += b5.getNonce();

         Entities.Block b7 = new Entities.Block();

         b7.setPreviousBlockHash(blockHashValue1);
         b7.setNonce(20);
         b7.setMerkleTreeRoot("7");
         b7.setTimestamp(time);

         String blockHashValue8 = "";
         blockHashValue8 += b7.getPreviousBlockHash();
         blockHashValue8 += b7.getMerkleTreeRoot();
         blockHashValue8 += b7.getTimestamp();
         blockHashValue8 += b7.getNonce();

         Entities.Block b8 = new Entities.Block();

         b8.setPreviousBlockHash(blockHashValue4);
         b8.setNonce(20);
         b8.setMerkleTreeRoot("8");
         b8.setTimestamp(time);

         String blockHashValue9 = "";
         blockHashValue9 += b8.getPreviousBlockHash();
         blockHashValue9 += b8.getMerkleTreeRoot();
         blockHashValue9 += b8.getTimestamp();
         blockHashValue9 += b8.getNonce();

         Entities.Block b9 = new Entities.Block();

         b9.setPreviousBlockHash(blockHashValue9);
         b9.setNonce(20);
         b9.setMerkleTreeRoot("9");
         b9.setTimestamp(time);

         String blockHashValue10 = "";
         blockHashValue10 += b9.getPreviousBlockHash();
         blockHashValue10 += b9.getMerkleTreeRoot();
         blockHashValue10 += b9.getTimestamp();
         blockHashValue10 += b9.getNonce();

         Entities.Block b10 = new Entities.Block();

         b10.setPreviousBlockHash(blockHashValue10);
         b10.setNonce(20);
         b10.setMerkleTreeRoot("10");
         b10.setTimestamp(time);

         String blockHashValue11 = "";
         blockHashValue11 += b10.getPreviousBlockHash();
         blockHashValue11 += b10.getMerkleTreeRoot();
         blockHashValue11 += b10.getTimestamp();
         blockHashValue11 += b10.getNonce();

         Entities.Block b11 = new Entities.Block();

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
        //testSortFile();
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
        Node node = new Node(1,2);
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

    public  static  ArrayList<JSONObject>  constructTransactions(String path,int NodeNum) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException, InvalidKeySpecException {
        SHA256 hasher = new SHA256();
        parsing parse = new parsing();
        ArrayList<TransactionFromText> transaction_parse = parse.readDataset(path);


        Map<Integer,String> map_txNum_to_hash = new HashMap<Integer,String>();
        String hash_temp = hasher.generateHash("temp");
        map_txNum_to_hash.put(-1,hash_temp);
        // initialize 50 nodes

        PrivateKey  privateKey = null;
        PublicKey  publicKeys [] = new PublicKey[50];

        try {
            File myObj = new File("transaction_hash.txt");
            Scanner myReader = new Scanner(myObj);
            int k=1;
            while (myReader.hasNextLine()) {
                String f = myReader.nextLine();
                map_txNum_to_hash.put(k,f);
                k++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        for(int i=0;i<50;i++){

            InputStream inputStream = new FileInputStream("testFiles/keys"+i+"c"+".txt");
            long fileSize = new File("testFiles/keys"+i+"c"+".txt").length();
            byte[] allBytes = new byte[(int) fileSize];
            inputStream.read(allBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
            publicKeys[i] = kf.generatePublic(new X509EncodedKeySpec(allBytes));
        }
        InputStream inputStream = new FileInputStream("testFiles/keys"+NodeNum+"p"+".txt");
        long fileSize = new File("testFiles/keys"+NodeNum+"p"+".txt").length();
        byte[]  allBytes = new byte[(int) fileSize];
        inputStream.read(allBytes);
        KeyFactory kf1 = KeyFactory.getInstance("RSA"); // or "EC" or whatever
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(allBytes);
        privateKey = kf1.generatePrivate(spec);


        ArrayList<JSONObject> transactions_objects = new ArrayList<JSONObject>();
        for(int i=0;i<transaction_parse.size();i++){

            ArrayList<OutputsFromText>  outputs_parse = transaction_parse.get(i).getOutputs();
            InputsFromText input_parse    =  transaction_parse.get(i).getInputs();

            PublicKey publicKey = publicKeys[input_parse.getInput()];

            transaction transaction = new transaction();

            transaction.setInputCounter(1);

            String prev_hash = map_txNum_to_hash.get(input_parse.getPreviousTX());
            if(input_parse.getPreviousTX() == 1 || input_parse.getPreviousTX() == 2 ){
            }
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
                output.setPublicKey(publicKeys[outputs_parse.get(j).getOutput()]);
                outputs.add(output);

            }

            transaction.setOutputs(outputs);

            transaction.setHash();
            transaction.setSignature(privateKey);

            //System.out.println(transaction.getTransactionObject().get("hash"));
            ///map_txNum_to_hash.put(i+1,transaction.getTransactionObject().get("hash").toString());

            JSONObject test =  transaction.getTransactionObject();
            // System.out.println(test);


            transactions_objects.add(test);

        }
        return transactions_objects;
    }

    public static void testDatasetParser(){
        parsing parse = new parsing();
        //ArrayList<TransactionFromText> transactions = parse.readDataset();
        //System.out.println("done");
    }
}
