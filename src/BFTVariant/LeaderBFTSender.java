package BFTVariant;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import Entities.*;

public class LeaderBFTSender extends Thread{

    int methodType = -1;
    Object b = null;
    LeaderBFT l = null;

    public  LeaderBFTSender (int methodType, Object b, LeaderBFT l){
        this.methodType = methodType;
        this.b = b;
        this.l = l;
    }

    public void run() {
        try {
            if (methodType == 1) {
                l.lock.lock();
                l.validateBlock((Block) b);
                l.lock.unlock();
            } else if (methodType == 2) {
                l.lock.lock();
                l.addToVotes((Vote) b);
                l.lock.unlock();
            }

        } catch (NoSuchAlgorithmException e) {

        } catch (SignatureException e) {

        } catch (InvalidKeyException | IOException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}

