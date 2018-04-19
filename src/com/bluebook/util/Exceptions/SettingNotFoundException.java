package com.bluebook.util.Exceptions;

public class SettingNotFoundException extends RuntimeException {

    public SettingNotFoundException(String message) {
        super(message);
    }

    public SettingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
