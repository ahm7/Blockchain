package BFTVariant;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class MinerBFTServer extends Thread{
    int portNum = -1;
    ServerSocket ss = null;
    MinerBFT n = null;
    public  MinerBFTServer(int portNum,MinerBFT n){
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
                        MinerBFTSender h = new MinerBFTSender(1,b,n);
                        Thread thread = new Thread(h);
                        thread.start();
                    }else if(name.equals("Entities.Vote")){
                        System.out.println("Received Entities.Vote !");
                        MinerBFTSender h = new MinerBFTSender(2,b,n);
                        Thread thread = new Thread(h);
                        thread.start();

                    }else{
                        //System.out.println(b);
                        System.out.println("Received Transaction !");
                        MinerBFTSender h = new MinerBFTSender(3,b,n);
                        Thread thread = new Thread(h);
                        thread.start();

                    }
                }
                socket.close();
            }
        }
        catch (Exception e)
        {

        }
    }
}
