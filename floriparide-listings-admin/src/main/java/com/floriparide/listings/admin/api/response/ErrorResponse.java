package com.floriparide.listings.admin.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Andrei Tupitcyn
 */
public class ErrorResponse implements IResponse {

    @JsonProperty("msg")
    String message;

    @JsonProperty("")
    int code = 500;

    public ErrorResponse(Exception error) {
        message = error.toString();
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
