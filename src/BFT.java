import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;

public class BFT {

    public BFT() {

    }

    public void prePrepare(Block b,int nodeNumber) throws IOException {
        // Miner sends block to leader
        PeerToPeer conn = new PeerToPeer();
        parsing p = new parsing();
        NodePeers nodePort = p.readPort(nodeNumber);
        String ip = nodePort.getIP();
        int port = nodePort.getPort();
        conn.sendBlock(ip, port, b);
    }

    public void prepare(Block b,int nodeNumber) throws IOException {
        // Leader sends block to all nodes in the network
        PeerToPeer conn = new PeerToPeer();
        parsing p = new parsing();
        ArrayList<NodePeers> peers = p.readNodePeers(nodeNumber);
        for(int i = 0 ; i < peers.size() ; i++) {
            String ip = peers.get(i).getIP();
            int port = peers.get(i).getPort();
            conn.sendBlock(ip, port, b);
        }
        //rest of this stage is in node class
    }

    public boolean commit(int nodeNumber, boolean[] receivedVotes){
        // Each node counts number of votes and decide whether to commit or not
        boolean commit = false;
        int trueCount = 0;
        for(int i = 0;i < receivedVotes.length;i++){
            if(receivedVotes[i]){
                trueCount++;
            }
            if(trueCount > receivedVotes.length){
                commit = true;
                break;
            }
        }
        return commit;
    }
}
