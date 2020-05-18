package POWVariant;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Entities.*;
import Helper.*;

public class Miner extends Node {

    public  String prevBlockHash ;
    public  int  difficulty  = 2;
    public Map<String,JSONObject> pending_transactions  = new HashMap<String,JSONObject>();
    public Map<String,JSONObject> all_valid_transactions  = new HashMap<String,JSONObject>();
    public Map<String,Integer> all_invalid_prevtransactions  = new HashMap<String,Integer>();
    ArrayList<Map<String,JSONObject>> branches_transactions = new ArrayList<Map<String,JSONObject>>();
    public  int blockSize = 2;
    public int choosed_branch = 0;
    boolean branchChanged = false;
    boolean newBlockArrived = false;
    public Miner(int portNum,int nodeNumber) {

        super(portNum,nodeNumber);


    }

    public void initializeServer(){
        MinerServer s = new MinerServer(portNum,this);
        Thread thread = new Thread(s);
        thread.start();
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


    public Block buildBlock(ArrayList<JSONObject> transactions ){
        Block b = new Block();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        b.setPreviousBlockHash(this.prevBlockHash);
        b.setTimestamp(time);
        b.setTransactions(transactions);
        b.generateBlockHash();
        POW pow = new POW(b,difficulty,this);
        b = pow.getProofOfWork();


        return b;
    }
    public  void  addToBlockchain(boolean isfirst , Block block) throws IOException {

        System.out.println("ADD  to BLOCKCHAIN " + block.getNonce());

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
        SHA256 hash2 = new SHA256();
        blockHashValue1 = hash2.generateHash(blockHashValue1);
        this.prevBlockHash = blockHashValue1;
        return max_index;
    }


    public void validateBlock(Block b) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
        System.out.println("Enter validate block");
        System.out.println("Pneding block size : " + pendingBlocks.size());
        System.out.println("prev block hash " + b.getPreviousBlockHash());
        ArrayList<JSONObject> blockTransaction = b.getTransactions();
        String blockHashValuee = "";
        blockHashValuee += b.getPreviousBlockHash();
        blockHashValuee += b.getMerkleTreeRoot();
        blockHashValuee += b.getTimestamp();
        blockHashValuee += b.getNonce();
        SHA256 hash2 = new SHA256();
        blockHashValuee = hash2.generateHash(blockHashValuee);

        //boolean valid = checkNonce(blockHashValuee);
        boolean valid = true;
        if(valid){
            for(int i = 0 ; i < blockTransaction.size() ; i++){
                valid = validateTransaction(blockTransaction.get(i));
                if(!valid){
                    break;
                }
            }

        }
        System.out.println("middle of  validate block");
        System.out.println("is transactions valid  : " + valid);
        //System.out.println("Valid  ? : "+valid);
        if(valid){
            PeerToPeer conn = new PeerToPeer();
            conn.broadcastBlock(b,nodeNumber);
            if(pendingBlocks.size() == 0){
                ArrayList<Block> temp = new ArrayList<Block>();
                Map<String,JSONObject>  temp1= new HashMap<String,JSONObject>();
                for(int i=0; i<b.getTransactions().size();i++){
                    temp1.put(b.getTransactions().get(i).get("hash").toString(),b.getTransactions().get(i));
                }

                branches_transactions.add(temp1);
                temp.add(b);
                pendingBlocks.add(temp);
                chooseBlockToMineOnTopOfIt();
                newBlockArrived = true;
                maxLength = 1;
                maxIndex = 0;

            }
            int size = pendingBlocks.size();
            for(int i = 0 ; i < size; i++){
                int listSize = pendingBlocks.get(i).size();
                System.out.println("EL list rqm " + i + " El size bt3ha : " + listSize);
                for(int j = 0 ; j < listSize; j++){
                    Block temp = pendingBlocks.get(i).get(j);
                    String blockHashValue = "";
                    blockHashValue += temp.getPreviousBlockHash();
                    blockHashValue += temp.getMerkleTreeRoot();
                    blockHashValue += temp.getTimestamp();
                    blockHashValue += temp.getNonce();
                    SHA256 hash = new SHA256();
                    blockHashValue = hash.generateHash(blockHashValue);
                    //System.out.println(blockHashValue );
                    //System.out.println(b.getPreviousBlockHash());
                    //System.out.println(pendingBlocks.size());
                    if(blockHashValue.equals(b.getPreviousBlockHash()) && j != listSize - 1){
                        System.out.println("L2et el prev block bs feh collision");

                        // check if block has duplicates
                        boolean isDuplicateBlock = false;
                        for(int p=0;p< pendingBlocks.size();p++){
                            if(pendingBlocks.get(p).size()>j+1 && pendingBlocks.get(p).get(j+1).getBlockHash().equals(b.getBlockHash())){
                                isDuplicateBlock = true;
                                break;
                            }
                        }
                        System.out.println("IS DUPLICATE BLOCK :" +isDuplicateBlock);

                        if(!isDuplicateBlock) {

                            ArrayList<Block> collisionList = new ArrayList<Block>();
                            for (int z = 0; z <= j; z++) {
                                collisionList.add(pendingBlocks.get(i).get(z));
                            }
                            // add block transaction to this branch
                            Map<String, JSONObject> temp1 = new HashMap(branches_transactions.get(i));

                            for (int k = 0; k < b.getTransactions().size(); k++) {
                                temp1.put(b.getTransactions().get(k).get("hash").toString(), b.getTransactions().get(k));
                            }

                            branches_transactions.add(temp1);
                            collisionList.add(b);
                            if (collisionList.size() > maxLength) {
                                maxLength = collisionList.size();
                                maxIndex = pendingBlocks.size();
                            }
                            pendingBlocks.add(collisionList);
                            chooseBlockToMineOnTopOfIt();
                            newBlockArrived = true;
                        }
                        break;
                    }else if(blockHashValue.equals(b.getPreviousBlockHash()) && j == listSize - 1){
                        System.out.println("L2et el prev block w 7azwdha fl list");
                        pendingBlocks.get(i).add(b);
                        chooseBlockToMineOnTopOfIt();
                        newBlockArrived = true;

                        for(int k=0; k<b.getTransactions().size();k++){
                            branches_transactions.get(i).put(b.getTransactions().get(k).get("hash").toString(),b.getTransactions().get(k));
                        }

                        if(pendingBlocks.get(i).size() > maxLength){
                            maxLength = pendingBlocks.get(i).size();
                            maxIndex = i;
                            branchChanged = true ;
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
                SHA256 hash = new SHA256();
                safeblockHashValue = hash.generateHash(safeblockHashValue);
                // chain.add(safeBlock);
                addToBlockchain(false,safeBlock);



                for(int i = 0 ; i <  pendingBlocks.size(); i++){
                    Block temp = pendingBlocks.get(i).get(0);
                    String blockHashValue = "";
                    blockHashValue += temp.getPreviousBlockHash();
                    blockHashValue += temp.getMerkleTreeRoot();
                    blockHashValue += temp.getTimestamp();
                    blockHashValue += temp.getNonce();
                    hash = new SHA256();
                    blockHashValue = hash.generateHash(blockHashValue);
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

            System.out.println("end of if valid");
            System.out.println("Pneding block size : " + pendingBlocks.size());
        }

        System.out.println("end of validate block");
        System.out.println("PENDING BLOCKS");
        for(int i = 0; i < pendingBlocks.size() ; i++){
            for(int j = 0 ; j < pendingBlocks.get(i).size() ; j++){
                System.out.print(pendingBlocks.get(i).get(j).getNonce() + " ");
            }
            System.out.println("");
        }
        System.out.println("MAX LENGTH : " + maxLength);
        System.out.println("MAX INDEX : " + maxIndex);

    }







}
