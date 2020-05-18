package BFTVariant;
import Entities.*;


public class NodeBFTSender extends Thread{
    int methodType = -1;
    Object b = null;
    NodeBFT n = null;

    public NodeBFTSender(int methodType, Object b, NodeBFT n){
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
                n.lock.lock();
                n.addToVotes((Vote) b);
                n.lock.unlock();
            }

        }catch (Exception e){
            System.out.println("Ana 3mlt error ");
        }

    }
}
