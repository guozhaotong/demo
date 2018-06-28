package com.example.guozhaotong.demo.entity;

public class ResponseEntity {
    private int code;
    private String responseMsg;
    private Object body;

    public ResponseEntity(int code, String responseMsg, Object body) {
        this.code = code;
        this.responseMsg = responseMsg;
        this.body = body;
    }

    public ResponseEntity() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "code=" + code +
                ", responseMsg='" + responseMsg + '\'' +
                ", body=" + body +
                '}';
    }
}
