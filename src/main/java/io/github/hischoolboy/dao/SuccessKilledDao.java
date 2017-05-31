package io.github.hischoolboy.dao;

import io.github.hischoolboy.domain.SuccessKilled;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *  Created by hischoolboy on 2017/5/21.
 */
@Mapper
public interface SuccessKilledDao {

    /**
     * 插入购买明细,可过滤重复(数据库有联合主键)
     *
     * @param seckilledId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckilledId") long seckilledId, @Param("userPhone") long userPhone);

    /**
     * 根据ID查询SuccessKilled并携带秒杀产品对象实体
     *
     * @param seckilledId
     * @param userPhone
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckilledId") long seckilledId, @Param("userPhone") long userPhone);

}
