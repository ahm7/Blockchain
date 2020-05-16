import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.sound.midi.SysexMessage;
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
    private ArrayList<Block> blockChain = new ArrayList<Block>();

    int maxLength = 0;
    int maxIndex = 0;
    private ArrayList<ArrayList<Block>> pendingBlocks = new ArrayList<ArrayList<Block>>();

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
                        collisionList.add(b);
                        if(collisionList.size() > maxLength) {
                            maxLength = collisionList.size();
                            maxIndex = pendingBlocks.size();
                        }
                        pendingBlocks.add(collisionList);
                        break;
                    }else if(blockHashValue.equals(b.getPreviousBlockHash()) && j == listSize - 1){
                        pendingBlocks.get(i).add(b);
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
                    }else{
                        pendingBlocks.remove(i);
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
        if(IS_UTXO){
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
            }else{
                return false;            }
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

    public  void  addToBlockchain(boolean isfirst , Block block){

        blockChain.add(block);


        ArrayList<JSONObject> transactions = block.getTransactions();

        for(int i=0;i<transactions.size();i++){
            if(!isfirst){
            update_UTXO(transactions.get(i));
            }
            add_UTXO(transactions.get(i));
        }


    }


}
