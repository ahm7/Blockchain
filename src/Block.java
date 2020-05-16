import netscape.javascript.JSObject;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.lang.Math;
import java.io.Serializable;
import java.sql.Timestamp;

public class Block implements Serializable{

    private Timestamp timestamp;
    private String previousBlockHash;
    private int  Nonce = 0;
    private String MerkleTreeRoot;
    private ArrayList<JSONObject> transactions = new ArrayList<JSONObject>();

    public ArrayList<JSONObject> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<JSONObject> transactions) {
        System.out.println(transactions.size());
        this.transactions = transactions;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public int getNonce() {
        return Nonce;
    }

    public void setNonce(int nonce) {
        this.Nonce = nonce;
    }

    public String getMerkleTreeRoot() {
        return MerkleTreeRoot;
    }

    public void setMerkleTreeRoot(String merkleTreeRoot) {
        this.MerkleTreeRoot = merkleTreeRoot;
    }
    
    public void generateBlockHash() {
        if(!checkLength()) return;
        int transactionsLength = transactions.size();
        System.out.println(transactionsLength);
        int depth = (int)Math.ceil(log2(transactionsLength));
        ArrayList<String> currArray = transactionsHashes();
        System.out.println(currArray.size() +" " +"testSize");
        ArrayList<String> nextArray = new ArrayList<String>();
        SHA256 hasher = new SHA256();
        if(transactionsLength%2 == 1) {
            currArray.add(currArray.get(transactionsLength-1));
            transactionsLength++;
        }
        for(int i = 0; i < depth; i++) {
            for(int j = 0; j < transactionsLength; j+=2) {
                nextArray.add(
                    hasher.generateHash(currArray.get(j) + currArray.get(j+1))
                );
            }
            currArray = nextArray;
            nextArray = new ArrayList<String>();
            transactionsLength /= 2;
        }
        MerkleTreeRoot = currArray.get(0);
    }



    private Boolean checkLength() {
       return log2(transactions.size()) % 1 == 0 || log2(transactions.size() + 1) % 1 == 0;
    }

    private double log2(int N) {
        double result = Math.log(N) / Math.log(2);
        return result;
    }

    private ArrayList<String> transactionsHashes() {
        ArrayList<String> result = new ArrayList<String>();

        for(int i = 0; i < transactions.size(); i++) {
            System.out.println("hash" + " "+transactions.get(i).get("hash"));
            result.add(transactions.get(i).get("hash").toString());
        }
        return result;
    }


}
