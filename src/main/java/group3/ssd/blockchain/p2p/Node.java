package group3.ssd.blockchain.p2p;

public class Node {

    String id;
    String ip;
    int port;
    int totalInteractions;
    int successfulInteractions;


    public Node(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.totalInteractions = 0;
        this.successfulInteractions = 0;
    }

    @Override
    public String toString() {
        return "{\n id: " + id + "\n ip: " + ip + "\n port: " + port + "\n}";
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            return id.equals(((Node) o).id) && ip.equals(((Node) o).ip) && port == ((Node) o).port;
        }
        return false;
    }


    public void addSuccessfulInteraction() {
        this.totalInteractions++;
        this.successfulInteractions++;
    }

    public void addUnsuccessfulInteraction() {
        this.totalInteractions++;
    }


}
