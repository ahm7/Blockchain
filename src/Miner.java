import com.sun.jdi.Value;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.image.ByteLookupTable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Miner extends Node {

    public  String prevBlockHash ;
    public  int  difficulty  = 2;
    //private ArrayList<JSONObject> pending_transactions = new ArrayList<JSONObject>();
    public Map<String,JSONObject> pending_transactions  = new HashMap<String,JSONObject>();
    public Map<String,JSONObject> all_valid_transactions  = new HashMap<String,JSONObject>();
    public Map<String,Integer> all_invalid_prevtransactions  = new HashMap<String,Integer>();
    //private Map<String,JSONObject> pending_transactions  = new HashMap<String,JSONObject>();
    public  int blockSize = 2;
    public Miner(int portNum) {
        super(portNum);
    }
    // Miner
    // construct block
    // proof of work
    // broadcast
    // BFT
    // validate transactions
    // ArrayList of transaction
    //


    public void  mine (){


    }

    public void recBlocks() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        while(true){
            Object b = s.getBlock();
            if(b != null){
                Class className = b.getClass();
                String name = className.getName();
                if(name.equals("Block")){
                    MinerSender h = new MinerSender(1,b,this);
                    Thread thread = new Thread(h);
                    thread.start();

                }else if(name.equals("Vote")){


                }else{
                    MinerSender h = new MinerSender(3,b,this);
                    Thread thread = new Thread(h);
                    thread.start();

                }
            }
        }
    }

    public Block buildBlock(ArrayList<JSONObject> transactions ){
        Block b = new Block();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        b.setPreviousBlockHash(this.prevBlockHash);
        b.setTimestamp(time);
        b.setTransactions(transactions);
        b.generateBlockHash();
        POW pow = new POW(b,difficulty);
        b = pow.getProofOfWork();

        return b;
    }
    public  void  addToBlockchain(boolean isfirst , Block block){

        this.blockChain.add(block);

        ArrayList<JSONObject> transactions = block.getTransactions();

        for(int i=0;i<transactions.size();i++){

            // delete from hash

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


    public  boolean isBLockHasTheRightTransactions(Block block){


         ArrayList<JSONObject> transactions  = block.getTransactions();


         for(int i=0;i<transactions.size();i++) {

             if (!pending_transactions.containsKey(transactions.get(i))) {

                 return false;
             }

         }


         return true;
    }






}

