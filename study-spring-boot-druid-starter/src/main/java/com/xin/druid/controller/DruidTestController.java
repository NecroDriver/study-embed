package com.xin.druid.controller;

import com.xin.druid.configuration.DruidAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author creator mafh 2019/11/20 10:30
 * @author updater
 * @version 1.0.0
 */
@RestController
@RequestMapping("/druidTest")
public class DruidTestController {

    /**
     * 数据源配置
     */
    @Autowired
    DruidAutoConfiguration druidAutoConfiguration;

    /**
     * 测试数据源
     *
     * @return 当前时间
     */
    @GetMapping("/dataSource")
    public String testDataSource() {
        String time = "";
        try {
            Connection connect = druidAutoConfiguration.dataSource().getConnection();
            PreparedStatement ps = connect.prepareStatement("select now() as time FROM DUAL");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                time = rs.getString("time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return time;
    }

}
