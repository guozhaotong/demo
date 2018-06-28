package com.example.guozhaotong.demo.entity;

public enum ReturnStatus {
    OPERATION_SUCCESS(10000, "操作成功"),
    ADD_DATA_SUCCESS(10001, "新增成功"),
    ADD_DATA_ERROR(20001, "新增失败"),
    QUERY_DATA_ERROR(20002, "查询失败"),
    DELETE_DATA_ERROR(20003, "查询失败");

    /**
     * 返回状态码
     */
    private int statusCode;
    /**
     * 返回状态信息
     */
    private String statusMsg;

    ReturnStatus(int statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @return the statusMsg
     */
    public String getStatusMsg() {
        return statusMsg;
    }
}
