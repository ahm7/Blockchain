import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
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
