import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Server {

    private HTTPServer http;
    public static TeamManager teamManager;

    public Server(int clientPort){
        teamManager = new TeamManager();
        Logger.log("Starting HTTP Server",Logger.Level.DEBUG);
        try {
            http = new HTTPServer(clientPort);
            http.registerHandler(new ConnectHandler());
            http.registerHandler(new InformHandler());
            http.registerHandler(new AliveHandler());
            http.registerHandler(new PaddleHandler());
            http.start();
        }catch(IOException ioe){
            Logger.log("IOException",Logger.Level.FATAL);
        }

    }

    public static void main(String[] args){
        Logger.log("Starting Brumball", Logger.Level.INFO);
        Server server = new Server(8080); // Port 8080 for dev purposes
    }

    public static class ConnectHandler extends HTTPServer.MainHandler {

        public ConnectHandler(){
            uri = "/connect";
        }

        @Override
        public String getResponse(HashMap <String,String> postData) {

            // To Add: Interface with TeamManager
            User user = Server.teamManager.registerUser();
            Logger.log("User Connected: " + user.getToken() + " to Team " + user.getTeam(), Logger.Level.INFO);
            return "{\"status\":\"success\",\"token\":\"" + user.getToken() + "\",\"paddle\":" + user.getTeam() + "}\n";
        }
    }

    public static class InformHandler extends HTTPServer.MainHandler {

        public InformHandler(){
            uri = "/inform";
        }

        @Override
        public String getResponse(HashMap <String,String> postData) {

            if(!postData.containsKey("token")) return "{\"status\":\"fail\",\"message\":\"Token missing\"}\n";

            User user = Server.teamManager.getUser(postData.get("token"));

            if(user == null) return "{\"status\":\"fail\",\"message\":\"Invalid Token\"}\n";

            if(!postData.containsKey("motion")) return "{\"status\":\"fail\"\"message\":\"Motion missing\"}\n";

            int motion = 0;

            try{
                motion = Integer.parseInt(postData.get("motion"));
            }catch (Exception e) {
                return "{\"status\":\"fail\",\"message\":\"Invalid Motion value\"}\n";
            }

            user.setVote(motion);

            return "{\"status\":\"success\"}\n";
        }
    }

    public static class AliveHandler extends HTTPServer.MainHandler {

        public AliveHandler(){
            uri = "/alive";
        }

        @Override
        public String getResponse(HashMap <String,String> postData) {

            if(!postData.containsKey("token")) return "{\"status\":\"fail\",\"message\":\"Token missing\"}\n";

            User user = Server.teamManager.getUser(postData.get("token"));

            if(user == null) return "{\"status\":\"fail\",\"message\":\"Invalid Token\"}\n";

            user.modify();

            return "{\"status\":\"success\"}\n";
        }
    }

    public static class PaddleHandler extends HTTPServer.MainHandler {

        public PaddleHandler(){
            uri = "/paddles";
        }

        @Override
        public String getResponse(HashMap <String,String> postData) {

            List<Double> teamvalues = Server.teamManager.teamList();

            String data = "";

            for(double value : teamvalues){
                data += Double.toString(value) + ",";
            }

            return "{\"status\":\"success\",\"data\":{" + data + "}\n";
        }
    }
}
