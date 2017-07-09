package me.cchu.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 *  Created by hischoolboy on 2017/5/20.
 */
@Getter
@Setter
@ToString
public class SeckillResult<T> implements Serializable {

    private static final long serialVersionUID = -3936895148526393338L;

    private boolean success;

    private T data;

    private String error;

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
