package kr.carrot.springsecurity.app.dto.common;

import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private int statusCode;
    private T data;
    private T error;

    private CommonResponse(int statusCode, T data, T error) {
        this.statusCode = statusCode;
        this.data = data;
        this.error = error;
    }

    public static <T> CommonResponse<T> success(int statusCode, T data) {
        return new CommonResponse<>(statusCode, data, null);
    }

    public static <T> CommonResponse<T> error(int statusCode, T error) {
        return new CommonResponse<>(statusCode, null, error);
    }
}
