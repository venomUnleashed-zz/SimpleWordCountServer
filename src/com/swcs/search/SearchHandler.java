package com.swcs.search;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.swcs.server.GenericRequestHandler;
import com.swcs.utils.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahul on 8/8/14.
 */
public class SearchHandler implements GenericRequestHandler {

    private LuceneSearchUtility luceneSearchUtility;

    @Override
    public void handleRequest(HttpExchange httpExchange, String path, Map<String, String> queryMap) {

        if(path.equalsIgnoreCase("/")){
            if(queryMap!=null && queryMap.containsKey("query")){
                String query = queryMap.get("query");
                if(query!=null && !query.trim().isEmpty())
                    getTermCount(httpExchange, query);
                else
                    Utils.send404(httpExchange);
            }
            else
                Utils.send404(httpExchange);
        }
        else if(path.equalsIgnoreCase("/reload"))
            reloadIndex(httpExchange);
        else
            Utils.send404(httpExchange);

    }

    /*
        Called when server restarts. Initiates in-memory indexing of docs in corpus folder
     */
    public boolean init(){
        luceneSearchUtility = new LuceneSearchUtility();
        try {
            return luceneSearchUtility.createIndex();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void reloadIndex(HttpExchange httpExchange){
        String res;
        try {
            if (luceneSearchUtility.createIndex())
                res = "Index Reload Successful";
            else
                res = "Index Reload Failed";

            httpExchange.sendResponseHeaders(200, res.length());

            OutputStream os = httpExchange.getResponseBody();
            os.write(res.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.sendTextResponse(httpExchange,500, "Server Error.\n\n " + e.getMessage());
        }

    }

    private void getTermCount(HttpExchange httpExchange, String query){

        try {
            Map<String, Long> res = new HashMap<String, Long>();
            res.put("count", luceneSearchUtility.getWordFrequency(query));

            Gson gson = new Gson();
            String json = gson.toJson(res);
            Headers responseHeaders = httpExchange.getResponseHeaders();
            responseHeaders.add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, json.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
        }
        catch(Exception e){
            e.printStackTrace();
            Utils.sendTextResponse(httpExchange,500, "Server Error.\n\n " + e.getMessage());
        }
    }
}
