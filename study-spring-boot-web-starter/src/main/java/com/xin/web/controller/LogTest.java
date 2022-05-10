package com.xin.web.controller;

import com.xin.web.base.Base;
import com.xin.web.vo.ResultVo;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author creator mafh 2019/11/21 17:51
 * @author updater
 * @version 1.0.0
 */
@RestController
@RequestMapping("/log")
public class LogTest extends Base {

    @GetMapping("/test")
    public ResultVo logTest(){
        logger.debug("逝者如斯夫");
        logger.warn("不舍昼夜");
        logger.error("武夫居中");
        System.out.println("阑尾");
        Assert.notNull(null, "断言异常");
        return ResultVo.successVo(null);
    }
}
