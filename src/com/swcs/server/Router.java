package com.swcs.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.swcs.search.SearchHandler;
import com.swcs.utils.Utils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by rahul on 8/8/14.
 */

public class Router implements HttpHandler{

    private HashMap<String, GenericRequestHandler> handlerMap = new HashMap<String, GenericRequestHandler>();

    public Router(){
        SearchHandler searchHandler = new SearchHandler();
        handlerMap.put("/", searchHandler);
        handlerMap.put("/reload", searchHandler);

        System.out.println("Starting in-memory indexing of documents");
        if(searchHandler.init())
            System.out.println("Indexing process finished successfully");
        else
            System.out.println("Indexing process failed");
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();

        GenericRequestHandler requestHandler = handlerMap.get(path);

        if(requestHandler == null){
            Utils.send404(httpExchange);
            return;
        }

        String query = httpExchange.getRequestURI().getQuery();


        System.out.println("path: "+path);
        System.out.println("query: "+query);

        requestHandler.handleRequest(httpExchange, path, Utils.generateQueryMap(query));

    }
}
