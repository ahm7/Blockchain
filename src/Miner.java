import org.json.simple.JSONObject;

import java.io.IOException;
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
    public Map<String,JSONObject> getPendingTrans(){
        return pending_transactions;
    }
    public void recBlocks() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
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






}

