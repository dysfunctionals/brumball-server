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
        Logger.log("Registered Handler for " + handler.uri, Logger.Level.INFO);
        server.createContext(handler.getUri(),handler);
    }

    public void start(){
        Logger.log("Now Accepting Requests", Logger.Level.INFO);
        server.createContext("/", new DefaultHandler());
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();
    }

    public static abstract class MainHandler implements HttpHandler {

        protected static String uri = "";

        public abstract String getResponse(HashMap<String,String> postData);


        public void handle(HttpExchange t) throws IOException {
            HashMap<String,String> postData = new HashMap<>();

            Headers requestHeaders = t.getRequestHeaders();
            if(requestHeaders.containsKey("Content-length")){
                int bodyLength = Integer.parseInt(requestHeaders.get("Content-length").get(0));


                if(bodyLength > 0){
                    BufferedInputStream bis = new BufferedInputStream(t.getRequestBody());
                    byte[] data = new byte[bodyLength];
                    bis.read(data);
                    String body = new String(data);

                    for(String part : body.split("&")){
                        String[] split = part.split("=");
                        if(split.length == 2){
                            postData.put(split[0],split[1]);
                        }
                    }
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

        public String getUri(){
            return uri;
        }
    }

    public static class DefaultHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            byte [] response = "{\"status\":\"fail\"}\n".getBytes();
            t.sendResponseHeaders(404, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }
}
