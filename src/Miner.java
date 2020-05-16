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

    private  String prevBlockHash ;
    private  int  difficulty  = 2;
    //private ArrayList<JSONObject> pending_transactions = new ArrayList<JSONObject>();
    private Map<String,JSONObject> pending_transactions  = new HashMap<String,JSONObject>();
    private Map<String,JSONObject> all_valid_transactions  = new HashMap<String,JSONObject>();
    private Map<String,Integer> all_invalid_prevtransactions  = new HashMap<String,Integer>();
    //private Map<String,JSONObject> pending_transactions  = new HashMap<String,JSONObject>();
    private  int blockSize = 2;
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
                    validateBlock((Block) b);
                }else if(name.equals("Vote")){

                }else{
                    JSONObject tx = (JSONObject) b;
                    boolean valid_trans = this.validateTransaction(tx);
                    if(valid_trans){
                        boolean double_spend = false;
                        JSONArray inputs_array = new JSONArray();
                        String hash = tx.get("hash").toString();
                        inputs_array = (JSONArray) tx.get("inputs");
                        JSONObject inputObject = (JSONObject) inputs_array.get(0);
                        String prev_tx_hash = (String) inputObject.get("prevTxHash");
                        int  outputIndex = (int) inputObject.get("outputIndex");
                        String string_outputIndex =""+outputIndex;

                        // check if prev transaction used but not in main blockchain
                        if(all_invalid_prevtransactions.containsKey(prev_tx_hash+string_outputIndex)){

                            if(all_invalid_prevtransactions.get(prev_tx_hash+string_outputIndex) == outputIndex){
                                double_spend = true;

                            }



                        }
                        // check if it's a duplicate transaction
                        if(all_valid_transactions.containsKey(hash)){
                            double_spend = true;
                        }

                        if(!double_spend){
                            pending_transactions.put(hash,tx);
                            all_valid_transactions.put(hash,tx);
                            all_invalid_prevtransactions.put(prev_tx_hash+string_outputIndex,outputIndex);
                        }
                        //pending_transactions.add(tx);
                        if(pending_transactions.size() == blockSize){
                            ArrayList<JSONObject> temp = new ArrayList<JSONObject>();

                            for (String key: pending_transactions.keySet()) {

                                temp.add(pending_transactions.get(key));
                            }
                            buildBlock(temp);
                            pending_transactions.clear();
                        }
                    }


                }
            }
        }
    }

    private Block buildBlock(ArrayList<JSONObject> transactions ){
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

