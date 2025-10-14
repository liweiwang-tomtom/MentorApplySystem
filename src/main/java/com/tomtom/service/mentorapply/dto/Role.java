package com.tomtom.service.mentorapply.dto;

public enum Role {
    ROOT,
    ADMIN,
    MENTOR,
    MENTEE;

    public static boolean hasAdminPermission(Role role) {
        return role == ADMIN || role == ROOT;
    }
}
