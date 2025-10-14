package com.tomtom.service.mentorapply.reponse;

import org.springframework.lang.NonNull;

public record Response<T>(
        @NonNull T data,
        int code,
        String message,
        String token
) {
    public Response(@NonNull T data, int code) {
        this(data, code, null, null);
    }

    public Response(@NonNull T data, int code, @NonNull String message) {
        this(data, code, message, null);
    }
}
