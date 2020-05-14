import java.security.Timestamp;
import java.util.ArrayList;
import java.lang.Math;
import java.io.Serializable;

public class Block implements Serializable{

    private Timestamp timestamp;
    private String previousBlockHash;
    private int  Nonce;
    private String MerkleTreeRoot;
    private ArrayList<transaction> transactions;

    public ArrayList<transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<transaction> transactions) {
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
    /*
    public void generateBlockHash() {
        if(!checkLength()) return;
        int transactionsLength = transactions.size();
        int depth = Math.ceil(log2(transactionsLength));
        ArrayList<String> currArray = transactionsToJson();
        ArrayList<String> nextArray = new ArrayList<String>();
        if(transactionsLength%2 == 1) {
            currArray.add(currArray.get(transactionsLength-1));
            transactionsLength++;
        }
        for(int i = 0; i < depth; i++) {
            for(int i = 0; i < transactionsLength; i+=2) {
                nextArray.add(
                    SHA256.generateHash(
                        SHA256.generateHash(currArray.get(i)) + SHA256.generateHash(currArray.get(i+1))
                    )
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

    private ArrayList<String> transactionsToJson() {
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < result.size(); i++) {
            result.add(transactions.get(i).toJson());
        }
        return result;
    }

     */
}
