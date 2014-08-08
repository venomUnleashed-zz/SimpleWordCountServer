package com.swcs.server;


import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

/**
 * Created by rahul on 8/8/14.
 */

/*
    Interface required to be implemented by all Request Handlers for registering to Router.
 */
public interface GenericRequestHandler {

    public void handleRequest(HttpExchange httpExchange, String path, Map<String, String> queryMap);

}
