package BFTVariant;

import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Entities.*;
import Helper.*;


public class MinerBFT extends NodeBFT{
    public Map<String, JSONObject> pending_transactions  = new HashMap<String,JSONObject>();
    public Map<String,JSONObject> all_valid_transactions  = new HashMap<String,JSONObject>();
    public Map<String,Integer> all_invalid_prevtransactions  = new HashMap<String,Integer>();
    ArrayList<Map<String,JSONObject>> branches_transactions = new ArrayList<Map<String,JSONObject>>();
    public  int blockSize = 2;
    boolean newBlockArrived = false;
    public  String prevBlockHash = "";
    int leaderNodeNum = 3;

    public MinerBFT(int portNum,int nodeNumber){
        super(portNum,nodeNumber);
    }

    public void initializeServer(){
        MinerBFTServer s = new MinerBFTServer(portNum,this);
        Thread thread = new Thread(s);
        thread.start();
    }

    public Block buildBlock(ArrayList<JSONObject> transactions ){
        Block b = new Block();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String blockHashValuee = "";
        Block lastBlock = blockChain.get(blockChain.size() - 1);
        blockHashValuee += lastBlock .getPreviousBlockHash();
        blockHashValuee += lastBlock .getMerkleTreeRoot();
        blockHashValuee += lastBlock .getTimestamp();
        blockHashValuee += lastBlock .getNonce();
        SHA256 hash = new SHA256();
        blockHashValuee = hash.generateHash(blockHashValuee);
        b.setPreviousBlockHash(blockHashValuee);
        b.setTimestamp(time);
        b.setTransactions(transactions);
        b.generateBlockHash();
        b.setNonce(0);
        return b;
    }



    public  void  addToBlockchain(boolean isfirst , Block block){

        System.out.println("ADD  to BLOCKCHAIN " + block.getNonce());

        this.blockChain.add(block);

        ArrayList<JSONObject> transactions = block.getTransactions();

        for(int i=0;i<transactions.size();i++){

            transaction t  = new transaction();
            all_valid_transactions.remove(transactions.get(i).get("hash").toString());
            String prevHash_outputindex = t.getPrevHash_outputindex(transactions.get(i));
            String prevhash = prevHash_outputindex.split(",")[0];
            String outputindex = prevHash_outputindex.split(",")[1];
            all_invalid_prevtransactions.remove(prevhash+outputindex);
            if(!isfirst){
                update_UTXO(transactions.get(i));
            }
            add_UTXO(transactions.get(i));
        }

    }



}
