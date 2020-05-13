import java.security.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
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
        this.transactionObject.put("signature",signature.toString());

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
}
