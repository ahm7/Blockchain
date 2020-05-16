import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MinerSender extends Thread{
    int methodType = -1;
    Object b = null;
    Miner m = null;
    public MinerSender(int methodType, Object b, Miner m){
        this.methodType = methodType;
        this.b = b;
        this.m = m;


    }
    public void run()
    {
        try{
            if(methodType == 1){
                m.validateBlock((Block) b);
            }else if(methodType == 2){


            }else if(methodType == 3){
                JSONObject tx = (JSONObject) b;
                boolean valid_trans = m.validateTransaction(tx);
                System.out.println(valid_trans);
                valid_trans = true;
                if(valid_trans){
                    boolean double_spend = false;
                    JSONArray inputs_array = new JSONArray();
                    inputs_array = (JSONArray) tx.get("inputs");
                    JSONObject inputObject = (JSONObject) inputs_array.get(0);
                    String prev_tx_hash = (String) inputObject.get("prevTxHash");
                    System.out.println(prev_tx_hash);
                    int  outputIndex = (int) inputObject.get("outputIndex");
                    System.out.println(outputIndex);
                    Map<String,JSONObject> pending_transactions = m.getPendingTrans();

                    if(pending_transactions.containsKey(prev_tx_hash)){
                        JSONArray inputs_array2 = new JSONArray();
                        inputs_array2 = (JSONArray) pending_transactions.get(prev_tx_hash).get("inputs");
                        JSONObject inputObject2 = (JSONObject) inputs_array2.get(0);
                        String prev_tx_hash2 = (String) inputObject.get("prevTxHash");
                        int  outputIndex2 = (int) inputObject.get("outputIndex");

                        if(prev_tx_hash.equals(prev_tx_hash2) && outputIndex == outputIndex2){
                            System.out.println("if cond");
                            double_spend = true;

                        }



                    }

                    if(!double_spend){
                        pending_transactions.put(prev_tx_hash,tx);
                    }
                    //pending_transactions.add(tx);
                    if(pending_transactions.size() == 2){
                        ArrayList<JSONObject> temp = new ArrayList<JSONObject>();

                        for (String key: pending_transactions.keySet()) {

                            temp.add(pending_transactions.get(key));
                        }
                        Block bb = m.buildBlock(temp);
                        PeerToPeer conn = new PeerToPeer();
                        conn.broadcastBlock(bb,2);
                        System.out.println(bb.getNonce());
                        pending_transactions.clear();
                    }
                }

            }

        }catch (Exception e){

        }

    }

}
