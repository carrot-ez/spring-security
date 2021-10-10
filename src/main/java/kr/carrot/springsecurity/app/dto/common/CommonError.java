package kr.carrot.springsecurity.app.dto.common;

import lombok.Data;

@Data
public class CommonError {

    String className;
    String message;

    public CommonError(Exception e) {
        this.className = e.getClass().getName();
        this.message = e.getMessage();
    }
}
