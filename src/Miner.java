import org.json.simple.JSONObject;

import java.awt.image.ByteLookupTable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Miner extends Node {


    private  String prevBlockHash ;
    private  int  difficulty  = 2;
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

