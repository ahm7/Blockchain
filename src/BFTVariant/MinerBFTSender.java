package BFTVariant;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import Entities.*;


public class MinerBFTSender extends Thread {

    int methodType = -1;
    Object b = null;
    MinerBFT m = null;

    public MinerBFTSender(int methodType, Object b, MinerBFT m) {
        this.methodType = methodType;
        this.b = b;
        this.m = m;
    }


    public void run() {
        try {
            if (methodType == 1) {
                m.lock.lock();
                m.validateBlock((Block) b);
                m.lock.unlock();
            } else if (methodType == 2) {
                m.lock.lock();
                m.addToVotes((Vote) b);
                m.lock.unlock();
            } else if (methodType == 3) {
                m.lock.lock();
                recieveTransactionHandling(true);
                m.lock.unlock();
            }

        } catch (NoSuchAlgorithmException e) {

        } catch (SignatureException e) {

        } catch (InvalidKeyException | IOException e) {

        }
    }

    private void recieveTransactionHandling(boolean isnewTransaction) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        JSONObject tx = (JSONObject) b;
        boolean valid_trans = m.validateTransaction(tx);
        System.out.println("TRANSACTION VALIDATION CHECK " + valid_trans);
        valid_trans = true;
        if (valid_trans) {
            m.pending_transactions.put(tx.get("hash").toString(), tx);
            if (m.pending_transactions.size() == m.blockSize) {
                //System.out.println(tx);
                System.out.println("Ana 3mlt block w 7b3to 5ly balek ");

                ArrayList<JSONObject> temp = new ArrayList<JSONObject>();

                for (String key : m.pending_transactions.keySet()) {

                    temp.add(m.pending_transactions.get(key));
                }
                Block bb = m.buildBlock(temp);
                System.out.println("AFTER BUILD BLOCK : " + bb);

                BFT bft = new BFT();
                bft.prePrepare(bb, m.leaderNodeNum);

                m.pending_transactions.clear();

                //m.validateBlock(bb);


            }
        }
    }
}
