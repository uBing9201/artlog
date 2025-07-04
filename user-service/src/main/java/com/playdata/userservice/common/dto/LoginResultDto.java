package com.playdata.userservice.common.dto;

import com.playdata.userservice.user.entity.User;

public class LoginResultDto {
    private boolean success;
    private String message;
    private User user;

    public LoginResultDto(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
