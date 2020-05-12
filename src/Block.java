import java.security.Timestamp;
import java.util.ArrayList;

public class Block {

    private Timestamp timestamp;
    private String previousBlockHash;
    private  int  Nonce ;
    private  String MerkleTreeRoot ;
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
        Nonce = nonce;
    }

    public String getMerkleTreeRoot() {
        return MerkleTreeRoot;
    }

    public void setMerkleTreeRoot(String merkleTreeRoot) {
        MerkleTreeRoot = merkleTreeRoot;
    }
}
