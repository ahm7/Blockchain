import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Server extends Thread{
    int portNum = -1;
    ServerSocket ss = null;
    Node n = null;
    public volatile Queue<Object> blocksRecieved = new LinkedList<>();
    public Server(int portNum, Node n){
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
                blocksRecieved.add(b);
                //System.out.println(blocksRecieved.size());
                System.out.println("Received Object !");

                if(b != null){
                    Class className = b.getClass();
                    String name = className.getName();
                    if(name.equals("Block")){
                        NodeSender h = new NodeSender(1,b,n);
                        Thread thread = new Thread(h);
                        thread.start();
                    }else if(name.equals("Vote")){

                    }else{
                        System.out.println(b);
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

    public Object getBlock(){

        if(blocksRecieved.size() != 0){
            //System.out.println("entered here");
            return blocksRecieved.remove();
        }else{
            return null;
        }
    }
}

