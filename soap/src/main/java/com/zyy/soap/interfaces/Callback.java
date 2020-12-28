package com.zyy.soap.interfaces;

public interface Callback<T> {
    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link ISoapCall} to determine if the response indicates success.
     */
    void onResponse(ISoapCall<T> call, ISoapResponse<T> response);

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     */
    void onFailure(ISoapCall<T> call, Throwable t);
}