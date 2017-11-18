import com.sun.net.httpserver.*;
import com.sun.xml.internal.ws.util.StringUtils;
import sun.rmi.runtime.Log;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;



public class HTTPServer {

    HttpServer server;

    public HTTPServer(int port) throws IOException{
        server = HttpServer.create(new InetSocketAddress(port),0);
        server.createContext("/connect", new ConnectHandler());
        server.createContext("/", new DefaultHandler());
        server.setExecutor(null);
        server.start();
    }

    public static class ConnectHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            Logger.log("Handling", Logger.Level.DEBUG);
            Logger.log(t.getRequestBody().available(), Logger.Level.DEBUG);
            //byte [] response = t.getRequestMethod().getBytes();
            t.sendResponseHeaders(200, t.getRequestBody().available());
            OutputStream os = t.getResponseBody();
            //os.write(response);
            Scanner sc = new Scanner(t.getRequestBody());
            while(sc.hasNext()){
                os.write(sc.nextByte());
            }
            os.close();
        }
    }

    public static class DefaultHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            byte [] response = "That's an invalid url!".getBytes();
            t.sendResponseHeaders(404, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }
}
