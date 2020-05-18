package Entities;

public class NodePeers {

    private int NodeTo;
    private String IP;
    private int port;

    public NodePeers() {

    }

    public int getNodeTo() {
        return NodeTo;
    }

    public void setNodeTo(int nodeTo) {
        NodeTo = nodeTo;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
