import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;

public class BFT {

    public BFT() {

    }

    public void prePrepare(Block b,int nodenumber) throws IOException {
        // send block to all nodes in the network
        PeerToPeer conn = new PeerToPeer();
        parsing p = new parsing();
        ArrayList<NodePeers> peers = p.readNodePeers(nodenumber);
        for(int i = 0 ; i < peers.size() ; i++) {
            String ip = peers.get(i).getIP();
            int port = peers.get(i).getPort();
            conn.sendBlock(ip, port, b);
        }
    }

    public void prepare(Block b,int nodenumber) throws IOException {
        // the node recieved block calls it

        // validation of the block
        boolean valid = false;
        // forward the block to all the network
        if(valid){
            PeerToPeer conn = new PeerToPeer();
            parsing p = new parsing();
            ArrayList<NodePeers> peers = p.readNodePeers(nodenumber);
            for(int i = 0 ; i < peers.size() ; i++) {
                String ip = peers.get(i).getIP();
                int port = peers.get(i).getPort();
                conn.sendBlock(ip, port, b);
            }
        }


    }

    public void commit(Block b, ArrayList<Block> recievedBlocks,int nodeNumber){
        // compare all recieved blocks with one recieved in the prepare stage.
        
        // send commit or not according to the number of recieved blocks


    }

    public void recieveCommits(){
        // recieve commit message
        // count number of commits and don't commit
    }


}
