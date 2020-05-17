import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class MinerServer extends Thread{
    int portNum = -1;
    ServerSocket ss = null;
    Miner n = null;
    public volatile Queue<Object> blocksRecieved = new LinkedList<>();
    public MinerServer(int portNum, Miner n){
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


                if(b != null){
                    Class className = b.getClass();
                    String name = className.getName();
                    if(name.equals("Block")){
                        System.out.println("Received BLOCK !");
                        MinerSender h = new MinerSender(1,b,n);
                        Thread thread = new Thread(h);
                        thread.start();
                    }else if(name.equals("Vote")){
                        System.out.println("Received Vote !");

                    }else{
                        //System.out.println(b);
                        System.out.println("Received Transaction !");
                        MinerSender h = new MinerSender(3,b,n);
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

    public Object getBlock(){

        if(blocksRecieved.size() != 0){
            //System.out.println("entered here");
            return blocksRecieved.remove();
        }else{
            return null;
        }
    }
}

