package com.swcs.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahul on 8/8/14.
 */
public class Utils {

    public static void send404(HttpExchange httpExchange){
        String res = "Unknown Request\n\nUsage:\n" +
                "    /?query= : To get total count of word in existing corpus. A JSON of following format is returned : {count:10}\n" +
                "    /reload : To re-index all documents in corpus folder. New files can be indexed by adding them to corpus folder and firing this url.\n";

        sendTextResponse(httpExchange,404,res);
    }

    public static void sendTextResponse(HttpExchange httpExchange, int code, String res){

        try {
            httpExchange.sendResponseHeaders(code, res.length());
            Headers responseHeaders = httpExchange.getResponseHeaders();
            responseHeaders.add("Content-Type", "plain/text");
            OutputStream os = httpExchange.getResponseBody();
            os.write(res.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> generateQueryMap(String query){
        Map<String, String> qmap = null;

        if(query!=null && query.trim().length() > 0 ){
            qmap = new HashMap<String, String>();

            for(String pair : query.split("&")){
                pair = pair.trim();
                if(pair.length() ==0)
                       continue;
                String[] arg = pair.split("=");
                if(arg.length > 1)
                    qmap.put(arg[0].toLowerCase(), arg[1]);
                else
                    qmap.put(arg[0].toLowerCase(),null);
            }
        }

        return qmap;
    }


}
