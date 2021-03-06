package POWVariant;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entities.*;
import Helper.*;


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
                m.lock2.lock();
                m.validateBlock((Block) b);
                int temp_branch_num = m.choosed_branch;
                int temp2 = m.chooseBlockToMineOnTopOfIt();
                if(temp_branch_num != temp2){
                    m.branchChanged = true;
                }
                m.lock2.unlock();


            }else if(methodType == 2){


            }else if(methodType == 3){
                m.lock.lock();

                System.out.println("enter lock on transaction");
                recieveTransactionHandling(true);

                m.lock.unlock();

            }
        } catch (NoSuchAlgorithmException e) {

        } catch (SignatureException e) {

        } catch (InvalidKeyException | IOException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }

    private void recieveTransactionHandling(boolean isnewTransaction) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InterruptedException, InvalidKeySpecException {


        if(m.branchChanged){
            System.out.println("is branch changed :"+ m.branches_transactions.size());
            //m.pending_transactions.clear();

            m.all_valid_transactions.clear();
            m.all_invalid_prevtransactions.clear();
            m.all_valid_transactions = new HashMap(m.branches_transactions.get(m.choosed_branch));
            transaction t = new transaction();
            List<String> keys = new ArrayList<String>(m.all_valid_transactions.keySet());
            for(int p=0;p<m.all_valid_transactions.size();p++){
                String prevHash_outputindex = t.getPrevHash_outputindex(m.all_valid_transactions.get(keys.get(p)));
                String prevhash = prevHash_outputindex.split(",")[0];
                String outputindex = prevHash_outputindex.split(",")[1];
                m.all_invalid_prevtransactions.put(prevhash+outputindex,Integer.parseInt(outputindex));
            }



            m.branchChanged = false;
        }
        // if it's not a new transaction filter pending list
        if(!isnewTransaction) {
            System.out.println("FILTRING PENDING LIST");
            System.out.println("PEnding size : " + m.pending_transactions.size());
            List<String> keys = new ArrayList<String>(m.pending_transactions.keySet());
            for (int i = 0; i < m.pending_transactions.size(); i++) {
                if (m.all_valid_transactions.containsKey(keys.get(i))) {
                    System.out.println("Deleting transaction number " + i + " KEy " + keys.get(i));

                    m.pending_transactions.remove(keys.get(i));
                }
            }
            System.out.println("PEnding size : " + m.pending_transactions.size());

        }
        JSONObject tx = (JSONObject) b;
        System.out.println("BLOCK CHAIN SIZE : " + m.blockChain.size());
        boolean valid_trans ;
        if(isnewTransaction){
            valid_trans= m.validateTransaction(tx);}
        else {
            valid_trans = true;}


        if(valid_trans){
            if(isnewTransaction) {
                boolean double_spend = false;
                JSONArray inputs_array = new JSONArray();
                String hash = tx.get("hash").toString();
                inputs_array = (JSONArray) tx.get("inputs");
                JSONObject inputObject = (JSONObject) inputs_array.get(0);
                String prev_tx_hash = (String) inputObject.get("prevTxHash");
                int outputIndex = (int) inputObject.get("outputIndex");
                String string_outputIndex = "" + outputIndex;

                // check if prev transaction used but not in main blockchain
                if (m.all_invalid_prevtransactions.containsKey(prev_tx_hash + string_outputIndex)) {

                    if (m.all_invalid_prevtransactions.get(prev_tx_hash + string_outputIndex) == outputIndex) {
                        double_spend = true;

                    }
                    //System.out.println("Exists in INVALID TRANSACTIONS !!!!!!!!!");


                }
                System.out.println("is double spend : " + double_spend);

                // check if it's a duplicate transaction
                if (m.all_valid_transactions.containsKey(hash)) {
                    double_spend = true;
                }


                if (!double_spend) {
                    //System.out.println("m.branches_transactions.size() : " + m.branches_transactions.size() );
                    if (m.branches_transactions.size() > 0 && m.branches_transactions.get(m.choosed_branch).containsKey(hash)) {

                        System.out.println("already exist transaction");
                    } else {
                        m.pending_transactions.put(hash, tx);
                    }
                    m.all_valid_transactions.put(hash, tx);
                    m.all_invalid_prevtransactions.put(prev_tx_hash + string_outputIndex, outputIndex);
                    //System.out.println("Hash " + hash);
                    //System.out.println("prev tx + "  + prev_tx_hash + string_outputIndex);
                }

                System.out.println("pending transaction size : " + m.pending_transactions.size());
            }else {
                System.out.println("KDA D5L YSHOF EL TRANSACTIONS EL ATB2T");
                System.out.println("pending list size : " + m.pending_transactions.size());
            }
            //pending_transactions.add(tx);
            if(m.pending_transactions.size() == m.blockSize){
                //System.out.println(tx);
                System.out.println("Ana 3mlt block w 7b3to 5ly balek ");

                ArrayList<JSONObject> temp = new ArrayList<JSONObject>();

                for (String key: m.pending_transactions.keySet()) {

                    temp.add(m.pending_transactions.get(key));
                }
                Block bb = m.buildBlock(temp);
                if(bb != null) {
                    System.out.println("AFTER BUILD BLOCK it's hash  : " + bb.getBlockHash());
                }
                System.out.println("IS NEW BLOCK ARRIVED: " + m.newBlockArrived);
                if(m.newBlockArrived && bb == null){
                    System.out.println("ANA D5LT fl condition l 8lat");
                    m.newBlockArrived = false;
                    m.branchChanged = true;
                    recieveTransactionHandling(false);

                }else {
                    m.lock2.lock();
                    m.maxLength = 0;
                    for(int u=0; u<m.pendingBlocks.size();u++){
                        if(m.pendingBlocks.get(u).size()>m.maxLength);
                        m.maxLength  = m.pendingBlocks.size() ;
                        m.maxIndex = u;
                    }
                    if(m.pendingBlocks.size()>0){
                        m.pendingBlocks.get(m.maxIndex).add(bb);
                        PeerToPeer conn = new PeerToPeer();
                        conn.broadcastBlock(bb,m.nodeNumber);
                        System.out.println("7b3aaaaat " + bb);
                        m.maxLength ++;
                        for(int k=0; k<bb.getTransactions().size();k++){
                            m.branches_transactions.get(m.maxIndex).put(bb.getTransactions().get(k).get("hash").toString(),bb.getTransactions().get(k));
                        }
                        m.maxLength = 0;
                        for(int o=0;o<m.pendingBlocks.size();o++){
                            if(m.pendingBlocks.get(o).size()>m.maxLength){
                                m.maxLength  = m.pendingBlocks.get(o).size();
                                m.maxIndex = o;
                            }
                        }

                        if(m.maxLength > m.uncertainity_block_num){
                            Block safeBlock = m.pendingBlocks.get(m.maxIndex).get(0);
                            String safeblockHashValue = "";
                            safeblockHashValue += safeBlock.getPreviousBlockHash();
                            safeblockHashValue += safeBlock.getMerkleTreeRoot();
                            safeblockHashValue += safeBlock.getTimestamp();
                            safeblockHashValue += safeBlock.getNonce();
                            SHA256 hash = new SHA256();
                            safeblockHashValue = hash.generateHash(safeblockHashValue);
                            // chain.add(safeBlock);
                            m.addToBlockchain(false,safeBlock);



                            for(int i = 0 ; i <  m.pendingBlocks.size(); i++){
                                Block temp1 = m.pendingBlocks.get(i).get(0);
                                String blockHashValue = temp1.getBlockHash();
                                if(safeblockHashValue.equals(blockHashValue)){
                                    m.pendingBlocks.get(i).remove(0);


                                    for(int u=0;u<safeBlock.getTransactions().size();u++){
                                        m.branches_transactions.get(i).remove(safeBlock.getTransactions().get(u).get("hash"));
                                       System.out.println("DELETE 3ADY W BEST NONCE IS :" + safeBlock.getNonce() );
                                    }
                                }else{
                                    m.pendingBlocks.remove(i);
                                    m.branches_transactions.remove(i);
                                    System.out.println("best Nonce : " + safeBlock.getNonce());
                                    i--;
                                }
                            }
                            m.maxLength = 0;
                            for(int  i=0;i<m.pendingBlocks.size();i++){
                                if(m.pendingBlocks.get(i).size()>m.maxLength){
                                    m.maxIndex = i;
                                    m.maxLength = m.pendingBlocks.get(i).size();
                                }
                            }


                        }


                    }else {

                        ArrayList<Block> temp4 = new ArrayList<Block>();
                        temp4.add(bb);
                        m.pendingBlocks.add(temp4);
                        m.maxLength = 1;

                        Map<String,JSONObject> temp1= new HashMap<String,JSONObject>();
                        for(int i=0; i<bb.getTransactions().size();i++){
                            temp1.put(bb.getTransactions().get(i).get("hash").toString(),bb.getTransactions().get(i));
                        }

                        m.branches_transactions.add(temp1);
                        PeerToPeer conn = new PeerToPeer();
                        conn.broadcastBlock(bb,m.nodeNumber);
                        System.out.println(" olt 7b3at " + bb);
                        System.out.println(" pending size "+m.pending_transactions.size());
                        System.out.println("all valid size " + m.all_valid_transactions.size());
                        System.out.println("all invalid size " + m.all_invalid_prevtransactions.size());
                        System.out.println("Nonce " + bb.getNonce());


                    }
                    m.chooseBlockToMineOnTopOfIt();
                    m.lock2.unlock();



                    for(int i = 0; i < m.pendingBlocks.size() ; i++){
                        for(int j = 0 ; j < m.pendingBlocks.get(i).size() ; j++){
                            System.out.print(m.pendingBlocks.get(i).get(j).getNonce() + " ");
                        }
                        System.out.println("");
                    }


                    m.pending_transactions.clear();

                    //recieveTransactionHandling(false);
                }
                //m.validateBlock(bb);


            }
        }
    }



}