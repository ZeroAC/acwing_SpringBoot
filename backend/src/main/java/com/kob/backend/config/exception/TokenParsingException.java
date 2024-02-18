package com.kob.backend.config.exception;

/**
 * @author zeroac
 */
public class TokenParsingException extends RuntimeException {
    public TokenParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
