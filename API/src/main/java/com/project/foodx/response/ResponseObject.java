package com.project.foodx.response;

public class ResponseObject {
    private String error;
    private Object data;

    public ResponseObject(String error, Object data) {
        this.error=error;
        this.data=data;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
