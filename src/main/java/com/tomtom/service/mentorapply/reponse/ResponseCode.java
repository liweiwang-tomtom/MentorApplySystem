package com.tomtom.service.mentorapply.reponse;

public record ResponseCode(int code) {
    public static final ResponseCode SUCCESS = new ResponseCode(0);
    public static final ResponseCode NO_CONTENT = new ResponseCode(200);
    public static final ResponseCode BAD_REQUEST = new ResponseCode(400);
    public static final ResponseCode UNAUTHORIZED = new ResponseCode(401);
    public static final ResponseCode INTERNAL_ERROR = new ResponseCode(500);
}
