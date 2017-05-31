package io.github.hischoolboy.exception;

/**
 *  Created by hischoolboy on 2017/5/20.
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
