package com.neuedu.product.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {

    private  int status; //状态码 0：接口调用成功，有返回值； 非0：接口调用失败，无返回值,失败信息放到msg中
    private String msg;
    private T data;


    private ServerResponse(){}
    private ServerResponse(int status){
        this.status=status;
    }
    private ServerResponse(int status, String msg){
        this.status=status;
        this.msg=msg;
    }
    private ServerResponse(int status, String msg, T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }

    /**
     * 判断接口是否调用成功
     * */
    @JsonIgnore
    public  boolean isSuccess(){
        return this.status==0;
    }

    public static ServerResponse createServerResponseBySuccess(){

        return new ServerResponse(0);
    }
    public static <T> ServerResponse createServerResponseBySuccess(T data){

        return new ServerResponse(0,null,data);
    }


    public static ServerResponse createServerResponseByFail(int status){
        return new ServerResponse(status);
    }
    public static ServerResponse createServerResponseByFail(int status,String msg){
        return new ServerResponse(status,msg);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
