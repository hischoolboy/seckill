package io.github.hischoolboy.service;


import io.github.hischoolboy.domain.Seckill;
import io.github.hischoolboy.dto.Exposer;
import io.github.hischoolboy.dto.SeckillExecution;
import io.github.hischoolboy.exception.RepeatKillException;
import io.github.hischoolboy.exception.SeckillCloseException;
import io.github.hischoolboy.exception.SeckillException;

import java.util.List;

/**
 * Created by hischoolboy on 2017/5/21.
 * 业务接口:站在"使用者"的角度设计接口
 * 1.方法的定义的粒度.2.参数.3.返回类型(return /异常)
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
