package POWVariant;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Entities.*;
import Helper.*;
;

public class Node {


    // conncetion
    // list of peers \
    // public key
    // UTXO list
    // block chain
    // validate block    // don't forget to check double spend
    // construct transaction
    // file parsing

    private PrivateKey privateKey;
    private PublicKey publicKey;
    public Map<String,JSONObject> UTXO_list = new HashMap<String,JSONObject>();
    public Map<String,JSONObject> temp_UTXO_list = new HashMap<String,JSONObject>();
    ArrayList<PublicKey> publicKeys;
    public ArrayList<Block> blockChain = new ArrayList<Block>();
    private int difficulty = 5 ;
    int nodeNumber = -1;
    private Map<String,JSONObject> waited_transactions = new HashMap<String,JSONObject>() ;
    int maxLength = 0;
    int maxIndex = 0;
    public ArrayList<ArrayList<Block>> pendingBlocks = new ArrayList<ArrayList<Block>>();
    public  ArrayList<JSONObject> issueTransactions = new ArrayList<>();
    Lock lock = new ReentrantLock();
    Lock lock2= new ReentrantLock();
    int portNum = -1;
    public int uncertainity_block_num = 5;

    public Node(int portNum,int nodeNumber){
        generateKeyPair();
        this.nodeNumber = nodeNumber;
        //this.UTXO_list = new HashMap<String,JSONObject>();
        this.publicKeys = new ArrayList<>();
        this.portNum = portNum;
    }

    public void setTrasactions(ArrayList<JSONObject> Node_transaction) {

       // System.out.println(Node_transaction.size());

        for(int i=0;i<Node_transaction.size();i++){

            JSONArray inputs  = (JSONArray) Node_transaction.get(i).get("inputs");
            for (Object o : inputs) {
                JSONObject jsonLineItem = (JSONObject) o;
                String prevTxHash = jsonLineItem.get("prevTxHash").toString();
                waited_transactions.put(prevTxHash,Node_transaction.get(i));
            }
        }

    }

    public void initializeServer(){
        NodeServer s = new NodeServer(portNum,this);
        Thread thread = new Thread(s);
        thread.start();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public boolean checkNonce(String concat){
        boolean check = true;
        String hashedHeader = concat;
        int index = 0;
        while (index < difficulty) {

            if (hashedHeader.charAt(index) != '0') {
                check = false;
                break;
            }
            index++;
        }
        return check;
    }


    public void validateBlock(Block b) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, InterruptedException, InvalidKeySpecException {
        System.out.println(" ENTER VALIDATE BLOCK ");
        ArrayList<JSONObject> blockTransaction = b.getTransactions();
        String blockHashValuee = "";
        blockHashValuee += b.getPreviousBlockHash();
        blockHashValuee += b.getMerkleTreeRoot();
        blockHashValuee += b.getTimestamp();
        blockHashValuee += b.getNonce();
        SHA256 hash = new SHA256();
        blockHashValuee = hash.generateHash(blockHashValuee);


        boolean valid = checkNonce(blockHashValuee);
        valid = true;
        System.out.println(" is Nonce valid : " + valid);

        //boolean valid = true;
        if(valid){
            //System.out.println("73ml validation lel transactions ");
            for(int i = 0 ; i < blockTransaction.size() ; i++){
                valid = validateTransaction(blockTransaction.get(i));
                if(!valid){
                    break;
                }
            }
        }

        System.out.println("IS TRANSACTION VALID ? : "+valid);
        //System.out.println("node number : "+nodeNumber);
        //System.out.println("size of blockchain "+ blockChain.size());
        //System.out.println("prev hash " + b.getPreviousBlockHash());
        //System.out.println("nonce " + b.getNonce());
        if(valid){
            try{
                System.out.println("send block to peers");
                PeerToPeer conn = new PeerToPeer();
                conn.broadcastBlock(b,nodeNumber);
               // System.out.println("end sending block to peers");
            }catch(Exception e){

            }

            System.out.println("pending block size : " +pendingBlocks.size() );
            boolean found_place = false;
            if(pendingBlocks.size() == 0){
                ArrayList<Block> temp = new ArrayList<Block>();
                temp.add(b);
                pendingBlocks.add(temp);
                maxLength = 1;
                maxIndex = 0;

            } else {
                int size = pendingBlocks.size();
                for (int i = 0; i < size; i++) {
                    int listSize = pendingBlocks.get(i).size();
                  //  System.out.println("list num " + i + "in pendingBlocks size is : " + pendingBlocks.get(i).size());
                    for (int j = 0; j < listSize; j++) {
                        Block temp = pendingBlocks.get(i).get(j);
                        String blockHashValue = "";
                        blockHashValue += temp.getPreviousBlockHash();
                        blockHashValue += temp.getMerkleTreeRoot();
                        blockHashValue += temp.getTimestamp();
                        blockHashValue += temp.getNonce();
                        SHA256 hash2 = new SHA256();
                        blockHashValue = hash2.generateHash(blockHashValue);
                    //    System.out.println("prev block hash " + b.getPreviousBlockHash());

                        if (blockHashValue.equals(b.getPreviousBlockHash()) && j != listSize - 1) {

                            found_place = true;
                            // check if block has duplicates
                            boolean isDuplicateBlock = false;
                            for (int p = 0; p < pendingBlocks.size(); p++) {
                                if (pendingBlocks.get(p).size() > j + 1 && pendingBlocks.get(p).get(j + 1).getBlockHash().equals(b.getBlockHash())) {
                                    isDuplicateBlock = true;
                                    break;
                                }
                            }
                            System.out.println("IS DUPLICATE BLOCK :" + isDuplicateBlock);
                            if (!isDuplicateBlock) {
                                ArrayList<Block> collisionList = new ArrayList<Block>();
                                for (int z = 0; z <= j; z++) {
                                    collisionList.add(pendingBlocks.get(i).get(z));
                                }
                                collisionList.add(b);
                                if (collisionList.size() > maxLength) {
                                    maxLength = collisionList.size();
                                    maxIndex = pendingBlocks.size()-1;
                                }
                                pendingBlocks.add(collisionList);
                            }
                            break;
                        } else if (blockHashValue.equals(b.getPreviousBlockHash()) && j == listSize - 1) {
                            found_place = true;
                            pendingBlocks.get(i).add(b);
                            if (pendingBlocks.get(i).size() > maxLength) {
                                maxLength = pendingBlocks.get(i).size();
                                maxIndex = i;
                            }
                            break;
                        }

                    }
                }
                if(!found_place){

                    boolean isDuplicateBlock = false;
                    for (int p = 0; p < pendingBlocks.size(); p++) {
                        if (pendingBlocks.get(p).size() > 0 && pendingBlocks.get(p).get(0).getBlockHash().equals(b.getBlockHash())) {
                            isDuplicateBlock = true;
                            break;
                        }
                    }
                    if (!isDuplicateBlock) {
                        // this mean this block will start it's own branch and see what will happen
                        ArrayList<Block> temp1 = new ArrayList<Block>();
                        temp1.add(b);
                        pendingBlocks.add(temp1);
                        PeerToPeer conn = new PeerToPeer();
                        conn.broadcastBlock(b,nodeNumber);
                    }

                }
            }
            if(maxLength > uncertainity_block_num){
                System.out.println(" FOUND SAFE BLOCK TO ADD ");
                Block safeBlock = pendingBlocks.get(maxIndex).get(0);
                String safeblockHashValue = "";
                safeblockHashValue += safeBlock.getPreviousBlockHash();
                safeblockHashValue += safeBlock.getMerkleTreeRoot();
                safeblockHashValue += safeBlock.getTimestamp();
                safeblockHashValue += safeBlock.getNonce();
                // chain.add(safeBlock);
                SHA256 hash2 = new SHA256();
                safeblockHashValue = hash2.generateHash(safeblockHashValue);
                addToBlockchain(false,safeBlock);

                for(int i = 0 ; i <  pendingBlocks.size(); i++){
                    Block temp = pendingBlocks.get(i).get(0);
                    String blockHashValue = "";
                    blockHashValue += temp.getPreviousBlockHash();
                    blockHashValue += temp.getMerkleTreeRoot();
                    blockHashValue += temp.getTimestamp();
                    blockHashValue += temp.getNonce();
                    SHA256 hash3 = new SHA256();
                    blockHashValue = hash3.generateHash(blockHashValue);

                    if(safeblockHashValue.equals(blockHashValue)){
                        pendingBlocks.get(i).remove(0);
                    }else{
                        pendingBlocks.remove(i);
                        i--;
                    }
                }
                maxLength = maxLength - 1;
            }

        }
        System.out.println("block chain size : " + blockChain.size());
        System.out.println("PENDING BLOCKS");
        for(int i = 0; i < pendingBlocks.size() ; i++){
            for(int j = 0 ; j < pendingBlocks.get(i).size() ; j++){
                System.out.print(pendingBlocks.get(i).get(j).getNonce() + " ");
            }
            System.out.println("");
        }
        System.out.println("MAX LENGTH : " + maxLength);
        System.out.println("MAX INDEX : " + maxIndex);

        System.out.println(" BLOCK CHAIN ");
        for(int z=0;z<blockChain.size();z++){
            System.out.println(blockChain.get(z).getNonce() + ">  " );
        }

    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

            KeyPair keyPair = keyGen.generateKeyPair();
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();

        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    // used after add block to block chain
    public void update_UTXO(JSONObject transaction){
     //   System.out.println(" ENTER UPDATE_UTXO");
        JSONArray inputs  = (JSONArray) transaction.get("inputs");
        for (Object o : inputs) {
            JSONObject jsonLineItem = (JSONObject) o;
            String prevTxHash = jsonLineItem.get("prevTxHash").toString();
            int  outputIndex = (int)jsonLineItem.get("outputIndex");
       //     System.out.println("transaction used  hash " +prevTxHash);

            if(UTXO_list.containsKey(prevTxHash)){
                JSONObject used_outputs = (JSONObject) UTXO_list.get(prevTxHash).get("used_outputs");
                int usedOutputcounter = (int)used_outputs.get("usedOutputcounter");
         //       System.out.println("used_outputs before ");
           //     System.out.println(used_outputs);
                used_outputs.remove("usedOutputcounter");
                used_outputs.put("usedOutputcounter",usedOutputcounter-1);


                used_outputs.remove(""+outputIndex);
                used_outputs.put(""+outputIndex,0);
             //   System.out.println("used_outputs after ");
               // System.out.println(used_outputs);
                if(usedOutputcounter-1 == 0){
                    UTXO_list.remove(prevTxHash);
                }



            }


        }



    }
    public  void add_UTXO(JSONObject transaction) throws IOException, InterruptedException {

        //System.out.println(" ENTER ADD TO UTXO ");
        String hash = transaction.get("hash").toString();
        int outputCounter = (int)transaction.get("outputCounter");
        JSONObject output_indexes = new JSONObject();
        output_indexes.put("usedOutputcounter",outputCounter);



        for(int i=0;i<outputCounter;i++){

            output_indexes.put((""+(i+1)),1); // if 1 that mean this output index unspend else 0
        }
        transaction.put("used_outputs",output_indexes);
        //System.out.println(hash);
        //System.out.println(transaction);
        JSONObject t = new JSONObject();
        UTXO_list.put(hash,transaction);
        if(waited_transactions.containsKey(hash)){

          //  System.out.println("found waiting prev " + hash);
            issueTransactions.add(waited_transactions.get(hash));
        }
    }


    public boolean validateTransaction(JSONObject transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {

       // System.out.println(" ENTER VALIDATE TRANSACTION");

        boolean IS_UTXO = isUnSpend(transaction);
        //System.out.println("is un spend :" + IS_UTXO);
        //System.out.println("IS UTXO : " + IS_UTXO );

        boolean singnatur_is_right = false ;
        if(IS_UTXO){
            singnatur_is_right = validateSignature(transaction);
            System.out.println("singnatur_is_right : " + singnatur_is_right );

        }
       // System.out.println("is signature right:" + singnatur_is_right);
        //boolean value_is_valid = validValue(transaction);

        return singnatur_is_right ;
    }

    private boolean validValue(JSONObject transaction) {

        return false;

    }

    private boolean isUnSpend(JSONObject transaction) {
        //System.out.println("ENTER isUnspend  FUNCTION ");
        JSONArray inputs  = (JSONArray) transaction.get("inputs");
        int inputCounter  = (int)transaction.get("inputCounter");
        for (Object o : inputs) {
            JSONObject jsonLineItem = (JSONObject) o;
            String prevTxHash = jsonLineItem.get("prevTxHash").toString();
            int  outputIndex = (int)jsonLineItem.get("outputIndex");
          //  System.out.println(" trying to spend : " +prevTxHash);
            if(UTXO_list.containsKey(prevTxHash)){
                //System.out.println("entered hereeeeee ");
                JSONObject used_outputs = (JSONObject) UTXO_list.get(prevTxHash).get("used_outputs");
            //    System.out.println("used output  "+used_outputs);
              //  System.out.println("output index "+outputIndex);
                //System.out.println("transaction  "+ jsonLineItem);
                if((int)used_outputs.get(""+outputIndex)==0){
                    return false;
                }
            }else{
                return false;            }
        }


        return true;
    }

    private boolean validateSignature(JSONObject transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {

      //  System.out.println("ENTER VALIDATE SIGNATURE ");
        JSONArray inputs  = (JSONArray) transaction.get("inputs");
        int inputCounter  = (int)transaction.get("inputCounter");
        for (Object o : inputs) {
            JSONObject jsonLineItem = (JSONObject) o;
            String prevTxHash = jsonLineItem.get("prevTxHash").toString();

            int  outputIndex = (int)jsonLineItem.get("outputIndex");
            JSONArray prevTxOutputs  = (JSONArray) UTXO_list.get(prevTxHash).get("outputs");

            JSONObject output =(JSONObject) prevTxOutputs.get(outputIndex-1);
            PublicKey publicKey = (PublicKey) output.get("publicKey");


            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            SHA256 hasher = new SHA256();
            String temp = hasher.generateHash(transaction.get("inputs").toString()+transaction.get("outputs").toString());
            sig.update(temp.getBytes());

            byte [] b = (byte[]) transaction.get("signature");
            boolean keyPairMatches = sig.verify(b);
            if(!keyPairMatches) {
                return keyPairMatches;
            }

        }

        return true ;

    }

    public  void  addToBlockchain(boolean isfirst , Block block) throws IOException, InterruptedException {

        blockChain.add(block);
        System.out.println("ADD TO BLOCK CHAIN");
        System.out.println("BLOCK CHAIN SIZE : "+ blockChain.size());


        ArrayList<JSONObject> transactions = block.getTransactions();

        for(int i=0;i<transactions.size();i++){
            if(!isfirst){
                update_UTXO(transactions.get(i));
            }
            add_UTXO(transactions.get(i));
        }

        for(int i=0;i<issueTransactions.size();i++){
            issueTransaction(issueTransactions.get(i));

        }


    }

    public void issueTransaction(JSONObject jsonObject) throws IOException, InterruptedException {

        PeerToPeer conn = new PeerToPeer();
        conn.broadcastTx(jsonObject,-1);

    }

}