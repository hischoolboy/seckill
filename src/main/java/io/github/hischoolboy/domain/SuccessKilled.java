package io.github.hischoolboy.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 *  Created by hischoolboy on 2017/5/9.
 */
@Setter
@Getter
@ToString
public class SuccessKilled {
    private Seckill seckill;

    private long seckillId;

    private long userPhone;

    private short state;

    private Date createTime;
}
