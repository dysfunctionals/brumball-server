import java.io.IOException;

public class Server {

    private HTTPServer http;
    // private DisplayServer disp;

    public Server(int clientPort, int displayPort){
        Logger.log("Starting HTTP Server",Logger.Level.DEBUG);
        try {
            http = new HTTPServer(clientPort);
        }catch(IOException ioe){
            Logger.log("Could not start HTTP Server",Logger.Level.FATAL);
        }

    }

    public static void main(String[] args){
        Logger.log("Starting Server", Logger.Level.INFO);
        Server server = new Server(8080,1729); // Port 8080 for dev purposes
    }
}
