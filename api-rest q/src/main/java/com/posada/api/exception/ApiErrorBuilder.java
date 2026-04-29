package com.posada.api.exception;

import com.posada.api.model.ApiError;
import java.time.OffsetDateTime;

public class ApiErrorBuilder {
    public static ApiError build(String code, String message) {
        ApiError error = new ApiError();
        error.setCode(code);
        error.setMessage(message);
        error.setTimestamp(OffsetDateTime.now());
        return error;
    }
}