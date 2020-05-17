import com.sun.jdi.Value;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
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
    public Map<String,JSONObject> pending_transactions  = new HashMap<String,JSONObject>();
    public Map<String,JSONObject> all_valid_transactions  = new HashMap<String,JSONObject>();
    public Map<String,Integer> all_invalid_prevtransactions  = new HashMap<String,Integer>();
    ArrayList<Map<String,JSONObject>> branches_transactions = new ArrayList<Map<String,JSONObject>>();
    public  int blockSize = 2;
    public int choosed_branch = 0;
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



    public int  chooseBlockToMineOnTopOfIt(){


        int max = 0;
        int max_index=0;
        for(int i=0;i< pendingBlocks.size();i++){
            if(pendingBlocks.get(i).size() > max){
                max  = pendingBlocks.get(i).size();
                max_index=i;
            }
        }
        this.choosed_branch = max_index;
        String hash = "";
        String blockHashValue1 = "";
        Block b = pendingBlocks.get(max_index).get(pendingBlocks.get(max_index).size()-1);
        blockHashValue1 += b.getPreviousBlockHash();
        blockHashValue1 += b.getMerkleTreeRoot();
        blockHashValue1 += b.getTimestamp();
        blockHashValue1 += b.getNonce();
        this.prevBlockHash = blockHashValue1;
        return max_index;
    }


    public void validateBlock(Block b)  throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        ArrayList<JSONObject> blockTransaction = b.getTransactions();
        boolean valid = true;
        for(int i = 0 ; i < blockTransaction.size() ; i++){
            valid = validateTransaction(blockTransaction.get(i));
            if(!valid){
                break;
            }
        }
        System.out.println(valid);
        if(valid){
            if(pendingBlocks.size() == 0){
                ArrayList<Block> temp = new ArrayList<Block>();
                Map<String,JSONObject>  temp1= new HashMap<String,JSONObject>();
                for(int i=0; i<b.getTransactions().size();i++){
                    temp1.put(b.getTransactions().get(i).get("hash").toString(),b.getTransactions().get(i));
                }

                branches_transactions.add(temp1);
                temp.add(b);
                pendingBlocks.add(temp);

            }
            int size = pendingBlocks.size();
            for(int i = 0 ; i < size; i++){
                int listSize = pendingBlocks.get(i).size();
                for(int j = 0 ; j < listSize; j++){
                    Block temp = pendingBlocks.get(i).get(j);
                    String blockHashValue = "";
                    blockHashValue += temp.getPreviousBlockHash();
                    blockHashValue += temp.getMerkleTreeRoot();
                    blockHashValue += temp.getTimestamp();
                    blockHashValue += temp.getNonce();
                    //System.out.println(blockHashValue );
                    //System.out.println(b.getPreviousBlockHash());
                    //System.out.println(pendingBlocks.size());
                    if(blockHashValue.equals(b.getPreviousBlockHash()) && j != listSize - 1){
                        ArrayList<Block> collisionList = new ArrayList<Block>();
                        for(int z = 0 ; z <= j ; z++){
                            collisionList.add(pendingBlocks.get(i).get(z));
                        }
                        // add block transaction to this branch
                        Map<String,JSONObject>  temp1= new HashMap(branches_transactions.get(i));

                        for(int k=0; k<b.getTransactions().size();i++){
                            temp1.put(b.getTransactions().get(i).get("hash").toString(),b.getTransactions().get(i));
                        }

                        branches_transactions.add(temp1);
                        collisionList.add(b);
                        if(collisionList.size() > maxLength) {
                            maxLength = collisionList.size();
                            maxIndex = pendingBlocks.size();
                        }
                        pendingBlocks.add(collisionList);
                        break;
                    }else if(blockHashValue.equals(b.getPreviousBlockHash()) && j == listSize - 1){
                        pendingBlocks.get(i).add(b);

                        for(int k=0; k<b.getTransactions().size();i++){
                            branches_transactions.get(i).put(b.getTransactions().get(i).get("hash").toString(),b.getTransactions().get(i));
                        }

                        if(pendingBlocks.get(i).size() > maxLength){
                            maxLength = pendingBlocks.get(i).size();
                            maxIndex = i;
                        }
                        break;
                    }

                }
            }

            if(maxLength > 2){
                Block safeBlock = pendingBlocks.get(maxIndex).get(0);
                String safeblockHashValue = "";
                safeblockHashValue += safeBlock.getPreviousBlockHash();
                safeblockHashValue += safeBlock.getMerkleTreeRoot();
                safeblockHashValue += safeBlock.getTimestamp();
                safeblockHashValue += safeBlock.getNonce();
                // chain.add(safeBlock);
                addToBlockchain(false,safeBlock);



                for(int i = 0 ; i <  pendingBlocks.size(); i++){
                    Block temp = pendingBlocks.get(i).get(0);
                    String blockHashValue = "";
                    blockHashValue += temp.getPreviousBlockHash();
                    blockHashValue += temp.getMerkleTreeRoot();
                    blockHashValue += temp.getTimestamp();
                    blockHashValue += temp.getNonce();
                    if(safeblockHashValue.equals(blockHashValue)){
                        pendingBlocks.get(i).remove(0);

                        for(int u=0;u<safeBlock.getTransactions().size();u++){
                            branches_transactions.get(i).remove(safeBlock.getTransactions().get(u).get("hash"));
                        }
                    }else{
                        pendingBlocks.remove(i);
                        branches_transactions.remove(i);
                        i--;
                    }
                }
                maxLength = maxLength - 1;
            }

        }
        for(int i = 0; i < pendingBlocks.size() ; i++){
            for(int j = 0 ; j < pendingBlocks.get(i).size() ; j++){
                System.out.print(pendingBlocks.get(i).get(j).getNonce() + " ");
            }
            System.out.println("");
        }
        System.out.println(maxLength);
        System.out.println(maxIndex);

    }







}

