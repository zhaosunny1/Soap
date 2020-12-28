package com.zyy.soap.interfaces;

import java.io.IOException;

/**
 * SoapCall
 * Created by zhaoyang on 2016/12/17.
 */

public interface ISoapCall<T> {

    void init(ISoapRequest request, ISoapResponse response);



    /**
     * Synchronously send the request and return its response.
     *
     * @throws IOException      if a problem occurred talking to the server.
     * @throws RuntimeException (and subclasses) if an unexpected error occurs creating the request
     *                          or decoding the response.
     */
    ISoapResponse<T> execute() throws IOException;

    /**
     * Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred talking to the server, creating the request, or processing the response.
     */
    void enqueue(Callback<T> callback);

}
