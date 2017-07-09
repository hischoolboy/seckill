package me.cchu.web.controller;

import me.cchu.domain.Seckill;
import me.cchu.dto.Exposer;
import me.cchu.dto.SeckillExecution;
import me.cchu.dto.SeckillResult;
import me.cchu.enums.SeckillStatEnum;
import me.cchu.exception.RepeatKillException;
import me.cchu.exception.SeckillCloseException;
import me.cchu.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @GetMapping("/index")
    public ModelAndView index(Model model) {
        return new ModelAndView("/seckill/index");
    }

    @GetMapping("/list")
    public ModelAndView list() {
        List<Seckill> seckills = seckillService.getSeckillList();
        if(log.isDebugEnabled()){
            log.debug("查询结果: " + seckills.toString());
        }
        ModelAndView mv = new ModelAndView("/seckill/list", "seckills", seckills);
        return mv;
    }

    @GetMapping("/{seckillId}/detail")
    public ModelAndView detail(@PathVariable("seckillId") Long seckillId) {
        ModelAndView mv = new ModelAndView();
        if (seckillId == null) {
            mv.setViewName("redirect:/seckill/list");
            return mv;
        }

        Seckill seckill = seckillService.getById(seckillId);

        if (seckill == null) {
            mv.setViewName("forward:/seckill/list");
            return mv;
        }

        mv.setViewName("/seckill/detail");
        mv.addObject("seckill", seckill);
        return mv;
    }

    @PostMapping(value = "/{seckillId}/exposer", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            log.error(e.getMessage());
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }

        return result;
    }

    @PostMapping(value = "/{seckillId}/{md5}/execution", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long killPhone) {

        if (killPhone == null) {
            return new SeckillResult<SeckillExecution>(false, SeckillStatEnum.NOT_LOGIN.getStateInfo());
        }

        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, killPhone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);

        } catch (SeckillCloseException e2) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);

        } catch (Exception e) {
            log.error(e.getMessage());
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }

    }

    @GetMapping("/time/now")
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult(true, now.getTime());
    }

}
