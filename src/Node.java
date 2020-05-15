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
    private Map<String,JSONObject> UTXO_list = new HashMap<String,JSONObject>();
    private Map<String,JSONObject> temp_UTXO_list = new HashMap<String,JSONObject>();
    ArrayList<PublicKey> publicKeys;
    private Block tempBlock;
    private Block lastBlock;
    private boolean collisionModeOn = false;
    ArrayList<ArrayList<Block>> collisionChains = new ArrayList<ArrayList<Block>>();

    public void  Node(){
        //this.UTXO_list = new HashMap<String,JSONObject>();
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

    public void validateBlock(Block b) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // check if dublicate
        SHA256 hash = new SHA256();
        String concat = "";
        concat += b.getPreviousBlockHash();
        concat += b.getMerkleTreeRoot();
        concat += b.getTimestamp();
        concat += b.getNonce();
        String concat2 = "";
        concat2 += tempBlock.getPreviousBlockHash();
        concat2 += tempBlock.getMerkleTreeRoot();
        concat2 += tempBlock.getTimestamp();
        concat2 += tempBlock.getNonce();


        if(concat != concat2){
            // validate the transactions
            ArrayList<transaction> blockTransaction = b.getTransactions();
            boolean valid = true;
            for(int i = 0 ; i < blockTransaction.size() ; i++){
                valid = validateTransaction(blockTransaction.get(i).getTransactionObject());
                if(!valid){
                    break;
                }
            }
            if(valid){
                String concat3 = "";
                concat3 += lastBlock.getPreviousBlockHash();
                concat3 += lastBlock.getMerkleTreeRoot();
                concat3 += lastBlock.getTimestamp();
                concat3 += lastBlock.getNonce();
            //    boolean checkEndOFCollision();
                // if valid
                // temporary
                if(collisionModeOn){
                    if(concat3 == b.getPreviousBlockHash()){
                        ArrayList<Block> temp = new ArrayList<Block>();
                        temp.add(b);
                        collisionChains.add(temp);

                    }else{
                        for(int j = 0 ; j < collisionChains.size() ; j++){
                            int lastIndex = collisionChains.get(j).size();

                            Block last = collisionChains.get(j).get(lastIndex - 1);
                            String concat4 = "";
                            concat4 += last.getPreviousBlockHash();
                            concat4 += last.getMerkleTreeRoot();
                            concat4 += last.getTimestamp();
                            concat4 += last.getNonce();
                            if(concat4 == b.getPreviousBlockHash()) {
                                collisionChains.get(j).add(b);
                            }
                        }

                    }
                    boolean checkEndOFCollision = false;
                    int index = 0;
                    for(int j = 0 ; j < collisionChains.size() ; j++){
                        int size = collisionChains.get(j).size();
                        if(size == 7){
                            index = j;
                            checkEndOFCollision = true;
                        }
                    }
                    if(checkEndOFCollision){
                        ArrayList<Block> temp = collisionChains.get(index);
                        for(int z =0 ; z < temp.size() - 1 ; z++){
                            // chain.add(temp.get(z));
                        }
                        tempBlock = temp.get(temp.size() - 1);

                      collisionChains.clear();
                      collisionModeOn = false;

                    }


                } else if(concat2 == b.getPreviousBlockHash()){
                    // add temp block to the chain
                    tempBlock = b;
                }else if(concat3 == b.getPreviousBlockHash()) {
                    collisionModeOn = true;
                    ArrayList<Block> temp = new ArrayList<Block>();
                    temp.add(tempBlock);
                    collisionChains.add(temp);
                    ArrayList<Block> temp2 = new ArrayList<Block>();
                    temp2.add(b);
                    collisionChains.add(temp2);
                }
                // collision or add block to chain
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


    // used after add block to block chain
    public void update_UTXO(JSONObject transaction){
        JSONArray inputs  = (JSONArray) transaction.get("inputs");
        for (Object o : inputs) {
            JSONObject jsonLineItem = (JSONObject) o;
            String prevTxHash = jsonLineItem.get("prevTxHash").toString();
            int  outputIndex = (int)jsonLineItem.get("outputIndex");


            if(UTXO_list.containsKey(prevTxHash)){
                JSONObject used_outputs = (JSONObject) UTXO_list.get(prevTxHash).get("used_outputs");
                int usedOutputcounter = (int)used_outputs.get("usedOutputcounter");
                used_outputs.remove("usedOutputcounter");
                used_outputs.put("usedOutputcounter",usedOutputcounter-1);

                used_outputs.remove(""+outputIndex);
                used_outputs.put(""+outputIndex,0);
                if(usedOutputcounter-1 == 0){
                    UTXO_list.remove(prevTxHash);
                }



            }


        }



    }
    public  void add_UTXO(JSONObject transaction){

        String hash = transaction.get("hash").toString();
        int outputCounter = (int)transaction.get("outputCounter");
        JSONObject output_indexes = new JSONObject();
        output_indexes.put("usedOutputcounter",outputCounter);


        for(int i=0;i<outputCounter;i++){
            output_indexes.put((""+(i+1)),1); // if 1 that mean this output index unspend else 0
        }
        transaction.put("used_outputs",output_indexes);
        System.out.println(hash);
        System.out.println(transaction);
        JSONObject t = new JSONObject();
        UTXO_list.put(hash,transaction);
    }


    public boolean validateTransaction(JSONObject transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {


       boolean IS_UTXO = isUnSpend(transaction);

       boolean singnatur_is_right = false ;
        if(IS_UTXO ){
            singnatur_is_right = validateSignature(transaction);
        }
       //boolean value_is_valid = validValue(transaction);

     return  singnatur_is_right & IS_UTXO ;
    }

    private boolean validValue(JSONObject transaction) {

        return false;

    }

    private boolean isUnSpend(JSONObject transaction) {
        JSONArray inputs  = (JSONArray) transaction.get("inputs");
        int inputCounter  = (int)transaction.get("inputCounter");
        for (Object o : inputs) {
            JSONObject jsonLineItem = (JSONObject) o;
            String prevTxHash = jsonLineItem.get("prevTxHash").toString();
            int  outputIndex = (int)jsonLineItem.get("outputIndex");

            if(UTXO_list.containsKey(prevTxHash)){
                JSONObject used_outputs = (JSONObject) UTXO_list.get(prevTxHash).get("used_outputs");
                if((int)used_outputs.get(""+outputIndex)==0){
                    return false;
                }
            }


        }


        return true;
    }

    private boolean validateSignature(JSONObject transaction) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        JSONArray inputs  = (JSONArray) transaction.get("inputs");
        int inputCounter  = (int)transaction.get("inputCounter");
        for (Object o : inputs) {
            JSONObject jsonLineItem = (JSONObject) o;
            String prevTxHash = jsonLineItem.get("prevTxHash").toString();
            int  outputIndex = (int)jsonLineItem.get("outputIndex");
            JSONArray prevTxOutputs  = (JSONArray) UTXO_list.get(prevTxHash).get("outputs");

            JSONObject output =(JSONObject) prevTxOutputs.get(outputIndex-1);
            PublicKey publicKey = (PublicKey) output.get("publicKey");

            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            SHA256 hasher = new SHA256();
            String temp = hasher.generateHash(transaction.get("inputs").toString()+transaction.get("outputs").toString());
            sig.update(temp.getBytes());

            byte [] b = (byte[]) transaction.get("signature");
            boolean keyPairMatches = sig.verify(b);
            if(!keyPairMatches) {
                return keyPairMatches;
            }

        }

return true ;

    }


}
