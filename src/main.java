import org.json.simple.JSONObject;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class main {

     public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, FileNotFoundException {


         //testDatasetParser();
         testCreateTransaction();
                 /*
         Node node = new Node();
         PublicKey publicKey = node.getPublicKey();
         PrivateKey privateKey = node.getPrivateKey();


         byte[] challenge = new byte[10000];
         ThreadLocalRandom.current().nextBytes(challenge);

         SHA256 test = new SHA256();
         String temp = test.generateHash(challenge.toString());
         Signature sig = Signature.getInstance("SHA256withRSA");
         sig.initSign(privateKey);
         byte[] b = temp.getBytes();
         sig.update(b);
         byte[] signature = sig.sign();
         System.out.println(signature);
// verify signature using the public key

         sig.initVerify(publicKey);
         sig.update(challenge);

         boolean keyPairMatches = sig.verify(signature);

         System.out.println(keyPairMatches);

    */



    }
    public static void testCreateTransaction() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, FileNotFoundException {
        Node node = new Node();
        PublicKey publicKey = node.getPublicKey();
        PrivateKey privateKey = node.getPrivateKey();

        transaction transaction = new transaction();

        transaction.setInputCounter(1);

        SHA256 hasher = new SHA256();
        String prev_hash = hasher.generateHash("ahmedHesham");
        input  input = new input(prev_hash,1);
        ArrayList<input> inputs  = new ArrayList<input>();
        inputs.add(input);
        transaction.setInputs(inputs);

        transaction.setOutputCounter(2);

        output  output = new output();
        output.setIndex(1);
        output.setValue(14.55);
        output.setPublicKey(publicKey);

        output  output1 = new output();
        output1.setIndex(2);
        output1.setValue(16.55);
        output1.setPublicKey(publicKey);

        ArrayList<output> outputs  = new ArrayList<output>();
        outputs.add(output);
        outputs.add(output1);
        transaction.setOutputs(outputs);

        transaction.setHash();
        transaction.setSignature(privateKey);

        JSONObject test =  transaction.getTransactionObject();


        PrintWriter pw = new PrintWriter("JSONExample.json");
        pw.write(test.toJSONString());

        pw.flush();
        pw.close();


    }


    public static void testDatasetParser(){
         parsing parse = new parsing();
         ArrayList<TransactionFromText> transactions = parse.readDataset();
         System.out.println("done");
    }

}
