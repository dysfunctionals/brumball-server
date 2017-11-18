import java.io.IOException;
import java.util.HashMap;

public class Server {

    private HTTPServer http;

    public Server(int clientPort, int displayPort){
        Logger.log("Starting HTTP Server",Logger.Level.DEBUG);
        try {
            http = new HTTPServer(clientPort);
            http.registerHandler(new ConnectHandler());
            http.start();
        }catch(IOException ioe){
            Logger.log("Could not start HTTP Server",Logger.Level.FATAL);
        }

    }

    public static void main(String[] args){
        Logger.log("Starting Server", Logger.Level.INFO);
        Server server = new Server(8080,1729); // Port 8080 for dev purposes
    }

    public static class ConnectHandler extends HTTPServer.MainHandler {

        public static String uri = "/connect";

        @Override
        public String getResponse(HashMap <String,String> postData) {

            // To Add: Interface with TeamManager


            return "{\"token\":\"gfsjng9ufgnj9isfgjo9isdfj\",\"paddle\":0}\n";
        }
    }

    public static class InformHandler extends HTTPServer.MainHandler {

        public static String uri = "/inform";

        @Override
        public String getResponse(HashMap <String,String> postData) {

            // To Add: Interface with TeamManager

            return "{\"status\":\"success\"\n";
        }
    }
}
