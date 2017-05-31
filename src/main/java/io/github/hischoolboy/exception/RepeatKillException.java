package io.github.hischoolboy.exception;

/**
 *  Created by hischoolboy on 2017/5/20.
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
