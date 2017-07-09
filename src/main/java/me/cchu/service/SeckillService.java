package me.cchu.service;


import me.cchu.domain.Seckill;
import me.cchu.dto.Exposer;
import me.cchu.dto.SeckillExecution;
import me.cchu.exception.RepeatKillException;
import me.cchu.exception.SeckillCloseException;
import me.cchu.exception.SeckillException;

import java.util.List;

/**
 *  Created by hischoolboy on 2017/5/21.
 */
public interface SeckillService {


    /**
     * 查询所有秒杀记录
     *
     * @return
     */
    List<Seckill> getSeckillList();


    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);


    /**
     * 秒杀开启时输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     *
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException
            , RepeatKillException, SeckillCloseException;

}
