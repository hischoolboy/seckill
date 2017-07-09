
package me.cchu.domain;

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
public class Seckill {

    private long seckillId;

    private String name;

    private int number;

    private Date startTime;

    private Date endTime;

    private Date createTime;
}
