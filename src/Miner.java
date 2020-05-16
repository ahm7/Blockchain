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
                        inputs_array = (JSONArray) tx.get("inputs");
                        JSONObject inputObject = (JSONObject) inputs_array.get(0);
                        String prev_tx_hash = (String) inputObject.get("prevTxHash");
                        int  outputIndex = (int) inputObject.get("outputIndex");

                        if(pending_transactions.containsKey(prev_tx_hash)){
                            JSONArray inputs_array2 = new JSONArray();
                            inputs_array2 = (JSONArray) pending_transactions.get(prev_tx_hash).get("inputs");
                            JSONObject inputObject2 = (JSONObject) inputs_array2.get(0);
                            String prev_tx_hash2 = (String) inputObject.get("prevTxHash");
                            int  outputIndex2 = (int) inputObject.get("outputIndex");

                            if(prev_tx_hash.equals(prev_tx_hash2) && outputIndex == outputIndex2){
                                double_spend = true;

                            }



                        }

                        if(!double_spend){
                            pending_transactions.put(prev_tx_hash,tx);
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






}

