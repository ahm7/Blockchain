package Entities;

import java.security.*;
import java.util.ArrayList;
import Helper.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class transaction {

    private  JSONObject transactionObject = new JSONObject();


    public JSONObject getTransactionObject(){
        return transactionObject;
    }


    public void setHash() {
        SHA256 hasher = new SHA256();
        String hash = hasher.generateHash(this.transactionObject.toString());
        transactionObject.put("hash",hash);
    }



    public void setSignature(PrivateKey privateKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        SHA256 hasher = new SHA256();
        String hash = hasher.generateHash(this.transactionObject.get("inputs").toString()+this.transactionObject.get("outputs").toString());

        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privateKey);
        byte[] b = hash.getBytes();
        sig.update(b);
        byte[] signature = sig.sign();
        this.transactionObject.put("signature",signature);

    }



    public void setInputCounter(int inputCounter) {
        this.transactionObject.put("inputCounter",inputCounter);
    }

    public void setInputs(ArrayList<input> inputs) {
        JSONArray inputs_array = new JSONArray();
        for(int i=0;i<inputs.size();i++){

            input input = inputs.get(i);
            JSONObject inputObject = new JSONObject();
            inputObject.put("prevTxHash", input.prevTxHash);
            inputObject.put("outputIndex", input.outputIndex);
            inputs_array.add(inputObject);
        }

        this.transactionObject.put("inputs",inputs_array);
    }


    public void setOutputCounter(int outputCounter) {
        this.transactionObject.put("outputCounter",outputCounter);

    }


    private double value;
    private int index;
    private String publicKey;

    public void setOutputs(ArrayList<output> outputs) {
        JSONArray outputs_array = new JSONArray();
        for(int i=0;i<outputs.size();i++){

            output output = outputs.get(i);
            JSONObject outputObject = new JSONObject();
            outputObject.put("index", output.getIndex());
            outputObject.put("value", output.getValue());
            outputObject.put("publicKey", output.getPublicKey());
            outputs_array.add(outputObject);
        }

        this.transactionObject.put("outputs",outputs_array);


    }

    public boolean check_if_has_same_input(JSONObject transaction1 , JSONObject transaction2) {

        JSONArray inputs = (JSONArray) transaction1.get("inputs");
        String[] prev_hashes = new String[inputs.size()];
        int[] input_indexes = new int[inputs.size()];
        int i = 0;
        for (Object o : inputs) {
            JSONObject jsonLineItem = (JSONObject) o;
            String prevTxHash = jsonLineItem.get("prevTxHash").toString();
            int outputIndex = (int) jsonLineItem.get("outputIndex");

            prev_hashes[i] = prevTxHash;
            input_indexes[i] = outputIndex;
            i++;
        }
        inputs = (JSONArray) transaction2.get("inputs");

        for (Object o : inputs) {
            JSONObject jsonLineItem = (JSONObject) o;
            String prevTxHash = jsonLineItem.get("prevTxHash").toString();
            int outputIndex = (int) jsonLineItem.get("outputIndex");

            for (int j = 0; j < prev_hashes.length; j++) {

                if (prev_hashes[j].equals(prevTxHash) && input_indexes[j] == outputIndex) {
                    return true;
                }

            }
        }
        return false ;

    }

    public String getPrevHash_outputindex(JSONObject transaction){
        JSONArray inputs = (JSONArray) transaction.get("inputs");
        JSONObject input = (JSONObject) inputs.get(0);
        String prevHash  = input.get("prevTxHash").toString();
        int outpuindex = (int) input.get("outputIndex");

        return prevHash +","+outpuindex;
    }
}