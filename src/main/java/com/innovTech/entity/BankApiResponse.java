package com.innovTech.entity;

public class BankApiResponse<T> {

	private String message;
	
    private T data;


    public BankApiResponse() {
		super();
	}
    

	public BankApiResponse(String message, T data) {
		super();
		this.message = message;
		this.data = data;
	}


	public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
