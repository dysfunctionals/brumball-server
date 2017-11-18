public class Server {

    public Server(int clientPort, int displayPort){

    }

    public static void main(String[] args){
        Logger.log("Starting Server", Logger.Level.INFO);
        Server server = new Server(80,1729);
    }
}
