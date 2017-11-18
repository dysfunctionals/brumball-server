import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;



public class HTTPServer {

    HttpServer server;

    public HTTPServer(int port) throws IOException{
        server = HttpServer.create(new InetSocketAddress(port),0);
    }

    public void registerHandler(MainHandler handler){
        server.createContext("/connect",handler);
    }

    public void start(){
        server.createContext("/", new DefaultHandler());
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();
    }

    public static abstract class MainHandler implements HttpHandler {

        public static String uri = "";

        public abstract String getResponse(HashMap<String,String> postData);


        public void handle(HttpExchange t) throws IOException {
            Logger.log("New request: " + uri, Logger.Level.DEBUG);

            Scanner sc = new Scanner(t.getRequestBody());
            String body = sc.nextLine();
            HashMap<String,String> postData = new HashMap<>();

            for(String part : body.split("&")){
                String[] split = part.split("=");
                if(split.length == 2){
                    postData.put(split[0],split[1]);
                }
            }


            byte [] response = getResponse(postData).getBytes();
            Headers responseHeaders = t.getResponseHeaders();
            responseHeaders.add("Content-Type","application/json");
            responseHeaders.add("Access-Control-Allow-Origin","*");
            t.sendResponseHeaders(200,response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    public static class DefaultHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            byte [] response = "That's an invalid uri!\n".getBytes();
            t.sendResponseHeaders(404, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }
}
