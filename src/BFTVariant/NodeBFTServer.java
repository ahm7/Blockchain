package BFTVariant;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NodeBFTServer extends Thread{
    int portNum = -1;
    ServerSocket ss = null;
    NodeBFT n = null;
    public NodeBFTServer(int portNum,NodeBFT n){
        this.portNum = portNum;
        this.n = n;
    }

    public void run()
    {
        try
        {
            ss = new ServerSocket(portNum);
            while(true) {
                Socket socket = ss.accept();
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                Object b =  objectInputStream.readObject();


                if(b != null){
                    Class className = b.getClass();
                    String name = className.getName();
                    if(name.equals("Entities.Block")){
                        System.out.println("Received BLOCK !");
                        NodeBFTSender h = new NodeBFTSender(1,b,n);
                        Thread thread = new Thread(h);
                        thread.start();
                    }else if(name.equals("Entities.Vote")){
                        System.out.println("Received Entities.Vote !");
                        NodeBFTSender h = new NodeBFTSender(2,b,n);
                        Thread thread = new Thread(h);
                        thread.start();
                    }
                }
                //System.out.println(b.getMerkleTreeRoot());
                socket.close();
            }
        }
        catch (Exception e)
        {

        }
    }

}
