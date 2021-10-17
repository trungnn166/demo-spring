package com.nnt.demo.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class Response <T>{
    private T element;
    private List<T> list;
    private int status;
    private String message;

    public Response(int status) {
        this.status = status;
    }

    public Response(int status, String message) {
        this(status);
        this.message = message;
    }

    public static Response ofNoContent() {
        return new Response<>(HttpStatus.OK.value());
    }

    public static Response ofError(int status, String message) {
        return new Response(status, message);
    }
}
