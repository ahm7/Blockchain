package BFTVariant;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Entities.*;
import Helper.*;

public class NodeBFT {


    private PrivateKey privateKey;
    private PublicKey publicKey;
    public Map<String, JSONObject> UTXO_list = new HashMap<String,JSONObject>();
    public Map<String,JSONObject> temp_UTXO_list = new HashMap<String,JSONObject>();
    ArrayList<PublicKey> publicKeys;
    public ArrayList<Block> blockChain = new ArrayList<Block>();
    int nodeNumber = -1;
    Lock lock = new ReentrantLock();
    int portNum = -1;
    ArrayList<Vote> votesRecieved = new ArrayList<Vote>();
    int networkSize = 3;
    Block currentBlock = null;


    public NodeBFT(int portNum,int nodeNumber){
        generateKeyPair();
        this.nodeNumber = nodeNumber;
        this.publicKeys = new ArrayList<>();
        this.portNum = portNum;
    }
    public void initializeServer(){
        NodeBFTServer s = new NodeBFTServer(portNum,this);
        Thread thread = new Thread(s);
        thread.start();
    }

    public void addToVotes(Vote v) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        votesRecieved.add(v);
        System.out.println("Vote For Block " + currentBlock.getPreviousBlockHash() + " " + v.getNodeVote());
        if(votesRecieved.size() == networkSize){
            BFT bft = new BFT();
            boolean commit = bft.commit(nodeNumber,votesRecieved);
            System.out.println("COMMIT VALUE : " + commit);
            if(commit){
                addToBlockchain(false,currentBlock);


            }
            votesRecieved.clear();
            System.out.println("BLOCKCHAIN SIZE : " + blockChain.size());
        }

    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void validateBlock(Block b) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, InterruptedException {
          // nt2kd mn l prev tx hash
          currentBlock = b;
          ArrayList<JSONObject> blockTransaction = b.getTransactions();
          boolean valid = false;
          for(int i = 0 ; i < blockTransaction.size() ; i++){
            valid = validateTransaction(blockTransaction.get(i));
            if(!valid){
                break;
                //return;
            }
          }
        System.out.println(b.getPreviousBlockHash() + " Valid value : " + valid);

          if(valid){
              String blockHashValuee = "";
              Block lastBlock = blockChain.get(blockChain.size() - 1);
              blockHashValuee += lastBlock .getPreviousBlockHash();
              blockHashValuee += lastBlock .getMerkleTreeRoot();
              blockHashValuee += lastBlock .getTimestamp();
              blockHashValuee += lastBlock .getNonce();
              SHA256 hash = new SHA256();
              blockHashValuee = hash.generateHash(blockHashValuee);
              System.out.println("LAST BLOCK : " +  blockHashValuee);
              if(blockHashValuee.equals(b.getPreviousBlockHash())){
                  valid = true;
              }else{
                  valid = false;
              }
          }
        System.out.println(b.getPreviousBlockHash() + " Valid value2 : " + valid);
        PeerToPeer conn = new PeerToPeer();
        Vote nodeVote = new Vote(valid);
        conn.broadcastVote(nodeVote,nodeNumber);
        votesRecieved.add(nodeVote);
        // nt2kd kol l transactions ele fl block slima
         // if valid broadcast vote true else broad cast false
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
        JSONArray inputs  = (JSONArray) transaction.get("inputs");
        for (Object o : inputs) {
            JSONObject jsonLineItem = (JSONObject) o;
            String prevTxHash = jsonLineItem.get("prevTxHash").toString();
            int  outputIndex = (int)jsonLineItem.get("outputIndex");


            if(UTXO_list.containsKey(prevTxHash)){
                JSONObject used_outputs = (JSONObject) UTXO_list.get(prevTxHash).get("used_outputs");
                int usedOutputcounter = (int)used_outputs.get("usedOutputcounter");
                used_outputs.remove("usedOutputcounter");
                used_outputs.put("usedOutputcounter",usedOutputcounter-1);

                used_outputs.remove(""+outputIndex);
                used_outputs.put(""+outputIndex,0);
                if(usedOutputcounter-1 == 0){
                    UTXO_list.remove(prevTxHash);
                }



            }


        }



    }
    public  void add_UTXO(JSONObject transaction){

        String hash = transaction.get("hash").toString();
        int outputCounter = (int)transaction.get("outputCounter");
        JSONObject output_indexes = new JSONObject();
        output_indexes.put("usedOutputcounter",outputCounter);


        for(int i=0;i<outputCounter;i++){
            output_indexes.put((""+(i+1)),1); // if 1 that mean this Entities.output index unspend else 0
        }
        transaction.put("used_outputs",output_indexes);
        //System.out.println(hash);
        //System.out.println(Entities.transaction);
        JSONObject t = new JSONObject();
        UTXO_list.put(hash,transaction);
    }


    public boolean validateTransaction(JSONObject transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {


        boolean IS_UTXO = isUnSpend(transaction);
        //System.out.println("IS UTXO : " + IS_UTXO );

        boolean singnatur_is_right = false ;
        if(IS_UTXO){
            singnatur_is_right = validateSignature(transaction);
            //System.out.println("singnatur_is_right : " + singnatur_is_right );

        }
        //boolean value_is_valid = validValue(Entities.transaction);

        return  singnatur_is_right & IS_UTXO ;
    }

    private boolean validValue(JSONObject transaction) {

        return false;

    }

    private boolean isUnSpend(JSONObject transaction) {
        JSONArray inputs  = (JSONArray) transaction.get("inputs");
        int inputCounter  = (int)transaction.get("inputCounter");
        for (Object o : inputs) {
            JSONObject jsonLineItem = (JSONObject) o;
            String prevTxHash = jsonLineItem.get("prevTxHash").toString();
            int  outputIndex = (int)jsonLineItem.get("outputIndex");
            //System.out.println(" here a  a a " +prevTxHash);
            if(UTXO_list.containsKey(prevTxHash)){
                //System.out.println("entered hereeeeee ");
                JSONObject used_outputs = (JSONObject) UTXO_list.get(prevTxHash).get("used_outputs");
                if((int)used_outputs.get(""+outputIndex)==0){
                    return false;
                }
            }else{
                return false;            }
        }


        return true;
    }

    private boolean validateSignature(JSONObject transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

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

    public  void  addToBlockchain(boolean isfirst , Block block){

        blockChain.add(block);


        ArrayList<JSONObject> transactions = block.getTransactions();

        for(int i=0;i<transactions.size();i++){
            if(!isfirst){
                update_UTXO(transactions.get(i));
            }
            add_UTXO(transactions.get(i));
        }


    }

}
