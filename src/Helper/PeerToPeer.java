package Helper;

import Entities.Block;
import Entities.NodePeers;
import Entities.Vote;
import Parsing.parsing;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PeerToPeer {


    public ServerSocket openConnection(int portNum) throws IOException {
        ServerSocket ss = new ServerSocket(portNum);
        return ss;
    }

    public void sendBlock(String peerIP, int portNum,Block b) throws IOException, InterruptedException {
        try{
            Socket socket = new Socket(peerIP, portNum);
            System.out.println("Connected with "+ peerIP+ "!");
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println("Sending Entities.Block to the Socket "+ peerIP + " on port " + portNum);
            objectOutputStream.writeObject(b);
            System.out.println("Connection with "+ peerIP+ " closed !");
            socket.close();
        }catch (Exception e){
            long t= System.currentTimeMillis();
            long end = t+15000;
            while(System.currentTimeMillis() < end) {
                // do something
                // pause to avoid churning
                Thread.sleep( 15 );
                System.out.println("waiting on sending block ");
            }

            sendBlock(peerIP,portNum,b);

        }

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
        System.out.println("Received Entities.transaction !");
        socket.close();
        return transaction;
    }

    public void sendVote(String peerIP, int portNum,Vote nodeVote) throws IOException {
        try{
            Socket socket = new Socket(peerIP, portNum);
            System.out.println("Connected with "+ peerIP+ "!");
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println("Sending Entities.Vote to the Socket "+ peerIP + " on port " + portNum);
            objectOutputStream.writeObject(nodeVote);
            System.out.println("Connection with "+ peerIP+ " closed !");
            socket.close();
        }catch (Exception e){
            sendVote( peerIP,portNum,nodeVote);
        }

    }

    public void sendtx(String peerIP, int portNum,JSONObject tx) throws IOException, InterruptedException {
        try{
            Socket socket = new Socket(peerIP, portNum);
            System.out.println("Connected with "+ peerIP+ "!");
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println("Sending Entities.transaction to the Socket "+ peerIP + " on port " + portNum);
            objectOutputStream.writeObject(tx);
            System.out.println("Connection with "+ peerIP+ " closed !");
            socket.close();
        }catch(Exception e){
            long t= System.currentTimeMillis();
            long end = t+15000;
            while(System.currentTimeMillis() < end) {
                // do something
                // pause to avoid churning
                Thread.sleep( 15 );
                System.out.println("waiting on sending tx");
            }
            sendtx( peerIP,portNum,tx);

        }

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

    public void broadcastVote(Vote nodeVote, int nodeNumber) throws IOException {
        parsing p = new parsing();
        ArrayList<NodePeers> peers = p.readNodePeers(nodeNumber);

        for(int i = 0 ; i < peers.size() ; i++) {
            String ip = peers.get(i).getIP();
            int port = peers.get(i).getPort();
            sendVote(ip, port, nodeVote);
        }
    }

    public void broadcastTx(JSONObject tx , int nodeNumber) throws IOException, InterruptedException {
        parsing p = new parsing();
        ArrayList<NodePeers> peers = p.readNodePeers(nodeNumber);

        for(int i = 0 ; i < peers.size() ; i++) {
            String ip = peers.get(i).getIP();
            int port = peers.get(i).getPort();
            sendtx(ip, port, tx);
        }
    }

    public void broadcastBlock(Block b, int nodeNumber) throws IOException, InterruptedException {
        parsing p = new parsing();
        ArrayList<NodePeers> peers = p.readNodePeers(nodeNumber);
        for(int i = 0 ; i < peers.size() ; i++) {
            String ip = peers.get(i).getIP();
            int port = peers.get(i).getPort();
            sendBlock(ip, port, b);
        }
    }
}