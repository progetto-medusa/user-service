package com.progettomedusa.user_service.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMsg {

    USRSRV01("USRSRV01", "Invalid fields:", HttpStatus.BAD_REQUEST),

    USRSRV02("USRSRV02", "Bad request", HttpStatus.BAD_REQUEST),

    USRSRV03("USRSRV03", "Bad request", HttpStatus.BAD_REQUEST),

    USRSRV04("USRSRV04", "Bad request", HttpStatus.BAD_REQUEST),

    USRSRV05("USRSRV05", "Bad request", HttpStatus.BAD_REQUEST),

    USRSRV06("USRSRV06", "Bad request", HttpStatus.BAD_REQUEST),

    USRSRV07("USRSRV07", "Activity details not found", HttpStatus.BAD_REQUEST),

    USRSRV08("USRSRV08", "Executions details not found", HttpStatus.BAD_REQUEST),

    USRSRV09("USRSRV09", "Error from DIM SDK", HttpStatus.BAD_REQUEST),

    USRSRV10("USRSRV10", "Error from Decoding Algorithm", HttpStatus.BAD_REQUEST),

    USRSRV11("USRSRV11", "Activity already closed", HttpStatus.BAD_REQUEST),

    USRSRV12("USRSRV12", "Decryption error", HttpStatus.BAD_REQUEST),

    USRSRV13("USRSRV13", "Missing mandatory parameters", HttpStatus.BAD_REQUEST),

    USRSRV14("USRSRV14", "Wrong password provided", HttpStatus.INTERNAL_SERVER_ERROR),

    USRSRV15("USRSRV15", "User not found in the system", HttpStatus.NOT_FOUND),

    USRSRV16("USRSRV16", "User not enabled in the system", HttpStatus.UNAUTHORIZED),

    USRSRV17("USRSRV17", "email non valida", HttpStatus.UNAUTHORIZED),

    USRSRV69("USRSRV69", "Bad Request: missing parameters", HttpStatus.BAD_REQUEST),

    USRSRV99("USRSRV99", "Generic error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;

    private final String message;

    private final HttpStatus httpStatus;

    ErrorMsg(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

