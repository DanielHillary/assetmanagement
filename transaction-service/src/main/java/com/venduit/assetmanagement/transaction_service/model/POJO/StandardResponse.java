package com.venduit.assetmanagement.transaction_service.model.POJO;



import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class StandardResponse {

    private boolean status;
    private String statuscode;
    private HttpStatus httpStatus;
    private String message;
    private Object data;

    public StandardResponse(){
        super();
    }

    public StandardResponse(boolean isStatus , String message, Object data, String statuscode){

        this.status = isStatus;
        this.message = message;
        this.data = data;
        this.statuscode = statuscode;
    }

    public StandardResponse(boolean isStatus , String message, Object data){

        this.status = isStatus;
        this.message = message;
        this.data = data;
    }

    public StandardResponse(boolean isStatus , String message){

        this.status = isStatus;
        this.message = message;
    }

    public StandardResponse sendResponse(boolean status, String message, Object data, String statuscode){
        return new StandardResponse(status, message, data, statuscode);
    }

    public static ResponseEntity<StandardResponse> sendHttpResponse(boolean status, String message, Object data, HttpStatus httpStatus, String statuscode){
        StandardResponse sr =  new StandardResponse(status, message, data, statuscode);

        return new ResponseEntity<>(sr, httpStatus);
    }

    public static ResponseEntity<StandardResponse> sendHttpResponse(boolean status, String message, Object data, HttpStatus httpStatus){
        StandardResponse sr =  new StandardResponse(status, message, data);

        return new ResponseEntity<>(sr, httpStatus);
    }

    public static ResponseEntity<StandardResponse> sendHttpResponse(boolean status, String message, HttpStatus httpStatus){
        StandardResponse sr =  new StandardResponse(status, message);

        return new ResponseEntity<>(sr, httpStatus);
    }

    public static ResponseEntity<StandardResponse> sendFailedHttpResponse(boolean status, String message){
        StandardResponse sr =  new StandardResponse(status, message);

        return new ResponseEntity<>(sr, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
