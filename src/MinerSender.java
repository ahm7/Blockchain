import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
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
                int temp_branch_num = m.choosed_branch;
                int temp2 = m.chooseBlockToMineOnTopOfIt();
                if(temp_branch_num != temp2){
                    m.branchChanged = true;
                }


            }else if(methodType == 2){


            }else if(methodType == 3){
                m.lock.lock();

                if(m.branchChanged){
                    m.pending_transactions.clear();
                    m.all_valid_transactions.clear();
                    m.all_invalid_prevtransactions.clear();
                    m.all_valid_transactions = new HashMap(m.branches_transactions.get(m.choosed_branch));
                    transaction t = new transaction();
                    for(int p=0;p<m.all_valid_transactions.size();p++){
                        String prevHash_outputindex = t.getPrevHash_outputindex(m.all_valid_transactions.get(p));
                        String prevhash = prevHash_outputindex.split(",")[0];
                        String outputindex = prevHash_outputindex.split(",")[1];
                        m.all_invalid_prevtransactions.put(prevhash+outputindex,Integer.parseInt(outputindex));
                    }

                }
                JSONObject tx = (JSONObject) b;
                boolean valid_trans = m.validateTransaction(tx);
                valid_trans = true;
                if(valid_trans){
                    boolean double_spend = false;
                    JSONArray inputs_array = new JSONArray();
                    String hash = tx.get("hash").toString();
                    inputs_array = (JSONArray) tx.get("inputs");
                    JSONObject inputObject = (JSONObject) inputs_array.get(0);
                    String prev_tx_hash = (String) inputObject.get("prevTxHash");
                    int  outputIndex = (int) inputObject.get("outputIndex");
                    String string_outputIndex =""+outputIndex;

                    // check if prev transaction used but not in main blockchain
                    if(m.all_invalid_prevtransactions.containsKey(prev_tx_hash+string_outputIndex)){

                        if(m.all_invalid_prevtransactions.get(prev_tx_hash+string_outputIndex) == outputIndex){
                            double_spend = true;

                        }



                    }
                    // check if it's a duplicate transaction
                    if(m.all_valid_transactions.containsKey(hash)){
                        double_spend = true;
                    }

                    if(!double_spend){
                        System.out.println("m.branches_transactions.size() : " + m.branches_transactions.size() );
                        if(m.branches_transactions.size() > 0 && m.branches_transactions.get(m.choosed_branch).containsKey(hash)){


                        }else{
                            m.pending_transactions.put(hash,tx);
                        }
                        m.all_valid_transactions.put(hash,tx);
                        m.all_invalid_prevtransactions.put(prev_tx_hash+string_outputIndex,outputIndex);
                        System.out.println("Hash " + hash);
                        System.out.println("prev tx + "  + prev_tx_hash + string_outputIndex);
                    }
                    //pending_transactions.add(tx);
                    if(m.pending_transactions.size() == m.blockSize){
                        System.out.println(tx);

                        ArrayList<JSONObject> temp = new ArrayList<JSONObject>();

                        for (String key: m.pending_transactions.keySet()) {

                            temp.add(m.pending_transactions.get(key));
                        }
                        Block bb = m.buildBlock(temp);
                        if(m.newBlockArrived && bb == null){

                        }else {
                            if(m.pendingBlocks.size()>0){
                                m.pendingBlocks.get(m.maxIndex).add(bb);

                            }else {

                                ArrayList<Block> temp4 = new ArrayList<Block>();
                                temp4.add(bb);
                                m.pendingBlocks.add(temp4);
                                PeerToPeer conn = new PeerToPeer();
                                conn.broadcastBlock(bb,m.nodeNumber);
                                System.out.println(" pending size "+m.pending_transactions.size());
                                System.out.println("all valid size " + m.all_valid_transactions.size());
                                System.out.println("all invalid size " + m.all_invalid_prevtransactions.size());
                                System.out.println("Nonce " + bb.getNonce());

                            }
                        }
                        //m.validateBlock(bb);




                        m.pending_transactions.clear();
                    }
                }
                m.lock.unlock();

                    }
                } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException | IOException e) {
            e.printStackTrace();
        }

    }

        }


