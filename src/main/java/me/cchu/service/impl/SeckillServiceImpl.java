package me.cchu.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.cchu.dao.SeckillDao;
import me.cchu.dao.SuccessKilledDao;
import me.cchu.domain.Seckill;
import me.cchu.domain.SuccessKilled;
import me.cchu.dto.Exposer;
import me.cchu.dto.SeckillExecution;
import me.cchu.exception.RepeatKillException;
import me.cchu.exception.SeckillCloseException;
import me.cchu.exception.SeckillException;
import me.cchu.service.SeckillService;
import me.cchu.enums.SeckillStatEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *  Created by hischoolboy on 2017/5/21.
 */
@Slf4j
@Service
@SuppressWarnings("unchecked")
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    //md5盐值字符串,用于混淆md5
    private final String slat = "asdfasd2341242@#$@#$%$%%#@$%#@%^%^";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 1000);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {

        Seckill seckill = getById(seckillId);

        if (seckill == null) {
            return new Exposer(false, seckillId);
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();

        Date nowTime = new Date();

        if (nowTime.getTime() > endTime.getTime() || nowTime.getTime() < startTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        //转化特定字符串的过程,不可逆
        return new Exposer(true, getMD5(seckillId), seckillId);
    }


    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {

        if (Objects.isNull(md5) || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException(SeckillStatEnum.DATA_REWRITE.getStateInfo());
        }

        //执行秒杀逻辑:1.减库存.2.记录购买行为
        Date nowTime = new Date();

        try {

            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);

            if (updateCount <= 0) {
                throw new SeckillCloseException(SeckillStatEnum.END.getStateInfo());
            } else {

                //记录购买行为
                int inserCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);

                if (inserCount <= 0) {
                    //重复秒杀
                    throw new RepeatKillException(SeckillStatEnum.REPEAT_KILL.getStateInfo());
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            log.error(e.getMessage());
            //所有的编译期异常转化为运行期异常,spring的声明式事务做rollback
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        if (log.isDebugEnabled()) {
            log.debug("md5:" + md5);
        }
        return md5;
    }
}
