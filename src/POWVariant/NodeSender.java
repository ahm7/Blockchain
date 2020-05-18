package POWVariant;
import Entities.*;

public class NodeSender extends Thread{
    int methodType = -1;
    Object b = null;
    Node n = null;
    public NodeSender(int methodType, Object b, Node n){
        this.methodType = methodType;
        this.b = b;
        this.n = n;


    }
    public void run()
    {
        try{
            if(methodType == 1){
                n.lock.lock();
                n.validateBlock((Block) b);
                n.lock.unlock();
            }else if(methodType == 2){


            }



        }catch (Exception e){
            System.out.println("Ana 3mlt error ");
        }

    }

}
