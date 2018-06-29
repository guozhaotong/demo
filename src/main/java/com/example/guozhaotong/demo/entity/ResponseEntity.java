package com.example.guozhaotong.demo.entity;

public class ResponseEntity {
    private ReturnStatus returnStatus;
    private Object body;

    public ResponseEntity() {
    }

    public ResponseEntity(ReturnStatus returnStatus, Object body) {
        this.returnStatus = returnStatus;
        this.body = body;
    }

    public ReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(ReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
