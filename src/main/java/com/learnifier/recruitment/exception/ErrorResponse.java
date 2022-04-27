package com.learnifier.recruitment.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private int code;
    private String message;

    public ErrorResponse(int code, String message) {
        this.timestamp = new Date();
        this.code = code;
        this.message = message;
    }
}
