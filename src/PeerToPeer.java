import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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

    public Block recieveblock(Socket socket) throws IOException, ClassNotFoundException {
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Block b = (Block) objectInputStream.readObject();
        System.out.println("Received block !");
        // print out the text of every message
        return b;
    }

}
