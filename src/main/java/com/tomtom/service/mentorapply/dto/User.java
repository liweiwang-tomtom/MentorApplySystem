package com.tomtom.service.mentorapply.dto;

public record User(
    long id,
    String name,
    String email,
    String password,
    Role role
) {
}
