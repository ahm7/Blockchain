public class POW {

    private Block block;
    private int difficulty;

    public POW(Block b,int difficulty){
     this.block = b;
     this.difficulty = difficulty;
    }

    public boolean checkNonce(String concat){
        boolean check = true;
        SHA256 hash = new SHA256();
        String hashedHeader = hash.generateHash(concat);
        int index = 0;
        while (index < difficulty) {
            if (hashedHeader.charAt(index) != '0') {
                check = false;
                break;
            }
            index++;
        }
        return check;
    }

    public Block getProofOfWork(){
        while(true){
            String concat = "";
            concat += block.getPreviousBlockHash();
            concat += block.getMerkleTreeRoot();
            concat += block.getTimestamp();
            concat += block.getNonce();
            if(checkNonce(concat)){
                break;
            }

            int nonce = block.getNonce();
            block.setNonce(nonce + 1);
        }

       return block;
    }


}
