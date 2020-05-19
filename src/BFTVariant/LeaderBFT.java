package BFTVariant;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import Entities.*;
import Helper.*;

public class LeaderBFT extends NodeBFT{

    Queue<Block> blocksRecieved = new LinkedList<Block>();
    boolean done = false;
    Block currentBlock = null;

    public LeaderBFT(int portNum, int nodeNumber) {
        super(portNum, nodeNumber);
    }
    public void initializeServer(){
        LeaderBFTServer s = new LeaderBFTServer(portNum,this);
        Thread thread = new Thread(s);
        thread.start();
    }

    public void validateBlock(Block b) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InterruptedException {
        if(currentBlock == null){
            currentBlock = b;
            handleBlock(b);

        }else{
            blocksRecieved.add(b);

        }
    }

    public void handleBlock(Block b) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InterruptedException {
        System.out.println("Handle Block " + currentBlock.getPreviousBlockHash());
        if(b != null){
            BFT bft = new  BFT();
            bft.prepare(b,nodeNumber);
            // validate el block
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
                blockHashValuee += lastBlock.getPreviousBlockHash();
                blockHashValuee += lastBlock.getMerkleTreeRoot();
                blockHashValuee += lastBlock.getTimestamp();
                blockHashValuee += lastBlock.getNonce();
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

        }

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
            System.out.println("BLOCKCHAIN SIZE : " + blockChain.size());
            votesRecieved.clear();
            if(blocksRecieved.size() > 0){
                currentBlock = blocksRecieved.remove();
                handleBlock(currentBlock);
            }else{
                currentBlock = null;
            }

        }

    }


}
