package com.example.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: xjc
 * @date 2018/11/20 15:36
 * @description
 **/

@Setter
@Getter
public class Response {

    private Integer status;

    private Object data;

    private String msg;

    private Response(){

    }


    public static Response ok(){
        Response result = new Response();
        result.status=200;

        return result;
    }

    public static Response ok(String msg){
        Response result = new Response();
        result.status=200;
        result.msg=msg;
        return result;
    }

    public static Response ok(String msg,Object data){
        Response result = new Response();
        result.status=200;
        result.msg=msg;
        result.data=data;
        return result;
    }

    public static Response error(){
        Response result = new Response();
        result.status=500;

        return result;
    }

    public static Response error(String msg){
        Response result = new Response();
        result.status=500;
        result.msg=msg;
        return result;
    }



}
