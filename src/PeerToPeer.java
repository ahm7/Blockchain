import org.json.simple.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PeerToPeer {


    public ServerSocket openConnection(int portNum) throws IOException {
        ServerSocket ss = new ServerSocket(portNum);
        return ss;
    }

    public void sendBlock(String peerIP, int portNum,Block b) throws IOException {
        Socket socket = new Socket(peerIP, portNum);
        System.out.println("Connected with "+ peerIP+ "!");
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        System.out.println("Sending Block to the Socket "+ peerIP + " on port " + b);
        objectOutputStream.writeObject(b);
        System.out.println("Connection with "+ peerIP+ " closed !");
        socket.close();
    }

    public Block receiveBlock(ServerSocket s) throws IOException, ClassNotFoundException {
        Socket socket = s.accept();
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Block b = (Block) objectInputStream.readObject();
        System.out.println("Received block !");
        socket.close();
        return b;
    }

    public JSONObject receiveTransaction(ServerSocket s) throws IOException, ClassNotFoundException {
        Socket socket = s.accept();
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        JSONObject transaction = (JSONObject) objectInputStream.readObject();
        System.out.println("Received transaction !");
        socket.close();
        return transaction;
    }

    public void sendVote(String peerIP, int portNum,Vote nodeVote) throws IOException {
        Socket socket = new Socket(peerIP, portNum);
        System.out.println("Connected with "+ peerIP+ "!");
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        System.out.println("Sending Vote to the Socket "+ peerIP + " on port " + portNum);
        objectOutputStream.writeObject(nodeVote);
        System.out.println("Connection with "+ peerIP+ " closed !");
        socket.close();
    }

    public void sendtx(String peerIP, int portNum,JSONObject tx) throws IOException {
        Socket socket = new Socket(peerIP, portNum);
        System.out.println("Connected with "+ peerIP+ "!");
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        System.out.println("Sending transaction to the Socket "+ peerIP + " on port " + portNum);
        objectOutputStream.writeObject(tx);
        System.out.println("Connection with "+ peerIP+ " closed !");
        socket.close();
    }

    public Vote receiveVote(ServerSocket s) throws IOException, ClassNotFoundException {
        Socket socket = s.accept();
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Vote nodeVote = (Vote) objectInputStream.readObject();
        System.out.println("Received vote !");
        socket.close();
        return nodeVote;
    }

    public void broadcastVote(boolean vote, int nodeNumber) throws IOException {
        parsing p = new parsing();
        ArrayList<NodePeers> peers = p.readNodePeers(nodeNumber);
        Vote nodeVote = new Vote(vote);
        for(int i = 0 ; i < peers.size() ; i++) {
            String ip = peers.get(i).getIP();
            int port = peers.get(i).getPort();
            sendVote(ip, port, nodeVote);
        }
    }

    public void broadcastTx(JSONObject tx , int nodeNumber) throws IOException {
        parsing p = new parsing();
        ArrayList<NodePeers> peers = p.readNodePeers(nodeNumber);

        for(int i = 0 ; i < peers.size() ; i++) {
            String ip = peers.get(i).getIP();
            int port = peers.get(i).getPort();
            sendtx(ip, port, tx);
        }
    }

    public void broadcastBlock(Block b, int nodeNumber) throws IOException {
        parsing p = new parsing();
        ArrayList<NodePeers> peers = p.readNodePeers(nodeNumber);
        for(int i = 0 ; i < peers.size() ; i++) {
            String ip = peers.get(i).getIP();
            int port = peers.get(i).getPort();
            sendBlock(ip, port, b);
        }
    }
}
