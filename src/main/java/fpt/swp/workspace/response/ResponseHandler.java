package fpt.swp.WorkSpace.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> responseBuilder (String message, HttpStatus status, Object responseObj){
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        response.put("data", responseObj);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<Object> responseBuilder (String message, HttpStatus status){
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);

        return new ResponseEntity<>(response, status);
    }


}
