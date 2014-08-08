package com.swcs.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by rahul on 8/8/14.
 */
public class Launcher {

    private static int DEFAULT_PORT=10000;

    public static void main(String[] args) {
        // write your code here
        try {
            int port = DEFAULT_PORT;
            System.out.println("args.length : "+args.length);
            if(args.length > 0){
                try {
                    System.out.println("args[0] : "+args[0]);
                    port = Integer.parseInt(args[0]);
                    if(port <=0)
                        port = DEFAULT_PORT;
                }
                catch (NumberFormatException e){
                    port = DEFAULT_PORT;
                }
            }

            System.out.println("Starting server on port : "+port);

            HttpServer httpsServer = HttpServer.create( new InetSocketAddress(port), 10);


            httpsServer.setExecutor(null);
            httpsServer.start();

            httpsServer.createContext("/", new Router());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
