import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Node {


    // conncetion
    // list of peers \
    // public key
    // UTXO list
    // block chain
    // validate block    // don't forget to check double spend
    // construct transaction
    // file parsing

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Map<String,JSONObject> UTXO_list;
    ArrayList<PublicKey> publicKeys;

    int maxLength = 0;
    int maxIndex = 0;
    private ArrayList<ArrayList<Block>> pendingBlocks = new ArrayList<ArrayList<Block>>();

    public void  Node(){
        this.UTXO_list = new HashMap<String,JSONObject>();
        this.publicKeys = new ArrayList<>();
    }
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public Node(){
        generateKeyPair();
    }


    public void validateBlock(Block b)  throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        ArrayList<transaction> blockTransaction = b.getTransactions();
        boolean valid = true;
        for(int i = 0 ; i < blockTransaction.size() ; i++){
            valid = validateTransaction(blockTransaction.get(i).getTransactionObject());
            if(!valid){
                break;
            }
        }
        if(valid){
            if(pendingBlocks.size() == 0){
                ArrayList<Block> temp = new ArrayList<Block>();
                temp.add(b);
                pendingBlocks.add(temp);

            }

            for(int i = 0 ; i < pendingBlocks.size() ; i++){
                int listSize = pendingBlocks.get(i).size();
                for(int j = 0 ; j < listSize; j++){
                    Block temp = pendingBlocks.get(i).get(j);
                    String blockHashValue = "";
                    blockHashValue += temp.getPreviousBlockHash();
                    blockHashValue += temp.getMerkleTreeRoot();
                    blockHashValue += temp.getTimestamp();
                    blockHashValue += temp.getNonce();
                    if(blockHashValue == b.getPreviousBlockHash() && j != listSize - 1){
                        ArrayList<Block> collisionList = new ArrayList<Block>();
                        for(int z = 0 ; z <= j ; z++){
                            collisionList.add(pendingBlocks.get(i).get(z));
                        }
                        collisionList.add(b);
                        if(collisionList.size() > maxLength) {
                            maxLength = collisionList.size();
                            maxIndex = pendingBlocks.size();
                        }
                        pendingBlocks.add(collisionList);
                        break;
                    }else if(blockHashValue == b.getPreviousBlockHash() && j == listSize - 1){
                        pendingBlocks.get(i).add(b);
                        if(pendingBlocks.get(i).size() > maxLength){
                            maxLength = pendingBlocks.get(i).size();
                            maxIndex = i;
                        }
                        break;
                    }

                }
            }

            if(maxLength > 6){
                Block safeBlock = pendingBlocks.get(maxIndex).get(0);
                String safeblockHashValue = "";
                safeblockHashValue += safeBlock.getPreviousBlockHash();
                safeblockHashValue += safeBlock.getMerkleTreeRoot();
                safeblockHashValue += safeBlock.getTimestamp();
                safeblockHashValue += safeBlock.getNonce();
                // chain.add(safeBlock);
                for(int i = 0 ; i < pendingBlocks.size() ; i++){
                    Block temp = pendingBlocks.get(i).get(0);
                    String blockHashValue = "";
                    blockHashValue += temp.getPreviousBlockHash();
                    blockHashValue += temp.getMerkleTreeRoot();
                    blockHashValue += temp.getTimestamp();
                    blockHashValue += temp.getNonce();
                    if(safeblockHashValue == blockHashValue){
                        pendingBlocks.get(i).remove(0);
                    }else{
                        pendingBlocks.remove(i);
                    }
                }
                maxLength = maxLength - 1;
            }

        }

    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

            KeyPair keyPair = keyGen.generateKeyPair();
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();

        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    public  void add_UTXO(JSONObject transaction){

        String hash = transaction.get("hash").toString();
        int outputCounter = (int)transaction.get("outputCounter");
        ArrayList<Integer> unspend_Outputs = new ArrayList<Integer>();
        unspend_Outputs.add(outputCounter); // decreases with each use to this transction when reach 0 remove from the list
        for(int i=0;i<outputCounter;i++){
            unspend_Outputs.add(1); // if 1 that mean this output index unspend else 0
        }
        transaction.put("used_outputs",unspend_Outputs);
        UTXO_list.put(hash,transaction);
    }


    public boolean validateTransaction(JSONObject transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

       boolean singnatur_is_right = validateSignature(transaction);
       boolean IS_UTXO = isUnSpend(transaction);
       boolean value_is_valid = validValue(transaction);

     return  singnatur_is_right & IS_UTXO ;
    }

    private boolean validValue(JSONObject transaction) {
        return false;
    }

    private boolean isUnSpend(JSONObject transaction) {
        return false;
    }

    private boolean validateSignature(JSONObject transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        JSONArray inputs  = (JSONArray) transaction.get("inputs");
        int inputCounter  = (int)transaction.get("inputCounter");
        for (Object o : inputs) {
            JSONObject jsonLineItem = (JSONObject) o;
            String prevTxHash = jsonLineItem.get("prevTxHash").toString();
            int  outputIndex = (int)jsonLineItem.get("outputIndex");
            JSONArray prevTxOutputs  = (JSONArray) UTXO_list.get(prevTxHash).get("outputs");

            JSONObject output =(JSONObject) prevTxOutputs.get(outputIndex);
            PublicKey publicKey = (PublicKey) output.get("publicKey");

            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            SHA256 hasher = new SHA256();
            String temp = hasher.generateHash(transaction.get("inputs").toString()+transaction.get("outputs").toString());
            sig.update(temp.getBytes());

            boolean keyPairMatches = sig.verify(transaction.get("signature").toString().getBytes());
            return  keyPairMatches ;


        }

return false ;

    }


}
