package POWVariant;
import Entities.*;
import Helper.*;

import POWVariant.Miner;

public class POW {

    private Block block;
    private int difficulty;
    Miner m = null ;
    public POW(Block b, int difficulty, Miner m){
     this.block = b;
     this.difficulty = difficulty;
     this.m = m;
    }

    public boolean checkNonce(String concat){
        boolean check = true;
        SHA256 hash = new SHA256();
        String hashedHeader = hash.generateHash(concat);
        int index = 0;
        while (index < difficulty) {

            if(m.newBlockArrived){
               return false;
            }

            if (hashedHeader.charAt(index) != '0') {
                check = false;
                break;
            }
            index++;
        }
        return check;
    }

    public Block getProofOfWork(){
        long start_millis = System.currentTimeMillis() ;
        while(true){
            String concat = "";
            concat += block.getPreviousBlockHash();
            concat += block.getMerkleTreeRoot();
            concat += block.getTimestamp();
            concat += block.getNonce();
            if(checkNonce(concat)){
                break;
            }

            if(m.newBlockArrived){
                return null;
            }

            int nonce = block.getNonce();
            block.setNonce(nonce + 1);
        }
        long end_millis = System.currentTimeMillis() ;

        System.out.println("mining time : " + (end_millis - start_millis));

       return block;
    }


}
