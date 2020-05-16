import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Map;

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
                n.validateBlock((Block) b);
            }else if(methodType == 2){


            }



        }catch (Exception e){

        }

    }

}
