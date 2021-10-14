package com.nnt.demo.response;

import lombok.Data;

import java.util.List;

@Data
public class Response <T>{
    private T element;
    private List<T> list;
    private boolean status;
    public Response(boolean status) {
        this.status = status;
    }

    public static Response ofNoContent() {
        return new Response<>(true);
    }
}
