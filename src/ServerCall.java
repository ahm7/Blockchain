import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;

public class ServerCall implements Callable {
    /**
     * @return Boolean.TRUE is server is accepting requests
     * Boolean.FALSE otherwise
     */
    int portNum = -1;
    ServerSocket ss = null;
    public ServerCall(int portNum){
        this.portNum = portNum;
    }

    public Object call() throws Exception {
        return null;
    }
}