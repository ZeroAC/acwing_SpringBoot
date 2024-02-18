package com.kob.backend.config.exception;

/**
 * @author zeroac
 */
public class UserNotLoggedInException extends RuntimeException {
    public UserNotLoggedInException(String message) {
        super(message);
    }
}
