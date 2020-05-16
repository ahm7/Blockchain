import org.json.simple.JSONObject;

import java.awt.image.ByteLookupTable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Miner extends Node {


    private  String prevBlockHash ;
    private  int  difficulty  = 2;

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

