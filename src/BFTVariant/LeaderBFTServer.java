package BFTVariant;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LeaderBFTServer extends Thread{

    int portNum = -1;
    ServerSocket ss = null;
    LeaderBFT l = null;
    public LeaderBFTServer(int portNum,LeaderBFT l){
        this.portNum = portNum;
        this.l = l;
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
                        LeaderBFTSender h = new LeaderBFTSender(1,b,l);
                        Thread thread = new Thread(h);
                        thread.start();
                    }else if(name.equals("Entities.Vote")){
                        System.out.println("Received Entities.Vote !");
                        LeaderBFTSender h = new LeaderBFTSender(2,b,l);
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
